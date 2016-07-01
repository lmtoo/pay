package com.somnus.pay.payment.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import com.somnus.pay.utils.PWCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.cache.CacheServiceExcutor;
import com.somnus.pay.exception.B5mException;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.dao.IPayDao;
import com.somnus.pay.payment.dao.IPayLogDao;
import com.somnus.pay.payment.dao.IPaymentOperDao;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.model.Payment;
import com.somnus.pay.payment.pojo.Msg;
import com.somnus.pay.payment.pojo.PaymentLog;
import com.somnus.pay.payment.pojo.PaymentOper;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.service.IClientService;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.payment.service.PaymentChannelService;
import com.somnus.pay.payment.service.PaymentOrderService;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.tencent.util.MD5Util;
import com.somnus.pay.payment.util.ConfigUtil;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.DESEncrypt;
import com.somnus.pay.payment.util.DateUtils;
import com.somnus.pay.payment.util.FuncUtils;
import com.somnus.pay.payment.util.MemCachedUtil;
import com.somnus.pay.payment.util.PayAmountUtil;
import com.somnus.pay.payment.util.PaySign;
import com.somnus.pay.payment.util.PaymentHttpUtils;
import com.somnus.pay.payment.util.ShortUrlUtil;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.utils.Assert;

@Service("payService")
@Transactional
public class PayServiceImpl implements IPayService {

    private final static Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

    @Resource
    private IPayDao iPayDao;
    @Resource
    private IPayLogDao iPayLogDao;
    @Resource
    private IPaymentOperDao iPaymentOperDao;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IConfigService iConfigService;
    @Autowired
    private PaymentChannelService paymentChannelService;
    @Autowired
    private PaymentOrderService paymentOrderService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static List<String> platformInit = new ArrayList<String>();
    public static List<String> onlineInit = new ArrayList<String>();
    public static List<String> fastpayInit = new ArrayList<String>();

    private static final String NEW_RESULT_PATH = ConfigUtil.getValue("config/config", "NEW_RESULT_PATH");
    private static final String CRM_UPDATE_STATUS = "b5m_crm_update_status";

    @Override
    public void checkPaymentOrder(PaymentOrder paymentOrder,boolean needVerifySign) {
        if(logger.isInfoEnabled()){
            logger.info("正在重新检查支付参数:[{}]", paymentOrder.toString());
        }
        checkPaymentSource(paymentOrder); // 处理sourceKey
        if (paymentOrder.getAmountDetail() == null || StringUtils.isBlank(paymentOrder.getAmountDetail())) {
            DecimalFormat df = new DecimalFormat("0.00");
            paymentOrder.setAmountDetail(df.format(paymentOrder.getAmount()));
        }
        if(needVerifySign){
            PaySign.verify(paymentOrder); // 校验签名
        }
        // 获取超级帮钻可用额度
        if (StringUtils.isNotBlank(paymentOrder.getUserId())) {
            Long bzUsable = 0L;
            Long bzBalance = clientService.getSuperBeanByUserId(paymentOrder.getUserId());
            Long amount = Long.valueOf(PayAmountUtil.mul(paymentOrder.getAmount().toString(), PayAmountUtil.PAY_MOUNT_UNIT.toString()));
            if (amount >= bzBalance) {
                bzUsable = bzBalance;
            } else {
                bzUsable = amount;
            }
            paymentOrder.setBzBalance(bzBalance);
            paymentOrder.setBzUsable(bzUsable);
        }
        // 校验该订单是否为第一次使用超级帮钻
        List<PaymentOrder> orderList = this.getOrderDetail(paymentOrder.getOrderId());
        if (orderList != null && orderList.size() > 0) {
            paymentOrder.setBzAmount(orderList.get(0).getBzAmount());
        }
        //订单支付总金额为空的时候设置
        if (paymentOrder.getTotalAmount() == null || paymentOrder.getTotalAmount() == 0) {
            paymentOrder.setTotalAmount(paymentOrder.getAmount());
        }
    }

