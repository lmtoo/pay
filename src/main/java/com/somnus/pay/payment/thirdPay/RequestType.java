package com.somnus.pay.payment.thirdPay;

/**
 * 
 * @description: 第三方支付渠道操作请求类型
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 
 */
public enum RequestType {

	RETURN(1, "前端回调"), NOTIFY(2, "后端回调"), ORDER(3, "支付建单"), REFUND(4, "退款"),
	QUERY(5, "结果查询"), CONFIRM(6, "结果确认"), BG(7, "报关");

	private Integer value;
	private String desc;

	private RequestType(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public static RequestType nameOf(String name){
		RequestType type = null;
		if (name != null){
			for (RequestType t : RequestType.values()) {
				if (t.name().equalsIgnoreCase(name))
					type = t;
			}
		}
		return type;
	}

}