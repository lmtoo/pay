package com.somnus.pay.payment.event;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.somnus.pay.payment.enums.NotifyType;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.service.PaymentNotifyService;
import com.somnus.pay.payment.service.PaymentOrderService;

/**
 *  @description: 支付成功时通知请求发起方事件监听器<br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-23<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-23       丹青生                        1.0            初始化 <br/>
 *  
 */
@Component
public class NotifyClientEventListener extends AsyncEventListener<NotifyClientEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(NotifyClientEventListener.class);

	@Resource
	private PaymentNotifyService paymentNotifyService;
	@Resource
	private PaymentOrderService paymentOrderService;

	public NotifyClientEventListener(){
		LOGGER.debug("支付通知事件监听器启动");
	}
	
	@Override
	protected void handle(NotifyClientEvent event) {
		NotifyType type = event.getType();
		String orderId = event.getOrderId();
		LOGGER.info("向订单[{}]支付请求发起方发送通知:{}", orderId, type);
		PaymentOrder paymentOrder = paymentOrderService.get(orderId);
		if(paymentOrder == null){
			LOGGER.warn("订单号为[{}]的订单不存在", orderId);
			return;
		}
		switch (type) {
			case BZ_FROZEN_SUCCESS:
				paymentNotifyService.notifyBzFrozenSuccess(paymentOrder);
				break;
			case PAY_SUCCESS:
				paymentNotifyService.notifyPaySuccess(paymentOrder);
				break;
			default:
				LOGGER.warn("不支持的通知类型:[{}]", type);
				break;
		}
	}

}
