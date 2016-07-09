package com.somnus.pay.payment.service;

import com.somnus.pay.payment.enums.NotifyStatus;
import com.somnus.pay.payment.pojo.Notify;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.pojo.Notify.NotifyId;

import java.util.Map;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface NotifyService {

	public void save(Notify notify);
	
	public Notify lock(NotifyId id);
	
	public void update(Notify notify);
	
	public void changeStatus(NotifyId id, NotifyStatus status, String data, String memo);
	
	/**
	 * 分页查询所有支付通知
	 * @param page
	 * @return
	 */
	public QueryResult<Notify> list(Page page,Map<String,Object> params);
	
}
