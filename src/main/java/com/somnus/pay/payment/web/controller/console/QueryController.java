package com.somnus.pay.payment.web.controller.console;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 控制台首页
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 丹青生
 * @version: 1.0
 * @createdate: 2015-12-3
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015-12-3       丹青生                               1.0            初始化
 */
@Controller
@RequestMapping("console")
public class QueryController {

	@RequestMapping("query")
	public String query(){
		return "console/query";
	}
	
}
