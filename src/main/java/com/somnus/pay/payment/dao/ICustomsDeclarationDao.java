package com.somnus.pay.payment.dao;

import java.util.Map;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.CustomsDeclaration;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;

/**
 * @description: ${TODO}
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/25
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/25     方东白          1.0             Why & What is modified
 */
public interface ICustomsDeclarationDao extends IBaseDao<CustomsDeclaration>{
	/**
	 * 分页查询所有报关
	 * @param page
	 * @return
	 */
	public QueryResult<CustomsDeclaration> list(Page page,Map<String,Object> params);

	public CustomsDeclaration getByUPK(String orderId, PayChannel channel, String bgConfigKey);


}
