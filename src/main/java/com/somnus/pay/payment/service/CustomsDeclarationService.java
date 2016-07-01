package com.somnus.pay.payment.service;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.CustomsDeclaration;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;

import java.util.Map;

/**
 * @description: ${TODO}
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/25
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/25       bai          1.0             Why & What is modified
 */
public interface CustomsDeclarationService {

    public void save(CustomsDeclaration customsDeclaration);

    public void saveOrUpdate(CustomsDeclaration customsDeclaration);
	
	/**
	 * 分页查询所有支付通知
	 * @param page
	 * @return
	 */
	public QueryResult<CustomsDeclaration> list(Page page,Map<String,Object> params);

    /**
     * 根据联合主键查询报关信息
     * @param orderId
     * @param channel
     * @param bgConfigKey
     * @return
     */
    public CustomsDeclaration getByUPK(String orderId, PayChannel channel, String bgConfigKey);

    /**
     * 根据联合主键组成CustomsDeclarationUPK进行查询
     * @param orderId
     * @param channel
     * @param bgConfigKey
     * @return
     */
    public CustomsDeclaration getById(String orderId, PayChannel channel, String bgConfigKey);

    /**
     * 更新报关信息
     * @param cd
     */
    public void update(CustomsDeclaration cd);

    /**
     * 删除报关信息
     * @param cd
     */
    public void delete(CustomsDeclaration cd);

}
