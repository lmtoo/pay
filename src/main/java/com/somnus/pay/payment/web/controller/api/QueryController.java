package com.somnus.pay.payment.web.controller.api;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.mvc.support.JsonResultWrapper;
import com.somnus.pay.payment.enums.PayChannel;

/**
 *  @description: <br/>
 *  @author: 丹青生<br/>
 *  @version: 1.0<br/>
 *  @createdate: 2016-3-2<br/>
 *  Modification  History:<br/>
 *  Date         Author        Version        Discription<br/>
 *  -----------------------------------------------------<br/>
 *  2016-3-2       丹青生                        1.0            初始化 <br/>
 *  
 */
@Controller("apiQueryController")
@RequestMapping("api")
public class QueryController {

	@RequestMapping("enums/paychannel")
	@JsonResult(debug = false, desc = "获取支付渠道枚举")
	public JsonResultWrapper getEnums() {
		JsonConfig config = new JsonConfig();
		JsonValueProcessor processor = new JsonValueProcessor() {
			
			private Object processValue(Object arg1){
				JSONObject object = new JSONObject();
				if(arg1 instanceof PayChannel){
					PayChannel value = (PayChannel) arg1;
					object.accumulate("name", value.name());
					object.accumulate("value", value.getValue());
					object.accumulate("desc", value.getDesc());
				}
				return object;
			}
			
			@Override
			public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
				return processValue(arg1);
			}
			
			@Override
			public Object processArrayValue(Object arg0, JsonConfig arg1) {
				return processValue(arg0);
			}
		};
		config.registerJsonValueProcessor(PayChannel.class, processor);
		ActionResult<PayChannel[]> result = new ActionResult<PayChannel[]>(StatusCode.SUCCESS, PayChannel.values());
		return new JsonResultWrapper(JSONObject.fromObject(result, config).toString());
	}
	
}
