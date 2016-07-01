package com.somnus.pay.payment.dao.impl;

import org.springframework.stereotype.Repository;

import com.somnus.pay.payment.dao.IPaymentOrderStatusHisDao;
import com.somnus.pay.payment.pojo.PaymentOrderStatusHis;
@Repository
public class PaymentOrderStatusHisDao extends BaseDao<PaymentOrderStatusHis>
		implements IPaymentOrderStatusHisDao {}
