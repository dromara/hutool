package cn.hutool.cron;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.thread.ThreadUtil;

public class TaskLauncherManager {
	
	protected Scheduler scheduler;
	/** 启动器列表 */
	protected List<TaskLauncher> launchers = new ArrayList<>();
	
	public TaskLauncherManager(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * 启动 TaskLauncher
	 * @param millis 触发事件的毫秒数
	 * @return {@link TaskLauncher}
	 */
	protected TaskLauncher spawnLauncher(long millis) {
		final TaskLauncher launcher = new TaskLauncher(this.scheduler, millis);
		synchronized (this.launchers) {
			this.launchers.add(launcher);
		}
		launcher.setDaemon(this.scheduler.daemon);
		launcher.start();
		return launcher;
	}
	
	/**
	 * 启动器启动完毕，启动完毕后从执行器列表中移除
	 * @param launcher 启动器 {@link TaskLauncher}
	 */
	protected void notifyLauncherCompleted(TaskLauncher launcher) {
		synchronized (launchers) {
			launchers.remove(launcher);
		}
	}

	/**
	 * 停止所有TaskExecutor
	 * @return this
	 */
	public TaskLauncherManager destroy() {
		synchronized (this.launchers) {
			for (TaskLauncher taskLauncher : launchers) {
				ThreadUtil.interupt(taskLauncher, true);
			}
		}
		this.launchers.clear();
		return this;
	}
}
