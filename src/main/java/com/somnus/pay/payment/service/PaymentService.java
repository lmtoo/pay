package com.somnus.pay.payment.service;

import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;

/**
 *  @description: 支付服务(不带事务控制)<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-20<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-20       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface PaymentService {

	/**
	 * 支付核心处理逻辑
	 * @param paymentOrder
	 * @return
	 */
	public String pay(PaymentOrder paymentOrder);
	
	/**
	 * 确认并更新订单的支付结果
	 * @param orderId
	 * @return
	 */
	public boolean confirmAndUpdateOrder(String orderId);
	
	/**
	 * 更新订单为支付成功
	 * @param paymentResult
	 */
	public void updateOrder2Success(PaymentResult paymentResult);

	/**
	 * 兼容老业务判断sourceId是否可以使用帮钻支付(帮钻充值等)
	 * @param sourceId
	 * @return
	 */
	public boolean isBZEnabledSoucreIds(Integer sourceId);

}
