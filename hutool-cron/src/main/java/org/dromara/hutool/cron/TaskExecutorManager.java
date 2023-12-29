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

package org.dromara.hutool.cron;

import org.dromara.hutool.cron.task.CronTask;
import org.dromara.hutool.cron.task.Task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作业执行管理器<br>
 * 负责管理作业的启动、停止等
 *
 * <p>
 * 此类用于管理正在运行的作业情况，作业启动后加入任务列表，任务结束移除
 * </p>
 *
 * @author Looly
 * @since 3.0.1
 */
public class TaskExecutorManager implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Scheduler scheduler;
	/**
	 * 执行器列表
	 */
	private final List<TaskExecutor> executors = new ArrayList<>();

	/**
	 * 构造
	 *
	 * @param scheduler {@link Scheduler}
	 */
	public TaskExecutorManager(final Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 获取所有正在执行的任务调度执行器
	 *
	 * @return 任务执行器列表
	 * @since 4.6.7
	 */
	public List<TaskExecutor> getExecutors() {
		return Collections.unmodifiableList(this.executors);
	}

	/**
	 * 启动 执行器TaskExecutor，即启动作业
	 *
	 * @param task {@link Task}
	 * @return {@link TaskExecutor}
	 */
	public TaskExecutor spawnExecutor(final CronTask task) {
		final TaskExecutor executor = new TaskExecutor(this.scheduler, task);
		synchronized (this.executors) {
			this.executors.add(executor);
		}
		this.scheduler.threadExecutor.execute(executor);
		return executor;
	}

	/**
	 * 执行器执行完毕调用此方法，将执行器从执行器列表移除，此方法由{@link TaskExecutor}对象调用，用于通知管理器自身已完成执行
	 *
	 * @param executor 执行器 {@link TaskExecutor}
	 * @return this
	 */
	public TaskExecutorManager notifyExecutorCompleted(final TaskExecutor executor) {
		synchronized (executors) {
			executors.remove(executor);
		}
		return this;
	}
}
