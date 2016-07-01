package com.somnus.pay.payment.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.concurrent.Task;
import com.somnus.pay.payment.concurrent.TaskExecutor;
import com.somnus.pay.payment.enums.NotifyType;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.service.ObserverService;
import com.somnus.pay.payment.service.PaymentOrderService;
import com.somnus.pay.payment.util.SpringContextUtil;

/**
 *  @description: 支付成功事件监听器<br/>
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
public class PaySuccessEventListener extends AsyncEventListener<PaySuccessEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaySuccessEventListener.class);
	
	@Autowired
	private ObserverService observerService;
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	public PaySuccessEventListener(){
		LOGGER.debug("支付成功事件监听器启动");
	}
	
	@Override
	protected void handle(PaySuccessEvent event) {
		PaymentResult[] paymentResults = event.getPaymentResults();
		LOGGER.info("订单已支付成功:{}", JsonUtils.toJson(paymentResults));
		for (int i = 0; i < paymentResults.length; i++) {
			final PaymentResult paymentResult = paymentResults[i];
			if(paymentResult != null && PaymentOrderType.SCCUESS.getValue().equals(paymentResult.getStatus())){
				// 通知订单事件
				publishNotifyClientEvent(this,NotifyType.PAY_SUCCESS, paymentResult.getOrderId());
				// 通知报关事件
				publishCustomsDeclarationEvent(this, paymentResult);
				TaskExecutor.run(new Task("系统交易数据统计") {
					
					@Override
					public void run() {
						statistic(paymentResult);
					}
				});
			}
		}
	}

	protected void publishNotifyClientEvent(Object source,NotifyType notifyType,String orderId){
		LOGGER.info("订单[{}]支付成功,触发<通知支付请求方>事件", orderId);
		SpringContextUtil.getApplicationContext().publishEvent(new NotifyClientEvent(source, notifyType,orderId));
	}

	protected void publishCustomsDeclarationEvent(Object source,PaymentResult paymentResult){
		LOGGER.info("订单[{}]支付成功,触发<报关>事件", paymentResult.getOrderId());
		SpringContextUtil.getApplicationContext().publishEvent(new CustomsDeclarationEvent(source, paymentResult));
	}
	
	protected void statistic(PaymentResult paymentResult) {
		String orderId = paymentResult.getOrderId();
		PaymentOrder paymentOrder = paymentOrderService.get(orderId);
		if(paymentOrder == null){
			LOGGER.warn("订单[{}]不存在", orderId);
			return;
		}
		observerService.increaseTotalOrderCount();
		observerService.increaseTotalOrderAmount(paymentOrder.getTotalAmount());
		observerService.increaseTotalMoneyAmount(paymentOrder.getFinalAmount());
		observerService.increaseTotalBzAmount(paymentOrder.getBzAmount());
	}

}
