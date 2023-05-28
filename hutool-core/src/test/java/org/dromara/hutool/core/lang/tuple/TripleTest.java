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

package org.dromara.hutool.core.lang.tuple;

import org.dromara.hutool.core.lang.mutable.MutableTriple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Triple} 三元组单元测试
 * {@link MutableTriple} 三元组单元测试
 *
 * @author kirno7
 */
public class TripleTest {

	@Test
	public void mutableTripleTest() {
		final MutableTriple<String, String, String> mutableTriple = MutableTriple
			.of("1", "1", "1");
		Assertions.assertEquals("Triple{left=1, middle=1, right=1}", mutableTriple.toString());

		mutableTriple.setLeft("2");
		mutableTriple.setMiddle("2");
		mutableTriple.setRight("2");
		Assertions.assertEquals("Triple{left=2, middle=2, right=2}", mutableTriple.toString());
	}

	@Test
	public void tripleTest() {
		final Triple<String, String, String> triple = Triple
			.of("3", "3", "3");
		Assertions.assertEquals("Triple{left=3, middle=3, right=3}", triple.toString());
	}
}