    public String buildResponse(PaymentOrder paymentOrder, Exception exception){
    	String response = PaymentOrderType.SCCUESS.name();
        boolean hasError = exception != null;
        String code = paymentOrder.getStatus()+"";
        String data = "";
        PayChannel payType = PayChannel.valueOf(paymentOrder.getThirdPayType());
        //如果支付流程处理有异常获取错误码及描述
        if(hasError) {
            if(exception instanceof B5mException){
                B5mException b5mException = (B5mException) exception;
                code = b5mException.getCode();
                data = b5mException.getMessage();
            }else {
                code = PayExceptionCode.PAY_FAILED.getCode();
                data = PayExceptionCode.PAY_FAILED.getMessage();
            }
        }
        //确认结果页面
        String action = NEW_RESULT_PATH;
        PaymentSource paymentSource =iConfigService.getPaymentSourceById(paymentOrder.getSource() + "");
        if(null!=paymentSource && StringUtils.isNotBlank(paymentSource.getResultUrl())){
            action = paymentSource.getResultUrl();
        }
        //获取请求参数
        Map<String, String> paramMap = null;
        //帮钻全额支付成功或支付出错或者支付状态为成功情况的处理返回页面
        if (StringUtils.isNotBlank(action)){
            paramMap = new HashMap<String, String>();
            //如是支付状态是成功，且不是已支付情况，就打你表为支付成功或全额帮钻支付
            if(PayExceptionCode.REPEAT_PAY_SUCCESS_ORDER_CODE.equals(code)){
                code = PaymentOrderType.PAID_ERROR.getValue() + "";
            }else if(hasError){
                code = PaymentOrderType.ERROR.getValue() + "";
            }
            paramMap.put("status", code);
            paramMap.put("resultMsg",data);
            paramMap.put("orderId", paymentOrder.getOrderId());
            paramMap.put("userId", paymentOrder.getUserId());
            paramMap.put("thirdPayType", paymentOrder.getThirdPayType()+"");
            paramMap.put("payFrom", paymentOrder.getPayFrom());
            paramMap.put("finalAmount", paymentOrder.getFinalAmount()+"");
            paramMap.put("superBz", paymentOrder.getBzAmount()+"");
            paramMap.put("sourceId", paymentOrder.getSource()+"");
            response = PaymentHttpUtils.buildRequest(paramMap, "get", action);
        }
        //没有支付出错且非超级帮钻全部支付情况的处理返回页面
        if(!hasError && (paymentOrder.getAmount() > 0)
                && (!PayChannel.SuperBeanPay.getValue().equals(paymentOrder.getThirdPayType()))) {
            response = paymentChannelService.createOrder(paymentOrder);
        }
        //错误时需额外处理手机端sdk方式调用结果,需重新封装错误消息
    	if(hasError && PayChannel.isMobileSDKPayWay(payType)) {
            JSONObject json = new JSONObject();
            json.put(Constants.B5M_RETURN_CODE_KEY, code);
            json.put(Constants.B5M_RETURN_DATA_KEY, data);
            response = PaymentHttpUtils.createGetURL(action,paramMap,null);
        }
    	return response;
    }

	public PaymentOrder initPaymentOrder(PaymentOrder paymentOrder){
    	PaymentOrder exist = paymentOrderService.get(paymentOrder.getOrderId(), paymentOrder.getUserId());
        if (exist == null) { // 第一次请求支付
            paymentOrder.setCreateTime(new Date());
            paymentOrder.setUpdateTime(paymentOrder.getCreateTime());
            this.iPayDao.save(paymentOrder); // 生成支付流水
            exist = paymentOrder;
        }else {
            logger.info("重试未成功的支付订单:{}", exist);
            //修复首信易网银选择问题
            exist.setDefaultBank(paymentOrder.getDefaultBank());
            if(!exist.getSource().equals(paymentOrder.getSource())){
                exist.setSource(paymentOrder.getSource());
                exist.setUpdateTime(new Date());
                logger.info("更新已存在的支付订单:{}", exist);
                this.iPayDao.update(exist);
            }
		}
        return exist;
	}

    /**
     * 第三方前台/后台通知处理
     * @param callbackParameter
     * @param paymentResults
     * @return
     */
    public void handleCallback(RequestParameter callbackParameter, PaymentResult...paymentResults) {
        try {
            for (int i = 0; i < paymentResults.length; i++) {
            	logger.info("处理第三方支付渠道[{}]的[{}]通知:[{}]", new Object[]{callbackParameter.getChannel(), callbackParameter.getType(), paymentResults[i]});
            	updateOrderStatus(paymentResults[i]);
			}
        } catch (Exception e) {
            logger.warn("处理第三方支付渠道回调通知失败", e);
            throw new PayException(PayExceptionCode.CALLBACK_PROCESS_ERROR);
        }
    }

