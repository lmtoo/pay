package com.somnus.pay.payment.util;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.somnus.pay.exception.B5mException;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.utils.Assert;

/**
 * 
 * @description: HTTP请求工具类
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-22
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-22       丹青生                               1.0            初始化
 
 */
public class PaymentHttpUtils {

    private static final Logger log = LoggerFactory.getLogger(PaymentHttpUtils.class);

    public static String notity(String url, Object parameter){
        String response = null;
        Map<String, String> param;
        try {
        	param = HttpClientUtils.buildParameter(parameter);
        	response = HttpClientUtils.get(url, param);
            log.info("PaymentHttpUtils notity response:{}", response);
            Map<String,Object> msg = JSON.parseObject(response, Map.class);
            boolean res = Boolean.TRUE.equals(msg.get("ok")) || StatusCode.SUCCESS_CODE.equals(msg.get("code"));
            Assert.isTrue(res,PayExceptionCode.HTTP_NOTIFY_ERROR);
        }catch (B5mException e) {
            log.error("请求HTTP服务[" + url + "]失败", e);
            throw e;
        } catch (Exception e) {
            log.error("请求HTTP服务[" + url + "]失败", e);
            throw new PayException(PayExceptionCode.HTTP_NOTIFY_ERROR, e);
        }
        return response;
    }

    /**
     * 将 java Object 转化 拼接参数
     * 例如：beanNum=0&amount=0.0&memo=帮币兑换帮钻&userId=123&orderId=234
     * 
     * @param obj
     * @return
     */
    private static String getParametersByObjectAndGet(Object obj, String enc) {
        if (null == obj)
            return StringUtils.EMPTY;
        StringBuffer strBuffer = new StringBuffer();
        Map<String, String> map = new HashMap<String, String>();
        try {
            map = HttpClientUtils.buildParameter(obj);
            for (String key : map.keySet()) {
                String value = (map.get(key) == null ? "" : map.get(key));
                if (!"class".equals(key) && StringUtils.isNotBlank(value)) {
                    value = URLEncoder.encode(value, enc);
                    strBuffer.append(key + "=" + value + "&");
                }
            }
        } catch (Exception e) {
            log.error("PaymentHttpUtils getParametersByObject has error", e);
            return StringUtils.EMPTY;
        }
        String parameterUrl = strBuffer.toString();
        return parameterUrl.substring(0, parameterUrl.length() - 1);
    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param paramMap 请求参数数组
     * @param method 提交方式。两个值可选：post、get
     * @param action 提交URL
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> paramMap, String method, String action) {
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        sbHtml.append("<form id=\"submitForm\" name=\"submitForm\" action=\"" + action + "\" method=\"" + method + "\">");
        for (Entry<String, String> item : paramMap.entrySet()) {
            String name = item.getKey();
            String value = item.getValue();
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        sbHtml.append("<input type=\"submit\" value=\"submit\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['submitForm'].submit();</script>");
        return sbHtml.toString();
    }

    /**
     * 直接跳转url链接
     * @param url
     * @return
     */
    public static String buildDirectUrl(String url) {
        StringBuilder sbHtml = new StringBuilder();
        sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        sbHtml.append("<script>window.location.href='"+url+"'</script>");
        return sbHtml.toString();
    }


    /**
     * 组get方式url地址
     * @param url
     * @param parameterObj
     * @param enc
     * @return
     */
    public static String createGetURL(String url,Object parameterObj,String enc){
        String response = StringUtils.EMPTY;
        if (StringUtils.isBlank(url))
            return response;
        StringBuffer strtTotalURL = new StringBuffer(url);
        if (StringUtils.isBlank(enc))
            enc = Consts.UTF_8.toString();
        if (strtTotalURL.indexOf("?") == -1) {
            if (null != parameterObj)
                strtTotalURL.append("?").append(getParametersByObjectAndGet(parameterObj, enc));
        } else {
            if (null != parameterObj)
                strtTotalURL.append("&").append(getParametersByObjectAndGet(parameterObj, enc));
        }
        url = strtTotalURL.toString();
        log.warn("PaymentHttpUtils urlGet Request url:[{}]",url);
        return url;
    }

}
