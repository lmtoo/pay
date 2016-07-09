package com.somnus.pay.payment.web.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.mvc.support.JsonResultWrapper;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.service.PaymentChannelService;
import com.somnus.pay.payment.thirdPay.RequestType;
import com.somnus.pay.payment.util.WebUtil;

/**
 * @description: 各支付渠道回调/通知接口统一控制器
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
@Controller
@RequestMapping({"wap", "third", "callback"})
public class CallbackController {

	private final static Logger LOGGER = LoggerFactory.getLogger(CallbackController.class);
	
	@Resource
	private PaymentChannelService paymentChannelService;

	@RequestMapping("{payChannel}/notify")
	@JsonResult(desc = "处理支付后端回调通知接口")
	public JsonResultWrapper handleNotify(@PathVariable String payChannel, @RequestBody String body){
		LOGGER.info("处理支付渠道[{}]回调[{}]请求", payChannel, RequestType.NOTIFY);
		PayChannel channel = PayChannel.nameOf(payChannel);
		String result;
		Map<String, String> paramMap = WebUtil.getParamMap();
		if(paramMap != null && !paramMap.isEmpty()){
			LOGGER.info("解析key-value参数:", paramMap);
			result = paymentChannelService.handleNotify(channel, paramMap);
		}else {
			LOGGER.info("解析requestbody参数:", body);
			result = paymentChannelService.handleNotify(channel, body);
		}
		LOGGER.info("[{}]后端回调请求处理结果:[{}]", payChannel, result);
		return new JsonResultWrapper(result);
	}

	@RequestMapping("{payChannel}/return")
	@JsonResult(desc = "处理支付前端回调通知接口")
	public String handleReturn(@PathVariable String payChannel, @RequestBody String body){
		LOGGER.info("处理支付渠道[{}]回调[{}]请求", payChannel, RequestType.RETURN);
		PayChannel channel = PayChannel.nameOf(payChannel);
		String result;
		Map<String, String> paramMap = WebUtil.getParamMap();
		if(paramMap != null && !paramMap.isEmpty()){
			LOGGER.info("解析key-value参数:", paramMap);
			result = paymentChannelService.handleReturn(channel, paramMap);
		}else {
			LOGGER.info("解析requestbody参数:", body);
			result = paymentChannelService.handleReturn(channel, body);
		}
		LOGGER.info("[{}]前端回调请求处理结果:[{}]", payChannel, result);
		return "redirect:" + result;
	}

	
}
