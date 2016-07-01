package com.somnus.pay.payment.thirdPay.alipay.config;

import com.somnus.pay.payment.util.ConfigUtil;

/**
 * <pre>
 * 支付宝配置信息
 * </pre>
 *
 * @author masanbao
 * @version $ AlipayConfig.java, v 0.1 2015年1月28日 下午4:11:13 masanbao Exp $
 * @since   JDK1.6
 */
public class AlipayConfig {

    public static String       seller_id      = ConfigUtil.getBankInfoValue("ALI_SELLER_ID");               // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static String       partner        = ConfigUtil.getBankInfoValue("ALI_PARTNER");                // 商户号
    public static String       key            = ConfigUtil.getBankInfoValue("ALI_KEY"); // 商户的私钥
    public static String       ali_public_key = ConfigUtil.getBankInfoValue("ALI_PUBLIC_KEY");                                // 支付宝的公钥，如果签名方式设置为“0001”时，请设置该参数
    public static String       private_key    = ConfigUtil.getBankInfoValue("ALI_PRIVATE_KEY");
	public static final String ALIPAY_PRIVATE_KEY = ConfigUtil.getBankInfoValue("ALI_PRIVATE_KEY_PKCS8");

    public static String       log_path       = "D:\\";                            // 调试用，创建TXT日志文件夹路径
    public static String       input_charset  = "utf-8";                           // 字符编码格式 目前支持 gbk 或 utf-8
    public static String       sign_type      = "MD5";                             // 签名方式 不需修改
    public static final int    PAY_MOUNT_UNIT = 1;                                 // 支付的单位为元
    
    /**
     * PC 支付宝回调地址
     */
    public static final String WEB_NOTIFY_URL = ConfigUtil.getBankInfoValue("ALI_WEB_BACK_URL");
    public static final String WEB_RETURN_URL = ConfigUtil.getBankInfoValue("ALI_WEB_FRONT_URL");
    
    public static final String WEB_REFUND_URL = ConfigUtil.getBankInfoValue("ALI_WEB_REFUND_URL");
    /**
     * wap 支付宝回调地址
     */
    public static final String WAP_NOTIFY_URL = ConfigUtil.getBankInfoValue("ALI_WAP_BACK_URL");
    public static final String WAP_RETURN_URL = ConfigUtil.getBankInfoValue("ALI_WAP_FRONT_URL");
    /**
     * APP 支付宝回调地址
     */
    public static final String APP_NOTIFY_URL = ConfigUtil.getBankInfoValue("ALI_APP_BACK_URL");

    /**
     * 帮韩品APP相关key
     */
    public static final String KOREA_SELLER_ID = ConfigUtil.getBankInfoValue("ALI_KOREAAPP_SELLER_ID");               // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static final String KOREA_PARTNER = ConfigUtil.getBankInfoValue("ALI_KOREAAPP_PARTNER");                // 商户号
    public static final String KOREA_KEY = ConfigUtil.getBankInfoValue("ALI_KOREAAPP_KEY"); // 商户的私钥
    public static final String KOREA_PRIVATE_KEY = ConfigUtil.getBankInfoValue("ALI_KOREAAPP_PRIVATE_KEY");
    public static final String KOREA_PRIVATE_KEY_PKCS8 = ConfigUtil.getBankInfoValue("ALI_KOREAAPP_PRIVATE_KEY_PKCS8");
    public static final String KOREA_ALIPAY_PUBLIC_KEY = ConfigUtil.getBankInfoValue("ALI_KOREAAPP_ALIPAY_PUBLIC_KEY");
    public static final String KOREA_APP_NOTIFY_URL = ConfigUtil.getBankInfoValue("ALI_KOREAAPP_BACK_URL");
    public static final String PAY_SERVICE_NAME = "mobile.securitypay.pay";

    /**
     * 帮我买APP相关key
     */
    public static final String B5MAPP_SELLER_ID = ConfigUtil.getBankInfoValue("ALI_B5MAPP_SELLER_ID");               // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static final String B5MAPP_PARTNER = ConfigUtil.getBankInfoValue("ALI_B5MAPP_PARTNER");                // 商户号
    public static final String B5MAPP_KEY = ConfigUtil.getBankInfoValue("ALI_B5MAPP_KEY"); // 商户的私钥
    public static final String B5MAPP_PRIVATE_KEY = ConfigUtil.getBankInfoValue("ALI_B5MAPP_PRIVATE_KEY");
    public static final String B5MAPP_PRIVATE_KEY_PKCS8 = ConfigUtil.getBankInfoValue("ALI_B5MAPP_PRIVATE_KEY_PKCS8");
    public static final String B5MAPP_ALIPAY_PUBLIC_KEY = ConfigUtil.getBankInfoValue("ALI_B5MAPP_ALIPAY_PUBLIC_KEY");
    public static final String B5MAPP_APP_NOTIFY_URL = ConfigUtil.getBankInfoValue("ALI_B5MAPP_BACK_URL");

