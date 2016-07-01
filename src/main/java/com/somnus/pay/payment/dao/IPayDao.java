package com.somnus.pay.payment.dao;

import java.util.List;
import java.util.Map;

import com.somnus.pay.payment.pojo.PaymentOrder;

public interface IPayDao extends IBaseDao<PaymentOrder> {

    /**
     * <pre>
     * 统计用户支付类型频率
     * </pre>
     *
     * @param userId
     * @return
     */
     public Map<String, List<String>> getBankTypeIndexByUserId(String userId);

}
