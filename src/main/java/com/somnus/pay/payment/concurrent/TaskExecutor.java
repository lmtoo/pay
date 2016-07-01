package com.somnus.pay.payment.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.log.ri.concurrent.RunnableAdapter;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2015-12-23<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2015-12-23       丹青生                        1.0            初始化 <br/>
 *  
 */
public class TaskExecutor {

	private final static Logger LOGGER = LoggerFactory.getLogger(TaskExecutor.class);
	
	private static final ScheduledExecutorService scheduledExcutor = Executors.newScheduledThreadPool(20); // TODO 算法升级:从配置文件中读取详细配置生成线程池

	public static void run(Task task) {
		if (task != null) {
			LOGGER.info("准备执行任务:[{}]", task.getName());
			try {
				if(task.getDelay() > 0){
					LOGGER.info("任务[{}]将在延迟[{}]毫秒后被执行", task.getName(), task.getDelay());
					scheduledExcutor.schedule(new RunnableAdapter(task), task.getDelay(), TimeUnit.MILLISECONDS);
				}else {
					scheduledExcutor.execute(new RunnableAdapter(task));
				}
			} catch (Exception e) {
				LOGGER.warn("任务:[" + task.getName() + "]执行失败", e);
			}
		}
	}
	
	public static void runFixed(Task task, long delay) {
		if (task != null) {
			LOGGER.info("准备执行任务:[{}]", task.getName());
			try {
				LOGGER.info("任务[{}]将在延迟[{}]毫秒后被执行", task.getName(), task.getDelay());
				scheduledExcutor.scheduleAtFixedRate(new RunnableAdapter(task), task.getDelay(), delay, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				LOGGER.warn("任务:[" + task.getName() + "]执行失败", e);
			}
		}
	}

}
