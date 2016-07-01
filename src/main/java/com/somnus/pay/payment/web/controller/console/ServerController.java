package com.somnus.pay.payment.web.controller.console;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.util.PageCommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.payment.config.IConfigService;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentConfig;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.service.IPayService;
import com.somnus.pay.payment.util.Constants;
import com.somnus.pay.payment.util.FuncUtils;
import com.somnus.pay.payment.util.MemCachedUtil;
import com.somnus.pay.payment.web.controller.BaseController;
import com.somnus.pay.utils.Assert;

/**
 * <pre>
 * 对外服务接口 Controller
 * </pre>
 *
 * @author masanbao
 * @version $ ServerController.java, v 0.1 2014年12月23日 下午2:48:18 masanbao Exp $
 * @since   JDK1.6
 */
@Controller
@RequestMapping("server")
public class ServerController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    private IPayService  iPayService;
    @Autowired
    private IConfigService iConfigService;

    /**
     * <pre>
     * 通过订单号获取订单支付明细
     * </pre>
     *
     * @param orderIds 订单号: 多个订单号用","分隔
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "getOrderDetail", produces = { "text/html;charset=UTF-8" })
    public @ResponseBody String getOrderDetail(String orderIds, HttpServletRequest request,
                                               HttpServletResponse response) {
        String result = "Parameter[orderIds] cannot be empty!";
        if (StringUtils.isNotBlank(orderIds)) {
            List<PaymentOrder> list = iPayService.getOrderDetail(orderIds);
            result = JSON.toJSONStringWithDateFormat(list, JSON.DEFFAULT_DATE_FORMAT);
        }
        return result;
    }
    
    /**
     * <pre>
     * 清空 userCache 缓存
     * </pre>
     *
     */
    @RequestMapping(value = "cleanUserCache", produces = { "text/html;charset=UTF-8" })
    @CacheEvict(value = "userCache", allEntries = true)
    public @ResponseBody String cleanUserCache() {
        return "Clean user cache success.";
    }

    /**
     * 支付配置管理页面
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("managePage")
    public String managePage(HttpServletRequest request, Model model, Page page, String keyName) {
    	if(logger.isInfoEnabled()){
            logger.info("Process managePage method: userIp:[{}],remoteIp:[{}]", new Object[]{FuncUtils.getIpAddr(request),request.getRemoteAddr()});
        }
        if(keyName != null){
        	keyName = keyName.trim();
        }
        page.setPageSize(10);
        model.addAttribute("rootPath", PageCommonUtil.getRootPath(request, false));
        model.addAttribute("keyName", keyName);
        model.addAttribute("paymentConfigs", iConfigService.getPaymentConfigListByPage(page, keyName));
        model.addAttribute("payServerIps", iConfigService.getStringValue(Constants.PAY_SERVER_IPS));
        return Constants.INNER_MANAGE_PAGE;
    }

    /**
     * 清空读取数据配置信息
     * @return
     */
    @RequestMapping(value = "cacheDBConfigClean", produces = { "text/html;charset=UTF-8" })
    @JsonResult(desc = "清空读取数据配置信息")
    public ActionResult<String> cacheDBConfigClean(String pwd, String ips) {
        Assert.isTrue(Constants.INNER_QUERY_PWD.equals(pwd), PayExceptionCode.PAYMENT_CONFIG_OPER_ERROR);
        return new ActionResult<String>(StatusCode.SUCCESS, iConfigService.sendOtherCacheDBConfigClean(ips, pwd));
    }

    /**
     * 更新支付侧配置信息
     * @return
     */
    @RequestMapping(value = "paymentConfigOper", method = RequestMethod.POST)
    @JsonResult(desc = "更新支付侧配置信息")
    public ActionResult<Boolean> paymentConfigOper(PaymentConfig paymentConfig, String pwd) {
    	Assert.isTrue(Constants.INNER_QUERY_PWD.equals(pwd), PayExceptionCode.PAYMENT_CONFIG_OPER_ERROR);
        return new ActionResult<Boolean>(StatusCode.SUCCESS, iConfigService.updateConfig(paymentConfig));
    }

    /**
     * 查询支付侧所有配置信息
     * @return
     */
    @RequestMapping(value = "paymentConfigQuery", produces = { "text/html;charset=UTF-8" })
    @JsonResult(desc = "查询支付侧所有配置信息")
    public ActionResult<List<PaymentConfig>> paymentConfigQuery(String pwd) {
        Assert.isTrue(Constants.INNER_QUERY_PWD.equals(pwd), PayExceptionCode.PAYMENT_CONFIG_OPER_ERROR);
        return new ActionResult<List<PaymentConfig>>(StatusCode.SUCCESS, iConfigService.getPaymentConfigList());
    }

    /**
     * 清空前端版本号
     * @return
     */
    @RequestMapping(value = "refleshFrontTimestamp", produces = { "text/html;charset=UTF-8" })
    @JsonResult(desc = "清空前端时间")
    public ActionResult<Boolean> refleshFrontTimestamp() {
        MemCachedUtil.cleanCache(Constants.MEMCACHE_PAY_FRONTTIMESTAMP);
        return new ActionResult<Boolean>(StatusCode.SUCCESS, true);
    }
    
    /**
     * 清空支付缓存数据
     * @param payIds
     * @return
     */
    @RequestMapping(value = "cancelPayment")
    @JsonResult(desc = "取消支付缓存数据")
    public ActionResult<Boolean> cancelPayment(HttpServletRequest request, String... payIds) {
        if(logger.isInfoEnabled()){
            logger.info("Process cancelPayment method: payIds=[{}]", Arrays.deepToString(payIds));
        }
        Assert.isTrue(checkConsolePwd(request.getParameter("pwd")), PayExceptionCode.ERROR_CANCEL_PAYMENT_OPER);
        iPayService.cancelPayment(payIds);
        return new ActionResult<Boolean>(StatusCode.SUCCESS, true);
    }

    /**
     * 版本控制页面（前端和清空支付信息缓存）
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("versionPage")
    public String versionPage(HttpServletRequest request, Model model) {
        if(logger.isInfoEnabled()){
            logger.info("Process versionPage method: userIp:[{}],remoteIp:[{}]", new Object[]{FuncUtils.getIpAddr(request),request.getRemoteAddr()});
        }
        model.addAttribute("rootPath", PageCommonUtil.getRootPath(request, false));
        model.addAttribute("pageVersion", PageCommonUtil.getFrontVersionTimestamp());
        return Constants.INNER_VERSION_PAGE;
    }

    /**
     * 验证后端请求密码是否正确
     * @param pwd
     * @return
     */
    private boolean checkConsolePwd(String pwd) {
        return StringUtils.isNotBlank(pwd) && Constants.INNER_QUERY_PWD.equals(pwd);
    }

}
