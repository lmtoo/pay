package com.somnus.pay.payment.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.SwitchType;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.pojo.Switch;
import com.somnus.pay.payment.service.PaymentChannelSwitchService;
import com.somnus.pay.payment.service.SwitchService;
import com.somnus.pay.payment.thirdPay.RequestType;
import com.somnus.pay.utils.Assert;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-31<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-31       丹青生                        1.0            初始化 <br/>
 *  
 */
@Service
@Transactional
public class PaymentChannelSwitchServiceImpl implements PaymentChannelSwitchService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentChannelSwitchServiceImpl.class);
	
	@Resource
	private SwitchService switchService;
	
	@Override
	public void toggle(PayChannel channel, RequestType type) {
		String key = getKey(channel, type);
		LOGGER.info("切换[{}]开关值", key);
		Switch item = switchService.lock(key);
		if(item == null){
			LOGGER.info("开关[{}]不存在,现在创建", key);
			item = new Switch();
			item.setKey(key);
			item.setValue("true");
			item.setCreateTime(new Date());
			item.setType(SwitchType.PAYMENT_CHANNEL);
			item.setMemo(channel.getDesc() + "渠道" + type.getDesc() + "类型请求开关");
			switchService.save(item);
		}else {
			switchService.setValue(key, "false".equalsIgnoreCase(item.getValue()) ? "true" : "false");
		}
	}

	@Override
	public void setValue(PayChannel channel, RequestType type, boolean value) {
		switchService.setValue(this.getKey(channel, type), "" + value);
	}

	@Override
	public boolean getValue(PayChannel channel, RequestType type) {
		String value = switchService.getValue(this.getKey(channel, type));
		return Boolean.parseBoolean(StringUtils.defaultIfEmpty(value, "false"));
	}

	@Override
	public List<Switch> getAll() {
		return switchService.getAll(SwitchType.PAYMENT_CHANNEL);
	}

	protected String getKey(PayChannel channel, RequestType type) {
		Assert.notNull(channel, PayExceptionCode.PAY_CHANNEL_IS_NULL);
		Assert.notNull(type, PayExceptionCode.REQUEST_TYPE_IS_NULL);
		return channel.name() + ":" + type.name();
	}

	@Override
	public void toggle(String key) {
		LOGGER.info("切换[{}]开关值", key);
		Switch item = switchService.lock(key);
		Assert.notNull(item, PayExceptionCode.SWITCH_RECORD_NOT_EXIST);
		switchService.setValue(key, "false".equalsIgnoreCase(item.getValue()) ? "true" : "false");
	}

	@Override
	public QueryResult<Switch> list(Page page) {
		return switchService.list(SwitchType.PAYMENT_CHANNEL, page);
	}

	@Override
	public QueryResult<Switch> list(Page page, Map<String, Object> params){
		return switchService.list(page, params);
	}
	
}
