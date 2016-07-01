package com.somnus.pay.payment.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.somnus.pay.payment.dao.IPayDao;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.util.DateUtils;

@Repository
public class PayDao extends BaseDao<PaymentOrder> implements IPayDao {

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, List<String>> getBankTypeIndexByUserId(String userId) {
        
        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lastMonth = sdf.format(DateUtils.getBeforeOrAfterMonth(new Date(), -1));
        
        String hql = "SELECT defaultBank FROM PaymentOrder WHERE defaultBank IN ('unionpay', 'tencent', 'lianlian', 'kuaiqian', 'alipay') AND status=1 AND userId=? AND createTime>='" + lastMonth + "' GROUP BY defaultBank ORDER BY COUNT(*) DESC";
        List<String> list = (List<String>) this.find(hql, userId);
        if (CollectionUtils.isEmpty(list)){
            list = new ArrayList<String>();
        }
        resultMap.put("platform", list);
        
        hql = "SELECT defaultBank FROM PaymentOrder WHERE defaultBank <> 'unionpay' AND status=1 AND thirdPayType=4 AND userId=? AND createTime>='" + lastMonth + "' GROUP BY defaultBank ORDER BY COUNT(*) DESC";
        list = (List<String>) this.find(hql, userId);
        if (CollectionUtils.isEmpty(list)){
            list = new ArrayList<String>();
        }
        resultMap.put("online", list);
        
        hql = "SELECT defaultBank FROM PaymentOrder WHERE defaultBank <> 'tencent' AND status=1 AND thirdPayType=3 AND userId=? AND createTime>='" + lastMonth + "' GROUP BY defaultBank ORDER BY COUNT(*) DESC";
        list = (List<String>) this.find(hql, userId);
        if (CollectionUtils.isEmpty(list)){
            list = new ArrayList<String>();
        }
        resultMap.put("fastpay", list);
        
        return resultMap;
    }
}
