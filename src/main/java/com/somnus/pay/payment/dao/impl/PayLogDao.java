package com.somnus.pay.payment.dao.impl;

import org.springframework.stereotype.Repository;

import com.somnus.pay.payment.dao.IPayLogDao;
import com.somnus.pay.payment.pojo.PaymentLog;

@Repository
public class PayLogDao extends BaseDao<PaymentLog> implements IPayLogDao {

}
