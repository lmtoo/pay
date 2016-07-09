package com.somnus.pay.payment.event;

import com.somnus.pay.payment.pojo.PaymentResult;

/**
 *  @description: 支付成功事件<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-23<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-23       丹青生                        1.0            初始化 <br/>
 *  
 */
public class PaySuccessEvent extends PaymentEvent {

	private static final long serialVersionUID = 1L;
	
	private PaymentResult[] paymentResults;
	
	/**
	 * @param source
	 * @param paymentResults
	 */
	public PaySuccessEvent(Object source, PaymentResult...paymentResults) {
		super(source, "支付成功");
		this.paymentResults = paymentResults;
	}

	public PaymentResult[] getPaymentResults() {
		return paymentResults;
	}
	
}
