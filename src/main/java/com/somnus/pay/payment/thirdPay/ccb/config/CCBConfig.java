package com.somnus.pay.payment.thirdPay.ccb.config;

import com.somnus.pay.payment.util.ConfigUtil;

/**
  * @description: 建行直连配置类
  * @author: qingshu
  * @version: 1.0
  * @createdate: 2015-08-20
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-08-20   qingshu       1.0            建行直连配置类
 */
public class CCBConfig {

    public static final String MERCHANT_ID = ConfigUtil.getBankInfoValue("CCB_MERCHANT_ID"); //商户代码
    public static final String POS_ID = ConfigUtil.getBankInfoValue("CCB_POS_ID"); //商户柜台代码
    public static final String BRANCH_ID = ConfigUtil.getBankInfoValue("CCB_BRANCH_ID"); //分行代码
    public static final String CUR_CODE = "01"; //币种
    public static final String TX_CODE = ConfigUtil.getBankInfoValue("CCB_TX_CODE");//交易码，由建行统一分配为520100
    public static final String PAY_REQUEST_ADDR = "https://ibsbjstar.ccb.com.cn/app/ccbMain";//支付请求地址
    public static final String CCB_PUBLIC_RSA = ConfigUtil.getBankInfoValue("CCB_PUBLIC_RSA");
    public static final String CCB_PUBLIC_RSA_LAST30 = ConfigUtil.getBankInfoValue("CCB_PUBLIC_RSA_LAST30"); //RSA公钥最后30进行组签密钥字段
    public static final int TYPE = 1;
    public static final String OUT_TRADE_NO = "ORDERID";
    public static final String NOTIFY_FAIL_RESULT = "fail";
    public static final String NOTIFY_SUCCESS_RESULT = "success";
    public static final String REMARK1 = "REMARK1";
    public static final String TRADE_NO = "ORDERID";
    public static final String SUCCESS_KEY = "SUCCESS";//通知返回支付状态字段
    public static final String SUCCESS = "Y"; //通知返回支付成功标识
    public static final String PAYMENT = "PAYMENT"; //通知返回支付金额
    public static final String USRMSG = "USRMSG";
    public static final String ACC_TYPE = "ACC_TYPE";
    public static final String SIGN = "SIGN";
    public static final String ORDER_ID = "order_id";
    public static final String QUERY_PWD = "b5m123456";
    public static final String QUERY_TYPE_PAY = "0"; //支付流水
    public static final String QUERY_TYPE_REFUND = "1"; //退款流水
    public static final String QUERY_TXCODE = "410408";
    public static final String QUERY_STATS_SUCCESS = "0";
    public static final String SEL_TYPE_XML = "2";
    public static final String DEFAULT_PAGE = "1";
    public static final String QUERY_KIND_0 = "0"; //未结算
    public static final String QUERY_KIND_1 = "1"; //已结算
    public static final String CCB_PAY_KEY = "CCB";
    public static final String WEB_NOTIFY_ADDRESS = ConfigUtil.getBankInfoValue("CCB_WEB_BACK_URL");
    public static final String REG_ESCAPE_STR = "[-|_|+|@|/|*|(|)|（|）|!|！|-|\\|、|#|$|%|^|&|￥|`|～|。|:|：|;|；|?|？]*";

}
