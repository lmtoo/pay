package com.somnus.pay.payment.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 支付项目-退款类
 * 
 * @author mike
 *
 */
@Entity
@Table(name = "t_user_payment_refund")
public class PaymentRefund {

	private Integer id;
	private String operatorId; // 发起请求的操作人
	private String orderNum; // 订单接口的订单号
	private String orderId; // 支付号
	private Integer thirdPayType;  // 支付渠道
	private String thirdTradeNo; // 第三方订单号
	private Double refundAmount; // 退款金额
	private Integer refundStatus; // #退款状态 0 | 1 退款成功 | -1 退款失败
	private String refundMsg ;
	private Date createTime;
	private Date updateTime;

	public PaymentRefund() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "order_id", nullable = false)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name = "operator_id", length = 50)
	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	@Column(name = "order_num", length = 50)
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "thirdPayType")
	public Integer getThirdPayType() {
		return thirdPayType;
	}

	public void setThirdPayType(Integer thirdPayType) {
		this.thirdPayType = thirdPayType;
	}
	@Column(name = "third_trade_no")
	public String getThirdTradeNo() {
		return thirdTradeNo;
	}

	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo;
	}

	@Column(name = "refund_amount")
	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	@Column(name = "refund_status")
	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}
	@Column(name = "refund_msg")
	public String getRefundMsg() {
		return refundMsg;
	}

	public void setRefundMsg(String refundMsg) {
		this.refundMsg = refundMsg;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "PaymentRefund [orderId=" + orderId + ", operatorId="
				+ operatorId + ", orderNum=" + orderNum
				+ ", thirdPayType=" + thirdPayType + ", refundAmount="
				+ refundAmount + ", refundStatus=" + refundStatus //+ ",refundMsg=" + refundMsg
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ "]";
	}
	
}
