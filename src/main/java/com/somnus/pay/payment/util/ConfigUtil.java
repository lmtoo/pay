package com.somnus.pay.payment.util;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <pre>
 * 获取配置信息
 * </pre>
 *
 * @author masanbao
 * @version $ ConfigUtil.java, v 0.1 2015年3月2日 下午1:40:24 masanbao Exp $
 * @since   JDK1.6
 */
public class ConfigUtil {

    protected Log logger = LogFactory.getLog(ConfigUtil.class);

    /**
     * 从bundle路径的配置文件中获得相应属性
     * @param bundle
     * @param key
     * @return
     */
    public static String getValue(String bundle, String key) {
        try {
            ResourceBundle rb = ResourceBundle.getBundle(bundle);
            return rb.getString(key);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 从"config/config.properties"配置文件中获得相应属性
     * @param key
     * @return
     */
    public static String getValue(String key) {
        return getValue("config/config",key);
    }

    /**
     * 从"config/bankInfoConfig.properties"配置文件中获得支付渠道相应属性
     * @param key
     * @return
     */
    public static String getBankInfoValue(String key) {
        return getValue("config/bankInfoConfig",key);
    }

}
