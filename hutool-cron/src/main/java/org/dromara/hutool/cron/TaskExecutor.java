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

import org.dromara.hutool.cron.task.CronTask;
import org.dromara.hutool.cron.task.Task;

/**
 * 作业执行器<br>
 * 执行具体的作业，执行完毕销毁<br>
 * 作业执行器唯一关联一个作业，负责管理作业的运行的生命周期。
 *
 * @author Looly
 */
public class TaskExecutor implements Runnable {

	private final Scheduler scheduler;
	private final CronTask task;

	/**
	 * 获得原始任务对象
	 *
	 * @return 任务对象
	 */
	public Task getTask() {
		return this.task.getRaw();
	}

	/**
	 * 获得原始任务对象
	 *
	 * @return 任务对象
	 * @since 5.4.7
	 */
	public CronTask getCronTask() {
		return this.task;
	}

	/**
	 * 构造
	 *
	 * @param scheduler 调度器
	 * @param task 被执行的任务
	 */
	public TaskExecutor(final Scheduler scheduler, final CronTask task) {
		this.scheduler = scheduler;
		this.task = task;
	}

	@Override
	public void run() {
		try {
			scheduler.listenerManager.notifyTaskStart(this);
			task.execute();
			scheduler.listenerManager.notifyTaskSucceeded(this);
		} catch (final Exception e) {
			scheduler.listenerManager.notifyTaskFailed(this, e);
		} finally {
			scheduler.taskExecutorManager.notifyExecutorCompleted(this);
		}
	}
}
