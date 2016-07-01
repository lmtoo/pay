package com.somnus.pay.payment.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.somnus.pay.payment.dao.IUserPayPasswordDao;
import com.somnus.pay.payment.pojo.PayPassword;

/**
 * @description: ${TODO}
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/1
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/1       bai          1.0             Why & What is modified
 */
@Repository
public class UserPayPasswordDao extends BaseDao<PayPassword> implements IUserPayPasswordDao {

    @Override
    public PayPassword getUserPayPasswordByUserId(String userId) {
        String hql = "from PayPassword where userId = ?";
        @SuppressWarnings("unchecked")
        List<PayPassword> payPassWordList = (List<PayPassword>)this.find(hql, userId);
        if(payPassWordList != null && !payPassWordList.isEmpty()){
            return payPassWordList.get(0);
        }
        return null;
    }
}
