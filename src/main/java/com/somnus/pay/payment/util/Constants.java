package com.somnus.pay.payment.util;


public class Constants {
    public static final String SHORT_URL_KEY = "short_";
    public static final String NATIVE_URL_KEY = "native_";
    public static final String MEMCACHE_ACTIVITY_SEND_KEY = "activity1_";
    public static final String MEMCACHE_PAYING_KEY = "memcache_paying_key_"; //正在支付中订单号缓存前缀


    /**
     * 支付方式
     */
    // APP扫码支付
    public static final String PAY_FROM_APP_NATIVE = "APP_NATIVE";
    // 使用b5m APP 支付
    public static final String PAY_FROM_APP_B5M = "B5MAPP";

    /**
     * 
     */
    public static final int ACTIVITY_EVENT_ID = 1;
    
    public static final int ACTIVITY_EVENT_BZAOMUNT  = 500 ;
    
    public static final String ACTIVITY_EVENT_MEMO  = "首笔订单支付成功返还帮钻" ;

    /**
     *支付回调地址（根据环境）
     */
    public final static String PAY_BACK_DOMAIN = ConfigUtil.getValue("backDomain");
    /**
     *支付回调地址（online配置）
     */
    public final static String PAY_ONLINE_BACK_DOMAIN = ConfigUtil.getValue("onlineBackDomain");
    /**
     *B5Mserver地址
     */
    public final static String B5M_DOMAIN = ConfigUtil.getValue("www.server");
    /**
     *B5Cserver地址
     */
    public final static String B5C_WAP_DOMAIN = ConfigUtil.getValue("b5cwap.server");
    /**
     *B5M手机端server地址
     */
    public final static String B5M_MOBILE_DOMAIN = ConfigUtil.getValue("m.server");
    /**
     *用户中心server地址
     */
    public final static String UCENTER_DOMAIN = ConfigUtil.getValue("ucenter.server");
    /**
     *AppServer地址为请求获取短链接
     */
    public final static String APP_DOMAIN = ConfigUtil.getValue("app.server");
    /**
     *配置新成功页面地址
     */
    public final static String NEW_RESULT_PATH = ConfigUtil.getValue("NEW_RESULT_PATH");

    /**
     * 微信新appV3接口订单号重复问题，预存在MEMCACHE中的key
     */
    public final static String PAY_WX_OTHERAPP_MEMCACHE_KEY  = "PAY_WX_OTHERAPP_ORDERID_";
    
    public final static String PAY_WX_OTHERAPP_MEMCACHE_KEY_B5M  = "PAY_WX_OTHERAPP_ORDERID_B5M_";

    /**
     * MetaQ请求出错的相关前缀
     */
    public final static String METAQ_REQUEST_PREFIX  = "METAQ:";

    /**
     * 手动更新操作时使用其对操作名进行加密
     */
    public final static String CRM_UPDATE_STATUS_OPERATION = ConfigUtil.getValue("CRM_UPDATE_STATUS_OPERATION");

    /**
     * 支付顶条缓存在MEMCACHE中的KEY
     */
    public final static String MEMCACHE_PAGE_HEAD_MODEL  = ConfigUtil.getValue("MEMCACHE_PAY_PAGE_HEAD_MODEL");

    /**
     * 区分是否合并付款的前缀
     */
    public static final String COMBINED_ORDER_PREFIX="MO_";

    /**
     * 未支付状态值字符串
     */
    public static final String NOPAY_STATUS_STR = "status=0";

    /**
     * 支付成功状态值字符串
     */
    public static final String PAID_STATUS_STR = "status=1";

    /**
     * 获取配置文件环境名-》stage，prod，online为空
     */
    public final static String PAY_ENVIRONMENT  = ConfigUtil.getValue("environment");

    /**
     * 帮我买内部WAP/APP返回json头的key
     */
    public static final String B5M_RETURN_CODE_KEY = "b5m_wap_pay_code";
    public static final String B5M_RETURN_DATA_KEY = "b5m_wap_pay_data";

    /**
     * 支付环境内部回调域名
     */
    public static final String PROD_PAY_DOMAIN = "http://pay.prod.b5m.com";
    public static final String STAGE_PAY_DOMAIN = "http://pay.stage.b5m.com";


