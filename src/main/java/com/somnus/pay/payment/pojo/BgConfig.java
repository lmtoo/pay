package com.somnus.pay.payment.pojo;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *  @description: 报关配置<br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-18<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-18       丹青生                        1.0            初始化 <br/>
 *  
 */
public class BgConfig implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String place;
	private String service;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String bgConfigKey(){
		return StringUtils.defaultIfEmpty(this.getCode(),"") + StringUtils.defaultIfEmpty(this.getPlace(), "");
	}
	
}
