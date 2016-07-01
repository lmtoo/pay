package com.somnus.pay.payment.pojo;

import java.io.Serializable;

/**
 *  分页实体类
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-28<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-28       丹青生                        1.0            初始化 <br/>
 *  
 */
public class Page implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long    total;
    private Integer pageNum  = 1;
    private Integer pageSize = 10;
    private String order;
    private boolean autoCount = true;
    
    public Page(){}
    
    public Page(int pageSize){
    	this.pageSize = pageSize;
    }
    
    public Page(int pageNum, int pageSize){
    	this.pageNum = pageNum;
    	this.pageSize = pageSize;
    }
    
    public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public boolean isAutoCount() {
		return autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	public int getFirstResult() {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return (this.getPageNum() - 1) * this.getPageSize();
    }

}
