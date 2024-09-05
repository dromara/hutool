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
