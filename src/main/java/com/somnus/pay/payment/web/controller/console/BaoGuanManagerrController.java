package com.somnus.pay.payment.web.controller.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.somnus.pay.payment.enums.CustomsDeclarationStatus;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.event.CustomsDeclarationEvent;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.CustomsDeclaration;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.CustomsDeclarationService;
import com.somnus.pay.payment.service.PaymentOrderService;
import com.somnus.pay.payment.util.SpringContextUtil;
import com.somnus.pay.utils.Assert;

/**
 * @description: 支付报关控制器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015-12-3
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-31    方东白           1.0            初始化
 */
@Controller
@RequestMapping("console/baoguan")
public class BaoGuanManagerrController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaoGuanManagerrController.class);

    @Autowired
    private CustomsDeclarationService customsDeclarationService;
    @Autowired
    private PaymentOrderService paymentOrderService;

    @RequestMapping("query")
    public String query(Page page, Model model, @RequestParam(required = false)String orderId,@RequestParam(required = false)String channel,@RequestParam(required = false)String status){
        Map<String,Object> params = new HashMap<String,Object>();
        if(orderId != null && !"".equals(orderId)){
            params.put("order_id",orderId.trim());
            model.addAttribute("orderId",orderId);
        }
        if(channel != null && !"".equals(channel)){
            PayChannel payChannel = PayChannel.getPayType(Integer.parseInt(channel.trim()));
            params.put("channel",payChannel);
            model.addAttribute("channel", channel);
        }
        if(status != null && !"".equals(status)){
            CustomsDeclarationStatus customsDeclarationStatus = CustomsDeclarationStatus.getCustomsDeclarationStatusByValue(status.trim());
            params.put("status",customsDeclarationStatus);
            model.addAttribute("status",status);
        }

        List<PayChannel> payChannelList = PayChannel.getAll();
        model.addAttribute("payChannelList",payChannelList);
        page.setOrder("updateTime desc");
        page.setPageSize(30);
        QueryResult<CustomsDeclaration> queryResult = customsDeclarationService.list(page,params);
        model.addAttribute("result", queryResult);
        return "console/baoguan";
    }

    @RequestMapping("retry")
    @JsonResult(desc = "报关重试处理")
    public ActionResult<String> retry(String orderId){
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("订单[{}]报关重试处理", orderId);
        }
        PaymentOrder paymentOrder = paymentOrderService.get(orderId);
        Assert.notNull(paymentOrder, PayExceptionCode.PAYMENT_ORDER_RECORD_NOT_EXIST);
        PaymentResult paymentResult = paymentOrderService.convert(paymentOrder);
        LOGGER.info("订单[{}]报关重试", paymentResult);
        CustomsDeclarationEvent event = new CustomsDeclarationEvent(this,paymentResult);
        event.setAsync(false);
        event.setTotal(0);
        SpringContextUtil.getApplicationContext().publishEvent(event);
        return new ActionResult<String>(StatusCode.SUCCESS, "报关重试处理成功");
    }
}