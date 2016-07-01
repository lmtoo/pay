package com.somnus.pay.payment.dao.impl;

import org.springframework.stereotype.Repository;

import com.somnus.pay.payment.dao.IPaymentOrderStatusErrDao;
import com.somnus.pay.payment.pojo.PaymentOrderStatusErr;
@Repository
public class PaymentOrderStatusErrDao extends BaseDao<PaymentOrderStatusErr>
		implements IPaymentOrderStatusErrDao {}