    /**
     * 国内支付渠道配置KEY
     */
    //支付渠道显示KEY
    public static final String ZF_PLATFORM = "ZF_PLATFORM";
    public static final String PAYONLINE_PLATFORM = "PAYONLINE_PLATFORM";
    public static final String PAYONLINE_ZL_PLATFORM = "PAYONLINE_ZL_PLATFORM";
    public static final String FASTPAY_PLATFORM = "FASTPAY_PLATFORM";
    public static final String FASTPAY_ZL_PLATFORM = "FASTPAY_ZL_PLATFORM";
    public static final String PAYONLINE_MORE_PLATFORM = "PAYONLINE_MORE_PLATFORM";
    public static final String FASTPAY_MORE_PLATFORM = "FASTPAY_MORE_PLATFORM";
    //网银/快捷主渠道KEY
    public static final String PAYONLINE_CHANNEL = "PAYONLINE_CHANNEL";
    public static final String FASTPAY_CHANNEL = "FASTPAY_CHANNEL";
    public static final String IS_NEED_BANK_DIRECT = "IS_NEED_BANK_DIRECT";
    public static final String IS_NEED_FP_BANK_DIRECT = "IS_NEED_FP_BANK_DIRECT";
    //WAP/AAP支付渠道显示KEY
    public static final String WAP_PLATFORM = "WAP_PLATFORM";
    public static final String WXBROWSER_PLATFORM = "WXBROWSER_PLATFORM";
    public static final String APP_PLATFORM = "APP_PLATFORM";
    public static final String KOREA_APP_PLATFORM = "KOREA_APP_PLATFORM";
    //网银/快捷支付提示信息配置KEY
    public static final String PAYONLINE_TIPS = "PAYONLINE_TIPS";
    public static final String FASTPAY_TIPS = "FASTPAY_TIPS";

    //是否需要区分境外支付KEY
    public static final String IS_NEED_CROSSPAY = "IS_NEED_CROSSPAY";

    /**
     * 境外支付渠道配置KEY(香港)
     */
    //支付渠道显示KEY
    public static final String CP_ZF_PLATFORM = "CP_ZF_PLATFORM";
    public static final String CP_PAYONLINE_PLATFORM = "CP_PAYONLINE_PLATFORM";
    public static final String CP_PAYONLINE_ZL_PLATFORM = "CP_PAYONLINE_ZL_PLATFORM";
    public static final String CP_FASTPAY_PLATFORM = "CP_FASTPAY_PLATFORM";
    public static final String CP_FASTPAY_ZL_PLATFORM = "CP_FASTPAY_ZL_PLATFORM";
    public static final String CP_PAYONLINE_MORE_PLATFORM = "CP_PAYONLINE_MORE_PLATFORM";
    public static final String CP_FASTPAY_MORE_PLATFORM = "CP_FASTPAY_MORE_PLATFORM";
    //网银/快捷主渠道KEY
    public static final String CP_PAYONLINE_CHANNEL = "CP_PAYONLINE_CHANNEL";
    public static final String CP_FASTPAY_CHANNEL = "CP_FASTPAY_CHANNEL";
    public static final String CP_IS_NEED_BANK_DIRECT = "CP_IS_NEED_BANK_DIRECT";
    public static final String CP_IS_NEED_FP_BANK_DIRECT = "CP_IS_NEED_FP_BANK_DIRECT";
    //WAP/AAP支付渠道显示KEY
    public static final String CP_WAP_PLATFORM = "CP_WAP_PLATFORM";
    public static final String CP_WXBROWSER_PLATFORM = "CP_WXBROWSER_PLATFORM";
    public static final String CP_APP_PLATFORM = "CP_APP_PLATFORM";
    public static final String CP_KOREA_APP_PLATFORM = "CP_KOREA_APP_PLATFORM";
    //网银/快捷支付提示信息配置KEY
    public static final String CP_PAYONLINE_TIPS = "CP_PAYONLINE_TIPS";
    public static final String CP_FASTPAY_TIPS = "CP_FASTPAY_TIPS";

    /**
     * 境外支付渠道配置KEY(韩国)
     */
    //支付渠道显示KEY
    public static final String CPK_ZF_PLATFORM = "CPK_ZF_PLATFORM";
    public static final String CPK_PAYONLINE_PLATFORM = "CPK_PAYONLINE_PLATFORM";
    public static final String CPK_PAYONLINE_ZL_PLATFORM = "CPK_PAYONLINE_ZL_PLATFORM";
    public static final String CPK_FASTPAY_PLATFORM = "CPK_FASTPAY_PLATFORM";
    public static final String CPK_FASTPAY_ZL_PLATFORM = "CPK_FASTPAY_ZL_PLATFORM";
    public static final String CPK_PAYONLINE_MORE_PLATFORM = "CPK_PAYONLINE_MORE_PLATFORM";
    public static final String CPK_FASTPAY_MORE_PLATFORM = "CPK_FASTPAY_MORE_PLATFORM";
    //网银/快捷主渠道KEY
    public static final String CPK_PAYONLINE_CHANNEL = "CPK_PAYONLINE_CHANNEL";
    public static final String CPK_FASTPAY_CHANNEL = "CPK_FASTPAY_CHANNEL";
    public static final String CPK_IS_NEED_BANK_DIRECT = "CPK_IS_NEED_BANK_DIRECT";
    public static final String CPK_IS_NEED_FP_BANK_DIRECT = "CPK_IS_NEED_FP_BANK_DIRECT";
    //WAP/AAP支付渠道显示KEY
    public static final String CPK_WAP_PLATFORM = "CPK_WAP_PLATFORM";
    public static final String CPK_WXBROWSER_PLATFORM = "CPK_WXBROWSER_PLATFORM";
    public static final String CPK_APP_PLATFORM = "CPK_APP_PLATFORM";
    public static final String CPK_KOREA_APP_PLATFORM = "CPK_KOREA_APP_PLATFORM";
    //网银/快捷支付提示信息配置KEY
    public static final String CPK_PAYONLINE_TIPS = "CPK_PAYONLINE_TIPS";
    public static final String CPK_FASTPAY_TIPS = "CPK_FASTPAY_TIPS";

