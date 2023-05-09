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

import org.dromara.hutool.core.lang.mutable.MutablePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Pair} 二元组单元测试
 * {@link MutablePair} 二元组单元测试
 *
 * @author liuchao
 */
public class PairTest {

	@Test
	public void pairTest() {
		final Pair<String, String> pair = Pair.of("l", "r");
		Assertions.assertEquals("Pair{left=l, right=r}", pair.toString());
	}

	@Test
	public void mutablePairTest() {
		final MutablePair<String, String> mutablePair = MutablePair.of("l", "r");
		Assertions.assertEquals("Pair{left=l, right=r}", mutablePair.toString());

		mutablePair.setLeft("L");
		mutablePair.setRight("R");
		Assertions.assertEquals("Pair{left=L, right=R}", mutablePair.toString());
	}
}
