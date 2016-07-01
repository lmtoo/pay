package com.somnus.pay.payment.thirdPay.icbc.bean.req;
import static com.somnus.pay.payment.thirdPay.icbc.bean.req.TranData.EMPTY;


public class Message {

	protected String creditType = "2";// 借记卡和信用卡都能对订单进行支付
	protected String notifyType = "HS";// 在交易完成后，实时发送通知结果
	protected String resultType = "1";// 无论支付成功或者失败，都发送支付结果
	protected String merReference = EMPTY;
	protected String merCustomIp = EMPTY;
	protected String goodsType = "0";// 实物商品
	protected String merCustomID = EMPTY;
	protected String merCustomPhone = EMPTY;
	protected String goodsAddress = "无";
	protected String merOrderRemark = "无";
	protected String merHint = EMPTY;
	protected String remark1 = EMPTY;
	protected String remark2 = EMPTY;
	protected String merURL = EMPTY;
	protected String merVAR = EMPTY;
}
