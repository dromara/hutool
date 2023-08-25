package cn.hutool.aop.proxy;

import cn.hutool.aop.aspects.Aspect;
import cn.hutool.aop.interceptor.CglibInterceptor;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Constructor;

/**
 * 基于Cglib的切面代理工厂
 *
 * @author looly
 *
 */
public class CglibProxyFactory extends ProxyFactory{
	private static final long serialVersionUID = 1L;

	@Override
	public <T> T proxy(T target, Aspect aspect) {
		final Class<?> targetClass = target.getClass();

		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(new CglibInterceptor(target, aspect));
		return create(enhancer, targetClass);
	}

	/**
	 * 创建代理对象<br>
	 * https://gitee.com/dromara/hutool/issues/I74EX7<br>
	 * 某些对象存在非空参数构造，则需遍历查找需要的构造完成代理对象构建。
	 *
	 * @param <T>         代理对象类型
	 * @param enhancer    {@link org.springframework.cglib.proxy.Enhancer}
	 * @param targetClass 目标类型
	 * @return 代理对象
	 */
	@SuppressWarnings("unchecked")
	private static <T> T create(final Enhancer enhancer, final Class<?> targetClass) {
		final Constructor<?>[] constructors = ReflectUtil.getConstructors(targetClass);
		Class<?>[] parameterTypes;
		Object[] values;
		IllegalArgumentException finalException = null;
		for (final Constructor<?> constructor : constructors) {
			parameterTypes = constructor.getParameterTypes();
			values = ClassUtil.getDefaultValues(parameterTypes);

			try {
				return (T) enhancer.create(parameterTypes, values);
			} catch (final IllegalArgumentException e) {
				//ignore
				finalException = e;
			}
		}
		if (null != finalException) {
			throw finalException;
		}

		throw new IllegalArgumentException("No constructor provided");
	}
}
