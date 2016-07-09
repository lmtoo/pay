package com.somnus.pay.payment.config.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.config.PayPlatformConfigService;
import com.somnus.pay.payment.dao.IPaymentConfigDao;
import com.somnus.pay.payment.enums.UAType;
import com.somnus.pay.payment.pojo.PaymentChannel;
import com.somnus.pay.payment.pojo.PaymentConfig;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentPageChannel;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.payment.util.Constants;

/**
 * @description: 支付渠道数据初始化业务类
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/11/26
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/11/26       bai          1.0          将支付渠道数据初始化核心业务从配置中心中移出来
 */
@Service
public class PayPlatformConfigServiceImpl extends AbstractConfigService implements PayPlatformConfigService{

    private static final Logger logger = LoggerFactory.getLogger(PayPlatformConfigServiceImpl.class);

    @Autowired
    IPaymentConfigDao iPaymentConfigDao;
    @Autowired
    private IPayService iPayService;
    
	private List<PaymentConfig> pcList = null;
    private Map<String,String> keyValues = null;
    private Map<String,List<PaymentChannel>> bankListMap = null;
    private Map<String,List<String>> defaultBankKeyMap = null;
    private Map<Integer,PaymentPageChannel> paymentPageChannelMap = null;
    private List<String> adminMobiles = null;

    public PayPlatformConfigServiceImpl() {
        super("支付渠道（平台）");
    }

    @Override
    protected void doRefresh() {
    	PayPlatformConfigLoader loader = new PayPlatformConfigLoader();
    	loader.load();
    	this.pcList = loader.getPcListTemp();
    	this.keyValues = loader.getKeyValuesTemp();
    	this.bankListMap = loader.getBankListMapTemp();
    	this.defaultBankKeyMap = loader.getDefaultBankKeyMapTemp();
    	this.paymentPageChannelMap = loader.getPaymentPageChannelMapTemp();
        this.adminMobiles = loader.getAdminMobilesTemp();
    }

    /**
     * 获得数据库相关key的value
     * @param key
     * @return
     */
    @Override
    public String getValue(String key) {
        try {
            if (null == keyValues || keyValues.isEmpty()) {
                doRefresh();
            }
            return keyValues.get(key);
        } catch (Exception e) {
            logger.error("getStringValue error,key:[" + key + "]", e);
        }
        return null;
    }

    /**
     * 获得所有数据库配置列表
     * @return
     */
    @Override
    public List<PaymentConfig> getPaymentConfigList(){
        if(null == pcList || pcList.isEmpty()){
            doRefresh();
        }
        return pcList;
    }

    /**
     * 清空缓存的数据相关配置信息
     * @return
     */
    @Override
    public boolean cacheDBConfigClean() {
        try{
            doRefresh();
        }catch (Exception e){
            logger.error("Process PaymentConfig cacheDBConfigClean error e:",e);
            return false;
        }
        return true;
    }

    /**
     * 获得相关渠道配置信息
     * @param platform
     * @return
     */
    @Override
    public List<PaymentChannel> getBankListMap(String platform) {
        if(null == keyValues || keyValues.isEmpty()){
            doRefresh();
        }
        if(null==bankListMap || bankListMap.isEmpty() || (null == bankListMap.get(platform))){
            return new ArrayList<PaymentChannel>();
        }
        return bankListMap.get(platform);
    }

    /**
     * 更新支付系统相关配置信息
     * @param paymentConfig
     * @return
     */
    @Override
    public boolean updateConfig(PaymentConfig paymentConfig) {
        if(logger.isInfoEnabled()){
            logger.info("Process updateConfig method: paymentConfig[{}]",paymentConfig.toString());
        }
        try{
            if(null == paymentConfig.getId()){
                Date now =new Date();
                paymentConfig.setCreateTime(now);
                paymentConfig.setUpdateTime(now);
                iPaymentConfigDao.save(paymentConfig);
            }else{
                PaymentConfig temp = iPaymentConfigDao.getById(paymentConfig.getId());
                if(null!=temp){
                    if(StringUtils.isBlank(paymentConfig.getMemo())){
                        paymentConfig.setMemo(temp.getMemo());
                    }
                    paymentConfig.setCreateTime(temp.getCreateTime());
                }
                paymentConfig.setUpdateTime(new Date());
                iPaymentConfigDao.update(paymentConfig);
            }
            return true;
        }catch (Exception e){
            logger.error("updateConfig e:"+e);
        }
        return false;
    }


