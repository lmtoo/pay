package com.somnus.pay.payment.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * @description: 获取核心异常信息描述
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2016/1/27
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2016/1/27    qingshu       1.0            递归递归调用获取核心异常信息描述
 */
public class ExceptionUtils {

    public static String getCauseMessage(Throwable e){
        String message = "";
        if(e != null){
            message = e.getMessage();
            String next = getCauseMessage(e.getCause());
            if(StringUtils.isNotEmpty(next)){
                message += "\nCaused by " + next;
            }
        }
        return message;
    }

}
