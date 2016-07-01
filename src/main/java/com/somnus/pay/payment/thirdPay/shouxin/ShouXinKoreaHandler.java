package com.somnus.pay.payment.thirdPay.shouxin;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.thirdPay.shouxin.config.ShouXinConfig;
import org.springframework.stereotype.Service;

/**
 * @description: 首信易韩国支付渠道回调处理器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 */
@Service
public class ShouXinKoreaHandler extends CustomsDeclarationHandler {
	
	public ShouXinKoreaHandler() {
		super(ShouXinConfig.KOREA_V_MID, ShouXinConfig.KOREA_V_MD5_KEY_INFO,
				PayChannel.ShouXinKoreaPay, PayChannel.V_ShouXinKoreaPay_BG_NingBo,
				PayChannel.V_ShouXinKoreaPay_BG_GuangZhou, PayChannel.V_ShouXinKoreaPay_BG_BaiYun, PayChannel.V_ShouXinKoreaPay_BG_NanSha);
	}

}
