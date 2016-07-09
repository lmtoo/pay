package com.somnus.pay.payment.web.controller.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.event.PaySuccessEvent;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.payment.service.PaymentOrderService;
import com.somnus.pay.payment.service.PaymentService;
import com.somnus.pay.payment.util.SpringContextUtil;
import com.somnus.pay.utils.Assert;

/**
 * @description: 订单展示
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015-12-3
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-31    方东白           1.0          订单展示
 */
@Controller
@RequestMapping("console/paymentOrder")
public class PaymentOrderManagerrController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentOrderManagerrController.class);
	
	@Autowired
	private PaymentOrderService paymentOrderService;
	@Autowired
	private IPayService payService;
	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping("query")
	public String query(Page page, Model model
			, @RequestParam(required = false)String orderId
			, @RequestParam(required = false)String userId
			, @RequestParam(required = false)String status
			, @RequestParam(required = false)String source
			, @RequestParam(required = false)String thirdPayType
			, @RequestParam(required = false)String thirdTradeNo
			, @RequestParam(required = false)String bzStatus
			, @RequestParam(required = false)String startTime, @RequestParam(required = false)String endTime){

		Map<String,Object> params = new HashMap<String,Object>();
		if(orderId != null && !"".equals(orderId)){
			params.put("orderId",orderId.trim());
			model.addAttribute("orderId",orderId);
		}

		if(userId != null && !"".equals(userId)){
			params.put("userId",userId.trim());
			model.addAttribute("userId",userId);
		}

		if(status != null && !"".equals(status)){
			params.put("status",Integer.parseInt(status));
			model.addAttribute("status",status);
		}

		if(thirdPayType != null && !"".equals(thirdPayType)){
			params.put("thirdPayType",Integer.parseInt(thirdPayType));
			model.addAttribute("thirdPayType",thirdPayType);
		}

		if(thirdTradeNo != null && !"".equals(thirdTradeNo)){
			params.put("thirdTradeNo",thirdTradeNo.trim());
			model.addAttribute("thirdTradeNo",thirdTradeNo);
		}

		if(bzStatus != null && !"".equals(bzStatus)){
			params.put("bzStatus",Integer.parseInt(bzStatus));
			model.addAttribute("bzStatus",bzStatus);
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
		page.setOrder("updateTime desc");
		page.setPageSize(30);
		QueryResult<PaymentOrder> queryResult = paymentOrderService.list(page,params);
		model.addAttribute("result", queryResult);
		return "console/paymentOrder";
	}

	@RequestMapping("retry")
	@JsonResult(desc = "强行纠正订单支付状态")
	public ActionResult<String> retry(String orderId, @RequestParam String thirdTradeNo, String channel){
		LOGGER.info("使用第三方支付号[{}]和支付渠道[{}]强行纠正订单[{}]的支付状态", new Object[]{thirdTradeNo, orderId, channel});
		Assert.hasText(thirdTradeNo, PayExceptionCode.THIRD_TRADE_NO_IS_NULL);
		PaymentOrder paymentOrder = paymentOrderService.get(orderId);
		Assert.notNull(paymentOrder, PayExceptionCode.PAYMENT_ORDER_RECORD_NOT_EXIST);
		PaymentResult paymentResult = paymentOrderService.convert(paymentOrder);
		paymentResult.setThirdTradeNo(thirdTradeNo);
		paymentResult.setTradeStatus(PaymentOrderType.SCCUESS.getValue() + "");
		paymentResult.setStatus(PaymentOrderType.SCCUESS.getValue());
		if(StringUtils.isNotEmpty(channel)){
			paymentResult.setChannel(PayChannel.valueOf(Integer.parseInt(channel)));
		}
		payService.updateOrderStatus(paymentResult);
        SpringContextUtil.getApplicationContext().publishEvent(new PaySuccessEvent(this, paymentResult));
		return new ActionResult<String>(StatusCode.SUCCESS, "强行纠正订单支付状态成功");
	}
	
	@RequestMapping("confirm")
	@JsonResult(desc = "确认订单支付结果")
	public ActionResult<String> confirm(String orderId){
		LOGGER.info("确认订单[{}]的支付结果", orderId);
		boolean success = paymentService.confirmAndUpdateOrder(orderId);
		return new ActionResult<String>(StatusCode.SUCCESS, success ? "支付成功" : "支付尚未成功");
	}
	
}
