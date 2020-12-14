package cn.hutool.cron;

import cn.hutool.cron.task.CronTask;
import cn.hutool.cron.task.Task;

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
	public TaskExecutor(Scheduler scheduler, CronTask task) {
		this.scheduler = scheduler;
		this.task = task;
	}

	@Override
	public void run() {
		try {
			scheduler.listenerManager.notifyTaskStart(this);
			task.execute();
			scheduler.listenerManager.notifyTaskSucceeded(this);
		} catch (Exception e) {
			scheduler.listenerManager.notifyTaskFailed(this, e);
		} finally {
			scheduler.taskExecutorManager.notifyExecutorCompleted(this);
		}
	}
}
