package com.somnus.pay.payment.thirdPay.cmb;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.cmb.config.CMBConfig;
import com.somnus.pay.payment.thirdPay.tencent.util.PayCommonUtil;
import com.somnus.pay.payment.util.DateUtils;
import com.somnus.pay.payment.util.HTMLUtil;
import com.somnus.pay.payment.util.PayAmountUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.payment.util.PageCommonUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * @description: 招行支付渠道回调处理器
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 */
@Component
public class CMBHandler extends AbstractCMBHandler {

	public CMBHandler() {
		super(PayChannel.CMBPay, CMBConfig.BRANCH_ID,CMBConfig.CO_NO);
	}

	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
		Map<String, String> requestMap = new HashMap<String, String>(7);
		requestMap.put("BranchID", CMBConfig.BRANCH_ID);
		requestMap.put("CoNo", CMBConfig.CO_NO);
		requestMap.put("BillNo", PayCommonUtil.createPayId(parameter.getData().getOrderId(), CMBConfig.BILLNO_LENGTH));
		requestMap.put("Amount", PayAmountUtil.getDoubleFormat(parameter.getData().getAmount()));
		requestMap.put("Date", DateUtils.Date2String(new Date(), DateUtils.DATE_FORMATTER_YYYYMMDD));
		requestMap.put("MerchantUrl", PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + CMBConfig.WEB_FRONTURL);
		requestMap.put("MerchantPara", parameter.getData().getOrderId());// 因为提交给招行的订单号易重复，所以我们使用该字段回传我们平台记录的请求流水号
//        reqMap.put("MerchantCode", code); //用招行做单纯网银渠道的时候使用
		return HTMLUtil.createSubmitHtml(CMBConfig.ACTION_URL, requestMap, null, null);
	}
}