    /**
     * 获得默认平台的字符串列表信息
     * @param defaultZfPlatform
     * @return
     */
    @Override
    public List<String> getDefaultBankList(String defaultZfPlatform){
        try{
            return defaultBankKeyMap.get(defaultZfPlatform);
        }catch (Exception e){
            logger.error("getDefaultBankList [" + defaultZfPlatform + "] error",e);
        }
        return new ArrayList<String>();
    }


    /**
     * 根据ips发送相应的清空请求并获得返回结果
     * @return
     * @param ips
     */
    @Override
    public String sendOtherCacheDBConfigClean(String ips, String pwd){
        logger.info("Process PaymentConfig sendOtherCacheDBConfigClean method: ips={}" + ips);
        StringBuilder result = new StringBuilder();
        if (StringUtils.isNotBlank(ips)) {
            String[] ipsArray = ips.split("\\|");
			for (String ip : ipsArray) {
                String response = StringUtils.isBlank(ip) ? "" :HttpClientUtils.get("http://"+ip + Constants.CACHE_CONFIG_CLEAN +"?pwd="+pwd, null);
                result.append(ip).append(":").append(response).append("\r");
            }
        } else if(cacheDBConfigClean()) {
            iPayService.cacheUserChooseConfigClean();
            result.append(StatusCode.SUCCESS_CODE);
        }
        return result.toString();
    }

    /**
     * 通过是否是境外支付和UA，获得对应渠道列表
     * @param uaType
     * @param paymentOrder
     * @return
     */
    @Override
    public List<PaymentChannel> getWapBankList(Integer uaType, PaymentOrder paymentOrder){
        List<PaymentChannel> res = null;
        PaymentPageChannel paymentPageChannel = getPaymentPageChannel(paymentOrder.getCrossPay());
        if(UAType.KOREA_APP.getValue().equals(uaType)){
            res = paymentPageChannel.getKoreaAppPlatformList();
        } else if(UAType.B5M_APP.getValue().equals(uaType)){
            res = paymentPageChannel.getAppPlatformList();
        } else if(UAType.WX_BROWSER.getValue().equals(uaType)){
            res = paymentPageChannel.getWxBrowserPlatformList();
        } else{
            res = paymentPageChannel.getWapPlatformList();
        }
        return res;
    }

    /**
     * 根据境外支付类型进行判断整个支付页渠道对象
     * @param crossPay
     * @return
     */
    @Override
    public boolean getIsEnabledBz(Integer crossPay) {
        PaymentPageChannel ppc = paymentPageChannelMap.get(crossPay);
        return (null != ppc)?ppc.isEnabledBz():false;
    }

    /**
     * 获取对应整个支付页渠道对象
     * @param crossPay
     * @return
     */
    @Override
    public PaymentPageChannel getPaymentPageChannel(Integer crossPay) {
        if(null == paymentPageChannelMap || paymentPageChannelMap.isEmpty()){
            doRefresh();
        }
        return paymentPageChannelMap.get(crossPay);
    }

    /**
     * 获取是否可以输入帮钻
     * @param paymentOrder
     * @return
     */
    @Override
    public boolean isDisabledEnabledBz(PaymentOrder paymentOrder){
        PaymentPageChannel ppc = paymentPageChannelMap.get(paymentOrder.getCrossPay());
        return (null != ppc)?ppc.isDisabledEnabledBz():false;
    }

    /**
     * 计算出帮钻最大值
     * @param paymentOrder
     * @return
     */
    @Override
    public String getBzMaxValue(PaymentOrder paymentOrder){
        PaymentPageChannel ppc = paymentPageChannelMap.get(paymentOrder.getCrossPay());
        if(null!=ppc){
            try {
                String bzLimit = ppc.getBzLimit();
                if(null == paymentOrder || StringUtils.isBlank(bzLimit)){
                    return "";
                }
                if(bzLimit.matches("[0-9]+%")){
                    BigDecimal result = new BigDecimal("0.00");
                    Double amount = paymentOrder.getAmount();
                    Double limit = Double.parseDouble(bzLimit.substring(0, bzLimit.indexOf("%")));
                    result = result.add(new BigDecimal(amount.toString()));
                    result = result.multiply(new BigDecimal(limit));
                    bzLimit = result.setScale(0, BigDecimal.ROUND_DOWN)+"";
                }
                Long bzMaxValue = Long.parseLong(StringUtils.defaultIfBlank(bzLimit,"0"));
                if (bzMaxValue >= paymentOrder.getBzUsable()) {
                    bzLimit = paymentOrder.getBzUsable().longValue()+"";
                }
                logger.info("支付付订单:[{}],计算后最大支付帮钻:[{}]",paymentOrder.getOrderId(),bzLimit);
                return bzLimit;
            }catch (Exception e){
                logger.warn("转换最大可用帮钻数量异常",e);
            }
        }
        return "";
    }

