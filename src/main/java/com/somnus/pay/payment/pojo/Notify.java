package com.somnus.pay.payment.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.somnus.pay.payment.enums.NotifyChannel;
import com.somnus.pay.payment.enums.NotifyStatus;
import com.somnus.pay.payment.enums.NotifyType;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.utils.Assert;

/**
 *  @description: 通知请求发起方流水记录<br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
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
@Table(name = "t_payment_notify")
public class Notify implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private NotifyId id;
	@Column(name = "source")
	private int source;
	@Column(name = "channel", nullable = true)
	@Enumerated(EnumType.STRING)
	private NotifyChannel channel = NotifyChannel.EMPTY;
	@Column(name = "target", nullable = true)
	private String target;
	@Column(name = "data", nullable = true)
	private String data;
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private NotifyStatus status;
	@Column(name = "create_time")
	private Date createTime;
	@Column(name = "update_time")
	private Date updateTime;
	@Column(name = "memo")
	private String memo;

	public Notify(){}

	public NotifyId getId() {
		return id;
	}

	public void setId(NotifyId id) {
		this.id = id;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public NotifyChannel getChannel() {
		return channel;
	}

	public void setChannel(NotifyChannel channel) {
		this.channel = channel;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public NotifyStatus getStatus() {
		return status;
	}

	public void setStatus(NotifyStatus status) {
		this.status = status;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Embeddable
	public static class NotifyId implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		@Column(name = "order_id")
		private String orderId;
		@Column(name = "type")
		@Enumerated(EnumType.STRING)
		private NotifyType type;
		
		
		public NotifyId(){}
		
		public NotifyId(String orderId, NotifyType type){
			this.orderId = orderId;
			this.type = type;
		}
		
		public String getOrderId() {
			return orderId;
		}
		
		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}
		
		public NotifyType getType() {
			return type;
		}

		public void setType(NotifyType type) {
			this.type = type;
		}

		public void validate() {
			Assert.notNull(this.getOrderId(), PayExceptionCode.NOTIFY_ID_PARAMETER_IS_NULL);
			Assert.notNull(getType(), PayExceptionCode.NOTIFY_ID_PARAMETER_IS_NULL);
		}
		
		@Override
		public int hashCode() {
			return this.orderId.hashCode() + this.type.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof NotifyId){  
				NotifyId id = (NotifyId) obj;
				return this.type == id.getType() && this.orderId.equals(id.getOrderId());
			}
	        return false ;
		}
		
		public String toString(){
			return "orderId=" + this.orderId + ", type=" + this.type;
		}
	}
}

