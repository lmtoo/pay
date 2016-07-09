package com.somnus.pay.payment.config.impl;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.payment.exception.PayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.payment.service.IRefreshable;

/**
 * @description: 抽象配置服务
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/11/26
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/11/26    方东白          1.0         新建作为抽象配置服务
 */
public abstract class AbstractConfigService implements IRefreshable {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractConfigService.class);
	
	private long count;
	private long lastRefreshTime;
	private String name; // 配置名称
	
	public AbstractConfigService(String name){
		this.name = name;
		this.count = 0;
		this.lastRefreshTime = 0;
	}
	
	@Override
	public void refresh(){
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("准备加载[{}]配置信息", name);
		}
		try {
			doRefresh();
		} catch (B5mException e){
			throw e;
		} catch(Exception e){
			if(LOGGER.isErrorEnabled()){
				LOGGER.error("加载[{}]配置信息失败:{}", name, e.getMessage());
			}
			throw new PayException(new StatusCode("50081010", "加载" + name + "配置信息失败"), e);
		}
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("加载[{}]配置完成", name);
		}
		count++;
		lastRefreshTime = System.currentTimeMillis();
	}
	
	@Override
	public long getLastRefreshTime() {
		return lastRefreshTime;
	}

	@Override
	public long getCount(){
		return this.count;
	}
	
	/**
	 * 配置数据重新加载
	 */
	protected abstract void doRefresh();
	
}
