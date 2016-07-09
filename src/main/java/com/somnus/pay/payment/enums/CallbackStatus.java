package com.somnus.pay.payment.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *  @description: 第三方支付渠道回调处理状态 <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-25       丹青生                        1.0            初始化 <br/>
 *  
 */
public enum CallbackStatus {

	INIT("初始化"), SUCCESS("成功"), FAILURE("失败");
	
	private String desc;
	
	private CallbackStatus(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static List<CallbackStatus> getAll(){
		List<CallbackStatus> list = new ArrayList<CallbackStatus>();
		for (CallbackStatus type : CallbackStatus.values()) {
			list.add(type);
		}
		return list;
	}

	public static CallbackStatus descOf(String desc){
		CallbackStatus callbackStatus = null;
		if (desc != null){
			for (CallbackStatus type : CallbackStatus.values()) {
				if (type.desc.equalsIgnoreCase(desc))
					callbackStatus = type;
			}
		}
		return callbackStatus;
	}
	
}
