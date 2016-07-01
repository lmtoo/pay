package com.somnus.pay.payment.thirdPay.tencent.util;

public class TencentConstants {
    public static final String PAY_URL            = "https://gw.tenpay.com/gateway/pay.htm";
    public static final String PAY_URL_VERIFY     = "https://gw.tenpay.com/gateway/verifynotifyid.xml";
    public static final String PAY_URL_QUERY      = "https://gw.tenpay.com/gateway/normalorderquery.xml";

    public static final String PAY_KEY            = "9224f6ffc4409fc49e24602b421b19a4";
    public static final String PAY_PARTNER_ID     = "1219056301";
    public static final String PAY_NOTIFY_URL     = "/third/tp/notify.htm";
    public static final String PAY_RETURN_URL     = "/third/tp/return.htm";
    // 交易成功
    public static final String PAY_RESULT_SUCCESS = "0";
    // 即时到账
    public static final String PAY_TRADE_MODE1    = "1";
}
