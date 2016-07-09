package com.somnus.pay.payment.thirdPay;

import java.net.URLDecoder;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.mvc.support.utils.JsonUtils;
import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.enums.CustomsDeclarationStatus;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.event.CallbackProcessEvent;
import com.somnus.pay.payment.exception.ExceptionUtils;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.exception.UnsupportedRequestTypeException;
import com.somnus.pay.payment.pojo.BgConfig;
import com.somnus.pay.payment.pojo.Callback;
import com.somnus.pay.payment.pojo.Callback.CallbackId;
import com.somnus.pay.payment.pojo.CustomsDeclaration;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.service.CallbackService;
import com.somnus.pay.payment.service.CustomsDeclarationService;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.payment.service.PaymentChannelSwitchService;
import com.somnus.pay.payment.service.PaymentOrderService;
import com.somnus.pay.payment.util.SpringContextUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.utils.Assert;

/**
 * @description: 支付渠道处理器
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-9
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-9       丹青生                               1.0            初始化
 */
public abstract class PaymentChannelHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaymentChannelHandler.class);
	
	@Resource
	protected IConfigService configService;
	@Resource
	protected IPayService payService;
	@Resource
	protected CallbackService callbackService;
	@Resource
	protected PaymentOrderService paymentOrderService;
	@Resource
	private PaymentChannelSwitchService paymentChannelSwitchService;
	@Resource
	private CustomsDeclarationService customsDeclarationService;
	private final Set<PayChannel> supportChannels;
	private ThreadLocal<RequestParameter<?, ?>> parameterHolder = new ThreadLocal<RequestParameter<?, ?>>();

	public PaymentChannelHandler(PayChannel...supportChannel){
		this.supportChannels = new HashSet<PayChannel>(Arrays.asList(supportChannel));
	}
	
	public IConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(IConfigService configService) {
		this.configService = configService;
	}
	
	public IPayService getPayService() {
		return payService;
	}

	public void setPayService(IPayService payService) {
		this.payService = payService;
	}

	/**
	 * 处理支付渠道的回调请求
	 * 
	 * @param parameter 回调参数
	 */
	public <R> R handle(RequestParameter<?, ?> parameter){
		LOGGER.info("处理[{}]渠道的[{}]请求:[{}]", new Object[]{parameter.getChannel(), parameter.getType(), parameter.getData()});
		boolean allow = paymentChannelSwitchService.getValue(parameter.getChannel(), parameter.getType());
		Assert.isTrue(allow, PayExceptionCode.PAYMENT_CHANNEL_SERVICE_IS_UNABLE);
		parameterHolder.set(parameter);
		RequestType type = parameter.getType();
		Object result = null;
		switch (type) {
			case RETURN:
			case NOTIFY:
				result = handleCallback((RequestParameter<?, String>) parameter);
				break;
			case ORDER:
				result = this.handleOrder((RequestParameter<PaymentOrder, String>) parameter);
				break;
			case QUERY:
				result = this.handleQuery((RequestParameter<String, Map<String, String>>) parameter);
				break;
			case CONFIRM:
				result = this.handleConfirm((RequestParameter<String, PaymentResult>) parameter);
				break;
			case BG:
				this.handleBg((RequestParameter<PaymentResult, String>) parameter);
				break;
			default:
				throw new UnsupportedRequestTypeException();
		}
		return (R) result;
	}
	
	protected String handleCallback(RequestParameter<?, String> parameter) {
		Map<String, String> data = this.parseParam(parameter.getData());
		Assert.notEmpty(data, PayExceptionCode.PAYMENT_PARAMETER_IS_NULL);
		String result = null;
		if(parameter.getType() == RequestType.RETURN){
			result = payService.getReturnUrl(this.handleReturn(data));
		}else if(parameter.getType() == RequestType.NOTIFY){
			result = this.handleNotify(data);
		}else {
			throw new PayException(PayExceptionCode.UNSUPPORTED_REQUEST_TYPE);
		}
		Assert.notNull(result, PayExceptionCode.PAYMENT_PARAMETER_IS_NULL);
		try {
			PaymentResult[] paymentResult = this.convert(data);
			List<Callback> callbacks = new ArrayList<Callback>(paymentResult.length);
			int localCount = 0;
			for (int i = 0; i < paymentResult.length; i++) {
				LOGGER.info("处理[{}]渠道的[{}]转换成paymentResult:[{}]", new Object[]{parameter.getChannel(), parameter.getType(), JsonUtils.toJson(paymentResult[i])});
				if(PaymentOrderType.SCCUESS.getValue().equals(paymentResult[i].getStatus())){
					PaymentOrder paymentorder = paymentOrderService.get(paymentResult[i].getOrderId());
					if(paymentorder == null){
						LOGGER.info("转发订单[{}]的回调通知", paymentResult[i].getOrderId());
						try {
							payService.backNotifyInnerAddress(data, parameter.getChannel());
						} catch (Exception e) {
							LOGGER.warn("转发订单[" + paymentResult[i].getOrderId() + "]的回调通知失败", e);
						}
					}else {
						localCount++;
						parameter.setChannel((null == paymentResult[i].getChannel())?parameter.getChannel():paymentResult[i].getChannel());
						paymentResult[i].setChannel(parameter.getChannel());
						CallbackId id = new CallbackId(paymentResult[i].getOrderId(), parameter.getChannel(), parameter.getType());
						callbacks.add(new Callback(id, JsonUtils.toJson(paymentResult[i])));
					}
				}else {
					LOGGER.info("非支付成功通知,可忽略:{}", JsonUtils.toJson(paymentResult[i]));
				}
			}
			if(localCount > 0){
				if(LOGGER.isInfoEnabled()){
					LOGGER.info("保存支付回调通知记录:{}", JsonUtils.toJson(callbacks));
				}
				callbackService.save(callbacks.toArray(new Callback[callbacks.size()]));
				SpringContextUtil.getApplicationContext().publishEvent(new CallbackProcessEvent(this, parameter, paymentResult));
			}
		} catch (Exception e) {
			LOGGER.warn("处理支付渠道的回调请求失败", e);
			throw new PayException(PayExceptionCode.CALLBACK_FAIL);
		}
		return result;
	}
	
	/**
	 * 当前处理器是否支持处理当前回调请求
	 * 
	 * @param parameter 回调请求参数
	 * @return 是否支持
	 */
	protected boolean isSupport(RequestParameter<?, ?> parameter){
		return parameter != null && supportChannels.contains(parameter.getChannel());
	}
	
	/**
	 * 处理通知类型的回调请求
	 * 
	 * @param data
	 * @return 请求响应结果
	 */
	protected abstract String handleNotify(Map<String, String> data);
	
	/**
	 * 处理返回类型的回调请求
	 * 
	 * @param data
	 * @return 订单号
	 */
	protected abstract String handleReturn(Map<String, String> data);

	/**
	 * 返回失败时的HTTP响应内容
	 * 
	 * @return 失败时的HTTP响应内容
	 */
	public abstract String getFailedResponse(RequestParameter<?, ?> parameter);

	/**
	 * 将通知数据转换到标准支付结果
	 * @param data 通知数据
	 * @return 标准支付结果
	 */
	protected abstract PaymentResult[] convert(Map<String, String> data);
	
	/**
	 * 解析回调参数（默认是获取key/Value处理）,子类有特殊的待重写
	 * @return
	 */
	protected Map<String, String> parseParam(Object parameter){
		if(parameter instanceof Map){
			return WebUtil.getParamMap((Map<String, ?>) parameter);
		}else if(parameter instanceof CharSequence){
			String text = parameter.toString();
			String[] pairs = text.split("&");
			if(pairs != null && pairs.length > 0){
				LOGGER.info("尝试解析key-value结构的requestbody支付参数");
				Map<String, String> params = new HashMap<String, String>();
				for (int i = 0; i < pairs.length; i++) {
					if(StringUtils.isNotEmpty(pairs[i])){
						String[] item = pairs[i].split("=");
						String value = "";
						try {
							value = item.length > 1 ? URLDecoder.decode(item[1], "UTF-8") : "";
						} catch (Exception e) {
							LOGGER.warn("URL转码失败", e);
						}
						params.put(item[0], value);
					}
				}
				return params.isEmpty() ? null : params;
			}
		}
		return null;
	}
	
	protected RequestParameter<?, ?> getRequestParameter(){
		return parameterHolder.get();
	}
	
	/**
	 * 构建第三方支付页面跳转表单
	 * @param parameter 请求参数
	 * @return 跳转表单
	 */
	protected abstract String handleOrder(RequestParameter<PaymentOrder, String> parameter);

	/**
	 * 根据订单号查询第三方支付结果
	 * @param parameter 请求参数
	 * @return 第三方支付结果
	 */
	protected Map<String, String> handleQuery(RequestParameter<String, Map<String, String>> parameter){
		throw new B5mException(PayExceptionCode.UNSUPPORTED_OPERATION);
	}
	
	/**
	 * 根据订单号确认支付是否成功
	 * @param parameter 请求参数
	 * @return 支付是否成功
	 */
	protected boolean handleConfirm(RequestParameter<String, PaymentResult> parameter){
		LOGGER.warn("当前渠道不支持主动确认支付结果");
		return false;
	}
	
	protected void handleBg(RequestParameter<PaymentResult, String> parameter){
		PaymentResult paymentResult = parameter.getData();
        LOGGER.info("订单[{}]准备报关", paymentResult.getOrderId());
        PaymentOrder paymentOrder = paymentOrderService.get(paymentResult.getOrderId());
        Assert.notNull(paymentOrder, PayExceptionCode.ORDER_IS_NULL);
		List<BgConfig> configs = this.getBgConfig(paymentOrder);
		if(null == configs || configs.isEmpty()){
			LOGGER.warn("未找到订单[{}]可用的报关配置", paymentOrder.getOrderId());
			return;
		}
		//循环支付渠道的报关配置配置
		for (BgConfig bgConfig: configs){
			LOGGER.info("支付渠道报关配置中orderNo:[{}],config:[{}]开始", paymentOrder.getOrderId(), bgConfig);
			// 根据orderId查询当前订单是否报关
			CustomsDeclaration customsDeclaration = customsDeclarationService.getById(paymentResult.getOrderId(), paymentResult.getChannel(), bgConfig.bgConfigKey());
			if(customsDeclaration != null){
				if(customsDeclaration.getStatus() == CustomsDeclarationStatus.SUCCEED){
					LOGGER.info("订单[{}]已经报关，不需要再次报关", paymentResult.getOrderId());
					return;
				}else{
					LOGGER.warn("订单[{}]已经报关，但是报关不成功，再次报关。当前报关状态为：{}", paymentResult.getOrderId(),customsDeclaration.getStatus());
				}
			}else{
				customsDeclaration = new CustomsDeclaration();
				customsDeclaration.setOrderId(paymentResult.getOrderId());
				customsDeclaration.setChannel(paymentResult.getChannel());
				customsDeclaration.setBgConfigKey(bgConfig.bgConfigKey());
				customsDeclaration.setCreateTime(new Date());
				customsDeclaration.setStatus(CustomsDeclarationStatus.NOTYET);
				customsDeclarationService.save(customsDeclaration);
			}
			customsDeclaration.setUpdateTime(new Date());
			LOGGER.info("使用配置[{}]为订单[{}]报关", configs, paymentOrder.getOrderId());
			try {
				doBg(parameter.getData(), paymentOrder, bgConfig, customsDeclaration);
			} catch(Exception e){
				LOGGER.warn(parameter.getChannel() + "报关失败", e);
				customsDeclaration.setResult(ExceptionUtils.getCauseMessage(e));
				customsDeclaration.setStatus(CustomsDeclarationStatus.FAILURE);
				customsDeclarationService.saveOrUpdate(customsDeclaration);
				throw new PayException(PayExceptionCode.CUSTOMS_DECLARATION_ERROR, e);
			}
		}
	}
	
	protected List<BgConfig> getBgConfig(PaymentOrder paymentOrder) {
		return null;
	}
	
	protected void doBg(PaymentResult paymentResult, PaymentOrder paymentOrder, BgConfig config, CustomsDeclaration customsDeclaration){
		throw new B5mException(PayExceptionCode.UNSUPPORTED_OPERATION);
	}
	
}
