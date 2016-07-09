package com.somnus.pay.payment.thirdPay.tencent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.somnus.pay.payment.util.PageCommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.tencent.config.WxConfig;
import com.somnus.pay.payment.thirdPay.tencent.util.XMLUtil;
import com.somnus.pay.payment.util.HTMLUtil;
import com.somnus.pay.payment.util.PaymentHttpUtils;
import com.somnus.pay.payment.util.WebUtil;

/**
 * @description: 微信支付渠道回调处理器
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
@Service     
public class WxHandler extends AbstractWxHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(WxHandler.class);
	
	public WxHandler() {
		super(PayChannel.WxPay, WxConfig.key, WxConfig.APPID, WxConfig.MCH_ID);
	}

	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
		if(LOGGER.isInfoEnabled()){
            LOGGER.info("创建微信PC端二维码支付的请求参数:[{}]", parameter.getData());
        }
        HttpServletRequest request = WebUtil.getRequest();
        String requestXML = transRequestWxPara2Xml(parameter.getData(),request, false);
        Map<String, String> map = null;
		try {
			String result = HttpClientUtils.post(WxConfig.UNIFIED_ORDER_URL, HttpClientUtils.buildParameter(requestXML));;
			map = XMLUtil.doXMLParse(result);
			String returnCode = map.get("return_code");
			String resultCode = map.get("result_code");
			if (returnCode.equalsIgnoreCase("SUCCESS") && resultCode.equalsIgnoreCase("SUCCESS")) {
				map.put("finalAmount", parameter.getData().getAmount()+"");     
				map.put("orderId", parameter.getData().getOrderId());		      	
				return PaymentHttpUtils.buildRequest(map, HTMLUtil.REQUEST_METHOD_POST, PageCommonUtil.getRootPath(request, false) + "/third/wxcode.htm");
			}else if(returnCode.equalsIgnoreCase("SUCCESS") && !resultCode.equalsIgnoreCase("SUCCESS")){
				return returnCode + "" + map.get("err_code_des") ;
			}
			return map.get("return_code") + map.get("return_msg");
		} catch (Exception e) {
			LOGGER.warn("创建微信PC端二维码支付的请求参数失败", e);
		}
		return null;
	}

}
