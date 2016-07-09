package com.somnus.pay.payment.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.somnus.pay.payment.dao.IBaseDao;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;
import com.somnus.pay.utils.Assert;

/**
 * 
 *  @description: 基础DAO操作<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-28<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-28       丹青生                        1.0            初始化 <br/>
 *  
 */
public class BaseDao<T> extends HibernateTemplate implements IBaseDao<T> {

    protected Logger LOGGER = LoggerFactory.getLogger(BaseDao.class);

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    public Session getSession() {
        return super.getSession();
    }

    @Override
    public T getById(Integer id) {
        if (id == null)
            return null;
        return (T) get(getEntityClass(), id);
    }

    @Override
    public T getById(String id) {
        if (id == null)
            return null;
        return (T) get(getEntityClass(), id, LockMode.PESSIMISTIC_WRITE);
    }

    @Override
    public T getById(Long id) {
        if (id == null)
            return null;
        return (T) get(getEntityClass(), id);
    }

    @Override
    public long getCounts() {
        String hql = "from " + this.getEntityClass().getSimpleName();
        return this.getCounts(hql);
    }

    @Override
    public long getCounts(String hql) {
        hql = "select count(*) as nums " + hql.substring(hql.toLowerCase().indexOf("from"));
        List<Long> list = (List<Long>) this.find(hql);
        Assert.notEmpty(list, PayExceptionCode.DAO_QUERY_RESULT_IS_NOT_EXPECTED);
        return list.get(0);
    }

    @Override
    public long getCounts(String hql, Object... obj) {
        hql = "select count(*) as nums " + hql.substring(hql.toLowerCase().indexOf("from"));
        List<Long> list;
        if (!isParameterNULL(obj)) {
            list = (List<Long>) this.find(hql, obj);
        } else {
            list = (List<Long>) this.find(hql);
        }
        Assert.notEmpty(list, PayExceptionCode.DAO_QUERY_RESULT_IS_NOT_EXPECTED);
        return list.get(0);
    }
    @Override
    public QueryResult<T> getPageList(String hql, Page pager) {
    	String obj = null;
        return this.getPageList(hql, pager, obj);
    }

    @Override
    public QueryResult<T> getPageList(final String hql, final Page pager, final Object... obj) {
        if (hql == null)
            throw new RuntimeException("HQL不能为空");
        if (pager == null)
            throw new RuntimeException("分页对象不能为空");
        if(pager.isAutoCount()){
        	pager.setTotal(getCounts(hql, obj));
        }
        return this.execute(new HibernateCallback<QueryResult<T>>() {
            public QueryResult<T> doInHibernate(Session session) throws HibernateException,
                                                                SQLException {
                Query query = session.createQuery(hql).setFirstResult(pager.getFirstResult())
                    .setMaxResults(pager.getPageSize());
                if (!isParameterNULL(obj)) {
                    for (int i = 0; i < obj.length; i++) {
                        query.setParameter(i, obj[i]);
                    }
                }
                return new QueryResult<T>(query.list(), pager);
            }
        });
    }

    protected Class<?> getEntityClass() {
        return this.getGenericClass(this.getClass(), 0);
    }

    private Class<?> getGenericClass(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if ((params != null) && (params.length >= (index - 1))) {
                return (Class<?>) params[index];
            }
        }// end if.
        return null;
    }

    private boolean isParameterNULL(Object... obj) {
        if (obj == null)
            return true;
        for (Object o : obj) {
            if (o == null)
                return true;
        }
        return false;
    }

    @Override
    public List<T> getAll() {
        String hql = "from " + this.getEntityClass().getSimpleName();
        return (List<T>) this.find(hql);
    }

    @Override
    public List<T> getListBySql(final String sql) {
        if (StringUtils.isBlank(sql))
            throw new RuntimeException("sql不能为空");
        List<T> list = null;
        try {
            list = this.execute(new HibernateCallback<List<T>>() {
                public List<T> doInHibernate(Session session) throws HibernateException,
                                                             SQLException {
                    return session.createSQLQuery(sql).list();
                }
            });
        } catch (Exception e) {
            logger.error("根据SQL[" + sql + "]查询列表失败", e);
            list = Collections.EMPTY_LIST;
        }
        return list;
    }
    
    /**
	 * 分页查询所有记录
	 * @param page
	 * @return
	 */
    @Override
    public QueryResult<T> list(Page page,Map<String,Object> params){
        page = page == null ? new Page() : page;
        StringBuilder search = new StringBuilder("from ");
        search.append(this.getEntityClass().getSimpleName());
        List<Object> paramList = new LinkedList<Object>();
        if(MapUtils.isNotEmpty(params)){
            search.append(" where 1 = 1");
            for(Map.Entry<String,Object> entry : params.entrySet()){
                String name = entry.getKey();
                Object value = entry.getValue();
                if((name.endsWith("time") || name.endsWith("Time")) && value != null && value.getClass().isArray()){
                    Object[] values = (Object[]) value;
                    if(values[0] != null){
                        search.append(" and ").append(name).append(" >= ?");
                        paramList.add(values[0]);
                    }
                    if(values.length >= 1){
                        if(values[1] != null){
                            search.append(" and ").append(name).append(" <= ?");
                            paramList.add(values[1]);
                        }
                    }
                }else{
                    search.append(" and ").append(name).append(" = ?");
                    paramList.add(value);
                }
            }
        }
        search.append(" order by ").append(StringUtils.defaultString(page.getOrder(), "createTime desc"));
        return this.getPageList(search.toString(), page, paramList.toArray());
    }

}
