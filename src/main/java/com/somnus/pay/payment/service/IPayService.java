package com.somnus.pay.payment.service;

import java.util.List;
import java.util.Map;

import com.somnus.pay.payment.api.PaymentService;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.model.Payment;
import com.somnus.pay.payment.pojo.Msg;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.thirdPay.RequestParameter;

public interface IPayService extends PaymentService {

    /**
     * 订单页面处理
     * @param paymentOrder
     * @param needVerifySign
     */
    public void checkPaymentOrder(PaymentOrder paymentOrder, boolean needVerifySign);
	
    public PaymentOrder initPaymentOrder(PaymentOrder paymentOrder);
	
    public void updateOrderStatus(final PaymentResult paymentResult);
    
	/**
	 * 构建响应文本
	 * @param paymentOrder
	 * @param exception
	 * @return
	 */
	public String buildResponse(PaymentOrder paymentOrder, Exception exception);
	
	/**
	 * 处理支付平台返回结果
	 * @param callbackParameter
	 * @param paymentResults
	 * @return
	 */
	public void handleCallback(RequestParameter callbackParameter, PaymentResult...paymentResults) throws Exception;

    /**
     * 根据订单获取 支付完成结果页
     * @param orderId
     * @return
     */
    public String getReturnUrl(String orderId);

    /**
     * 根据订单获取 支付完成结果页
     * @param paymentOrder
     * @return
     */
    public String getReturnUrl(PaymentOrder paymentOrder);

    /**
     * 订单 详细
     * @param orderId
     * @param source
     * @return
     */
    public Msg getOrderDetails(String orderId,Integer source);
    
    /**
     * 第三方查询并更新订单状态
     * @param params
     * @return
     */
    public Msg queryPaymentOrder(Map<String,String> params,PayChannel payType);

    /**
     * 手动更新支付状态
     * @param params
     * @return
     */
    public Msg updateStatusManual(Map<String,String> params);
    
    /**
     * 查询sourceId对应的PaymentSource
     * @param id
     * @return
     */
    public PaymentSource handlePaymentSource(String id);

    /**
     * <pre>
     * 通过订单明细获取订单详情
     * </pre>
     *
     * @param orderId
     * @return
     */
    public List<PaymentOrder> getOrderDetail(String orderId);

    /**
     * <pre>
     * 通过用户ID获取用户使用支付类型的频率
     * </pre>
     *
     * @param userId
     * @return
     */
    public Map<String, List<String>> getBankTypeIndexByUserId(String userId);

    /**
     * 根据订单号获取需要支付的生成扫码的短连接
     * @param rootPath
     * @param paymentOrder
     */
    public void getShortUrl4Sina(String rootPath,PaymentOrder paymentOrder);

    /**
     * 获取前台通知页面的请求表单字符串
     * @param paymentOrder
     * @param errorMsg
     * @return
     */
    public String getErrorURLRequestForm(PaymentOrder paymentOrder, String errorMsg);

    /**
     * 清空用户渠道的缓存信息
     */
    public void cacheUserChooseConfigClean();

    /**
     * 根据key从缓存中取支付信息
     * 
     * @param key 缓存key
     * @return
     */
    public Payment getPayment(String key);
    
    /**
     * 将来自HTTP参数构造的支付信息与来自缓存的支付信息(如果有)进行合并
     * 
     * @param paymentOrder paymentOrder 来自HTTP参数构造的支付信息
     * @return false=未合并,true=合并成功
     */
    public boolean mergePayment(PaymentOrder paymentOrder);

    /**
     * 取消支付缓存数据
     * @param payIds
     * @return
     */
    public void cancelPayment(String ... payIds);

    /**
     * 创建支付信息缓存key
     * @param payId
     * @return
     */
    public String createPaymentStandbyKey(String payId);

    public boolean backNotifyInnerAddress(Map<String, String> params, PayChannel channel);
    
}
