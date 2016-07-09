package com.somnus.pay.payment.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

import com.somnus.pay.utils.PWCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.somnus.pay.payment.api.PayPasswordService;
import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.dao.IPayDao;
import com.somnus.pay.payment.enums.NotifyType;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaySource;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.event.NotifyClientEvent;
import com.somnus.pay.payment.event.PaySuccessEvent;
import com.somnus.pay.payment.event.PaymentEvent;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.ConfirmResult;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.service.IClientService;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.payment.service.PaymentChannelService;
import com.somnus.pay.payment.service.PaymentOrderService;
import com.somnus.pay.payment.service.PaymentService;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.MemCachedUtil;
import com.somnus.pay.payment.util.PayAmountUtil;
import com.somnus.pay.payment.util.SpringContextUtil;
import com.somnus.pay.utils.Assert;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-20<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-20       丹青生                        1.0            初始化 <br/>
 *  
 */
@Service
public class PaymentServiceImpl implements PaymentService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);
	
	@Autowired
	private PayPasswordService payPasswordService;
	@Autowired
	private IConfigService configService;
	@Autowired
	private IPayService payService;
	@Autowired
	private IClientService clientService;
	@Autowired
	private IPayDao payDao;
	@Autowired
	private PaymentChannelService paymentChannelService;
	@Autowired
	private PaymentOrderService paymentOrderService;
	
	@Override
	public String pay(PaymentOrder paymentOrder) {
        LOGGER.info("处理支付请求:[{}]", paymentOrder);
        boolean useBz = Constants.USE_BZ.equals(paymentOrder.getUsebz()) && paymentOrder.getBzAmount() != null;
        if (paymentOrder != null && useBz && PayAmountUtil.divide(paymentOrder.getAmount()+"", "1").equals(PayAmountUtil.divide(paymentOrder.getBzAmount()+"", "100"))) {
            paymentOrder.setThirdPayType(PayChannel.SuperBeanPay.getValue()); // 超级帮钻全额支付
        }else{
            LOGGER.info("支付请求为可疑全额帮钻支付请求:[{}]", paymentOrder);
            Assert.isTrue(!PayChannel.SuperBeanPay.getValue().equals(paymentOrder.getThirdPayType()), PayExceptionCode.THIRD_PAY_TYPE_ERROR);
        }
        Assert.isTrue(paymentOrder != null && paymentOrder.getThirdPayType() != null, PayExceptionCode.THIRD_PAY_TYPE_NULL);
        if (useBz && paymentOrder.isNeedSuperBzPayPwd()) { // 如果有超级帮钻支付，需要判断超级帮钻支付密码是否正确
			LOGGER.info("校验用户[{}]支付密码", paymentOrder.getUserId());
    		payPasswordService.validatePayPassword(paymentOrder.getUserId(), paymentOrder.getSuperBzPayPwd());
    	}
        boolean lock = MemCachedUtil.addCache(Constants.MEMCACHE_PAYING_KEY + paymentOrder.getOrderId(), paymentOrder.getOrderId(), 3 * 60);
        Assert.isTrue(lock, PayExceptionCode.ORDER_IS_PAYING);
        Assert.isTrue(!paymentOrder.getIsCombined(), PayExceptionCode.NOT_SUPPORT_COMBINED_PAY);
        Double amount = paymentOrder.getAmount();
        Long bzAmount = paymentOrder.getBzAmount();
        //校验帮钻渠道最大可支付金额
        Long bzMaxValue = Long.parseLong(StringUtils.defaultIfBlank(configService.getBzMaxValue(paymentOrder),"0"));
        Assert.isTrue((null == bzMaxValue || bzMaxValue == 0) || (bzAmount <= bzMaxValue), PayExceptionCode.BZ_LIMIT_ERROR);
        BigDecimal total = new BigDecimal("0.00"); // 人民币支付额度
        if (bzAmount != null && bzAmount > 0) {
            total = total.add(new BigDecimal(amount.toString()));
            total = total.subtract(new BigDecimal(bzAmount.toString()).divide(new BigDecimal(PayAmountUtil.PAY_MOUNT_UNIT)));
        } else {
            total = total.add(new BigDecimal(amount.toString()));
        }
        paymentOrder.setAmount(total.doubleValue());
        LOGGER.info("执行订单支付:[{}]", paymentOrder);
        Integer thirdPayTypeValue = paymentOrder.getThirdPayType();
        paymentOrder.setThirdPayType(null);//未支付成功不保存支付渠道Type
        PaymentOrder order = payService.initPaymentOrder(paymentOrder);
        Assert.isTrue(PaymentOrderType.SCCUESS != PaymentOrderType.valueOf(order.getStatus()), PayExceptionCode.REPEAT_PAY_SUCCESS_ORDER); // 成功订单不允许重复支付
        boolean bzTradeSuccess = PaymentOrderType.valueOf(order.getBzStatus()) == PaymentOrderType.SCCUESS && order.getBzAmount() != null && order.getBzAmount() > 0;
        if(bzTradeSuccess){ // 如果超级帮钻抵扣已成功,则前后两次提交的超级帮钻抵扣额度及订单金额必须一致
        	Assert.isTrue(paymentOrder.getBzAmount() != null && paymentOrder.getBzAmount().equals(order.getBzAmount()), PayExceptionCode.SUPER_BEAN_AMOUNT_DIFFERENCE);
        	Assert.isTrue(paymentOrder.getAmount() != null && paymentOrder.getAmount().equals(order.getAmount()), PayExceptionCode.ORDER_AMOUNT_DIFFERENCE);
        }else { // 如果超级帮钻抵扣未成功,则使用新提交的超级帮钻额度抵扣数据
            order.setBzAmount(paymentOrder.getBzAmount());
            order.setBzStatus(paymentOrder.getBzStatus());
            order.setAmount(paymentOrder.getAmount());
            this.paySuperBean(order);
		}
        order.setThirdPayType(thirdPayTypeValue);
        //如果是全额帮钻支付，直接更新数据库状态
        if((PayChannel.SuperBeanPay.getValue().equals(order.getThirdPayType())) && (PaymentOrderType.valueOf(order.getBzStatus()) == PaymentOrderType.SCCUESS)){
            PaymentResult paymentResult = new PaymentResult();
            paymentResult.setIsCombined(order.getIsCombined() ? 1 : 0);
            paymentResult.setOrderId(order.getOrderId());
            paymentResult.setTradeStatus(PaymentOrderType.SCCUESS.getValue() + "");
            paymentResult.setChannel(PayChannel.SuperBeanPay);
            paymentResult.setThirdTradeNo(order.getOrderId());
            paymentResult.setPrice(order.getAmount());
            paymentResult.setPayInfo("全额帮钻支付");
            paymentResult.setStatus(PaymentOrderType.SCCUESS.getValue());
            LOGGER.info("全额超级帮钻抵扣结果:{}", paymentResult);
            payService.updateOrderStatus(paymentResult);
            order.setStatus(PaymentOrderType.SCCUESS.getValue());
            PaymentEvent event = new PaySuccessEvent(this, paymentResult);
            event.setDelay(5000);
            SpringContextUtil.getApplicationContext().publishEvent(event);
        }
        String buildQuestForm = payService.buildResponse(order, null);
        //清除缓存中正在处理订单数据
        MemCachedUtil.cleanCache(Constants.MEMCACHE_PAYING_KEY + paymentOrder.getOrderId());
        return buildQuestForm;
	}
	
	public void paySuperBean(PaymentOrder paymentOrder){
		LOGGER.info("超级帮钻抵扣请求:[{}]", paymentOrder);
		boolean success = false;
        if(PaySource.BANGZUAN_DUIHUAN.getValue().equals(paymentOrder.getSource())){
        	success = paySuperBean(paymentOrder, configService.getStringValue(Constants.DUIHUAN_EVENTID), null, null);
        }else{
        	success = paySuperBean(paymentOrder,configService.getStringValue(Constants.ORDER_EVENTID),"OM_", "超级帮钻抵现");
        }
        if(success){
        	paymentOrder.setUpdateTime(new Date());
        	this.payDao.update(paymentOrder); // 更新帮钻抵扣状态
        	PaymentEvent event = new NotifyClientEvent(this, NotifyType.BZ_FROZEN_SUCCESS, paymentOrder.getOrderId());
        	SpringContextUtil.getApplicationContext().publishEvent(event);
        }
	}
	
	protected boolean paySuperBean(PaymentOrder paymentOrder,String eventId,String orderPrefix,String productName) {
        boolean isUseBz = !paymentOrder.getIsCombined() && paymentOrder.getBzAmount() != null && paymentOrder.getBzAmount() > 0;
        if (isUseBz) {
            Map<String, String> paramtMap = new HashMap<String, String>(8);
            paramtMap.put("orderId", StringUtils.defaultIfBlank(orderPrefix,"")+paymentOrder.getOrderId());
            paramtMap.put("userId", paymentOrder.getUserId());
            paramtMap.put("amount", "" + paymentOrder.getBzAmount());
            paramtMap.put("eventId", eventId);
            paramtMap.put("productName", productName);
            paramtMap.put("productNum", "1");
            paramtMap.put("memo", StringUtils.defaultIfBlank(productName, paymentOrder.getSubject()));
            String signature = PWCode.getMD5String(paymentOrder.getUserId() + paymentOrder.getOrderId() + paymentOrder.getBzAmount());
            signature += PWCode.getMD5String("payment");
            signature = PWCode.getMD5String(signature);
            paramtMap.put("signature", signature);
            LOGGER.info("调用帮钻系统抵扣超级帮钻:{}", paramtMap);
            String payResult = clientService.paySuperBean(paramtMap);
            Assert.hasText(payResult, PayExceptionCode.SUPER_BEAN_TRADE_NO_RESPONSE);
            LOGGER.info("调用帮钻系统抵扣超级帮钻:{},返回结果payResult:{}", paramtMap, payResult);
            paymentOrder.setBzStatus(PaymentOrderType.SCCUESS.getValue());
            if (PayChannel.SuperBeanPay.getValue().equals(paymentOrder.getThirdPayType())) {
                paymentOrder.setThirdTradeNo(paymentOrder.getOrderId());
                paymentOrder.setDefaultBank("b5m");
            }
        }else{
            LOGGER.info("当前支付请求不允许使用超级帮钻抵扣功能");
        }
        return isUseBz;
    }

	@Override
	public boolean confirmAndUpdateOrder(String orderId) {
		PaymentOrder paymentOrder = paymentOrderService.get(orderId);
		Assert.notNull(paymentOrder, PayExceptionCode.PAYMENT_ORDER_RECORD_NOT_EXIST);
		if(PaymentOrderType.SCCUESS.getValue().equals(paymentOrder.getStatus())){
			return true;
		}
		ConfirmResult confirmResult = paymentChannelService.confirmOrder(paymentOrder);
		boolean success = confirmResult.isSupport() && confirmResult.getResult() != null;
		success = success && PaymentOrderType.SCCUESS.getValue().equals(confirmResult.getResult().getStatus());
		if(success){
			PaymentResult paymentResult = paymentOrderService.convert(paymentOrder);
			LOGGER.info("订单[{}]已支付成功,第三方支付号:{}", orderId, confirmResult.getResult().getThirdTradeNo());
			paymentResult.setThirdTradeNo(confirmResult.getResult().getThirdTradeNo());
			this.updateOrder2Success(paymentResult);
		}
		return success;
	}

	@Override
	public void updateOrder2Success(PaymentResult paymentResult) {
		paymentResult.setTradeStatus(PaymentOrderType.SCCUESS.getValue() + "");
		paymentResult.setStatus(PaymentOrderType.SCCUESS.getValue());
		payService.updateOrderStatus(paymentResult);
        SpringContextUtil.getApplicationContext().publishEvent(new PaySuccessEvent(this, paymentResult));
	}

    @Override
    public boolean isBZEnabledSoucreIds(Integer sourceId){
        boolean res = true;
        String unsupportBZSoucreIdsStr = configService.getStringValue(Constants.UNSUPPORT_BZ_SOUCREID);
        String [] unsupportBZSoucreIdArray =StringUtils.isNotBlank(unsupportBZSoucreIdsStr)?unsupportBZSoucreIdsStr.split(","):new String[]{};
        List<String> unsupportBZSoucreIds = Arrays.asList(unsupportBZSoucreIdArray);
        if(null != unsupportBZSoucreIds && unsupportBZSoucreIds.contains(sourceId+"")){
            res = false;
        }
        return res;
    }
}
