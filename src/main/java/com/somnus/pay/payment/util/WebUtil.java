package com.somnus.pay.payment.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 将request和response放入本地线程堆栈
 */

/**
 * 通过SpringMVC获取request和response
 */
public class WebUtil {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static Map<String, String> getParamMap(){
        HttpServletRequest request = getRequest();
        Map<String, String> params = null;
        if(request != null) {
            params = getParamMap(request.getParameterMap());
        }
        return params;
    }

    public static Map<String, String> getParamMap(Map<String, ?> paramMap){
        Map<String, String> params = null;
        if (paramMap != null && !paramMap.isEmpty()) {
            params = new HashMap<String, String>(paramMap.size());
            for (Map.Entry<String, ?> item : paramMap.entrySet()) {
                String name = item.getKey();
                String value = "";
                if (item.getValue() instanceof String) {
                    value = (String) item.getValue();
                } else {
                    value = ((String[]) item.getValue())[0];
                }
                params.put(name, value);
            }
        }
        return params;
    }

}
