/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.cache;

import org.dromara.hutool.core.cache.impl.TimedCache;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * https://gitee.com/dromara/hutool/issues/I8MEIX<br>
 * get操作非原子
 */
public class IssueI8MEIXTest {
	@Test
	@Disabled
	void getRemoveTest() {
		final TimedCache<String, String> cache = new TimedCache<>(200);
		cache.put("a", "123");

		ThreadUtil.sleep(300);

		// 测试时，在get后的remove前加sleep测试在读取过程中put新值的问题
		ThreadUtil.execute(() -> {
			Console.log("get begin.");
			Console.log(cache.get("a"));
		});

		ThreadUtil.execute(() -> {
			ThreadUtil.sleep(200);
			cache.put("a", "456");
			Console.log("put ok.");
		});

		ThreadUtil.sleep(1000);
	}
}
