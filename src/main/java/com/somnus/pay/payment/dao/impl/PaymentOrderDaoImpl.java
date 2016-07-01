package com.somnus.pay.payment.dao.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.somnus.pay.payment.dao.PaymentOrderDao;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.utils.Assert;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
@Repository
public class PaymentOrderDaoImpl extends BaseDao<PaymentOrder> implements PaymentOrderDao {

	@Override
	public PaymentOrder get(String orderId, int source) {
		String hql = "from PaymentOrder paymentOrder where paymentOrder.orderId=? and paymentOrder.source=?";
		List<PaymentOrder> list = (List<PaymentOrder>) super.find(hql, orderId, source);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}
	
	@Override
	public PaymentOrder get(String orderId) {
		String hql = "from PaymentOrder paymentOrder where paymentOrder.orderId=?";
		List<PaymentOrder> list = (List<PaymentOrder>) super.find(hql, orderId);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public PaymentOrder get(String orderId, String userId) {
		String hql = "from PaymentOrder where orderId = ? and userId = ?";
		List<PaymentOrder> list = (List<PaymentOrder>) super.find(hql, orderId, userId);
		boolean isEmpty = CollectionUtils.isEmpty(list);
		Assert.isTrue(isEmpty || list.size() == 1, PayExceptionCode.MULTI_PAYMENT_ORDER); // 交易记录重复
		return isEmpty ? null : list.get(0);
	}


}
