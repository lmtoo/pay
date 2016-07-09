package com.somnus.pay.payment.event;


/**
 *  @description: 系统警告事件<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-23<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-23       丹青生                        1.0            初始化 <br/>
 *  
 */
public class WarnningEvent extends PaymentEvent {

	private static final long serialVersionUID = 1L;
	
	private String warn;
	
	/**
	 * @param source
	 */
	public WarnningEvent(Object source, String warn) {
		super(source, "系统警告");
		this.warn = warn;
	}

	public String getWarn() {
		return warn;
	}

}
