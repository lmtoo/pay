package com.somnus.pay.payment.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <pre>
 * 支付渠道定义类
 * </pre>
 *
 * @author mike
 * @version $ PaymentChannel.java, v 0.1 2015年9月15日
 * @since   JDK1.7
 */
@Entity
@Table(name = "t_user_payment_channel")
public class PaymentChannel implements Serializable {

    private static final long serialVersionUID = -3887045404428381695L;
    private Integer id; // 渠道id
    private String keyName; // 渠道关键字
    private String name; // 渠道名称
    private String title; // 渠道显示
    private String blockId; // 页面表单ParentID
    private String blockClass; // 页面表单ParentClass
    private String formId; // 页面表单ID
    private String formValue; // 页面表单value
    private String formClass; // 页面表单class
    private String dataMps; // 页面表单data_mps
    private String dataId; // 页面表单data_id
    private String channelImg; // 渠道logo  #如果有多个使用,号分隔
    private Integer status; // 渠道状态 # 1 正常 | 0 关闭
    private Integer supportPlatform; //  # com.somnus.pay.payment.frame.enums.UAType ：pc wap app(android,ios),wxBrowser
    private Date createTime; // 渠道创建时间
    private Date updateTime; // 最后更新时间
    private Integer isUnionPayChannel; // 是否为网银支付主通道
    private Integer isFastPayChannel; // 是否为快捷支付主通道
    private Integer orderNum; // 默认排序
    private Integer displayChannelType; // 显示类型
    private boolean defaultChecked; // 是否默认选中
    private Integer thirdPayType; //第三方支付渠道
    private String desc;//相关描述

    public PaymentChannel() {
    }

    @Column(name = "key_name")
    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    @Column(name = "third_pay_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "form_id")
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    @Column(name = "form_value")
    public String getFormValue() {
        return formValue;
    }

    public void setFormValue(String formValue) {
        this.formValue = formValue;
    }

    @Column(name = "data_mps")
    public String getDataMps() {
        return dataMps;
    }

    public void setDataMps(String dataMps) {
        this.dataMps = dataMps;
    }

    @Column(name = "data_id")
    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    @Column(name = "channel_img")
    public String getChannelImg() {
        return channelImg;
    }

    public void setChannelImg(String channelImg) {
        this.channelImg = channelImg;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "support_platform")
    public Integer getSupportPlatform() {
        return supportPlatform;
    }

    public void setSupportPlatform(Integer supportPlatform) {
        this.supportPlatform = supportPlatform;
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

    @Column(name = "is_unionpay_channel")
    public Integer getIsUnionPayChannel() {
        return isUnionPayChannel;
    }

    public void setIsUnionPayChannel(Integer isUnionPayChannel) {
        this.isUnionPayChannel = isUnionPayChannel;
    }

    @Column(name = "is_fastpay_channel")
    public Integer getIsFastPayChannel() {
        return isFastPayChannel;
    }

    public void setIsFastPayChannel(Integer isFastPayChannel) {
        this.isFastPayChannel = isFastPayChannel;
    }

    @Column(name = "order_num")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Column(name = "display_channel_type")
    public Integer getDisplayChannelType() {
        return displayChannelType;
    }

    public void setDisplayChannelType(Integer displayChannelType) {
        this.displayChannelType = displayChannelType;
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

    @Column(name = "block_id")
    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    @Column(name = "form_class")
    public String getFormClass() {
        return formClass;
    }

    public void setFormClass(String formClass) {
        this.formClass = formClass;
    }

    @Column(name = "default_checked")
    public boolean isDefaultChecked() {
        return defaultChecked;
    }

    public void setDefaultChecked(boolean defaultChecked) {
        this.defaultChecked = defaultChecked;
    }

    @Column(name = "third_pay_type")
    public Integer getThirdPayType() {
        return thirdPayType;
    }

    public void setThirdPayType(Integer thirdPayType) {
        this.thirdPayType = thirdPayType;
    }

    @Column(name = "block_class")
    public String getBlockClass() {
        return blockClass;
    }

    public void setBlockClass(String blockClass) {
        this.blockClass = blockClass;
    }

    @Transient
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Transient
    public boolean isDispalyZFPlatform(){
        if(this.displayChannelType.equals(1)){
            return true;
        }
        return false;
    }
    @Transient
    public boolean isDispalyPayOnlinePlatform(){
        if(this.displayChannelType.equals(2)){
            return true;
        }
        return false;
    }
    @Transient
    public boolean isDispalyFastPayPlatform(){
        if(this.displayChannelType.equals(3)){
            return true;
        }
        return false;
    }
    @Transient
    public boolean isWebChannel(){
        if(this.supportPlatform.equals(0)){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "PaymentChannel{" +
                "id=" + id +
                ", keyName='" + keyName + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", blockId='" + blockId + '\'' +
                ", blockClass='" + blockClass + '\'' +
                ", formId='" + formId + '\'' +
                ", formValue='" + formValue + '\'' +
                ", formClass='" + formClass + '\'' +
                ", dataMps='" + dataMps + '\'' +
                ", dataId='" + dataId + '\'' +
                ", channelImg='" + channelImg + '\'' +
                ", status=" + status +
                ", supportPlatform=" + supportPlatform +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", isUnionPayChannel=" + isUnionPayChannel +
                ", isFastPayChannel=" + isFastPayChannel +
                ", orderNum=" + orderNum +
                ", displayChannelType=" + displayChannelType +
                ", defaultChecked=" + defaultChecked +
                ", thirdPayType=" + thirdPayType +
                ", desc='" + desc + '\'' +
                '}';
    }
}
