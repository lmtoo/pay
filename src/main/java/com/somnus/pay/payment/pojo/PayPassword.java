package com.somnus.pay.payment.pojo;


import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description: 支付密码POJO 
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-2
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-2       丹青生                               1.0            初始化
 */
@Entity
@Table(name = "t_user_payment_paypassword")
public class PayPassword  extends com.somnus.pay.payment.model.PayPassword {

	private final static Logger logger = LoggerFactory.getLogger(PayPassword.class);

	private static final long serialVersionUID = 1L;

	@Id
	@Override
	public String getId() {
		return super.getId();
	}

	@Column(name = "user_id")
	@Override
	public String getUserId() {
		return super.getUserId();
	}

	@Column(name = "pay_password")
	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Column(name = "is_secret")
	@Override
	public int getIsSecret() {
		return super.getIsSecret();
	}

	@Column(name = "free_quota")
	@Override
	public Integer getFreeQuota() {
		return super.getFreeQuota();
	}

	@Column(name = "free_quota_rmb")
	@Override
	public Integer getFreeQuotaRmb() {
		return super.getFreeQuotaRmb();
	}

	@Column(name = "add_time")
	@Override
	public Date getAddTime() {
		return super.getAddTime();
	}

	@Column(name = "update_time")
	@Override
	public Date getUpdateTime() {
		return super.getUpdateTime();
	}

	/**
	 * 转换成model的PayPassword
	 * @return
	 */
	public com.somnus.pay.payment.model.PayPassword convert() {
		com.somnus.pay.payment.model.PayPassword payPassword = new com.somnus.pay.payment.model.PayPassword();
		try {
			BeanUtils.copyProperties(payPassword, this);
		} catch (Exception e) {
			logger.warn("PayPassword convert error",e);
		}
		return payPassword;
	}
}
