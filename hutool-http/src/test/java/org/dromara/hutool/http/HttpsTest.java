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

package org.dromara.hutool.http;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class HttpsTest {

	/**
	 * 测试单例的SSLSocketFactory是否有线程安全问题
	 */
	@Test
	@Disabled
	public void getTest() {
		final AtomicInteger count = new AtomicInteger();
		for(int i =0; i < 100; i++){
			ThreadUtil.execute(()->{
				final String s = HttpUtil.get("https://www.baidu.com/");
				Console.log(count.incrementAndGet());
			});
		}
		ThreadUtil.sync(this);
	}
}
