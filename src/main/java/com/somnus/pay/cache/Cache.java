package com.somnus.pay.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存注解
 * 
 * @author 丹青生
 *
 * @date 2015-8-13
 */
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)  
@Documented
public @interface Cache {

	String key() default ""; // 缓存key,支持SPEL表达式
	int expire() default 0; // 过期时间(负值时则清理对应缓存)
	String[] clean() default {}; // 被关联清除的缓存key,支持SPEL表达式
	
}
