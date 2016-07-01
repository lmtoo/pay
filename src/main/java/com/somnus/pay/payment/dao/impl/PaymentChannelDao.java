package com.somnus.pay.payment.dao.impl;

import com.somnus.pay.payment.dao.IPaymentChannelDao;
import com.somnus.pay.payment.pojo.PaymentChannel;

import org.springframework.stereotype.Repository;

@Repository
public class PaymentChannelDao extends BaseDao<PaymentChannel> implements IPaymentChannelDao {

}
