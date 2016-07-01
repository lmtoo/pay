package com.somnus.pay.payment.dao;

import com.somnus.pay.payment.pojo.CustomsDeclaration;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.QueryResult;

import java.util.Map;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface PaymentOrderDao extends IBaseDao<PaymentOrder>{

	public PaymentOrder get(String orderId, int source);
	
	public PaymentOrder get(String orderId);

	/**
	 * 分页查询所有报关
	 * @param page
	 * @return
	 */
	public QueryResult<PaymentOrder> list(Page page,Map<String,Object> params);
	
	public PaymentOrder get(String orderId, String userId);
	
}
