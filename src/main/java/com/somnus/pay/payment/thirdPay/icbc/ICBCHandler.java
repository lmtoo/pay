package com.somnus.pay.payment.thirdPay.icbc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.com.infosec.icbc.ReturnValue;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.icbc.bean.req.TranData;
import com.somnus.pay.payment.thirdPay.icbc.config.ICBCConfig;
import com.somnus.pay.payment.thirdPay.icbc.util.ICBCUtil;
import com.somnus.pay.payment.util.DateUtils;
import com.somnus.pay.payment.util.FuncUtils;
import com.somnus.pay.payment.util.HTMLUtil;
import com.somnus.pay.payment.util.PayAmountUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.payment.util.PageCommonUtil;

/**
 * @description: 工行支付渠道回调处理器
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 */
@Component
public class ICBCHandler extends AbstractICBCHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(ICBCHandler.class);

	public ICBCHandler() {
		super(PayChannel.ICBCPay);
	}

	@Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
		String interfaceName = ICBCConfig.INTERFACE_NAME;
        String interfaceVersion = ICBCConfig.INTERFACE_VERSION;
        String amount = PayAmountUtil.mul(String.valueOf(parameter.getData().getAmount()), "100");
        TranData tranData = new TranData();
        tranData.setInterfaceName(interfaceName);
        tranData.setInterfaceVersion(interfaceVersion);
        tranData.setMerID(ICBCConfig.MER_ID);
        tranData.setMerAcct(ICBCConfig.MER_ACCT);
        tranData.setAmount(amount);// 订单金额（分）
        tranData.setOrderid(parameter.getData().getOrderId());// 订单号
        tranData.setOrderDate(DateUtils.Date2String(new Date(), "yyyyMMddHHmmss"));// 交易日期，
        tranData.setMerReference(createMerReference());// 商户网站域名  "*.stage.com"
        HttpServletRequest request = WebUtil.getRequest();
        String user_ip = FuncUtils.getIpAddr(request);
        tranData.setMerCustomIp(user_ip);
        tranData.setGoodsName(parameter.getData().getSubject());
        tranData.setInstallmentTimes(ICBCConfig.INSTALLMENT_TIMES_1);// 分期付款期数，1、3、6、9、12、18、24，1代表全额付款
        String merURL = PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + ICBCConfig.NOTIFY_URL; //主动通知URL
        tranData.setMerURL(merURL);// 返回商户URL
        tranData.setCarriageAmt(amount);
        tranData.setVerifyJoinFlag(ICBCConfig.VERIFY_JOIN_FLAG);
        tranData.setResultType(ICBCConfig.RESULT_TYPE);// 结果发送类型，0：无论成功或失败，均发送通知消息，1：只通知成功交易
        tranData.setCreditType(ICBCConfig.CREDIT_TYPE);// 支持银行卡种类，0：借记卡，1：信用卡，2：借记卡和信用卡
        StringBuffer s = new StringBuffer();
        s.append("<?xml version='1.0' encoding='GBK' standalone='no'?>");
        s.append(tranData.toXML());
        String data = s.toString();
        HashMap<String, Object> reqMap = new HashMap<String, Object>(5);
        reqMap.put("interfaceName", interfaceName);
        reqMap.put("interfaceVersion", interfaceVersion);
        try {
            reqMap.put("tranData", new String(ReturnValue.base64enc(data.getBytes(ICBCConfig.CHAR_SET)),ICBCConfig.CHAR_SET));
            reqMap.put("merSignMsg", sign(data, ICBCConfig.KEY_PATH, ICBCConfig.KEY_PASS));
            reqMap.put("merCert", getEncCert(ICBCConfig.CERT_PATH));
        } catch (Exception e) {
            LOGGER.warn("构建工行支付表单失败", e);
        }
        return HTMLUtil.createHtml(ICBCConfig.PAY_URL, reqMap, HTMLUtil.ENCODING,HTMLUtil.REQUEST_METHOD_POST);
	}

	@Override
	public String getOrderId(Map<String, String> data) {
		return ICBCUtil.obtainOrderId(data);
	}
	
}
