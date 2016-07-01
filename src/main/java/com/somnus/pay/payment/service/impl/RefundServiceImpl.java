package com.somnus.pay.payment.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.payment.dao.IPayDao;
import com.somnus.pay.payment.dao.IPayRefundDao;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.enums.RefundType;
import com.somnus.pay.payment.pojo.Msg;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentRefund;
import com.somnus.pay.payment.service.IRefundService;
import com.somnus.pay.payment.service.PaymentChannelService;
import com.somnus.pay.payment.util.MetaqUtil;
import com.somnus.pay.payment.util.PaySign;

@Service
@Transactional
public class RefundServiceImpl implements IRefundService{
	
	protected Log logger = LogFactory.getLog(RefundServiceImpl.class);
	@Resource
	private IPayDao iPayDao;
	@Resource
	private IPayRefundDao iPayRefundDao;
	@Autowired
	private PaymentChannelService paymentChannelService;
	@Autowired
	private MetaqUtil metaqUtil;
	
	private static final String key = "b5mrefund_key7872018834387a56884586fdfdfdsfcetpay";
	public static final String DOUBLE_FORMAT = "0.00";
	private ExecutorService executorService = Executors.newFixedThreadPool(5);

	@SuppressWarnings("unchecked")
	@Override
	public Msg verifyRefund(Map<String, String> requestParamsMap,PayChannel payType) {
		if(logger.isInfoEnabled()){
			logger.info("Process verifyRefund method: requestParamsMap=" + requestParamsMap + ",payType="+payType.toString());
		}
		Msg verifyMsg = new Msg(false,"error");		
		requestParamsMap.put("orderId", requestParamsMap.get("pay_orderId"));
		requestParamsMap.put("memo", "b5m_tuikuan");
		String orderNum = requestParamsMap.get("orderNum");
		String orderId = requestParamsMap.get("orderId"); // 订单系统的 订单号
		String thirdPayType = requestParamsMap.get("thirdPayType");
		String refund_amount = requestParamsMap.get("refund_amount");
		String operatorId = requestParamsMap.get("operatorId");		
		String memo = requestParamsMap.get("memo");		
		String sign =requestParamsMap.get("sign");
		// 校验参数
		if (StringUtils.isBlank(orderId) || StringUtils.isBlank(thirdPayType) || StringUtils.isBlank(refund_amount) || StringUtils.isBlank(memo) || StringUtils.isBlank(sign) || StringUtils.isBlank(orderNum)) {
			verifyMsg.setData("想退款,传参数啊！");
			return verifyMsg;
		}
		logger.info("refund >> verifyRefund  param is :" + requestParamsMap.toString());
		try {
			String signStr = "orderNum" + orderNum + "pay_orderId=" + orderId + "thirdPayType=" + thirdPayType + "refund_amount=" + refund_amount + "key=" + key;
			String sign_after = PaySign.sign(signStr, null);
			//签名运算校验
			if(!sign_after.equals(sign)){
				verifyMsg.setData("哈！签名不对");
				logger.warn("refund >> verifyRefund  sign is error :" + sign_after + "  >> " + sign);
				return verifyMsg;
			}	
			Double amount  = Double.parseDouble(refund_amount);
			//校验参数格式			
			if (amount <= 0) {
				verifyMsg.setData("退款金额无效，重新再来！");
				return verifyMsg;
			}			
			
			//验证订单  订单是否存在| 订单是否支付 | 校验订单支付渠道类型
			try {
				List<PaymentOrder> orderList = ((List<PaymentOrder>) (this.iPayDao.find(" from PaymentOrder where orderId = ?  ", orderId)));
				if (orderList != null && orderList.size() > 0) {
					for (PaymentOrder order : orderList) {
						// 订单是否支付
						if (order.getStatus().equals(PaymentOrderType.SCCUESS.getValue())) {
							// 判断当前支付类型
							if (PayChannel.getPayType(order.getThirdPayType()) != payType) {
								verifyMsg.setData("[" + order.getOrderId() + "] 支付渠道不正确");
								return verifyMsg;
							}else{
								// 校验退款订单号
								if(StringUtils.isBlank(order.getThirdTradeNo())){
									verifyMsg.setData("支付流水号为空，对,就是你还没付款啊 ");
									return verifyMsg;
								}
								// 判断退款额度
								if(amount > order.getAmount()){
									verifyMsg.setData("想多退款么？我也想！");
									return verifyMsg;
								}								
								// 查询是否已经申请退款
								PaymentRefund paymentRefund = getPaymentRefund(orderId, refund_amount, thirdPayType, orderNum);										
								if (paymentRefund == null) {
									paymentRefund = new PaymentRefund();
									// 校验通过 插入数据库保存
									verifyMsg = new Msg(true, order.getThirdTradeNo() + "#" + order.getAmount());
									paymentRefund.setOperatorId(operatorId);
									paymentRefund.setOrderId(orderId);
									paymentRefund.setOrderNum(orderNum);
									paymentRefund.setRefundAmount(Double.parseDouble(refund_amount));
									paymentRefund.setThirdTradeNo(order.getThirdTradeNo());
									paymentRefund.setRefundStatus(0);
									paymentRefund.setThirdPayType(Integer.parseInt(thirdPayType));
									Date date = new Date();
									paymentRefund.setCreateTime(date);
									paymentRefund.setUpdateTime(date);
									iPayRefundDao.save(paymentRefund);
								} else if (paymentRefund.getRefundStatus() == RefundType.NOTDONE.getValue() || paymentRefund.getRefundStatus() == RefundType.FAIL.getValue()) {
									verifyMsg = new Msg(true, order.getThirdTradeNo() + "#" + order.getAmount());
								} else{
									verifyMsg.setData("[" + order.getOrderId() + "] " + RefundType.getMeassage(paymentRefund.getRefundStatus()));
								}
								return verifyMsg;
							}
						} else {
							verifyMsg.setData("[" + order.getOrderId() + "] 该订单未支付");
						}
					}
				}else{
					verifyMsg.setData("[" + orderId + "] 该订单不存在");
				}
            } catch (DataAccessException e) {            	
				if (!verifyMsg.isOk()) {
					logger.warn("refund >> verifyRefund  DB is error " + e);
					verifyMsg.setData(orderId + "查询订单 失败 ");
					return verifyMsg;
				}
				verifyMsg.setData("查询订单存在  但可能你上次提交退款 没执行完整");
				verifyMsg.setOk(false);
				logger.warn("refund >> verifyRefund  DB is error " + e);		
				e.printStackTrace();
            }			
		} catch (Exception e) {
			verifyMsg.setOk(false);
			verifyMsg.setData("系统错误。。。。打回 重新来");
			logger.warn("refund >> verifyRefund system_error");
		}
		return verifyMsg;
	}
	
