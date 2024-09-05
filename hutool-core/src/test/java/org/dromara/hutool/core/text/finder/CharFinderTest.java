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

package org.dromara.hutool.core.text.finder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharFinderTest {

	@Test
	public void startTest(){
		int start = new CharFinder('a').setText("cba123").start(2);
		Assertions.assertEquals(2, start);

		start = new CharFinder('c').setText("cba123").start(2);
		Assertions.assertEquals(-1, start);

		start = new CharFinder('3').setText("cba123").start(2);
		Assertions.assertEquals(5, start);
	}
	@Test
	public void negativeStartTest(){
		int start = new CharFinder('a').setText("cba123").setNegative(true).start(2);
		Assertions.assertEquals(2, start);

		start = new CharFinder('2').setText("cba123").setNegative(true).start(2);
		Assertions.assertEquals(-1, start);

		start = new CharFinder('c').setText("cba123").setNegative(true).start(2);
		Assertions.assertEquals(0, start);
	}
}
