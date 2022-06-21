package cn.hutool.extra.aop.proxy;

import cn.hutool.core.reflect.ConstructorUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import cn.hutool.extra.aop.aspects.Aspect;

import java.io.Serializable;

/**
 * 代理工厂<br>
 * 根据用户引入代理库的不同，产生不同的代理对象
 *
 * @author looly
 */
public abstract class ProxyFactory implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建代理
	 *
	 * @param <T>         代理对象类型
	 * @param target      被代理对象
	 * @param aspectClass 切面实现类，自动实例化
	 * @return 代理对象
	 * @since 5.3.1
	 */
	public <T> T proxy(final T target, final Class<? extends Aspect> aspectClass) {
		return proxy(target, ConstructorUtil.newInstanceIfPossible(aspectClass));
	}

	/**
	 * 创建代理
	 *
	 * @param <T>    代理对象类型
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 * @return 代理对象
	 */
	public abstract <T> T proxy(T target, Aspect aspect);

	/**
	 * 根据用户引入Cglib与否自动创建代理对象
	 *
	 * @param <T>         切面对象类型
	 * @param target      目标对象
	 * @param aspectClass 切面对象类
	 * @return 代理对象
	 */
	public static <T> T createProxy(final T target, final Class<? extends Aspect> aspectClass) {
		return createProxy(target, ConstructorUtil.newInstance(aspectClass));
	}

	/**
	 * 根据用户引入Cglib与否自动创建代理对象
	 *
	 * @param <T>    切面对象类型
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 * @return 代理对象
	 */
	public static <T> T createProxy(final T target, final Aspect aspect) {
		return of().proxy(target, aspect);
	}

	/**
	 * 根据用户引入Cglib与否创建代理工厂
	 *
	 * @return 代理工厂
	 */
	public static ProxyFactory of() {
		return ServiceLoaderUtil.loadFirstAvailable(ProxyFactory.class);
	}
}
