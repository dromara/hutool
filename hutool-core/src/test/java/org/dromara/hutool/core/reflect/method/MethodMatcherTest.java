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
