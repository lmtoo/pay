package com.somnus.pay.payment.service.impl;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.LockMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.dao.CallbackDao;
import com.somnus.pay.payment.enums.CallbackStatus;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Callback;
import com.somnus.pay.payment.pojo.Callback.CallbackId;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.CallbackService;
import com.somnus.pay.utils.Assert;

/**
 *  @description: 支付渠道回调流水service<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-25       丹青生                        1.0            初始化 <br/>
 *  
 */
@Service
@Transactional
public class CallbackServiceImpl implements CallbackService {

	private final static Logger LOGGER = LoggerFactory.getLogger(CallbackServiceImpl.class);
	
	@Resource
	private CallbackDao callbackDao;
	
	@Override
	public void save(Callback... callbacks) {
		for (int i = 0; i < callbacks.length; i++) {
			if(callbacks[i] == null){
				continue;
			}
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("保存支付回调：{}", JsonUtils.toJson(callbacks[i]));
			}
			validateId(callbacks[i].getId());
			Callback exist = this.get(callbacks[i].getId());
			if(exist != null){
				if(exist.getStatus() == CallbackStatus.SUCCESS){
					LOGGER.warn("收到与系统中已存在的支付回调重复的通知,且前一次通知已处理成功,忽略本次回调:{}", JsonUtils.toJson(exist));
					exist.setMemo("收到重复的支付回调,已忽略:" + JsonUtils.toJson(callbacks[i]));
					callbacks[i] = exist;
				}else {
					LOGGER.warn("覆盖重复的支付回调通知:{}", JsonUtils.toJson(exist));
				}
				this.update(callbacks[i]);
			}else {
				if(callbacks[i].getCreateTime() == null){
					callbacks[i].setCreateTime(new Date());
				}
				callbackDao.save(callbacks[i]);
			}
		}
	}

	@Override
	public void update(Callback callback) {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("更新支付回调：{}", JsonUtils.toJson(callback));
		}
		validateId(callback.getId());
		callback.setUpdateTime(new Date());
		callbackDao.update(callback);
	}

	@Override
	public Callback get(CallbackId id) {
		validateId(id);
		return callbackDao.get(Callback.class, id, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	public QueryResult<Callback> list(Page page,Map<String,Object> params) {
		return callbackDao.list(page,params);
	}

	@Override
	public void changeStatus(CallbackId id, CallbackStatus status, String memo) {
		Assert.notNull(status, PayExceptionCode.QUERY_PARAMETER_IS_NULL);
		validateId(id);
		Callback callback = this.get(id);
		Assert.notNull(callback, PayExceptionCode.EXPECTED_RECORD_IS_NOT_EXIST);
		if(callback.getStatus() == CallbackStatus.SUCCESS){
			LOGGER.warn("回调已处理成功,不允许重新覆盖状态");
			return;
		}
		callback.setStatus(status);
		callback.setMemo(StringUtils.trimToEmpty(memo));
		this.update(callback);
	}
	
	protected void validateId(CallbackId id) {
		Assert.notNull(id, PayExceptionCode.CALLBACK_ID_PARAMETER_IS_NULL);
		id.validate();
	}

	@Override
	public boolean exist(CallbackId id) {
		return this.get(id) != null;
	}

}
