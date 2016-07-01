package com.somnus.pay.payment.service;

import java.util.List;
import java.util.Map;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.pojo.Switch;
import com.somnus.pay.payment.thirdPay.RequestType;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-31<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-31       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface PaymentChannelSwitchService {

	public void toggle(PayChannel channel, RequestType type);
	
	public void toggle(String key);
	
	public void setValue(PayChannel channel, RequestType type, boolean value);
	
	public boolean getValue(PayChannel channel, RequestType type);
	
	public List<Switch> getAll();
	
	public QueryResult<Switch> list(Page page);

	public QueryResult<Switch> list(Page page, Map<String, Object> params);

}
