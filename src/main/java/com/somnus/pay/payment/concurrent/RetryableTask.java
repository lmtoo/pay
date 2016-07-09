package com.somnus.pay.payment.concurrent;

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
public abstract class RetryableTask extends Task implements Retryable {

	private int total;
	private long interval;
	private int current;
	
	public RetryableTask(String name) {
		super(name);
		current = 0;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrent() {
		return current;
	}
	
	public void increaseCurrent() {
		this.current += 1;
	}

	public boolean canRetry(){
		return this.current < this.total;
	}
	
}
