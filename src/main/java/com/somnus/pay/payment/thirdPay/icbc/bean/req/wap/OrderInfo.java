package com.somnus.pay.payment.thirdPay.icbc.bean.req.wap;

public class OrderInfo {

	protected String orderDate;
    protected String orderid;
    protected String amount;// 分
    protected String installmentTimes;// 分期付款期数
    protected String curType = "001";// 支付币种，人民币
    protected String merID;
    protected String merAcct;// 商户入账账号
}
