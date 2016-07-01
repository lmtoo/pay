package com.somnus.pay.payment.thirdPay.icbc.bean.req;
import static com.somnus.pay.payment.thirdPay.icbc.bean.req.TranData.EMPTY;


public class SubOrderInfo {

	protected String orderid;
	protected String amount;// 分
	protected String installmentTimes;// 分期付款期数
	protected String merAcct;// 商户入账账号
	protected String goodsID = "001";
	protected String goodsName;
	protected String goodsNum = "1";
	protected String carriageAmt = EMPTY;
}
