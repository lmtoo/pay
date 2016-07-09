package com.somnus.pay.payment.dao;

import com.somnus.pay.payment.pojo.Notify;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;

import java.util.Map;

/**
 *  @description: 请求发起方支付成功通知DAO<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface NotifyDao extends IBaseDao<Notify> {

	/**
	 * 分页查询所有支付通知
	 * @param page
	 * @return
	 */
	public QueryResult<Notify> list(Page page,Map<String,Object> params);
	
}
