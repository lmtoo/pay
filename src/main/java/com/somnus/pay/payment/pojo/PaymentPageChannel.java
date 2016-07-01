package com.somnus.pay.payment.pojo;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 支付页面所有渠道定义类
 * @author qingshu
 */
public class PaymentPageChannel implements Serializable {

	private static final long serialVersionUID = 5290492500860227445L;
    private static final int STATUS_OPEN = 1;

    private Integer id; // 渠道id
    private Integer isEnabledBz; //  是否支持帮钻支付
    private String bzLimit;//帮钻支付金额（1/1%）
    private Integer isDisabledEnabledBz; //  是否支持帮钻支付
    private Integer isBZModeDisplay; //  是否已帮钻模式显示
    private String bzTipInfo; //  PC帮钻使用描述信息
    private String bzWapTipInfo; //  WAP帮钻使用描述信息
    private String zfPlatformKey; // PC端支付平台区域对应支付渠道父类key
    private List<PaymentChannel> zfPlatformList; // PC端支付支付区域对应支付渠道
    private String payOnlineChannel; // PC端网银支付区域对应主渠道
    private String payOnlinePlatformKey; // PC端网银支付区域对应支付渠道父类key
    private List<PaymentChannel> payOnlinePlatformList; // PC端网银支付区域对应支付渠道
    private String payOnlineZlPlatformKey; // PC端网银支付区域对应银行直连支付渠道父类key
    private List<PaymentChannel> payOnlineZlPlatformList; // PC端网银支付区域对应银行直连支付渠道
    private Integer isNeedBankDirect; // PC端网银支付区域是否开启对应银行直连支付渠道
    private List<PaymentChannel> payOnlineMorePlatformList; // PC端网银支付区域对应银行直连/普通支付渠道
    private String fastPayChannel; // PC端快捷支付区域对应主渠道
    private String fastPayPlatformKey; // PC端快捷支付区域对应支付渠道父类key
    private List<PaymentChannel> fastPayPlatformList; // PC端快捷支付区域对应支付渠道
    private String fastPayZlPlatformKey; // PC端快捷支付区域对应银行直连支付渠道父类key
    private List<PaymentChannel> fastPayZlPlatformList; // PC端快捷支付区域对应银行直连支付渠道
    private Integer isNeedFPBankDirect; // PC端快捷支付区域是否开启对应银行直连支付渠道
    private List<PaymentChannel> fastPayMorePlatformList; // PC端快捷支付区域对应银行直连/普通支付渠道
    private String wapPlatformKey; // 手机端浏览器支付区域对应支付渠道父类key
    private List<PaymentChannel> wapPlatformList; // 手机端浏览器对应支付渠道
    private String wxBrowserPlatformKey; // 手机端微信浏览器支付区域对应支付渠道父类key
    private List<PaymentChannel> wxBrowserPlatformList; // 手机端微信浏览器对应支付渠道
    private String appPlatformKey; // 手机端帮我买APP支付区域对应支付渠道父类key
    private List<PaymentChannel> appPlatformList; // 手机端帮韩品APP对应支付渠道
    private String koreaAppPlatformKey; // 手机端帮韩品APP支付区域对应支付渠道父类key
    private List<PaymentChannel> koreaAppPlatformList; // 手机端帮韩品APP对应支付渠道
    private String payOnlineTips;//网银支付区域提示信息
    private String fastPayTips;//快捷支付区域提示信息
    private String payOnlineDivDataMps;//网银支付区域datamps
    private String fastPayDivDataMps;//快捷支付区域datamps
    private Integer status; // 是否有效
    private String desc;//相关描述
    private Date createTime; // 渠道创建时间
    private Date updateTime; // 最后更新时间

    public PaymentPageChannel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsEnabledBz() {
        return isEnabledBz;
    }

    public void setIsEnabledBz(Integer isEnabledBz) {
        this.isEnabledBz = isEnabledBz;
    }

    public String getZfPlatformKey() {
        return zfPlatformKey;
    }

