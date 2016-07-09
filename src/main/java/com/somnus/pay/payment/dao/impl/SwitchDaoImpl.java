package com.somnus.pay.payment.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.somnus.pay.payment.dao.SwitchDao;
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
@Repository
public class SwitchDaoImpl extends BaseDao<Switch> implements SwitchDao {

	@Override
	public List<Switch> getAll(SwitchType type) {
		String hql = "from Switch t where t.type=?";
		return super.getPageList(hql, new Page(Integer.MAX_VALUE), type).getList();
	}

	@Override
	public QueryResult<Switch> list(SwitchType type, Page page) {
		String hql = "from Switch t where t.type=? order by " + StringUtils.defaultString(page.getOrder(), "createTime desc");
		return this.getPageList(hql, page, type);
	}

}
