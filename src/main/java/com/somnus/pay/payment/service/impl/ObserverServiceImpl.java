package com.somnus.pay.payment.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.somnus.pay.cache.CacheServiceExcutor;
import com.somnus.pay.payment.concurrent.Task;
import com.somnus.pay.payment.concurrent.TaskExecutor;
import com.somnus.pay.payment.pojo.SystemInfo;
import com.somnus.pay.payment.service.HealthService;
import com.somnus.pay.payment.service.ObserverService;
import com.somnus.pay.utils.MoneyUtils;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-25       丹青生                        1.0            初始化 <br/>
 *  
 */
@Service
public class ObserverServiceImpl implements ObserverService, ApplicationListener<ContextRefreshedEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(ObserverServiceImpl.class);
	
	@Autowired
	private HealthService healthService;
	@Autowired
	private MemcachedClient memcachedClient;
	
	private long serverId;
	private String randomCacheKey;
	private String serverIdCacheKey = "pay_server_id_";
	private String cacheRandomKey = "pay_server_cache_random_key";
	private String systemInfoCacheKey = "systemInfo_";
	private String startTimeCacheKey = "pay_server_start_time";
	private String totalOrderCountCacheKey = "total_order_count_";
	private String totalOrderAmountCacheKey = "total_order_amount_";
	private String totalMoneyAmountCacheKey = "total_money_amount_";
	private String totalBzAmountCacheKey = "total_bz_amount_";
	private String totalOrderCountValueCacheKey = "total_order_count_value_";
	private String totalOrderAmountValueCacheKey = "total_order_amount_value_";
	private String totalMoneyAmountValueCacheKey = "total_money_amount_value_";
	private String totalBzAmountValueCacheKey = "total_bz_amount_value_";
	
	@Override
	public long getServerId() {
		return serverId;
	}

	@Override
	public void reportSystemInfo() {
		SystemInfo systemInfo = healthService.getSystemInfo();
		LOGGER.info("报告系统基本信息:[{}]", systemInfo);
		CacheServiceExcutor.put(systemInfoCacheKey + serverId, systemInfo, 900);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() != null){
			return;
		}
		serverId = memcachedClient.incr(serverIdCacheKey, 1, 1, 600); // 预计十分钟内系统启动完成
		if(serverId == 1){
			CacheServiceExcutor.put(startTimeCacheKey, System.currentTimeMillis(), Integer.MAX_VALUE);
			randomCacheKey = RandomStringUtils.randomAlphabetic(5).toLowerCase();
			LOGGER.info("本次启动随机缓存key:[{}]", randomCacheKey);
			CacheServiceExcutor.put(cacheRandomKey, randomCacheKey, Integer.MAX_VALUE);
		}
		while(StringUtils.isEmpty(randomCacheKey)){ 
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				LOGGER.warn("搜索本次启动随机缓存key时等待失败");
			}
			LOGGER.info("搜索本次启动随机缓存key");
			randomCacheKey = (String) CacheServiceExcutor.get(cacheRandomKey);
			LOGGER.info("本次启动随机缓存key:[{}]", randomCacheKey);
		}
		systemInfoCacheKey += randomCacheKey;
		totalOrderCountCacheKey += randomCacheKey;
		totalOrderAmountCacheKey += randomCacheKey;
		totalMoneyAmountCacheKey += randomCacheKey;
		totalBzAmountCacheKey += randomCacheKey;
		totalOrderCountValueCacheKey += randomCacheKey;
		totalOrderAmountValueCacheKey += randomCacheKey;
		totalMoneyAmountValueCacheKey += randomCacheKey;
		totalBzAmountValueCacheKey += randomCacheKey;
		LOGGER.info("根据key[{}]获取当前服务器序号:[{}]", serverIdCacheKey, serverId);
		final ObserverService observerService = this;
		TaskExecutor.runFixed(new Task("报告系统运行数据") { // 启动后立即报告系统信息
			
			@Override
			public void run() {
				observerService.reportSystemInfo();
			}
		}, 60000); // 每隔10分钟报告一次
	}

	@Override
	public List<SystemInfo> collectSystemInfo() {
		List<SystemInfo> list = null;
		list = new ArrayList<SystemInfo>();
		for (int i = 1; ; i++) {
			SystemInfo systemInfo = (SystemInfo) CacheServiceExcutor.get(systemInfoCacheKey + i);
			if(systemInfo == null){
				break;
			}
			list.add(systemInfo);
		}
		LOGGER.info("集群内各节点系统基本信息:", list);
		return list;
	}

	@Override
	public Date getStartTime() {
		Long startTime = (Long) CacheServiceExcutor.get(startTimeCacheKey);
		if(startTime == null){
			LOGGER.warn("没有找到集群最后一次启动时间");
			startTime = System.currentTimeMillis();
		}
		return new Date(startTime);
	}

	@Override
	public void increaseTotalOrderCount() {
		long value = memcachedClient.incr(totalOrderCountCacheKey, 1, 1, 86400); // 1天有效期
		CacheServiceExcutor.put(totalOrderCountValueCacheKey, value, 86400);
	}

	@Override
	public void increaseTotalOrderAmount(double amount) {
		int value = MoneyUtils.getIntCents(new Float(amount));
		long finalValue = memcachedClient.incr(totalOrderAmountCacheKey, value, value, 86400); // 1天有效期
		CacheServiceExcutor.put(totalOrderAmountValueCacheKey, finalValue, 86400);
	}

	@Override
	public void increaseTotalBzAmount(long amount) {
		int vlaue = new Long(amount).intValue();
		long finalValue = memcachedClient.incr(totalBzAmountCacheKey, vlaue, vlaue, 86400); // 1天有效期
		CacheServiceExcutor.put(totalBzAmountValueCacheKey, finalValue, 86400);
	}

	@Override
	public long getTotalOrderCount() {
		Long value = (Long) CacheServiceExcutor.get(totalOrderCountValueCacheKey);
		return value == null ? 0L : value;
	}

	@Override
	public double getTotalOrderAmount() {
		Long value = (Long) CacheServiceExcutor.get(totalOrderAmountValueCacheKey);
		return value == null ? 0D : value.doubleValue() / 100;
	}

	@Override
	public long getTotalBzAmount() {
		Long value = (Long) CacheServiceExcutor.get(totalBzAmountValueCacheKey);
		return value == null ? 0L : value;
	}

	@Override
	public void increaseTotalMoneyAmount(double amount) {
		int value = MoneyUtils.getIntCents(new Float(amount));
		long finalValue = memcachedClient.incr(totalMoneyAmountCacheKey, value, value, 86400); // 1天有效期
		CacheServiceExcutor.put(totalMoneyAmountValueCacheKey, finalValue, 86400);
	}

	@Override
	public double getTotalMoneyAmount() {
		Long value = (Long) CacheServiceExcutor.get(totalMoneyAmountValueCacheKey);
		return value == null ? 0D : value.doubleValue() / 100;
	}

}