    public void setZfPlatformKey(String zfPlatformKey) {
        this.zfPlatformKey = zfPlatformKey;
    }

    public List<PaymentChannel> getZfPlatformList() {
        return zfPlatformList;
    }

    public void setZfPlatformList(List<PaymentChannel> zfPlatformList) {
        this.zfPlatformList = zfPlatformList;
    }

    public String getPayOnlineChannel() {
        return payOnlineChannel;
    }

    public void setPayOnlineChannel(String payOnlineChannel) {
        this.payOnlineChannel = payOnlineChannel;
    }

    public String getPayOnlinePlatformKey() {
        return payOnlinePlatformKey;
    }

    public void setPayOnlinePlatformKey(String payOnlinePlatformKey) {
        this.payOnlinePlatformKey = payOnlinePlatformKey;
    }

    public List<PaymentChannel> getPayOnlinePlatformList() {
        return payOnlinePlatformList;
    }

    public void setPayOnlinePlatformList(List<PaymentChannel> payOnlinePlatformList) {
        this.payOnlinePlatformList = payOnlinePlatformList;
    }

    public String getPayOnlineZlPlatformKey() {
        return payOnlineZlPlatformKey;
    }

    public void setPayOnlineZlPlatformKey(String payOnlineZlPlatformKey) {
        this.payOnlineZlPlatformKey = payOnlineZlPlatformKey;
    }

    public List<PaymentChannel> getPayOnlineZlPlatformList() {
        return payOnlineZlPlatformList;
    }

    public void setPayOnlineZlPlatformList(List<PaymentChannel> payOnlineZlPlatformList) {
        this.payOnlineZlPlatformList = payOnlineZlPlatformList;
    }

    public Integer getIsNeedBankDirect() {
        return isNeedBankDirect;
    }

    public void setIsNeedBankDirect(Integer isNeedBankDirect) {
        this.isNeedBankDirect = isNeedBankDirect;
    }

    public String getFastPayChannel() {
        return fastPayChannel;
    }

    public void setFastPayChannel(String fastPayChannel) {
        this.fastPayChannel = fastPayChannel;
    }

    public String getFastPayPlatformKey() {
        return fastPayPlatformKey;
    }

    public void setFastPayPlatformKey(String fastPayPlatformKey) {
        this.fastPayPlatformKey = fastPayPlatformKey;
    }

    public List<PaymentChannel> getFastPayPlatformList() {
        return fastPayPlatformList;
    }

    public void setFastPayPlatformList(List<PaymentChannel> fastPayPlatformList) {
        this.fastPayPlatformList = fastPayPlatformList;
    }

    public String getFastPayZlPlatformKey() {
        return fastPayZlPlatformKey;
    }

    public void setFastPayZlPlatformKey(String fastPayZlPlatformKey) {
        this.fastPayZlPlatformKey = fastPayZlPlatformKey;
    }

    public List<PaymentChannel> getFastPayZlPlatformList() {
        return fastPayZlPlatformList;
    }

    public void setFastPayZlPlatformList(List<PaymentChannel> fastPayZlPlatformList) {
        this.fastPayZlPlatformList = fastPayZlPlatformList;
    }

    public Integer getIsNeedFPBankDirect() {
        return isNeedFPBankDirect;
    }

    public void setIsNeedFPBankDirect(Integer isNeedFPBankDirect) {
        this.isNeedFPBankDirect = isNeedFPBankDirect;
    }

    public String getWapPlatformKey() {
        return wapPlatformKey;
    }

    public void setWapPlatformKey(String wapPlatformKey) {
        this.wapPlatformKey = wapPlatformKey;
    }

    public String getWxBrowserPlatformKey() {
        return wxBrowserPlatformKey;
    }

    public void setWxBrowserPlatformKey(String wxBrowserPlatformKey) {
        this.wxBrowserPlatformKey = wxBrowserPlatformKey;
    }

    public String getAppPlatformKey() {
        return appPlatformKey;
    }

    public void setAppPlatformKey(String appPlatformKey) {
        this.appPlatformKey = appPlatformKey;
    }