	/**
	 * 提交退款请求
	 */
	@Override
	public String refundSubmit(Map<String, String> requestParamsMap, PayChannel channel) {
		if(logger.isInfoEnabled()){
			logger.info("Process refundSubmit method: requestParamsMap=" + requestParamsMap + ",payType="+channel.toString());
		}
		String thirdTradeNo = requestParamsMap.get("thirdTradeNo");
		String refund_amount = requestParamsMap.get("refund_amount");
		String memo = requestParamsMap.get("memo");		
		if (StringUtils.isBlank(thirdTradeNo) || StringUtils.isBlank(refund_amount) || StringUtils.isBlank(memo)) {
			return "退款 提交参数为空！";
		}
		memo = memo.replace("^", "").replace("|", "").replace("$", "").replace("^", "").replace("#", "");
		refund_amount = getDoubleFormat(Double.parseDouble(refund_amount));
		String resultForm = (String) paymentChannelService.handleRefund(channel, requestParamsMap);
		logger.warn("refund >> refundSubmit : partamer:" + requestParamsMap.toString() + "  request_result :" + resultForm);
		return resultForm;
	}
	
	/**
	 * 异步发送mq消息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void syncSendToMetaq(String orderNum,String thirdTradeNo, String refundAmount, Integer refund_status, int thirdPayType, String msg) {
		if(logger.isInfoEnabled()){
			logger.info("refund >> syncSendToMetaq update status comie in orderNum:"
					+ orderNum + " thirdTradeNo:" + thirdTradeNo + " refund_status:" + refund_status
					+ " thirdPayType:" + thirdPayType + " msg:" + msg);
		}
		if(StringUtils.isNotBlank(thirdTradeNo) && StringUtils.isNotBlank(refundAmount)){
			try {
				PaymentRefund paymentRefund = new PaymentRefund();
				List<PaymentRefund> refundList = (List<PaymentRefund>) (this.iPayRefundDao.find(" from PaymentRefund where third_trade_no = ?  and refund_amount = ? and  thirdPayType = ? and order_num = ? ", thirdTradeNo, refundAmount,thirdPayType, orderNum));		
				if (refundList != null && refundList.size() > 0) {
					paymentRefund = refundList.get(0);
					if(paymentRefund.getRefundStatus() != RefundType.SCCUESS.getValue()){
						Date date = new Date();
						paymentRefund.setUpdateTime(date);
						paymentRefund.setRefundStatus(refund_status);
						paymentRefund.setRefundMsg(msg);
						iPayRefundDao.saveOrUpdate(paymentRefund);
						logger.warn("refund >> syncSendToMetaq update status paymentRefund:" + paymentRefund.toString());	
					}else{
						logger.warn("refund >> syncSendToMetaq this refund is success paymentRefund:" + paymentRefund.toString());	
					}				
				}
				final JSONObject json = new JSONObject();
				json.put("orderNum", orderNum);
				json.put("orderId", paymentRefund.getOrderId());
				json.put("pay_orderId", paymentRefund.getOrderId());
				json.put("refund_amount", refundAmount);
				json.put("thirdPayType", thirdPayType);	
				json.put("operatorId", paymentRefund.getOperatorId());	
				json.put("refund_time", paymentRefund.getUpdateTime());
				json.put("refund_status", refund_status);
				json.put("refund_msg", msg);
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						boolean isSend = metaqUtil.send(json);
						logger.warn("refund >> syncSendToMetaq send to mq json :" + json.toJSONString() + " isSend_status:" + isSend);	
					}
				});
			} catch (Exception e) {
				logger.warn("refund >> syncSendToMetaq  is select DB error " );	
			}
			
		}else{
			logger.warn("refund >> syncSendToMetaq  paymentRefund  param is null ");		
		}		
	}
	
	/**
	 * double类型转string
	 * @param amount
	 * @return
	 */
    private static String getDoubleFormat(Double amount) {
        DecimalFormat df = new DecimalFormat(DOUBLE_FORMAT);
        return df.format(amount);
    }

    private PaymentRefund getPaymentRefund(String orderId,String refundAmount,String thirdPayType,String orderNum){
    	String hql = " from PaymentRefund where thirdPayType= "+ thirdPayType + " and refundAmount =" + refundAmount +" and orderId ='" + orderId + "' and orderNum ='" + orderNum +"'";
		try {
			Query query = this.iPayRefundDao.getSession().createQuery(hql);
			PaymentRefund paymentRefund = (PaymentRefund) query.uniqueResult();
			return paymentRefund;
		} catch (Exception e) {
			logger.warn("refund >> getPaymentRefund  DB is error ");
		} finally {
			this.iPayRefundDao.getSession().close();
		}    	
		return null ;    	
    }
}
