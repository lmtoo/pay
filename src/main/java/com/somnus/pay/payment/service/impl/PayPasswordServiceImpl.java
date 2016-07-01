package com.somnus.pay.payment.service.impl;

import java.util.Date;

import com.somnus.pay.utils.PWCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.somnus.pay.cache.CacheServiceExcutor;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.payment.api.PayPasswordService;
import com.somnus.pay.payment.dao.IUserPayPasswordDao;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PayPassword;
import com.somnus.pay.utils.Assert;

/**
 * @description: 支付密码实现
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/1
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/1       bai          1.0          支付密码实现
 */
@Service("payPasswordService")
@Transactional
public class PayPasswordServiceImpl implements PayPasswordService {

    protected Logger logger = LoggerFactory.getLogger(PayPasswordServiceImpl.class);

    @Autowired
    private IUserPayPasswordDao iUserPayPasswordDao;

    private static final String VALIDATE_ONCE_HOUR_KEY = "pay_validateOnceHour_";	//缓存一小时验证的操作记录

    private static final String REMAINTIME_CAN_VALIDATE_KEY = "pay_remaintimeCanValidate_";	//缓存操作次数超过后, 可以下次操作的剩余时间

    @Value("#{configProperties['password.onceCanEveryHour']}")
    private Integer ONCE_CAN_EVERY_HOUR; // 一小时可以操作的次数

    public static final int PAYPASSWORD_ISSECRET = 1;		//支付密码允许免密
    public static final int PAYPASSWORD_ISNOTSECRET = 0;	//支付密码不允许免密

    @Value("#{configProperties['password.limitTime']}")
	private Integer limitTime; // 一小时


    @Override
    public void validatePayPassword(String userId, String payPassword) {
        String validateOnceHourKey = VALIDATE_ONCE_HOUR_KEY + userId;
        String remaintimeCanValidateKey = REMAINTIME_CAN_VALIDATE_KEY + userId;
        Integer alreadyCount = this.queryAlreadyOperateCount(validateOnceHourKey) + 1;
        if(alreadyCount > ONCE_CAN_EVERY_HOUR){
            Long remainTime = this.queryRemainTime(remaintimeCanValidateKey);
            Long restrictTime = (System.currentTimeMillis() - remainTime) / 1000;
            throw new PayException(new StatusCode(PayExceptionCode.PASSWORD_ERROR_A_FEW_MINUTES_CODE, "密码错误次数过多，账户被锁定，" + (60 - (restrictTime + 59) / 60) + "分钟后重试"));
        }
        PayPassword PayPassword = this.iUserPayPasswordDao.getUserPayPasswordByUserId(userId);
        Assert.isTrue(PayPassword != null, PayExceptionCode.PAYPASSWORD_NOT_SET);
        if(PWCode.getMD5Code_64(payPassword).equals(PayPassword.getPassword())) {
            this.clearCountWhenSuccess(userId);
            return;
        }
        //超过了一小时的验证次数限制
        if(alreadyCount >= ONCE_CAN_EVERY_HOUR){
            CacheServiceExcutor.put(validateOnceHourKey, alreadyCount, limitTime);
            CacheServiceExcutor.put(remaintimeCanValidateKey, System.currentTimeMillis(), limitTime);
            throw new PayException(PayExceptionCode.PASSWORD_ERROR_ONE_HOUR);
        }
        CacheServiceExcutor.put(validateOnceHourKey, alreadyCount, limitTime);
        throw new PayException(new StatusCode(PayExceptionCode.PAYPASSWORD_ERROR.getCode(), "支付密码不正确, 您还可以输入" + (ONCE_CAN_EVERY_HOUR - alreadyCount) + "次"));
    }

    @Override
    public void setPayPassword(String userId, String payPassword) {
        PayPassword PayPassword = this.iUserPayPasswordDao.getUserPayPasswordByUserId(userId);
        Assert.isTrue(PayPassword == null,PayExceptionCode.PAYPASSWORD_ALREADY_SET);
        payPassword = PWCode.getMD5Code_64(payPassword);
        PayPassword record = new PayPassword();
        record.setId(PWCode.getUUID());
        record.setUserId(userId);
        record.setPassword(payPassword);
        record.setIsSecret(PAYPASSWORD_ISNOTSECRET);
        record.setAddTime(new Date());
        this.iUserPayPasswordDao.save(record);
    }

