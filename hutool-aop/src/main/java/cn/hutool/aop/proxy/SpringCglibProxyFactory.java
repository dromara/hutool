package cn.hutool.aop.proxy;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.aop.interceptor.SpringCglibInterceptor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * 基于Spring-cglib的切面代理工厂
 * 
 * @author looly
 *
 */
public class SpringCglibProxyFactory extends ProxyFactory{
	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T proxy(T target, Aspect aspect) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(new SpringCglibInterceptor(target, aspect));
		return (T) enhancer.create();
	}

}