    /**
     * 支付宝常量
     */
    public static final String TRADE_FINISHED        = "TRADE_FINISHED";
    public static final String TRADE_SUCCESS         = "TRADE_SUCCESS";
    public static final String TRADE_WAIT_BUYER_PAY  = "WAIT_BUYER_PAY";
    public static final String TRADE_TRADE_CLOSED    = "TRADE_CLOSED";
    public static final String TRADE_TRADE_PENDING   = "TRADE_PENDING";

    // 支付宝 notify_url 需要 返回的两种值
    public static final String NOTIFY_SUCCESS_RESULT = "success";
    public static final String NOTIFY_FAIL_RESULT    = "fail";

    // 支付宝 支付涉及的参数名
    public static final String OUT_TRADE_NO = "out_trade_no";
    public static final String SUBJECT = "subject";
    public static final String TRADE_STATUS = "trade_status";
    public static final String TRADE_NO = "trade_no";
    public static final String TOTAL_FEE = "total_fee";
    public static final String BUYER_ID = "buyer_id";
    public static final String NOTIFY_ID = "notify_id";
    public static final String SIGN_KEY = "sign";
    public static final String SIGN_TYPE_RSA = "RSA";
    public static final String SIGN_TYPE_MD5 = "MD5";

    // 支付宝 部分参数默认值
    public static final String PAYMETHOD = "bankPay";
    public static final String DOUBLE_FORMAT = "0.00";
    public static final String B5M_SUCCESS_CODE = "100";
    public static final String VERIFY_RESPONSE_TRUE = "true";

    /**
     * 支付宝国际跨境支付渠道(香港)为app使用
     */
    public static final String CROSS_PAY_SERVICE_NAME = "create_forex_trade_wap";
    public static final String CROSS_PAY_PARTNER = ConfigUtil.getBankInfoValue("ALI_HONGKONG_CROSS_PAY_PARTNER"); //测试商户号"2088101122136241";
    public static final String CROSS_PAY_MD5_KEY = ConfigUtil.getBankInfoValue("ALI_HONGKONG_CROSS_PAY_MD5_KEY"); //测试key："760bdzec6y9goq7ctyx96ezkz78287de";
    public static final String CROSS_PAY_INPUT_CHARSET = "utf-8";
    public static final String CROSS_PAY_SIGN_TYPE = "MD5";
    public static final String CROSS_PAY_CURRENCY_TYPE = "USD";
    public static final String CROSS_PAY_WAP_NOTIFY_URL = ConfigUtil.getBankInfoValue("ALI_HONGKONG_CROSS_PAY_WAP_BACK_URL");
    public static final String CROSS_PAY_WAP_RETURN_URL = ConfigUtil.getBankInfoValue("ALI_HONGKONG_CROSS_PAY_WAP_FRONT_URL");
    public static final String CROSS_PAY_REQUEST_WAP = "https://mapi.alipay.com/gateway.do"; //测试api接口："http://openapi.alipaydev.com/gateway.do";
    public static final String CROSS_PAY_CURRENCY = "currency";
    public static final String QUERY_SERVICE_NAME = "single_trade_query";
    /**
     * 香港账号支付宝国际支付app相关信息
     */
    public static final String CROSS_PAY_SELLER_ID = ConfigUtil.getBankInfoValue("ALI_HONGKONG_CROSS_PAY_SELLER_ID");
    public static final String CROSS_PAY_PRIVATE_KEY = ConfigUtil.getBankInfoValue("ALI_HONGKONG_CROSS_PAY_PRIVATE_KEY");
    public static final String CROSS_PAY_PRIVATE_KEY_PKCS8 = ConfigUtil.getBankInfoValue("ALI_HONGKONG_CROSS_PAY_PRIVATE_KEY_PKCS8");
    public static final String CROSS_PAY_ALIPAY_PUBLIC_KEY = ConfigUtil.getBankInfoValue("ALI_HONGKONG_CROSS_PAY_ALIPAY_PUBLIC_KEY");
    public static final String CROSS_PAY_FOREX_BIZ = "FP";
    public static final String CROSS_PAY_CURRENCY_USD = "USD";
    public static final String CROSS_PAY_PAYMENT_TYPE_1 = "1";

