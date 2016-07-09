package com.somnus.pay.payment.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.somnus.pay.payment.enums.SwitchType;

/**
 *  @description: 第三方支付渠道回调流水记录<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-25       丹青生                        1.0            初始化 <br/>
 *  
 */
@Entity
@Table(name = "t_payment_switch")
public class Switch implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "channel")
	private String key;
	@Column(name = "value")
	private String value;
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private SwitchType type;
	@Column(name = "memo")
	private String memo;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "update_time")
	private Date updateTime;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SwitchType getType() {
		return type;
	}

	public void setType(SwitchType type) {
		this.type = type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
