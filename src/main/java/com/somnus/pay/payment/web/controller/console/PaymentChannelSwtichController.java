package com.somnus.pay.payment.web.controller.console;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.service.PaymentChannelSwitchService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 支付通知管理控制器
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-3
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-3       丹青生                               1.0            初始化
 */
@Controller
@RequestMapping("console/switch")
public class PaymentChannelSwtichController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentChannelSwtichController.class);
	
	@Resource
	private PaymentChannelSwitchService paymentChannelSwitchService;
	
	@RequestMapping("query")
	public String query(Page page, @RequestParam(required = false)String keyName, Model model){
		Map<String,Object> params = new HashMap<String,Object>();
		if(keyName != null && !"".equals(keyName)){
			params.put("key", keyName.trim());
			model.addAttribute("keyName", keyName.trim());
		}
		page.setPageSize(30);
		page.setOrder("key, createTime desc");
		model.addAttribute("result", paymentChannelSwitchService.list(page,params));
		return "console/switch";
	}
	
	@RequestMapping("toggle")
	@JsonResult(desc = "支付渠道开关切换")
	public ActionResult<String> retry(String key){
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("支付渠道[{}]开关切换", key);
		}
		paymentChannelSwitchService.toggle(key);
		return new ActionResult<String>(StatusCode.SUCCESS, "支付渠道开关切换成功");
	}
	
}
