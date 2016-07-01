package com.somnus.pay.payment.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 统计订单支付成功来源数量
 * 
 * @author Mike
 *
 */
public class Log4JPaySource {

	protected static Log logger = LogFactory.getLog(Log4JPaySource.class);

	/**
	 * 
	 * @param orderId
	 *            订单
	 * @param source
	 *            支付来源
	 * @param currentTime
	 *            进入时间
	 * @param sericalNum
	 *            序列号
	 * @param dsrc
	 *            备注
	 */
	public static void i(String source) {
		logger.warn("Log4JPaySource " + source);
	}

}
