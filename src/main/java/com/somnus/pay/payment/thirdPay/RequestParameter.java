package com.somnus.pay.payment.thirdPay;

import java.io.Serializable;

import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.enums.PayChannel;

/**
 * @description: 支付渠道回调参数 
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
public class RequestParameter<P, R> implements Serializable {

	private static final long serialVersionUID = 1L;

	private PayChannel channel;
	private RequestType type;
	private P data;
	private R result;

	public RequestParameter(){}
	
	public RequestParameter(PayChannel channel, RequestType type, P data){
		this.channel = channel;
		this.type = type;
		this.data = data;
	}
	
	public PayChannel getChannel() {
		return channel;
	}

	public void setChannel(PayChannel channel) {
		this.channel = channel;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public P getData() {
		return data;
	}

	public void setData(P data) {
		this.data = data;
	}
	
	public String toString(){
		return JsonUtils.toJson(this);
	}

	public R getResult() {
		return result;
	}

	public void setResult(R result) {
		this.result = result;
	}
}
