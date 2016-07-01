package com.somnus.pay.payment.thirdPay.icbc.bean.res;

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
        xstream.alias("subOrderInfo", SubOrderInfo.class);
    }

    public static NotifyData fromXML(String xml) {
        return XMLUtil.fillByXml(xml, new NotifyData(), xstream);
    }

    public String getTranSerialNo() {
        return orderInfo.subOrderInfoList.get(0).tranSerialNo;
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
        return orderInfo.subOrderInfoList.get(0).orderid;
    }

    public String getAmount() {
        return orderInfo.subOrderInfoList.get(0).amount;
    }

    public String getInstallmentTimes() {
        return orderInfo.subOrderInfoList.get(0).installmentTimes;
    }
}
