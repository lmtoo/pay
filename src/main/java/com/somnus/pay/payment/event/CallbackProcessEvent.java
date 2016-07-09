package com.somnus.pay.payment.event;

import java.util.Arrays;

import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.RequestParameter;

/**
 *  @description: 支付回调处理事件<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-23<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-23       丹青生                        1.0            初始化 <br/>
 *  
 */
public class CallbackProcessEvent extends RetryablePaymentEvent {

	private static final long serialVersionUID = 1L;
	
	private RequestParameter<?, ?> requestParameter;
	private PaymentResult[] paymentResults;
	
	/**
	 * @param source
	 * @param name
	 */
	public CallbackProcessEvent(Object source, RequestParameter<?, ?> requestParameter, PaymentResult...paymentResults) {
		super(source, "订单" + getOrderId(paymentResults) + "支付回调处理事件");
		this.requestParameter = requestParameter;
		this.paymentResults = paymentResults;
	}

	public RequestParameter<?, ?> getRequestParameter() {
		return requestParameter;
	}

	public PaymentResult[] getPaymentResults() {
		return paymentResults;
	}
	
	private static String getOrderId(PaymentResult...paymentResults){
		String[] orderIds = new String[paymentResults.length];
		for (int i = 0; i < paymentResults.length; i++) {
			orderIds[i] = paymentResults[i].getOrderId();
		}
		return Arrays.deepToString(orderIds);
	}
	
}
