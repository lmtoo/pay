package com.somnus.pay.payment.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 支付项目- 对外接口操作类
 *
 * @author qingshu
 *
 */
@Entity
@Table(name = "t_user_payment_oper")
public class PaymentOper implements Serializable {

    private static final long serialVersionUID = -7177156497705541402L;
    
    private Integer id;
    private String operatorId; // 发起请求的操作人
    private String payId; // 支付号
    private Integer thirdPayType;  // 第三方支付支付渠道
    private String thirdTradeNo; // 第三方支付平台订单号
    private Double thirdTradeAmount; // 第三方支付平台支付金额
    private Integer status; // 支付状态
    private Date createTime;
    private Date updateTime;
    private String operatorMsg;// 操作描述
    private int operatorType = 0; //操作类型，（默认为0代表CRM手动调用）
    private String memo; //备注

    public PaymentOper() {

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

    @Column(name = "operator_id", length = 50)
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    @Column(name = "pay_id")
    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    @Column(name = "third_pay_type")
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

    @Column(name = "third_trade_amount")
    public Double getThirdTradeAmount() {
        return thirdTradeAmount;
    }

    public void setThirdTradeAmount(Double thirdTradeAmount) {
        this.thirdTradeAmount = thirdTradeAmount;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Column(name = "operator_msg")
    public String getOperatorMsg() {
        return operatorMsg;
    }

    public void setOperatorMsg(String operatorMsg) {
        this.operatorMsg = operatorMsg;
    }

    @Column(name = "operator_type")
    public int getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(int operatorType) {
        this.operatorType = operatorType;
    }

    @Column(name = "memo")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return "PaymentOper{" +
                "id=" + id +
                ", operatorId='" + operatorId + '\'' +
                ", payId='" + payId + '\'' +
                ", thirdPayType=" + thirdPayType +
                ", thirdTradeNo='" + thirdTradeNo + '\'' +
                ", thirdTradeAmount=" + thirdTradeAmount +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", operatorMsg='" + operatorMsg + '\'' +
                ", operatorType=" + operatorType +
                '}';
    }
}
