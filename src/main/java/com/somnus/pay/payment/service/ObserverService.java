package com.somnus.pay.payment.service;

import java.util.Date;
import java.util.List;

import com.somnus.pay.payment.pojo.SystemInfo;

/**
 *  @description: 系统观察者服务<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-25       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface ObserverService {

	public long getServerId();
	
	public void reportSystemInfo();
	
	public Date getStartTime();
	
	public void increaseTotalOrderCount();
	
	public void increaseTotalOrderAmount(double amount);
	
	public void increaseTotalMoneyAmount(double amount);
	
	public void increaseTotalBzAmount(long amount);
	
	public long getTotalOrderCount();
	
	public double getTotalOrderAmount();
	
	public double getTotalMoneyAmount();
	
	public long getTotalBzAmount();
	
	public List<SystemInfo> collectSystemInfo();
	
}
