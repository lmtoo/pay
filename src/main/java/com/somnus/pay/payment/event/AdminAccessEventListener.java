package com.somnus.pay.payment.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.somnus.pay.cache.CacheServiceExcutor;
import com.somnus.pay.payment.config.SystemConfig;

/**
 *  @description: 支付成功时通知请求发起方时间监听器<br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-23<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-23       丹青生                        1.0            初始化 <br/>
 *  
 */
@Component
public class AdminAccessEventListener extends AsyncEventListener<AdminAccessEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(AdminAccessEventListener.class);

	public AdminAccessEventListener(){
		LOGGER.debug("管理员访问事件监听器启动");
	}
	
	@Override
	protected void handle(AdminAccessEvent event) {
		String token = event.getToken();
		String ip = event.getIp();
		LOGGER.info("来自[{}]的管理员会话:[{}]访问", event.getIp(), token);
		CacheServiceExcutor.put(SystemConfig.ADMIN_LOGIN_CACHE_KEY_PREFIX + token, ip, SystemConfig.ADMIN_LOGIN_CACHE_EXPIRE);
	}

}
