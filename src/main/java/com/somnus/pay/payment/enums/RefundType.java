package com.somnus.pay.payment.enums;

/**
 * 退款状态
 * @author mike
 *
 */
public enum RefundType {

    NOTDONE(0), SCCUESS(1), FAIL(-1), WAIT(2), ERROR(-2);

    private Integer value;

    private RefundType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

	public static String getMeassage(Integer value) {
		String message = "退款状态不存在" ;
		if(value == null){
			message = "退款状态为空" ;
		}
		if(value == NOTDONE.value){
			message = "退款记录已保存" ;
		}else if(value == SCCUESS.value){
			message = "退款申请已受理或者已经退款成功" ;
		}else if(value == FAIL.value){
			message = " 第三方退款操作失败" ;
		}else if(value == WAIT.value){
			message = " 第三方退款申请已受理 请等待" ;
		}else if(value == ERROR.value){
			message = " 第三方退款失败，该用户的卡号被封或者失效" ;
		}		
		return message;
	}
}
