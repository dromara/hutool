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

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class FunctionPoolTest {

	@Test
	public void createStringTest() {
		// 预热
		FunctionPool.createString("123".toCharArray());

		// 测试数据
		final ArrayList<char[]> list = ListUtil.of();
		for (int i = 0; i < 100000; i++) {
			list.add(RandomUtil.randomStringLower(100).toCharArray());
		}

		final StopWatch stopWatch = DateUtil.createStopWatch();
		stopWatch.start("copy creator");
		for (final char[] value : list) {
			new String(value);
		}
		stopWatch.stop();

		stopWatch.start("zero copy creator");
		for (final char[] value : list) {
			FunctionPool.createString(value);
		}
		stopWatch.stop();

		//Console.log(stopWatch.prettyPrint());
	}
}
