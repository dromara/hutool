package cn.hutool.extra.aop.proxy;

import cn.hutool.extra.aop.ProxyUtil;
import cn.hutool.extra.aop.aspects.Aspect;
import cn.hutool.extra.aop.interceptor.JdkInterceptor;

/**
 * JDK实现的切面代理
 *
 * @author looly
 */
public class JdkProxyFactory extends ProxyFactory {
	private static final long serialVersionUID = 1L;

	@Override
	public <T> T proxy(final T target, final Aspect aspect) {
		return ProxyUtil.newProxyInstance(//
				target.getClass().getClassLoader(), //
				new JdkInterceptor(target, aspect), //
				target.getClass().getInterfaces());
	}
}
