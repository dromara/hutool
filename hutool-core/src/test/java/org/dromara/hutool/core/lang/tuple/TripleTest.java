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
