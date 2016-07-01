package com.somnus.pay.payment.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateOperations;

import com.somnus.pay.payment.pojo.Page;
import com.somnus.pay.payment.pojo.QueryResult;

/**
 * @Author 尹正飞
 * @Email zhengfei.yin@b5m.com
 * @Version 2013-4-25 上午11:02:57
 **/

public interface IBaseDao<T> extends HibernateOperations {

    /**
     * 根据ID获取对象
     * @param id	Id
     * @return
     */
    T getById(Integer id);

    /**
     * 根据ID获取对象
     * @param id	Id
     * @return
     */
    T getById(String id);

    /**
     * 根据ID获取对象
     * @param id	Id
     * @return
     */
    T getById(Long id);

    /**
     * 获取记录总和
     * 
     * @return long
     */
    long getCounts();

    /**
     * 获取记录总和
     * @param hql	没有参数的Hql
     * @return
     */
    long getCounts(String hql);

    /**
     * 获取记录总和
     * @param hql	有参数的Hql
     * @param obj	参数对象
     * @return
     */
    long getCounts(String hql, Object... obj);

    /**
     * 分页查询数据
     * @param hql	没有参数的Hql
     * @param page	分页对象
     * @return
     */
    QueryResult<T> getPageList(String hql, Page page);

    /**
     * 分页查询数据
     * @param hql	有参数的Hql
     * @param page	分页对象
     * @param obj	参数对象
     * @return
     */
    QueryResult<T> getPageList(String hql, Page page, Object... obj);

    /**
     * 获取所有数据
     * @return
     */
    List<T> getAll();

    /**
     * sql 查询
     * @param sql
     * @return
     */
    public List<T> getListBySql(final String sql);

    /** 
     * @return
     */
    Session getSession();

    /**
     * 分页查询数据
     * @param page 定义分页对象
     * @param params
     * @return
     */
    QueryResult<T> list(Page page,Map<String,Object> params);

}
