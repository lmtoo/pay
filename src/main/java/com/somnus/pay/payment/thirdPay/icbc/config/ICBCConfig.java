package com.somnus.pay.payment.thirdPay.icbc.config;

import com.somnus.pay.payment.util.ConfigUtil;

/**
  * @description: 工行直连配置类
  * Copyright 2011-2015 B5M.COM. All rights reserved
  * @author: qingshu
  * @version: 1.0
  * @createdate: 2015-08-20
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-08-20   qingshu       1.0            支付能力
 */
public class ICBCConfig {

    public static final String MER_ID = ConfigUtil.getBankInfoValue("ICBC_MER_ID"); //商户代码
    public static final String MER_ACCT = ConfigUtil.getBankInfoValue("ICBC_MER_ACCT"); //
    public static final String CHAR_SET = "GBK"; //
    public static final String KEY_PATH = ConfigUtil.getBankInfoValue("ICBC_KEY_PATH");
    public static final String KEY_PASS = ConfigUtil.getBankInfoValue("ICBC_KEY_PASS");//
    public static final String CERT_PATH = ConfigUtil.getBankInfoValue("ICBC_CERT_PATH");
    public static final String CERT_RES_SIGN_PATH = ConfigUtil.getBankInfoValue("ICBC_CERT_RES_SIGN_PATH");
    public static final String JKS_PATH =  ConfigUtil.getBankInfoValue("ICBC_JKS_PATH");
    public static final String JKS_PASS = ConfigUtil.getBankInfoValue("ICBC_KS_PASS"); //
    public static final String INTERFACE_NAME = "ICBC_PERBANK_B2C";
    public static final String INTERFACE_VERSION = "1.0.0.11";

    public static final String PAY_URL = "https://b2c.icbc.com.cn/servlet/ICBCINBSEBusinessServlet";
    public static final String PAY_URL_TEST = "https://mybank3.dccnet.com.cn/servlet/NewB2cMerPayReqServlet";
    public static final String WAP_URL = "https://mywap2.icbc.com.cn/ICBCWAPBank/servlet/ICBCWAPEBizServlet ";
    public static final String WAP_URL_TEST = "https://mywap2.dccnet.com.cn:447/ICBCWAPBank/servlet/ICBCWAPEBizServlet";
    public static final String QUERY_URL = "https://corporbank.icbc.com.cn/servlet/ICBCINBSEBusinessServlet";

    public static final String API_NAME = "EAPI";
    public static final String API_VERSION = "001.001.002.001";

    public static final String VERIFY_JOIN_FLAG = "0";
    public static final String CUR_TYPE = "001";
    public static final String INSTALLMENT_TIMES_1 = "1";
    public static final String CREDIT_TYPE = "2";
    public static final String NOTIFY_TYPE_HS = "HS";
    public static final String RESULT_TYPE = "1";
    public static final String OUT_TRADE_NO = "orderId";
    public static final String ORDER_CREATE_TIME = "create_time";
    public static final String WEB_URL = ConfigUtil.getBankInfoValue("ICBC_WEB_FRONTURL");
    public static final String NOTIFY_URL = ConfigUtil.getBankInfoValue("ICBC_WEB_BACKURL");
    public static final String WAP_WEB_URL = ConfigUtil.getBankInfoValue("ICBC_WAP_FRONTURL");
    public static final String WAP_NOTIFY_URL = ConfigUtil.getBankInfoValue("ICBC_WAP_BACKURL");

    public static final String SUCCESS = "1";
    public static final String NOTIFY_NOTE_FAIL = "fail";
    public static final String NOTIFY_NOTE_SUCCESS = "success";

    public static final String WAP_INTERFACE_NAME = "ICBC_WAPB_B2C";
    public static final String WAP_INTERFACE_VERSION = "1.0.0.6";
    public static final String CLIENT_TYPE_0 = "0";
}
