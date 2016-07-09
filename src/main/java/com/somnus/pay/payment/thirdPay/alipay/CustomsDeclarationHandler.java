package com.somnus.pay.payment.thirdPay.alipay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import com.somnus.pay.payment.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.enums.CustomsDeclarationStatus;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.BgConfig;
import com.somnus.pay.payment.pojo.CustomsDeclaration;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.service.CustomsDeclarationService;
import com.somnus.pay.payment.thirdPay.alipay.config.AlipayConfig;
import com.somnus.pay.payment.thirdPay.alipay.sign.MD5;
import com.somnus.pay.payment.thirdPay.alipay.sign.RSA;
import com.somnus.pay.payment.thirdPay.alipay.util.AlipayCore;
import com.somnus.pay.utils.Assert;

/**
 * @description: 报关业务处理
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/25
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/25       方东白          1.0        报关业务处理
 */
public class CustomsDeclarationHandler extends  AbstractAliHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomsDeclarationHandler.class);

    @Autowired
    CustomsDeclarationService customsDeclarationService;
    private Map<PayChannel, List<BgConfig>> bgConfigMap = new HashMap<PayChannel, List<BgConfig>>();
    
    public CustomsDeclarationHandler(String partner, String signKey, PayChannel...channel){
        super(partner, signKey, channel);
        initBgConfig();
    }

    /**
     * 初始化支付宝国际的报关配置
     */
    private void initBgConfig() {
        List<BgConfig> bgConfigs = new ArrayList<BgConfig>();
        BgConfig gzConfig = new BgConfig();
        //广州白云仓报关配置
        gzConfig.setCode("IE150824112547");
        gzConfig.setName("上海载和网络科技有限公司");
        gzConfig.setPlace("GUANGZHOU");
        gzConfig.setService(AlipayConfig.CUSTOMS_DECLARATION_SERVICE_NAME);
        bgConfigs.add(gzConfig);
        bgConfigMap.put(PayChannel.V_AliKoreaWapPay_BG_BaiYun, bgConfigs);
        bgConfigMap.put(PayChannel.V_AliKorea2WapPay_BG_BaiYun, bgConfigs);
        bgConfigMap.put(PayChannel.V_AliHKWapPay_BG_BaiYun, bgConfigs);

        //广州南沙仓报关配置
        bgConfigs = new ArrayList<BgConfig>();
        BgConfig config = new BgConfig();
        config.setCode("1000001093");
        config.setName("上海载和网络科技有限公司");
        config.setPlace("NANSHAGJ");
        config.setService(AlipayConfig.CUSTOMS_DECLARATION_SERVICE_NAME);
        bgConfigs.add(gzConfig);//南沙仓要报2个
        bgConfigs.add(config);
        bgConfigMap.put(PayChannel.V_AliKoreaWapPay_BG_NanSha, bgConfigs);
        bgConfigMap.put(PayChannel.V_AliKorea2WapPay_BG_NanSha, bgConfigs);
        bgConfigMap.put(PayChannel.V_AliHKWapPay_BG_NanSha, bgConfigs);

        //宁波仓1/2报关配置
        bgConfigs = new ArrayList<BgConfig>();
        config = new BgConfig();
        config.setCode("18551");
        config.setName("帮5买");
        config.setPlace("NINGBO");
        config.setService(AlipayConfig.CUSTOMS_DECLARATION_SERVICE_NAME);
        bgConfigs.add(config);
        bgConfigMap.put(PayChannel.V_AliKoreaWapPay_BG_NingBo, bgConfigs);
        bgConfigMap.put(PayChannel.V_AliKorea2WapPay_BG_NingBo, bgConfigs);
        bgConfigMap.put(PayChannel.V_AliHKWapPay_BG_NingBo, bgConfigs);
    }

    @Override
	protected List<BgConfig> getBgConfig(PaymentOrder paymentOrder) {
        PayChannel channel = this.getRequestParameter().getChannel();
        LOGGER.info("获取订单[{}]可用的报关配置:[{}]", paymentOrder.getOrderId(), channel);
        return bgConfigMap.get(channel);
	}

	@Override
	protected void doBg(PaymentResult paymentResult, PaymentOrder paymentOrder, BgConfig config, CustomsDeclaration customsDeclaration) {
        try{
            TreeMap<String,String> requestParams = new TreeMap<String,String>();
            requestParams.put("out_request_no", getOutRequestNo()); //报关流水号
            requestParams.put("trade_no", paymentResult.getThirdTradeNo()); //支付宝交易号
            requestParams.put("merchant_customs_code", config.getCode()); //商户海关备编号
            requestParams.put("merchant_customs_name", config.getName()); //商户海关备名称
            requestParams.put("amount", paymentResult.getPrice().toString()); //报关金额
            requestParams.put("customs_place", config.getPlace());

            requestParams.put("service", config.getService()); //报关服务名
            requestParams.put("partner",this.partner); //合作者身份ID
            requestParams.put("_input_charset",AlipayConfig.CROSS_PAY_INPUT_CHARSET); //编码格式 utf-8
            requestParams.put("sign",getSign(requestParams)); //签名
            requestParams.put("sign_type",AlipayConfig.CROSS_PAY_SIGN_TYPE); //签名方式

            // 获取支付宝报关http请求超时时长配置
            Integer timeOut = configService.getIntegerValue(Constants.HTTP_TIMED_OUT_ALIPAY_BG);
            LOGGER.info("支付宝国际报关配置中读取http请求超时时长:[{}]", timeOut);
            timeOut = (timeOut == null || (timeOut >120000 || timeOut<=0)) ? 60000 : timeOut;
            LOGGER.info("支付宝国际报关重置http请求超时时长:[{}]", timeOut);
            HttpClientUtils.setCurrentTimeOut(timeOut); //设置本次请求超时
            LOGGER.info("支付宝国际报关相关请求参数requestParams:[{}]", requestParams);
            String resTxt = HttpClientUtils.get(AlipayConfig.CROSS_PAY_REQUEST_WAP, requestParams);//正式地址
            resTxt = StringUtils.trimToEmpty(resTxt).replaceAll("\r\n", "");
//            String resTxt = HttpClientUtils.get("http://openapi.alipaydev.com/gateway.do", requestParams);//测试地址
            customsDeclaration.setResult(resTxt);
            customsDeclarationService.saveOrUpdate(customsDeclaration);
            LOGGER.info("支付宝国际报关返回结果:[{}]",resTxt);

            //解析xml节点
            Document document = DocumentHelper.parseText(resTxt);
            String httpStatus = document.selectSingleNode("//alipay/is_success").getText(); //http请求状态
            customsDeclaration.setStatus(CustomsDeclarationStatus.FAILURE); //表示报关失败
            if(httpStatus.equals("T")){
                String resultCode = document.selectSingleNode("//alipay/response/alipay/result_code").getText(); //业务请求状态
                //验证签名
                checkSign(document);
                if(resultCode.equals("SUCCESS")){
                    customsDeclaration.setStatus(CustomsDeclarationStatus.SUCCEED); //表示报关成功
                }

            }
            LOGGER.info("订单[{}]报关{}",customsDeclaration.getOrderId(), customsDeclaration.getStatus() == CustomsDeclarationStatus.SUCCEED ? "成功" : "失败");
            customsDeclarationService.saveOrUpdate(customsDeclaration);
        } catch(B5mException e){
            throw e;
        } catch(Exception e){
            throw new PayException(PayExceptionCode.CUSTOMS_DECLARATION_ERROR, e);
        }
    }

    /**
     * 验签
     * @param document
     */
    protected void checkSign(Document document){
        TreeMap<String,String> requestParams = new TreeMap<String,String>();

        String httpStatus = document.selectSingleNode("//alipay/is_success").getText(); //http请求状态

        if(httpStatus.equals("F")){
            requestParams.put("error",document.selectSingleNode("//alipay/error").getText());
        }else{
            List<Element> list = document.selectNodes("//alipay/response/alipay");
            Element element = list.get(0);
            List<Element> childElement = element.elements();
            for(Element ele : childElement){
                requestParams.put(ele.getName(), ele.getTextTrim());
            }
        }

        String sign = document.selectSingleNode("//alipay/sign").getText(); //返回签名
        String signType = document.selectSingleNode("//alipay/sign_type").getText(); //签名类型

        //获取待签名字符串
        String preSignStr = AlipayCore.createLinkString(requestParams);

        boolean isSign = false;
        if(AlipayConfig.sign_type.equals(signType)){
            isSign = MD5.verify(preSignStr, sign, signKey, AlipayConfig.input_charset);
        }else if(AlipayConfig.SIGN_TYPE_RSA.equals(signType)){
            isSign = RSA.verify(preSignStr, sign, signKey, AlipayConfig.input_charset);
        }
        Assert.isTrue(isSign, PayExceptionCode.SIGN_ERROR);
    }

    /**
     * 获取随机的报关流水号
     * @return
     */
    public String getOutRequestNo(){
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String formatDate = sf.format(d);
        String s = formatDate + RandomStringUtils.randomNumeric(8);
        return s;
    }

}