    @Override
    public com.somnus.pay.payment.model.PayPassword getPayPasswordInfo(String userId) {
        PayPassword payPassword = this.iUserPayPasswordDao.getUserPayPasswordByUserId(userId);
        if(payPassword != null){
            return payPassword.convert();
        }
        return null;
    }

    @Override
    public void updatePayPassword(String userId, String oldPayPassword,String newPayPassword) {
        Assert.isTrue(oldPayPassword != null && newPayPassword != null, PayExceptionCode.PAYPASSWORD_CANT_BE_NULL);
        //判断新密码和旧密码是否相同
        Assert.isTrue(!oldPayPassword.equals(newPayPassword), PayExceptionCode.NEW_AND_OLD_PAYPASSWORD_CANT_BE_SAME);
        this.validatePayPassword(userId, oldPayPassword);//调用校验密码功能校验密码是否正确
        this.resetPayPassword(userId, newPayPassword);
    }

    @Override
    public void resetPayPassword(String userId, String payPassword) {
        PayPassword PayPassword = this.iUserPayPasswordDao.getUserPayPasswordByUserId(userId);
        PayPassword.setPassword(PWCode.getMD5Code_64(payPassword));
        PayPassword.setUpdateTime(new Date());
        this.iUserPayPasswordDao.update(PayPassword);
    }

    @Override
    public void setFreeQuotaValue(String userId, Integer freeQuota) {
        PayPassword PayPassword = this.iUserPayPasswordDao.getUserPayPasswordByUserId(userId);
        PayPassword.setFreeQuota(freeQuota);
        PayPassword.setIsSecret(PAYPASSWORD_ISSECRET);
        PayPassword.setUpdateTime(new Date());
        this.iUserPayPasswordDao.update(PayPassword);
    }

    @Override
    public void setFreeQuotaOrNot(String userId, Byte flag) {
        PayPassword PayPassword = this.iUserPayPasswordDao.getUserPayPasswordByUserId(userId);
        PayPassword.setIsSecret(flag);
        PayPassword.setUpdateTime(new Date());
        this.iUserPayPasswordDao.update(PayPassword);
    }




    /**
     * 验证成功后清除此操作相关的次数限制
     *
     * @param userId
     */
    private void clearCountWhenSuccess(String userId) {
        String validateOnceHourKey = VALIDATE_ONCE_HOUR_KEY + userId;
        String remaintimeCanValidateKey = REMAINTIME_CAN_VALIDATE_KEY + userId;
        CacheServiceExcutor.remove(validateOnceHourKey);
        CacheServiceExcutor.remove(remaintimeCanValidateKey);

    }

    /**
     * 查询操作次数
     *
     * @param key
     * @return
     */
    private Integer queryAlreadyOperateCount(String key) {
        Integer count = (Integer)CacheServiceExcutor.get(key);
        if (count == null) {
            count = 0;
        }
        logger.info("已经操作次数：{} : {}",key , count);
        return count;
    }

    /**
     * 查询操作次数超过后, 可以下次操作的剩余时间
     *
     * @param key
     * @return
     */
    private Long queryRemainTime(String key) {
        Long remainTime = (Long)CacheServiceExcutor.get(key);
        if (remainTime == null) {
            remainTime = 0L;
        }
    	logger.info("当前用户被锁定:{} : {}", key, remainTime);
        return remainTime;
    }

	@Override
	public boolean hadSetPassword(String userId) {
		Assert.hasText(userId, PayExceptionCode.USER_ID_ERROR);
		PayPassword PayPassword = this.iUserPayPasswordDao.getUserPayPasswordByUserId(userId);
		return PayPassword != null && StringUtils.isNotEmpty(PayPassword.getPassword());
	}

}