    /**
     * <pre>
     * 更新订单状态
     * </pre>
     *
     * @param paymentResult
     */
    public void updateOrderStatus(final PaymentResult paymentResult) {
        logger.info("更新订单状态参数：{}", paymentResult);
        // 合并和普通订单查询区分
        List<PaymentOrder> orderList = new ArrayList<PaymentOrder>();
        // 是否应该查询两次,批量支付号应该加索引
        if (paymentResult.getIsCombined() == 0) {
            orderList = (List<PaymentOrder>) (this.iPayDao.find(" from PaymentOrder where orderId = ?  ", paymentResult.getOrderId()));
        } else {
            orderList = (List<PaymentOrder>) (this.iPayDao.find(" from PaymentOrder where parentOrderId = ?  ", paymentResult.getOrderId()));
        }

        for (final PaymentOrder paTmpOrder : orderList) {
            // 处理一个订单多次支付成功情况
            if (StringUtils.isNotBlank(paymentResult.getThirdTradeNo())
                    && StringUtils.isNotBlank(paTmpOrder.getThirdTradeNo())
                    && !paymentResult.getThirdTradeNo().equals(paTmpOrder.getThirdTradeNo())) {
                updateOrderStatus(paymentResult, paTmpOrder, false);
            } else {
                // 防止 notify 响应超时（订单状态 已经更新）重新发送
                if (PaymentOrderType.SCCUESS.getValue() == paTmpOrder.getStatus()) {
                    continue;
                }
                updateOrderStatus(paymentResult, paTmpOrder, true);
            }
        }
    }

    private void updateOrderStatus(final PaymentResult paymentResult, final PaymentOrder paTmpOrder, boolean isUpdate) {
        paTmpOrder.setUpdateTime(new Date());
        paTmpOrder.setThirdPayType(paymentResult.getChannel().getValue());
        paTmpOrder.setThirdTradeNo(paymentResult.getThirdTradeNo());
        paTmpOrder.setStatus(paymentResult.getStatus());
        paTmpOrder.setFinalAmount(paymentResult.getPrice());
        if (StringUtils.isNotBlank(paymentResult.getBankBillNo())) {
            paTmpOrder.setBankBillNo(paymentResult.getBankBillNo());
        }

        String errorMsg = "";
        //第三方支付机构返回金额小于需支付金额
        if (paymentResult.getPrice() < paTmpOrder.getAmount()) {
            String ip = WebUtil.getRequest() == null ? "" : FuncUtils.getIpAddr(WebUtil.getRequest());
            String remoteIp = WebUtil.getRequest() == null ? "" : WebUtil.getRequest().getRemoteAddr();
            errorMsg = "-原金额" + paTmpOrder.getAmount() + "-ip" + ip + "-remoteIp" + remoteIp;
            paTmpOrder.setStatus(PaymentOrderType.ERROR.getValue());
            if (logger.isWarnEnabled()) {
                logger.warn("支付金额与第三方支付接口回调金额不一致：原payOrder:" + paTmpOrder + ",回调结果:" + paymentResult + ",ip:" + ip + ",remoteIp:" + remoteIp);
            }
        }

        if (isUpdate) {
            this.iPayDao.update(paTmpOrder);
        } else {
            this.iPayDao.save(paTmpOrder);
        }
        paTmpOrder.setParentOrderId(paymentResult.getOrderId());
        // 更新日志
        this.iPayLogDao.save(new PaymentLog(paTmpOrder.getOrderId(), paTmpOrder.getUserId(),
                paTmpOrder.getSubject(),
                paTmpOrder.getOrderId() + "--" + paymentResult.getThirdTradeNo() + "--" + paymentResult.getBuyerId() + "--" + paymentResult.getPrice() + errorMsg, new Date()));
    }

    @Override
    public String getReturnUrl(String orderId) {
        String returnUrl = Constants.B5M_DOMAIN;
        if(StringUtils.isBlank(orderId)){
            return returnUrl;
        }
        String hql = "from PaymentOrder where orderId = ? ";
        List<PaymentOrder> list = (List<PaymentOrder>) this.iPayDao.find(hql, new Object[] { orderId });
        if (CollectionUtils.isEmpty(list)){
            return returnUrl;
        }
        PaymentOrder paymentOrder = list.get(0) ;
        return getReturnUrl(paymentOrder);
    }

    @Override
    public String getReturnUrl(PaymentOrder paymentOrder) {
        String returnUrl = Constants.B5M_DOMAIN;
        if(null == paymentOrder){
            return returnUrl;
        }
        PaymentSource paymentSource = iConfigService.getPaymentSourceById(paymentOrder.getSource() + "");
        if (null != paymentSource) {
            returnUrl = paymentSource.getResultUrl();
            if(StringUtils.isEmpty(returnUrl)){
                returnUrl = paymentSource.getReturnUrl();
            }
        }
        String param = "orderId=" + paymentOrder.getOrderId() + "&userId=" + paymentOrder.getUserId() + "&status="
                + paymentOrder.getStatus() + "&thirdPayType=" + paymentOrder.getThirdPayType() + "&payFrom="
                + paymentOrder.getPayFrom() + "&finalAmount=" + paymentOrder.getFinalAmount() + "&superBz=" + paymentOrder.getBzAmount()+ "&sourceId=" + paymentOrder.getSource();
        if (returnUrl.indexOf("?") == -1) {
            returnUrl = returnUrl + "?" + param;
        } else {
            returnUrl = returnUrl + "&" + param;
        }
        return returnUrl;
    }

