/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.cron;

/**
 * 作业启动器<br>
 * 负责检查 {@link TaskTable} 是否有匹配到此时运行的Task<br>
 * 检查完毕后启动器结束
 *
 * @author Looly
 */
public class TaskLauncher implements Runnable {

	private final Scheduler scheduler;
	private final long millis;

	/**
	 * 构造
	 *
	 * @param scheduler {@link Scheduler}
	 * @param millis    毫秒数
	 */
	public TaskLauncher(final Scheduler scheduler, final long millis) {
		this.scheduler = scheduler;
		this.millis = millis;
	}

	@Override
	public void run() {
		//匹配秒部分由用户定义决定，始终不匹配年
		scheduler.taskTable.executeTaskIfMatch(this.scheduler, this.millis);

		//结束通知
		scheduler.taskLauncherManager.notifyLauncherCompleted(this);
	}
}
