package com.somnus.pay.payment.pojo;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;

/**
 * 支付结果
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-24<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-24       丹青生                        1.0            初始化 <br/>
 *  
 */
public class PaymentResult {

	private String orderId = null;
	private String tradeStatus = "";
	private PayChannel channel;
	private String thirdTradeNo = "";
	private Double price = 0d;
	private String buyerId = "";
	private String memo = "";
	private String bankType = "";
	private String bankBillNo = "";
	private String attach = "";
	// 支付失败时候的错误信息
	private String payInfo = "";
	// 0：普通订单 1：合并订单
	private int isCombined = 0;
	private int status = PaymentOrderType.SCCUESS.getValue();

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public PayChannel getChannel() {
		return channel;
	}

	public void setChannel(PayChannel channel) {
		this.channel = channel;
	}

	public String getThirdTradeNo() {
		return thirdTradeNo;
	}

	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getBankBillNo() {
		return bankBillNo;
	}

	public void setBankBillNo(String bankBillNo) {
		this.bankBillNo = bankBillNo;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	public int getIsCombined() {
		return isCombined;
	}

	public void setIsCombined(int isCombined) {
		this.isCombined = isCombined;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PaymentResult [orderId=" + orderId + ", tradeStatus="
				+ tradeStatus + ", channel=" + channel
				+ ", thirdTradeNo=" + thirdTradeNo + ", price=" + price
				+ ", buyerId=" + buyerId + ", memo=" + memo + ", bankType="
				+ bankType + ", bankBillNo=" + bankBillNo + ", attach="
				+ attach + ", payInfo=" + payInfo + ", isCombined="
				+ isCombined + ", status=" + status + "]";
	}

}
