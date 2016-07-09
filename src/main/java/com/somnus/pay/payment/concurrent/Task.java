package com.somnus.pay.payment.concurrent;

import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.utils.Assert;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-23<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-23       丹青生                        1.0            初始化 <br/>
 *  
 */
public abstract class Task implements Runnable{

	private String name;
	private long delay;
	
	public Task(String name){
		Assert.hasText(name, PayExceptionCode.ASYNC_TASK_NAME_ERROR);
		this.name = name;
	}
	
	public Task(String name, long delay){
		this(name);
		this.delay = delay >= 0 ? delay : 0;
	}
	
	public String getName() {
		return name;
	}
	
	public long getDelay() {
		return delay;
	}

	/**
	 * 设置延迟时间(毫秒)
	 * @param delay 延迟时间(毫秒)
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	@Override
	public abstract void run();

}
