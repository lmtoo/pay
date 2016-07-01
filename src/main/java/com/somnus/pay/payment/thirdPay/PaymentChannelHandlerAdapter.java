package com.somnus.pay.payment.thirdPay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Service;

import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.utils.Assert;

/**
 * @description: 回调请求处理器适配器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-11
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-11       丹青生                               1.0            初始化
 */
@Service
public class PaymentChannelHandlerAdapter implements InitializingBean, ApplicationListener<ContextRefreshedEvent> {

	@Resource
	private IConfigService configService;
	@Resource
	private IPayService payService;
	private boolean detectAllHandlers = true;
	private List<PaymentChannelHandler> callbackHandlers;
	
	/**
	 * 获得一个能够处理当前回调请求的处理器
	 * @param parameter 回调请求参数
	 * @return 能够处理当前请求的处理器(如果未找到,则抛异常)
	 */
	public PaymentChannelHandler getHandler(RequestParameter parameter){
		Assert.notNull(parameter, PayExceptionCode.PAYMENT_PARAMETER_IS_NULL);
		Assert.notNull(parameter.getChannel(), PayExceptionCode.PAY_CHANNEL_IS_NULL);
		int count = callbackHandlers.size();
		for (int i = 0; i < count; i++) {
			PaymentChannelHandler handler = callbackHandlers.get(i);
			if(handler.isSupport(parameter)){
				return handler;
			}
		}
		return null;
	}

	public IConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(IConfigService configService) {
		this.configService = configService;
	}

	public IPayService getPayService() {
		return payService;
	}

	public void setPayService(IPayService payService) {
		this.payService = payService;
	}

	public boolean isDetectAllHandlers() {
		return detectAllHandlers;
	}

	public void setDetectAllHandlers(boolean detectAllHandlers) {
		this.detectAllHandlers = detectAllHandlers;
	}

	public List<PaymentChannelHandler> getCallbackHandlers() {
		return callbackHandlers;
	}

	public void setCallbackHandlers(List<PaymentChannelHandler> callbackHandlers) {
		this.callbackHandlers = callbackHandlers;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		callbackHandlers = callbackHandlers == null ? new ArrayList<PaymentChannelHandler>() : callbackHandlers;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		if(context.getParent() == null && this.detectAllHandlers){
			Map<String, PaymentChannelHandler> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, PaymentChannelHandler.class, true, false);
			if (!matchingBeans.isEmpty()) {
				callbackHandlers.addAll(matchingBeans.values());
				OrderComparator.sort(this.callbackHandlers);
			}
		}
	}
	
}
