package com.somnus.pay.payment.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * <pre>
 * 订单来源实体类
 * </pre>
 *
 * @author masanbao
 * @version $ PaymentSource.java, v 0.1 2015年1月28日 下午4:57:29 masanbao Exp $
 * @since   JDK1.6
 */
@Entity
@Table(name = "t_user_payment_source")
public class PaymentSource implements Serializable {

    private static final long serialVersionUID = 4990594234839942433L;
    private Integer           id;
    private Integer           sourceId;
    private String            sourceKey;
    private String            returnUrl;
    private String            notifyUrl;
    private String            memo;
    private Date              createTime;

    private String resultUrl;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "source_id")
    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    @Column(name = "source_key")
    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    @Column(name = "return_url")
    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Column(name = "notify_url")
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

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

    @Column(name = "result_url")
    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    @Override
    public String toString() {
        return "PaymentSource{" +
                "id=" + id +
                ", sourceId=" + sourceId +
                ", sourceKey='" + sourceKey + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", memo='" + memo + '\'' +
                ", createTime=" + createTime +
                ", resultUrl='" + resultUrl + '\'' +
                '}';
    }
}
