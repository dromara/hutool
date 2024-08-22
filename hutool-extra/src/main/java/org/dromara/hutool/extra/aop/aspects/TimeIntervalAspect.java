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
