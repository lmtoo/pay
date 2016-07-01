package com.somnus.pay.payment.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 支付配置入数据库表
 */
@Entity
@Table(name = "t_user_payment_config")
public class PaymentConfig implements Serializable {

    private static final long serialVersionUID = -3887045404428381695L;
    private Integer id; // 配置id
    private String keyName; // 配置关键字
    private String keyValue; // 配置内容
    private String memo; // 配置备注
    private Date createTime; // 配置创建时间
    private Date updateTime; // 最后更新时间

    public PaymentConfig() {
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

    @Column(name = "key_name")
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Column(name = "key_value")
    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    @Column(name = "memo")
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "PaymentConfig{" +
                "id=" + id +
                ", keyName='" + keyName + '\'' +
                ", keyValue='" + keyValue + '\'' +
                ", memo='" + memo + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
