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

import java.io.Serializable;

/**
 * 简单拦截器，保存了被拦截的对象和Aspect实现
 *
 * @author looly
 * @since 6.0.0
 */
public class SimpleInterceptor implements Serializable {
	private static final long serialVersionUID = 1L;

	protected final Object target;
	protected final Aspect aspect;

	/**
	 * 构造
	 *
	 * @param target 被代理对象
	 * @param aspect 切面实现
	 */
	public SimpleInterceptor(final Object target, final Aspect aspect) {
		this.target = target;
		this.aspect = aspect;
	}

	/**
	 * 获取目标对象
	 *
	 * @return 目标对象
	 */
	public Object getTarget() {
		return this.target;
	}

}
