package com.somnus.pay.payment.thirdPay.alipay;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.somnus.pay.payment.thirdPay.alipay.sign.RSA;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandler;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.alipay.config.AlipayConfig;
import com.somnus.pay.payment.thirdPay.alipay.util.AlipayNotify;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.HTMLUtil;
import com.somnus.pay.payment.util.MD5Util;
import com.somnus.pay.payment.util.PayAmountUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.payment.util.PageCommonUtil;
import com.somnus.pay.utils.Assert;

/**
 * @description: 支付宝支付渠道回调抽象处理器
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 * 2015-12-23   丹青生                       1.0            结构调整
 */
public abstract class AbstractAliHandler extends PaymentChannelHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractAliHandler.class);

	protected String partner;
	protected String signKey;
	protected boolean covert2RMB = true;
	
	public AbstractAliHandler(PayChannel channel, String partner, String signKey) {
		this(partner, signKey, channel);
	}

	public AbstractAliHandler(String partner, String signKey, PayChannel...channel) {
		super(channel);
		this.partner = partner;
		this.signKey = signKey;
	}

	@Override
	public String handleNotify(Map<String, String> data) {
		LOGGER.info("处理支付宝支付后端返回通知:[{}]", data);
		checkSign(data);
		return AlipayConfig.NOTIFY_SUCCESS_RESULT;
	}

	@Override
	public String handleReturn(Map<String, String> data) {
		LOGGER.info("处理支付宝支付前端返回通知:[{}]", data);
		checkSign(data);
		return data.get(AlipayConfig.OUT_TRADE_NO);
	}
	
	protected void checkSign(Map<String, String> data) {
		boolean success = AlipayNotify.verifyMD5(data, partner, signKey);
		Assert.isTrue(success, PayExceptionCode.SIGN_ERROR);
	}

	@Override
	public String getFailedResponse(RequestParameter<?, ?> parameter) {
		return AlipayConfig.NOTIFY_FAIL_RESULT;
	}

	@Override
	public PaymentResult[] convert(Map<String, String> data) {
        try {
            int isCombined = 0;
            String orderId = data.get(AlipayConfig.OUT_TRADE_NO);
            if (orderId.indexOf(Constants.COMBINED_ORDER_PREFIX) > -1) {
                isCombined = 1;
            } else {
                isCombined = 0;
            }
            PaymentResult paymentResult = new PaymentResult();
            paymentResult.setIsCombined(isCombined);
            paymentResult.setOrderId(orderId);
            paymentResult.setTradeStatus(data.get(AlipayConfig.TRADE_STATUS));
            paymentResult.setThirdTradeNo(data.get(AlipayConfig.TRADE_NO));
            paymentResult.setPrice(Double.valueOf(StringUtils.defaultIfBlank(data.get("total_fee"), "0")));
            paymentResult.setBuyerId(data.get("buyerId"));
            if(covert2RMB){
            	LOGGER.info("通过查询接口转换订单[{}]的RMB金额", orderId);
            	Map<String,String> queryMap = new HashMap<String,String>();
            	queryMap.put(Constants.PAY_ID_KEY,paymentResult.getOrderId());
            	Map<String,String> resMap = queryPaymentOrder(queryMap);
            	paymentResult.setPrice(Double.valueOf(StringUtils.defaultIfBlank(resMap.get("price"),"0")));
            	paymentResult.setBuyerId(resMap.get("buyerId"));
            }
            paymentResult.setPayInfo(data.get(AlipayConfig.CROSS_PAY_CURRENCY)+","+Double.valueOf(StringUtils.defaultIfBlank(data.get(AlipayConfig.TOTAL_FEE),"0")));
            String tradeStatus = data.get(AlipayConfig.TRADE_STATUS);
            boolean status = false;
            //如果退款功能支付成功为“TRADE_SUCCESS”，如果没有退款功能成功为“TRADE_FINISHED”
            if(AlipayConfig.TRADE_SUCCESS.equals(tradeStatus) || AlipayConfig.TRADE_FINISHED.equals(tradeStatus)){
                status = true;
            }
            paymentResult.setStatus(status? PaymentOrderType.SCCUESS.getValue() : PaymentOrderType.FAIL.getValue());
            return new PaymentResult[]{paymentResult};
        } catch (Exception e) {
            LOGGER.warn("支付结果通知数据格式转换错误", e);
            throw new PayException(PayExceptionCode.ALI_CALLBACK_PARAMETER_CONVERT_ERROR, e);
        }
	}

	public Map<String,String> queryPaymentOrder(Map<String, String> requestParamsMap) {
		return queryPaymentOrder(requestParamsMap,true);
	}
	
	public Map<String,String> queryPaymentOrder(Map<String, String> requestParamsMap, boolean isMD5SignType) {
        Map<String,String> result = null;
        try {
            //请求参数
            TreeMap<String,String> requestParams = new TreeMap<String,String> ();
            requestParams.put("service", AlipayConfig.QUERY_SERVICE_NAME);
            requestParams.put("partner", partner);
            requestParams.put("_input_charset", AlipayConfig.CROSS_PAY_INPUT_CHARSET);
            requestParams.put("out_trade_no", requestParamsMap.get(Constants.PAY_ID_KEY));
			if(isMD5SignType){
				requestParams.put("sign", getSign(requestParams));
				requestParams.put("sign_type", AlipayConfig.CROSS_PAY_SIGN_TYPE);
			}else{
				requestParams.put("sign", getSign(requestParams,false));
				requestParams.put("sign_type", AlipayConfig.SIGN_TYPE_RSA);
			}
            String resTxt = HttpClientUtils.get(AlipayConfig.CROSS_PAY_REQUEST_WAP,requestParams);
            Document document = DocumentHelper.parseText(resTxt);
            String orderId = document.selectSingleNode("//*[@name='out_trade_no']").getText();
            String tradeStatus = document.selectSingleNode("//alipay/response/trade/trade_status").getText();
            String thirdTradeNo = document.selectSingleNode("//alipay/response/trade/trade_no").getText();
            Double price = Double.valueOf(document.selectSingleNode("//alipay/response/trade/price").getText());
            String buyerId = document.selectSingleNode("//alipay/response/trade/seller_email").getText();
            //如果退款功能支付成功为“TRADE_SUCCESS”，如果没有退款功能成功为“TRADE_FINISHED”
            if(AlipayConfig.TRADE_SUCCESS.equals(tradeStatus) || AlipayConfig.TRADE_FINISHED.equals(tradeStatus)){
                result = new HashMap<String,String>();
                result.put("orderId",orderId);
                result.put("tradeStatus",tradeStatus);
                result.put("thirdTradeNo",thirdTradeNo);
                result.put("price",price+"");
                result.put("buyerId",buyerId);
            }
        } catch (Exception e) {
        	LOGGER.warn("支付宝国际支付订单详情查询失败", e);
        	throw new PayException(PayExceptionCode.ALI_ORDER_QUERY_ERROR, e);
        }
        return result;
    }

	protected String getSign(TreeMap<String, String> param) {
		return getSign(param,true);
	}
	
	protected String getSign(TreeMap<String, String> param, boolean isMD5SignType) {
		String res = "";
		StringBuilder builder = new StringBuilder();
		if (MapUtils.isNotEmpty(param)) {
			for (Entry<String, String> entry : param.entrySet()) {
				builder.append(entry.getKey() + "=" + entry.getValue() + "&");
			}
			builder.deleteCharAt(builder.length() - 1);
			res = builder.toString();
			LOGGER.info("即将进行签名计算:{}", res);
			if(isMD5SignType){
				res = MD5Util.sign(res, signKey, AlipayConfig.CROSS_PAY_INPUT_CHARSET);
			}else{
				res = RSA.sign(res, signKey, AlipayConfig.CROSS_PAY_INPUT_CHARSET);
			}
			LOGGER.info("签名计算结果:{}", res);
		}
		return res;
	}

	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
		LOGGER.info("构建[{}]支付页面跳转form", parameter.getChannel());
		TreeMap<String, String> requestMap = new TreeMap<String, String>();
		requestMap.put("service", AlipayConfig.CROSS_PAY_SERVICE_NAME);
		requestMap.put("partner", partner);
		requestMap.put("_input_charset", AlipayConfig.CROSS_PAY_INPUT_CHARSET);
		String callback = PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + "/callback/" + parameter.getChannel().name();
		requestMap.put("notify_url", callback + "/notify.htm");// 服务器异步通知页面路径
		requestMap.put("return_url", callback + "/return.htm");// 页面跳转同步通知页面路径
		requestMap.put("out_trade_no", parameter.getData().getOrderId());
		requestMap.put("currency", AlipayConfig.CROSS_PAY_CURRENCY_TYPE);
		requestMap.put("merchant_url", Constants.B5M_DOMAIN);
		requestMap.put("subject", parameter.getData().getSubject());
        requestMap.put("rmb_fee", PayAmountUtil.getDoubleFormat(parameter.getData().getAmount()));
		requestMap.put("sign", getSign(requestMap));
		requestMap.put("sign_type", AlipayConfig.CROSS_PAY_SIGN_TYPE);
		String html = HTMLUtil.createSubmitHtml(AlipayConfig.CROSS_PAY_REQUEST_WAP + "?_input_charset=" + AlipayConfig.CROSS_PAY_INPUT_CHARSET, requestMap, null, null);
		LOGGER.info("支付宝支付跳转form:{}", html);
		return html;
	}

}
