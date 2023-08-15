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

package org.dromara.hutool.cron.timingwheel;

public class Issue3090Test {
	public static void main(String[] args) {
		SystemTimer timer = new SystemTimer();
		timer.setDelayQueueTimeout(1000);
		timer.start();
		timer.addTask(new TimerTask(() -> {
			System.out.println(1);
			System.out.println("任务已经完成");
		}, 1000));

		timer.stop();
		System.out.println("线程池已经关闭");
	}
}
