package com.somnus.pay.payment.thirdPay.abc;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abc.pay.client.Base64;
import com.abc.pay.client.JSON;
import com.abc.pay.client.ebus.PaymentRequest;
import com.abc.pay.client.ebus.QueryOrderRequest;
import com.somnus.pay.exception.B5mException;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandler;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.abc.config.ABCConfig;
import com.somnus.pay.payment.util.DateUtils;
import com.somnus.pay.payment.util.HTMLUtil;
import com.somnus.pay.payment.util.PayAmountUtil;
import com.somnus.pay.utils.Assert;
import com.somnus.pay.utils.MoneyUtils;

/**
 * @description: 农行支付渠道接口接入抽象类
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2016/2/24
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2016/2/24    qingshu       1.0            农行支付能力
 */
public abstract class ABCAbstractHandler extends PaymentChannelHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(ABCAbstractHandler.class);

    public ABCAbstractHandler(PayChannel payChannel) {
        super(payChannel);
    }

    @Override
    protected String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
        PaymentOrder paymentOrder = parameter.getData();
        LOGGER.info("农行支付信息请求参数paymentOrder:{}", paymentOrder);
        String date = DateUtils.Date2String(paymentOrder.getCreateTime(), "yyyy/MM/ddHH:mm:ss");
        String orderDate = date.substring(0, 10); // 交易日期
        String orderTime = date.substring(10, date.length()); // 交易时间
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.dicOrder.put("PayTypeID", ABCConfig.Pay_Type_ID);                   //设定交易类型
        paymentRequest.dicOrder.put("OrderDate", orderDate );                  //设定订单日期 （必要信息 - YYYY/MM/DD）
        paymentRequest.dicOrder.put("OrderTime", orderTime);                   //设定订单时间 （必要信息 - HH:MM:SS）
        paymentRequest.dicOrder.put("OrderNo", paymentOrder.getOrderId());                       //设定订单编号 （必要信息）
        paymentRequest.dicOrder.put("CurrencyCode", ABCConfig.CURRENCEYCODE_RMB);             //设定交易币种
        paymentRequest.dicOrder.put("OrderAmount", PayAmountUtil.getDoubleFormat(parameter.getData().getAmount()));      //设定交易金额
        paymentRequest.dicOrder.put("OrderDesc", paymentOrder.getSubject());                   //设定订单说明
        paymentRequest.dicOrder.put("InstallmentMark", ABCConfig.InstallmentMark_NO_FENQI);       //分期标识
        paymentRequest.dicOrder.put("CommodityType", ABCConfig.COMMODITYTYPE_0202);           //设置商品种类
        this.buildCreateOrderParameter(paymentRequest);
        //订单明细
        LinkedHashMap<String, String> orderitem = new LinkedHashMap<String, String>(3);
        orderitem.put("ProductName",paymentOrder.getSubject());//商品名称
        orderitem.put("UnitPrice",PayAmountUtil.getDoubleFormat(parameter.getData().getAmount()));//商品总价
        orderitem.put("Qty","1");//商品数量
        paymentRequest.orderitems.put(1, orderitem);
        paymentRequest.dicRequest.put("PaymentType", ABCConfig.PAYMENTTYPE_1);  //设定支付类型
        paymentRequest.dicRequest.put("NotifyType", ABCConfig.NOTIFYTYPE_1);              //设定通知方式
        paymentRequest.dicRequest.put("IsBreakAccount", "0"); //交易是否分账
        //同步给农行获取下单结果
        JSON json = paymentRequest.postRequest();
        LOGGER.info("农行支付信息请求参数:{}，返回结果:{}", paymentRequest,json);
        String returnCode = json.GetKeyValue("ReturnCode");
        if (ABCConfig.REQUEST_ORDER_SUCCESS.equals(returnCode)){
            String html = HTMLUtil.createSubmitHtml(json.GetKeyValue("PaymentURL"), new HashMap<String, String>(), null, null);
            LOGGER.info("农行支付跳转form:{}", html);
            return html;
        } else {
        	String errorMessage = json.GetKeyValue("ErrorMessage");
            LOGGER.warn("农行支付跳转页面获取失败:" + errorMessage);
            throw new B5mException(PayExceptionCode.THIRD_PAY_ERROR);
        }
    }

    protected abstract void buildCreateOrderParameter(PaymentRequest paymentRequest);
    
    @Override
    protected String handleNotify(Map<String, String> data) {
        checkSign(data);
        return ABCConfig.NOTIFY_SUCCESS_RESULT;
    }

    @Override
    protected String handleReturn(Map<String, String> data) {
        checkSign(data);
        PaymentResult[] paymentResults = convert(data);
        return paymentResults.length>0 ? ((null!=paymentResults[0]?paymentResults[0].getOrderId():null)):null;
    }

    protected void checkSign(Map<String, String> data) {
        LOGGER.info("农行校验返回结果签名:{}", data);
        boolean success = false;
        String msg = data.get("MSG");
        com.abc.pay.client.ebus.PaymentResult result = null;
        try {
            result = new com.abc.pay.client.ebus.PaymentResult(msg);
            success = result.isSuccess();
        } catch (Exception e) {
            LOGGER.warn("农行支付回调验证参数出错:{}", data, e);
        }
        Assert.isTrue(success, PayExceptionCode.SIGN_ERROR);
    }

    @Override
    public String getFailedResponse(RequestParameter<?, ?> parameter) {
        return ABCConfig.NOTIFY_FAIL_RESULT;
    }

    @Override
    protected PaymentResult[] convert(Map<String, String> data) {
        LOGGER.info("农行支付回调信息转换convert:{}",data);
        try {
            String msg = data.get("MSG");
            com.abc.pay.client.ebus.PaymentResult result = new com.abc.pay.client.ebus.PaymentResult(msg);
            String respCode = result.getReturnCode();
            String respDesc = result.getErrorMessage();
            String orderId = result.getValue("OrderNo"); // 订单号
            String tranAmt = result.getValue("Amount"); // 订单金额
            String voucherNo = result.getValue("VoucherNo"); // 交易凭证号
            String iRspRef = result.getValue("iRspRef"); // 银行返回交易流水号
            String hostDate = StringUtils.defaultString(result.getValue("HostDate")).replace("/", ""); // 银行交易日期(yyyy/MM/DD)
            String hostTime = StringUtils.defaultString(result.getValue("HostTime")).replace(":", ""); // 银行交易时间(HH:MM:SS)
            String tranDateTime = hostDate.concat(hostTime);
            String AccountName = result.getValue("AccountName"); //户名
            LOGGER.info("农行支付回调信息转换orderId:{},tranAmt:{},voucherNo:{},iRspRef:{},AccountName:{},hostDate:{},hostTime:{},tranDateTime:{}respCode:{},respDesc:{}",
                    new Object[]{orderId,tranAmt,voucherNo,iRspRef,AccountName,hostDate,hostTime,tranDateTime,respCode,respDesc});
            PaymentResult paymentResult = new PaymentResult();
            paymentResult.setIsCombined(0);
            paymentResult.setOrderId(orderId);
            paymentResult.setTradeStatus(result.isSuccess()+"");
            paymentResult.setThirdTradeNo(iRspRef);
            paymentResult.setPrice(Double.valueOf(StringUtils.defaultIfBlank(tranAmt, "0")));
            paymentResult.setPayInfo("");
            paymentResult.setStatus(result.isSuccess()? PaymentOrderType.SCCUESS.getValue() : PaymentOrderType.FAIL.getValue());
            return new PaymentResult[]{paymentResult};
        } catch (Exception e) {
            LOGGER.warn("农行支付回调信息转换出错:{}", data, e);
            throw new PayException(PayExceptionCode.CALLBACK_PARAMETER_CONVERT_ERROR, e);
        }
    }

    @Override
    protected boolean handleConfirm(RequestParameter<String, PaymentResult> parameter) {
        boolean success = false;
        LOGGER.info("农行渠道正在确认订单[{}]是否支付成功", parameter.getData());
        RequestParameter<String, Map<String, String>> newParameter = new RequestParameter<String, Map<String, String>>();
        newParameter.setChannel(parameter.getChannel());
        newParameter.setType(parameter.getType());
        newParameter.setData(parameter.getData());
        Map<String, String> result = this.handleQuery(newParameter);
        if(null != result){
            PaymentOrder order = paymentOrderService.get(parameter.getData());
            if(order == null){
                LOGGER.warn("农行渠道订单[{}]不存在", parameter.getData());
            }else {
                double amount = result.containsKey("OrderAmount") ? Double.parseDouble(result.get("OrderAmount")) : 0.0D;
                LOGGER.info("农行渠道确认订单[{}]的已支付金额[{}]与待支付金额[{}]是否一致", new Object[]{parameter.getData(),order.getAmount(),amount});
                success = order.getAmount().equals(amount);
            }
            if (success){
                PaymentResult paymentResult = new PaymentResult();
                paymentResult.setStatus(PaymentOrderType.SCCUESS.getValue());
                parameter.setResult(paymentResult);
            }
        }
        return success;
    }

    /**
     * 根据订单号查询第三方支付结果
     * @param parameter 请求参数
     * @return 第三方支付结果
     */
    protected Map<String, String> handleQuery(RequestParameter<String, Map<String, String>> parameter){
        String orderId = parameter.getData();
        LOGGER.info("农行查询订单[{}]支付结果", orderId);
        try {
            Map<String, String> result = null;
            QueryOrderRequest queryOrderRequest = new QueryOrderRequest();
            queryOrderRequest.queryRequest.put("PayTypeID",ABCConfig.Pay_Type_ID);
            queryOrderRequest.queryRequest.put("OrderNo",orderId);
            queryOrderRequest.queryRequest.put("QueryDetail", ABCConfig.QUERY_DETAIL);
            JSON response = queryOrderRequest.postRequest();
            String returnCode = response.GetKeyValue("ReturnCode");
            String errorMessage = response.GetKeyValue("ErrorMessage");
            String orderInfo = response.GetKeyValue("Order");
            Base64 tBase64 = new Base64();
            String orderDetail = new String(tBase64.decode(orderInfo));
            response.setJsonString(orderDetail);
            if(StringUtils.isNotBlank(orderDetail) && ABCConfig.REQUEST_ORDER_SUCCESS.equals(returnCode)) {
                String status = response.GetKeyValue("Status");
                LOGGER.info("农行查询订单支付结果:orderId:{},status:{}ReturnCode:{},ErrorMessage:{}", new Object[]{orderId, status, returnCode, errorMessage});
                if (ABCConfig.QUERY_ORDER_IS_SUCCESS.equals(status)) {
                    result = new HashMap<String, String>();
                    result.put("OrderNo", response.GetKeyValue("OrderNo"));
                    result.put("OrderAmount", response.GetKeyValue("OrderAmount"));
                    result.put("Status", status);
                }
            }
            return result;
        } catch (B5mException e) {
            LOGGER.warn("农行查询订单支付结果失败", e);
            throw e;
        } catch (Exception e) {
            LOGGER.warn("农行查询订单支付结果失败", e);
            throw new B5mException(PayExceptionCode.QUERY_ORDER_PAY_RESULT_ERROR);
        }
    }



}
