package com.somnus.pay.payment.thirdPay.icbc.bean.res;

import java.util.ArrayList;

public class OrderInfo {

    protected String orderDate;
    protected String curType;
    protected String merID;

    protected ArrayList<SubOrderInfo> subOrderInfoList = new ArrayList<SubOrderInfo>(1);
}