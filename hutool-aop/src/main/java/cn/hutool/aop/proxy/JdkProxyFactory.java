package cn.hutool.aop.proxy;

import cn.hutool.aop.ProxyUtil;
import cn.hutool.aop.aspects.Aspect;
import cn.hutool.aop.interceptor.JdkInterceptor;

/**
 * JDK实现的切面代理
 *
 * @author looly
 */
public class JdkProxyFactory extends ProxyFactory {
	private static final long serialVersionUID = 1L;

	@Override
	public <T> T proxy(T target, Aspect aspect) {
		return ProxyUtil.newProxyInstance(//
				target.getClass().getClassLoader(), //
				new JdkInterceptor(target, aspect), //
				target.getClass().getInterfaces());
	}
}
