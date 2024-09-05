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