    @Override
    public Msg getOrderDetails(String orderId, Integer source) {
        if (StringUtils.isBlank(orderId) && source == null)
            return new Msg(false, "param is not blank");

        Msg msg;
        String hql = " from PaymentOrder where orderId = ? ";

        if (StringUtils.isNotBlank(orderId)) {
            List<PaymentOrder> list = (List<PaymentOrder>) this.iPayDao.find(hql, new Object[] { orderId });
            if (CollectionUtils.isEmpty(list)) {
                msg = new Msg(false, "orderId is not exsit");
            } else {
                msg = new Msg(true, list.get(0));
            }
            return msg;
        }

        hql = " from PaymentOrder where source = ? and updateTime between ? and ? ";
        List<PaymentOrder> list = (List<PaymentOrder>) this.iPayDao.find(
                hql, new Object[] { source,
                        DateUtils.String2Date(DateUtils.getBeforeOrAfterDay(-1), "yyyy-MM-dd"),
                        DateUtils.String2Date(DateUtils.getCurrentDay(), "yyyy-MM-dd") });
        msg = new Msg(1, true, list);
        return msg;
    }

    /**
     * 校验判断 source是否符合，后续加缓存
     * @param paymentOrder
     * @return
     */

    private void checkPaymentSource(PaymentOrder paymentOrder) {
    	Assert.notNull(paymentOrder.getSource(), PayExceptionCode.PAYMENT_SOURCE_IS_NULL);
        PaymentSource paymentSource = iConfigService.getPaymentSourceById(paymentOrder.getSource() + "");
        Assert.notNull(paymentSource, PayExceptionCode.PAYMENT_SOURCE_IS_INVALID);
        paymentOrder.setSourceKey(paymentSource.getSourceKey());
    }

    /**
     * 查询订单状态，返回支付平台返回的参数列表
     * @param params 必须包含参数 "orderId"
     * @param payType
     * @return
     */
    @Override
    public Msg queryPaymentOrder(Map<String, String> params, PayChannel payType) {
        if(logger.isInfoEnabled()){
            logger.info("-----query_pay_msg0---->" + payType + "----->" + params);
        }
        params = processRequestParams(params);
        String queryOrderId = params.get("orderId");
        Msg msg = new Msg(false, "queryPaymentOrder-queryOrderId:"+queryOrderId+"fail");
        try {
            //查询DB中相关支付信息
            Map<String,PaymentOrder> paymentOrderMap = getPaymentOrderMap(params);
            List<Msg> result = null;
            if(null!=paymentOrderMap && !paymentOrderMap.isEmpty()){
                result = new ArrayList<Msg>();
                for(String orderId: paymentOrderMap.keySet()){
                    PaymentOrder paymentOrder = paymentOrderMap.get(orderId);
                    Msg resTemp = new Msg(false,"");
                    if(null == paymentOrder){
                        resTemp.setData("pay_orderId:"+orderId+" is not found");
                    }else if (PaymentOrderType.SCCUESS.getValue() == paymentOrder.getStatus()) {
                        resTemp.setData("pay_orderId:"+orderId + " status already success");
                    }else{
                    	logger.warn("若订单状态为原始状态，应主动去查询订单状态");
                    }
                    result.add(resTemp);
                }
                msg.setOk(true);
                msg.setData(result);
            }else{
                msg.setData("queryPaymentOrder-queryPayOrderId:"+queryOrderId+" is not found");
            }
        }catch (Exception e){
            logger.error("queryPaymentOrder fail,e:" + e);
        }
        return msg;
    }

    /**
     * 手动处理第三方支付数据（CRM系统使用）
     * @param params
     * @param paOrder
     * @return
     */
    private Msg handleManualPaymentNotify(Map<String, String> params, PaymentOrder paOrder) {
        if(logger.isInfoEnabled ()){
            logger.info("手动处理第三方支付数据参数params："+params+",paOrder="+paOrder.toString());
        }
        Msg msg = new Msg(false,"handleManualPaymentNotify fail");
        try {
            String payOrderId =paOrder.getOrderId();
            double amount = Double.parseDouble(StringUtils.defaultIfEmpty( params.get("thirdTradeAmount"),"0.00"));
            if(amount == paOrder.getAmount()){
                // 更新订单中心订单状态
                PaymentResult paymentResult = new PaymentResult();
                paymentResult.setIsCombined(paOrder.getIsCombined() ? 1 : 0);
                paymentResult.setOrderId(paOrder.getOrderId());
                paymentResult.setTradeStatus(PaymentOrderType.SCCUESS.getValue() + "");
                paymentResult.setThirdTradeNo(params.get("thirdTradeNo"));
                paymentResult.setPrice(paOrder.getAmount());
                paymentResult.setStatus(PaymentOrderType.SCCUESS.getValue());
                updateOrderStatus(paymentResult);
                logger.warn("operatorId:" + params.get("operatorId") + ",update payOrderId:" + payOrderId);
                msg = new Msg(true,"payOrderId:"+payOrderId+" is updated");
            }else{
                msg = new Msg(false,"payOrderId:"+payOrderId+" 支付金额不一致");
            }
        }catch (Exception e){
            logger.error("handleManualPaymentNotify fail,e:" + e);
        }
        return msg;
    }

