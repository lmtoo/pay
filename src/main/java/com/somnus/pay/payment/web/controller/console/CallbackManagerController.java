package com.somnus.pay.payment.web.controller.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.enums.CallbackStatus;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.event.CallbackProcessEvent;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Callback;
import com.somnus.pay.payment.pojo.Callback.CallbackId;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.CallbackService;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.util.SpringContextUtil;
import com.somnus.pay.utils.Assert;

/**
 * @description: 支付回调管理控制器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-3
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-3       丹青生                               1.0            初始化
 */
@Controller
@RequestMapping("console/callback")
public class CallbackManagerController {

	private final static Logger LOGGER = LoggerFactory.getLogger(CallbackManagerController.class);
	
	@Resource
	private CallbackService callbackService;
	
	@RequestMapping("query")
	public String query(Page page, Model model, @RequestParam(required = false)String orderId,@RequestParam(required = false)String status,@RequestParam(required = false)String channel,@RequestParam(required = false)String startTime,@RequestParam(required = false)String endTime){
		Map<String,Object> params = new HashMap<String,Object>();
		if(orderId != null && !"".equals(orderId)){
			params.put("id.orderId",orderId.trim());
			model.addAttribute("orderId",orderId);
		}
		if(status != null && !"".equals(status)){
			CallbackStatus callbackStatus = CallbackStatus.valueOf(status);
			params.put("status",callbackStatus == null ? "" : callbackStatus);
			model.addAttribute("status",status);
		}
		if(channel != null && !"".equals(channel)){
			PayChannel payChannel = PayChannel.getPayType(Integer.parseInt(channel.trim()));
			params.put("id.channel",payChannel == null  ? "" : payChannel);
			model.addAttribute("channel", channel);
		}
		Object[] o = new Object[]{null,null};
		if(startTime != null && !"".equals(startTime)){
			o[0] = startTime;
			model.addAttribute("startTime",startTime);
		}
		if(endTime != null && !"".equals(endTime)){
			o[1] = endTime;
			model.addAttribute("endTime",endTime);
		}
		if(o[0] != null || o[1] != null){
			params.put("create_time",o);
		}

		// 获取渠道和状态的列表
		List<PayChannel> payChannelList = PayChannel.getAll();
		model.addAttribute("payChannelList",payChannelList);
		List<CallbackStatus> callbackStatusList = CallbackStatus.getAll();
		model.addAttribute("callbackStatusList",callbackStatusList);

		page.setOrder("updateTime desc");
		page.setPageSize(30);
		QueryResult<Callback> queryResult = callbackService.list(page,params);
		model.addAttribute("result", queryResult);
		return "console/callback";
	}

	@RequestMapping("retry")
	@JsonResult(desc = "第三方支付回调结果重试处理")
	public ActionResult<String> retry(CallbackId id){
		id.validate();
		Callback callback = callbackService.get(id);
		Assert.notNull(callback, PayExceptionCode.CALLBACK_RECORD_NOT_EXIST);
		Assert.isTrue(callback.getStatus() != CallbackStatus.SUCCESS, PayExceptionCode.CALLBACK_PROCESS_ERROR);
		PaymentResult paymentResult = JsonUtils.parse2Bean(callback.getData(), PaymentResult.class);
		Assert.notNull(paymentResult, PayExceptionCode.CALLBACK_PROCESS_ERROR);
		RequestParameter<Object, Object> requestParameter = new RequestParameter<Object, Object>(id.getChannel(), id.getType(), null);
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("第三方支付回调[{}]结果重试处理:{}", new Object[]{JsonUtils.toJson(id), paymentResult});
		}
		CallbackProcessEvent event = new CallbackProcessEvent(this, requestParameter, paymentResult);
		event.setAsync(false);
		event.setTotal(0);
		SpringContextUtil.getApplicationContext().publishEvent(event);
		return new ActionResult<String>(StatusCode.SUCCESS, "回调结果重试处理成功");
	}
	
}
