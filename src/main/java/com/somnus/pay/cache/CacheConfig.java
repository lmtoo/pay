package com.somnus.pay.cache;

import java.io.Serializable;

/**
 * 缓存配置信息
 * 
 * @author 丹青生
 * 
 * @date 2015-8-25
 */
public class CacheConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private int expire;
	private String[] clean;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public String[] getClean() {
		return clean;
	}

	public void setClean(String[] clean) {
		this.clean = clean;
	}

}
