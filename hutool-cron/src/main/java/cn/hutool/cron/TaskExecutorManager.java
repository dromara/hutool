package cn.hutool.cron;

import cn.hutool.cron.task.CronTask;
import cn.hutool.cron.task.Task;

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

	public TaskExecutorManager(Scheduler scheduler) {
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
	public TaskExecutor spawnExecutor(CronTask task) {
		final TaskExecutor executor = new TaskExecutor(this.scheduler, task);
		synchronized (this.executors) {
			this.executors.add(executor);
		}
		// 子线程是否为deamon线程取决于父线程，因此此处无需显示调用
		// executor.setDaemon(this.scheduler.daemon);
//		executor.start();
		this.scheduler.threadExecutor.execute(executor);
		return executor;
	}

	/**
	 * 执行器执行完毕调用此方法，将执行器从执行器列表移除，此方法由{@link TaskExecutor}对象调用，用于通知管理器自身已完成执行
	 *
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
	 *
	 * @return this
	 * @deprecated 作业执行器只是执行给定的定时任务线程，无法强制关闭，可通过deamon线程方式关闭之
	 */
	@Deprecated
	public TaskExecutorManager destroy() {
		// synchronized (this.executors) {
		// for (TaskExecutor taskExecutor : executors) {
		// ThreadUtil.interupt(taskExecutor, false);
		// }
		// }
		this.executors.clear();
		return this;
	}
}
