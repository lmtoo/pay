package com.somnus.pay.payment.dao;

import com.somnus.pay.payment.pojo.Callback;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;

import java.util.Map;

/**
 *  @description: 第三方支付渠道DAO<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-25       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface CallbackDao extends IBaseDao<Callback> {

	/**
	 * 分页查询所有支付回调通知
	 * @param page
	 * @return
	 */
	public QueryResult<Callback> list(Page page,Map<String,Object> params);
	
}
