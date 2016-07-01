package com.somnus.pay.payment.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.pay.payment.dao.SwitchDao;
import com.somnus.pay.payment.enums.SwitchType;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.pojo.Switch;
import com.somnus.pay.payment.service.SwitchService;
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
public class SwitchServiceImpl implements SwitchService {

	private final static Logger LOGGER = LoggerFactory.getLogger(SwitchServiceImpl.class);
	
	@Resource
	private SwitchDao switchDao;
	
	@Override
	public void setValue(String key, String value) {
		LOGGER.info("设置[{}]开关的值:{}", key, value);
		Switch item = this.lock(key);
		Assert.notNull(item, PayExceptionCode.SWITCH_RECORD_NOT_EXIST);
		item.setValue(value);
		item.setUpdateTime(null);
		switchDao.saveOrUpdate(item);
	}

	@Override
	public String getValue(String key) {
		Switch item = this.get(key);
		return item == null ? null : item.getValue();
	}

	@Override
	public List<Switch> getAll() {
		return switchDao.getAll();
	}

	@Override
	public Switch get(String key) {
		if(StringUtils.isEmpty(key)){
			return null;
		}
		return switchDao.getById(key);
	}

	@Override
	public Switch lock(String key) {
		if(StringUtils.isEmpty(key)){
			return null;
		}
		return switchDao.get(Switch.class, key, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	public void save(Switch item) {
		switchDao.saveOrUpdate(item);
	}

	@Override
	public List<Switch> getAll(SwitchType type) {
		return switchDao.getAll(type);
	}

	@Override
	public QueryResult<Switch> list(SwitchType type, Page page) {
		return switchDao.list(type, page == null ? new Page() : page);
	}

	@Override
	public QueryResult<Switch> list(Page page, Map<String, Object> params){
		return switchDao.list(page == null ? new Page() : page, params);
	}

}
