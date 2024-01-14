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
