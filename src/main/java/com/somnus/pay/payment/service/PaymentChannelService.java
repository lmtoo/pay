package com.somnus.pay.payment.service;

import java.util.Map;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.ConfirmResult;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;

/**
 * @description: 支付回调服务接口
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
public interface PaymentChannelService {

	/**
	 * 构建第三方支付页面跳转表单
	 * @param paymentOrder 支付订单信息
	 * @return 跳转表单
	 */
	public String createOrder(PaymentOrder paymentOrder);

	public String handleReturn(PayChannel channel, Map<String, String> parameter);
	
	public String handleReturn(PayChannel channel, String parameter);
	
	public String handleNotify(PayChannel channel, Map<String, String> parameter);
	
	public String handleNotify(PayChannel channel, String parameter);
	
	public String handleRefund(PayChannel channel, Map<String, String> parameter);

	public void baoguan(PaymentResult result);
	
	/**
	 * 查询第三方支付订单详情
	 * @param paymentOrder 本地支付订单信息
	 * @return 第三方支付订单详情
	 */
	public Map<String, String> queryOrder(PaymentOrder paymentOrder);
	
	/**
	 * 确认订单支付是否成功
	 * @param paymentOrder 本地支付订单信息
	 * @return 確認結果
	 */
	public ConfirmResult confirmOrder(PaymentOrder paymentOrder);
	
}
