package com.somnus.pay.cache;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存管理器
 * 
 * @author 丹青生
 *
 * @date 2015-8-13
 */
public class CacheManager implements Comparable<CacheManager> {

	private final static Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
	
	private String[] support;
	@Resource
	private CacheService cacheService;
	/*
	 * 排序属性,越小越优先
	 */
	private int order;
	
	public void put(String key, Object value, int expire) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("添加缓存:{} , 过期时间:{}", key, expire);
		}
		cacheService.put(key, value, expire);
	}

	public Object get(String key) {
		return cacheService.get(key);
	}

	public void remove(String...keys) {
		if(keys != null && keys.length > 0){
			for (int i = 0; i < keys.length; i++) {
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("清理缓存:{}", keys[i]);
				}
				cacheService.remove(keys[i]);
			}
		}
	}

	public boolean support(String type, String method, Cache cache){
		boolean supportCache = false;
		if(support == null){
			supportCache = true;
		}else if(StringUtils.isNotEmpty(type)){
			for (int i = 0; i < support.length; i++) {
				if(StringUtils.isEmpty(support[i]) || type.contains(support[i])){
					supportCache = true;
					break;
				}
			}
		}
		return supportCache;
	}
	
	public String[] getSupport() {
		return support;
	}

	public void setSupport(String[] support) {
		this.support = support;
	}

	public CacheService getCacheService() {
		return cacheService;
	}

	public void setCacheService(CacheService cacheService) {
		this.cacheService = cacheService;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(CacheManager o) {
		return this.order - o.order;
	}
	
}
