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

package org.dromara.hutool.cron.demo;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.core.data.id.IdUtil;

/**
 * 测试定时任务，当触发到定时的时间点时，执行doTest方法
 *
 * @author looly
 *
 */
public class TestJob {

	private final String jobId = IdUtil.simpleUUID();

	/**
	 * 执行定时任务内容
	 */
	public void doTest() {
//		String name = Thread.currentThread().getName();
		Console.log("Test Job {} running... at {}", jobId, DateUtil.formatNow());
	}

	/**
	 * 执行循环定时任务，测试在定时任务结束时作为deamon线程是否能正常结束
	 */
	@SuppressWarnings("InfiniteLoopStatement")
	public void doWhileTest() {
		final String name = Thread.currentThread().getName();
		while (true) {
			Console.log("Job {} while running...", name);
			ThreadUtil.sleep(2000);
		}
	}
}
