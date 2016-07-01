package com.somnus.pay.payment.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.payment.exception.ExceptionUtils;
import com.somnus.pay.payment.exception.PayException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.enums.NotifyChannel;
import com.somnus.pay.payment.enums.NotifyStatus;
import com.somnus.pay.payment.enums.NotifyType;
import com.somnus.pay.payment.enums.PaySource;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Notify;
import com.somnus.pay.payment.pojo.Notify.NotifyId;
import com.somnus.pay.payment.pojo.PaymentMessage;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.service.NotifyService;
import com.somnus.pay.payment.service.PaymentNotifyService;
import com.somnus.pay.payment.util.ConfigUtil;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.MetaqUtil;
import com.somnus.pay.payment.util.PaySign;
import com.somnus.pay.payment.util.PaymentHttpUtils;
import com.somnus.pay.utils.Assert;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-30<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-30       丹青生                        1.0            初始化 <br/>
 *  
 */
@Service
@Transactional
public class PaymentNotifyServiceImpl implements PaymentNotifyService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentNotifyServiceImpl.class);
	
	// 支付回调后支付状态变更消息的TOPIC
	private  static final String METAQ_TOPIC_PAYORDER  = ConfigUtil.getValue("config/config", "metaq_topic_pay_oper_order");
	// 使用MetaQ通知内部系统的sourceId
	private  static final String NOTIFY_METAQ_SOURCEID  = ConfigUtil.getValue("config/config", "NOTIFY_METAQ_SOURCEID");
	// 订单接收超级帮钻冻结成功通知的地址
	private static final String ORDERGROUP_URL_PAYING = "/dh/order/group/paying.htm";
	
	@Resource
	private NotifyService notifyService;
	@Resource
	private IConfigService configService;
	@Autowired
    private MetaqUtil metaqUtil;
    @Value("#{configProperties['order.server']}")
    private String ORDER_SERVER;
	
	@Override
	public void notifyPaySuccess(PaymentOrder paymentOrder) {
		LOGGER.info("通知发起方订单[{}]已支付成功:{}", paymentOrder.getOrderId(), paymentOrder);
		Assert.isTrue(paymentOrder.getStatus() == PaymentOrderType.SCCUESS.getValue(), PayExceptionCode.PAY_RESULT_IS_NOT_SUCCESS);
        NotifyId id = new NotifyId(paymentOrder.getOrderId(), NotifyType.PAY_SUCCESS);
        Notify notify = notifyService.lock(id);
        boolean exist = notify != null;
        if(exist){
        	if (notify.getStatus() == NotifyStatus.SUCCESS) {
        		LOGGER.warn("订单[{}]支付成功通知已通过[{}]成功送达,无需重复通知", paymentOrder.getOrderId(), notify.getChannel());
        		return;
			}
        }else {
        	notify = new Notify();
        	notify.setId(id);
        	notify.setSource(paymentOrder.getSource());
		}
        PaymentSource paymentSource = configService.getPaymentSourceById(paymentOrder.getSource() + "");
        Assert.notNull(paymentSource, PayExceptionCode.ORDER_PAYMENT_SOURCE_NOT_FOUND);
        String key = paymentSource.getSourceKey();
        String callbackUrl = paymentSource.getNotifyUrl();
        Integer sourceId = paymentSource.getSourceId();
        String orderId = paymentOrder.getOrderId();
        String userId = paymentOrder.getUserId();
        String thirdTradeNo = paymentOrder.getThirdTradeNo();
        String parentOrderId = paymentOrder.getParentOrderId();
        String price = String.valueOf(paymentOrder.getAmount());
        Integer status = paymentOrder.getStatus();
        Integer freefeeType = paymentOrder.getFreeFeeType();
        String sign = "";
        PaymentMessage message = new PaymentMessage();
        message.setPayId(orderId);
        message.setUserId(userId);
        message.setStatus(status);
        message.setThirdPayType(paymentOrder.getThirdPayType());
        message.setThirdTradeNo(thirdTradeNo);
        message.setParentOrderId(parentOrderId);
        message.setPayFrom(paymentOrder.getPayFrom());
        message.setImei(paymentOrder.getImei());
        Double finalAmount  = paymentOrder.getFinalAmount();
        if(finalAmount ==null || finalAmount < 0){
            finalAmount = 0d ;
        }
        message.setFinalAmount(finalAmount);
        Long superBz = paymentOrder.getBzAmount();
        if (superBz == null || superBz < 0) {
            superBz = 0l;
        }
        message.setSuperBz(superBz);
        message.setAmount(paymentOrder.getAmount());
        message.setTotalAmount(paymentOrder.getTotalAmount());
        message.setSource(paymentOrder.getSource());
        message.setIsCombined(paymentOrder.getIsCombined());
        //区分topic类型的如果是231和232表示帮我采类型
        if(sourceId == 231 || sourceId == 232){
        	message.setMessageType(0);
        }else{
        	message.setMessageType(1);
        }
        // TODO:这种特殊的业务逻辑放在这里显然不合适
        if ((sourceId == 225 || sourceId == 226 || sourceId == 228) && null != freefeeType && (freefeeType == 1 || freefeeType == 2)) { // 做VIP购买时
            message.setPrice(price);
            message.setPayTime(paymentOrder.getCreateTime() == null ? new Date().getTime() : paymentOrder.getCreateTime().getTime());
            message.setFreeFeeType(freefeeType);
            String inviterId = paymentOrder.getInviterId();
            message.setInviterId(StringUtils.trimToEmpty(inviterId));
            // 购买黄金VIP的时候需要传入价格
            sign = PaySign.sign(userId + orderId + status + key + freefeeType + inviterId + price, null);
        } else if (isNeedMetaQNotifySoucreIds(sourceId)) {
            sign = PaySign.sign(orderId + userId + thirdTradeNo + status + "" + finalAmount + "" +superBz + key, null);
        } else {
            sign = PaySign.sign(userId + orderId + status + key, null);
        }
        message.setSign(sign);
        String notifyParam = JsonUtils.toJson(message);
        if(LOGGER.isInfoEnabled()){
        	LOGGER.info("订单[{}]支付成功通知:订单来源={}, 通知参数={}", new Object[]{orderId, sourceId, callbackUrl, notifyParam});
        }
        String result = null;
        Exception error = null;
        try {
        	if(isNeedMetaQNotifySoucreIds(sourceId)){
        		notify.setChannel(NotifyChannel.METAQ);
        		notify.setTarget(METAQ_TOPIC_PAYORDER);
        		notify.setData(notifyParam);
        		LOGGER.info("通过JMS发送回调通知,topic:{}", METAQ_TOPIC_PAYORDER); // TODO 根据paysource动态决定
                boolean success = metaqUtil.send(notifyParam, METAQ_TOPIC_PAYORDER);
        		result = success ? "成功" : "失败";
                LOGGER.info("通过JMS发送回调通知{}", result);
                Assert.isTrue(success, PayExceptionCode.METAQ_SERVICE_ERROR); // metaq出错
        	}else{
        		notify.setChannel(NotifyChannel.HTTP);
        		notify.setTarget(callbackUrl);
        		Map<String, Object> paramsMap = message.toNotifyParamsMap();
        		notify.setData(JsonUtils.toJson(paramsMap));
        		LOGGER.info("通过HTTP发送回调通知:地址={}, 参数={}", callbackUrl, paramsMap);
        		result = PaymentHttpUtils.notity(callbackUrl, paramsMap);
        		LOGGER.warn("通过HTTP发送回调通知结果:{}", result);
        	}
        	notify.setStatus(NotifyStatus.SUCCESS);
		} catch (Exception e) {
            error = e;
			LOGGER.warn("支付成功时通知请求发起方失败", e);
			result = ExceptionUtils.getCauseMessage(e);
			notify.setStatus(NotifyStatus.FAILURE);
		}
        notify.setMemo(result);
        notifyService.save(notify);
        if(error != null){ // 遇到异常时继续往上抛可导致任务自动重试
            throw  error instanceof B5mException ? ((B5mException) error) : new PayException(PayExceptionCode.SEND_NOTIFY_ERROR,error);
        }
	}
	
	/**
     * 判断sourceId是否需要走订单通知逻辑
     * @param sourceId
     * @return
     */
    private boolean isNeedMetaQNotifySoucreIds(Integer sourceId){
        boolean res = false;
        String needMetaQNotifySoucreIdStr = configService.getStringValue(Constants.NEED_METAQ_NOTIFY_SOUCREIDS);
        String [] needMetaQNotifySoucreIdArray =StringUtils.isNotBlank(needMetaQNotifySoucreIdStr)?needMetaQNotifySoucreIdStr.split(","):NOTIFY_METAQ_SOURCEID.split(",");
        List<String> needMetaQNotifySoucreIds = Arrays.asList(needMetaQNotifySoucreIdArray);
        if(null != needMetaQNotifySoucreIds && needMetaQNotifySoucreIds.contains(sourceId+"")){
            res = true;
        }
        return res;
    }

	@Override
	public void notifyBzFrozenSuccess(PaymentOrder paymentOrder) {
		LOGGER.info("超级帮钻冻结成功通知:[{}]", paymentOrder);
		if(PaySource.BANGZUAN_DUIHUAN.getValue().equals(paymentOrder.getSource())){
			LOGGER.warn("当前支付请求源不支持接收超级帮钻冻结成功回调通知");
			return;
		}
		NotifyId id = new NotifyId(paymentOrder.getOrderId(), NotifyType.BZ_FROZEN_SUCCESS);
        Notify notify = notifyService.lock(id);
        boolean exist = notify != null;
        if(exist){
        	if (notify.getStatus() == NotifyStatus.SUCCESS) {
        		LOGGER.warn("订单[{}]支付成功通知已通过[{}]成功送达,无需重复通知", paymentOrder.getOrderId(), notify.getChannel());
        		return;
			}
        }else {
        	notify = new Notify();
        	notify.setId(id);
        	notify.setSource(paymentOrder.getSource());
		}
        boolean isUseBz = !paymentOrder.getIsCombined() && paymentOrder.getBzAmount() != null && paymentOrder.getBzAmount() > 0;
        if (isUseBz) {
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("groupId", paymentOrder.getOrderId());
            paramMap.put("userId", paymentOrder.getUserId());
            paramMap.put("superBz", paymentOrder.getBzAmount() + "");
            String url = ORDER_SERVER + ORDERGROUP_URL_PAYING;
			LOGGER.info("发送超级帮钻冻结成功通知:{}", url);
    		String response = "";
    		try {
    			notify.setChannel(NotifyChannel.HTTP);
        		notify.setTarget(url);
        		notify.setData(JsonUtils.toJson(paramMap));
    			response = HttpClientUtils.post(url, paramMap);
				LOGGER.info("超级帮钻冻结成功通知已送达:{}", response);
				if(StringUtils.isEmpty(response)){
					response = "无响应";
					notify.setStatus(NotifyStatus.FAILURE);
				}else {
					JSONObject object = JSON.parseObject(response);
					notify.setStatus(object.getBooleanValue("ok") ? NotifyStatus.SUCCESS : NotifyStatus.FAILURE);
				}
    		} catch (Exception e) {
    			notify.setStatus(NotifyStatus.FAILURE);
    			response = e.getMessage()+ "\n" + response;
				LOGGER.warn("发送超级帮钻冻结成功通知失败", e);
    		}
    		notify.setMemo(response);
    		notifyService.save(notify);
        }else if(LOGGER.isInfoEnabled()){
        	LOGGER.info("当前支付并未使用超级帮钻抵扣,无需通知");
        }
	}

}
