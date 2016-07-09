package com.somnus.pay.payment.enums;

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
public enum NotifyType {

	BZ_FROZEN_SUCCESS("帮钻冻结成功"), PAY_SUCCESS("支付成功");
	
	private String desc;
	
	private NotifyType(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
}
