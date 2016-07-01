package com.somnus.pay.payment.thirdPay.bocom.util;

import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.utils.Assert;
import com.bocom.netpay.b2cAPI.BOCOMB2CClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  * @description: BOCOMB2C配置初始化
  * Copyright 2011-2015 B5M.COM. All rights reserved
  * @author: masanbao
  * @version: 1.0
  * @createdate: 2015-01-22
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-01-22   masanbao       1.0            初始化
  * 2015-11-10   丹青生       	1.1            初始化加载方式修复
  * 2015-12-15   qingshu        1.2            添加类描述
  */
public class ContextUtil {

	private final static Logger LOGGER = LoggerFactory.getLogger(ContextUtil.class);

	private static ContextUtil instance = new ContextUtil();
	private BOCOMB2CClient client = null;

	public static ContextUtil getInstance() {
		return instance;
	}

	private ContextUtil() {
		try {
			String xmlConfigPath = BocomConstants.CER_PATH + "bocommjava/ini/B2CMerchant.xml";
			client = new BOCOMB2CClient();
			int ret = client.initialize(xmlConfigPath); // 该代码只需调用一次
			Assert.isTrue(ret == 0, new StatusCode(PayExceptionCode.BOCOMB2C_INIT_ERROR.getCode(), "初始化失败：" + client.getLastErr())); // 初始化失败
		}catch (Exception e){
			LOGGER.warn("交行初始化失败",e);
		}
	}

	public BOCOMB2CClient getClient() {
		return client;
	}

}
