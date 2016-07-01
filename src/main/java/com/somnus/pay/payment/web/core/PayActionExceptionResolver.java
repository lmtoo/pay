package com.somnus.pay.payment.web.core;

import java.lang.annotation.Annotation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.HandlerMethod;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.mvc.support.ActionResult;
import com.somnus.pay.mvc.support.JsonResult;
import com.somnus.pay.mvc.support.SmartExceptionResolver;

/**
 * 能够自动封装业务执行失败结果的异常处理器
 * 
 * @author 丹青生
 * @date 2015-07-15
 * 
 * @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver
 *
 */
public class PayActionExceptionResolver extends SmartExceptionResolver {

	@Override
    protected ActionResult<Object> getErrorResponse(JsonResult jsonResult, Exception exception) {
    	boolean hasJsonResult = jsonResult != null;
    	String errorCode;
    	String errorMsg;
    	if(exception instanceof MissingServletRequestParameterException){
    		errorCode = StatusCode.PARAMETER_ERROR_CODE;
    		errorMsg = "缺少必须的参数:" + ((MissingServletRequestParameterException) exception).getParameterName();
    	}else if(exception instanceof B5mException){
    		errorCode = ((B5mException)exception).getCode();
    		errorMsg = ((B5mException) exception).getMessage();
    	}else{
    		errorCode = hasJsonResult && StringUtils.isNotEmpty(jsonResult.errorCode()) ? jsonResult.errorCode() : StatusCode.SERVER_ERROR_CODE;
    		errorMsg = hasJsonResult && StringUtils.isNotEmpty(jsonResult.desc()) ? (jsonResult.desc() + "执行失败") : StatusCode.SERVER_ERROR.getMessage();
    	}
    	return new ActionResult<Object>(errorCode, errorMsg, null);
	}

	@Override
	protected boolean canUseJsonResult(HandlerMethod handler, MethodParameter returnType) {
		return false; // 暂时所有接口均使用JsonResult渲染
	}

	@Override
	protected JsonResult getDefaultJsonResult() {
		return new JsonResult() { // 默认JsonResult配置
			
			@Override
			public Class<? extends Annotation> annotationType() {
				return JsonResult.class;
			}
			
			@Override
			public boolean useUnicode() {
				return false;
			}
			
			@Override
			public String errorCode() {
				return StatusCode.SERVER_ERROR_CODE;
			}
			
			@Override
			public String desc() {
				return "";
			}
			
			@Override
			public String callback() {
				return "jsonpCallback";
			}

			@Override
			public boolean convertString() {
				return false;
			}

			@Override
			public boolean debug() {
				return true;
			}
		};
	}
    
}