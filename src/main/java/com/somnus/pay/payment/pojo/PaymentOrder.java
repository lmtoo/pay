package com.somnus.pay.payment.pojo;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.somnus.pay.payment.model.PayWayType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.model.Payment;

/**
 * <pre>
 * 支付信息实体类
 * </pre>
 *
 * @author masanbao
 * @version $ PaymentOrder.java, v 0.1 2015年1月28日 下午4:55:43 masanbao Exp $
 * @since   JDK1.6
 */
@Entity
@Table(name = "t_user_payment_order")
public class PaymentOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    
    @NotBlank(message = "{payment.order_id}")
    @Column(name = "order_id", nullable = false)
    private String  orderId; // 订单号信息，用来支持多订单的方式,多个订单号使用","分割
    
    @NotBlank(message = "{payment.user_id}")
    @Column(name = "user_id", nullable = false, length = 50)
    private String  userId; // 用户UUID
    
    @NotBlank(message = "{payment.subject}")
    @Column(name = "subject", nullable = false, length = 255)
    private String  subject       = ""; // 提交主题
    
    @NotNull(message = "{payment.amount}")
    @Min(value = 0, message = "{payment.amount.size}")
    @Column(name = "amount")
    private Double  amount        = 0.0; // 订单总金额
    
    @Column(name = "final_amount")
    private Double  finalAmount   = 0.00; // 订单回传金额，支付平台回传
    
    @Column(name = "total_amount")
    private Double  totalAmount   = 0.00; // 订单总金额，包含混合支付帮豆金额数
    
    @Column(name = "status")
    private Integer status        = PaymentOrderType.NOTDONE.getValue(); // 支付状态
    
    @Column(name = "bz_amount")
    private Long  bzAmount        = null; // 超级帮钻抵现额度
    
    @Column(name = "bz_status")
    private Integer bzStatus      = PaymentOrderType.NOTDONE.getValue(); // 超级帮钻抵现状态
      
    @Column(name = "source")
    private Integer source        = 0; // 支付来源,用来却分是普通代码还是VIP支付，还是充值
    
    @Column(name = "third_pay_type")
    private Integer thirdPayType  = 0; // 支付平台类型
    
    @Column(name = "memo")
    private String  memo;
    
    @Column(name = "third_trade_no")
    private String  thirdTradeNo; // 第三方支付平台号，自定义
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date    createTime;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date    updateTime; // 订单支付时间
    
    @Column(name = "bank_type")
    private String  defaultBank; // 支付选择的银行类型
    
    @Column(name = "bank_bill_no")
    private String  bankBillNo; // 银行对账单
    
    @Column(name = "inviter_id")
    private String  inviterId;
    
    @Column(name = "free_fee_type")
    private Integer freeFeeType; // VIP的类型、1黄金 2白金
    
    @Column(name = "parent_order_id")
    private String  parentOrderId = ""; // 合并付款的批量支付ID,默认""，即单个订单;xxxxx表示多个订单
    
    @Column(name = "traffic_source")
    private String  trafficSource; // 订单流量来源
    
    @Column(name = "fee")
    private Double fee; //运费
    
    @Column(name = "tax")
    private Double tax; //税款
    
    @Column(name = "goods_amount")
    private Double goodsAmount; //货款金额

    @Column(name = "bg_code")
    private String bgCode; //商户海关备案号
    
    @Transient
    private String  sourceKey; // 校验Key
    @Transient
    private String  sign; // 订单校验签名
    @Transient
    private String  buyerId;
    @Transient
    private String  address;
    @Transient
    private String  mobileNum; // 充值的电话号码
    @Transient
    private String  chargeValue; // 手机充值的金额
    @Transient
    private Boolean isCombined    = false; // 是否合并付款 0：不是(默认)，1：是
    @Transient  
    private String  amountDetail  = ""; // 合并订单的详细价格信息,多个订单价格使用","分割
    @Transient
    private Integer isAllowAliPay = 0; // 是否可用支付宝支付 0：否(默认)，1：是
    @Transient
    private Long    bzUsable; // 可用帮钻额   
    @Transient
    private Long    bzBalance; // 帮钻余额
    @Transient
	private String code;	//微信用户授权后返回的code
    @Transient
    private String scene; // 用于支付场景
    @Transient
    private String usebz; // 是否使用帮钻 1 | 0
    @Transient
    private String payFrom; // 支付来源
    @Transient
    private String imei;
    @Transient
    private String superBzPayPwd; //超级帮钻支付密码
    @Transient
    private boolean isSuperBzPayPWD; //是否设置超级帮钻支付密码
    @Transient
    private boolean needSuperBzPayPwd = true; //是否需要验证超级帮钻支付密码
    @Transient
    private Integer crossPay = 0; //是否需要境外支付0为国内，1为境外（香港），2为境外（韩国）
    @Transient
    private String addressId; //用户中心地址Id
    @Transient
    private Date startTime;
    @Transient
    private Date endTime;

    //报关需要下单时使用
    @Transient
    private String idCardName; //下单者身份证对应姓名
    @Transient
    private String idCard; //下单者身份证号
    @Transient
    private String addressInfo; //下单者地址信息

    public PaymentOrder(){}
    
    public PaymentOrder(Payment payment){
    	this.merge(payment);
    }
    
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

    public String getParentOrderId() {
        return parentOrderId;
    }

    public void setParentOrderId(String parentOrderId) {
        this.parentOrderId = parentOrderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(Double finalAmount) {
        this.finalAmount = finalAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBzAmount() {
        return bzAmount;
    }

    public void setBzAmount(Long bzAmount) {
        this.bzAmount = bzAmount;
    }

    public Integer getBzStatus() {
        return bzStatus;
    }

    public void setBzStatus(Integer bzStatus) {
        this.bzStatus = bzStatus;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getThirdPayType() {
        return thirdPayType;
    }

    public void setThirdPayType(Integer thirdPayType) {
        this.thirdPayType = thirdPayType;
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

    public String getThirdTradeNo() {
        return thirdTradeNo;
    }

    public void setThirdTradeNo(String thirdTradeNo) {
        this.thirdTradeNo = thirdTradeNo;
    }

    public String getDefaultBank() {
        return defaultBank;
    }

    public void setDefaultBank(String bankType) {
        this.defaultBank = bankType;
    }

    public String getBankBillNo() {
        return bankBillNo;
    }

    public void setBankBillNo(String bankBillNo) {
        this.bankBillNo = bankBillNo;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public Integer getFreeFeeType() {
        return freeFeeType;
    }

    public void setFreeFeeType(Integer freeFeeType) {
        this.freeFeeType = freeFeeType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public Boolean getIsCombined() {
        return isCombined;
    }

    public void setIsCombined(Boolean isCombined) {
        this.isCombined = isCombined;
    }

    public String getAmountDetail() {
        return amountDetail;
    }

    public void setAmountDetail(String amountDetail) {
        this.amountDetail = amountDetail;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(String chargeValue) {
        this.chargeValue = chargeValue;
    }

    public String getTrafficSource() {
        return trafficSource;
    }

    public void setTrafficSource(String trafficSource) {
        this.trafficSource = trafficSource;
    }

    public Integer getIsAllowAliPay() {
        return isAllowAliPay;
    }

    public void setIsAllowAliPay(Integer isAllowAliPay) {
        this.isAllowAliPay = isAllowAliPay;
    }

    public Long getBzUsable() {
        return bzUsable;
    }

    public void setBzUsable(Long bzUsable) {
        this.bzUsable = bzUsable;
    }

    public Long getBzBalance() {
        return bzBalance;
    }

    public void setBzBalance(Long bzBalance) {
        this.bzBalance = bzBalance;
    }
    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}
	
	public String getUsebz() {
		return usebz;
	}

	public void setUsebz(String usebz) {
		this.usebz = usebz;
	}	

	public String getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(String payFrom) {
		this.payFrom = payFrom;
	}	

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

    public String getSuperBzPayPwd() {
        return superBzPayPwd;
    }

    public void setSuperBzPayPwd(String superBzPayPwd) {
        this.superBzPayPwd = superBzPayPwd;
    }

    public boolean isSuperBzPayPWD() {
        return isSuperBzPayPWD;
    }

    public void setSuperBzPayPWD(boolean isSuperBzPayPWD) {
        this.isSuperBzPayPWD = isSuperBzPayPWD;
    }

    public boolean isNeedSuperBzPayPwd() {
        return needSuperBzPayPwd;
    }

    public void setNeedSuperBzPayPwd(boolean needSuperBzPayPwd) {
        this.needSuperBzPayPwd = needSuperBzPayPwd;
    }

    public Integer getCrossPay() {
        return crossPay;
    }

    public void setCrossPay(Integer crossPay) {
        this.crossPay = crossPay;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    
    public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Double getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(Double goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

    public String getBgCode() {
        return bgCode;
    }

    public void setBgCode(String bgCode) {
        this.bgCode = bgCode;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    @Override
	public String toString() {
		return "PaymentOrder [id=" + id + ",orderId=" + orderId + ",userId="
				+ userId + ",subject=" + subject + ",amount=" + amount
				+ ",finalAmount=" + finalAmount + ",totalAmount=" + totalAmount
				+ ",status=" + status + ",source=" + source
				+ ",thirdPayType=" + thirdPayType + ",memo=" + memo
				+ ",thirdTradeNo=" + thirdTradeNo + ",createTime="
				+ createTime + ",updateTime=" + updateTime + ",sign=" + sign
				+ ",defaultBank=" + defaultBank + ",address=" + address
				+ ",sourceKey=" + sourceKey + ",buyerId=" + buyerId
				+ ",bzAmount=" + bzAmount + ",bzStatus=" + bzStatus + ",imei=" + imei
				+ ",inviterId=" + inviterId + ",freeFeeType=" + freeFeeType + ",payFrom=" + payFrom
				+ ",mobileNum=" + mobileNum + ",chargeValue=" + chargeValue
				+ ",code=" + code + ",scene=" + scene  +  ",usebz=" + usebz + ",isCombined=" + isCombined + ",amountDetail="
				+ amountDetail + ",parentOrderId=" + parentOrderId
				+ ",fee=" + fee + ",tax=" + tax + ",goodsAmount=" + goodsAmount
                + ",superBzPayPwd=" + superBzPayPwd  + ",isSuperBzPayPWD=" + isSuperBzPayPWD  +
                ",needSuperBzPayPwd=" + needSuperBzPayPwd +",crossPay=" + crossPay +",addressId=" + addressId
                +", idCardName=" + idCardName + ", idCard=" + idCard + ", addressInfo=" + addressInfo +"]";
	}

    public String toRequestString() {
		String str = "a=1";
		String[] a = this.toString().replace("PaymentOrder [","").replace("]","").replaceAll(" +","").split(",");
		for (int i = 0; i < a.length; i++) {			
			String[] b = a[i].split("=");
			if(b.length >1 && StringUtils.isNotBlank(b[1].trim()) && !b[1].equals("null")){
				str = str + "&" + b[0] + "=" + b[1];
			}
		}
		return str;
	}

    public void merge(Payment payment){
    	if(payment != null){
    		this.setOrderId(payment.getOrderId()); // 订单号
    		this.setUserId(payment.getUserId()); // 订单所属用户
    		this.setSubject(payment.getSubject()); // 提交主题
    		this.setAmount(payment.getAmount()); // 订单支付金额
            this.setTotalAmount(payment.getTotalAmount()); // 订单总金额
            this.setAmountDetail(payment.getAmount() + ""); // 订单总金额
    		this.setMemo(payment.getMemo()); // 备注
            this.setAddress(payment.getAddressId()); // 地址信息id
            this.setFee(payment.getFee()); //邮费
            this.setTax(payment.getTax()); //税费
            this.setGoodsAmount(payment.getGoodsAmount()); //货款金额
            if(null == this.crossPay || this.getCrossPay().equals(PayWayType.COMMON_WAY.getValue())){
                this.setCrossPay(payment.getCrossPay().getValue()); // 支付类型
            }
            this.setIdCardName(payment.getIdCardName());
            this.setIdCard(payment.getIdCard());
            this.setAddressInfo(payment.getAddressInfo());
    	}
    }

    public Payment toPayment(){
        Payment payment = new Payment();
        payment.setOrderId(this.getOrderId()); // 订单号
        payment.setUserId(this.getUserId()); // 订单所属用户
        payment.setSubject(this.getSubject()); // 提交主题
        payment.setAmount(this.getAmount()); // 订单支付金额
        payment.setTotalAmount(this.getTotalAmount()); // 订单总金额
        payment.setMemo(this.getMemo()); // 备注
        payment.setAddressId(this.getAddressId()); // 地址信息id
        payment.setFee(this.getFee()); //邮费
        payment.setTax(this.getTax()); //税费
        payment.setGoodsAmount(this.getGoodsAmount()); //货款金额
        payment.setCrossPay(PayWayType.getCrossPay(this.getCrossPay())); // 支付类型
        payment.setIdCardName(this.getIdCardName());
        payment.setIdCard(this.getIdCard());
        payment.setAddressInfo(this.getAddressInfo());
        return payment;
    }

}