    /**
     * 是否默认选中帮钻
     * @return
     */
    @Override
    public boolean defaultSelectedBz(PaymentOrder paymentOrder) {
        PaymentPageChannel ppc = paymentPageChannelMap.get(paymentOrder.getCrossPay());
        return (null != ppc)?ppc.defaultSelectedBz():false;
    }

    /**
     * 是否已帮钻显示模式
     * @param paymentOrder
     * @return
     */
    @Override
    public boolean isBZModeDisplay(PaymentOrder paymentOrder){
        PaymentPageChannel ppc = paymentPageChannelMap.get(paymentOrder.getCrossPay());
        return (null != ppc)?ppc.isBZModeDisplay():false;
    }

    /**
     * 获取帮钻使用信息描述
     * @param paymentOrder
     * @return
     */
    @Override
    public String getBzTipInfo(PaymentOrder paymentOrder){
        PaymentPageChannel ppc = paymentPageChannelMap.get(paymentOrder.getCrossPay());
        return (null != ppc)?ppc.getBzTipInfo():"";
    }

    /**
     * 获取Wap/App帮钻使用信息描述
     * @param paymentOrder
     * @return
     */
    @Override
    public String  getBzWapTipInfo(PaymentOrder paymentOrder){
        PaymentPageChannel ppc = paymentPageChannelMap.get(paymentOrder.getCrossPay());
        return (null != ppc)?ppc.getBzWapTipInfo():"";
    }

    /**
     * 获取支付系统后台可登录的手机号
     * @return
     */
    @Override
    public List<String> getAdminMobiles(){
        return this.adminMobiles;
    }

    /**
     * 内部类统一处理DB配置信息的加载
     */
    private class PayPlatformConfigLoader {
    	
    	private List<PaymentConfig> pcListTemp = null;
        private Map<String,String> keyValuesTemp = new ConcurrentHashMap<String,String>();
        private Map<String,List<PaymentChannel>> bankListMapTemp = new ConcurrentHashMap<String,List<PaymentChannel>>();
        private Map<String,List<String>> defaultBankKeyMapTemp = new ConcurrentHashMap<String,List<String>>();
        private Map<Integer,PaymentPageChannel> paymentPageChannelMapTemp = new ConcurrentHashMap<Integer,PaymentPageChannel>();

        public void load() {
            pcListTemp = iPaymentConfigDao.getAll();
            if(CollectionUtils.isNotEmpty(pcListTemp)){
                for (PaymentConfig pc : pcListTemp) {
                    keyValuesTemp.put(pc.getKeyName(),pc.getKeyValue());
                    if(logger.isInfoEnabled()){
                        logger.info("数据库中配置表信息：[{}] = [{}]", pc.getKeyName(), pc.getKeyValue());
                    }
                }
                //初始化所有支付渠道对应key（无重复）
                initDefaultBankMap(Constants.DEFAULT_ZF_PLATFORM);
                initDefaultBankMap(Constants.DEFAULT_PAYONLINE_PLATFORM);
                initDefaultBankMap(Constants.DEFAULT_FASTPAY_PLATFORM);

                //初始化支付页面的对应需展示的所有渠道
                initPaymentPageChannelMap(Constants.PAYPAGE_ALL_CHANNEL);

                if(logger.isInfoEnabled()){
                    logger.info("默认整个系统可选银行渠道KEYs:[{}]", defaultBankKeyMapTemp);
                    logger.info("支付平台对应所有银行渠道:[{}]", bankListMapTemp);
                    logger.info("支付页面对应的所有支付渠道:[{}]", defaultBankKeyMapTemp);
                }
            }else {
                pcListTemp = new ArrayList<PaymentConfig>(0);
			}
        }
    	
    	/**
         * 初始化整个系统默认可选银行渠道KEY及默认相应排序
         * @param defaultZfPlatform
         */
        private void initDefaultBankMap(String defaultZfPlatform) {
            logger.info("解析默认整个系统可选银行渠道KEY:[{}]", defaultZfPlatform);
            try {
                List<String> bankList = JSONArray.parseArray(keyValuesTemp.get(defaultZfPlatform), String.class);
                defaultBankKeyMapTemp.put(defaultZfPlatform, bankList == null ? new ArrayList<String>(0) : bankList);
            } catch (Exception e) {
                logger.error("解析默认整个系统可选银行渠道KEYs:[" + defaultZfPlatform + "]", e);
            }
        }
        
