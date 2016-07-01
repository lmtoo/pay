package com.somnus.pay.payment.thirdPay.tencent.config;


/**
  * @description: 基础配置类
  * Copyright 2011-2015 B5M.COM. All rights reserved
  * @author: masanbao
  * @version: 1.0
  * @createdate: 2015-08-10
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-08-10   masanbao       1.0           初始化
  * 2015-12-15   qingshu       1.1           添加类描述

 *类名：TencentConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 *
 *用户帐号：
 *email:liubang@b5m.com
 *Pid:2088011820675002
 *Key:ikezlvaqhnxahet09u8lgwuu1vs87dwm
 */
import com.somnus.pay.payment.util.ConfigUtil;

public class TencentConfig {

    // 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = ConfigUtil.getBankInfoValue("TENCENT_PARTNER_ID");
	// 商户号
	public static String op_user_id = ConfigUtil.getBankInfoValue("TENCENT_OP_USER_ID");
	// 商户后台登陆密码
	public static String recv_user_id = ConfigUtil.getBankInfoValue("TENCENT_RECV_USER_ID");
    // 商户的私钥
	public static String key = ConfigUtil.getBankInfoValue("TENCENT_KEY");
	public static final String PAY_URL = "https://gw.tenpay.com/gateway/pay.htm";
	public static final String PAY_URL0 = "https://gw.tenpay.com/gateway/verifynotifyid.xml";
	public static final String QUERY_URL = "https://gw.tenpay.com/gateway/normalorderquery.xml";
	public static final String REFUND_URL = "https://mch.tenpay.com/refundapi/gateway/refund.xml"; // 退款

    public static final String PAY_NOTIFY_URL     = ConfigUtil.getBankInfoValue("TENCENT_BACK_URL");
    public static final String PAY_RETURN_URL     = ConfigUtil.getBankInfoValue("TENCENT_FRONT_URL");
    // 交易成功
    public static final String PAY_RESULT_SUCCESS = "0";
    // 即时到账
    public static final String PAY_TRADE_MODE1    = "1";
    // 支付的单位为分
    public static final int    PAY_MOUNT_UNIT     = 100;

    // 调试用，创建TXT日志文件夹路径
    public static String       log_path           = "D:\\";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String       input_charset      = "utf-8";

    // 签名方式 不需修改
    public static String       sign_type          = "MD5";

    // 接口版本
    public static String       version_type       = "1.0";
}