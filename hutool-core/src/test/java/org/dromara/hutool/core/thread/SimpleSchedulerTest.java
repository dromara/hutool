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

package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.RuntimeUtil;

/**
 * 简单定时任务测试
 */
public class SimpleSchedulerTest {
	public static void main(final String[] args) {

		// 新建一个定时任务，定时获取内存信息
		final SimpleScheduler<Long> scheduler = new SimpleScheduler<>(new SimpleScheduler.Job<Long>() {
			private volatile long maxAvailable;

			@Override
			public Long getResult() {
				return this.maxAvailable;
			}

			@Override
			public void run() {
				this.maxAvailable = RuntimeUtil.getFreeMemory();
			}
		}, 50);

		// 另一个线程不停获取内存结果计算值
		ThreadUtil.execAsync(() -> {
			//noinspection InfiniteLoopStatement
			while (true) {
				Console.log(FileUtil.readableFileSize(scheduler.getResult()));
				ThreadUtil.sleep(1000);
			}
		});
	}
}