    public String getKoreaAppPlatformKey() {
        return koreaAppPlatformKey;
    }

    public void setKoreaAppPlatformKey(String koreaAppPlatformKey) {
        this.koreaAppPlatformKey = koreaAppPlatformKey;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<PaymentChannel> getWapPlatformList() {
        return wapPlatformList;
    }

    public void setWapPlatformList(List<PaymentChannel> wapPlatformList) {
        this.wapPlatformList = wapPlatformList;
    }

    public List<PaymentChannel> getWxBrowserPlatformList() {
        return wxBrowserPlatformList;
    }

    public void setWxBrowserPlatformList(List<PaymentChannel> wxBrowserPlatformList) {
        this.wxBrowserPlatformList = wxBrowserPlatformList;
    }

    public List<PaymentChannel>  getAppPlatformList() {
        return appPlatformList;
    }

    public void setAppPlatformList(List<PaymentChannel>  appPlatformList) {
        this.appPlatformList = appPlatformList;
    }

    public List<PaymentChannel>  getKoreaAppPlatformList() {
        return koreaAppPlatformList;
    }

    public void setKoreaAppPlatformList(List<PaymentChannel>  koreaAppPlatformList) {
        this.koreaAppPlatformList = koreaAppPlatformList;
    }

    public List<PaymentChannel>  getPayOnlineMorePlatformList() {
        return payOnlineMorePlatformList;
    }

    public void setPayOnlineMorePlatformList(List<PaymentChannel>  payOnlineMorePlatformList) {
        this.payOnlineMorePlatformList = payOnlineMorePlatformList;
    }

    public List<PaymentChannel>  getFastPayMorePlatformList() {
        return fastPayMorePlatformList;
    }

    public void setFastPayMorePlatformList(List<PaymentChannel>  fastPayMorePlatformList) {
        this.fastPayMorePlatformList = fastPayMorePlatformList;
    }

    public String getPayOnlineTips() {
        return payOnlineTips;
    }

    public void setPayOnlineTips(String payOnlineTips) {
        this.payOnlineTips = payOnlineTips;
    }

    public String getFastPayTips() {
        return fastPayTips;
    }

    public void setFastPayTips(String fastPayTips) {
        this.fastPayTips = fastPayTips;
    }

    public String getPayOnlineDivDataMps() {
        return payOnlineDivDataMps;
    }

    public void setPayOnlineDivDataMps(String payOnlineDivDataMps) {
        this.payOnlineDivDataMps = payOnlineDivDataMps;
    }

    public String getFastPayDivDataMps() {
        return fastPayDivDataMps;
    }

    public void setFastPayDivDataMps(String fastPayDivDataMps) {
        this.fastPayDivDataMps = fastPayDivDataMps;
    }

    public Integer getIsDisabledEnabledBz() {
        return isDisabledEnabledBz;
    }

    public void setIsDisabledEnabledBz(Integer isDisabledEnabledBz) {
        this.isDisabledEnabledBz = isDisabledEnabledBz;
    }

    public String getBzLimit() {
        return bzLimit;
    }

    public void setBzLimit(String bzLimit) {
        this.bzLimit = bzLimit;
    }

    public Integer getIsBZModeDisplay() {
        return isBZModeDisplay;
    }

    public void setIsBZModeDisplay(Integer isBZModeDisplay) {
        this.isBZModeDisplay = isBZModeDisplay;
    }

    public String getBzTipInfo() {
        return bzTipInfo;
    }

    public void setBzTipInfo(String bzTipInfo) {
        this.bzTipInfo = bzTipInfo;
    }

    public String getBzWapTipInfo() {
        return bzWapTipInfo;
    }

    public void setBzWapTipInfo(String bzWapTipInfo) {
        this.bzWapTipInfo = bzWapTipInfo;
    }

    /**
     * 获取是否可以使用帮钻
     * @return
     */
    public boolean isEnabledBz() {
        return (null == isEnabledBz) ? false:(isEnabledBz.equals(STATUS_OPEN)) ;
    }

    /**
     * 获取是否支持网银直连银行
     * @return
     */
    public boolean isNeedBankDirect() {
        return (null == isNeedBankDirect) ? false:(isNeedBankDirect.equals(STATUS_OPEN)) ;
    }

    /**
     * 获取是否支持快捷直连银行
     * @return
     */
    public boolean isNeedFPBankDirect() {
        return (null == isNeedFPBankDirect) ? false:(isNeedFPBankDirect.equals(STATUS_OPEN)) ;
    }

    /**
     * 获取是否可以输入帮钻
     * @return
     */
    public boolean isDisabledEnabledBz() {
        return (null == isDisabledEnabledBz) ? false:(isDisabledEnabledBz.equals(STATUS_OPEN)) ;
    }

    /**
     * 是否默认选中帮钻（帮钻可以使用且禁用的话说明是需要默认选中）
     * @return
     */
    public boolean defaultSelectedBz() {
        return (null == isDisabledEnabledBz) ? false:(isDisabledEnabledBz.equals(STATUS_OPEN) && isEnabledBz()) ;
    }

    /**
     * 是否已帮钻显示模式
     * @return
     */
    public boolean isBZModeDisplay() {
        return (null == isBZModeDisplay) ? false:(isBZModeDisplay.equals(STATUS_OPEN)) ;
    }

    @Override
    public String toString() {
        return "PaymentPageChannel{" +
                "id=" + id +
                ", isEnabledBz=" + isEnabledBz +
                ", bzLimit='" + bzLimit + '\'' +
                ", isDisabledEnabledBz=" + isDisabledEnabledBz +
                ", isBZModeDisplay=" + isBZModeDisplay +
                ", bzTipInfo='" + bzTipInfo + '\'' +
                ", bzWapTipInfo='" + bzWapTipInfo + '\'' +
                ", zfPlatformKey='" + zfPlatformKey + '\'' +
                ", zfPlatformList=" + zfPlatformList +
                ", payOnlineChannel='" + payOnlineChannel + '\'' +
                ", payOnlinePlatformKey='" + payOnlinePlatformKey + '\'' +
                ", payOnlinePlatformList=" + payOnlinePlatformList +
                ", payOnlineZlPlatformKey='" + payOnlineZlPlatformKey + '\'' +
                ", payOnlineZlPlatformList=" + payOnlineZlPlatformList +
                ", isNeedBankDirect=" + isNeedBankDirect +
                ", payOnlineMorePlatformList=" + payOnlineMorePlatformList +
                ", fastPayChannel='" + fastPayChannel + '\'' +
                ", fastPayPlatformKey='" + fastPayPlatformKey + '\'' +
                ", fastPayPlatformList=" + fastPayPlatformList +
                ", fastPayZlPlatformKey='" + fastPayZlPlatformKey + '\'' +
                ", fastPayZlPlatformList=" + fastPayZlPlatformList +
                ", isNeedFPBankDirect=" + isNeedFPBankDirect +
                ", fastPayMorePlatformList=" + fastPayMorePlatformList +
                ", wapPlatformKey='" + wapPlatformKey + '\'' +
                ", wapPlatformList=" + wapPlatformList +
                ", wxBrowserPlatformKey='" + wxBrowserPlatformKey + '\'' +
                ", wxBrowserPlatformList=" + wxBrowserPlatformList +
                ", appPlatformKey='" + appPlatformKey + '\'' +
                ", appPlatformList=" + appPlatformList +
                ", koreaAppPlatformKey='" + koreaAppPlatformKey + '\'' +
                ", koreaAppPlatformList=" + koreaAppPlatformList +
                ", payOnlineTips='" + payOnlineTips + '\'' +
                ", fastPayTips='" + fastPayTips + '\'' +
                ", payOnlineDivDataMps='" + payOnlineDivDataMps + '\'' +
                ", fastPayDivDataMps='" + fastPayDivDataMps + '\'' +
                ", status=" + status +
                ", desc='" + desc + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
