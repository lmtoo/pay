package com.somnus.pay.payment.exception;

import com.somnus.pay.exception.B5mException;

/**
 * @description: 不支持的支付回调请求类型
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
public class UnsupportedRequestTypeException extends B5mException {

	private static final long serialVersionUID = 1L;

	public UnsupportedRequestTypeException(){
		super(PayExceptionCode.UNSUPPORTED_REQUEST_TYPE);
	}
	
	public UnsupportedRequestTypeException(Throwable cause){
		super(PayExceptionCode.UNSUPPORTED_REQUEST_TYPE, cause);
	}
	
}
