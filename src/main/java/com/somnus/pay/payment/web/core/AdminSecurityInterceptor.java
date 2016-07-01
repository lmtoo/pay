package com.somnus.pay.payment.web.core;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.somnus.pay.cache.CacheServiceExcutor;
import com.somnus.pay.payment.config.SystemConfig;
import com.somnus.pay.payment.event.AdminAccessEvent;
import com.somnus.pay.payment.util.SpringContextUtil;
import com.somnus.pay.utils.RequestUtils;

/**
 * HTTP请求记录拦截器
 * 
 * @author 丹青生
 *
 * @date 2015-7-31
 */
public class AdminSecurityInterceptor extends HandlerInterceptorAdapter {

	private final static Logger LOGGER = LoggerFactory.getLogger(AdminSecurityInterceptor.class);
	
	public AdminSecurityInterceptor(){
		LOGGER.debug("管理员权限控制器启动");
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String url = request.getRequestURI();
		if(url.startsWith("/console/") || url.startsWith("/server/")){
			// 开放给CRM的接口无需认证,并增加服务器之间访问刷新问题
			if(url.startsWith("/server/getOrderDetail") || url.startsWith("/order/getOrderDetail")
					|| url.startsWith("/server/cleanUserCache")
					|| url.startsWith("/server/cacheDBConfigClean")
					|| url.startsWith("/server/paymentConfigOper")
					|| url.startsWith("/server/paymentConfigQuery")
					|| url.startsWith("/server/refleshFrontTimestamp")
					|| url.startsWith("/console/cacheDBPaySourceClean")){
				return true;
			}
			String ip = RequestUtils.getRemoteIP(request);
//			if("127.0.0.1,172.16.13.18,172.16.13.36,172.16.13.44".contains(ip)){
				LOGGER.info("来自疑似安全IP[{}]的请求", ip);
				if(url.startsWith("/console/terrorists")){
					return true;
				}
				String token = null;
				Cookie[] cookies = request.getCookies();
				if(cookies != null && cookies.length > 0){
					for (int i = 0; i < cookies.length; i++) {
						Cookie cookie = cookies[i];
						if("pay_token".equalsIgnoreCase(cookie.getName())){
							token = cookie.getValue();
							break;
						}
					}
				}
				if(StringUtils.isNotEmpty(token)){
					if(url.startsWith("/console/get/key") || url.startsWith("/console/login")){
						return true;
					}
					String loginIP = (String) CacheServiceExcutor.get(SystemConfig.ADMIN_LOGIN_CACHE_KEY_PREFIX + token);
					LOGGER.info("正在检查会话ID:{}的身份认证信息,上次有效登录IP:{}", token, loginIP);
					if(ip.equals(loginIP)){
						LOGGER.info("当前会话[{}]已确认为安全状态", token);
						AdminAccessEvent event = new AdminAccessEvent(this, token, loginIP);
						SpringContextUtil.getApplicationContext().publishEvent(event);
						return super.preHandle(request, response, handler);
					}
				}
//			}
			LOGGER.info("未授权或不安全的会话");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return false;
		}
		return true;
	}
	
}
