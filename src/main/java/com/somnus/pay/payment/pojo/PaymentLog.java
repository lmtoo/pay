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

import org.hibernate.validator.constraints.NotBlank;

/**
 * <pre>
 * 支付日志记录实体类
 * </pre>
 *
 * @author masanbao
 * @version $ PaymentLog.java, v 0.1 2015年1月28日 下午4:54:37 masanbao Exp $
 * @since   JDK1.6
 */
@Entity
@Table(name = "t_user_payment_log")
public class PaymentLog {

    private Integer id;
    private String  orderId;
    private String  operatorId;
    private String  subject;
    private String  memo;
    private Date    createTime;

    public PaymentLog() {
    }

    public PaymentLog(String orderId, String operatorId, String subject, String memo) {
        this.orderId = orderId;
        this.operatorId = operatorId;
        this.subject = subject;
        this.memo = memo;
    }

    public PaymentLog(String orderId, String operatorId, String subject, String memo, Date date) {
        this.orderId = orderId;
        this.operatorId = operatorId;
        this.subject = subject;
        this.memo = memo;
        this.createTime = date;
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

    @NotBlank(message = "{payment.order_id}")
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
