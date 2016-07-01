package com.somnus.pay.payment.service;

import com.somnus.pay.payment.pojo.*;

import java.util.Map;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface PaymentOrderService {

	public PaymentOrder get(String orderId, int source);
	
	public PaymentOrder get(String orderId);
	
	public PaymentResult convert(PaymentOrder paymentOrder);

	/**
	 * 分页查询所有支付通知
	 * @param page
	 * @return
	 */
	public QueryResult<PaymentOrder> list(Page page,Map<String,Object> params);
	
	public PaymentOrder get(String orderId, String userId);
	
}
