package com.somnus.pay.payment.thirdPay.bocom;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.somnus.pay.payment.util.PageCommonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandler;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.bocom.util.BocomConstants;
import com.somnus.pay.payment.thirdPay.bocom.util.ContextUtil;
import com.somnus.pay.payment.util.DateUtils;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.utils.Assert;
import com.bocom.netpay.b2cAPI.BOCOMSetting;

/**
 * @description: 交行支付渠道回调处理器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 */
@Component
public class BocomHandler extends PaymentChannelHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(BocomHandler.class);

	private static final String NOTIFY_SUCCESS_RESPONSE = "success";
	private static final String NOTIFY_FAIL_RESPONSE = "fail";
	private static final String TRADE_SUCCESS = "1";
	private static final String OUT_TRADE_NO = "ORDERNO";

	public BocomHandler() {
		super(PayChannel.BocomPay);
        ContextUtil.getInstance();
	}

	@Override
	public String handleNotify(Map<String, String> data) {
		LOGGER.info("处理交行支付后端返回通知:[{}]", data);
		data.put(OUT_TRADE_NO, parseNotifyMsg(data.get("notifyMsg")).get(1));
		checkSign(data);
		return NOTIFY_SUCCESS_RESPONSE;
	}

	@Override
	public String handleReturn(Map<String, String> data) {
		LOGGER.info("处理交行支付前端返回通知:[{}]", data);
		Map<String, String> params = this.parseParam(data);
		String orderId = parseNotifyMsg(data.get("notifyMsg")).get(1);
		params.put(OUT_TRADE_NO, orderId);
		checkSign(data);
		return orderId;
	}

	/**
	 * 检查通知参数中的签名是否正确
	 * @param data 通知参数
	 * @return 签名是否正确
	 */
	protected void checkSign(Map<String, String> data) {
		Assert.isTrue(MapUtils.isNotEmpty(data), PayExceptionCode.PAYMENT_PARAM_ERROR);
		String notifyMsg = data.get("notifyMsg"); //获取银行通知结果
		int lastIndex = notifyMsg.lastIndexOf("|");
		String signMsg = notifyMsg.substring(lastIndex + 1, notifyMsg.length()); //获取签名信息
		String srcMsg = notifyMsg.substring(0, lastIndex + 1);
		com.bocom.netpay.b2cAPI.NetSignServer nss = new com.bocom.netpay.b2cAPI.NetSignServer();
		boolean success = false;
		try {
			nss.NSDetachedVerify(signMsg.getBytes("GBK"), srcMsg.getBytes("GBK"));
			success = nss.getLastErrnum() >= 0;
		} catch (Exception e) {
			LOGGER.error("交行校验验签失败", e);
		}
		Assert.isTrue(success, PayExceptionCode.SIGN_VALIDATE_FAILED);
	}

	@Override
	public String getFailedResponse(RequestParameter<?, ?> parameter) {
		return NOTIFY_FAIL_RESPONSE;
	}

	@Override
	public PaymentResult[] convert(Map<String, String> data){
		/** 处理参数 */
		List<String> list = parseNotifyMsg(data.get("notifyMsg"));
		// 是否是合并付款
		int isCombined = 0;
		try {
			isCombined = Integer.valueOf(list.get(5) + "");
		} catch (Exception e) {
			isCombined = 0;
		}
		PaymentResult paymentResult = new PaymentResult();
		paymentResult.setIsCombined(isCombined);
		paymentResult.setOrderId(list.get(1) + "");
		paymentResult.setTradeStatus(list.get(9) + "");
		paymentResult.setThirdTradeNo(list.get(8) + "");
		paymentResult.setPrice(Double.valueOf((list.get(2) + "")));
		paymentResult.setBuyerId("");
		paymentResult.setBankType(list.get(11) + ""); //银行卡类型: 0 借记卡; 1 准贷记卡; 2 贷记卡; 3 他行银联卡
		paymentResult.setBankBillNo(list.get(8) + "");
		paymentResult.setPayInfo(list.get(9) + "");
		paymentResult.setStatus(TRADE_SUCCESS.equals(paymentResult.getTradeStatus()) ? PaymentOrderType.SCCUESS.getValue() : PaymentOrderType.FAIL.getValue());
		return new PaymentResult[]{paymentResult};
	}

	private List<String> parseNotifyMsg(String notifyMsg) {
		Assert.hasText(notifyMsg, PayExceptionCode.BOCOM_CALLBACK_PARAMETER_IS_NULL);
		String[] array = notifyMsg.split("\\|");
		List<String> list = new ArrayList<String>(array.length);
		int length = array.length - 1;
		for (int i = 0; i < length; i++) {
			list.add(array[i]);
		}
		return list;
	}
	
	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
        ContextUtil.getInstance();
        /** 获取参数 */
        String interfaceVersion = BocomConstants.INTERFACE_VERSION; //消息版本号: 固定为 1.0.0.0
        String merID = BocomConstants.MERCHANT_ID; //商户编号: 商户客户号是银行生成的15位编号

        String orderid = "";//订单号: 商户应保证定单号+定单日期的唯一性,最好支持20个字符
        String orderContent = ""; //订单内容: 商家填写的其他订单信息，在个人客户页面显示
        /** 合并支付 */
        if (parameter.getData().getIsCombined()) {
            orderid = parameter.getData().getParentOrderId();
            orderContent = parameter.getData().getParentOrderId();
        } else {
            orderid = parameter.getData().getOrderId();
            orderContent = parameter.getData().getParentOrderId();
        }
        String createTimeStr = DateUtils.Date2String(new Date(), "yyyyMMddHHmmss");
        String orderDate = createTimeStr.substring(0, 8); //商户订单日期: YyyyMMdd
        String orderTime = createTimeStr.substring(8); //商户订单时间: Hhmmss
        String tranType = "0"; //交易类别: 0-B2C
        String amount = parameter.getData().getAmount() + ""; //订单金额: 单位:元 并带两位小数 15位整数+2位小数
        String curType = "CNY"; //订单币种: 人民币 CNY

        String orderMono = StringUtils.isBlank(parameter.getData().getMemo()) ? "" : parameter.getData().getMemo(); //商家备注: 不在个人客户页面显示的备注，但可在商户管理页面上显示
        String phdFlag = "0"; //物流配送标志: 0 非物流 1 物流配送
        String notifyType = "1"; //通知方式: 1 通知
        String merURL = PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + BocomConstants.MER_URL; //主动通知URL
        String goodsURL = PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + BocomConstants.GOODS_URL; //取货URL: 为空则不显示按钮，不自动跳转
        String jumpSeconds = ""; //自动跳转时间: 等待n秒后自动跳转取货URL；若不填写则表示不自动跳转
        String payBatchNo = parameter.getData().getIsCombined() ? "1" : "0"; //商户批次号: 商家可填入自己的批次号，对账使用
        String proxyMerName = ""; //二级商户名称
        String proxyMerType = ""; //二级商户类型
        String proxyMerCredentials = ""; //二级商户号码
        String netType = "0"; //渠道编号: 固定填0:（html渠道）
        String issBankNo = parameter.getData().getDefaultBank(); //发卡行行号: 同银联标准，不进行签名，不输默认为交行

        /** 组装参数 */
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("interfaceVersion", interfaceVersion);
        paramMap.put("merID", merID);
        paramMap.put("orderid", orderid);
        paramMap.put("orderDate", orderDate);
        paramMap.put("orderTime", orderTime);
        paramMap.put("tranType", tranType);
        paramMap.put("amount", amount);
        paramMap.put("curType", curType);
        paramMap.put("orderContent", orderContent);
        paramMap.put("orderMono", orderMono);
        paramMap.put("phdFlag", phdFlag);
        paramMap.put("notifyType", notifyType);
        paramMap.put("merURL", merURL);
        paramMap.put("goodsURL", goodsURL);
        paramMap.put("jumpSeconds", jumpSeconds);
        paramMap.put("payBatchNo", payBatchNo);
        paramMap.put("proxyMerName", proxyMerName);
        paramMap.put("proxyMerType", proxyMerType);
        paramMap.put("proxyMerCredentials", proxyMerCredentials);
        paramMap.put("netType", netType);
        paramMap.put("issBankNo", issBankNo);

        /** 签名内容 */
        String sourceMsg = interfaceVersion + "|" + merID + "|" + orderid + "|" + orderDate + "|"
                           + orderTime + "|" + tranType + "|" + amount + "|" + curType + "|"
                           + orderContent + "|" + orderMono + "|" + phdFlag + "|" + notifyType
                           + "|" + merURL + "|" + goodsURL + "|" + jumpSeconds + "|" + payBatchNo
                           + "|" + proxyMerName + "|" + proxyMerType + "|" + proxyMerCredentials
                           + "|" + netType;

        /** 校验签名 */
        com.bocom.netpay.b2cAPI.NetSignServer nss = new com.bocom.netpay.b2cAPI.NetSignServer();
        String merchantDN = (String) BOCOMSetting.dnmap.get(merID);
        String signMsg = "";
        try {
            nss.NSSetPlainText(sourceMsg.getBytes("GBK"));
            byte bSignMsg[] = nss.NSDetachedSign(merchantDN);
            signMsg = new String(bSignMsg, "GBK");
            paramMap.put("merSignMsg", signMsg);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("编码出错:", e);
        }

        if (nss.getLastErrnum() < 0) {
            LOGGER.error("ERROR:商户端签名失败");
            return "";
        }
        LOGGER.info("建立请求参数：[{}]", paramMap);
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" />");
        sbHtml.append("<form id=\"bocomForm\" name=\"bocomForm\" action=\"" + BOCOMSetting.OrderURL + "\" method=\"POST\">");
        for (Entry<String, String> item : paramMap.entrySet()) {
            String name = item.getKey();
            String value = item.getValue();
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        sbHtml.append("<input type=\"submit\" value=\"submit\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['bocomForm'].submit();</script>");
        return sbHtml.toString();
	}
	
}
