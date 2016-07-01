package com.somnus.pay.cache;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheAspect implements InitializingBean {

	private final static Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);
	private final static Map<String, String[]> PARAM_NAME_MAP = new ConcurrentHashMap<String, String[]>();
	private final static Map<String, Expression> EXPRESSION_CACHE = new ConcurrentHashMap<String, Expression>();
	private ThreadLocal<ExpressionParser> parserHolder = new ThreadLocal<ExpressionParser>();
	private ClassPool classPool;
	
	@Resource
	private List<CacheManager> cacheManagers;
	
	public CacheAspect(){
		classPool = ClassPool.getDefault();
		classPool.insertClassPath(new ClassClassPath(this.getClass()));
	}
	
	@Around("@annotation(cache)")
	public Object process(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
		Signature sign = joinPoint.getSignature();
		String signature = sign.toString();
		CacheManager cacheManager = CacheServiceExcutor.getCacheManager(sign.getDeclaringTypeName(), sign.getName(), cache);
		if(cacheManager == null){
			if(LOGGER.isWarnEnabled()){
				LOGGER.warn("未找到支持[{}]的缓存管理器", signature);
			}
			return joinPoint.proceed();
		}
		CacheConfig cacheConfig = null;
		Object result = null;
		try {
			cacheConfig = buildCacheInfo(cache, joinPoint);
			if(StringUtils.isNotEmpty(cacheConfig.getKey())){
				result = cacheManager.get(cacheConfig.getKey());
				if(result != null){
					if(LOGGER.isTraceEnabled()){
						LOGGER.trace("方法[{}]返回结果缓存[{}]命中", signature, cacheConfig.getKey());
					}
					return result;
				}
			}
		} catch (Exception e) {
			if(LOGGER.isWarnEnabled()){
				LOGGER.warn("自动处理缓存失败", e);
			}
		}
		result = joinPoint.proceed();
		if(cacheConfig != null){
			if(cacheConfig.getClean() != null && cacheConfig.getClean().length > 0){ // 清理关联缓存
				try {
					cacheManager.remove(cacheConfig.getClean());
				} catch (Exception e) {
					if(LOGGER.isWarnEnabled()){
						LOGGER.warn("清理关联缓存数据失败", e);
					}
				}
			}
			if(result != null && StringUtils.isNotEmpty(cacheConfig.getKey())){ // 缓存当前结果
				if(LOGGER.isDebugEnabled()){
					LOGGER.debug("缓存方法[{}]返回结果,key:[{}],过期时间[{}秒]", new Object[]{signature, cacheConfig.getKey(), cache.expire()});
				}
				try {
					cacheManager.put(cacheConfig.getKey(), result, cache.expire());
				} catch (Exception e) {
					if(LOGGER.isWarnEnabled()){
						LOGGER.warn("缓存数据失败", e);
					}
				}
			}
		}
		return result;
	}
	
	protected CacheConfig buildCacheInfo(Cache cache, ProceedingJoinPoint joinPoint) throws Exception {
		String signature = joinPoint.getSignature().toString();
		String[] paramNames = null;
		if(PARAM_NAME_MAP.containsKey(signature)){
			paramNames = PARAM_NAME_MAP.get(signature);
        }else {
    		paramNames = getMethodParamNames(joinPoint);
    		PARAM_NAME_MAP.put(signature, paramNames);
		}
		EvaluationContext context = null;
		if(paramNames.length > 0){
			if(paramNames.length == joinPoint.getArgs().length){
				context = new StandardEvaluationContext();
				Object[] args = joinPoint.getArgs();
				for (int i = 0; i < paramNames.length; i++) {
					context.setVariable(paramNames[i], args[i]);
				}
			}else if(LOGGER.isWarnEnabled()){
				LOGGER.warn("方法[{}]上得到的参数长度[{}]与实际[{}]不符", new Object[] { joinPoint.getSignature(), paramNames.length, joinPoint.getArgs().length });
			}
		}
		CacheConfig cacheInfo = new CacheConfig();
		String[] cleans = cache.clean();
		if(cleans.length > 0){
			List<String> keyList = new ArrayList<String>(cleans.length);
			for (int i = 0; i < cleans.length; i++) {
				String key = buildCacheKey(cleans[i], context, null);
				if(StringUtils.isEmpty(key)){
					if(LOGGER.isInfoEnabled()){
						LOGGER.info("忽略无效的缓存key:{表达式:[{}] , 计算值:[{}]}", cleans[i], key);
					}
				}else {
					keyList.add(key);
				}
			}
			cacheInfo.setClean(keyList.toArray(new String[0]));
		}
		cacheInfo.setKey(buildCacheKey(cache.key(), context, cleans.length == 0 ? signature : null));
		cacheInfo.setExpire(cache.expire());
		return cacheInfo;
	}
	
	protected String buildCacheKey(String expression, EvaluationContext context, String defaultKey){
		if(context != null  && StringUtils.isNotEmpty(expression)){
			Expression exp = EXPRESSION_CACHE.get(expression);
			if(exp == null){
				exp = getParser().parseExpression(expression);
				EXPRESSION_CACHE.put(expression, exp);
			}
			expression = context == null ? exp.getValue(String.class) : exp.getValue(context, String.class);
		}
		return StringUtils.isEmpty(expression) ? defaultKey : expression;
	}
	
	private ExpressionParser getParser() {
		ExpressionParser parser = parserHolder.get();
		if(parser == null){
			parser = new SpelExpressionParser();
			parserHolder.set(parser);
		}
		return parser;
	}
	
	/**
	 * 从连接点中获取方法参数名
	 * @param joinPoint
	 * @return
	 * @throws Exception
	 */
	private String[] getMethodParamNames(ProceedingJoinPoint joinPoint) throws Exception {
		Class<?> target = joinPoint.getTarget().getClass();
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (method.getDeclaringClass().isInterface()) {
			try {
				method = target.getDeclaredMethod(method.getName(), parameterTypes);
			} catch (SecurityException e) {
				if(LOGGER.isWarnEnabled()){
					LOGGER.warn("在实现类中获取方法[" + method.getName() + "]定义失败", e);
				}
			} catch (NoSuchMethodException e) {
				if(LOGGER.isWarnEnabled()){
					LOGGER.warn("实现类中没有目标方法[" + method.getName() + "]", e);
				}
			}
		}
		CtClass[] paramTypes = new CtClass[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			paramTypes[i] = classPool.getCtClass(parameterTypes[i].getName());
		}
		CtClass cc = classPool.get(target.getName());
		CtMethod cm = getMethod(cc, method.getName(), paramTypes);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        String[] paramNames = null;
        if (attr == null) {
        	paramNames = new String[0];
        	if(LOGGER.isWarnEnabled()){
            	LOGGER.warn("未找到方法[{}]的本地变量信息", method.getName());
            }
        }else {
    		paramNames = new String[parameterTypes.length];
        	int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        	for (int i = 0; i < paramNames.length; i++) {
        		paramNames[i] = attr.variableName(i + pos);
        	}
		}
        return paramNames;
    }

	private CtMethod getMethod(CtClass cc, String method, CtClass[] paramTypes) throws NotFoundException{
		try {
			return cc.getDeclaredMethod(method, paramTypes);
		} catch (NotFoundException e) {
			return getMethod(cc.getSuperclass(), method, paramTypes);
		}
	}
	
	public List<CacheManager> getCacheManagers() {
		return cacheManagers;
	}

	public void setCacheManagers(List<CacheManager> cacheManagers) {
		this.cacheManagers = cacheManagers;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		CacheServiceExcutor.setCacheManagers(cacheManagers);
	}

	
	
}