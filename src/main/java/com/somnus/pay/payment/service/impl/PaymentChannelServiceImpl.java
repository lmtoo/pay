package com.somnus.pay.payment.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.ConfirmResult;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.service.PaymentChannelService;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandler;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandlerAdapter;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.RequestType;
import com.somnus.pay.utils.Assert;

/**
 * @description: 
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
@Service
public class PaymentChannelServiceImpl implements PaymentChannelService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentChannelServiceImpl.class);
	
	@Resource
	private PaymentChannelHandlerAdapter handlerAdapter;
	
	protected PaymentChannelHandler getHandler(RequestParameter<?, ?> parameter) {
		LOGGER.info("处理支付请求:{}", parameter);
		Assert.notNull(parameter.getChannel(), PayExceptionCode.PAY_CHANNEL_IS_NULL);
		Assert.notNull(parameter.getType(), PayExceptionCode.REQUEST_TYPE_IS_NULL);
		Assert.notNull(parameter.getData(), PayExceptionCode.PAYMENT_PARAMETER_IS_NULL);
		PaymentChannelHandler handler = handlerAdapter.getHandler(parameter);
		Assert.notNull(handler, PayExceptionCode.PAYMENT_HANDLER_NOT_FOUND);
		return handler;
	}
	
	protected String handleCallback(RequestParameter<?, String> parameter) {
		PaymentChannelHandler handler = this.getHandler(parameter);
		try {
			return handler.handle(parameter);
		} catch (Exception e) {
			LOGGER.warn("处理支付前端回调请求失败", e);
		}
		return handler.getFailedResponse(parameter);
	}

	@Override
	public String createOrder(PaymentOrder paymentOrder) {
		RequestParameter parameter = new RequestParameter(PayChannel.valueOf(paymentOrder.getThirdPayType()), RequestType.ORDER, paymentOrder);
		PaymentChannelHandler handler = handlerAdapter.getHandler(parameter);
		return (String) handler.handle(parameter);
	}

	@Override
	public String handleReturn(PayChannel channel, Map<String, String> parameter) {
		return handleCallback(new RequestParameter<Map<String, String>, String>(channel, RequestType.RETURN, parameter));
	}

	@Override
	public String handleReturn(PayChannel channel, String parameter) {
		return handleCallback(new RequestParameter<String, String>(channel, RequestType.RETURN, parameter));
	}

	@Override
	public String handleNotify(PayChannel channel, Map<String, String> parameter) {
		return handleCallback(new RequestParameter<Map<String, String>, String>(channel, RequestType.NOTIFY, parameter));
	}

	@Override
	public String handleNotify(PayChannel channel, String parameter) {
		return handleCallback(new RequestParameter<String, String>(channel, RequestType.NOTIFY, parameter));
	}

	@Override
	public String handleRefund(PayChannel channel, Map<String, String> parameter) {
		return handleCallback(new RequestParameter<Map<String, String>, String>(channel, RequestType.REFUND, parameter));
	}

	@Override
	public Map<String, String> queryOrder(PaymentOrder paymentOrder) {
		RequestParameter<String, Map<String, String>> parameter = new RequestParameter<String, Map<String,String>>();
		parameter.setChannel(PayChannel.valueOf(paymentOrder.getThirdPayType()));
		parameter.setType(RequestType.QUERY);
		parameter.setData(paymentOrder.getOrderId());
		PaymentChannelHandler handler = this.getHandler(parameter);
		Map<String, String> result = null;
		try {
			result = handler.handle(parameter);
		} catch (B5mException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.warn("查询订单支付结果失败", e);
			throw new B5mException(PayExceptionCode.QUERY_ORDER_PAY_RESULT_ERROR);
		}
		LOGGER.info("订单[{}]支付结果:{}", paymentOrder.getOrderId(), result);
		return result;
	}

	@Override
	public ConfirmResult confirmOrder(PaymentOrder paymentOrder) {
		RequestParameter<String, PaymentResult> parameter = new RequestParameter<String, PaymentResult>();
		parameter.setChannel(PayChannel.valueOf(paymentOrder.getThirdPayType()));
		parameter.setType(RequestType.CONFIRM);
		parameter.setData(paymentOrder.getOrderId());
		PaymentChannelHandler handler = this.getHandler(parameter);
		boolean result = false;
		try {
			result = handler.handle(parameter);
		} catch (B5mException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.warn("确认订单支付结果失败", e);
			throw new B5mException(PayExceptionCode.CONFIRM_ORDER_PAY_RESULT_ERROR);
		}
		LOGGER.info("订单[{}]支付结果:{}", paymentOrder.getOrderId(), result);
		return new ConfirmResult(result, parameter.getResult());
	}

	public void baoguan(PaymentResult result) {
		RequestParameter parameter = new RequestParameter(result.getChannel(), RequestType.BG, result);
		this.getHandler(parameter).handle(parameter);
	}
	
}
