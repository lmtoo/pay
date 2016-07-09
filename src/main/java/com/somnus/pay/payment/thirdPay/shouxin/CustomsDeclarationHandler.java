package com.somnus.pay.payment.thirdPay.shouxin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
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
import com.somnus.pay.payment.service.PaymentOrderService;

/**
 * @description: 首信易报关业务处理
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/25
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/25       方东白          1.0        报关业务处理
 */
public class CustomsDeclarationHandler extends  AbstractShouXinHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomsDeclarationHandler.class);

    @Autowired
    CustomsDeclarationService customsDeclarationService;

    @Autowired
    protected IConfigService configService;

    private Map<PayChannel, List<BgConfig>> bgConfigMap = new HashMap<PayChannel, List<BgConfig>>();
    
    @Autowired
	PaymentOrderService paymentOrderService;

    public CustomsDeclarationHandler(String partner, String signKey, PayChannel...channel){
        super(partner, signKey, channel);
        initBgConfig();
    }

    /**
     * 初始化首信易的报关配置
     */
    private void initBgConfig() {
        List<BgConfig> bgConfigs = new ArrayList<BgConfig>();
        //广州海关仓报关配置
        BgConfig gzConfig = new BgConfig();
        gzConfig.setCode("2");
        bgConfigs.add(gzConfig);
        bgConfigMap.put(PayChannel.V_ShouXinPay_BG_GuangZhou, bgConfigs);
        bgConfigMap.put(PayChannel.V_ShouXinWapPay_BG_GuangZhou, bgConfigs);
        bgConfigMap.put(PayChannel.V_ShouXinKoreaPay_BG_GuangZhou, bgConfigs);


        //广州白云仓报关配置
        bgConfigs = new ArrayList<BgConfig>();
        BgConfig config = new BgConfig();
        config.setCode("30");
        bgConfigs.add(gzConfig);
        bgConfigs.add(config);
        bgConfigMap.put(PayChannel.V_ShouXinPay_BG_BaiYun, bgConfigs);
        bgConfigMap.put(PayChannel.V_ShouXinWapPay_BG_BaiYun, bgConfigs);
        bgConfigMap.put(PayChannel.V_ShouXinKoreaPay_BG_BaiYun, bgConfigs);

        //广州南沙仓报关配置
        bgConfigs = new ArrayList<BgConfig>();
        config = new BgConfig();
        config.setCode("31");
        bgConfigs.add(gzConfig);
        bgConfigs.add(config);
        bgConfigMap.put(PayChannel.V_ShouXinPay_BG_NanSha, bgConfigs);
        bgConfigMap.put(PayChannel.V_ShouXinWapPay_BG_NanSha, bgConfigs);
        bgConfigMap.put(PayChannel.V_ShouXinKoreaPay_BG_NanSha, bgConfigs);

        //宁波仓1/2报关配置
        bgConfigs = new ArrayList<BgConfig>();
        config = new BgConfig();
        config.setCode("4");
        bgConfigs.add(config);
        bgConfigMap.put(PayChannel.V_ShouXinPay_BG_NingBo, bgConfigs);
        bgConfigMap.put(PayChannel.V_ShouXinWapPay_BG_NingBo, bgConfigs);
        bgConfigMap.put(PayChannel.V_ShouXinKoreaPay_BG_NingBo, bgConfigs);
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
            Map<String,String> requestParams = new HashMap<String,String>();
            requestParams.put("orderNo", paymentResult.getThirdTradeNo()); //首信易平台订单号
            requestParams.put("subOrderNo", ""); //子订单号，没有传空
            requestParams.put("eStoreOrderNO",paymentOrder.getOrderId()); //b5m商城订单号，也即支付平台的支付号
            requestParams.put("payDate", paymentOrder.getCreateTime().toString()); //支付日期
            requestParams.put("payAmount", paymentOrder.getAmount().toString()); //支付金额
            requestParams.put("payGoodsAmount", paymentOrder.getGoodsAmount().toString()); //支付货款
            requestParams.put("payTaxAmount", paymentOrder.getTax().toString()); //税款
            requestParams.put("payFeeAmount", paymentOrder.getFee().toString()); //运费
            requestParams.put("v_export_port", config.getCode()); //海关编号
            // 获取易支付报关http请求超时时长配置
            Integer timeOut = configService.getIntegerValue(Constants.HTTP_TIMED_OUT_SHOUXIN_BG);
            LOGGER.info("首信易报关配置中读取http请求超时时长：[{}]", timeOut);
            timeOut = (timeOut == null || (timeOut >120000 || timeOut<=0)) ? 60000 : timeOut;
            LOGGER.info("首信易报关重置http请求超时时长：[{}]", timeOut);
            HttpClientUtils.setCurrentTimeOut(timeOut); //设置本次请求超时
            LOGGER.info("首信易报关相关请求参数requestParams:[{}]", requestParams);
            String resTxt = HttpClientUtils.get("http://210.73.90.235:8090/jiaofei/CrossTrade_OrderSend.jsp", requestParams);
            resTxt = StringUtils.trimToEmpty(resTxt).replaceAll("\r\n", "");
            customsDeclaration.setResult(resTxt);
            customsDeclarationService.saveOrUpdate(customsDeclaration);
            LOGGER.info("首信易报关返回结果[{}]",resTxt);

            //解析xml节点
            Document document = DocumentHelper.parseText(resTxt);
            String httpStatus = document.selectSingleNode("//Message/Head/Status").getText(); //http请求状态
            customsDeclaration.setStatus(CustomsDeclarationStatus.FAILURE); //表示报关失败
            if(httpStatus.equals("0")){
                String resultCode = document.selectSingleNode("//Message/Response/retCode").getText(); //业务请求状态
                if(resultCode.equals("00")){
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
}
