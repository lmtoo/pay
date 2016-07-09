package com.somnus.pay.payment.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.payment.pojo.Msg;

/**
 * @Author 尹正飞
 * @Version 2013-4-25 上午10:30:38
 **/

@Controller
public class BaseController {

    protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 公用页面跳转
     * @param method	跳转方法名
     * @return
     */
    @RequestMapping("jump")
    @SuppressWarnings("unchecked")
    public String forward(String[] method, HttpServletRequest req, Model model) {
        Enumeration<String> enu = req.getParameterNames();
        String key;
        while (enu.hasMoreElements()) {
            key = enu.nextElement();
            model.addAttribute(key, req.getParameter(key));
        }
        return ArrayUtils.isEmpty(method) ? "" : method[0];
    }

    /**
     * 将request 中的 请求参数 重新拼装成Map<String,String>
     * @param req
     * @return Map<String,String>
     */
    @SuppressWarnings("rawtypes")
    public Map<String, String> requestParams2Map(HttpServletRequest req) {
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = req.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i]
                                                                             + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 统一错误处理
     * @param msg
     */
    @RequestMapping("data/info")
    @JsonResult(desc = "统一错误处理")
    public Msg error(Msg msg) {
    	return msg;
    }

}
