package com.somnus.pay.payment.dao;

import java.util.List;
import java.util.Map;

import com.somnus.pay.payment.enums.SwitchType;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.payment.pojo.Switch;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-31<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-31       丹青生                        1.0            初始化 <br/>
 *  
 */
public interface SwitchDao extends IBaseDao<Switch> {

	public List<Switch> getAll(SwitchType type);
	
	public QueryResult<Switch> list(SwitchType type, Page page);

	public QueryResult<Switch> list(Page page, Map<String,Object> params);

}
