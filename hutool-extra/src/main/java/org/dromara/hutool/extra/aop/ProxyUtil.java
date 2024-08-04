/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.aop;

import org.dromara.hutool.core.reflect.JdkProxyUtil;
import org.dromara.hutool.extra.aop.engine.ProxyEngine;
import org.dromara.hutool.extra.aop.engine.ProxyEngineFactory;

/**
 * 代理工具类
 *
 * @author Looly
 */
public class ProxyUtil extends JdkProxyUtil {

	/**
	 * 获取动态代理引擎
	 *
	 * @return {@link ProxyEngine}
	 */
	public static ProxyEngine getEngine() {
		return ProxyEngineFactory.getEngine();
	}

	/**
	 * 使用切面代理对象
	 *
	 * @param <T>         切面对象类型
	 * @param target      目标对象
	 * @param aspectClass 切面对象类
	 * @return 代理对象
	 */
	public static <T> T proxy(final T target, final Class<? extends Aspect> aspectClass) {
		return getEngine().proxy(target, aspectClass);
	}

	/**
	 * 使用切面代理对象
	 *
	 * @param <T>    被代理对象类型
	 * @param target 被代理对象
	 * @param aspect 切面对象
	 * @return 代理对象
	 */
	public static <T> T proxy(final T target, final Aspect aspect) {
		return getEngine().proxy(target, aspect);
	}
}
