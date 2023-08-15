/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.extra.aop.aspects;

import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;

import java.lang.reflect.Method;

/**
 * 通过日志打印方法的执行时间的切面
 *
 * @author Looly
 */
public class TimeIntervalAspect extends SimpleAspect {
	private static final long serialVersionUID = 1L;

	private final StopWatch interval = new StopWatch();

	@Override
	public boolean before(final Object target, final Method method, final Object[] args) {
		interval.start();
		return true;
	}

	@Override
	public boolean after(final Object target, final Method method, final Object[] args, final Object returnVal) {
		interval.stop();
		Console.log("Method [{}.{}] execute spend [{}]ms return value [{}]",
				target.getClass().getName(), //
				method.getName(), //
				interval.getLastTaskTimeMillis(), //
				returnVal);
		return true;
	}
}
