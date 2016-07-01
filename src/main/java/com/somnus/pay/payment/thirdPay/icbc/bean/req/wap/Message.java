package com.somnus.pay.payment.thirdPay.icbc.bean.req.wap;

import static com.somnus.pay.payment.thirdPay.icbc.bean.req.wap.TranData.EMPTY;

public class Message {

    protected String goodsID = EMPTY;
    protected String goodsName;
    protected String goodsNum = EMPTY;
    protected String carriageAmt = EMPTY;
    protected String merHint = EMPTY;
    protected String remark1 = EMPTY;
    protected String remark2 = EMPTY;
    protected String merURL = EMPTY;
    protected String merVAR = EMPTY;
    protected String notifyType = "HS";// 在交易完成后，实时发送通知结果
    protected String resultType = "1";// 无论支付成功或者失败，都发送支付结果
    protected String backup1 = EMPTY;
    protected String backup2 = EMPTY;
    protected String backup3 = EMPTY;
    protected String backup4 = EMPTY;
}
