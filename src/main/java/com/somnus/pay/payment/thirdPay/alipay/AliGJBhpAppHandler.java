package com.somnus.pay.payment.thirdPay.alipay;

import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.UAType;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.alipay.config.AlipayConfig;
import com.somnus.pay.payment.thirdPay.alipay.sign.RSA;
import com.somnus.pay.payment.thirdPay.alipay.util.AlipayNotify;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.MD5Util;
import com.somnus.pay.payment.util.PageCommonUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.utils.Assert;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

/**
 * @description: 支付宝支付渠道回调处理器
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2016-05-25   qingshu       1.0            初始化
 */
@Service
public class AliGJBhpAppHandler extends AbstractAliHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(AliGJBhpAppHandler.class);

	public AliGJBhpAppHandler() {
		super(AlipayConfig.CROSS_PAY_PARTNER, AlipayConfig.CROSS_PAY_PRIVATE_KEY_PKCS8,
                PayChannel.AliGJBhpAppPay, PayChannel.V_AliGJBhpAppPay_BG_NanSha,
                PayChannel.V_AliGJBhpAppPay_BG_BaiYun, PayChannel.V_AliGJBhpAppPay_BG_NingBo);
		this.covert2RMB = true;
	}

	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
		LOGGER.info("创建帮韩贝支付宝国际钱包渠道[{}]支付请求提交表单:[{}]", parameter.getChannel(), parameter.getData());
        HttpServletRequest request = WebUtil.getRequest();
        JSONObject json = new JSONObject();
        json.put("code", AlipayConfig.B5M_SUCCESS_CODE);
        json.put("success", AlipayConfig.B5M_SUCCESS_CODE);
        String body = parameter.getData().getOrderId();
        if (parameter.getData().getIsCombined()) {
            body = parameter.getData().getParentOrderId();
        }
        String amount = new DecimalFormat(AlipayConfig.DOUBLE_FORMAT).format(parameter.getData().getAmount());
        StringBuilder builder = new StringBuilder();
        builder.append("partner=\"" + partner + "\"");
        builder.append("&seller_id=\"" + AlipayConfig.CROSS_PAY_SELLER_ID + "\"");
        builder.append("&out_trade_no=\"" + parameter.getData().getOrderId() + "\"");
        builder.append("&subject=\"" + parameter.getData().getSubject() + "\"");
        builder.append("&body=\"" + body + "\"");
        builder.append("&rmb_fee=\"" + amount + "\"");
        String callback = PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + "/callback/" + parameter.getChannel().name();
        builder.append("&notify_url=\"" + callback + "/notify.htm" + "\"");
        builder.append("&service=\"" + AlipayConfig.PAY_SERVICE_NAME + "\"");
        builder.append("&payment_type=\"" + AlipayConfig.CROSS_PAY_PAYMENT_TYPE_1 + "\"");
        builder.append("&forex_biz=\"" + AlipayConfig.CROSS_PAY_FOREX_BIZ + "\"");
        builder.append("&currency=\"" + AlipayConfig.CROSS_PAY_CURRENCY_USD + "\"");
        builder.append("&_input_charset=\"" + AlipayConfig.input_charset + "\"");
        if (UAType.isIOSPlatform(request.getHeader("User-Agent"))) { // 区别 ios和 andriod的参数
            builder.append("&show_url=\"m.alipay.com\"");
        }else {
            builder.append("&return_url=\"m.alipay.com\"");
        }
        String orderInfo = builder.toString();
        String sign =  RSA.sign(orderInfo, AlipayConfig.CROSS_PAY_PRIVATE_KEY_PKCS8, AlipayConfig.input_charset);
        JSONObject json2 = new JSONObject();
        json2.put("orderInfo", orderInfo);
        try {
            json2.put("sign", URLEncoder.encode(sign,"UTF-8"));
        } catch (Exception e) {
        	LOGGER.warn("不支持UTF-8编码",e);
        }
        json2.put("success_return", payService.getReturnUrl(parameter.getData()).replace(Constants.NOPAY_STATUS_STR,Constants.PAID_STATUS_STR));
        json.put("appresult", json2.toJSONString());
        return json.toJSONString();
	}

	@Override
	protected void checkSign(Map<String, String> data) {
		boolean success = AlipayNotify.verifyRSA(data, this.partner, AlipayConfig.CROSS_PAY_ALIPAY_PUBLIC_KEY);
		Assert.isTrue(success, PayExceptionCode.SIGN_ERROR);
	}

    @Override
    public Map<String,String> queryPaymentOrder(Map<String, String> requestParamsMap) {
        return queryPaymentOrder(requestParamsMap,false);
    }

}
