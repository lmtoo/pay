package com.somnus.pay.payment.service;

import java.util.List;
import java.util.Map;

import com.somnus.pay.payment.enums.SwitchType;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.pojo.Switch;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-31<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-31       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface SwitchService {

	public void save(Switch item);
	
	public void setValue(String key, String value);
	
	public String getValue(String key);
	
	public List<Switch> getAll();
	
	public Switch get(String key);
	
	public Switch lock(String key);
	
	public List<Switch> getAll(SwitchType type);
	
	public QueryResult<Switch> list(SwitchType type, Page page);

	public QueryResult<Switch> list(Page page, Map<String, Object> params);
}
