package com.somnus.pay.payment.enums;

/**
 * 订单状态枚举
 * @author jianyuanyang
 *
 */
public enum PaymentOrderType {

    NOTDONE(0,"未支付"),
    SCCUESS(1,"支付成功"),
    FAIL(2,"支付失败"),
    DONE(3,"已完成"),
    PAID_ERROR(4,"已付款"),
    BZ_DEDUCTION_ERROR(5,"前后两次帮钻抵现额不一致"),
    SUPER_BZ_PAY_PWD_ERROR(6,"超级帮钻支付密码错误"),
    ERROR(7,"支付错误"),
    BZ_PAY_SCCUESS(8,"帮钻支付成功");

    private Integer value;
    private String message;

    private PaymentOrderType(Integer value,String message) {
        this.value = value;
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public static PaymentOrderType valueOf(Integer value){
    	PaymentOrderType type = null;
    	if(value != null){
			for (PaymentOrderType t : PaymentOrderType.values()) {
				if(t.getValue().equals(value)){
					type = t;
					break;
				}
			}
    	}
    	return type;
    }

}
