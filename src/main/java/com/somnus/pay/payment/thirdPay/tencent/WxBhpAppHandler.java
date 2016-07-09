package com.somnus.pay.payment.thirdPay.tencent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.somnus.pay.payment.util.FuncUtils;
import com.somnus.pay.payment.util.MemCachedUtil;
import com.somnus.pay.payment.util.PayAmountUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.payment.util.PageCommonUtil;

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
public class WxBhpAppHandler extends AbstractWxHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(WxBhpAppHandler.class);
	
	public WxBhpAppHandler() {
		super(PayChannel.WxBhpAppPay, WxConfig.APIKEY_KOREA_APP, WxConfig.APPID_KOREA_APP, WxConfig.MCH_KOREA_ID);
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
		LOGGER.info("创建[{}]支付请求的参数:[{}]", parameter.getChannel(), parameter.getData());
        HttpServletRequest request = WebUtil.getRequest();
        Map<String,String> propsMap = new HashMap<String,String>();
        propsMap.put(WxConfig.APP_ID_KEY, WxConfig.APPID_KOREA_APP);
        propsMap.put(WxConfig.MCH_ID_KEY, WxConfig.MCH_KOREA_ID);
        propsMap.put(WxConfig.MCH_APIKEY_KEY, WxConfig.APIKEY_KOREA_APP);
        String requestXML = transRequestWxPara2Xml(parameter.getData(), request, propsMap);
        Map<String, String> map = null;
        String html = null;
        try {
        	String result = HttpClientUtils.post(WxConfig.UNIFIED_ORDER_URL, HttpClientUtils.buildParameter(requestXML));
            map = XMLUtil.doXMLParse(result);
            JSONObject json = new JSONObject();
            String errcode = map.get("err_code");
            String errmsg =  map.get("err_code_des");
            json.put("errcode", errcode);
            json.put("errmsg", errmsg);
            String returnCode = map.get("return_code");
            String resultCode = map.get("result_code");
            String orderId = parameter.getData().getOrderId();
            if (returnCode.equalsIgnoreCase("SUCCESS") && resultCode.equalsIgnoreCase("SUCCESS")) {
            	String getUnixTime = TenpayUtil.getUnixTime(new Date()) +"";
            	String noncestr = PayCommonUtil.createNoncestr();
            	Map<String, String> parameters = new HashMap<String, String>();
            	parameters.put("appid", WxConfig.APPID_KOREA_APP);
            	parameters.put("partnerid", WxConfig.MCH_KOREA_ID);
            	parameters.put("prepayid", map.get("prepay_id"));
            	parameters.put("package", "Sign=WXPay");
            	parameters.put("noncestr",noncestr);
            	parameters.put("timestamp",getUnixTime);
            	String sign_result =  PayCommonUtil.createSign(parameters, key);
            	
            	JSONObject json2 = new JSONObject();
            	json2.put("appid", WxConfig.APPID_KOREA_APP);
            	json2.put("noncestr", noncestr);
            	json2.put("package", "Sign=WXPay");
            	json2.put("partnerid", WxConfig.MCH_KOREA_ID);
            	json2.put("prepayid", map.get("prepay_id"));
            	json2.put("sign", sign_result);
            	json2.put("timestamp", getUnixTime);
            	json2.put("success_return", payService.getReturnUrl(parameter.getData()).replace(Constants.NOPAY_STATUS_STR,Constants.PAID_STATUS_STR));
            	json.put("appresult", json2.toJSONString());
            	json.put("errcode", "0");
            	json.put("errmsg", "Success");
            	MemCachedUtil.setCache(Constants.PAY_WX_OTHERAPP_MEMCACHE_KEY + orderId, json.toJSONString());
            }else{
            	if(WxConfig.OUT_TRADE_NO_USED.equals(errcode)){
            		Object wxPayResult = MemCachedUtil.getCache(Constants.PAY_WX_OTHERAPP_MEMCACHE_KEY + orderId);
            		if(null!=wxPayResult){
            			LOGGER.warn("创建帮韩品APP微信支付请求重复,从MemCache中获得orderId:[{}],res:[{}]", orderId, wxPayResult);
            			return wxPayResult.toString();
            		}
            	}
            }
            html = json.toJSONString();
        } catch (Exception e) {
        	LOGGER.warn("创建帮韩品APP微信支付请求失败", e);
        }
        return	html;
	}

	/**
     * 转换微信需求参数格式XML
     * @param paymentOrder
     * @param request
     * @param propsMap
     * @return
     */
    public String transRequestWxPara2Xml(PaymentOrder paymentOrder, HttpServletRequest request, Map<String, String> propsMap){
        if(LOGGER.isInfoEnabled()){
        	LOGGER.info("转换微信需求参数格式XML参数[{}]+[{}]",paymentOrder.toString(),propsMap);
        }
        if (null == paymentOrder || null == paymentOrder.getOrderId())
            return null;
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("appid", WxConfig.APPID_KOREA_APP);	//公众号
        String body = paymentOrder.getOrderId();
        if (paymentOrder.getIsCombined()) {
            body = paymentOrder.getParentOrderId();
        }
        parameters.put("body", body);
        if(StringUtils.isNotBlank(paymentOrder.getImei())){
            parameters.put("device_info", paymentOrder.getImei());
        }
        parameters.put("mch_id", WxConfig.MCH_KOREA_ID);	//商户号
        parameters.put("nonce_str", PayCommonUtil.createNoncestr());	//随机码
        String notify_Url = WxConfig.NOTIFY_NEW_APP_URL;
        parameters.put("notify_url", PageCommonUtil.getRootPath(request, true) + notify_Url);	//支付成功后回调的地址
        String  orderId = paymentOrder.getOrderId();	// 商家订单号
        //合并支付的订单
        if (paymentOrder.getIsCombined()) {
            orderId = paymentOrder.getParentOrderId();
        }
        parameters.put("out_trade_no", orderId);	//订单号
        String user_ip = FuncUtils.getIpAddr(request);
        if(StringUtils.isBlank(user_ip) || user_ip.indexOf("0:0:0:0") != -1){  //测试使用
            user_ip = "116.231.217.212";
        }
        parameters.put("spbill_create_ip",user_ip.trim());	// 用户ip
        parameters.put("time_stamp", "" + TenpayUtil.getUnixTime(new Date()));	//时间戳（秒级）
        String totalFee = PayAmountUtil.mul(paymentOrder.getAmount().toString(), WxConfig.PAY_MOUNT_UNIT + "") + "";
        parameters.put("total_fee", totalFee);	// 商品金额,以分为单位
        parameters.put("trade_type", "APP");
        parameters.put("sign",PayCommonUtil.createSign(parameters, key));
        return  PayCommonUtil.getRequestXml(parameters);
    }
	
}
