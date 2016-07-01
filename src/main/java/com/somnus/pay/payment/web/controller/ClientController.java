package com.somnus.pay.payment.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.mvc.support.JsonResultWrapper;
import com.somnus.pay.payment.api.PayPasswordService;
import com.somnus.pay.payment.service.IClientService;

/**
 * <pre>
 * 调用外部接口 Controller
 * </pre>
 *
 * @author masanbao
 * @version $ ClientController.java, v 0.1 2014年12月23日 下午2:48:18 masanbao Exp $
 * @since   JDK1.6
 */
@Controller
@RequestMapping("client")
public class ClientController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private IClientService clientService;
    @Autowired
    private PayPasswordService payPasswordService;

    /**
     * 获得超级帮钻数
     * @param userId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "getSuperBean", produces = { "text/html;charset=UTF-8" })
    public @ResponseBody String getOrderDetail(String userId, HttpServletRequest request,
                                               HttpServletResponse response) {

        String result = "Parameter[userId] cannot be empty!";
        try {
            if (StringUtils.isNotBlank(userId)) {
                Long amount = clientService.getSuperBeanByUserId(userId);
                result = amount.toString();
            }
        }catch (Exception e){
                logger.error("getOrderDetail error,userId:" + userId);
        }
        return result;
    }

    /**
     * 验证用户支付密码是否正确
     * @param userId
     * @param password
     * @return
     */
    @RequestMapping(value = "validateUserBzPayPassword")
    @JsonResult(desc = "验证用户支付密码是否正确")
	public ActionResult<Boolean> validateUserBzPayPassword(String userId, String password) {
		payPasswordService.validatePayPassword(userId, password);
		return new ActionResult<Boolean>(StatusCode.SUCCESS, true);
	}

    /**
     * 设置超级帮钻密码
     * @param userId
     * @param password
     * @param mobile
     * @param validateCode
     * @return
     */
    @RequestMapping(value = "setUserBzPayPassword")
    @JsonResult(desc = "设置超级帮钻密码")
    public JsonResultWrapper setUserBzPayPassword(String userId,String password,String mobile,String validateCode) {
        logger.info("Process setUserBzPayPassword method: userId={} , validateCode={}", userId, validateCode);
        return new JsonResultWrapper(clientService.setUserBzPayPassword(userId, password, mobile, validateCode));
    }

    /**
     * 发送超级帮钻密码短信
     * @param userId
     * @param mobile
     * @return
     */
    @RequestMapping(value = "sendPayPasswordSMS")
    @JsonResult(desc = "发送超级帮钻密码短信")
    public ActionResult<Boolean> sendBzPayPasswordSMS(String userId,String mobile) {
        logger.info("Process sendBzPayPasswordSMS method: userId={} , mobile={}", userId, mobile);
        clientService.sendBzPayPasswordSMS(userId, mobile);
        return new ActionResult<Boolean>(StatusCode.SUCCESS, true);
    }

    /**
     * 验证超级帮钻支付密码及超级帮钻数量
     * @param userId
     * @return
     */
    @RequestMapping(value = "validatePwdObtainBzNum")
    @JsonResult(desc = "验证超级帮钻支付密码及超级帮钻数量")
    public ActionResult<Boolean> validatePwdObtainBzNum(String userId,String password) {
    	payPasswordService.validatePayPassword(userId, password);
    	return new ActionResult<Boolean>(StatusCode.SUCCESS, true);
    }


}
