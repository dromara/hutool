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
