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
