package com.somnus.pay.cache.memcache;

import javax.annotation.Resource;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.cache.CacheService;

/**
 * 基于MemCache的缓存服务实现
 * 
 * @author 丹青生
 *
 * @date 2015-8-13
 */
public class MemCacheService implements CacheService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MemCacheService.class);
	
	/*
	 * 默认设置缓存有效时间10分钟
	 */
	private int expireTime = 600;
	@Resource
	private MemcachedClient memcachedClient;

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public long incr(String key, int by, long def, int exp){
		return memcachedClient.incr(key, by, def, exp);
	}
	
	public void decr(String key, int by){
		memcachedClient.decr(key, by);
	}

	@Override
	public void put(String key, Object value) {
		put(key, value, expireTime);
	}

	@Override
	public void put(String key, Object value, int expire) {
		if (StringUtils.isNotBlank(key)){
			memcachedClient.set(key, expire, value);
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("添加缓存{} , 过期时间={}秒", key, expire);
			}
		}else if(LOGGER.isDebugEnabled()){
			LOGGER.debug("无效缓存key,忽略");
		}
	}

	@Override
	public void remove(String key) {
		if(StringUtils.isNotEmpty(key)){
			memcachedClient.delete(key);
		}
	}
	
	public Object get(String key) {
		return StringUtils.isEmpty(key) ? null : memcachedClient.get(key);
	}
	
	public void clean() {
		memcachedClient.flush();
	}
	
}
