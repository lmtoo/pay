package com.somnus.pay.payment.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author masanbao
 * @version $ PaymentOrderStatusErr.java, v 0.1 2015年1月28日 下午4:56:38 masanbao Exp $
 * @since   JDK1.6
 */
@Entity
@Table(name = "t_user_chgOrder_status_err")
public class PaymentOrderStatusErr {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "order_id")
    private String  orderId;

    @Column(name = "url")
    private String  url;

    @Column(name = "msg")
    private String  msg;

    @Column(name = "err_num")
    private Integer errNum;

    @Column(name = "status_code")
    private String  statusCode;

    @Column(name = "create_time")
    private Date    createTime;

    @Column(name = "update_time")
    private Date    updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getErrNum() {
        return errNum;
    }

    public void setErrNum(Integer errNum) {
        this.errNum = errNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
