package com.somnus.pay.payment.thirdPay.cmb;

import cmb.netpayment.Security;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandler;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.cmb.config.CMBConfig;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.utils.Assert;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @description: 招行支付渠道回调处理器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 */
public abstract class AbstractCMBHandler extends PaymentChannelHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractCMBHandler.class);

	private String branchId;
	private String coNo;

	public AbstractCMBHandler(PayChannel channel, String branchId, String coNo) {
		super(channel);
		this.branchId = branchId;
		this.coNo = coNo;
	}

	@Override
	public String handleNotify(Map<String, String> data) {
		checkSign(data);
		return Constants.NOTIFY_SUCCESS_RESULT;
	}

	@Override
	public String handleReturn(Map<String, String> data) {
		checkSign(data);
		return data.get(CMBConfig.OUT_TRADE_NO);
	}

	/**
	 * 检查通知参数中的签名是否正确
	 * @param data 通知参数
	 * @return 签名是否正确
	 */
	protected void checkSign(Map<String, String> data) {
		Assert.isTrue(MapUtils.isNotEmpty(data), PayExceptionCode.PAYMENT_PARAM_ERROR);
		String succeed = data.get("Succeed"); // 取值Y(成功)或N(失败)；
		String billNo = data.get("BillNo"); // 定单号(由支付命令送来)；
		String amount = data.get("Amount"); // 实际支付金额(由支付命令送来)；
		String date = data.get("Date"); // 交易日期（我们发送的日期）；
		String merchantPara = data.get("MerchantPara");// 因为提交给招行的订单号易重复，所以我们使用该字段回传我们平台记录的请求流水号
		String msg = data.get("Msg"); // 银行通知用户的支付结果消息
		String signature = data.get("Signature"); // 加密串
		if (!StringUtils.endsWith(signature, "|")) {
			signature += "|";
		}
		// 组装待签名验证的报文
		StringBuffer sValue = new StringBuffer();
		sValue.append("Succeed=").append(succeed).append("&CoNo=").append(this.coNo).append("&BillNo=").append(billNo);
		sValue.append("&Amount=").append(amount).append("&Date=").append(date).append("&MerchantPara=");
		sValue.append(merchantPara).append("&Msg=").append(msg).append("&Signature=").append(signature);
		// 由StringBuffer类型转换成String类型
		String resMsg = sValue.toString();
		try {
			Security security = new Security(CMBConfig.PUB_KEY_FILE);
			//防止利用测试攻击，做一下订单比较
			Assert.isTrue((security.checkInfoFromBank(resMsg)
					&& (StringUtils.isNotBlank(merchantPara) && merchantPara.contains(billNo))
					&& (StringUtils.isNotBlank(msg) && msg.contains(this.branchId+this.coNo))), PayExceptionCode.SIGN_VALIDATE_FAILED);
		} catch (Exception e) {
			LOGGER.error("CMBPay handlePaymentOrderResponse error,e:", e);
			throw new PayException(PayExceptionCode.SIGN_VALIDATE_FAILED);
		}
	}

	@Override
	public String getFailedResponse(RequestParameter<?, ?> requestParameter) {
		return Constants.NOTIFY_FAIL_RESULT;
	}

	@Override
	public PaymentResult[] convert(Map<String, String> data){
		PaymentResult paymentResult = new PaymentResult();
		int isCombined = 0;
		String orderId = data.get(CMBConfig.MERCHANT_PARA);
		if (orderId.indexOf(Constants.COMBINED_ORDER_PREFIX) > -1) {
			isCombined = 1;
		} else {
			isCombined = 0;
		}
		paymentResult.setIsCombined(isCombined);
		paymentResult.setOrderId(orderId);
		String tradeStatus = data.get(CMBConfig.SUCCEED_KEY);
		paymentResult.setTradeStatus(tradeStatus);
		paymentResult.setThirdTradeNo(data.get(CMBConfig.BILL_NO));
		paymentResult.setPrice(Double.valueOf(StringUtils.defaultIfBlank(data.get(CMBConfig.AMOUNT), "0")));
		paymentResult.setBuyerId("");
		paymentResult.setBankType("");
		paymentResult.setBankBillNo("");
		paymentResult.setPayInfo(data.get(CMBConfig.RETURN_MSG_KEY));
		paymentResult.setStatus((CMBConfig.TRADE_SUCCESS.equals(tradeStatus)) ? PaymentOrderType.SCCUESS.getValue() : PaymentOrderType.FAIL.getValue());
		return new PaymentResult[]{paymentResult};
	}
	
}
