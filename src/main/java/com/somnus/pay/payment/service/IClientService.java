package com.somnus.pay.payment.service;

import java.util.Map;


/**
 * <pre>
 * 调用外部系统接口
 * </pre>
 *
 * @author masanbao
 * @version $ IClientService.java, v 0.1 2015年1月19日 下午1:34:26 masanbao Exp $
 * @since   JDK1.6
 */
public interface IClientService {

    /**
     * <pre>
     * 根据用户id获取用户账户的超级帮钻余额
     * </pre>
     *
     * @param userId
     * @return
     */
    public Long getSuperBeanByUserId(String userId);
    
    /**
     * <pre>
     * 支付超级帮钻
     * </pre>
     *
     * @param paramtMap
     * @return
     */
    public String paySuperBean(Map<String, String> paramtMap);

    /**
     * 设置超级帮钻支付密码
     * @param userId
     * @param password
     * @param mobile
     * @param validateCode
     * @return
     */
    public String setUserBzPayPassword(String userId,String password,String mobile,String validateCode);

    /**
     * 获取设置超级帮钻支付密码时发送短信
     * @param userId
     * @param mobile
     * @return
     */
    public void sendBzPayPasswordSMS(String userId,String mobile);

    /**
     * 获得用户绑定手机号
     * @param userId
     * @return
     */
    public String getUserBindMobile(String userId);

}
