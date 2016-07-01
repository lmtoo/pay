package com.somnus.pay.cache.sample;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.cache.CacheService;

/**
 * 简单的基于HashMap的缓存
 * 
 * @author 丹青生
 *
 * @date 2015-8-25
 */
public class MemoryCache implements CacheService {

	private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCache.class);
	
	private Map<String, Object> cache = new ConcurrentHashMap<String, Object>();
	
	@Override
	public void put(String key, Object value) {
		if (StringUtils.isNotBlank(key)){
			cache.put(key, value);
		}else if(LOGGER.isDebugEnabled()){
			LOGGER.debug("无效缓存key,忽略");
		}
	}

	@Override
	public void put(String key, Object value, int expire) {
		this.put(key, value); // 暂不支持自动过期
	}

	@Override
	public Object get(String key) {
		return key == null ? null : cache.get(key);
	}

	@Override
	public void remove(String key) {
		if(key != null){
			cache.remove(key);
		}
	}

	@Override
	public void clean() {
		cache.clear();
	}

}
