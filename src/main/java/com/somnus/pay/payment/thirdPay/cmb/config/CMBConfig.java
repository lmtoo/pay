package com.somnus.pay.payment.thirdPay.cmb.config;

import com.somnus.pay.payment.util.ConfigUtil;

/**
  * @description: 招行直连支付配置信息
  * Copyright 2011-2015 B5M.COM. All rights reserved
  * @author: qingshu
  * @version: 1.0
  * @createdate: 2015-08-20
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-08-20   qingshu       1.0            支付能力
 */
public class CMBConfig {

    public static final String BRANCH_ID = ConfigUtil.getBankInfoValue("CMB_BRANCH_ID");
    public static final String CO_NO = ConfigUtil.getBankInfoValue("CMB_CO_NO");
    public static final String WAP_CO_NO = ConfigUtil.getBankInfoValue("CMB_WAP_CO_NO");

    public static final String ACTION_URL = "https://netpay.cmbchina.com/netpayment/BaseHttp.dll?PrePayC2";
    public static final String WAP_ACTION_URL = "https://netpay.cmbchina.com/netpayment/BaseHttp.dll";

    public static final String WEB_FRONTURL = ConfigUtil.getBankInfoValue("CMB_WEB_FRONT_URL");
    public static final String WEB_BACKURL = ConfigUtil.getBankInfoValue("CMB_WEB_BACK_URL");
    public static final String WAP_FRONTURL = ConfigUtil.getBankInfoValue("CMB_WAP_FRONT_URL");
    public static final String WAP_BACKURL = ConfigUtil.getBankInfoValue("CMB_WAP_BACK_URL");

    public static final String PUB_KEY_FILE = ConfigUtil.getBankInfoValue("CMB_CERTS_FILE");
    public static final String OUT_TRADE_NO = "MerchantPara";
    public static final String BILL_NO = "BillNo";
    public static final String SUCCEED_KEY = "Succeed";
    public static final String TRADE_NO = "BillNo";
    public static final String AMOUNT = "Amount";
    public static final String TRADE_SUCCESS = "Y";
    public static final String MERCHANT_PARA = "MerchantPara";
    public static final int BILLNO_LENGTH = 10;
    public static final String WAP_MFCIS_API_COMMAND = "PrePayWAP";

    public static final String RETURN_MSG_KEY = "Msg";
}

