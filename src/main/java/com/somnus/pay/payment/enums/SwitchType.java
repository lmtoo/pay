package com.somnus.pay.payment.enums;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-31<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-31       丹青生                        1.0            初始化 <br/>
 *  
 */
public enum SwitchType {

	PAYMENT_CHANNEL("支付渠道");
	
	private String desc;
	
	private SwitchType(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
}
