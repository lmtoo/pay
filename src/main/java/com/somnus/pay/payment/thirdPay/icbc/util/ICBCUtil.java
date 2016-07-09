package com.somnus.pay.payment.thirdPay.icbc.util;

import cn.com.infosec.icbc.ReturnValue;

import com.somnus.pay.payment.thirdPay.icbc.bean.res.NotifyData;
import com.somnus.pay.payment.thirdPay.icbc.config.ICBCConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
  * @description: 工行工具类，主要获得订单id
  * @author: qingshu
  * @version: 1.0
  * @createdate: 2015-08-20
  * Modification  History:
  * Date         Author        Version        Discription
  * -----------------------------------------------------------------------------------
  * 2015-08-20   qingshu       1.0            支付能力
 */
public class ICBCUtil {
    protected static Log logger = LogFactory.getLog(ICBCUtil.class);

    //获得支付号
    public static String obtainOrderId(Map<String, String> params) {
        String notifyData = params.get("notifyData");
        NotifyData data = new NotifyData();
        if (StringUtils.isNotBlank(notifyData)) {
            try {
                String src = new String(ReturnValue.base64dec(notifyData.getBytes(ICBCConfig.CHAR_SET)), ICBCConfig.CHAR_SET);
                data = NotifyData.fromXML(src);
                return data.getOrderid();
            } catch (Exception e) {
                logger.error("ICBCUtil obtainOrderId error:",e);
            }
        }
        return null;
    }

    //获得支付号
    public static String wapObtainOrderId(Map<String, String> params) {
        String notifyData = params.get("notifyData");
        com.somnus.pay.payment.thirdPay.icbc.bean.res.wap.NotifyData data = new com.somnus.pay.payment.thirdPay.icbc.bean.res.wap.NotifyData();
        if (StringUtils.isNotBlank(notifyData)) {
            try {
                String src = new String(ReturnValue.base64dec(notifyData.getBytes(ICBCConfig.CHAR_SET)), ICBCConfig.CHAR_SET);
                data = com.somnus.pay.payment.thirdPay.icbc.bean.res.wap.NotifyData.fromXML(src);
                return data.getOrderid();
            } catch (Exception e) {
                logger.error("ICBCUtil wapObtainOrderId error:",e);
            }
        }
        return null;
    }
}
