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

package org.dromara.hutool.extra.aop.engine.spring;

import org.dromara.hutool.extra.aop.Aspect;
import org.dromara.hutool.extra.aop.engine.ProxyEngine;
import org.springframework.cglib.proxy.Enhancer;

/**
 * 基于Spring-cglib的切面代理工厂
 *
 * @author looly
 *
 */
public class SpringCglibProxyEngine implements ProxyEngine {

	@Override
	@SuppressWarnings("unchecked")
	public <T> T proxy(final T target, final Aspect aspect) {
		final Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(new SpringCglibInterceptor(target, aspect));
		return (T) enhancer.create();
	}

}
