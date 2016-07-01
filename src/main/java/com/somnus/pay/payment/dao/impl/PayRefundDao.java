package com.somnus.pay.payment.dao.impl;

import org.springframework.stereotype.Repository;

import com.somnus.pay.payment.dao.IPayRefundDao;
import com.somnus.pay.payment.pojo.PaymentRefund;

@Repository
public class PayRefundDao extends BaseDao<PaymentRefund> implements IPayRefundDao {

}