    /**
     * 网银/快捷支付渠道默认显示个数配置KEY
     */
    public static final String BANK_MAX_LENGTH_KEY = "BANK_MAX_LENGTH";
    public static final String BANK_MAX_LENGTH = "10";

    /**
     * 用户默认所有支付渠道配置KEY
     */
    public static final String DEFAULT_ZF_PLATFORM = "DEFAULT_ZF_PLATFORM";
    public static final String DEFAULT_PAYONLINE_PLATFORM = "DEFAULT_PAYONLINE_PLATFORM";
    public static final String DEFAULT_FASTPAY_PLATFORM = "DEFAULT_FASTPAY_PLATFORM";

    /**
     * 后端内部查询密码
     */
    public static final String INNER_QUERY_PWD = ConfigUtil.getValue("INNER_QUERY_PWD");
    public static final String INNER_MANAGE_PAGE = "console/manage"; //后端管理页面
    public static final String INNER_VERSION_PAGE = "console/version"; //后端版本管理页面
    public static final String PAY_SERVER_IPS = "PAY_SERVER_IPS"; //后端负载均衡配置key
    public static final String CACHE_CONFIG_CLEAN = "/server/cacheDBConfigClean.htm"; //后端清空配置缓存地址
    public static final String CACHE_PAYMENT_SOURCE_CLEAN = "/console/cacheDBPaySourceClean.htm"; //后端清空配置支付来源缓存地址

    /**
     *查询接口中支付号key
     */
    public static final String PAY_ID_KEY = "orderId";
    /**
     * 交易通知返回状态值
     */
    public static final String NOTIFY_SUCCESS_RESULT = "success";
    public static final String NOTIFY_FAIL_RESULT = "fail";
    public static final String PAYMENT_DIV_DATAMPS = "MO12000";
    public static final String CP_PAYMENT_DIV_DATAMPS = "MO13000";
    public static final String CPK_PAYMENT_DIV_DATAMPS = "MO15000";

    public static final String CONFIG_FRONTTIMESTAMP = "FRONT_TIMESTAMP";
    public final static String MEMCACHE_PAY_FRONTTIMESTAMP  = ConfigUtil.getValue("MEMCACHE_PAY_FRONTTIMESTAMP");
    public static final String FRONT_TIMESTAMP_URL = ConfigUtil.getValue("FRONT_TIMESTAMP_URL");
    public static final String FRONT_TIMESTAMP = ConfigUtil.getValue("FRONT_TIMESTAMP");
    public static final int MEMCACHE_PAY_FRONTTIMESTAMP_EXP = 300;
    /**
     * 自动关闭支付信息不正确现象
     */
    public static final String PAY_AUTO_CLOSE_ERROR_MSG = "<script>var msg = '支付页面已过期,请刷新后重试';alert(msg);function CloseWebPage(){if(navigator.userAgent.indexOf('MSIE')>0){if(navigator.userAgent.indexOf('MSIE6.0')>0){window.opener=null;window.close();}else{window.open('','_top');window.top.close();}}else if(navigator.userAgent.indexOf('Firefox')>0){window.location.href='about:blank';}else{window.opener=null;window.open('','_self','');window.close();}};CloseWebPage();</script>";

    public static final String STANDBY_PAY_KEY = ConfigUtil.getValue("STANDBY_PAY_KEY");

    public static final String PAYPAGE_ALL_CHANNEL = "PAYPAGE_ALL_CHANNEL";
    public static final String ENVIRONMENT_JS_VAR = ConfigUtil.getValue("ENVIRONMENT_JS_VAR");
    public static final String NEED_METAQ_NOTIFY_SOUCREIDS = "NEED_METAQ_NOTIFY_SOUCREIDS";
    public static final String DUIHUAN_EVENTID = "DUIHUAN_EVENTID";
    public static final String ORDER_EVENTID = "ORDER_EVENTID";
    public static final String UNSUPPORT_BZ_SOUCREID = "UNSUPPORT_BZ_SOUCREID";

    public static final String HTTP_TIMED_OUT_SHOUXIN_BG = "HTTP_TIMED_OUT_SHOUXIN_BG";
    public static final String HTTP_TIMED_OUT_ALIPAY_BG = "HTTP_TIMED_OUT_ALIPAY_BG";
    public static final String ADMIN_MOBILES = "ADMIN_MOBILES";
    /**是否使用帮钻 1：使用  0：未使用*/
    public static final String USE_BZ = "1";
}
