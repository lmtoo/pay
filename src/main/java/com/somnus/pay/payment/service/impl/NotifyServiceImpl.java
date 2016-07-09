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
import com.somnus.pay.payment.dao.NotifyDao;
import com.somnus.pay.payment.enums.NotifyStatus;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Notify;
import com.somnus.pay.payment.pojo.Notify.NotifyId;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.NotifyService;
import com.somnus.pay.utils.Assert;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
@Service
@Transactional
public class NotifyServiceImpl implements NotifyService {

	private final static Logger LOGGER = LoggerFactory.getLogger(NotifyServiceImpl.class);
	
	@Resource
	private NotifyDao notifyDao;
	
	@Override
	public void save(Notify notify) {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("保存支付通知记录:{}", JsonUtils.toJson(notify));
		}
		Notify exist = this.lock(notify.getId());
		if(exist == null){
			notify.setCreateTime(new Date());
		}else if(exist != notify){
			Assert.isTrue(exist.getStatus() != NotifyStatus.SUCCESS, PayExceptionCode.NOTIFY_CLIENT_RECORD_REPEAT);
		}
		notify.setUpdateTime(new Date());
		notifyDao.saveOrUpdate(notify);
	}

	@Override
	public Notify lock(NotifyId id) {
		return notifyDao.get(Notify.class, id, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	public void update(Notify notify) {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("更新支付通知记录:{}", JsonUtils.toJson(notify));
		}
		notifyDao.update(notify, LockMode.PESSIMISTIC_WRITE);
	}

	@Override
	public void changeStatus(NotifyId id, NotifyStatus status, String data, String memo) {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("更新订单[{}]支付通知记录状态:{}, 数据:{}, 备注:{}", new Object[]{id, status, data, memo});
		}
		Notify notify = this.lock(id);
		Assert.notNull(notify, PayExceptionCode.NOTIFY_CLIENT_RECORD_NOT_FOUND);
		notify.setStatus(status);
		if(StringUtils.isNotEmpty(data)){
			notify.setData(data);
		}
		notify.setMemo(memo);
		this.update(notify);
	}

	@Override
	public QueryResult<Notify> list(Page page,Map<String,Object> params) {
		return notifyDao.list(page == null ? new Page() : page,params);
	}

}
