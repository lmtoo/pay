package com.somnus.pay.payment.thirdPay.tencent.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.utils.Assert;

/**
 *  微信支付工具类
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-29<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-29       丹青生                        1.0            初始化 <br/>
 *  
 */
public class PayCommonUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(PayCommonUtil.class);
	
	public static String createNoncestr(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < length; i++) {
			Random rd = new Random();
			res += chars.indexOf(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	public static String createNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

    /**
     * 计算签名
     * @param data 参数
     * @param key 加密key
     * @return 签名
     */
    public static String createSign(Map<String, String> data, String key){
    	LOGGER.info("计算签名:{}", key, data);
    	Assert.isTrue(MapUtils.isNotEmpty(data), PayExceptionCode.PAYMENT_PARAM_ERROR);
		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.putAll(data);
		StringBuilder signBuilder = new StringBuilder();
		for (Entry<String, String> entry : parameters.entrySet()) {
			String k = entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v)  && !"sign".equals(k) && !"key".equals(k)) {
				signBuilder.append(k + "=" + v + "&");
			}
		}
		signBuilder.append("key=" + key);
		String sign = MD5Util.MD5Encode(signBuilder.toString(), "UTF-8").toUpperCase();
    	LOGGER.info("签名结算结果:{}", sign);
    	return sign;
    }

	/**
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters  请求参数
	 * @return
	 */
	public static String getRequestXml(Map<String, String> parameters){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		for (Entry<String, String> entry : parameters.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
				sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
			}else {
				sb.append("<"+k+">"+v+"</"+k+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
	/**
	 * @Description：返回给微信的参数
	 * @param return_code 返回编码
	 * @param return_msg  返回信息
	 * @return
	 */
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}
	
	
	/**
	 * 发送https请求
	 * @param requestUrl 请求地址
	 * @param requestMethod 请求方式（GET、POST）
	 * @param outputStr 提交的数据
	 * @return 返回微信服务器响应的信息
	 */
	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setSSLSocketFactory(ssf);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			LOGGER.error("连接超时：{}", ce);
		} catch (Exception e) {
			LOGGER.error("https请求异常：{}", e);
		}
		return null;
	}
	/**
	 * SHA1 算法签名
	 * @param decript
	 * @return
	 */
	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * SHA 算法签名
	 * @param decript
	 * @return
	 */
	public static String SHA(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/** 
     * 将map转换成url 
     * @param map 
     * @return 
     */  
    public static String getUrlParamsByMap(SortedMap<Object, Object> map) {  
        if (map == null) {  
            return "";  
        }  
        StringBuffer sb = new StringBuffer();  
        Set<Entry<Object, Object>> entries=map.entrySet();  
        Iterator<Map.Entry<Object,Object>> iEntries=entries.iterator();  
        while(iEntries.hasNext()){  
            Map.Entry<Object, Object> entry=iEntries.next();  
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }  
        String s = sb.toString();  
        if (s.endsWith("&")) {  
            s = org.apache.commons.lang3.StringUtils.substringBeforeLast(s, "&");  
        }  
        return s;  
    }

    /**
     * 根据订单号按第三方渠道截取相应位数
     * @param orderId
     * @return
     */
    public static String createPayId(String orderId,int length) {
        return StringUtils.isNotBlank(orderId) ? orderId.substring(orderId.length()-length):"";
    }
}
