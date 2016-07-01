package com.somnus.pay.payment.thirdPay.tencent.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求处理类
 * 请求处理类继承此类，重写createSign方法即可。
 * @author miklchen
 *
 */
@SuppressWarnings("rawtypes")
public class RequestHandler {

    /** 网关url地址 */
    private String              gateUrl;

    /** 密钥 */
    private String              key;

    /** 请求的参数 */
    private SortedMap           parameters;

    /** debug信息 */
    private String              debugInfo;

    private HttpServletRequest  request;

    private HttpServletResponse response;

    /**
     * 构造函数
     * @param request
     * @param response
     */
    public RequestHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;

        this.gateUrl = "https://gw.tenpay.com/gateway/pay.htm";
        this.key = "";
        this.parameters = new TreeMap();
        this.debugInfo = "";
    }

    /**
    *初始化函数。
    */
    public void init() {
    }

    /**
    *获取入口地址,不包含参数值
    */
    public String getGateUrl() {
        return gateUrl;
    }

    /**
    *设置入口地址,不包含参数值
    */
    public void setGateUrl(String gateUrl) {
        this.gateUrl = gateUrl;
    }

    /**
    *获取密钥
    */
    public String getKey() {
        return key;
    }

    /**
    *设置密钥
    */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 获取参数值
     * @param parameter 参数名称
     * @return String 
     */
    public String getParameter(String parameter) {
        String s = (String) this.parameters.get(parameter);
        return (null == s) ? "" : s;
    }

    /**
     * 设置参数值
     * @param parameter 参数名称
     * @param parameterValue 参数值
     */
    @SuppressWarnings("unchecked")
    public void setParameter(String parameter, String parameterValue) {
        String v = "";
        if (null != parameterValue) {
            v = parameterValue.trim();
        }
        this.parameters.put(parameter, v);
    }

    /**
     * 返回所有的参数
     * @return SortedMap
     */
    public SortedMap getAllParameters() {
        return this.parameters;
    }

    /**
    *获取debug信息
    */
    public String getDebugInfo() {
        return debugInfo;
    }

    /**
     * 获取带参数的请求URL
     * @return String
     * @throws UnsupportedEncodingException 
     */
    public String getRequestURL() throws UnsupportedEncodingException {

        this.createSign();

        StringBuffer sb = new StringBuffer();
        String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append(k + "=" + URLEncoder.encode(v, enc) + "&");
        }

        //去掉最后一个&
        String reqPars = sb.substring(0, sb.lastIndexOf("&"));
        return this.getGateUrl() + "?" + reqPars;

    }

    /**
    * 生成Form
    * @return String
    * @throws UnsupportedEncodingException 
    */
    @SuppressWarnings("unchecked")
    public String createSubmitForm() throws UnsupportedEncodingException {

        this.createSign();
        String form = buildRequest(this.parameters, "POST", "submit");
        return form;

    }

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param paramMap 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     * @throws UnsupportedEncodingException 
     */
    public String buildRequest(SortedMap<String, String> paramMap, String method, String buttonName)
                                                                                                    throws UnsupportedEncodingException {

        StringBuffer sbHtml = new StringBuffer();
        String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
        sbHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"" + enc
                      + "\" />");
        sbHtml.append("<form id=\"tencentForm\" name=\"tencentForm\" action=\"" + this.getGateUrl()
                      + "\" method=\"" + method + "\">");

        for (Entry<String, String> item : paramMap.entrySet()) {
            String name = item.getKey();
            String value = item.getValue();
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        sbHtml.append("<input type=\"submit\" value=\"" + buttonName
                      + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['tencentForm'].submit();</script>");

        return sbHtml.toString();
    }

    public void doSend() throws UnsupportedEncodingException, IOException {
        this.response.sendRedirect(this.getRequestURL());
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    protected void createSign() {
        StringBuffer sb = new StringBuffer();
        Set es = this.parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + this.getKey());

        String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
        String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();

        this.setParameter("sign", sign);

        //debug信息
        this.setDebugInfo(sb.toString() + " => sign:" + sign);

    }
    
    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
	public static void createSign(SortedMap<String, String> parameters, String key) {
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k)
					&& !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + key);
		String sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		parameters.put("sign", sign);
	}

    /**
    *设置debug信息
    */
    protected void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    protected HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    protected HttpServletResponse getHttpServletResponse() {
        return this.response;
    }

}
