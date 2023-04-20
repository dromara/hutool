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

package org.dromara.hutool.extra.aop.interceptor;

import org.dromara.hutool.extra.aop.aspects.Aspect;

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
