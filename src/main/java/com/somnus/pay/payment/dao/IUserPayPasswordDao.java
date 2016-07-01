package com.somnus.pay.payment.dao;

import com.somnus.pay.payment.pojo.PayPassword;

/**
 * @description: 支付密码
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/1
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/1       bai          1.0          支付密码
 */
public interface IUserPayPasswordDao extends IBaseDao<PayPassword> {
    public PayPassword getUserPayPasswordByUserId(String userId);


}
