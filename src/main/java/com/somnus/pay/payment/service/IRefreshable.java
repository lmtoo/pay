package com.somnus.pay.payment.service;

/**
 * 配置刷新接口
 * 
 * @author 丹青生
 *
 * @date 2015-11-27
 */
public interface IRefreshable {

	/**
	 * 刷新配置数据
	 * @throws Exception 
	 */
	public void refresh();

	/**
	 * 最近一次刷新时间(时间戳)
	 * @return 时间戳
	 */
	public long getLastRefreshTime();
	
	/**
	 * 自系统启动以来的刷新次数
	 * @return
	 */
	public long getCount();
	
}
