package com.somnus.pay.payment.util;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * MemCached 工具类   备注： 还需优化配置文件
 * @author jianyuanyang
 *
 */
public class MemCachedUtil {

    protected static Log logger = LogFactory.getLog(MemCachedUtil.class);

    //默认设置缓存有效时间 1 天 单位s
    public final static int        DEFAULT_CACHE_TIME = 60 * 60 * 24;   

    private static MemCachedUtil   _this              = null;

    private static MemcachedClient memcachedClient;

    /**
     * 初始化 MemCacheUtil 单例 对象
     * @return
     */
    public static synchronized MemCachedUtil getInstance() {
        if (_this == null) {
            _this = new MemCachedUtil();
            _this.getMemCacheInstance();
        }
        return _this;
    }

    /**
     * 利用 spring 上下文 根据beanName 获取memCahcheClient 实体
     */
    public void getMemCacheInstance() {
        if (null == memcachedClient) {
            memcachedClient = (MemcachedClient) SpringContextUtil.getBean("memcachedClient");
        }
    }

    /**
     * 默认设置memcache 缓存
     * @param key
     *           缓存的key
     * @param value
     *           缓存的value  
     * @return
     */
    public static void setCache(String key, Object value) {
        setCache(key, value, DEFAULT_CACHE_TIME);
    }

    /**
     * 设置memcache 缓存包括有效时间
     * @param key
     *           缓存的key
     * @param value
     *           缓存的value
     * @param exp
     *           缓存的有效时间
     * @return 
     */
    @SuppressWarnings("static-access")
    public static void setCache(String key, Object value, int exp) {
        if (StringUtils.isNotBlank(key)) {
            try {
                MemCachedUtil.getInstance().memcachedClient.set(key, exp, value);
            } catch (Exception e) {
                logger.error("MemCachedUtil setCache error:",e);
            }
        }
    }

    /**
     * 获取缓存对象
     *
     * @param key
     *           缓存的key
     * @return  value
     */
    @SuppressWarnings("static-access")
    public static Object getCache(String key) {
        Object result = null;
        if (StringUtils.isNotBlank(key)) {
            result = MemCachedUtil.getInstance().memcachedClient.get(key);
        }
        return result;
    }

    /**
     * 清空某缓存对象
     *
     * @param key
     *          缓存的key
     * @return
     */
    @SuppressWarnings("static-access")
    public static void cleanCache(String key) {
        MemCachedUtil.getInstance().memcachedClient.delete(key);
    }

    /**
     * 清空所有缓存对象
     * @return
     */
    @SuppressWarnings("static-access")
    public static void cleanAllRemoteCache() {
        MemCachedUtil.getInstance().memcachedClient.flush();
    }
    
    /**
     * <pre>
     * 自动统计数量，步长为1
     * </pre>
     *
     * @param key
     */
    @SuppressWarnings("static-access")
    public static void autoIncrease(String key) {
        MemCachedUtil.getInstance().memcachedClient.incr(key, 1);
    }
    
    /**
     * <pre>
     * 自动减少数量，步长为1
     * </pre>
     *
     * @param key
     */
    @SuppressWarnings("static-access")
    public static void autoDecrease(String key) {
        MemCachedUtil.getInstance().memcachedClient.decr(key, 1);
    }

    /**
     * 设置memcache 缓存包括有效时间
     * @param key
     *           缓存的key
     * @param value
     *           缓存的value
     * @param exp
     *           缓存的有效时间
     * @return
     */
    public static boolean addCache(String key, Object value, int exp) {
        if (StringUtils.isNotBlank(key)) {
            try {
                return (MemCachedUtil.getInstance().memcachedClient.add(key, exp, value)).getStatus().isSuccess();
            } catch (Exception e) {
                logger.error("MemCachedUtil addCache error:",e);
            }
        }
        return false;
    }

}