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

import org.dromara.hutool.core.lang.mutable.MutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Pair} 二元组单元测试
 * {@link MutablePair} 二元组单元测试
 *
 * @author looly
 */
public class PairTest {

	@Test
	public void mutablePairTest() {
		final MutablePair<String, String> pair = MutablePair
			.of("1", "1");
		Assertions.assertEquals("Pair{left=1, right=1}", pair.toString());

		pair.setLeft("2");
		pair.setRight("2");
		Assertions.assertEquals("Pair{left=2, right=2}", pair.toString());
	}

	@Test
	public void pairTest() {
		final Pair<String, String> triple = Pair
			.of("3", "3");
		Assertions.assertEquals("Pair{left=3, right=3}", triple.toString());
	}
}
