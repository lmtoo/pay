package com.somnus.pay.payment.thirdPay.ccb;

import java.util.Map;
import java.util.TreeMap;

import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Msg;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandler;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.ccb.util.RSASig;
import com.somnus.pay.payment.util.FuncUtils;
import com.somnus.pay.payment.util.HTMLUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.utils.Assert;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.thirdPay.ccb.config.CCBConfig;

/**
 * @description: 建行支付渠道回调处理器
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 */
@Service
public class CCBCallbackHandler extends PaymentChannelHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(CCBCallbackHandler.class);

	public CCBCallbackHandler() {
		super(PayChannel.CCBPay);
	}

	@Override
	public String handleNotify(Map<String, String> data) {
		checkSign(data);
		return CCBConfig.NOTIFY_SUCCESS_RESULT;
	}

	@Override
	public String handleReturn(Map<String, String> data) {
		checkSign(data);
		return data.get(CCBConfig.OUT_TRADE_NO);
	}

	/**
	 * 检查通知参数中的签名是否正确
	 * @param data 通知参数
	 */
	protected void checkSign(Map<String, String> data) {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("处理建行直连支付返回的参数:[{}]", data);
		}
		boolean verify_result = false;
		String sign = data.get(CCBConfig.SIGN);
		String[] keys = {"POSID", "BRANCHID","ORDERID", "PAYMENT", "CURCODE","REMARK1", "REMARK2","ACC_TYPE","SUCCESS","TYPE","REFERER","CLIENTIP","ACCDATE","USRMSG"};
		String content = "";
		for (int i = 0; i < keys.length; i++) {
			if(null!= data.get(keys[i])){
				content += keys[i] + "=" + data.get(keys[i]) + "&";
			}
		}
		if (content.length() > 0) {
			content = content.substring(0, content.length() - 1);
		}
		if(StringUtils.isNotBlank(sign) && verify(sign, content)){
			verify_result = true;
		}
		Assert.isTrue(verify_result, PayExceptionCode.SIGN_VALIDATE_FAILED);
	}

	/**
	 * RSA验证方法
	 * @param signature
	 * @param value
	 * @return
	 */
	public boolean verify(String signature, String value) {
		try {
			RSASig sign = new RSASig();
			sign.setPublicKey(CCBConfig.CCB_PUBLIC_RSA);
			return sign.verifySigature(signature, value);
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String getFailedResponse(RequestParameter<?, ?> requestParameter) {
		return CCBConfig.NOTIFY_FAIL_RESULT;
	}

	@Override
	public PaymentResult[] convert(Map<String, String> data){
		// 是否是合并付款
		int isCombined = 0;
		try {
			isCombined = Integer.valueOf(data.get(CCBConfig.REMARK1));
		} catch (Exception e) {
			isCombined = 0;
		}
		PaymentResult paymentResult = new PaymentResult();
		paymentResult.setIsCombined(isCombined);
		paymentResult.setOrderId(data.get(CCBConfig.OUT_TRADE_NO));
		paymentResult.setTradeStatus(data.get(CCBConfig.SUCCESS_KEY));
		paymentResult.setThirdTradeNo(data.get(CCBConfig.TRADE_NO));
		paymentResult.setPrice(Double.valueOf(data.get(CCBConfig.PAYMENT)+""));
		paymentResult.setBuyerId(data.get(CCBConfig.ACC_TYPE));
		paymentResult.setPayInfo(data.get(CCBConfig.USRMSG));
		paymentResult.setStatus(CCBConfig.SUCCESS.equals(paymentResult.getTradeStatus()) ? PaymentOrderType.SCCUESS.getValue() : PaymentOrderType.FAIL.getValue());
		return new PaymentResult[]{paymentResult};
	}

	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
		//请求参数
		Map<String,Object> requestParams = new TreeMap<String, Object>();
		requestParams.put("MERCHANTID", CCBConfig.MERCHANT_ID);
		requestParams.put("POSID", CCBConfig.POS_ID);
		requestParams.put("BRANCHID",CCBConfig.BRANCH_ID);
		requestParams.put("ORDERID", parameter.getData().getOrderId());
		requestParams.put("PAYMENT",parameter.getData().getAmount());
		requestParams.put("CURCODE",CCBConfig.CUR_CODE);
		requestParams.put("REMARK1",parameter.getData().getIsCombined()?1:0);
		requestParams.put("REMARK2","");
		requestParams.put("TXCODE",CCBConfig.TX_CODE);
		String mac ="";
		requestParams.put("MAC",mac);
		requestParams.put("TYPE",CCBConfig.TYPE);
		requestParams.put("PUB",CCBConfig.CCB_PUBLIC_RSA_LAST30);
		requestParams.put("GATEWAY","");
		requestParams.put("CLIENTIP", FuncUtils.getIpAddr(WebUtil.getRequest()));
		requestParams.put("REGINFO", parameter.getData().getUserId());
		requestParams.put("PROINFO",getProductInfo(parameter.getData()));
		requestParams.put("REFERER","");

		String[] keys = {"MERCHANTID","POSID", "BRANCHID","ORDERID", "PAYMENT", "CURCODE","TXCODE","REMARK1", "REMARK2","TYPE","PUB","GATEWAY","CLIENTIP","REGINFO","PROINFO","REFERER"};
		String content = "";
		for (int i = 0; i < keys.length; i++) {
			content += keys[i] + "=" + requestParams.get(keys[i]) + "&";
		}
		if (content.length() > 0) {
			content = content.substring(0, content.length() - 1);
		}
		mac = DigestUtils.md5Hex(content);
		requestParams.remove("PUB");
		requestParams.put("MAC",mac);
		return HTMLUtil.createHtml(CCBConfig.PAY_REQUEST_ADDR, requestParams, HTMLUtil.ENCODING, HTMLUtil.REQUEST_METHOD_POST);
	}

	/**
	 * 按建行要求获得商品信息
	 * @param paymentOrder
	 */
	private String getProductInfo(PaymentOrder paymentOrder) {
		if(StringUtils.isNotBlank(paymentOrder.getSubject()) && paymentOrder.getSubject().length() < 100){
			return HTMLUtil.getJSScriptReturn("escape('"+paymentOrder.getSubject().replaceAll(",", "，").replaceAll(CCBConfig.REG_ESCAPE_STR, "")+"')", paymentOrder.getOrderId());
		}else{
			return paymentOrder.getOrderId();
		}
	}

	/**
	 *
	 * 查询订单支付状态（暂时未调通需要开放额外ip）
	 * @param requestParamsMap
	 * @return
	 */
	public Object queryPaymentOrder(Map<String, String> requestParamsMap) {
		Msg msg = new Msg(false, "");
		try {
			//请求参数
			Map<String,Object> requestParams = new TreeMap<String, Object>();
			requestParams.put("MERCHANTID", CCBConfig.MERCHANT_ID);
			requestParams.put("BRANCHID",CCBConfig.BRANCH_ID);
			requestParams.put("POSID", CCBConfig.POS_ID);
			requestParams.put("ORDERDATE", "");
			requestParams.put("BEGORDERTIME", "");
			requestParams.put("ENDORDERTIME", "");
			requestParams.put("ORDERID", requestParamsMap.get(CCBConfig.ORDER_ID));
			requestParams.put("QUPWD",CCBConfig.QUERY_PWD);
			requestParams.put("TYPE",CCBConfig.QUERY_TYPE_PAY);
			requestParams.put("TXCODE",CCBConfig.QUERY_TXCODE);
			requestParams.put("KIND",CCBConfig.QUERY_KIND_0);
			requestParams.put("STATUS",CCBConfig.QUERY_STATS_SUCCESS);
			requestParams.put("SEL_TYPE",CCBConfig.SEL_TYPE_XML);
			requestParams.put("PAGE",CCBConfig.DEFAULT_PAGE);
			requestParams.put("OPERATOR","");
			requestParams.put("CHANNEL","");
			requestParams.put("MAC", FuncUtils.getIpAddr(WebUtil.getRequest()));

			String[] keys = {"MERCHANTID","BRANCHID", "POSID","ORDERDATE", "BEGORDERTIME", "ENDORDERTIME","ORDERID","QUPWD", "TXCODE","TYPE","KIND","STATUS","SEL_TYPE","PAGE","OPERATOR","CHANNEL"};
			String content = "";
			for (int i = 0; i < keys.length; i++) {
				content += keys[i] + "=" + requestParams.get(keys[i]) + "&";
			}
			if (content.length() > 0) {
				content = content.substring(0, content.length() - 1);
			}

			LOGGER.info("content---"+content);
			String mac = DigestUtils.md5Hex(content);
			requestParams.put("MAC",mac);

			String getURL= HTMLUtil.createHtml(CCBConfig.PAY_REQUEST_ADDR, requestParams, HTMLUtil.ENCODING,HTMLUtil.REQUEST_METHOD_GET);
			LOGGER.info("getURL---"+getURL);

			msg.setOk(false);
			msg.setData("");
		} catch (Exception e) {
			e.printStackTrace();
			msg.setOk(false);
		}
		return msg;
	}
}
