package com.somnus.pay.payment.event;

import java.net.InetAddress;

import javax.annotation.Resource;

import com.somnus.pay.payment.config.PayPlatformConfigReadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.somnus.pay.sms.model.Message;
import com.somnus.pay.sms.service.SMSService;

/**
 *  @description: 支付成功时通知请求发起方时间监听器<br/>
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
public class WarnningEventListener extends AsyncEventListener<WarnningEvent> {

	private final static Logger LOGGER = LoggerFactory.getLogger(WarnningEventListener.class);

	@Resource
	private SMSService smsService;
	private String ip;
	@Resource
	private PayPlatformConfigReadable payPlatformConfigReadable;


	public WarnningEventListener(){
		LOGGER.debug("系统警告事件监听器启动");
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			LOGGER.warn("获取本地IP失败");
		}
	}
	
	@Override
	protected void handle(WarnningEvent event) {
		String warn = event.getWarn();
		LOGGER.info("发送系统警告消息:[{}]", warn);
		try{
			for (String mobile : payPlatformConfigReadable.getAdminMobiles()) {
				LOGGER.info("发送系统警告消息到:[{}]", mobile);
				Message message = new Message();
				message.setContent("系统警告:" + warn);
				message.setReplaceValues(mobile);
				message.setCreateIp(ip);
				smsService.send(message);
			}
		}catch (Exception e){
			LOGGER.warn("发送系统警告消息异常error:[{}]", warn);
		}

	}

}
