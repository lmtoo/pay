package com.somnus.pay.payment.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.Msg;
import com.somnus.pay.payment.service.IRefundService;
@Controller
@RequestMapping("refund")
public class RefundController extends BaseController {
	
	protected Log logger = LogFactory.getLog(RefundController.class);
	
	@Autowired
	private IRefundService iRefundService;
    /**
     * 退款操作接口
     * @param request
     * @return
     */
	@RequestMapping(value = "operRefund" , produces = "text/html;charset=UTF-8")
	public @ResponseBody String operRefund(HttpServletRequest request) {
		Map<String, String> requestParamsMap = requestParams2Map(request);
		if(logger.isInfoEnabled()){
			logger.info("Process operRefund method: params=" + requestParamsMap);
		}
		String thirdPayType = requestParamsMap.get("thirdPayType");
		if (StringUtils.isBlank(thirdPayType) || !StringUtils.isNumeric(thirdPayType)) {
			return " 校验失败 thirdPayType参数错误";
		}
		if (Integer.parseInt(thirdPayType) == 9) {
			return "暂不支持 帮钻退款";
		}
		PayChannel payType = PayChannel.getPayType(Integer.parseInt(thirdPayType));
		Msg msg = iRefundService.verifyRefund(requestParamsMap, payType);
		if(logger.isInfoEnabled()){
			logger.info("operRefund >> verifyRefund: " + requestParamsMap.toString() + " " + msg.toString());
		}

		if(!msg.isOk()){
			return msg.toString();
		}
		String result = "也许你需要再来一次!";
		String data = (String)msg.getData();
		if(StringUtils.isNotBlank(data)){
			String [] s = data.split("#");
			requestParamsMap.put("thirdTradeNo", s[0]);	
			requestParamsMap.put("total_fee", s[1]);	
			result = iRefundService.refundSubmit(requestParamsMap, payType);
		}
		if(logger.isInfoEnabled()){
			logger.info("operRefund >> end: " + result);
		}

		return result;
	}
	
}
