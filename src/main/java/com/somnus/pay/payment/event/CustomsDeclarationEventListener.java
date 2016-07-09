package com.somnus.pay.payment.event;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.service.PaymentChannelService;

/**
 *  @description: 支付宝报关
 *  @author: 方东白
 *  @version: 1.0
 *  @createdate: 2015-12-25
 *  Modification  History:
 *  Date         Author        Version        Discription
 *  -----------------------------------------------------
 *  2015-12-25       方东白     1.0            支付宝报关
 *  
 */
@Component
public class CustomsDeclarationEventListener extends AsyncEventListener<CustomsDeclarationEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(CustomsDeclarationEventListener.class);

	@Resource
	private PaymentChannelService paymentChannelService;

	public CustomsDeclarationEventListener(){
		LOGGER.debug("报关事件监听器启动");
	}
	
	@Override
	protected void handle(CustomsDeclarationEvent event) {
		PaymentResult paymentResult = event.getPaymentOrder();
		if(paymentResult == null){
			LOGGER.warn("订单对象为null,无法报关");
		}else {
			paymentChannelService.baoguan(paymentResult);
			LOGGER.info("订单[{}],报关成功:{}", paymentResult.getOrderId(), JsonUtils.toJson(paymentResult));
		}
	}

}
