package com.somnus.pay.payment.web.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.Msg;
import com.somnus.pay.payment.service.IPayService;

/**
 * User: jianyuanyang
 * Date: 14-3-25
 * Time: 上午10:33
 */
@Controller
@RequestMapping("order")
public class OrderController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
	
    @Autowired
	private IPayService iPayService;
    
    @RequestMapping("/details")
    @JsonResult(desc = "查看订单详情")
    public Msg getOrderDetail(String orderId,Integer source){
    	LOGGER.info("Process getOrderDetail method: orderId={} , source={}", orderId, source);
        return iPayService.getOrderDetails(orderId,source);
    }
    
    @RequestMapping("/wxresult")
    public String resultPage(HttpServletRequest request, Model model) {
        String orderId = request.getParameter("orderId");
        String returnUrl = iPayService.getReturnUrl(orderId);
        return "redirect:"+returnUrl;
    }

    /**
     * 提供给CRM手动更新支付状态信息并通知订单中心
     * @param request
     * @return
     */
    @RequestMapping(value = "updateStatusManual")
    @JsonResult(desc = "提供给CRM手动更新支付状态信息并通知订单中心")
    public Msg updateStatusManual(HttpServletRequest request) {
        Map<String, String> params =requestParams2Map(request);
        logger.info("Process updateStatusManual method: params={}", params);
        return iPayService.updateStatusManual(params);
    }

    /**
     * <pre>
     * 从第三方支付系统查询订单
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "query")
    @JsonResult(desc = "从第三方支付系统查询订单")
    public Msg query(HttpServletRequest request) {
        Map<String, String> params =requestParams2Map(request);
        logger.info("Process order_query method: params={}", params);
        return iPayService.queryPaymentOrder(params, PayChannel.getPayType(Integer.parseInt(params.get("thirdPayType"))));
    }

    /**
     * 清空支付缓存数据
     * @param payIds
     * @return
     */
    @RequestMapping(value = "cancelPayment")
    @JsonResult(desc = "取消订单支付")
    public ActionResult<Boolean> cancelPayment(String... payIds) {
        if(logger.isInfoEnabled()){
        	logger.info("Process cancelPayment method: payIds={}", Arrays.deepToString(payIds));
        }
        iPayService.cancelPayment(payIds);
        return new ActionResult<Boolean>(StatusCode.SUCCESS, true);
    }


}
