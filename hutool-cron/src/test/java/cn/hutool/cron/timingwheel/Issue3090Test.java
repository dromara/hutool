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

package cn.hutool.cron.timingwheel;

import cn.hutool.core.lang.Console;

public class Issue3090Test {
	public static void main(String[] args) {
		final SystemTimer timer = new SystemTimer();
		timer.setDelayQueueTimeout(1000);
		timer.start();
		timer.addTask(new TimerTask(() -> {
			Console.log(1);
			Console.log("任务已经完成");
		}, 1000));

		timer.stop();
		Console.log("线程池已经关闭");
	}
}
