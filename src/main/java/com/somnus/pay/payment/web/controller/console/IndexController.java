package com.somnus.pay.payment.web.controller.console;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somnus.pay.payment.config.PayPlatformConfigReadable;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import com.somnus.pay.cache.CacheServiceExcutor;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.payment.config.PaySourceConfigReadable;
import com.somnus.pay.payment.config.SystemConfig;
import com.somnus.pay.payment.pojo.SystemInfo;
import com.somnus.pay.payment.service.ObserverService;
import com.somnus.pay.sms.model.Message;
import com.somnus.pay.sms.service.SMSService;
import com.somnus.pay.utils.RequestUtils;

/**
 * @description: 控制台首页
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-3
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-3       丹青生                               1.0            初始化
 */
@Controller
@RequestMapping("console")
public class IndexController {

	private final static Logger LOGGER = LoggerFactory.getLogger(IndexController.class);
	
	@Resource
	private PaySourceConfigReadable paySourceConfig;
	@Resource
	private SMSService smsService;
	@Autowired
	private ObserverService observerService;
	@Autowired
	private PayPlatformConfigReadable payPlatformConfigReadable;
	
	@RequestMapping("terrorists")
	public String login(@CookieValue(value = "pay_token", required = false) String token, HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("ip", RequestUtils.getRemoteIP(request));
		if(StringUtils.isNotEmpty(token)){
			LOGGER.info("擦除过期会话[{}]的相关数据", token);
			CacheServiceExcutor.remove("pay_console_ip_" + token);
			CacheServiceExcutor.remove("pay_console_login_" + token);
		}
		token = RandomStringUtils.randomNumeric(16);
		String ip = RequestUtils.getRemoteIP(request);
		LOGGER.info("为来自[{}]的请求生成会话ID:{}", ip, token);
		int expire = 60 * 30;
		CacheServiceExcutor.put("pay_console_ip_" + token, ip, expire);
		Cookie cookie = new Cookie("pay_token", token);
		cookie.setDomain(request.getServerName());
		cookie.setPath("/");
		response.addCookie(cookie);
		return this.getPage("login");
	}
	
	@RequestMapping("index")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model){
		model.addAttribute("ip", RequestUtils.getRemoteIP(request));
		try {
			List<SystemInfo> systemInfos = observerService.collectSystemInfo();
			model.addAttribute("systemInfos", systemInfos);
			Date startTime = observerService.getStartTime();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			model.addAttribute("startTime", simpleDateFormat.format(startTime));
			long totalOrderCount = observerService.getTotalOrderCount();
			model.addAttribute("totalOrderCount", totalOrderCount);
			double totalOrderAmount = observerService.getTotalOrderAmount();
			model.addAttribute("totalOrderAmount", new BigDecimal(totalOrderAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			double totalMoneyAmount = observerService.getTotalMoneyAmount();
			model.addAttribute("totalMoneyAmount", new BigDecimal(totalMoneyAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			long totalBzAmount = observerService.getTotalBzAmount();
			model.addAttribute("totalBzAmount", totalBzAmount);
		} catch (Exception e) {
			model.addAttribute("exception", e.getMessage());
			LOGGER.warn("提取控制台数据失败", e);
		}
		return this.getPage("index");
	}
	
	protected String getPage(String page) {
		return "console/" + page;
	}
	
	@RequestMapping("refresh/all")
	@JsonResult(desc = "刷新所有系统配置")
	public ActionResult<String> refreshAll() throws Exception {
		LOGGER.info("刷新所有系统配置");
		long start = System.currentTimeMillis();
		paySourceConfig.refresh();
		long end = System.currentTimeMillis();
		return new ActionResult<String>("2000", "刷新成功(" + (end - start) + " ms)");
	}
	
	@RequestMapping("get/key")
	@JsonResult(desc = "获取临时登录口令")
	public ActionResult<String> getKey(@CookieValue("pay_token") String token, String key, HttpServletRequest request) {
		LOGGER.info("为[{}]授予临时登录口令", key);
		if(payPlatformConfigReadable.getAdminMobiles().contains(key)){
			String password = RandomStringUtils.randomNumeric(6);
			LOGGER.info("为[{}]分配临时登录口令:[{}]", key, password);
			CacheServiceExcutor.put("pay_console_password_" + token, password, 180); // 3分钟过期
			Message message = new Message();
			message.setContent("临时登录口令:" + password);
			message.setReplaceValues(key);
			message.setCreateIp(RequestUtils.getRemoteIP(request));
			LOGGER.info("发送通知短信:[{}]", message.getContent());
			smsService.send(message);
		}else{
			LOGGER.warn("该[{}]非法获取管理员的授予临时登录口令", key);
		}
		return new ActionResult<String>("2000", "发送成功");
	}
	
	@RequestMapping("login")
	@JsonResult(desc = "验证临时登录口令")
	public ActionResult<Boolean> login(@CookieValue("pay_token") String token, String key, HttpServletRequest request){
		LOGGER.info("验证会话[{}]的临时登录口令:[{}]", token, key);
		String cacheKey = "pay_console_password_" + token;
		String souceKey = (String) CacheServiceExcutor.get(cacheKey);
		LOGGER.info("曾为会话[{}]授予的临时登录口令:{}", token, souceKey);
		boolean hasCreated = StringUtils.isNotEmpty(souceKey);
		if(hasCreated){
			CacheServiceExcutor.remove(cacheKey);
			LOGGER.info("已擦除为会话[{}]授予的临时登录口令", token);
		}
		boolean success = hasCreated && souceKey.equalsIgnoreCase(key);
		if(success){
			CacheServiceExcutor.put("pay_console_login_" + token, RequestUtils.getRemoteIP(request), SystemConfig.ADMIN_LOGIN_CACHE_EXPIRE);
		}
		return new ActionResult<Boolean>("2000", "验证完成", success);
	}
	
}
