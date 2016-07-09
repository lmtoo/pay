package com.somnus.pay.payment.concurrent;

/**
 *  @description: 可重试任务接口<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-26<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-26       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface Retryable {

	/**
	 * 获取可重试次数
	 * @return 可重试次数
	 */
	public int getTotal();
	
	/**
	 * 获取重试时间间隔(毫秒)
	 * @return 重试时间间隔(毫秒)
	 */
	public long getInterval();
	
}
