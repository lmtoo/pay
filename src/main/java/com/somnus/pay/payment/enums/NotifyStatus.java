package com.somnus.pay.payment.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *  @description: 支付成功时通知请求发起方的处理状态 <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-25       丹青生                        1.0            初始化 <br/>
 *  
 */
public enum NotifyStatus {

	SUCCESS("成功"), FAILURE("失败");
	
	private String desc;
	
	private NotifyStatus(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public static List<NotifyStatus> getAll(){
		List<NotifyStatus> list = new ArrayList<NotifyStatus>();
		for (NotifyStatus type : NotifyStatus.values()) {
			list.add(type);
		}
		return list;
	}

	public static NotifyStatus descOf(String desc){
		NotifyStatus notifyStatus = null;
		if (desc != null){
			for (NotifyStatus type : NotifyStatus.values()) {
				if (type.desc.equalsIgnoreCase(desc))
					notifyStatus = type;
			}
		}
		return notifyStatus;
	}
}
