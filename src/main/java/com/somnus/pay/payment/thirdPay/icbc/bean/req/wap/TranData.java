package com.somnus.pay.payment.thirdPay.icbc.bean.req.wap;

import com.somnus.pay.payment.util.XMLUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.KXml2Driver;

public class TranData {

	private static XStream xstream = new XStream(new KXml2Driver());
	protected static final String EMPTY = "";

    private String interfaceName;
    private String interfaceVersion;

	private OrderInfo orderInfo = new OrderInfo();
	protected Custom custom = new Custom();
	private Message message = new Message();

	static {
		xstream.alias("B2CReq", TranData.class);
		xstream.alias("orderInfo", OrderInfo.class);
	}

	public String toXML() {
		return XMLUtil.toXML(this, xstream);
	}

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public void setMerID(String merID) {
		orderInfo.merID = merID;
	}

	public void setOrderDate(String orderDate) {
		orderInfo.orderDate = orderDate;
	}

	public void setMerAcct(String merAcct) {
		orderInfo.merAcct = merAcct;
	}

	public void setOrderid(String orderid) {
		orderInfo.orderid = orderid;
	}

	public void setGoodsName(String goodsName) {
        message.goodsName = goodsName;
	}

	public void setAmount(String amount) {
		orderInfo.amount = amount;
	}

	public void setInstallmentTimes(String installmentTimes) {
		orderInfo.installmentTimes = installmentTimes;
	}

	public void setMerURL(String merURL) {
		message.merURL = merURL;
	}

	public void setCarriageAmt(String carriageAmt) {
        message.carriageAmt = carriageAmt;
	}
}
