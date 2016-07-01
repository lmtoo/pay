package com.somnus.pay.payment.thirdPay.tencent.config;

import com.somnus.pay.payment.util.ConfigUtil;

/**
  * @description: 微信支付基础配置类
  * Copyright 2011-2015 B5M.COM. All rights reserved
  * @author: mike
  * @version: 1.0
  * @createdate: 2015-12-15
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-12-15   mike        	1.0           初始化
  * 2015-12-15   qingshu        1.1           添加类描述
 */
public class WxConfig {

	/**
	 * 载和 微信PCNative
	 */
	public static String AppSecret = ConfigUtil.getBankInfoValue("WX_NATIVE_APP_SECRET"); //应用密钥
	public static String MCH_ID = ConfigUtil.getBankInfoValue("WX_NATIVE_MCH_ID"); // 商户号
	public static String APPID= ConfigUtil.getBankInfoValue("WX_NATIVE_APP_ID"); // 服务号的应用号
	public static String key = ConfigUtil.getBankInfoValue("WX_NATIVE_API_KEY"); // API密钥

	/**
	 * APP-- 载和 新(帮我买APP)
	 */
	public static String MCH_B5M_ID = ConfigUtil.getBankInfoValue("WX_B5MAPP_MCH_ID");
	public static String APPID_B5M_APP = ConfigUtil.getBankInfoValue("WX_B5MAPP_APP_ID");
	public static String APIKEY_B5M_APP = ConfigUtil.getBankInfoValue("WX_B5MAPP_API_KEY");

    /**
     * 韩国城APP“帮韩品”
     */
    public static String MCH_KOREA_ID = ConfigUtil.getBankInfoValue("WX_B5MKOREA_APP_MCH_ID");
    public static String APPID_KOREA_APP = ConfigUtil.getBankInfoValue("WX_B5MKOREA_APP_APP_ID");
    public static String APIKEY_KOREA_APP = ConfigUtil.getBankInfoValue("WX_B5MKOREA_APP_API_KEY");

	/**
	 * 载信
	 */
//	public static String AppSecret = "7b9bb554d6b08ce735fa572c13d2b937"; //应用密钥
//	public static String MCH_ID = "1231783402";
//	public static String APPID = "wx97f7d134c73242e4"; // 服务号的应用号
//	public static String key = "b5msoft1238888888888888888888888"; // API密钥	
//	
	public final static String TOKEN = "weixinCourse";// 服务号的配置token	
	public final static String SIGN_TYPE = "MD5";// 签名加密方式
	// 微信支付统一接口的回调action
	public final static String NOTIFY_URL = ConfigUtil.getBankInfoValue("WX_NATIVE_BACK_URL");
	public final static String NOTIFY_WAP_URL = ConfigUtil.getBankInfoValue("WX_NATIVE_WAP_BACK_URL");
	public final static String NOTIFY_APP_URL = ConfigUtil.getBankInfoValue("WX_B5MAPP_BACK_URL");
    public final static String NOTIFY_NEW_APP_URL = ConfigUtil.getBankInfoValue("WX_B5MKOREA_APP_BACK_URL");

	// 支付的单位为分
	public static final int PAY_MOUNT_UNIT = 100;
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "UTF-8";
	
	public static String COOKIE_WX_CODE = "wxcode";
	public static String COOKIE_WX_OPENID = "wxopenid";

	// 签名方式 不需修改
	public static String sign_type = "MD5";
	// 接口版本
	public static String version_type = "3.0";
	// 微信 notify_url 需要 返回的两种值
	public static final String NOTIFY_SUCCESS_RESULT = "SUCCESS";
	
	public static final String NOTIFY_FAIL_RESULT = "FAIL";

	public static final String TRADE_SUCCESS = "SUCCESS"; // 微信支付交易成功的状态
	
	public static final String TRADE_SUCCESS_APP = "0"; // APP支付交易成功的状态

	public static final String RETURN_CODE = "return_code"; // 返回状态：SUCCESS成功 | FAIL失败
	
	public static final String RETURN_CODE_APP = "trade_state"; // 返回状态：0成功 | 其他失败
	
	public static final String RESULT_CODE = "result_code"; // 业务结果

	public static final String OUT_TRADE_NO = "out_trade_no"; // 商户系统的订单号，与请求一致,我们后台自己创建的订单号

    public static final String ORDER_ID = "orderId"; // 请求参数中支付号的对应key

	public static final String TRADE_NO = "transaction_id"; // 微信支付交易号

	public static final String TOTAL_FEE = "total_fee"; // 支付金额，单位为分，如果discount有值通知的total_fee+discount = 请求的total_fee

	public static final String PAY_INFO = "return_code"; // 支付结果信息，支付成功时为空
	public static final String PARTNER = "mch_id"; // 商户号
	public static final String BANK_TYPE = "bank_type"; // 银行类型

	public static final String SUBJECT = "body";
	public static final String BUYER_ID = "buyer_alias";
	public static final String ATTACH = "attach";
    public static final String OUT_TRADE_NO_USED = "OUT_TRADE_NO_USED"; //商户订单号重复错误码

	/**
	 * 微信基础接口地址
	 */
	// 获取token接口(GET)  https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
	public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	// oauth2授权接口(GET)
	public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	//到用户授权页面获取code
	public final static String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
	// 刷新access_token接口（GET）
	public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	// 菜单创建接口（POST）
	public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 菜单查询（GET）
	public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// 菜单删除（GET）
	public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	/**
	 * 微信支付接口地址
	 */
	// 微信支付统一接口(POST)
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 微信退款接口(POST)
	public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 订单查询接口(POST)
	public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	// 关闭订单接口(POST)
	public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	// 退款查询接口(POST)
	public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	// 对账单接口(POST)
	public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	// 短链接转换接口(POST)
	public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
	// 接口调用上报接口(POST)
	public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";
	
	/**
	 * 微信APP使用接口
	 */
	// 微信APP下单接口  https://api.weixin.qq.com/pay/genprepay?access_token=ACCESS_TOKEN
	public final static String APP_ORDER_URL  = "https://api.weixin.qq.com/pay/genprepay";
	// APP订单查询接口
	public final static String APP_CHECK_ORDER_URL = "https://api.weixin.qq.com/pay/orderquery";

    //微信常量Key
    public final static String APP_ID_KEY = "app_id"; //appId
    public final static String MCH_ID_KEY = "mch_id"; //商户号
    public final static String MCH_APIKEY_KEY = "mch_api_key"; //商户api密钥




}