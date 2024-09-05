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
