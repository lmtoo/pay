package com.somnus.pay.payment.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 相关获取域名提取工具类
 * Copyright 2011-2016 B5M.COM. All rights reserved
 * @author: gongliu
 * @version: 1.0
 * @createdate: 2016/4/22
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2016/4/22    gongliu       1.0            相关获取域名提取工具类
 */
public class PageCommonUtil {

    private final static Logger logger = LoggerFactory.getLogger(PageCommonUtil.class);

    /**
     * 获取项目访问的根路径
     * @param request
     * @param isReturn
     * @return
     */
    public static String getRootPath(HttpServletRequest request, boolean isReturn) {
        String rootPath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() != 80 ? ":" + request.getServerPort() : "") + request.getContextPath();
        if(isReturn && (rootPath.contains("stage.com") || rootPath.contains("prod.com"))) {
            rootPath = StringUtils.isBlank(Constants.PAY_BACK_DOMAIN) ?
                    Constants.PAY_ONLINE_BACK_DOMAIN : Constants.PAY_BACK_DOMAIN;
        }
        return rootPath;
    }

    /**
     * 获取前端最新版本号
     * @return
     */
    public static String getFrontVersionTimestamp() {
        try {
            Object version = MemCachedUtil.getCache(Constants.MEMCACHE_PAY_FRONTTIMESTAMP);
            if(null == version || StringUtils.isBlank(version.toString())){
                String result = HttpClientUtils.get(Constants.FRONT_TIMESTAMP_URL);
                JSONObject json = JSON.parseObject(result);
                if ("0".equals(json.getString("code")) && StringUtils.isNotBlank(json.getString("data"))) {
                    MemCachedUtil.setCache(Constants.MEMCACHE_PAY_FRONTTIMESTAMP, json.getString("data"));
                    return json.getString("data");
                }
            }else{
                return version.toString();
            }
        } catch (Exception e) {
            if(logger.isWarnEnabled()){
                logger.warn("getFrontVersionTimestamp exception.",e);
            }
        }
        return Constants.FRONT_TIMESTAMP;
    }


}
