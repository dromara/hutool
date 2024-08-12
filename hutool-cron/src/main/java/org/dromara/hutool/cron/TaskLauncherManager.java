/*
 * Copyright (c) 2013-2024 Hutool Team.
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
