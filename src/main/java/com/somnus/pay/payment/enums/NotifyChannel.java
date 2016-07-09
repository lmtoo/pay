package com.somnus.pay.payment.enums;

import java.util.ArrayList;
import java.util.List;

/**
 *  @description: 支付成功时通知请求发起方的通知类型<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
public enum NotifyChannel {

	EMPTY(""), METAQ("metaq"), HTTP("http");
	
	private String desc;
	
	private NotifyChannel(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}


	public static List<NotifyChannel> getAll(){
		List<NotifyChannel> list = new ArrayList<NotifyChannel>();
		for (NotifyChannel type : NotifyChannel.values()) {
			list.add(type);
		}
		return list;
	}

	public static NotifyChannel descOf(String desc){
		NotifyChannel notifyChannel = null;
		if (desc != null){
			for (NotifyChannel type : NotifyChannel.values()) {
				if (type.desc.equalsIgnoreCase(desc))
					notifyChannel = type;
			}
		}
		return notifyChannel;
	}
	
}
