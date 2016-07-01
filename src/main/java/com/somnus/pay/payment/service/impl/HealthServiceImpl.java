package com.somnus.pay.payment.service.impl;

import java.util.Properties;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.NetUtils;
import com.somnus.pay.payment.pojo.SystemInfo;
import com.somnus.pay.payment.service.HealthService;

/**
 *  @description: <br/>
 *  Copyright 2011-2015 B5M.COM. All rights reserved<br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-1-25<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-1-25       丹青生                        1.0            初始化 <br/>
 *  
 */
@Service
public class HealthServiceImpl implements HealthService {

	private SystemInfo systemInfo;
	
	public HealthServiceImpl(){
		Properties props = System.getProperties();
		String jdk = props.getProperty("java.vm.name") + " (" + props.getProperty("java.vm.version") + ")";
		String jvm = props.getProperty("java.runtime.name") + " (" + props.getProperty("java.runtime.version") + ")";
		String encoding = props.getProperty("file.encoding");
		String userHome = props.getProperty("user.home");
		String osName = props.getProperty("os.name");
		String tempDir = props.getProperty("java.io.tmpdir");
		systemInfo = new SystemInfo();
		systemInfo.setIp(NetUtils.getLocalHost());
		systemInfo.setJdk(jdk);
		systemInfo.setJvm(jvm);
		systemInfo.setEncoding(encoding);
		systemInfo.setUserHome(userHome);
		systemInfo.setOsName(osName);
		systemInfo.setTempDir(tempDir);
	}
	
	@Override
	public SystemInfo getSystemInfo() {
		SystemInfo systemInfo = new SystemInfo();
		systemInfo.setIp(this.systemInfo.getIp());
		systemInfo.setJdk(this.systemInfo.getJdk());
		systemInfo.setJvm(this.systemInfo.getJvm());
		systemInfo.setEncoding(this.systemInfo.getEncoding());
		systemInfo.setUserHome(this.systemInfo.getUserHome());
		systemInfo.setOsName(this.systemInfo.getOsName());
		systemInfo.setTempDir(this.systemInfo.getTempDir());
		Runtime runtime = Runtime.getRuntime();
		long freeMemoery = runtime.freeMemory();
		systemInfo.setFreeMemoery(transform(freeMemoery));
		long totalMemory = runtime.totalMemory();
		systemInfo.setTotalMemory(transform(totalMemory));
		long usedMemory = totalMemory - freeMemoery;
		systemInfo.setUsedMemory(transform(usedMemory));
		long maxMemory = runtime.maxMemory();
		systemInfo.setMaxMemory(transform(maxMemory));
		long useableMemory = maxMemory - totalMemory + freeMemoery;
		systemInfo.setUseableMemory(transform(useableMemory));
		return systemInfo;
	}
	
	private long transform(long memory){
		memory = (memory + 1023) / 1024; // byte -> Kb
		memory = (memory + 1023) / 1024; // Kb -> Mb
		return memory;
	}

}
