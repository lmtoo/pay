package com.somnus.pay.payment.enums;

import com.somnus.pay.payment.thirdPay.ccb.config.CCBConfig;
import com.somnus.pay.payment.thirdPay.shouxin.config.ShouXinConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 支付渠道枚举
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-11
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-11       丹青生                               1.0            初始化
 
 */
public enum PayChannel {
	AliPay(1, "支付宝"), KuanqianPay(2, "快钱"), TencentPay(3, "财付通"), BocomPay(4, "交行(网银)"), AliWapPay(5, "支付宝(WAP)"), UnionWapPay(6, "银联(WAP)"),
	LianLianPay(7, "连连"), LianLianWapPay(8, "连连(WAP)"), SuperBeanPay(9, "超级帮钻"), WxPay(10, "微信"),
	WxWapPay(11, "微信(WAP)"), WxAppPay(12, "微信(APP)"), UmpPay(13, "U联"), UmpWapPay(14, "U联(WAP)"), UnionPay(15, "银联"),
	WxBhpAppPay(16, "微信(帮韩品)"), AliBhpAppPay(17, "支付宝(帮韩品)"), BaiDuWalletPay(18, "百度钱包"), BaiDuWalletWapPay(19, "百度钱包(WAP)"),
	BaiDuWalletB5mAppPay(20, "百度钱包(帮5买)"), ShouXinPay(21, "首信易"), CCBPay(22, "建行(网银)"), ShouXinWapPay(23, "首信易(WAP)"), ICBCPay(24, "工行(网银)"),
	ICBCWapPay(25, "工行(WAP网银)"), AliHKWapPay(26, "支付宝国际(香港)"), CMBPay(27, "招行(网银)"), CMBWapPay(28, "招行(WAP网银)"), BocomWapPay(29, "交行(WAP网银)"),
	CCBWapPay(30, "建行(WAP网银)"), ShouXinKoreaPay(31, "首信易(韩国)"), AliKoreaWapPay(32, "支付宝国际(韩国)"), AliBhp2AppPay(33, "支付宝(帮韩品-2)"),
	AliKorea2WapPay(34, "支付宝国际(韩国-2)"), ABCPay(35, "农行(网银)"), ABCWapPay(36, "农行(WAP网银)"),
	AliB5mAppPay(37, "支付宝(帮我买)"), AliB5mWapPay(38, "支付宝WAP(帮我买)"), AliGJBhpAppPay(39, "支付宝国际香港(帮海贝)"),

	/**
	 * 宁波仓报关虚拟渠道(暂时不分1/2待详细)
	 */
	V_ShouXinPay_BG_NingBo(101, "首信易-宁波海关报关", ShouXinPay),
	V_ShouXinWapPay_BG_NingBo(102, "首信易(WAP)-宁波海关报关",ShouXinWapPay),
	V_ShouXinKoreaPay_BG_NingBo(103, "首信易(韩国)-宁波海关报关",ShouXinKoreaPay),
	V_AliKoreaWapPay_BG_NingBo(104, "支付宝国际(韩国)-宁波海关报关",AliKoreaWapPay),
	V_AliKorea2WapPay_BG_NingBo(105, "支付宝国际(韩国-2)-宁波海关报关",AliKorea2WapPay),
	V_AliHKWapPay_BG_NingBo(106, "支付宝国际(香港)-宁波海关报关",AliHKWapPay),

	/**
	 * 广州海关报关虚拟渠道(首信易需要报2次)
	 */
	V_ShouXinPay_BG_GuangZhou(107, "首信易-广州海关报关", ShouXinPay),
	V_ShouXinWapPay_BG_GuangZhou(108, "首信易(WAP)-广州海关报关",ShouXinWapPay),
	V_ShouXinKoreaPay_BG_GuangZhou(109, "首信易(韩国)-广州海关报关",ShouXinKoreaPay),

	/**
	 * 广州-白云仓报关虚拟渠道
	 */
	V_ShouXinPay_BG_BaiYun(110, "首信易-广州白云仓海关报关", ShouXinPay),
	V_ShouXinWapPay_BG_BaiYun(111, "首信易(WAP)-广州白云仓海关报关",ShouXinWapPay),
	V_ShouXinKoreaPay_BG_BaiYun(112, "首信易(韩国)-广州白云仓海关报关",ShouXinKoreaPay),
	V_AliKoreaWapPay_BG_BaiYun(113, "支付宝国际(韩国)-广州白云仓海关报关",AliKoreaWapPay),
	V_AliKorea2WapPay_BG_BaiYun(114, "支付宝国际(韩国-2)-广州白云仓海关报关",AliKorea2WapPay),
	V_AliHKWapPay_BG_BaiYun(115, "支付宝国际(香港)-广州白云仓海关报关",AliHKWapPay),

