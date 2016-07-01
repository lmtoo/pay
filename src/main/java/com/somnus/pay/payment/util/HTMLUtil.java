package com.somnus.pay.payment.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 组返回HTML信息的工具类
 */
public class HTMLUtil {

    private static Log logger  = LogFactory.getLog(HTMLUtil.class);
    public static final String ENCODING = "utf-8";
    public static final String ENCODING_GBK = "gbk";
    public static final String REQUEST_METHOD_POST = "post";
    public static final String REQUEST_METHOD_GET = "get";

    /**
     * 构造直接浏览器地址请求html
     *
     * @param action
     *            表单提交地址
     * @param hiddens
     *            以MAP形式存储的表单键值
     * @return 构造好的HTTP
     */
    public static String createHtmlHref(String action, Map<String, Object> hiddens, String encoding) {
        encoding = StringUtils.isNotBlank(encoding)?encoding:ENCODING;
        String url = action + "?";
        Set<Map.Entry<String, Object>> set = hiddens.entrySet();
        Iterator<Map.Entry<String, Object>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> ey = it.next();
            String key = ey.getKey();
            String value = ey.getValue().toString();
            url += key+"="+value+"&";
        }
        url = url.substring(0,url.length() -1);
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding+"\" />");
        sbHtml.append("<script>window.location.href=\"" + url  + "\"</script>");
        return sbHtml.toString();
    }

    /**
     * 构造直接浏览器地址请求html
     *
     * @param action
     *            表单提交地址
     * @param hiddens
     *            以MAP形式存储的表单键值
     * @return 构造好的HTTP
     */
    public static String createHtml(String action, Map<String, Object> hiddens, String encoding, String method) {
        encoding = StringUtils.isNotBlank(encoding)?encoding: ENCODING;
        method = StringUtils.isNotBlank(method)?REQUEST_METHOD_GET: REQUEST_METHOD_POST;
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset="
                + encoding + "\" />");
        sbHtml.append("<form id=\"payFormAutoSubmit\" name=\"payFormAutoSubmit\" action=\""
                + action + "\" method=\"" + method + "\">");
        Set<Map.Entry<String, Object>> set = hiddens.entrySet();
        Iterator<Map.Entry<String, Object>> it = set.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> ey = it.next();
            String name = ey.getKey();
            String value = ey.getValue().toString();
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        sbHtml.append("<input type=\"submit\" value=\"payFormAutoSubmit\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['payFormAutoSubmit'].submit();</script>");
        return sbHtml.toString();
    }


    /**
     * 构造HTTP POST交易表单的方法示例
     *
     * @param action
     *            表单提交地址
     * @param hiddens
     *            以MAP形式存储的表单键值
     * @return 构造好的HTTP POST交易表单
     */
    public static String createSubmitHtml(String action, Map<String, String> hiddens,String actionType,String encoding) {
        actionType = StringUtils.isNotBlank(actionType)?actionType:"post";
        encoding = StringUtils.isNotBlank(encoding)?encoding:"UTF-8";
        StringBuffer sf = new StringBuffer();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding+"\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"" + action + "\" method=\"" + actionType + "\">");
        if (null != hiddens && 0 != hiddens.size()) {
            Set<Map.Entry<String, String>> set = hiddens.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> ey = it.next();
                String key = ey.getKey();
                String value = ey.getValue();
                sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\""
                        + value + "\"/>");
            }
        }
        sf.append("</form>");
        sf.append("</body>");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.all.pay_form.submit();");
        sf.append("</script>");
        sf.append("</html>");
        return sf.toString();
    }

    /**
     * 获取请求参数Str
     * @param obj
     * @param enc
     * @return
     */
    public static String createParamsStr(Object obj, String enc) {
        if (null == obj)
            return StringUtils.EMPTY;

        StringBuffer strBuffer = new StringBuffer();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = Object2Map(obj);
            for (String key : map.keySet()) {
                String value = (map.get(key) == null ? "" : map.get(key).toString());
                if (!"class".equals(key) && StringUtils.isNotBlank(value)) {
                    value = URLEncoder.encode(value, enc);
                    strBuffer.append(key + "=" + value + "&");
                }
            }
        } catch (Exception e) {
            logger.error("createParamsStr has Exception---", e);
            return StringUtils.EMPTY;
        }
        String parameterUrl = strBuffer.toString();
        return parameterUrl.substring(0, parameterUrl.length() - 1);
    }

    /**
     * 将对象属性转换成Map形式
     * @param obj
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Map Object2Map(Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (null == obj)
            return MapUtils.EMPTY_MAP;

        if (obj instanceof Map)
            return (Map) obj;
        return BeanUtils.describe(obj);
    }

    /**
     * 获得前端js脚本的执行输出结果
     * 默认输出""
     * @return
     */
    public static String getJSScriptReturn(String jsScriptStr,String defaultStr){
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine engine = sem.getEngineByExtension("js");
        Object res = StringUtils.isNotBlank(defaultStr)?defaultStr:"";
        try{
            res = engine.eval(jsScriptStr);
        }catch(Exception e){
            logger.error("getJSScriptReturn has Exception---", e);
        }
        return res.toString();
    }

}
