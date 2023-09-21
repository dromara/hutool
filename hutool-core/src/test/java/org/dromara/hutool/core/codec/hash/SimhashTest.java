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

package org.dromara.hutool.core.codec.hash;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.split.SplitUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimhashTest {

	@Test
	public void simTest() {
		final String text1 = "我是 一个 普通 字符串";
		final String text2 = "我是 一个 普通 字符串";

		final Simhash simhash = new Simhash();
		final long hash = simhash.hash64(SplitUtil.split(text1, StrUtil.SPACE));
		Assertions.assertTrue(hash != 0);

		simhash.store(hash);
		final boolean duplicate = simhash.equals(SplitUtil.split(text2, StrUtil.SPACE));
		Assertions.assertTrue(duplicate);
	}
}
