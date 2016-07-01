package com.somnus.pay.payment.event;

import org.springframework.context.ApplicationEvent;

/**
 * @description: 
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-23
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-23       丹青生                               1.0            初始化
 */
public abstract class PaymentEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected boolean async = true; // 默认使用异步模式
	protected long delay;
	
	/**
	 * @param source
	 */
	public PaymentEvent(Object source, String name) {
		super(source);
		this.name = name;
	}
	
	public PaymentEvent(Object source, String name, boolean async) {
		super(source);
		this.name = name;
		this.async = async;
	}

	public String getName() {
		return name;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public boolean isAsync() {
		return async;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
	
}
