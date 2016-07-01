package com.somnus.pay.payment.config.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.log.ri.http.HttpClientUtils;
import com.somnus.pay.payment.config.PaySourceConfigService;
import com.somnus.pay.payment.dao.IPaySourceDao;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.somnus.pay.payment.pojo.PaymentSource;

/**
 * @description: 支付来源配置服务初始化业务类
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/11/26
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/11/26       bai          1.0          将支付来源配置服务初始化从配置中心中移出来
 */
@Service
public class PaySourceConfigServiceImpl extends AbstractConfigService implements PaySourceConfigService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PaySourceConfigServiceImpl.class);
	
	@Autowired
    IPaySourceDao iPaySourceDao;
	
	private List<PaymentSource> sourceList = new ArrayList<PaymentSource>(0);
    private Map<String, String> sourceMap = new ConcurrentHashMap<String,String>(0);
    private Map<String, PaymentSource> paySourceMap = new ConcurrentHashMap<String,PaymentSource>(0);
	
	public PaySourceConfigServiceImpl() {
		super("支付来源");
	}

	@Override
	protected void doRefresh(){
		sourceList = iPaySourceDao.getAll(); // 加载所有配置数据
		Map<String, PaymentSource> paySourceMapTemp = new ConcurrentHashMap<String,PaymentSource>();
		Map<String, String> sourceMapTemp = new ConcurrentHashMap<String,String>();
        for (PaymentSource source : sourceList) {
			paySourceMapTemp.put(source.getSourceId() + "", source);
			sourceMapTemp.put(source.getSourceId() + "", source.getReturnUrl());
        }
		sourceMap = sourceMapTemp;
		paySourceMap = paySourceMapTemp;
		LOGGER.info("支付来源信息初始化sourceList:[{}],sourceMap:[{}],paySourceMap:[{}]",
				new Object[]{JSON.toJSONString(sourceList),sourceMap,paySourceMap});
	}

	@Override
	public PaymentSource getPaymentSourceById(String sourceId) {
		try {
			if(null == paySourceMap || paySourceMap.isEmpty()){
				doRefresh();
			}
			return paySourceMap.get(sourceId);
		} catch (Exception e) {
			LOGGER.error("getPaymentSourceById error,sourceId:"+ sourceId +",e" + e);
		}
		return null;
	}

	@Override
	public String getPaymentSourceUrlById(String sourceId) {
		try {
			if(null == sourceMap || sourceMap.isEmpty()){
				doRefresh();
			}
			return sourceMap.get(sourceId);
		} catch (Exception e) {
			LOGGER.error("getPaymentSourceUrlById error,sourceId:"+ sourceId +",e" + e);
		}
		return null;
	}

	@Override
	public void updatePaymentSource(PaymentSource paymentSource) {
		iPaySourceDao.update(paymentSource);
	}

	@Override
	public void addPaymentSource(PaymentSource paymentSource) {
		iPaySourceDao.save(paymentSource);
	}

	@Override
	public void removePaymentSource(String sourceId) {
		PaymentSource t = new PaymentSource();
		t.setId(Integer.parseInt(sourceId));
		iPaySourceDao.delete(t);
	}

	@Override
	public QueryResult<PaymentSource> getAllByPage(Page page) {
		return iPaySourceDao.list(page, null);
	}

	/**
	 * 根据ips发送相应的清空请求并获得返回结果
	 * @return
	 * @param ips
	 */
	@Override
	public String sendOtherCachePaymentSourceDBClean(String ips, String pwd){
		LOGGER.info("Process PaymentSource sendOtherCacheDBConfigClean method: ips={}" + ips);
		StringBuilder result = new StringBuilder();
		if (StringUtils.isNotBlank(ips)) {
			String[] ipsArray = ips.split("\\|");
			for (String ip : ipsArray) {
				String response = StringUtils.isBlank(ip) ? "" : HttpClientUtils.get("http://" + ip + Constants.CACHE_PAYMENT_SOURCE_CLEAN + "?pwd=" + pwd, null);
				result.append(ip).append(":").append(response).append("\r");
			}
		} else if(cachePaymentSourceDBClean()) {
			result.append(StatusCode.SUCCESS_CODE);
		}
		return result.toString();
	}

	/**
	 * 清空PaymentSource的相关本地缓存
	 * @return
	 */
	public boolean cachePaymentSourceDBClean() {
		try{
			doRefresh();
		}catch (Exception e){
			LOGGER.error("Process PaymentSource cacheDBConfigClean error e:",e);
			return false;
		}
		return true;
	}

	@Override
	public QueryResult<PaymentSource> getAllByPage(Page page, String sourceId){
		List<PaymentSource> list = sourceList;
		List<PaymentSource> paymentSourceList = new ArrayList<PaymentSource>();
		QueryResult<PaymentSource> result = new QueryResult<PaymentSource>();
		if(StringUtils.isNotBlank(sourceId)){
			for(int i = 0; i < list.size(); i++){
				if(Integer.valueOf(sourceId).equals(list.get(i).getSourceId())){
					paymentSourceList.add(list.get(i));
				}
			}
			page.setTotal(10l);
			result.setList(paymentSourceList);
			result.setPage(page);
			return result;
		}
		page.setTotal(new Long(list.size()));
		int start = (page.getPageNum()-1) * page.getPageSize();
		int end = start + page.getPageSize();
		if(end > page.getTotal())end = page.getTotal().intValue();
		for(int i = start; i < end; i++){
			paymentSourceList.add(list.get(i));
		}
		result.setList(paymentSourceList);
		result.setPage(page);
		return result;
	}

}
