/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.lang.caller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CallerUtil} 单元测试
 * @author Looly
 *
 */
public class CallerTest {

	@Test
	public void getCallerTest() {
		final Class<?> caller = CallerUtil.getCaller();
		Assertions.assertEquals(this.getClass(), caller);

		final Class<?> caller0 = CallerUtil.getCaller(0);
		Assertions.assertEquals(CallerUtil.class, caller0);

		final Class<?> caller1 = CallerUtil.getCaller(1);
		Assertions.assertEquals(this.getClass(), caller1);
	}

	@Test
	public void getCallerCallerTest() {
		final Class<?> callerCaller = CallerTestClass.getCaller();
		Assertions.assertEquals(this.getClass(), callerCaller);
	}

	private static class CallerTestClass{
		public static Class<?> getCaller(){
			return CallerUtil.getCallerCaller();
		}
	}
}
