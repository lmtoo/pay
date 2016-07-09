package com.somnus.pay.payment.config;

import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.pojo.QueryResult;

/**
 * @description: 支付来源配置服务接口
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/11/26
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/11/26       bai          1.0         新建作为支付来源配置服务接口
 */

public interface PaySourceConfigService extends PaySourceConfigReadable {

	/**
	 * 更新支付来源信息
	 * @param paymentSource
	 */
	public void updatePaymentSource(PaymentSource paymentSource);

	/**
	 * 新增支付来源信息
	 * @param paymentSource
	 */
	public void addPaymentSource(PaymentSource paymentSource);

	/**
	 * 删除支付来源信息
	 * @param sourceId
	 */
	public void removePaymentSource(String sourceId);

	/**
	 * 获取所有支付来源信息
	 * @return
	 */
	public QueryResult<PaymentSource> getAllByPage(Page page);

	/**
	 * 清空PaymentSource的相关缓存数据
	 * @param ips
	 * @param pwd
	 * @return
	 */
	public String sendOtherCachePaymentSourceDBClean(String ips, String pwd);

}
