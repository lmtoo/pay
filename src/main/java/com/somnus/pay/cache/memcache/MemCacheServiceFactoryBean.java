package com.somnus.pay.cache.memcache;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * MemCacheService工厂Bean
 * 
 * @author 丹青生
 *
 * @date 2015-8-17
 */
public class MemCacheServiceFactoryBean implements FactoryBean<MemCacheService>, InitializingBean, DisposableBean {

	private final static Logger LOGGER = LoggerFactory.getLogger(MemCacheServiceFactoryBean.class);
	
	/*
	 * 默认设置缓存有效时间10分钟
	 */
	private int expireTime = 600;
	private boolean locallyManaged = true;;
	private long shutdwonTimeout = -1;
	private TimeUnit shutdwonTimeUnit = TimeUnit.MILLISECONDS;
	@Resource
	private MemcachedClient memcachedClient;
	private MemCacheService memCacheService;
	
	@Override
	public void destroy() throws Exception {
		if(locallyManaged){
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("正在关闭MemcacheService");
			}
			memcachedClient.shutdown(shutdwonTimeout, shutdwonTimeUnit);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("正在初始化MemcacheService");
		}
		memCacheService = new MemCacheService();
		memCacheService.setExpireTime(expireTime);
		memCacheService.setMemcachedClient(memcachedClient);
	}

	@Override
	public MemCacheService getObject() throws Exception {
		return memCacheService;
	}

	@Override
	public Class<?> getObjectType() {
		return MemCacheService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public void setLocallyManaged(boolean locallyManaged) {
		this.locallyManaged = locallyManaged;
	}

	public void setShutdwonTimeout(long shutdwonTimeout) {
		this.shutdwonTimeout = shutdwonTimeout;
	}

	public void setShutdwonTimeUnit(TimeUnit shutdwonTimeUnit) {
		this.shutdwonTimeUnit = shutdwonTimeUnit;
	}

}
