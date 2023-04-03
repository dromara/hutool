/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.aop.proxy;

import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.util.ServiceLoaderUtil;
import org.dromara.hutool.extra.aop.aspects.Aspect;

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
