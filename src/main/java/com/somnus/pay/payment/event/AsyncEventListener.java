package com.somnus.pay.payment.event;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.payment.concurrent.Retryable;
import com.somnus.pay.payment.concurrent.RetryableTask;
import com.somnus.pay.payment.concurrent.Task;
import com.somnus.pay.payment.concurrent.TaskExecutor;
import com.somnus.pay.payment.exception.PayExceptionCode;

/**
 * @description: 支持异步处理的时间监听器
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-23
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-23       丹青生                               1.0            初始化
 */
public abstract class AsyncEventListener<T extends PaymentEvent> implements ApplicationListener<T>{

	private final static Logger LOGGER = LoggerFactory.getLogger(AsyncEventListener.class);
	private final static List<String> RETRY_IGNORE_EXCEPTION_CODE = Arrays.asList(
					PayExceptionCode.PAYMENT_HANDLER_NOT_FOUND.getCode(),
					PayExceptionCode.PAYMENT_CHANNEL_SERVICE_IS_UNABLE.getCode());

	@Override
	public void onApplicationEvent(final T event) {
		LOGGER.info("收到[{}]发起的[{}]事件通知,即将响应", event.getSource().getClass().getSimpleName(), event.getName());
		final AsyncEventListener<T> listener = this;
		RetryableTask task = new RetryableTask(event.getName() + "事件响应") {
			
			@Override
			public void run() {
				listener.doIt(event, this);
			}
		};
		task.setDelay(event.getDelay());
		if(event instanceof Retryable && ((Retryable) event).getTotal() > 0){
			Retryable retryable = (Retryable) event;
			task.setTotal(retryable.getTotal());
			task.setInterval(retryable.getInterval());
			LOGGER.info("当前任务失败时可重试:[总重试次数={} , 重试间隔:{}ms]", task.getTotal(), task.getInterval());
		}
		if(event.isAsync()){
			TaskExecutor.run(task);
		}else {
			doIt(event, task);
		}
	}

	private void doIt(T event, Task task){
		try {
			handle(event);
			LOGGER.info("[{}]事件响应处理完成", event.getName());
			retry(event, task, null);
		} catch (Exception e) {
			if(event.isAsync()){
				LOGGER.warn("[" + event.getName() + "]事件响应失败", e);
				retry(event, task, e);
			}else {
				LOGGER.warn("[" + event.getName() + "]事件响应失败:{}", e.getMessage());
				throw (e instanceof B5mException) ? ((B5mException) e) : new B5mException(PayExceptionCode.EVENT_LISTENER_HANDLE_ERROR, e);
			}
		}
	}
	
	private void retry(T event, Task task, Exception e){
		boolean isRetry = task instanceof RetryableTask;
		if(!isRetry){
			return;
		}
		RetryableTask retryAbleTask = (RetryableTask) task;
		int retryCount = retryAbleTask.getCurrent();
		if(e == null){
			if(retryCount > 0){
				LOGGER.info("[" + event.getName() + "]事件在重试时处理成功");
//				String message = retryAbleTask.getName() + "重试" + retryCount + "次后终于处理成功";
//				WarnningEvent warnningEvent = new WarnningEvent(this, message);
//				SpringContextUtil.getApplicationContext().publishEvent(warnningEvent);
			}
		}else {
			if(e instanceof B5mException && RETRY_IGNORE_EXCEPTION_CODE.contains(((B5mException) e).getCode())){
				LOGGER.warn("忽略的异常码[{}],无需重试", ((B5mException) e).getCode());
				return;
			}
			if(retryAbleTask.canRetry()){
				task.setDelay(retryAbleTask.getInterval());
				retryAbleTask.increaseCurrent();
				LOGGER.info("失败的任务[{}]即将进行第[{}]次重试", retryAbleTask.getName(), retryAbleTask.getCurrent());
				if(event.isAsync()){
					TaskExecutor.run(task);
				}else {
					doIt(event, task);
				}
			}else{
				LOGGER.warn("失败的任务[{}]已进行了[{}]次重试,不允许继续重试", retryAbleTask.getName(), retryAbleTask.getCurrent());
//				String message = retryAbleTask.getName() + "重试" + retryAbleTask.getCurrent() + "次后仍然失败(" + e.getMessage() + ")";
//				WarnningEvent warnningEvent = new WarnningEvent(this, message);
//				SpringContextUtil.getApplicationContext().publishEvent(warnningEvent);
			}
		}
	}
	
	protected abstract void handle(T event);
	
}
