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

package org.dromara.hutool.core.func;

import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.func.PredicateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PredicateUtilTest {

	@Test
	public void notContainsTest() {
		final Set<String> sets = SetUtil.of("1", "2", "3");
		final List<String> collect = Stream.of("3", "4", "5")
				.filter(PredicateUtil.negate(sets::contains))
				.collect(Collectors.toList());

		Assertions.assertEquals(2, collect.size());
		Assertions.assertEquals("4", collect.get(0));
		Assertions.assertEquals("5", collect.get(1));
	}

	@Test
	public void andTest() {
		boolean condition = Stream.of(1, 3, 5)
				.allMatch(
						PredicateUtil.and(
								Objects::nonNull,
								i -> i < 10,
								i -> i % 2 == 1
						)
				);
		Assertions.assertTrue(condition);
	}

	@Test
	public void orTest() {
		boolean condition = Stream.of(1, 3, 5)
				.anyMatch(
						PredicateUtil.or(
								Objects::isNull,
								i -> i > 10,
								i -> i % 2 == 0
						)
				);
		Assertions.assertFalse(condition);
	}


}
