package com.somnus.pay.payment.web.core;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.somnus.pay.utils.JsonUtils;
import com.somnus.pay.utils.RequestUtils;

/**
 * HTTP请求记录拦截器
 * 
 * @author 丹青生
 *
 * @date 2015-7-31
 */
public class RequestLoggerInterceptor extends HandlerInterceptorAdapter {

	private final static Logger LOGGER = LoggerFactory.getLogger(RequestLoggerInterceptor.class);
	
	public RequestLoggerInterceptor(){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("请求记录器启动");
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(LOGGER.isDebugEnabled()){
			String url = request.getRequestURI();
			String paramJson = JsonUtils.toJson(request.getParameterMap());
			StringBuilder builder = new StringBuilder();
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()){
				String name = (String)headerNames.nextElement();
				if("cookie".equalsIgnoreCase(name)){
					continue;
				}
				builder.append(name).append("=").append(request.getHeader(name)).append(" ;");
			}
			LOGGER.debug("[{}]请求访问:[{}], 参数:{} ,HEADs:{}", new Object[]{RequestUtils.getRemoteIP(request), url, paramJson, builder});
		}
		return super.preHandle(request, response, handler);
	}
	
}
