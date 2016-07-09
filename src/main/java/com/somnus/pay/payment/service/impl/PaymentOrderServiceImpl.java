package com.somnus.pay.payment.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.somnus.pay.payment.dao.PaymentOrderDao;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.PaymentOrderService;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
@Service
public class PaymentOrderServiceImpl implements PaymentOrderService {

	@Resource
	private PaymentOrderDao paymentOrderDao;
	
	@Override
	public PaymentOrder get(String orderId, int source) {
		return paymentOrderDao.get(orderId, source);
	}
	
	@Override
	public PaymentOrder get(String orderId) {
		return paymentOrderDao.get(orderId);
	}

	@Override
	public PaymentResult convert(PaymentOrder paymentOrder) {
		PaymentResult paymentResult = new PaymentResult();
		paymentResult.setOrderId(paymentOrder.getOrderId());
		paymentResult.setChannel(PayChannel.getPayType(paymentOrder.getThirdPayType()));
		paymentResult.setThirdTradeNo(paymentOrder.getThirdTradeNo());
		paymentResult.setPrice(paymentOrder.getAmount());
		paymentResult.setIsCombined(paymentOrder.getIsCombined() ? 1 : 0);
		paymentResult.setChannel(PayChannel.valueOf(paymentOrder.getThirdPayType()));
		paymentResult.setStatus(paymentOrder.getStatus());
		return paymentResult;
	}

	@Override
	public PaymentOrder get(String orderId, String userId) {
		return paymentOrderDao.get(orderId, userId);
	}
	
	@Override
	public QueryResult<PaymentOrder> list(Page page,Map<String,Object> params) {
		return paymentOrderDao.list(page == null ? new Page() : page,params);
	}
}
