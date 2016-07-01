package com.somnus.pay.payment.dao.impl;

import com.somnus.pay.payment.dao.ICustomsDeclarationDao;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.pojo.CustomsDeclaration;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description: ${TODO}
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/25
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/25       bai          1.0             Why & What is modified
 */
@Repository
public class CustomsDeclarationDaoImpl extends BaseDao<CustomsDeclaration> implements ICustomsDeclarationDao {

    @Override
    public CustomsDeclaration getByUPK(String orderId, PayChannel channel, String bgConfigKey) {
        String hql = "from CustomsDeclaration t where t.orderId=? and t.channel=? and t.bgConfigKey=? order by createTime desc";
        List result= this.find(hql,orderId,channel,bgConfigKey);
        return (null == result || result.isEmpty())?null:(CustomsDeclaration)result.get(0) ;
    }

}
