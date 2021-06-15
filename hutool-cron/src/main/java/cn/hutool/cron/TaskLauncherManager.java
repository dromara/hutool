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
		//子线程是否为deamon线程取决于父线程，因此此处无需显示调用
		//launcher.setDaemon(this.scheduler.daemon);
//		launcher.start();
		this.scheduler.threadExecutor.execute(launcher);
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
}
