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

package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.lang.Console;

import java.util.concurrent.ExecutorService;

public class Issue3167Test {
	public static void main(final String[] args) {
		final ExecutorService executorService = ThreadUtil.newExecutor(2);

		for (int i = 0; i < 1035; i++) {
			final int finalI = i;
			executorService.submit(() -> {
				Console.log(Thread.currentThread().getName(), finalI);
				ThreadUtil.sleep(5000);
			});
		}
	}
}
