package com.somnus.pay.payment.event;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.enums.CallbackStatus;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Callback.CallbackId;
import com.somnus.pay.payment.pojo.ConfirmResult;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.service.CallbackService;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.payment.service.PaymentChannelService;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.RequestType;
import com.somnus.pay.payment.util.SpringContextUtil;
import com.somnus.pay.utils.Assert;

/**
 *  @description: 支付回调等待处理事件监听器<br/>
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
public class CallbackProcessEventListener extends AsyncEventListener<CallbackProcessEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(CallbackProcessEventListener.class);
	
	@Resource
	private CallbackService callbackService;
	@Resource
	private PaymentChannelService paymentChannelService;
	@Resource
	private IPayService payService;
	
	public CallbackProcessEventListener(){
		LOGGER.debug("支付回调处理事件监听器启动");
	}
	
	@Override
	protected void handle(CallbackProcessEvent event) {
		PaymentResult[] paymentResults = event.getPaymentResults();
		RequestParameter<?, ?> parameter = event.getRequestParameter();
		PayChannel channel = parameter.getChannel();
		RequestType type = parameter.getType();
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("订单[{}]支付回调[{}]通知等待处理:{}", new Object[]{channel, type, JsonUtils.toJson(paymentResults)});
		}
		for (int i = 0; i < paymentResults.length; i++) {
			PaymentResult paymentResult = paymentResults[i];
			if(paymentResult != null && PaymentOrderType.SCCUESS.getValue().equals(paymentResult.getStatus())){
				CallbackId id = new CallbackId(paymentResult.getOrderId(), channel, type);
				try {
					PaymentOrder paymentOrder = new PaymentOrder();
					paymentOrder.setOrderId(paymentResult.getOrderId());
					paymentOrder.setThirdPayType(channel.getValue());
					ConfirmResult confirmResult = paymentChannelService.confirmOrder(paymentOrder);
					if(confirmResult.isSupport()){
						boolean success = confirmResult.getResult() != null;
						success = success && PaymentOrderType.SCCUESS.getValue().equals(confirmResult.getResult().getStatus());
						Assert.isTrue(success, PayExceptionCode.PAYMENT_RESULT_ERROR);
					}
					payService.handleCallback(parameter, paymentResult);
					callbackService.changeStatus(id, CallbackStatus.SUCCESS, "回调处理成功");
					SpringContextUtil.getApplicationContext().publishEvent(new PaySuccessEvent(this, paymentResult));
				} catch (Exception e) {
					String message = "订单[" + paymentResult.getOrderId() + "]回调处理失败";
					LOGGER.warn(message, e);
					callbackService.changeStatus(id, CallbackStatus.FAILURE, "回调处理失败:" + e.getMessage());
					throw (e instanceof B5mException) ? ((B5mException) e) : new PayException(e);
				}
			}
		}
	}

}
