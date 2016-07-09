package com.somnus.pay.payment.thirdPay.shouxin.config;

import com.somnus.pay.payment.util.ConfigUtil;

/**
  * @description: 首信支付的相关配置
  * @author: qingshu
  * @version: 1.0
  * @createdate: 2015-08-20
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-08-20     qingshu         1.0            支付能力
 */
public class ShouXinConfig {
    public static final String V_MID = ConfigUtil.getBankInfoValue("SHOUXIN_HONGKONG_V_MID");//商户号
    public static final String V_MD5_KEY_INFO =ConfigUtil.getBankInfoValue("SHOUXIN_HONGKONG_V_MD5_KEY_INFO");//MD5加签字段

    public static final String V_REQUEST_COMMON_ADDR = "https://pay.yizhifubj.com/prs/user_payment.checkit";//标准提交接口
    public static final String V_REQUEST_BANK_ADDR ="https://pay.yizhifubj.com/customer/gb/pay_bank.jsp";//银行快捷通道接口

    public static final String WEB_FRONTURL = ConfigUtil.getBankInfoValue("SHOUXIN_HONGKONG_WEB_KOREA_FRONTURL");
    public static final String WEB_BACKURL = ConfigUtil.getBankInfoValue("SHOUXIN_HONGKONG_WEB_KOREA_BACKURL");

    public static final String WAP_FRONTURL = ConfigUtil.getBankInfoValue("SHOUXIN_HONGKONG_WAP_KOREA_FRONTURL");
    public static final String WAP_BACKURL = ConfigUtil.getBankInfoValue("SHOUXIN_HONGKONG_WAP_KOREA_BACKURL");

    public static final int ORDER_PREFIX_LENGTH = 29; //yyyyMMdd-8757-yyyyMMddHHmmss的长度

    public static final String OUT_TRADE_NO = "v_oid"; //yyyyMMdd-商户标号-yyyyMMddHHmmss-支付号（b5mXXXXX）
    public static final String TRADE_NO = "v_oid";
    public static final String PAY_RESULT = "v_pstatus";
    public static final String PAY_RESULT_INFO = "v_pstring";
    public static final String RETURN_MD5_INFO = "v_md5info";
    public static final String TOTAL_AMOUNT = "v_amount";
    public static final String MONEY_TYPE = "v_moneytype";
    public static final String IS_VIREMENT = "v_isvirement";
    public static final String P_MODE = "v_pmode";

    public static final String TRADE_SUCCESS = "20";
    public static final String NOTIFY_TRADE_SUCCESS = "1";
    public static final String NOTIFY_NOTE_FAIL = "error";
    public static final String NOTIFY_NOTE_SUCCESS = "sent";

    public static final String CER_PATH = "/opt/pay_conf/shouxin/Public1024.key"; //"E:\\360yunPan\\首信\\Public1024.key";

    public static final String V_REQUEST_QUERY = "http://api.yizhifubj.com/merchant/order/order_ack_oid_list.jsp";

    public static final String ORDER_ID = "order_id";
    public static final String V_IDCOUNTRY = "000"; //默认000，中国156
    public static final String V_IDCOUNTRY_CHINA = "156"; //中国156
    public static final String V_IDTYPE = "00";//默认00
    public static final String V_IDTYPE_IDCARD = "01";//身份证类型
    public static final String V_PRODUCTTYPE = "";
    public static final String V_IDNUMBER = "000000000000000";
    public static final String V_ORDERSTATUS_0 ="0";//配货状态，未配齐􁵚􄝽􅖀
    public static final String V_ORDERSTATUS_1 ="1";//配货状态，已配齐
    public static final String V_MONEYTYPE_RMB ="0";//币种。0为人民币


    public static final String USER_NAME = "userName";
    public static final String USER_ADDRESS = "detailAddress";
    public static final String USER_MOBILE = "mobile";
    public static final String USER_ADDRESS_POST = "zipcode";
    public static final String USER_IDCARD = "idCard";
    public static final String SX_PAY_KEY = "shouxin";
    public static final String V_RCVTEL = "v_rcvtel"; //该字段临时扩展（用于存放是否组）

    //韩国商户key
    public static final String KOREA_V_MID = ConfigUtil.getBankInfoValue("SHOUXIN_KOREA_V_MID");//商户号
    public static final String KOREA_V_MD5_KEY_INFO = ConfigUtil.getBankInfoValue("SHOUXIN_KOREA_V_MD5_KEY_INFO");//MD5加签字段
    public static final String WEB_KOREA_FRONTURL = ConfigUtil.getBankInfoValue("SHOUXIN_KOREA_WEB_KOREA_FRONTURL");
    public static final String WEB_KOREA_BACKURL = ConfigUtil.getBankInfoValue("SHOUXIN_KOREA_WEB_KOREA_BACKURL");

}
