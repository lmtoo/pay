package com.somnus.pay.cache;


/**
 * 缓存服务接口
 * 
 * @author 丹青生
 *
 * @date 2015-8-13
 */
public interface CacheService {

	/**
	 * 添加缓存
	 * @param key
	 * @param value
	 */
	public void put(String key, Object value);

	/**
	 * 添加缓存
	 * @param key
	 * @param value
	 * @param expire 缓存自动失效时间(以秒为单位)
	 */
	public void put(String key, Object value, int expire);

	public Object get(String key);
	
	public void remove(String key);

	public void clean();
	
}
