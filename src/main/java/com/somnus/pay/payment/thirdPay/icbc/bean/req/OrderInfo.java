package com.somnus.pay.payment.thirdPay.icbc.bean.req;

import java.util.ArrayList;


public class OrderInfo {

	protected String orderDate;
	protected String curType = "001";// 支付币种，人民币
	protected String merID;
	protected ArrayList<SubOrderInfo> subOrderInfoList = new ArrayList<SubOrderInfo>(1);

	public OrderInfo() {
		this.subOrderInfoList.add(new SubOrderInfo());
	}
}
