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

package org.dromara.hutool.core.reflect.method;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

/**
 * test for {@link MethodMatcher}
 *
 * @author huangchengxing
 */
class MethodMatcherTest {

	private final MethodMatcher matchToString = t -> "toString".equals(t.getName());

	@SneakyThrows
	@Test
	void test() {
		final Method toString = Object.class.getDeclaredMethod("toString");
		Assertions.assertTrue(matchToString.test(toString));
		final Method hashCode = Object.class.getDeclaredMethod("hashCode");
		Assertions.assertFalse(matchToString.test(hashCode));
	}

	@SneakyThrows
	@Test
	void and() {
		final Method toString = Object.class.getDeclaredMethod("toString");
		Assertions.assertTrue(matchToString.test(toString));
		final MethodMatcher newMatcher = matchToString.and(t -> t.getReturnType() == String.class);
		Assertions.assertTrue(newMatcher.test(toString));
	}

	@SneakyThrows
	@Test
	void negate() {
		final Method toString = Object.class.getDeclaredMethod("toString");
		Assertions.assertTrue(matchToString.test(toString));
		final MethodMatcher newMatcher = matchToString.negate();
		Assertions.assertFalse(newMatcher.test(toString));
	}

	@SneakyThrows
	@Test
	void or() {
		final MethodMatcher newMatcher = matchToString.or(t -> "hashCode".equals(t.getName()));
		final Method toString = Object.class.getDeclaredMethod("toString");
		final Method hashCode = Object.class.getDeclaredMethod("hashCode");
		Assertions.assertTrue(newMatcher.test(toString));
		Assertions.assertTrue(newMatcher.test(hashCode));
	}

	@SneakyThrows
	@Test
	void inspect() {
		final Method toString = Object.class.getDeclaredMethod("toString");
		Assertions.assertTrue(matchToString.inspect(toString));
		final Method hashCode = Object.class.getDeclaredMethod("hashCode");
		Assertions.assertNull(matchToString.inspect(hashCode));
	}
}
