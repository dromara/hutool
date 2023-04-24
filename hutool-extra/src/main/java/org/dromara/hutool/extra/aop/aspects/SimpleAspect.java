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

package org.dromara.hutool.extra.aop.aspects;

import org.dromara.hutool.extra.aop.Aspect;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 简单切面类，不做任何操作<br>
 * 可以继承此类实现自己需要的方法即可
 *
 * @author Looly, ted.L
 */
public class SimpleAspect implements Aspect, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean before(final Object target, final Method method, final Object[] args) {
		//继承此类后实现此方法
		return true;
	}

	@Override
	public boolean after(final Object target, final Method method, final Object[] args, final Object returnVal) {
		//继承此类后实现此方法
		return true;
	}

	@Override
	public boolean afterException(final Object target, final Method method, final Object[] args, final Throwable e) {
		//继承此类后实现此方法
		return true;
	}

}
