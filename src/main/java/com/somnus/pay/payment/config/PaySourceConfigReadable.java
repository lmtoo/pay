package com.somnus.pay.payment.config;

import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.IRefreshable;

/**
 * @description: 支付来源配置只读接口
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/11/26
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/11/26       bai          1.0         新建作为配置中心获取支付来源在内存中数据的接口
 */
public interface PaySourceConfigReadable extends IRefreshable {

	/**
	 * 根据支付来源ID获取支付来源信息
	 * 
	 * @param sourceId 支付来源ID
	 * @return 支付来源信息
	 */
	public PaymentSource getPaymentSourceById(String sourceId);

	/**
	 * 根据来源ID获取returnUrl
	 * @param sourceId
	 * @return
	 */
	public String getPaymentSourceUrlById(String sourceId);

	/**
	 * 获取相关sourceId的PaymentSource集合
	 * @param page
	 * @param sourceId
	 * @return
	 */
	public QueryResult<PaymentSource> getAllByPage(Page page, String sourceId);
}