	/**
	 * 广州-南沙仓报关虚拟渠道
	 */
	V_ShouXinPay_BG_NanSha(116, "首信易-广州南沙海关报关", ShouXinPay),
	V_ShouXinWapPay_BG_NanSha(117, "首信易(WAP)-广州南沙海关报关",ShouXinWapPay),
	V_ShouXinKoreaPay_BG_NanSha(118, "首信易(韩国)-广州南沙海关报关",ShouXinKoreaPay),
	V_AliKoreaWapPay_BG_NanSha(119, "支付宝国际(韩国)-广州南沙海关报关",AliKoreaWapPay),
	V_AliKorea2WapPay_BG_NanSha(120, "支付宝国际(韩国-2)-广州南沙海关报关",AliKorea2WapPay),
	V_AliHKWapPay_BG_NanSha(121, "支付宝国际(香港)-广州南沙海关报关",AliHKWapPay),

	/**
	 * 帮我采支付支付宝国际香港wap渠道
	 */
	V_AliHKWapPay_B5C(122, "支付宝国际(香港)-帮我采支付",AliHKWapPay),

	V_AliGJBhpAppPay_BG_NanSha(123, "支付宝国际(香港)app支付-广州南沙海关报关",AliGJBhpAppPay),
	V_AliGJBhpAppPay_BG_BaiYun(124, "支付宝国际(香港)app支付-广州白云仓海关报关",AliGJBhpAppPay),
	V_AliGJBhpAppPay_BG_NingBo(125, "支付宝国际(香港)app支付-宁波海关报关",AliGJBhpAppPay);

	private Integer value;
	private String desc;
	private PayChannel realPayChannel;

	private PayChannel(Integer value, String desc) {
		this.value = value;
		this.desc = desc;
		this.realPayChannel=this;
	}

	private PayChannel(Integer value, String desc,PayChannel realPayChannel) {
		this.value = value;
		this.desc = desc;
		this.realPayChannel = realPayChannel;
	}
	
	public Integer getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}

	public static List<PayChannel> getAll(){
		List<PayChannel> list = new ArrayList<PayChannel>();
		for (PayChannel type : PayChannel.values()) {
			list.add(type);
		}
		return list;
	}

	public static PayChannel descOf(String desc){
		PayChannel payType = null;
		if (desc != null){
			for (PayChannel type : PayChannel.values()) {
				if (type.desc.equalsIgnoreCase(desc))
					payType = type;
			}
		}
		return payType;
	}

	public static PayChannel nameOf(String name){
		PayChannel payType = null;
		if (name != null){
			for (PayChannel type : PayChannel.values()) {
				if (type.name().equalsIgnoreCase(name))
					payType = type;
			}
		}
		return payType;
	}
	
	public static PayChannel getPayType(Integer value) {
		PayChannel payType = null;
		if (value == null)
			payType = PayChannel.AliPay;
		for (PayChannel pt : PayChannel.values()) {
			if (pt.getValue().equals(value))
				payType = pt;
		}
		return payType;
	}

	public static PayChannel valueOf(Integer value) {
		if (value != null) {
			for (PayChannel payType : PayChannel.values()) {
				if (payType.getValue().equals(value))
					return payType;
			}
		}
		return null;
	}

	/**
	 * 判断是否手机端支付并且是使用sdk方式的支付
	 * 
	 * @param payType
	 * @return
	 */
	public static boolean isMobileSDKPayWay(PayChannel payType) {
		payType = payType.getRealPayChannel();
		if (PayChannel.WxWapPay == payType || PayChannel.WxAppPay == payType
				|| PayChannel.WxBhpAppPay == payType
				|| PayChannel.AliBhpAppPay == payType) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获得支付类型后台通知地址
	 */
	public static String getNotifyAddress(PayChannel payType) {
		payType = payType.getRealPayChannel();
		String res = "";
		if (payType.name().equals(PayChannel.CCBPay.name())) {
			res = CCBConfig.WEB_NOTIFY_ADDRESS;
		} else if (payType.name().equals(PayChannel.ShouXinPay.name())) {
			res = ShouXinConfig.WEB_BACKURL;
		} else if (payType.name().equals(PayChannel.ShouXinWapPay.name())) {
			res = ShouXinConfig.WAP_BACKURL;
		} else if (payType.name().equals(PayChannel.ShouXinKoreaPay.name())) {
			res = ShouXinConfig.WEB_KOREA_BACKURL;
		}
		return res;
	}

	@Override
	public String toString() {
		return this.name() + "( " + this.value + " , " + this.desc + " )";
	}
	
	public PayChannel getRealPayChannel(){
		return this.realPayChannel == null ? this : this.realPayChannel;
	}

}