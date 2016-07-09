package com.somnus.pay.payment.event;

import com.somnus.pay.payment.concurrent.Retryable;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-26<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-26       丹青生                        1.0            初始化 <br/>
 *  
 */
public abstract class RetryablePaymentEvent extends PaymentEvent implements Retryable {
	
	private static final long serialVersionUID = 1L;

	protected int total;
	protected long interval;
	
	/**
	 * @param source
	 * @param name
	 */
	public RetryablePaymentEvent(Object source, String name) {
		super(source, name);
		this.total = 5;
		this.interval = 600000;
	}
	
	/**
	 * @param source
	 * @param name
	 */
	public RetryablePaymentEvent(Object source, String name, int total, long interval) {
		super(source, name);
		this.total = total;
		this.interval = interval;
	}
	
	@Override
	public int getTotal() {
		return total;
	}

	@Override
	public long getInterval() {
		return interval;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}
	
}
