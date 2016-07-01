package com.somnus.pay.payment.enums;

/**
 * 支付系统提供请求能源
 * 0国内支付
 * 1境外支付（香港）
 * 2境外支付（韩国）
 * 3国内支付（不允许使用帮钻）
 */
public enum PaySource {

    HAIWAI(23,"b5m_haiwai"),//暂时不用
    HAIWAI_HDCZ(222,"haiwai_hdcz"),//暂时不用
    SEARCH_DAIGOU(22,"search_daigou"),//暂时不用
    UCENTER_MAIN(1,"用户中心帮钻充值"),
    DAIGOU_NEW(220,"b5m_online_daigou_new"),//暂时不用
    WORLD_CUP_PHP(221,"b5m_online_world_cup_php"),//暂时不用
    ORDER_PAY(223,"PC端支付"),
    MOBILE_PAY(224,"手机端支付"),
    FREE_POST(225,"9或99免邮"),//暂时不用
    WX_VIP(226,"微信购买vip套餐"),//暂时不用
    FREE_FEE(228,"VIP购买_online_free_fee"), //暂时不用
    BANGZUAN_WAP(229,"app端帮钻充值"),
    BANGZUAN_DUIHUAN(230,"1元兑换"),
    B5C_ORDER(231,"帮我采业务"),
    B5C_WAP_ORDER(232,"帮我采WAP业务"),
    DUIHUAN_WAP_ORDER(233,"1元兑换WAP业务"),
    BHB_WAP_ORDER(234,"帮海贝APP业务");

    private Integer value;
    private String desc;

    PaySource(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static PaySource valueOf(Integer value) {
        if (value != null){
            for (PaySource paymentSource : PaySource.values()) {
                if (paymentSource.getValue().equals(value))
                    return paymentSource;
            }
        }
        return null;
    }


    /**
     * 判断是否是帮我采业务
     * @param value
     * @return
     */
    public static boolean isB5CBusiness(Integer value) {
        PaySource paySource= valueOf(value);
        if(PaySource.B5C_ORDER == paySource || PaySource.B5C_WAP_ORDER == paySource){
            return true;
        }
        return false;
    }

    /**
     * 判断是否是帮海贝业务
     * @param source
     * @return
     */
    public static boolean isBHBBusiness(Integer source) {
        PaySource paySource= valueOf(source);
        if(PaySource.BHB_WAP_ORDER == paySource){
            return true;
        }
        return false;
    }
}
