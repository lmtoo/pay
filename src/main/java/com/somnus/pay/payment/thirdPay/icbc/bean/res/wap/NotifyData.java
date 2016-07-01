package com.somnus.pay.payment.thirdPay.icbc.bean.res.wap;

import com.somnus.pay.payment.util.XMLUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.KXml2Driver;

public class NotifyData {

	private static XStream xstream = new XStream(new KXml2Driver());

	protected String interfaceName;
	protected String interfaceVersion;

	protected OrderInfo orderInfo;
	protected Custom custom;
	protected Bank bank;

	static {
		xstream.alias("B2CRes", NotifyData.class);
		xstream.alias("orderInfo", OrderInfo.class);
        xstream.alias("bank", Bank.class);
	}

	public static NotifyData fromXML(String xml) {
		return XMLUtil.fillByXml(xml, new NotifyData(), xstream);
	}

	public String getTranSerialNo() {
		return bank.TranSerialNo;
	}

	public String getNotifyDate() {
		return bank.notifyDate;
	}

	public String getTranStat() {
		return bank.tranStat;
	}

	public String getComment() {
		return bank.comment;
	}

	public String getOrderid() {
		return orderInfo.orderid;
	}

	public String getAmount() {
		return orderInfo.amount;
	}

	public String getInstallmentTimes() {
		return orderInfo.installmentTimes;
	}
}
