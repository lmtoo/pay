package com.somnus.pay.payment.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PayAmountUtil {

    public static final Integer PAY_MOUNT_UNIT = 100;
    public static final String DOUBLE_FORMAT = "0.00";

    /**
    * 提供精确的乘法运算。
    * @param v1 被乘数
    * @param v2 乘数
    * @return 两个参数的积
    */
    public static String mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2).intValue() + "";
    }

    /**
     * 提供精确的除法运算。
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static String divide(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        DecimalFormat format = new DecimalFormat("#0.00");
        double result = b1.divide(b2).doubleValue();
        String str = format.format(result);
        return str;
    }

    /**
     * 将Double数值显示为0.00方式
     * @param amount
     * @return
     */
    public static String getDoubleFormat(Double amount) {
        DecimalFormat df = new DecimalFormat(DOUBLE_FORMAT);
        return df.format(amount);
    }

}
