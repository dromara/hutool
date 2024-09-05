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

import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.cron.CronUtil;
import org.dromara.hutool.cron.task.InvokeTask;

public class DeamonMainTest {
	public static void main(final String[] args) {
		// 测试守护线程是否对作业线程有效
		CronUtil.schedule("*/2 * * * * *", new InvokeTask("org.dromara.hutool.cron.demo.TestJob.doWhileTest"));
		// 当为守护线程时，stop方法调用后doWhileTest里的循环输出将终止，表示作业线程正常结束
		// 当非守护线程时，stop方法调用后，不再产生新的作业，原作业正常执行。
		CronUtil.setMatchSecond(true);
		CronUtil.start(true);

		ThreadUtil.sleep(3000);
		CronUtil.stop();
	}
}
