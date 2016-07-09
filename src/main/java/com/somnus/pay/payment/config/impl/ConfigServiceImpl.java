package com.somnus.pay.payment.config.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.config.PayPlatformConfigReadable;
import com.somnus.pay.payment.config.PayPlatformConfigService;
import com.somnus.pay.payment.config.PaySourceConfigReadable;

import com.somnus.pay.payment.pojo.PaymentChannel;
import com.somnus.pay.payment.pojo.PaymentConfig;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentPageChannel;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;

/**
 * @description: 支付配置表的操作类
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/11/26
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/11/26   方东白          1.0         将各配置数据的初始化核心业务从配置中心中独立出去，在配置中心中调用各业务类的方法来完成初始化工作
 */

@Service
public class ConfigServiceImpl implements IConfigService{

    private Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);

    @Autowired
    private PaySourceConfigReadable paySourceConfigReadable;

    @Autowired
    private PayPlatformConfigReadable payPlatformConfigReadable;

    @Autowired
    private PayPlatformConfigService payPlatformConfigService;

    /**
     * 初始化DB配置信息转换java缓存
     * @throws Exception 
     */
    @PostConstruct
    public void initConfigService(){
        if(logger.isInfoEnabled()){
            logger.info("开始读取系统配置信息");
        }
        paySourceConfigReadable.refresh(); // 调用刷新方法初始化支付来源数据
        payPlatformConfigReadable.refresh(); // 初始化支付渠道数据
    }

    /**
     * 获取支付来源
     * @return
     */
    @Override
    public PaymentSource getPaymentSourceById(String sourceId) {
        return paySourceConfigReadable.getPaymentSourceById(sourceId);
    }

    /**
     * 根据来源ID获取returnUrl
     * @param sourceId
     * @return
     */
    @Override
    public String getPaymentSourceUrlById(String sourceId) {
        return paySourceConfigReadable.getPaymentSourceUrlById(sourceId);
    }

    /**
     * 获得数据库相关key的value
     * @param key
     * @return
     */
    @Override
	public String getStringValue(String key) {
		return payPlatformConfigReadable.getValue(key);
	}

    /**
     * 获得所有数据库配置列表
     * @return
     */
    @Override
    public List<PaymentConfig> getPaymentConfigList(){
        return payPlatformConfigReadable.getPaymentConfigList();
    }

    /**
     * 清空缓存的数据相关配置信息
     * @return
     */
    @Override
    public boolean cacheDBConfigClean() {
        return payPlatformConfigReadable.cacheDBConfigClean();
    }

    /**
     * 获得相关渠道配置信息
     * @param platform
     * @return
     */
    @Override
    public List<PaymentChannel> getBankListMap(String platform) {
        return payPlatformConfigReadable.getBankListMap(platform);
    }

    /**
     * 更新支付系统相关配置信息
     * @param paymentConfig
     * @return
     */
    @Override
    public boolean updateConfig(PaymentConfig paymentConfig) {
        return payPlatformConfigService.updateConfig(paymentConfig);
    }

    /**
     * 获得默认平台的字符串列表信息
     * @param defaultZfPlatform
     * @return
     */
    @Override
    public List<String> getDefaultBankList(String defaultZfPlatform){
        return payPlatformConfigReadable.getDefaultBankList(defaultZfPlatform);
    }

    /**
     * 根据ips发送相应的清空请求并获得返回结果
     * @return
     * @param ips
     */
    @Override
    public String sendOtherCacheDBConfigClean(String ips,String pwd){
        return payPlatformConfigReadable.sendOtherCacheDBConfigClean(ips, pwd);
    }

    /**
     * 通过是否是境外支付和UA，获得对应渠道列表
     * @param uaType
     * @param paymentOrder
     * @return
     */
    @Override
    public List<PaymentChannel> getWapBankList(Integer uaType, PaymentOrder paymentOrder){
        return payPlatformConfigReadable.getWapBankList(uaType, paymentOrder);
    }

    /**
     * 根据境外支付类型进行判断整个支付页渠道对象
     * @param crossPay
     * @return
     */
    @Override
    public boolean getIsEnabledBz(Integer crossPay) {
        return payPlatformConfigReadable.getIsEnabledBz(crossPay);
    }

    /**
     * 获取对应整个支付页渠道对象
     * @param crossPay
     * @return
     */
    @Override
    public PaymentPageChannel getPaymentPageChannel(Integer crossPay) {
        return  payPlatformConfigReadable.getPaymentPageChannel(crossPay);
    }

    /**
     * 获取是否可以输入帮钻
     * @param paymentOrder
     * @return
     */
    @Override
    public boolean isDisabledEnabledBz(PaymentOrder paymentOrder){
        return payPlatformConfigReadable.isDisabledEnabledBz(paymentOrder);
    }

    /**
     * 计算出帮钻最大值
     * @param paymentOrder
     * @return
     */
    @Override
    public String getBzMaxValue(PaymentOrder paymentOrder){
        return payPlatformConfigReadable.getBzMaxValue(paymentOrder);
    }

    /**
     * 是否默认选中帮钻
     * @return
     */
    public boolean defaultSelectedBz(PaymentOrder paymentOrder){
        return payPlatformConfigReadable.defaultSelectedBz(paymentOrder);
    }

    /**
     * 是否已帮钻显示模式
     * @param paymentOrder
     * @return
     */
    public boolean isBZModeDisplay(PaymentOrder paymentOrder){
        return payPlatformConfigReadable.isBZModeDisplay(paymentOrder);
    }

    /**
     * 获取帮钻使用信息描述
     * @param paymentOrder
     * @return
     */
    public String getBzTipInfo(PaymentOrder paymentOrder){
        return payPlatformConfigReadable.getBzTipInfo(paymentOrder);
    }

    /**
     * 获取帮钻使用信息描述
     * @param paymentOrder
     * @return
     */
    public String getBzWapTipInfo(PaymentOrder paymentOrder){
        return payPlatformConfigReadable.getBzWapTipInfo(paymentOrder);
    }

    public Integer getIntegerValue(String key){
        String value = this.getStringValue(key);
        Integer result = null;
        if(StringUtils.isNotEmpty(value) && StringUtils.isNumeric(value)){
            result = Integer.parseInt(value);
        }
        return result;
    }

    @Override
    public QueryResult<PaymentConfig> getPaymentConfigListByPage(Page page, String keyName) {
        List<PaymentConfig> list = payPlatformConfigReadable.getPaymentConfigList();
        List<PaymentConfig> paymentConfiglist = new ArrayList<PaymentConfig>();
        QueryResult<PaymentConfig> result = new QueryResult<PaymentConfig>();
        if(keyName != null && keyName.trim()!=""){
            for(int i = 0; i < list.size(); i++){
                if(keyName.equals(list.get(i).getKeyName())){
                    paymentConfiglist.add(list.get(i));
                }
            }
            page.setTotal(10l);
            result.setList(paymentConfiglist);
            result.setPage(page);
            return result;
        }
        page.setTotal(new Long(list.size()));
        int start = (page.getPageNum()-1) * page.getPageSize();
        int end = start + page.getPageSize();
        if(end > page.getTotal())end = page.getTotal().intValue();
        for(int i = start; i < end; i++){
            paymentConfiglist.add(list.get(i));
        }
        result.setList(paymentConfiglist);
        result.setPage(page);
        return result;
    }

}
