package com.somnus.pay.payment.service;

import com.somnus.pay.payment.pojo.PaymentOrder;

/**
 *  @description: 支付通知服务<br/>
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
public interface PaymentNotifyService {

	public void notifyBzFrozenSuccess(PaymentOrder paymentOrder);
	
	public void notifyPaySuccess(PaymentOrder paymentOrder);
	
}
