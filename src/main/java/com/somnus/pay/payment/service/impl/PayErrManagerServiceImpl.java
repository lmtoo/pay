package com.somnus.pay.payment.service.impl;


import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.pay.payment.service.IPayErrManagerService;

@Service
@Transactional
public class PayErrManagerServiceImpl implements IPayErrManagerService {

    /**
     * <pre>
     * 清空 userCache 缓存
     * </pre>
     *
     */
    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public void cleanUserCache() {
    }
}
