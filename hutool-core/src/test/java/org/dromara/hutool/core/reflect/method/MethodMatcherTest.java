/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
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
import java.util.function.Predicate;

/**
 * @author huangchengxing
 */
class MethodMatcherTest {

	private final Predicate<Method> matchToString = t -> "toString".equals(t.getName());

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
		final Predicate<Method> newMatcher = matchToString.and(t -> t.getReturnType() == String.class);
		Assertions.assertTrue(newMatcher.test(toString));
	}

	@SneakyThrows
	@Test
	void negate() {
		final Method toString = Object.class.getDeclaredMethod("toString");
		Assertions.assertTrue(matchToString.test(toString));
		final Predicate<Method> newMatcher = matchToString.negate();
		Assertions.assertFalse(newMatcher.test(toString));
	}

	@SneakyThrows
	@Test
	void or() {
		final Predicate<Method> newMatcher = matchToString.or(t -> "hashCode".equals(t.getName()));
		final Method toString = Object.class.getDeclaredMethod("toString");
		final Method hashCode = Object.class.getDeclaredMethod("hashCode");
		Assertions.assertTrue(newMatcher.test(toString));
		Assertions.assertTrue(newMatcher.test(hashCode));
	}

	@SneakyThrows
	@Test
	void inspect() {
		final Method toString = Object.class.getDeclaredMethod("toString");
		Assertions.assertTrue(matchToString.test(toString));
		final Method hashCode = Object.class.getDeclaredMethod("hashCode");
		Assertions.assertFalse(matchToString.test(hashCode));
	}
}
