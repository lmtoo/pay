package com.somnus.pay.payment.event;

import com.somnus.pay.payment.pojo.PaymentResult;

/**
 * @description: ${TODO}
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/25
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/25       方东白          1.0       支付报关事件
 */
public class CustomsDeclarationEvent  extends RetryablePaymentEvent {

    private static final long serialVersionUID = 1L;

    private PaymentResult paymentResult;

    /**
     * @param source
     * @param paymentResult
     */
    public CustomsDeclarationEvent(Object source, PaymentResult paymentResult) {
    	super(source, "订单[" + paymentResult.getOrderId() + "]报关");
    	this.paymentResult = paymentResult;
    }
    
    public PaymentResult getPaymentOrder() {
        return paymentResult;
    }

}
