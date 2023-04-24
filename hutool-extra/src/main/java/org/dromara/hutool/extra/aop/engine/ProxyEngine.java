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

package org.dromara.hutool.extra.aop.engine;

import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.extra.aop.Aspect;

/**
 * 动态代理引擎接口
 *
 * @author looly
 * @since 6.0.0
 */
public interface ProxyEngine {
	/**
	 * 创建代理
	 *
	 * @param <T>    代理对象类型
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 * @return 代理对象
	 */
	<T> T proxy(T target, Aspect aspect);

	/**
	 * 创建代理
	 *
	 * @param <T>         代理对象类型
	 * @param target      被代理对象
	 * @param aspectClass 切面实现类，自动实例化
	 * @return 代理对象
	 * @since 5.3.1
	 */
	default <T> T proxy(final T target, final Class<? extends Aspect> aspectClass) {
		return proxy(target, ConstructorUtil.newInstanceIfPossible(aspectClass));
	}
}
