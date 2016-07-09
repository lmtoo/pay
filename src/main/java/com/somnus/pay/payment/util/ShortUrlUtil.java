package com.somnus.pay.payment.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.pojo.Msg;

/**
 * 
 * @description: 获取短链接
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-22
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-22       丹青生                               1.0            初始化
 
 */
public class ShortUrlUtil {

	public static Msg getShortUrl(String url) {
		if (StringUtils.isBlank(url))
			return new Msg(false, "长链接为空");
		Msg msg = new Msg(false, "亲，赶紧去卖彩票吧，短链接出问题了");
		JSONObject jsObject1 = null;
		try {
			String shorturl = "https://api.weibo.com/2/short_url/shorten.json";
			String source = "2982910324";
			Map<String, String> param = new HashMap<String, String>();
			param.put("source", source);
			param.put("url_long", url);
			String res = HttpClientUtils.get(shorturl, param);
			jsObject1 = JsonUtils.parse2Bean(res, JSONObject.class);
		} catch (Exception e) {
			return msg;
		}
		if (jsObject1 == null)
			return msg;
		JSONArray jsArray = jsObject1.getJSONArray("urls");
		if (jsArray == null)
			return msg;
		JSONObject jsObject2 = jsArray.getJSONObject(0);
		if (jsObject2 == null)
			return msg;
		String shortUrl = jsObject2.getString("url_short");
		if (shortUrl == null)
			return msg;
		return new Msg(1, true, shortUrl);
	}

}