    /**
     * 保存处理支付状态的信息表
     *
     * @param params
     * @param paOrder
     * @param msg
     */
    private void savePaymentOper(Map<String, String> params, PaymentOrder paOrder,Msg msg) {
        if(logger.isInfoEnabled ()){
            logger.info("保存处理支付状态的信息表参数params："+params+",paOrder="+paOrder.toString()+",msg="+msg);
        }
        try {
            //手工更新更新支付状态操作表
            PaymentOper paymentOper = new PaymentOper();
            paymentOper.setPayId(paOrder.getOrderId());
            paymentOper.setThirdPayType(Integer.parseInt(StringUtils.defaultIfEmpty(params.get("thirdPayType"), "0")));
            paymentOper.setThirdTradeNo(params.get("thirdTradeNo"));
            paymentOper.setThirdTradeAmount(Double.parseDouble(StringUtils.defaultIfEmpty(params.get("thirdTradeAmount"),"0.00")));
            paymentOper.setStatus(PaymentOrderType.SCCUESS.getValue());
            paymentOper.setOperatorId(params.get("operatorId"));
            paymentOper.setOperatorMsg(params.get("operatorMsg"));
            paymentOper.setOperatorType(Integer.parseInt(StringUtils.defaultIfEmpty(params.get("operatorType"), "0")));
            paymentOper.setCreateTime(new Date());
            paymentOper.setMemo(msg.getData().toString());
            iPaymentOperDao.save(paymentOper);
        }catch (Exception e){
            logger.error("updatePaymentStatusOpertaion fail,e:" + e);
        }
    }

    @Override
    public PaymentSource handlePaymentSource(String id) {
        String hql = "from PaymentSource where sourceId=?";
        List list = this.iPayDao.find(hql, Integer.parseInt(id));
        PaymentSource obj = null;
        if (list.size() > 0) {
            obj = (PaymentSource) list.get(0);
        }
        return obj;
    }

    @Override
    public List<PaymentOrder> getOrderDetail(String orderId) {
        String[] orderIds = orderId.split(",");
        String hql = "from PaymentOrder where orderId in (:list)";
        List<PaymentOrder> list = (List<PaymentOrder>) this.iPayDao.findByNamedParam(hql, "list", orderIds);
        return list;
    }

    @Override
    @Cacheable(value = "userCache", key = "#userId")
    public Map<String, List<String>> getBankTypeIndexByUserId(String userId) {

        // 默认排序列表
        List<String> platformInit = iConfigService.getDefaultBankList(Constants.DEFAULT_ZF_PLATFORM);
        List<String> onlineInit = iConfigService.getDefaultBankList(Constants.DEFAULT_PAYONLINE_PLATFORM);
        List<String> fastpayInit = iConfigService.getDefaultBankList(Constants.DEFAULT_FASTPAY_PLATFORM);

        // 统计用户支付信息
        Map<String, List<String>> resultMap = this.iPayDao.getBankTypeIndexByUserId(userId);

        // 组装结果列表
        for (String bankType : platformInit) {
            if (!resultMap.get("platform").contains(bankType)) {
                resultMap.get("platform").add(bankType);
            }
        }
        for (String bankType : onlineInit) {
            if (!resultMap.get("online").contains(bankType)) {
                resultMap.get("online").add(bankType);
            }
        }
        for (String bankType : fastpayInit) {
            if (!resultMap.get("fastpay").contains(bankType)) {
                resultMap.get("fastpay").add(bankType);
            }
        }

        return resultMap;
    }

