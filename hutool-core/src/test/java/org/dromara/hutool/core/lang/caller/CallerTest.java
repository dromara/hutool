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
