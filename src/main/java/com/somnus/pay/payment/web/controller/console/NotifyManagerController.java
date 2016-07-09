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
import com.somnus.pay.payment.enums.NotifyChannel;
import com.somnus.pay.payment.enums.NotifyStatus;
import com.somnus.pay.payment.event.NotifyClientEvent;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Notify;
import com.somnus.pay.payment.pojo.Notify.NotifyId;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.NotifyService;
import com.somnus.pay.payment.util.SpringContextUtil;
import com.somnus.pay.utils.Assert;

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
@RequestMapping("console/notify")
public class NotifyManagerController {

	private final static Logger LOGGER = LoggerFactory.getLogger(NotifyManagerController.class);
	
	@Resource
	private NotifyService notifyService;
	
	@RequestMapping("query")
	public String query(Page page, Model model, @RequestParam(required = false)String orderId,@RequestParam(required = false)String status,@RequestParam(required = false)String channel,@RequestParam(required = false)String startTime,@RequestParam(required = false)String endTime){
		Map<String,Object> params = new HashMap<String,Object>();
		if(orderId != null && !"".equals(orderId)){
			params.put("id.orderId",orderId.trim());
			model.addAttribute("orderId",orderId);
		}
		if(status != null && !"".equals(status)){
			NotifyStatus notifyStatus = NotifyStatus.valueOf(status);
			params.put("status",notifyStatus == null ? "" : notifyStatus);
			model.addAttribute("status",status);
		}

		if(channel != null && !"".equals(channel)){
			NotifyChannel notifyChannel = NotifyChannel.descOf(channel.trim().equals("无") ? "" : channel.trim());
			params.put("channel",notifyChannel == null ? "" : notifyChannel);
			model.addAttribute("channel",channel);
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

		//获取渠道和状态
		List<NotifyChannel> notifyChannelList = NotifyChannel.getAll();
		List<NotifyStatus> notifyStatusList = NotifyStatus.getAll();

		model.addAttribute("notifyChannelList",notifyChannelList);
		model.addAttribute("notifyStatusList",notifyStatusList);

		page.setOrder("updateTime desc");
		page.setPageSize(30);
		QueryResult<Notify> queryResult = notifyService.list(page,params);
		model.addAttribute("result", queryResult);
		return "console/notify";
	}
	
	@RequestMapping("retry")
	@JsonResult(desc = "支付通知重试处理")
	public ActionResult<String> retry(NotifyId id){
		if(LOGGER.isInfoEnabled()){
			LOGGER.info("支付通知[{}]重试处理", id);
		}
		id.validate();
		Notify notify = notifyService.lock(id);
		Assert.isTrue(notify == null || notify.getStatus() != NotifyStatus.SUCCESS, PayExceptionCode.NOTIFY_CLIENT_RECORD_REPEAT);
		LOGGER.info("订单[{}]支付通知重试", id.getOrderId());
		NotifyClientEvent event = new NotifyClientEvent(this, id.getType(), id.getOrderId());
		event.setAsync(false);
		event.setTotal(0);
		SpringContextUtil.getApplicationContext().publishEvent(event);
		return new ActionResult<String>(StatusCode.SUCCESS, "支付通知重试处理成功");
	}
	
}