        /**
         * 初始化BankMap主要用于缓存并显示不同渠道,并返回Banklist
         * @param zfPlatform
         */
        private List<PaymentChannel> initBankMapReturnList(String zfPlatform) {
            if (logger.isInfoEnabled()) {
                logger.info("解析初始银行列表:[{}]", zfPlatform);
            }
            try {
                if (StringUtils.isNotBlank(keyValuesTemp.get(zfPlatform))) {
                    List<PaymentChannel> bankList = JSONArray.parseArray(keyValuesTemp.get(zfPlatform), PaymentChannel.class);
                    if (null != bankList && !bankList.isEmpty()) {
                        // 去掉设置关闭的渠道
                        Iterator<PaymentChannel> iter = bankList.iterator();
                        while (iter.hasNext()) {
                            PaymentChannel temp = iter.next();
                            if (temp.getStatus().equals(0)) {
                                iter.remove();
                            }
                        }
                        bankListMapTemp.put(zfPlatform, bankList);
                    }
                    return bankList;
                }
            } catch (Exception e) {
                logger.error("解析初始银行列表[" + zfPlatform + "]出错", e);
            }
            return null;
        }

        /**
         * 支付页面所有支付渠道的统一配置
         * @param payPageAllChannel
         */
        private void initPaymentPageChannelMap(String payPageAllChannel) {
            List<PaymentPageChannel> bankList = JSONArray.parseArray(keyValuesTemp.get(payPageAllChannel), PaymentPageChannel.class);
            for (PaymentPageChannel ppc : bankList) {
                logger.info("支付页面所有支付渠道的统一配置PaymentPageChannel:[{}]", ppc);
                ppc.setZfPlatformList(initBankMapReturnList(ppc.getZfPlatformKey()));
                ppc.setPayOnlinePlatformList(initBankMapReturnList(ppc.getPayOnlinePlatformKey()));
                ppc.setPayOnlineZlPlatformList(initBankMapReturnList(ppc.getPayOnlineZlPlatformKey()));
                List<PaymentChannel> temp1 = new ArrayList<PaymentChannel>();
                if(ppc.getPayOnlinePlatformList() != null){
                    temp1.addAll(ppc.getPayOnlinePlatformList());
                }
                if(ppc.getPayOnlineZlPlatformList() != null){
                    temp1.addAll(ppc.getPayOnlineZlPlatformList());
                }
                ppc.setPayOnlineMorePlatformList(temp1);

                ppc.setFastPayPlatformList(initBankMapReturnList(ppc.getFastPayPlatformKey()));
                ppc.setFastPayZlPlatformList(initBankMapReturnList(ppc.getFastPayZlPlatformKey()));
                List<PaymentChannel> temp2 = new ArrayList<PaymentChannel>();
                if(ppc.getFastPayPlatformList() != null){
                    temp2.addAll(ppc.getFastPayPlatformList());
                }
                if(ppc.getFastPayZlPlatformList() != null){
                    temp2.addAll(ppc.getFastPayZlPlatformList());
                }
                ppc.setFastPayMorePlatformList(temp2);

                ppc.setWapPlatformList(initBankMapReturnList(ppc.getWapPlatformKey()));
                ppc.setWxBrowserPlatformList(initBankMapReturnList(ppc.getWxBrowserPlatformKey()));
                ppc.setAppPlatformList(initBankMapReturnList(ppc.getAppPlatformKey()));
                ppc.setKoreaAppPlatformList(initBankMapReturnList(ppc.getKoreaAppPlatformKey()));
                paymentPageChannelMapTemp.put(ppc.getId(),ppc);
            }
            logger.info("支付页面所有支付渠道的统一配置:[{}]", paymentPageChannelMapTemp);
        }

        public List<PaymentConfig> getPcListTemp() {
            return pcListTemp;
        }

        public Map<String, String> getKeyValuesTemp() {
            return keyValuesTemp;
        }

        public Map<String, List<PaymentChannel>> getBankListMapTemp() {
            return bankListMapTemp;
        }

        public Map<String, List<String>> getDefaultBankKeyMapTemp() {
            return defaultBankKeyMapTemp;
        }

        public Map<Integer, PaymentPageChannel> getPaymentPageChannelMapTemp() {
            return paymentPageChannelMapTemp;
        }

        public List<String> getAdminMobilesTemp() {
            String mobilsArrStr = keyValuesTemp.get(Constants.ADMIN_MOBILES);
            List<String> mobiles = new ArrayList();
            if(StringUtils.isNotBlank(mobilsArrStr)){
                mobiles = JSONArray.parseArray(mobilsArrStr, String.class);
            }
            logger.info("支付管理页面的相关手机号列表:[{}]", mobiles);
            return mobiles;
        }
    }
    
}