    /**
     * 支付宝国际跨境支付渠道(韩国)为app使用
     */
    public static final String CUSTOMS_DECLARATION_SERVICE_NAME = "alipay.acquire.customs"; //报关服务名
    public static final String KOREA_CROSS_PAY_PARTNER = ConfigUtil.getBankInfoValue("ALI_KOREA_CROSS_PAY_PARTNER");
    public static final String KOREA_CROSS_PAY_MD5_KEY = ConfigUtil.getBankInfoValue("ALI_KOREA_CROSS_PAY_MD5_KEY");
    public static final String KOREA_CROSS_PAY_WAP_NOTIFY_URL = ConfigUtil.getBankInfoValue("ALI_KOREA_CROSS_PAY_WAP_BACK_URL");
    public static final String KOREA_CROSS_PAY_WAP_RETURN_URL = ConfigUtil.getBankInfoValue("ALI_KOREA_CROSS_PAY_WAP_FRONT_URL");

    /**
     * 帮韩品APPWap方式相关key
     */
    public static final String WAP_KOREAAPP_SELLER_ID = ConfigUtil.getBankInfoValue("ALI_WAP_KOREAAPP_SELLER_ID");               // 合作身份者ID，以2088开头由16位纯数字组成的字符串
    public static final String WAP_KOREAAPP_PARTNER = ConfigUtil.getBankInfoValue("ALI_WAP_KOREAAPP_PARTNER");                // 商户号
    public static final String WAP_KOREAAPP_KEY = ConfigUtil.getBankInfoValue("ALI_WAP_KOREAAPP_KEY"); // 商户的私钥
    public static final String WAP_KOREAAPP_PRIVATE_KEY = ConfigUtil.getBankInfoValue("ALI_WAP_KOREAAPP_PRIVATE_KEY");
    public static final String WAP_KOREAAPP_PRIVATE_KEY_PKCS8 = ConfigUtil.getBankInfoValue("ALI_WAP_KOREAAPP_PRIVATE_KEY_PKCS8");
    public static final String WAP_KOREAAPP_ALIPAY_PUBLIC_KEY = ConfigUtil.getBankInfoValue("ALI_WAP_KOREAAPP_ALIPAY_PUBLIC_KEY");
    public static final String WAP_KOREAAPP_BACK_URL = ConfigUtil.getBankInfoValue("ALI_WAP_KOREAAPP_BACK_URL");
    public static final String WAP_KOREAAPP_FRONT_URL = ConfigUtil.getBankInfoValue("ALI_WAP_KOREAAPP_FRONT_URL");
    public static final String WAP_PAY_SERVICE_NAME = "alipay.wap.create.direct.pay.by.user";
    public static final String WAP_PAY_INPUT_CHARSET = "utf-8";
    public static final String WAP_PAYMENT_TYPE = "1";
    public static final String WAP_PAY_REQUEST_WAP = "https://mapi.alipay.com/gateway.do";


    /**
     * 支付宝国际跨境支付渠道(韩国)为wap渠道使用
     */
    public static final String WAP_KOREA_CROSS_PAY_PARTNER = ConfigUtil.getBankInfoValue("ALI_WAP_KOREA_CROSS_PAY_PARTNER");
    public static final String WAP_KOREA_CROSS_PAY_MD5_KEY = ConfigUtil.getBankInfoValue("ALI_WAP_KOREA_CROSS_PAY_MD5_KEY");
    public static final String WAP_KOREA_CROSS_PAY_WAP_NOTIFY_URL = ConfigUtil.getBankInfoValue("ALI_WAP_KOREA_CROSS_PAY_WAP_BACK_URL");
    public static final String WAP_KOREA_CROSS_PAY_WAP_RETURN_URL = ConfigUtil.getBankInfoValue("ALI_WAP_KOREA_CROSS_PAY_WAP_FRONT_URL");

    /**
     * 阿里测试环境
     */
    public static String TEST_KEY = ConfigUtil.getBankInfoValue("ALI_TEST_KEY"); // 商户的私钥
    public static String TEST_PARTNER = ConfigUtil.getBankInfoValue("ALI_TEST_PARTNER"); // 商户的私钥
    public static final String TEST_CROSS_PAY_REQUEST_WAP = "http://openapi.alipaydev.com/gateway.do"; //测试api接口



}
