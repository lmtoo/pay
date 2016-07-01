package com.somnus.pay.payment.thirdPay.tencent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.cache.CacheServiceExcutor;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.tencent.config.WxConfig;
import com.somnus.pay.payment.thirdPay.tencent.util.PayCommonUtil;
import com.somnus.pay.payment.thirdPay.tencent.util.TenpayUtil;
import com.somnus.pay.payment.thirdPay.tencent.util.XMLUtil;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.WebUtil;

/**
 * @description: 微信支付渠道回调处理器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
@Service     
public class WxWapHandler extends AbstractWxHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(WxWapHandler.class);
	
	public WxWapHandler() {
		super(PayChannel.WxWapPay, WxConfig.key, WxConfig.APPID, WxConfig.MCH_ID);
	}

	@Override
	public PaymentResult[] convert(Map<String, String> data) {
		PaymentResult[] paymentResult = super.convert(data);
		for (int i = 0; i < paymentResult.length; i++) {
			CacheServiceExcutor.remove(Constants.PAY_WX_OTHERAPP_MEMCACHE_KEY + paymentResult[i].getOrderId());
		}
		return paymentResult;
	}

	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("创建微信wap端jsapi的请求参数:[{}]",parameter.getData());
		}
		if (null == parameter.getData() || null == parameter.getData().getOrderId())
            return null;
		HttpServletRequest request = WebUtil.getRequest();		    
		String requestXML = transRequestWxPara2Xml(parameter.getData(), request, true);
        Map<String, String> map = null;
		try {
			String result = HttpClientUtils.post(WxConfig.UNIFIED_ORDER_URL, HttpClientUtils.buildParameter(requestXML));
			map = XMLUtil.doXMLParse(result);
			String returnCode = map.get("return_code");
			String resultCode = map.get("result_code");
			JSONObject json = new JSONObject();
			json.put("return_code", returnCode);
			json.put("result_code", resultCode);
			json.put("return_msg", map.get("return_msg"));
			json.put("err_code", map.get("err_code"));
			json.put("err_code_des", map.get("err_code_des"));
			json.put("ua", request.getHeader("User-Agent"));
			json.put("success_return", payService.getReturnUrl(parameter.getData()).replace(Constants.NOPAY_STATUS_STR, Constants.PAID_STATUS_STR));
			if (returnCode.equalsIgnoreCase("SUCCESS") && resultCode.equalsIgnoreCase("SUCCESS")) {
				Map<String, String> parameters = new HashMap<String, String>();  
				String getUnixTime = TenpayUtil.getUnixTime(new Date()) +"";
				String nonce_str = PayCommonUtil.createNoncestr() ; 
				parameters.put("appId", WxConfig.APPID);
				parameters.put("nonceStr", nonce_str);
				parameters.put("package", "prepay_id=" + map.get("prepay_id"));
				parameters.put("signType", WxConfig.sign_type);
				parameters.put("timeStamp",getUnixTime);
				
				json.put("appId", WxConfig.APPID);
				json.put("nonce_str", nonce_str);
				json.put("prepay_id",  map.get("prepay_id"));
				json.put("signType", WxConfig.sign_type);
				json.put("timeStamp", getUnixTime);
				json.put("paySign", PayCommonUtil.createSign(parameters, key));	
			}
			return json.toJSONString();
		} catch (Exception e) {
			LOGGER.warn("创建微信wap端jsapi的请求参数失败", e);
		}
		return null;
	}
	
}
