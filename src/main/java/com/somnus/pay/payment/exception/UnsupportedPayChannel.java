package com.somnus.pay.payment.exception;

import com.somnus.pay.exception.B5mException;

/**
 * @description: 不支持的支付渠道
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
public class UnsupportedPayChannel extends B5mException {

	private static final long serialVersionUID = 1L;

	public UnsupportedPayChannel(){
		super(PayExceptionCode.UNSUPPORTED_PAY_CHANNEL);
	}
	
	public UnsupportedPayChannel(Throwable cause){
		super(PayExceptionCode.UNSUPPORTED_PAY_CHANNEL, cause);
	}
	
}
