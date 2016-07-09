package com.somnus.pay.payment.thirdPay.alipay;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.alipay.config.AlipayConfig;
import com.somnus.pay.payment.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

/**
  * @description: 配合帮我采使用支付宝支付渠道回调处理器
  * @author: qingshu
  * @version: 1.0
  * @createdate: 2016-05-09
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2016-05-09   qingshu       1.0            初始化
  */
@Service
public class AliB5MWapHandler extends AbstractAliHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(AliB5MWapHandler.class);

	public AliB5MWapHandler() {
		super(PayChannel.AliB5mWapPay, AlipayConfig.B5MAPP_PARTNER, AlipayConfig.B5MAPP_KEY);
		this.covert2RMB = false;
	}

	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
		LOGGER.info("构建支付宝wap去[{}]支付页面跳转form", parameter.getChannel());
		TreeMap<String, String> requestMap = new TreeMap<String, String>();
		requestMap.put("service", AlipayConfig.WAP_PAY_SERVICE_NAME);
		requestMap.put("partner", partner);
		requestMap.put("_input_charset", AlipayConfig.WAP_PAY_INPUT_CHARSET);
		String callback = PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + "/callback/" + parameter.getChannel().name();
		requestMap.put("notify_url", callback + "/notify.htm");// 服务器异步通知页面路径
		requestMap.put("return_url", callback + "/return.htm");// 页面跳转同步通知页面路径
		requestMap.put("out_trade_no", parameter.getData().getOrderId());
		requestMap.put("merchant_url", Constants.B5M_MOBILE_DOMAIN);
		requestMap.put("subject", parameter.getData().getSubject());
		requestMap.put("total_fee", PayAmountUtil.getDoubleFormat(parameter.getData().getAmount()));
		requestMap.put("seller_id", AlipayConfig.B5MAPP_SELLER_ID);
		requestMap.put("payment_type", AlipayConfig.WAP_PAYMENT_TYPE);
		requestMap.put("sign", getSign(requestMap));
		requestMap.put("sign_type", AlipayConfig.CROSS_PAY_SIGN_TYPE);
		String html = HTMLUtil.createSubmitHtml(AlipayConfig.CROSS_PAY_REQUEST_WAP + "?_input_charset=" + AlipayConfig.CROSS_PAY_INPUT_CHARSET, requestMap, null, null);
		LOGGER.info("支付宝支付跳转form:{}", html);
		return html;
	}


}
