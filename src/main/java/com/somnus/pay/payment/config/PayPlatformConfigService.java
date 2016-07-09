package com.somnus.pay.payment.config;

import com.somnus.pay.payment.pojo.PaymentConfig;

/**
 * @description: 初始化支付渠道（平台）接口
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/11/26
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/11/26       bai          1.0         新建初始化支付渠道（平台）接口
 */
public interface PayPlatformConfigService extends PayPlatformConfigReadable{
    /**
     * 更新支付系统相关配置信息
     * @param paymentConfig
     * @return
     */
    public boolean updateConfig(PaymentConfig paymentConfig);
}
