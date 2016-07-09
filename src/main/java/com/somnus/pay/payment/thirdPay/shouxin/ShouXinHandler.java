package com.somnus.pay.payment.thirdPay.shouxin;

import org.springframework.stereotype.Service;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.thirdPay.shouxin.config.ShouXinConfig;

/**
 * @description: 首信易支付渠道回调处理器
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 */
@Service
public class ShouXinHandler extends CustomsDeclarationHandler {

	public ShouXinHandler() {
		super(ShouXinConfig.V_MID, ShouXinConfig.V_MD5_KEY_INFO,
				PayChannel.ShouXinPay, PayChannel.V_ShouXinPay_BG_NingBo,
				PayChannel.V_ShouXinPay_BG_GuangZhou, PayChannel.V_ShouXinPay_BG_BaiYun, PayChannel.V_ShouXinPay_BG_NanSha);
	}

}
