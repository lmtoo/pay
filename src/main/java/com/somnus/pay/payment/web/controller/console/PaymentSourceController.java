package com.somnus.pay.payment.web.controller.console;

import java.util.Date;


import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.utils.Assert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.payment.config.PaySourceConfigService;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.pojo.QueryResult;

/**
  * @description: 支付来源管理
  * Copyright 2011-2016 B5M.COM. All rights reserved
  * @author: 允礼
  * @version: 1.0
  * @createdate: 2015-12-9
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2016-04-18   yunli         1.0            初始化
  */
@Controller
@RequestMapping("console")
public class PaymentSourceController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentSourceController.class);

	@Autowired
	private PaySourceConfigService paySourceService;

	@Autowired
    private PaySourceConfigService paySourceConfigService;

	@RequestMapping("managePaymentSource")
	public String managePaymentSource(Page page, Model model, String sourceId){
		LOGGER.info("PaymentSource管理页面.查询相关sourceId:[{}]", sourceId);
		if(StringUtils.isNotBlank(sourceId)){
			sourceId = sourceId.trim();
		}
		page.setPageSize(10);
		QueryResult<PaymentSource> list = paySourceService.getAllByPage(page,sourceId);
		model.addAttribute("result", list);
		model.addAttribute("page", page);
		model.addAttribute("sourceIdVal", sourceId);
		return "console/paymentSource";
	}

	@RequestMapping("deletePaymentSource")
	@JsonResult
	public ActionResult<Boolean>  deletePaymentSource(String id, String pwd){
		LOGGER.info("删除PaymentSource.id:[{}]", id);
		Assert.isTrue(Constants.INNER_QUERY_PWD.equals(pwd), PayExceptionCode.PAYMENT_SOURCE_OPER_ERROR);
		Assert.isTrue(StringUtils.isNotBlank(id), PayExceptionCode.PAYMENT_SOURCE_OPER_ERROR);
		paySourceConfigService.removePaymentSource(id);
		return new ActionResult<Boolean>(StatusCode.SUCCESS);
	}

	@RequestMapping(value="addPaymentSource")
	@JsonResult
	public ActionResult<Boolean> addPaymentSource(PaymentSource ps, String pwd) {
		LOGGER.info("新增PaymentSource:[{}]",ps);
		Assert.isTrue(Constants.INNER_QUERY_PWD.equals(pwd), PayExceptionCode.PAYMENT_SOURCE_OPER_ERROR);
		Assert.isTrue(null != ps, PayExceptionCode.PAYMENT_SOURCE_OPER_ERROR);
		ps.setCreateTime(new Date());
		paySourceConfigService.addPaymentSource(ps);
		return new ActionResult<Boolean>(StatusCode.SUCCESS);
	}

	@RequestMapping("updatePaymentSource")
	@JsonResult
	public ActionResult<Boolean> updatePaymentSource(PaymentSource ps, String pwd){
		LOGGER.info("更新PaymentSource:[{}]",ps);
		Assert.isTrue(Constants.INNER_QUERY_PWD.equals(pwd), PayExceptionCode.PAYMENT_SOURCE_OPER_ERROR);
		Assert.isTrue(null != ps, PayExceptionCode.PAYMENT_SOURCE_OPER_ERROR);
		ps.setCreateTime(new Date());
		paySourceConfigService.updatePaymentSource(ps);
		return new ActionResult<Boolean>(StatusCode.SUCCESS);
	}

	/**
	 * 清空读取的PaymentSource缓存信息
	 * @return
	 */
	@RequestMapping(value = "cacheDBPaySourceClean")
	@JsonResult(desc = "清空读取数据配置信息")
	public ActionResult<String> cacheDBConfigClean(String pwd, String ips) {
		Assert.isTrue(Constants.INNER_QUERY_PWD.equals(pwd), PayExceptionCode.PAYMENT_SOURCE_OPER_ERROR);
		return new ActionResult<String>(StatusCode.SUCCESS, paySourceConfigService.sendOtherCachePaymentSourceDBClean(ips, pwd));
	}



}
