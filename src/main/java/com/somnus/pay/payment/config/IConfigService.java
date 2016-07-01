package com.somnus.pay.payment.config;

import java.util.List;

import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.PaymentChannel;
import com.somnus.pay.payment.pojo.PaymentConfig;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentPageChannel;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.pojo.QueryResult;

/**
 * 支付配置表的操作类接口
 */
public interface IConfigService {

    /**
     * 根据来源ID获取支付来源
     * @return
     */
    public PaymentSource getPaymentSourceById(String sourceId);

    /**
     * 根据来源ID获取returnUrl
     * @param sourceId
     * @return
     */
    public String getPaymentSourceUrlById(String sourceId);

     /**
     * 获得数据库相关key的value
     * @param key
     * @return
     */
    public String getStringValue(String key);

    /**
     * 获得相关渠道配置信息
     * @param zfPlatform
     * @return
     */
    public List<PaymentChannel> getBankListMap(String zfPlatform);

    /**
     * 更新支付系统相关配置信息
     * @param paymentConfig
     * @return
     */
    public boolean updateConfig(PaymentConfig paymentConfig);

    /**
     * 获得所有数据库配置列表
     * @return
     */
    public List<PaymentConfig> getPaymentConfigList();

    /**
     * 清空缓存的数据相关配置信息
     * @return
     */
    public boolean cacheDBConfigClean();

    /**
     * 获得默认平台的字符串列表信息
     * @param defaultZfPlatform
     * @return
     */
    public List<String> getDefaultBankList(String defaultZfPlatform);

    /**
     * 根据ips发送相应的清空请求并获得返回结果
     * @param ips
     * @param pwd
     */
    public String sendOtherCacheDBConfigClean(String ips, String pwd);

    /**
     * 通过是否是境外支付和UA，获得对应渠道列表
     * @param uaType
     * @param paymentOrder
     * @return
     */
    public List<PaymentChannel> getWapBankList(Integer uaType, PaymentOrder paymentOrder);


    /**
     * 根据境外支付类型进行判断整个支付页渠道对象
     * @param crossPay
     * @return
     */
    public boolean getIsEnabledBz(Integer crossPay);

    /**
     * 获取对应整个支付页渠道对象
     * @param crossPay
     * @return
     */
    public PaymentPageChannel getPaymentPageChannel(Integer crossPay);

    /**
     * 获取是否可以输入帮钻
     * @return
     */
    public boolean isDisabledEnabledBz(PaymentOrder paymentOrder);

    /**
     * 计算出帮钻最大值
     * @param paymentOrder
     * @return
     */
    public String getBzMaxValue(PaymentOrder paymentOrder);

    /**
     * 是否默认选中帮钻
     * @return
     */
    public boolean defaultSelectedBz(PaymentOrder paymentOrder);

    /**
     * 是否已帮钻显示模式
     * @param paymentOrder
     * @return
     */
    public boolean isBZModeDisplay(PaymentOrder paymentOrder);


    /**
     * 获取PC帮钻使用信息描述
     * @param paymentOrder
     * @return
     */
    public String getBzTipInfo(PaymentOrder paymentOrder);

    /**
     * 获取Wap/App帮钻使用信息描述
     * @param paymentOrder
     * @return
     */
    public String getBzWapTipInfo(PaymentOrder paymentOrder);

    /**
     * 读取配置信息中Integer值
     * @param key
     * @return
     */
    public Integer getIntegerValue(String key);

    /**
     * 分页获得所有数据库配置列表
     * @return
     */
    public QueryResult<PaymentConfig> getPaymentConfigListByPage(Page page, String keyName);


}
