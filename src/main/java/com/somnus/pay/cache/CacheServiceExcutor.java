package com.somnus.pay.cache;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CacheManager持有器
 * 
 * @author 丹青生
 *
 * @date 2015-8-25
 */
public final class CacheServiceExcutor {

	private final static Logger LOGGER = LoggerFactory.getLogger(CacheServiceExcutor.class);
	
	private final static CacheServiceExcutor INSTANCE = new CacheServiceExcutor();
	protected List<CacheManager> cacheManagers;
	protected boolean hasCacheManager;
	
	/**
	 * 注入所有可用的缓存管理器
	 * @param cacheManagers 缓存管理器
	 */
	protected static void setCacheManagers(List<CacheManager> cacheManagers){
		INSTANCE.cacheManagers = cacheManagers;
		INSTANCE.hasCacheManager = CollectionUtils.isNotEmpty(cacheManagers);
		if(INSTANCE.hasCacheManager){
			Collections.sort(cacheManagers);
		}
	}
	
	/**
	 * 根据类型和方法以及Cache注解配置获取最近(根据排序字段)的一个缓存管理器
	 * @param type 目标类名
	 * @param method 目标方法名
	 * @param cache 缓存注解
	 * @return
	 */
	protected static CacheManager getCacheManager(String type, String method, Cache cache){
		CacheManager cacheManager = null;
		if (INSTANCE.hasCacheManager) {
			for (CacheManager cm : INSTANCE.cacheManagers) {
				if(cm.support(type, method, cache)){
					cacheManager = cm;
					break;
				}
			}
		}
		return cacheManager;
	}
	
	/**
	 * 放入缓存
	 * @param key 缓存key
	 * @param value 缓存数据
	 * @param expire 过期时间(单位秒(s))
	 */
	public static void put(String key, Object value, int expire) {
		CacheManager cacheManager = getCacheManager();
		if(cacheManager != null){
			try {
				cacheManager.put(key, value, expire);
			} catch (Exception e) {
				if(LOGGER.isWarnEnabled()){
					LOGGER.warn("添加缓存失败", e);
				}
			}
		}
	}

	/**
	 * 查询缓存
	 * @param key 缓存key
	 * @return
	 */
	public static Object get(String key) {
		CacheManager cacheManager = getCacheManager();
		if(cacheManager != null){
			try {
				return cacheManager.get(key);
			} catch (Exception e) {
				if(LOGGER.isWarnEnabled()){
					LOGGER.warn("查询缓存失败", e);
				}
			}
		}
		return null;
	}

	/**
	 * 移除缓存
	 * @param keys
	 */
	public static void remove(String... keys) {
		CacheManager cacheManager = getCacheManager();
		if(cacheManager != null){
			try {
				cacheManager.remove(keys);
			} catch (Exception e) {
				if(LOGGER.isWarnEnabled()){
					LOGGER.warn("清理缓存失败", e);
				}
			}
		}
	}
	
	/**
	 * 获得最近(根据排序字段)的一个支持当前调用者的缓存管理器
	 * @return
	 */
	private static CacheManager getCacheManager(){
		CacheManager cacheManager = null;
		if (INSTANCE.hasCacheManager) {
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();
			if(stack.length >= 4){
				StackTraceElement caller = stack[3];
				cacheManager = getCacheManager(caller.getClassName(), caller.getMethodName(), null);
			}
		}
		return cacheManager;
	}

	/**
	 * CacheManager组件是否支持当前调用者
	 * @return 是否支持
	 */
	public boolean support(){
		return getCacheManager() != null;
	}
	
}
