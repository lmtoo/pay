package com.somnus.pay.payment.thirdPay.abc.config;

import com.somnus.pay.payment.util.ConfigUtil;

/**
 * @description:  农行配置信息
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2016/2/24
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2016/2/24    qingshu       1.0            农行相关配置信息
 */
public class ABCConfig {

    public static final String Pay_Type_ID = "ImmediatePay";
    public static final int CURRENCEYCODE_RMB = 156;
    public static final int InstallmentMark_NO_FENQI = 0;
    public static final String NOTIFY_URL_PC = ConfigUtil.getBankInfoValue("ABC_NOTIFY_URL_PC");
    public static final String RETURN_URL_PC = ConfigUtil.getBankInfoValue("ABC_RETURN_URL_PC");
    public static final String NOTIFY_URL_WAP = ConfigUtil.getBankInfoValue("ABC_NOTIFY_URL_WAP");
    public static final String RETURN_URL_WAP = ConfigUtil.getBankInfoValue("ABC_RETURN_URL_WAP");
    public static final String COMMODITYTYPE_0202 = "0202";
    public static final String PAYMENTTYPE_1 = "1"; //农行卡支付
    public static final String PAYMENTLINKTYPE_PC = "1"; //internet网络接入
    public static final String PAYMENTLINKTYPE_WAP = "2"; //手机网络接入
    public static final String NOTIFYTYPE_1 = "1";
    public static final int MER_INDEX = 2;
    public static final String REQUEST_ORDER_SUCCESS = "0000";

    public static final String NOTIFY_SUCCESS_RESULT = "success";
    public static final String NOTIFY_FAIL_RESULT = "fail";

    public static final String QUERY_DETAIL = "0"; //0：订单状态查询,1：订单详细信息查询
    public static String QUERY_ORDER_IS_SUCCESS = "04";
}