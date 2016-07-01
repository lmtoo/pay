package com.somnus.pay.cache.config;

import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.context.annotation.AnnotationConfigBeanDefinitionParser;
import org.w3c.dom.Element;

import com.somnus.pay.cache.CacheAspect;

public class AnnotationDrivenBeanDefinitionParser extends AnnotationConfigBeanDefinitionParser {

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		super.parse(element, parserContext);
		AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext, element);
		RootBeanDefinition beanDefinition = new RootBeanDefinition(CacheAspect.class);
        beanDefinition.setLazyInit(false);
		return beanDefinition;
	}

}