package com.somnus.pay.payment.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Embeddable;
import javax.persistence.IdClass;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.somnus.pay.payment.enums.CustomsDeclarationStatus;
import com.somnus.pay.payment.enums.PayChannel;

/**
 * @description: 报关实体
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/25
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/25       方东白          1.0       报关实体
 */
@Entity
@Table(name = "t_payment_customs_declaration")
@IdClass(value = CustomsDeclaration.CustomsDeclarationUPK.class)
public class CustomsDeclaration implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderId;
    private PayChannel channel;
    private String bgConfigKey;//code+place拼接
    private CustomsDeclarationStatus status;
    private String result;
    private Date createTime;
    private Date updateTime;

    @Id
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Id
    public PayChannel getChannel() {
        return channel;
    }

    public void setChannel(PayChannel channel) {
        this.channel = channel;
    }

    @Id
    public String getBgConfigKey() {
        return bgConfigKey;
    }

    public void setBgConfigKey(String bgConfigKey) {
        this.bgConfigKey = bgConfigKey;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public CustomsDeclarationStatus getStatus() {
        return status;
    }

    public void setStatus(CustomsDeclarationStatus status) {
        this.status = status;
    }

    @Column(name = "result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomsDeclaration that = (CustomsDeclaration) o;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (channel != that.channel) return false;
        if (bgConfigKey != null ? !bgConfigKey.equals(that.bgConfigKey) : that.bgConfigKey != null) return false;
        if (status != that.status) return false;
        return !(result != null ? !result.equals(that.result) : that.result != null);

    }

    @Override
    public int hashCode() {
        int result1 = orderId != null ? orderId.hashCode() : 0;
        result1 = 31 * result1 + (channel != null ? channel.hashCode() : 0);
        result1 = 31 * result1 + (bgConfigKey != null ? bgConfigKey.hashCode() : 0);
        result1 = 31 * result1 + (status != null ? status.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }


    @Embeddable
    public static class CustomsDeclarationUPK implements Serializable {

        private static final long serialVersionUID = 1L;
        private String orderId;
        private PayChannel channel;
        private String bgConfigKey;//code+place拼接

        public CustomsDeclarationUPK() {
        }

        public CustomsDeclarationUPK(String orderId, PayChannel channel, String bgConfigKey) {
            this.orderId = orderId;
            this.channel = channel;
            this.bgConfigKey = bgConfigKey;
        }

        @Column(name = "order_id")
        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        @Column(name = "channel")
        @Enumerated(EnumType.STRING)
        public PayChannel getChannel() {
            return channel;
        }

        public void setChannel(PayChannel channel) {
            this.channel = channel;
        }

        @Column(name = "bg_config_key")
        public String getBgConfigKey() {
            return bgConfigKey;
        }

        public void setBgConfigKey(String bgConfigKey) {
            this.bgConfigKey = bgConfigKey;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomsDeclarationUPK that = (CustomsDeclarationUPK) o;
            if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
            if (channel != that.channel) return false;
            return !(bgConfigKey != null ? !bgConfigKey.equals(that.bgConfigKey) : that.bgConfigKey != null);

        }

        @Override
        public int hashCode() {
            int result = orderId != null ? orderId.hashCode() : 0;
            result = 31 * result + (channel != null ? channel.hashCode() : 0);
            result = 31 * result + (bgConfigKey != null ? bgConfigKey.hashCode() : 0);
            return result;
        }
    }

}
