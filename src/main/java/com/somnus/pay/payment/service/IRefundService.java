package com.somnus.pay.payment.service;

import java.util.Map;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.Msg;

public interface IRefundService {
	
	/**
	 * 订单校验
	 * @param requestParamsMap
	 * @param payType
	 * @return
	 */
    public Msg verifyRefund(Map<String, String> requestParamsMap,PayChannel payType);
    
    /**
     * 提交退款请求
     * @param requestParamsMap
     * @param payType
     * @return
     */
    public String refundSubmit(Map<String, String> requestParamsMap,PayChannel payType);
    
	/**
	 * 发送到metaq 异步请求发送队列
	 * @param thirdTradeNo
	 * @param refundAmount
	 * @param success
	 * @param thirdPayType
	 * @param json
	 */
	public void syncSendToMetaq(String orderNum,String thirdTradeNo, String refundAmount, Integer refund_status, int thirdPayType, String msg);
}
