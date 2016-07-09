package com.somnus.pay.payment.pojo;

import java.io.Serializable;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-29<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-29       丹青生                        1.0            初始化 <br/>
 *  
 */
public class ConfirmResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean support;
	private PaymentResult result;

	public ConfirmResult(){}
	
	public ConfirmResult(boolean support, PaymentResult result){
		this.support = support;
		this.result = result;
	}
	
	public boolean isSupport() {
		return support;
	}

	public void setSupport(boolean support) {
		this.support = support;
	}

	public PaymentResult getResult() {
		return result;
	}

	public void setResult(PaymentResult result) {
		this.result = result;
	}

}
