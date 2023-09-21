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
