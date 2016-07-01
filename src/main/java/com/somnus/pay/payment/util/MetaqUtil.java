package com.somnus.pay.payment.util;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.taobao.metamorphosis.Message;
import com.taobao.metamorphosis.client.producer.MessageProducer;
import com.taobao.metamorphosis.client.producer.SendResult;

import java.util.Map;

/**
 * mq消息队列发送工具类
 * @author mike
 *
 */
@Service
public class MetaqUtil{

    private static final Logger log = LoggerFactory.getLogger(MetaqUtil.class);
    
    @Autowired
    private MessageProducer messageProducer;
    
    // 退款支付状态变更消息的TOPIC
    private @Value("#{configProperties['metaq_topic_pay_oper_refund']}") String metaqTopicOrderStatusChange;
    // 支付回调后支付状态变更消息的TOPIC
    private @Value("#{configProperties['metaq_topic_pay_oper_order']}") String metaqTopicPayOperOrder;

    /**
     * 同步退款状态消息
     * @param json
     * @return
     */
	public boolean send(JSONObject json) {
        if(StringUtils.isBlank(metaqTopicOrderStatusChange)){
            metaqTopicOrderStatusChange = ConfigUtil.getValue("config/config", "metaq_topic_pay_oper_refund");
        }
        return send(json,metaqTopicOrderStatusChange);
	}

    /**
     * 发送Metaq消息
     * @param json 消息体
     * @param messageTicp 消息队列名
     * @return
     */
    public boolean send(JSONObject json,String messageTicp) {
        String msgJSONStr = null;
        if(null!=json){
            msgJSONStr = json.toJSONString();
        }
        return send(msgJSONStr,messageTicp);
    }

    /**
     * 发送Metaq消息
     * @param msgJSONStr 消息
     * @param messageTicp 消息队列名
     * @return
     */
    public boolean send(String msgJSONStr,String messageTicp) {
        try {
            if(StringUtils.isNotBlank(msgJSONStr) && StringUtils.isNotEmpty(messageTicp)){
                messageProducer.publish(messageTicp);
                SendResult sendResult = messageProducer.sendMessage(new Message(messageTicp, msgJSONStr.getBytes()));
                if (sendResult.isSuccess()) {
                    log.info("发送状态通知到metaq success:{}", msgJSONStr);
                } else {
                    sendResult = messageProducer.sendMessage(new Message(messageTicp, msgJSONStr.getBytes()));
                    log.warn("messageTicp:" + messageTicp + "发送状态通知到metaq error:" + msgJSONStr);
                }
                return sendResult.isSuccess();
            }
        } catch (B5mException e) {
            log.error("messageTicp:"+messageTicp+"发送状态通知到metaq error1:" + msgJSONStr, e);
            throw e;
        } catch (Exception e) {
            log.error("messageTicp:"+messageTicp+"发送状态通知到metaq error2:" + msgJSONStr, e);
            throw new PayException(PayExceptionCode.METAQ_SERVICE_ERROR, e);
        }
        return false;
    }

    /**
     * Map转换Json，发送给相应MetaQ
     * @param paramsMap
     * @param metaqTopic
     * @return
     */
    public boolean send(Map<String, Object> paramsMap, String metaqTopic) {
        final JSONObject json = new JSONObject();
        json.putAll(paramsMap);
        return send(json,metaqTopic);
    }

    /**
     * 只触发一次MetaQ请求
     * @param msgJSONStr
     * @param messageTicp
     */
    public boolean sendOnce(String msgJSONStr, String messageTicp) {
        boolean res = false;
        try {
            if(StringUtils.isNotBlank(msgJSONStr) && StringUtils.isNotEmpty(messageTicp)){
                messageProducer.publish(messageTicp);
                SendResult sendResult = messageProducer.sendMessage(new Message(messageTicp, msgJSONStr.getBytes()));
                res = sendResult.isSuccess();
                if(!res){
                    log.warn("messageTicp:"+messageTicp+"发送状态通知到metaq :" + msgJSONStr+" fail.");
                }
            }
        } catch (Exception e) {
            log.error("messageTicp:"+messageTicp+"发送状态通知到metaq processSendError:" + msgJSONStr +",e:"+ e);
        }
        return res;
    }
}
