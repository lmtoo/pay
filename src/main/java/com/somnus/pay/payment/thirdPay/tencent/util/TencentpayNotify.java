package com.somnus.pay.payment.thirdPay.tencent.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 *类名：TencentpayNotify
 *功能：财付通支付通知（后台通知）示例，商户按照此文档进行开发即可
 */
public class TencentpayNotify {
    private HttpServletRequest  request;
    private HttpServletResponse response;
    protected Log               logger = LogFactory.getLog(TencentpayNotify.class);

    public TencentpayNotify(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public boolean verifyThirdPayResponse() throws Exception {
        ResponseHandler resHandler = new ResponseHandler(request, response);
        resHandler.setKey(TencentConstants.PAY_KEY);

        // 判断签名
        if (resHandler.isTenpaySign()) {
            // 通知id
            String notify_id = resHandler.getParameter("notify_id");
            // 创建请求对象
            RequestHandler queryReq = new RequestHandler(null, null);
            // 通信对象
            TenpayHttpClient httpClient = new TenpayHttpClient();
            // 应答对象
            ClientResponseHandler queryRes = new ClientResponseHandler();

            // 通过通知ID查询，确保通知来至财付通
            queryReq.init();
            queryReq.setKey(TencentConstants.PAY_KEY);
            queryReq.setGateUrl(TencentConstants.PAY_URL_VERIFY);
            queryReq.setParameter("partner", TencentConstants.PAY_PARTNER_ID);
            queryReq.setParameter("notify_id", notify_id);

            // 通信对象
            httpClient.setTimeOut(5);
            // 设置请求内容
            httpClient.setReqContent(queryReq.getRequestURL());
            logger.warn("queryReq:" + queryReq.getRequestURL());
            // 后台调用
            if (httpClient.call()) {
                // 设置结果参数
                queryRes.setContent(httpClient.getResContent());
                logger.warn("queryRes:" + httpClient.getResContent());
                queryRes.setKey(TencentConstants.PAY_KEY);

                // 获取返回参数
                String retcode = queryRes.getParameter("retcode");
                String trade_state = resHandler.getParameter("trade_state");

                String trade_mode = resHandler.getParameter("trade_mode");

                // 判断签名及结果
                if (queryRes.isTenpaySign() && "0".equals(retcode) && "0".equals(trade_state)
                    && "1".equals(trade_mode)) {
                    logger.warn("订单查询成功");
                    // 取结果参数做业务处理
                    logger.warn("out_trade_no:" + queryRes.getParameter("out_trade_no")
                                + " transaction_id:" + queryRes.getParameter("transaction_id"));
                    logger.warn("trade_state:" + queryRes.getParameter("trade_state")
                                + " total_fee:" + queryRes.getParameter("total_fee"));
                    // 如果有使用折扣券，discount有值，total_fee+discount=原请求的total_fee
                    logger.warn("discount:" + queryRes.getParameter("discount") + " time_end:"
                                + queryRes.getParameter("time_end"));
                    // ------------------------------
                    // 处理业务开始
                    // ------------------------------

                    // 处理数据库逻辑
                    // 注意交易单不要重复处理
                    // 注意判断返回金额

                    // ------------------------------
                    // 处理业务完毕
                    // ------------------------------
                    resHandler.sendToCFT("Success");
                    return true;
                } else {
                    // 错误时，返回结果未签名，记录retcode、retmsg看失败详情。
                    logger.warn("查询验证签名失败或业务错误");
                    logger.warn("retcode:" + queryRes.getParameter("retcode") + " retmsg:"
                                + queryRes.getParameter("retmsg"));
                    return false;
                }

            } else {
                logger.warn("后台调用通信失败");
                logger.warn(httpClient.getResponseCode());
                logger.warn(httpClient.getErrInfo());
                // 有可能因为网络原因，请求已经处理，但未收到应答。
                return false;
            }
        } else {
            logger.warn("通知签名验证失败");
            return false;
        }
    }
}
