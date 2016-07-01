package com.somnus.pay.payment.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * UA枚举 请求终端
 * @description: 
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-11
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-11       丹青生                               1.0            初始化
 
 */
public enum UAType {

    PC(0,"PC"),
    WX_BROWSER(1,"微信"),
    B5M_APP(2,"帮我买"),
    KOREA_APP(3,"帮韩品");

    private Integer value;
    private String message;

    private UAType(Integer value,String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取UA相关描述
     * @param ua
     * @return
     */
    public static Integer getUAType(String ua) {
        Integer res = 0;
        if(StringUtils.isNotBlank(ua)){
            String temp = ua.toLowerCase();
            if (temp.indexOf("micromessenger") != -1){
                res = WX_BROWSER.value;
            }
            if (temp.indexOf("b5m") != -1) {
                if (temp.indexOf("appsource=korea") != -1) {
                    res = KOREA_APP.value;
                }else{
                    res = B5M_APP.value;
                }
            }
        }
        return res;
    }

    /**
     * 判断是否是APP
     * @param value
     * @return
     */
    public static boolean isAPPType(Integer value) {
        boolean res = false;
        if((null != value) && (KOREA_APP.value == value || B5M_APP.value == value)  ){
            res = true ;
        }
        return res;
    }

    /**
     * 判断是否是IOS平台
     * @param ua
     * @return
     */
    public static boolean isIOSPlatform(String ua) {
        boolean res = false;
        if(StringUtils.isNotBlank(ua)){
            String temp = ua.toLowerCase();
            if (temp.toLowerCase().indexOf("iphone") != -1 || temp.toLowerCase().indexOf("ipad") != -1) {// 区别 ios和 andriod的参数
                res = true;
            }
        }
        return res;
    }

}
