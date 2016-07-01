package com.somnus.pay.payment.web.controller;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.somnus.pay.payment.util.PageCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.UAType;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.thirdPay.tencent.config.WxConfig;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.RequestParameterUtil;

/**
 * @description: WAP渠道支付流程控制器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-11-27
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-1       丹青生                               1.0            初始化
 */
@Controller
@RequestMapping("wap")
public class WapPayController extends PaymentController {

	protected Logger logger = LoggerFactory.getLogger(WapPayController.class);

    @Value("#{configProperties['URL_VIEW_PAY_ERROR_0']}")
    private String createOrderFailedPage;
    @Value("#{configProperties['URL_VIEW_WAP_ORDER_0']}")
    private String createOrderSuccessPage;
	@Value("#{configProperties['staticPath']}")
	private String mobilePath;
	@Value("#{configProperties['koreaPath']}")
	private String koreaPath;
	@Value("#{configProperties['mobileCartPath']}")
	private String mobileCartPath;

	@Override
	protected String getCreateOrderFailedPage() {
		return createOrderFailedPage;
	}

	@Override
	protected String getCreateOrderSuccessPage(PaymentOrder paymentOrder, Model model) {
		return createOrderSuccessPage;
	}

	@Override
	protected void createOrder(PaymentOrder paymentOrder, HttpServletRequest request, Model model) {
		super.createOrder(paymentOrder, request, model);
		String ua = request.getHeader("User-Agent");
		model.addAttribute("ua", ua);
        Integer uaType = UAType.getUAType(ua);
        model.addAttribute("uaType", uaType);
        model.addAttribute("isAPP", UAType.isAPPType(uaType));
        model.addAttribute("appVersion", RequestParameterUtil.getValueFromUA(ua, "v"));
        model.addAttribute("isWeiXin", UAType.WX_BROWSER.getValue().equals(uaType));
        model.addAttribute("isKoreaAPP", UAType.KOREA_APP.getValue().equals(uaType));
        model.addAttribute("mobilePath", mobilePath);
        model.addAttribute("koreaPath", koreaPath);
        model.addAttribute("mobileCartPath", mobileCartPath);
        logger.info("正在根据UA[{}]类型为订单[{}]生成对应渠道列表", uaType, paymentOrder.getOrderId());
        model.addAttribute("wapPlatform", configService.getWapBankList(uaType, paymentOrder));
        model.addAttribute("environmentJSVar",Constants.ENVIRONMENT_JS_VAR);
        if(PayChannel.valueOf(paymentOrder.getThirdPayType()) == PayChannel.WxWapPay 
        		&& StringUtils.isNotBlank((paymentOrder.getCode()))){ // 获取微信支付code以及openid 存入cookie
        	String cookieStr =(String) request.getSession().getAttribute(WxConfig.COOKIE_WX_CODE);
    		if(StringUtils.isNotBlank(cookieStr)){
    			String []codes = new String[]{"",""};
    			codes = cookieStr.split("_");
    			if(StringUtils.isNotBlank(codes[1]) && paymentOrder !=null && StringUtils.isNotBlank(paymentOrder.getCode())){
    				long timestamp = Long.parseLong(codes[1]);
  					if (System.currentTimeMillis() > timestamp) {
  						String expires_in = request.getParameter("expires_in");
  						if (StringUtils.isNotBlank(expires_in) && StringUtils.isNumeric(expires_in)) {
  							timestamp = System.currentTimeMillis() + Long.parseLong(expires_in) * 1000;
  							request.getSession().setAttribute(WxConfig.COOKIE_WX_CODE, paymentOrder.getCode() + "_" + timestamp);
  						}
  					}
    			}	
    		}       	
        }
	}
	
	@Override
	protected String doPay(PaymentOrder paymentOrder, HttpServletRequest request, HttpServletResponse response, Model model) {
		logger.info("为订单[{}]执行支付动作", paymentOrder.getOrderId());
		if(PayChannel.WxWapPay.getValue().equals(paymentOrder.getThirdPayType()) 
				&& StringUtils.isBlank(paymentOrder.getCode())){
            return wxCode(paymentOrder, request, response);
        }else{
            String ua = request.getHeader("User-Agent").toLowerCase();
            paymentOrder.setImei(RequestParameterUtil.getValueFromUA(ua, "did"));
            return paymentService.pay(paymentOrder);
        }
	}
	
	@RequestMapping(value = "wx/code")
    public @ResponseBody String wxCode(@Valid PaymentOrder paymentOrder,HttpServletRequest request, HttpServletResponse response) {
        if(logger.isInfoEnabled()){
            logger.info("Process wxCode method: paymentOrder=" + paymentOrder);
        }
        boolean isRquest = true;
		String code = "";
		String cookieStr =(String) request.getSession().getAttribute(WxConfig.COOKIE_WX_CODE);
		if(StringUtils.isNotBlank(cookieStr)){
			String []codes = new String[]{"",""};
			codes = cookieStr.split("_");
			if(StringUtils.isNotBlank(codes[1]) && paymentOrder !=null && StringUtils.isNotBlank(paymentOrder.getCode())){
				long timestamp = Long.parseLong(codes[1]);
				if(System.currentTimeMillis() < timestamp){
					isRquest = false;
					code = codes[0];
				}else{
					request.getSession().removeAttribute(WxConfig.COOKIE_WX_CODE);
					request.getSession().removeAttribute( WxConfig.COOKIE_WX_OPENID);
				}
			}	
		}
		if(isRquest){
			request.getSession().removeAttribute(WxConfig.COOKIE_WX_CODE);
			request.getSession().removeAttribute(WxConfig.COOKIE_WX_OPENID);
			// 跳转到订单页面   "/wap/pay.htm?"
			String redirect_uri = PageCommonUtil.getRootPath(request, true) + "/wap/order.htm?" + paymentOrder.toRequestString();
			StringBuffer sb = new StringBuffer();
			sb.append("?appid=" + WxConfig.APPID);
			try {
				sb.append("&redirect_uri=" +  URLEncoder.encode(redirect_uri, "UTF-8"));
			} catch (Exception e) {
				sb.append("&redirect_uri=" + redirect_uri);
				logger.warn("URL转码错误", e);
			}
			sb.append("&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
			return WxConfig.CODE_URL + sb.toString();
		}
		return "ok#" + code;
    }

}
