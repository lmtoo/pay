package com.somnus.pay.payment.thirdPay.bocom.util;

import com.somnus.pay.payment.util.ConfigUtil;

/**
  * @description: BOCOMB2C常量配置
  * @author: masanbao
  * @version: 1.0
  * @createdate: 2015-01-28
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-01-28   masanbao       1.0            初始化
  * 2015-12-15   qingshu        1.1            添加类描述
  */
public class BocomConstants {

    public static final String INTERFACE_VERSION = "1.0.0.0";
    public static final String MERCHANT_ID = ConfigUtil.getBankInfoValue("BOCOM_MERCHANT_ID");
    public static final String MER_URL = ConfigUtil.getBankInfoValue("BOCOM_WEB_BACK_URL");
    public static final String GOODS_URL = ConfigUtil.getBankInfoValue("BOCOM_WEB_FRONT_URL");
    public static final String BOCOM_PAY_KEY = "BOCOM"; //交行key
    public static final String COMM_PAY_KEY = "COMM"; //支付宝的交行网银key
    public static final String CER_PATH = ConfigUtil.getBankInfoValue("BOCOM_CERTS_PATH"); //证书存放目录

}
