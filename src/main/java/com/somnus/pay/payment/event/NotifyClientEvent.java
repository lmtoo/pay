package com.somnus.pay.payment.event;

import com.somnus.pay.payment.enums.NotifyType;

/**
 *  @description: 支付成功时通知请求发起方事件<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-23<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-23       丹青生                        1.0            初始化 <br/>
 *  
 */
public class NotifyClientEvent extends RetryablePaymentEvent {

	private static final long serialVersionUID = 1L;
	
	private NotifyType type;
	private String orderId;
	
	/**
	 * @param source
	 */
	public NotifyClientEvent(Object source, NotifyType type, String orderId) {
		super(source, "向支付发起方推送订单[" + orderId + type.getDesc() + "]通知");
		this.type = type;
		this.orderId = orderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public NotifyType getType() {
		return type;
	}

}
