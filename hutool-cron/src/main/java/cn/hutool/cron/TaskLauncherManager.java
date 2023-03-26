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

package cn.hutool.cron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作业启动管理器
 *
 * @author looly
 *
 */
public class TaskLauncherManager implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Scheduler scheduler;
	/** 启动器列表 */
	protected final List<TaskLauncher> launchers = new ArrayList<>();

	/**
	 * 构造
	 * @param scheduler {@link Scheduler}
	 */
	public TaskLauncherManager(final Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 启动 TaskLauncher
	 * @param millis 触发事件的毫秒数
	 * @return {@link TaskLauncher}
	 */
	protected TaskLauncher spawnLauncher(final long millis) {
		final TaskLauncher launcher = new TaskLauncher(this.scheduler, millis);
		synchronized (this.launchers) {
			this.launchers.add(launcher);
		}
		this.scheduler.threadExecutor.execute(launcher);
		return launcher;
	}

	/**
	 * 启动器启动完毕，启动完毕后从执行器列表中移除
	 * @param launcher 启动器 {@link TaskLauncher}
	 */
	protected void notifyLauncherCompleted(final TaskLauncher launcher) {
		synchronized (launchers) {
			launchers.remove(launcher);
		}
	}
}
