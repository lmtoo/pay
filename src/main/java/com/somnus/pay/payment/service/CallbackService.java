package com.somnus.pay.payment.service;

import com.somnus.pay.payment.enums.CallbackStatus;
import com.somnus.pay.payment.pojo.Callback;
import com.somnus.pay.payment.pojo.Callback.CallbackId;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;

import java.util.Map;

/**
 *  @description: 第三方支付渠道服务<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-25       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface CallbackService {

	/**
	 * 批量保存回调通知
	 * @param callbacks
	 */
	public void save(Callback... callbacks);
	
	/**
	 * 更新回调通知
	 * @param callback
	 */
	public void update(Callback callback);
	
	/**
	 * 根据ID更新支付回调通知状态
	 * @param id
	 * @param status
	 * @param memo
	 */
	public void changeStatus(CallbackId id, CallbackStatus status, String memo);
	
	/**
	 * 根据ID获取支付回调通知
	 * @param id
	 * @return
	 */
	public Callback get(CallbackId id);
	
	/**
	 * 分页查询所有支付回调
	 * @param page
	 * @return
	 */
	public QueryResult<Callback> list(Page page,Map<String,Object> params);
	
	/**
	 * 指定的支付回调通知是否已存在
	 * @param id
	 * @return
	 */
	public boolean exist(CallbackId id);
	
}
