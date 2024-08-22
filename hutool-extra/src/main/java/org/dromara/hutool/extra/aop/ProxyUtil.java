/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
