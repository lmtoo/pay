package com.somnus.pay.payment.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.somnus.pay.utils.PWCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Msg;
import com.somnus.pay.payment.service.IClientService;
import com.somnus.pay.utils.Assert;

@Service
@Transactional
public class ClientServiceImpl implements IClientService {

	private Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

	private static final String SUPERBEAN_URL_QUERY = "/account/query.htm";
    private static final String SUPERBEAN_URL_TRADE = "/b5m-superbean/trade.htm";

	/**
	 * 超级帮钻支付密码是否正确接口
	 */
	private static final String SET_USER_BZ_PAY_PASSWORD = "/user/paypassword/setpaypassword.htm";
	private static final String SEND_BZ_PAY_PASSWORD_SMS = "/user/info/data/sendSMSValidateCode.htm";
	private static final String USER_INFO = "/user/user/data/info.htm";
	private static final String USER_BZ_PAY_PWD_SMS_APPTYPE = "41";
	private static final String USER_BZ_PAY_PWD_SMS_BUSICODE = "paypassword_validate";

	private @Value("#{configProperties['superBean.server']}")
	String SUPERBEAN_SERVER;
	private @Value("#{configProperties['ucenter.server']}")
	String UCENTER_SERVER;

	@Override
	public Long getSuperBeanByUserId(String userId) {
		Long tradeAmount = 0L;
		try {
			Map<String, String> paramtMap = new HashMap<String, String>();
			paramtMap.put("userId", userId);
			String url = SUPERBEAN_SERVER + SUPERBEAN_URL_QUERY;
			String result = HttpClientUtils.post(url, paramtMap);
			logger.info("Process in getSuperBeanByUserId response: result={}", result);
			JSONObject object = JSON.parseObject(result);
			if (object != null && object.getString("code") != null && "100".equals(object.getString("code"))) {
				object = object.getJSONObject("data");
				tradeAmount = object == null ? 0 : object.getLong("tradeAmount");
			}
		} catch (Exception e) {
			logger.warn("Process in getSuperBeanByUserId fail.userId:[{}]", userId);
		}
		return tradeAmount;
	}

	@Override
    public String paySuperBean(Map<String, String> param) {
        String url = SUPERBEAN_SERVER + SUPERBEAN_URL_TRADE;
        if(logger.isInfoEnabled()){
        	logger.info("请求抵扣超级帮钻:{} , 参数={}", url, param);
        }
        String response = null;
        try{
            response = HttpClientUtils.post(url, param);
        }catch(Exception e){
        	if(logger.isWarnEnabled()){
				logger.warn("请求帮钻结算系统失败", e);
			}
			throw new PayException(PayExceptionCode.REQUEST_BPS_SERVICE_ERROR);
        }
        if(logger.isInfoEnabled()){
        	logger.info("超级帮钻抵扣结果:{}", response);
        }
        Assert.hasText(response, PayExceptionCode.SUPER_BEAN_TRADE_NO_RESPONSE);
        JSONObject object = JSON.parseObject(response);
        Assert.isTrue(object.getBooleanValue("success"), PayExceptionCode.SUPER_BEAN_TRADE_FAILED);
        return object.getString("payid");
    }

	@Override
	public String setUserBzPayPassword(String userId, String password, String mobile, String validateCode) {
		String requestUrl = UCENTER_SERVER + SET_USER_BZ_PAY_PASSWORD + createUcenterUserTokenParams(userId);
		requestUrl += "&payPassword=" + password + "&mobile=" + mobile + "&validateCode=" + validateCode;
		logger.info("Process in setUserBzPayPassword method: requestUrl={}", requestUrl);
		String result = HttpClientUtils.get(requestUrl);
		logger.info("Process in setUserBzPayPassword response: result={}", result);
		Assert.hasText(result, PayExceptionCode.PAYPASSWORD_SETTING_ERROR);
		return result;
	}

	@Override
	public void sendBzPayPasswordSMS(String userId, String mobile) {
		String requestUrl = UCENTER_SERVER + SEND_BZ_PAY_PASSWORD_SMS + createUcenterUserTokenParams(userId);
		requestUrl += "&mobile=" + mobile + "&busiCode=" + USER_BZ_PAY_PWD_SMS_BUSICODE;
		logger.info("Process in sendBzPayPasswordSMS method: requestUrl={}", requestUrl);
		String result = HttpClientUtils.get(requestUrl);
		logger.info("Process in sendBzPayPasswordSMS response: result={}", result);
		Msg msg = JSON.parseObject(result, Msg.class);
		Assert.isTrue(msg.isOk(), PayExceptionCode.PAYPASSWORD_SENDSMS_ERROR);
	}

	/**
	 * 组装请求用户中心的用户信息
	 * 
	 * @param userId
	 * @return
	 */
	private String createUcenterUserTokenParams(String userId) {
		return "?identifier=" + userId + "&appType="
				+ USER_BZ_PAY_PWD_SMS_APPTYPE + "&encryCode="
				+ PWCode.getPassWordCode("b5m" + userId);
	}

	@Override
	public String getUserBindMobile(String userId) {
		String mobile = "";
		try {
			String requestUrl = UCENTER_SERVER + USER_INFO;
			requestUrl += "?identifier=" + userId + "&encryCode=" + PWCode.getPassWordCode("b5m" + userId) + "&params=isMobileBind,mobile ";
			String result = HttpClientUtils.get(requestUrl);
			Map<?, ?> userInfo = (Map<?,?>) JsonUtils.parse2Map(result).get("data");
			String isBind = (String) userInfo.get("isMobileBind");
			mobile = "1".equals(isBind) ? (String) userInfo.get("mobile") : "";
		} catch (Exception e) {
			logger.warn("获取用户[" + userId + "]绑定的手机号码出错", e);
		}
		return mobile;
	}

}