    /**
     * 根据订单生成需要支付的扫码短连接
     */
    @Override
    public void getShortUrl4Sina(String rootPath, PaymentOrder paymentOrder) {
    	final String orderId = paymentOrder.getOrderId();
		logger.info("准备为订单[{}]生成支付短链接二维码", orderId);
        final String nativeUrl = rootPath
                + "/wap/order.htm?" + paymentOrder.toRequestString()
                + "&payFrom=" + Constants.PAY_FROM_APP_NATIVE
                + "&hmsr=b5mpay&hmmd=qrcode&hmci=apppay&utm_source=b5mpay&utm_medium=qrcode&utm_content=apppay";
        logger.info("为订单[{}]生成支付短链接:[{}]", paymentOrder.getOrderId(), nativeUrl);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                MemCachedUtil.cleanCache(Constants.SHORT_URL_KEY + orderId);
                logger.info("正在为订单[{}]生成支付短链接", orderId);
                getShortUrl4Sina(orderId, nativeUrl);
            }
        });
        MemCachedUtil.setCache(Constants.NATIVE_URL_KEY + orderId, nativeUrl, 60);
    }
    
    /**
     * 调用接口请求获取短连接
     * @param orderId
     * @param nativeUrl
     * @return
     */
    private String getShortUrl4Sina(String orderId, String nativeUrl) {
        try {
            Msg shortUrlMsg = ShortUrlUtil.getShortUrl(nativeUrl);
            if (shortUrlMsg.isOk()) {
                nativeUrl = (String) shortUrlMsg.getData();
            }
            String encrypt = DESEncrypt.encryptBase64String(nativeUrl, "7872018834387a56349a5a8f5b58b91e");
            nativeUrl = Constants.APP_DOMAIN + "/?dest=" + encrypt;
            MemCachedUtil.setCache(Constants.SHORT_URL_KEY + orderId, nativeUrl, 60);
        } catch (Exception e) {
            logger.error("Process app native pay word:" + nativeUrl);
        }
        return nativeUrl;
    }

    /**
     * 验证请求参数是否为空
     * @param params
     * @param keywords
     * @return
     */
    private Msg checkParamsIsNotNull(Map<String, String> params, String[] keywords) {
        Msg res = new Msg(false,"checkParamsIsNotNull fail");
        try {
            String resStr = null;
            StringBuilder resSB = new StringBuilder();
            if (null != params) {
                for (String key : keywords) {
                    if (StringUtils.isBlank(params.get(key))) {
                        resSB.append(key + ",");
                    }
                }
                if (resSB.toString().length() > 0) {
                    resStr = resSB.toString().substring(0, resSB.toString().length() - 1) + " is null";
                }
            } else {
                resStr = "params is null";
            }
            if (null != resStr) {
                res.setData(resStr);
                return res;
            }
        }catch (Exception e){
            logger.error("checkParamsIsNotNull fail,e:" + e);
            return res;
        }
        return null;
    }

    /**
     * 验证手动更新状态签名
     * @param params
     * @return
     */
    private boolean isValidManualSign(Map<String, String> params) {
        try{
            String sign = params.get("sign");
            String operatorId = params.get("operatorId");
            String admin = params.get("admin");
            String resouce = "pay_orderId="+params.get("pay_orderId")+"&thirdPayType="+params.get("thirdPayType")+"&thirdTradeNo="+params.get("thirdTradeNo")
                    +"&thirdTradeAmount="+params.get("thirdTradeAmount")+"&operatorId="+operatorId+"&admin="+admin+"&key="+CRM_UPDATE_STATUS;
            resouce = MD5Util.MD5Encode(resouce,null);
            resouce = MD5Util.MD5Encode(resouce,null);
            String adminSrc = "operatorId="+operatorId+Constants.CRM_UPDATE_STATUS_OPERATION;
            adminSrc = MD5Util.MD5Encode(adminSrc,null);
            if((StringUtils.isNotBlank(sign) && sign.equals(resouce))
                    && (StringUtils.isNotBlank(admin) && admin.equals(adminSrc)) ){
                return true;
            }
        }catch (Exception e){
            logger.error("isValidManualSign fail,e:" + e);
        }
        return false;
    }

    /**
     * 处理请求参数兼容orderId
     * @param params
     * @return
     */
    public Map<String, String> processRequestParams(Map<String, String> params) {
        if(null != params){
            String payId = params.get("pay_orderId");
            String orderId = params.get("orderId");
            if(StringUtils.isBlank(orderId) && StringUtils.isNotBlank(payId)){
                params.put("orderId", payId);
            }
            if(StringUtils.isBlank(payId) && StringUtils.isNotBlank(orderId)){
                params.put("pay_orderId", orderId);
            }
        }
        return params;
    }

    /**
     * 手动更新支付状态并发送支付成功通知
     * @param params
     * @return
     */
    public Msg updateStatusManual(Map<String,String> params){
        if(logger.isInfoEnabled ()){
            logger.info("手动更新支付状态并发送支付成功通知参数params："+params);
        }
        Msg msg = new Msg(false, "updateStatusManual fail");
        try{
            params = processRequestParams(params);
            //检查属性不能为空
            Msg checkParamsMsg = checkParamsIsNotNull(params, new String[]{"pay_orderId", "thirdPayType", "thirdTradeNo", "thirdTradeAmount", "sign", "operatorId", "admin"});
            if(null != checkParamsMsg){
                return checkParamsMsg;
            }
            //检查数据签名
            if(!isValidManualSign(params)){
                return new Msg(false,"request sign error");
            }
            //查询DB中相关支付信息
            Map<String,PaymentOrder> paymentOrderMap = getPaymentOrderMap(params);
            if(null!=paymentOrderMap && !paymentOrderMap.isEmpty()){
                //现默认手动处理状态只有一个订单
                for(String orderId: paymentOrderMap.keySet()){
                    PaymentOrder paymentOrder = paymentOrderMap.get(orderId);
                    if(null == paymentOrder){
                        msg.setData("pay_orderId:"+orderId+" is not found");
                    }else if (PaymentOrderType.SCCUESS.getValue() == paymentOrder.getStatus()) {
                        msg.setData("pay_orderId:"+orderId + " status already success");
                    }else{
                        //手动处理
                        msg = handleManualPaymentNotify(params, paymentOrder);
                        //保存处理支付状态的信息表
                        savePaymentOper(params, paymentOrder, msg);
                    }
                }
            }
        }catch (Exception e){
            logger.error("updateStatusManual fail,e:" + e);
        }
        return msg;
    }

    /**
     * 通过OrderId查询db返回的PaymentOrder对象对应集合
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, PaymentOrder> getPaymentOrderMap(Map<String, String> params) {
        logger.info("-----getPaymentOrderMap----->" + params);
        Map<String, PaymentOrder> paymentOrderMap = null;
        try {
            String orderId = params.get("orderId");
            if (orderId.startsWith(",")) {
                orderId = orderId.substring(1);
            }
            if (orderId.endsWith(",")) {
                orderId = orderId.substring(0, orderId.length() - 1);
            }
            if (orderId != null && orderId.length() > 0) {
                String[] arr = orderId.split(",");
                paymentOrderMap = new HashMap<String, PaymentOrder>();
                for (String tmpOrderId : arr) {
                    params.put("orderId", tmpOrderId);
                    //查询订单状态
                    PaymentOrder paOrder = null;
                    try {
                        List<PaymentOrder> orderList = ((List<PaymentOrder>) (this.iPayDao.find(
                                " from PaymentOrder where orderId = ?  ", tmpOrderId)));
                        if (orderList != null && orderList.size() > 0) {
                            paOrder = orderList.get(0);
                        }
                    } catch (Exception e) {
                        logger.error("getPaymentOrderMap payOrderId: " + tmpOrderId + " found fail,e:"+e);
                    }
                    paymentOrderMap.put(tmpOrderId,paOrder);
                }
            }
        } catch (Exception e) {
            logger.error("getPaymentOrderMap fail,e:"+e);
        }
        return paymentOrderMap;
    }

    /**
     * 获取前台通知页面的请求表单字符串
     * @param paymentOrder
     * @param errorMsg
     * @return
     */
    @Override
    public String getErrorURLRequestForm(PaymentOrder paymentOrder, String errorMsg){
        return PaymentHttpUtils.buildDirectUrl(getReturnUrl(paymentOrder) + "&errorMsg="+errorMsg);
    }

    /**
     * 清空用户渠道缓存信息
     */
    @CacheEvict(value = "userCache", allEntries = true)
    public void cacheUserChooseConfigClean() {
        platformInit = new ArrayList<String>();
        onlineInit = new ArrayList<String>();
        fastpayInit = new ArrayList<String>();
    }

	@Override
	public String standby(Payment... payments) throws B5mException {
		String key = "";
		if(payments != null && payments.length > 0){
			if(logger.isInfoEnabled()){
				logger.info("收到[{}]个准备支付请求", payments.length);
			}
			try {
				for (int i = 0; i < payments.length; i++) {
					Payment payment = payments[i];
					logger.info("支付请求详情:[{}]", payment);
					if(payment != null){
						//校验payment数据
						Assert.hasText(payment.getOrderId(), PayExceptionCode.STANDBY_ORDERID_ERROR);
                        Assert.hasText(payment.getUserId(), PayExceptionCode.STANDBY_USERID_ERROR);
                        Assert.hasText(payment.getSubject(), PayExceptionCode.STANDBY_SUBJECT_ERROR);
                        Assert.isTrue(payment.getAmount() > 0, PayExceptionCode.STANDBY_AMOUNT_ERROR);
                        payment.setTotalAmount(payment.getAmount());//支付缓存信息传过来时将支付金额赋值给订单总金额
                        key = payment.getOrderId(); // 直接返回订单号,后期可升级至返回缓存key
                        if(logger.isInfoEnabled()){
                            logger.info("根据订单号[{}]获取key",key);
                        }
                        if(key.length() > 0){
                            key = createPaymentStandbyKey(key);
                            logger.info("支付请求缓存key:[{}]", key);
                            PaymentOrder cachePayOrder = new PaymentOrder();
                            cachePayOrder.merge(payment);
                            CacheServiceExcutor.put(key, cachePayOrder, 600); // 10分钟自动过期
                        }
					}
				}

			} catch (B5mException e) {
				throw e;
			} catch (Exception e) {
				if(logger.isWarnEnabled()){
					logger.warn("处理准备支付请求失败", e);
				}
				throw new B5mException(PayExceptionCode.STANDBY_PAYMENT_ERROR);
			}
		}
		if(logger.isInfoEnabled()){
			logger.info("支付请求处理完成,返回可用缓存key:[{}]", key);
		}
		return key; 
	}

    @Override
	public Payment getPayment(String key) {
        logger.info("根据订单号[{}]获取key",key);
		Payment payment = null;
        key = createPaymentStandbyKey(key);
        logger.info("根据缓存key[{}]查询支付信息", key);
		if(StringUtils.isNotBlank(key)){
            if(null != CacheServiceExcutor.get(key)){
            	PaymentOrder paymentOrder = (PaymentOrder)CacheServiceExcutor.get(key);
                payment = paymentOrder == null ? null : paymentOrder.toPayment();
                logger.info("支付信息:[{}]", payment);
            }
		}
		return payment;
	}

	@Override
	public boolean mergePayment(PaymentOrder paymentOrder) {
		boolean merged = false;
    	Payment payment = this.getPayment(paymentOrder.getOrderId()); // 根据订单号从缓存中提取支付请求数据
        if(payment != null){
        	paymentOrder.merge(payment); // 使用来自缓存的数据直接进行下一步的业务操作
        	merged = true;
        }
        if(logger.isInfoEnabled()){
            logger.info("是否根据缓存key查询支付信息[{}],缓存对象[{}],更新对象paymentOrder[{}]", new Object[]{merged,payment,paymentOrder});
        }
        return merged;
	}

    /**
     * 取消支付缓存数据
     * @param payIds
     * @return
     */
    @Override
    public void cancelPayment(String ... payIds){
        if(logger.isInfoEnabled()){
            logger.info("调用service层处理cancelPayment method: payIds=[{}]", ArrayUtils.toString(payIds));
        }
        Assert.isTrue(null!=payIds && payIds.length != 0);
        for(String payId : payIds){
            if(logger.isInfoEnabled()){
                logger.info("调用service层处理cancelPayment: payId=[{}]进行删除",payId);
            }
            String key = createPaymentStandbyKey(payId);
            Assert.isTrue(StringUtils.isNotBlank(key));
            CacheServiceExcutor.remove(key);
            if(logger.isInfoEnabled()){
                logger.info("调用service层处理cancelPayment: payId=[{}]删除成功",payId);
            }
        }
    }

    /**
     * 创建支付信息缓存key
     * @param payId
     * @return
     */
    @Override
    public String createPaymentStandbyKey(String payId) {
        if(logger.isInfoEnabled()){
            logger.info("创建支付缓存信息key[{}]", payId);
        }
        if(StringUtils.isNotBlank(payId)){
            if(!(payId.startsWith(Constants.STANDBY_PAY_KEY) && payId.length() > 32)){
                if(logger.isInfoEnabled()){
                    logger.info("创建支付缓存信息key根据订单号[{}]", payId);
                }
                payId = Constants.STANDBY_PAY_KEY + PWCode.getMD5String(payId); // MD5计算
            }
            if(logger.isInfoEnabled()){
                logger.info("最终支付缓存信息key[{}]", payId);
            }
        }
        return payId;
    }

    
    /**
     *  通知内部后台通知系统地址(如stage，prod等)
     * @param params
     * @param channel
     * @return
     */
    public boolean backNotifyInnerAddress(Map<String, String> params, PayChannel channel){
        try{
            String result = "";
            String address = PayChannel.getNotifyAddress(channel);
            if(StringUtils.isBlank(Constants.PAY_BACK_DOMAIN)){ //online环境
                result = HttpClientUtils.get(Constants.PROD_PAY_DOMAIN + address, params);
                logger.info("online goto prod result1 --- " + result);
                result =  HttpClientUtils.get(Constants.STAGE_PAY_DOMAIN + address, params);
                logger.info("online goto stage result2 --- " + result);
            }else if(Constants.PAY_BACK_DOMAIN.contains("prod")) { //prod环境
                result =  HttpClientUtils.get(Constants.STAGE_PAY_DOMAIN + address, params);
                logger.info("prod goto stage result --- " + result);
            }else {
				logger.info("来自[{}]的无法转发的可疑订单回调:[{}]", channel, params);
			}
        }catch (Exception e){
            logger.error("notifyInnerAddress error:"+e);
            return false;
        }
        return true;
    }
    
}
