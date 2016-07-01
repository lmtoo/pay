package com.somnus.pay.payment.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somnus.pay.payment.util.PageCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.Msg;
import com.somnus.pay.payment.pojo.PaymentSource;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.payment.thirdPay.tencent.util.TenpayUtil;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.FuncUtils;
import com.somnus.pay.payment.util.MemCachedUtil;

/**
 * WEB 支付 Controller
 * 
 * @author 丹青生
 *
 * @date 2015-10-27
 */
@Controller
@RequestMapping("third")
public class PayController extends BaseController {

    protected static Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private IPayService                                                     iPayService;
    @Autowired
    private IConfigService configService;

    private @Value("#{configProperties['staticPath']}") String              staticPath;
    private @Value("#{configProperties['staticWebPath']}") String           staticWebPath;
    private @Value("#{configProperties['NEW_RESULT_PATH']}") String  newResultPath;
    private @Value("#{configProperties['www.server']}") String  b5mPath;
    private @Value("#{configProperties['cdnTJPath']}") String  cdnTJPath;

    /**
     * PC端微信扫码支付页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "getShortUrl", method = RequestMethod.POST)
    public @ResponseBody String getShortUrl(String orderId) {
        if(logger.isInfoEnabled()){
            logger.info("链接转换成短码链接请求:{}", orderId);
        }
        String shortUrl = null;
        for (int i = 0; i < 20; i++) {
        	try {
        		shortUrl =  MemCachedUtil.getCache(Constants.SHORT_URL_KEY + orderId) + "";
        		if(StringUtils.isNotBlank(shortUrl)){
        			break;
        		}
        		Thread.sleep(500);
        	} catch (Exception e) {
        		logger.error("短码链接转换错误", e);
        	}
        }
        if(logger.isInfoEnabled()){
            logger.info("短码链接:{}", shortUrl);
        }
        return shortUrl;
    }

    /**
     * PC端微信扫码页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("wxcode")
    public String wxPay(HttpServletRequest request, Model model) {
        String wxcode = request.getParameter("code_url");
        String orderId = request.getParameter("orderId");
        model.addAttribute("wxcode", wxcode);
        model.addAttribute("orderId", orderId);
        model.addAttribute("finalAmount", request.getParameter("finalAmount"));
        model.addAttribute("rootPath", PageCommonUtil.getRootPath(request, false));
        model.addAttribute("staticPath", staticPath);
        model.addAttribute("staticWebPath", staticWebPath);
        model.addAttribute("cdnTJPath", cdnTJPath);
        return "/pay/wxcode";
    }

    /**
     * <pre>
     * 从第三方支付系统查询订单
     * </pre>
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "query")
    public @ResponseBody String queryPay(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, String> params = requestParams2Map(request);
        if(logger.isInfoEnabled()){
            logger.info("Process queryPay method star" + params);
        }

        Msg msg = null;
        try {
            msg = iPayService.queryPaymentOrder(params, PayChannel.getPayType(Integer.parseInt(params.get("thirdPayType"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(logger.isInfoEnabled()){
            logger.info("Process queryPay method end" + msg);
        }
        return msg.toString();
    }

    /**
     * 查询sourceId对应的sourceKey信息
     * @param request "sourceId" 
     * @param response
     * @return getChannel
     */
    @RequestMapping(value = "sourceKey")
    public @ResponseBody String getSourceKey(HttpServletRequest request,
                                             HttpServletResponse response, Model model) {
        Map<String, String> params = requestParams2Map(request);
        if(logger.isInfoEnabled()){
            logger.info("---getSourceKey--->" + params);
        }

        PaymentSource obj = null;
        try {
            obj = iPayService.handlePaymentSource(params.get("sourceId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(logger.isInfoEnabled()){
            logger.info("---getSourceKey--->" + obj);
        }

        if (obj != null) {
            return obj.getSourceKey();
        }
        return null;
    }

    @RequestMapping("error")
    public String errorPage(HttpServletRequest request) {
        if(logger.isInfoEnabled()){
            logger.info("----pay_error_page,request from--->" + request.getRemoteAddr() +",realIp:"+ FuncUtils.getIpAddr(request)+ "---->" + TenpayUtil.getCurrTime());
        }
        return "redirect:"+b5mPath;
    }

    @RequestMapping("result")
    public String resultPage(Msg msg, HttpServletRequest request, Model model) {
        model.addAttribute("msg", msg);
        model.addAttribute("today", System.currentTimeMillis());
        model.addAttribute("rootPath", PageCommonUtil.getRootPath(request, false));
        model.addAttribute("staticPath", staticPath);
        model.addAttribute("staticWebPath", staticWebPath);
        model.addAttribute("returnUrl", configService.getPaymentSourceUrlById(request.getParameter("source")));
        model.addAttribute("cdnTJPath", cdnTJPath);
        return "redirect:"+newResultPath;
    }

    /**
     * <pre>
     * 获取头部
     * </pre>
     *
     * @return
     */
    public static JSONObject getPageModuledule() {
        JSONObject res = null;
        try {
            Object headObject = MemCachedUtil.getCache(Constants.MEMCACHE_PAGE_HEAD_MODEL);
            if (null == headObject) {
                res = getPageModuleSource();
                if(null != res){
                    MemCachedUtil.setCache(Constants.MEMCACHE_PAGE_HEAD_MODEL, res);
                }
            }else {
                return (JSONObject) headObject;
            }
        } catch (Exception e) {
            logger.error("getPageModuledule exception. e:[{}]", e);
            res = getPageModuleSource();
        }
        if(null == res){
            res = new JSONObject();
        }
        return res;
    }

    /**
     * 获取头部信息JSONObject
     * @return
     */
    public static JSONObject getPageModuleSource() {
        try {
            String www_server_domain = Constants.B5M_DOMAIN;
            String result = HttpClientUtils.get(www_server_domain + "/modules/combinepublic?top_search_id=200022");
            JSONObject json = JSON.parseObject(result);
            if (json.getString("error").equals("false")) {
                MemCachedUtil.setCache(Constants.MEMCACHE_PAGE_HEAD_MODEL, json.getJSONObject("msg"));
                return json.getJSONObject("msg");
            }
        } catch (Exception e) {
            logger.error("getPageModuleSource exception. e:[{}]", e);
        }
        return null;
    }
    
    /**
     * <pre>
     * 刷新头部
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("refleshHead")
    public @ResponseBody String refleshHead(HttpServletRequest request) {
        String res = "refleshHead seccuss.";
        try{
            MemCachedUtil.cleanCache(Constants.MEMCACHE_PAGE_HEAD_MODEL);
        }catch (Exception e) {
            res = "refleshHead exception.";
        }
        return res;
    }
}
