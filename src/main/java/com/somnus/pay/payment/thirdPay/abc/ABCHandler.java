package com.somnus.pay.payment.thirdPay.abc;

import com.somnus.pay.payment.util.PageCommonUtil;
import org.springframework.stereotype.Service;

import com.abc.pay.client.ebus.PaymentRequest;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.thirdPay.abc.config.ABCConfig;
import com.somnus.pay.payment.util.WebUtil;

/**
 * @description: 农行支付PC渠道接口接入
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2016/2/24
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2016/2/24    qingshu       1.0            农行支付能力
 */
@Service
public class ABCHandler extends ABCAbstractHandler {

    public ABCHandler() {
        super(PayChannel.ABCPay);
    }
    
    @Override
	protected void buildCreateOrderParameter(PaymentRequest paymentRequest) {
		paymentRequest.dicRequest.put("PaymentLinkType", ABCConfig.PAYMENTLINKTYPE_PC);    //设定支付接入方式
		paymentRequest.dicRequest.put("ResultNotifyURL", PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + ABCConfig.RETURN_URL_PC);//设定通知URL地址
	}
    
}
