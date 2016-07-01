package com.somnus.pay.payment.service.impl;

import java.util.Map;

import com.somnus.pay.payment.enums.PayChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.pay.payment.dao.ICustomsDeclarationDao;
import com.somnus.pay.payment.pojo.CustomsDeclaration;
import com.somnus.pay.payment.pojo.CustomsDeclaration.CustomsDeclarationUPK;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.service.CustomsDeclarationService;

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
@Service
@Transactional
public class CustomsDeclarationServiceImpl implements CustomsDeclarationService {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(CustomsDeclarationServiceImpl.class);
	
    @Autowired
    ICustomsDeclarationDao iCustomsDeclarationDao;

    @Override
    public CustomsDeclaration getById(String orderId, PayChannel channel, String bgConfigKey){
        LOGGER.info("通过getById获取报关对象:orderId:[{}],channel:[{}],bgConfigKey:[{}]", new Object[]{orderId,channel,bgConfigKey});
        CustomsDeclarationUPK id = new CustomsDeclarationUPK();
        id.setOrderId(orderId);
        id.setChannel(channel);
        id.setBgConfigKey(bgConfigKey);
        return iCustomsDeclarationDao.get(CustomsDeclaration.class, id);
    }

    @Override
    public CustomsDeclaration getByUPK(String orderId, PayChannel channel, String bgConfigKey){
        LOGGER.info("通过getByUPK获取报关对象:orderId:[{}],channel:[{}],bgConfigKey:[{}]", new Object[]{orderId,channel,bgConfigKey});
        return iCustomsDeclarationDao.getByUPK(orderId, channel, bgConfigKey);
    }

    @Override
    public void save(CustomsDeclaration customsDeclaration) {
    	LOGGER.info("保存报关记录:{}", customsDeclaration);
        iCustomsDeclarationDao.save(customsDeclaration);
    }

    @Override
    public void saveOrUpdate(CustomsDeclaration customsDeclaration) {
    	LOGGER.info("保存或更新报关记录:{}", customsDeclaration);
        iCustomsDeclarationDao.saveOrUpdate(customsDeclaration);
    }

	@Override
	public QueryResult<CustomsDeclaration> list(Page page,Map<String,Object> params) {
        LOGGER.info("查询报关列表:page:[{}],params:[{}]", page, params);
		return iCustomsDeclarationDao.list(page == null ? new Page() : page,params);
	}

    @Override
    public void update(CustomsDeclaration cd){
        LOGGER.info("更新报关记录:{}", cd);
        iCustomsDeclarationDao.update(cd);
    }

    @Override
    public void delete(CustomsDeclaration cd){
        LOGGER.info("删除报关记录:{}", cd);
        iCustomsDeclarationDao.delete(cd);
    }
}
