package com.somnus.pay.payment.thirdPay.alipay;

import org.springframework.stereotype.Service;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.thirdPay.alipay.config.AlipayConfig;

/**
 * @description: 支付宝支付渠道回调处理器
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 * 2015-12-23   丹青生                       1.0            结构调整
 */
@Service
public class AliKorea2WapHandler extends CustomsDeclarationHandler {

	public AliKorea2WapHandler() {
		super(AlipayConfig.WAP_KOREA_CROSS_PAY_PARTNER, AlipayConfig.WAP_KOREA_CROSS_PAY_MD5_KEY,
				PayChannel.AliKorea2WapPay, PayChannel.V_AliKorea2WapPay_BG_NingBo,
				PayChannel.V_AliKorea2WapPay_BG_BaiYun, PayChannel.V_AliKorea2WapPay_BG_NanSha);
	}
	
}
