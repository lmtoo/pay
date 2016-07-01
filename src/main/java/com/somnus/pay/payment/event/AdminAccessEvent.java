package com.somnus.pay.payment.event;


/**
 *  @description: 管理员访问事件<br/>
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
public class AdminAccessEvent extends PaymentEvent {

	private static final long serialVersionUID = 1L;
	
	private String token;
	private String ip;
	/**
	 * @param source
	 */
	public AdminAccessEvent(Object source, String token, String ip) {
		super(source, "管理员访问");
		this.token = token;
		this.ip = ip;
	}

	public String getToken() {
		return token;
	}

	public String getIp() {
		return ip;
	}
	
}
