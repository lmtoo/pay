package com.somnus.pay.payment.util;

import java.text.DecimalFormat;

import com.somnus.pay.payment.enums.PayWay;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.utils.Assert;

/**
  * @description: 支付校验签名
  * Copyright 2011-2015 B5M.COM. All rights reserved
  * @author: mike
  * @version: 1.0
  * @createdate: 2015-3-20
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-10-30    丹青生  1.0            支付能力
 *  2015-12-14   qingshu       1.1            代码整理及描述
 */
public class PaySign {

	private static final String DEFAULT_CHARSET = "utf-8";
	private static final Integer SUBJECT_VALIDATE_LENGTH = 255;
	protected static Logger logger = LoggerFactory.getLogger(PaySign.class);

    /**
     * 签名加密
     * @param paymentOrder
     * @return
     */
    public static String paymentSign(PaymentOrder paymentOrder) {
        DecimalFormat df = new DecimalFormat("0.00");
        String text = paymentOrder.getUserId() + paymentOrder.getOrderId()
                      + df.format(paymentOrder.getAmount()) + paymentOrder.getAmountDetail()
                      + paymentOrder.getSubject() + paymentOrder.getSourceKey();

        if (null != paymentOrder.getFreeFeeType()) {
            text = text + paymentOrder.getFreeFeeType();
        }
        if (StringUtils.isNotBlank(paymentOrder.getInviterId())) {
            text = text + paymentOrder.getInviterId();
        }
        if(PayWay.isNeedCrossPay(paymentOrder.getCrossPay())){
            text += paymentOrder.getCrossPay();
        }
        return sign(text, null);
    }

    public static String sign(String paramsStr, String input_charSet) {
        String sign = "";
        if (StringUtils.isBlank(paramsStr))
            return sign;
        String charSet = (null == input_charSet ? DEFAULT_CHARSET : input_charSet);
        try {
            String md51 = DigestUtils.md5Hex(paramsStr.getBytes(charSet));
            sign = DigestUtils.md5Hex(md51.getBytes(charSet));
        } catch (Exception e) {
            throw new PayException(PayExceptionCode.SIGN_MD5_FAILED);
        }
        return sign;
    }

    /**
     * 签名验证
     * @param paymentOrder
     * @return
     */
    public static void verify(PaymentOrder paymentOrder) {
        String sign = paymentOrder.getSign();
        Assert.hasText(sign, PayExceptionCode.SIGN_IS_BLANK);
        String subject = paymentOrder.getSubject();
        Assert.isTrue(count(subject) <= SUBJECT_VALIDATE_LENGTH, PayExceptionCode.SIGN_TOO_LONG);
        String mySign = PaySign.paymentSign(paymentOrder);
        Assert.isTrue(sign.equals(mySign), PayExceptionCode.SIGN_VALIDATE_FAILED);
    }

    /**
     * 计算字符串长度，汉字算两个字符
     * @param str
     * @return
     */
    private static int count(String str) {
        int count = 0;
        if (str == null || str.length() == 0)
            return count;
        char[] chs = str.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            count += (chs[i] > 0xff) ? 2 : 1;
        }
        return count;
    }
}
