package cn.hutool.cron;

import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.task.Task;

/**
 * 任务执行管理器<br>
 * 负责管理任务的启动、停止等
 * 
 * @author Looly
 * @since 3.0.1
 */
public class TaskExecutorManager {

	protected Scheduler scheduler;
	/** 执行器列表 */
	private List<TaskExecutor> executors = new ArrayList<>();
	
	public TaskExecutorManager(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	
	/**
	 * 启动 TaskExecutor
	 * @param task {@link Task}
	 * @return {@link TaskExecutor}
	 */
	public TaskExecutor spawnExecutor(Task task) {
		final TaskExecutor executor = new TaskExecutor(this.scheduler, task);
		synchronized (this.executors) {
			this.executors.add(executor);
		}
		executor.setDaemon(this.scheduler.daemon);
		executor.start();
		return executor;
	}
	
	/**
	 * 执行器执行完毕调用此方法，将执行器从执行器列表移除
	 * @param executor 执行器 {@link TaskExecutor}
	 * @return this
	 */
	public TaskExecutorManager notifyExecutorCompleted(TaskExecutor executor) {
		synchronized (executors) {
			executors.remove(executor);
		}
		return this;
	}

	/**
	 * 停止所有TaskExecutor
	 * @return this
	 */
	public TaskExecutorManager destroy() {
		synchronized (this.executors) {
			for (TaskExecutor taskExecutor : executors) {
				ThreadUtil.interupt(taskExecutor, true);
			}
		}
		this.executors.clear();
		return this;
	}
}
