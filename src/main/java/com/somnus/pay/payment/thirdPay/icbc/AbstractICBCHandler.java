package com.somnus.pay.payment.thirdPay.icbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.somnus.pay.payment.util.PageCommonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.infosec.icbc.ReturnValue;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandler;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.icbc.bean.res.NotifyData;
import com.somnus.pay.payment.thirdPay.icbc.config.ICBCConfig;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.PayAmountUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.utils.Assert;

/**
 * @description: 工行支付渠道回调处理器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: qingshu
 * @version: 1.0
 * @createdate: 2015-12-15
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-15   qingshu       1.0            初始化
 */
public abstract class AbstractICBCHandler extends PaymentChannelHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractICBCHandler.class);
	protected byte[] icbcPublicCert;

	public AbstractICBCHandler(PayChannel channel) {
		super(channel);
		FileInputStream in1 = null;
		try {
			in1 = new FileInputStream(ICBCConfig.CERT_RES_SIGN_PATH);// 读取密钥
			icbcPublicCert = new byte[in1.available()];
			in1.read(icbcPublicCert);
		} catch (Exception e) {
			LOGGER.warn("导入工行安全证书失败", e);
		} finally {
			IOUtils.closeQuietly(in1);
		}
	}

	@Override
	public String handleNotify(Map<String, String> data) {
		checkSign(data);
		return PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + (PayChannel.CMBPay.equals(this.getRequestParameter().getChannel())? ICBCConfig.WEB_URL:ICBCConfig.WAP_WEB_URL);
	}

	@Override
	public String handleReturn(Map<String, String> data) {
		checkSign(data);
		return getOrderId(data);
	}

	/**
	 * 从请求参数中解析订单号
	 * @param data 请求参数
	 * @return 订单号
	 */
	protected abstract String getOrderId(Map<String, String> data);
	
	/**
	 * 检查通知参数中的签名是否正确
	 * @param data 通知参数
	 * @return 签名是否正确
	 */
	protected void checkSign(Map<String, String> data) {
		Assert.isTrue(MapUtils.isNotEmpty(data), PayExceptionCode.PAYMENT_PARAM_ERROR);
		String notifyData = data.get("notifyData");
		String signMsg = data.get("signMsg");
		Assert.isTrue((StringUtils.isNotBlank(notifyData) && StringUtils.isNotBlank(signMsg)), PayExceptionCode.PAYMENT_PARAM_ERROR);
		try {
			byte[] byteSrc = ReturnValue.base64dec(notifyData.getBytes());// 通知信息BASE64解码
			byte[] decSign = ReturnValue.base64dec(signMsg.getBytes());// 签名信息BASE64解码
			Assert.isTrue((ReturnValue.verifySign(byteSrc, byteSrc.length, icbcPublicCert, decSign) == 0), PayExceptionCode.PAYMENT_PARAM_ERROR);
		} catch (Exception e) {
			LOGGER.warn("工行回调通知签名校验错误", e);
			throw new PayException(PayExceptionCode.SIGN_VALIDATE_FAILED);
		}
	}

	@Override
	public String getFailedResponse(RequestParameter<?, ?> parameter) {
		return ICBCConfig.NOTIFY_NOTE_FAIL;
	}

	@Override
	public PaymentResult[] convert(Map<String, String> data){
		String merVAR = data.get("merVAR");
		String notifyData = data.get("notifyData");
		NotifyData notifyData1 = new NotifyData();
		if (StringUtils.isNotBlank(notifyData)) {
			try {
				String src = new String(ReturnValue.base64dec(notifyData.getBytes(ICBCConfig.CHAR_SET)), ICBCConfig.CHAR_SET);
				notifyData1 = NotifyData.fromXML(src);
			} catch (Exception e) {
				LOGGER.warn("工行回调通知参数格式转换错误",e);
			}
		}
		// 是否是合并付款
		int isCombined = 0;
		try {
			isCombined = Integer.valueOf(merVAR);
		} catch (Exception e) {
			isCombined = 0;
		}
		PaymentResult paymentResult = new PaymentResult();
		paymentResult.setIsCombined(isCombined);
		paymentResult.setOrderId(notifyData1.getOrderid());
		paymentResult.setTradeStatus(notifyData1.getTranStat());
		paymentResult.setThirdTradeNo(notifyData1.getTranSerialNo());
		paymentResult.setPrice(Double.valueOf(PayAmountUtil.divide(String.valueOf(notifyData1.getAmount()), "100")));
		paymentResult.setPayInfo(notifyData1.getComment());
		paymentResult.setStatus(ICBCConfig.SUCCESS.equals(notifyData1.getTranStat()) ? PaymentOrderType.SCCUESS.getValue() : PaymentOrderType.FAIL.getValue());
		return new PaymentResult[]{paymentResult};
	}
	
	protected String getEncCert(String certPath) throws Exception {
        InputStream in1 = null;
        byte[] cert = null;
        try {
            in1 = new FileInputStream(certPath);
            cert = new byte[in1.available()];
            in1.read(cert);
        } catch (Exception e) {
            LOGGER.warn("读取工行安全证书失败", e);
        } finally {
            IOUtils.closeQuietly(in1);
        }
        return new String(ReturnValue.base64enc(cert),ICBCConfig.CHAR_SET);
    }
	
	protected String sign(String srcStr, String keyPath, String privatePasswd) throws Exception {
        FileInputStream fileKey=null;
        try{
            byte[] byteSrc = srcStr.getBytes(ICBCConfig.CHAR_SET);
            fileKey  = new FileInputStream(keyPath);
            byte[] PriK = new byte[fileKey.available()];
            fileKey.read(PriK);
            fileKey.close();
            char[] keyPass = privatePasswd.toCharArray();
            byte[] sign = ReturnValue.sign(byteSrc,byteSrc.length,PriK,keyPass);
            byte[] tmpSign = ReturnValue.base64enc(sign);
            return new String(tmpSign,ICBCConfig.CHAR_SET);
        }catch(IOException ex){
            LOGGER.warn("计算工行签名失败", ex);
            throw new PayException(PayExceptionCode.SIGN_ERROR, ex);
        }finally {
            if(fileKey!=null){
            	IOUtils.closeQuietly(fileKey);
            }
        }
    }
	
	/**
     * 通过配置区分我们提交的reference
     * @return
     */
	protected String createMerReference() {
        if(StringUtils.isNotBlank(Constants.PAY_BACK_DOMAIN)){
            if(Constants.PAY_BACK_DOMAIN.contains("stage")){ //online环境
                return "*.stage.com";
            }else if(Constants.PAY_BACK_DOMAIN.contains("prod")) { //prod环境
                return "*.prod.com";
            }
        }
        return "*.b5m.com";
    }
	
}
