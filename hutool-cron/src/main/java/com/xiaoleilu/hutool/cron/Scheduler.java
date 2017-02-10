package com.xiaoleilu.hutool.cron;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import com.xiaoleilu.hutool.cron.listener.TaskListener;
import com.xiaoleilu.hutool.cron.listener.TaskListenerManager;
import com.xiaoleilu.hutool.cron.pattern.CronPattern;
import com.xiaoleilu.hutool.cron.task.RunnableTask;
import com.xiaoleilu.hutool.cron.task.Task;
import com.xiaoleilu.hutool.util.ThreadUtil;

/**
 * 任务调度器<br>
 * 
 * 调度器启动流程：<br>
 * 
 * <pre>
 * 启动Timer -> 启动TaskLauncher -> 启动TaskExecutor
 * </pre>
 * 
 * 调度器关闭流程:<br>
 * 
 * <pre>
 * 关闭Timer -> 关闭所有运行中的TaskLauncher -> 关闭所有运行中的TaskExecutor
 * </pre>
 * 
 * 其中：
 * 
 * <pre>
 * <strong>TaskLauncher</strong>：定时器每分钟调用一次，负责检查<strong>TaskTable</strong>是否有匹配到此时运行的Task
 * </pre>
 * 
 * <pre>
 * <strong>TaskExecutor</strong>：TaskLauncher匹配成功后，触发TaskExecutor执行具体的作业，执行完毕销毁
 * </pre>
 * 
 * @author Looly
 *
 */
public class Scheduler {
	private Object lock = new Object();

	/** 时区 */
	private TimeZone timezone;
	/** 是否已经启动 */
	private boolean started = false;
	/** 是否支持秒匹配 */
	protected boolean matchSecond = false;
	/** 是否支持年匹配 */
	protected boolean matchYear = false;
	/** 是否为守护线程 */
	protected boolean daemon;

	/** 定时器 */
	private CronTimer timer;
	/** 定时任务表 */
	protected TaskTable taskTable = new TaskTable(this);
	/** 启动器列表 */
	protected List<TaskLauncher> launchers;
	/** 执行器列表 */
	protected List<TaskExecutor> executors;
	/** 监听管理器列表 */
	protected TaskListenerManager listenerManager;

	// --------------------------------------------------------- Getters and Setters start
	/**
	 * 设置时区
	 * 
	 * @param timezone 时区
	 * @return this
	 */
	public Scheduler setTimeZone(TimeZone timezone) {
		this.timezone = timezone;
		return this;
	}

	/**
	 * 获得时区，默认为 {@link TimeZone#getDefault()}
	 * 
	 * @return 时区
	 */
	public TimeZone getTimeZone() {
		return timezone != null ? timezone : TimeZone.getDefault();
	}

	/**
	 * 是否为守护线程<br>
	 * 默认非守护线程
	 * 
	 * @param on <code>true</code>为守护线程，否则非守护线程
	 * @throws CronException
	 */
	public void setDaemon(boolean on) throws CronException {
		synchronized (lock) {
			if (started) {
				throw new CronException("Scheduler already started!");
			}
			this.daemon = on;
		}
	}
	
	/**
	 * 是否支持秒匹配
	 * @return <code>true</code>使用，<code>false</code>不使用
	 */
	public boolean isMatchSecond(){
		return this.matchSecond;
	}
	
	/**
	 * 设置是否支持秒匹配，默认不使用
	 * @param isMatchSecond <code>true</code>支持，<code>false</code>不支持
	 * @return this
	 */
	public Scheduler setMatchSecond(boolean isMatchSecond) {
		this.matchSecond = isMatchSecond;
		return this;
	}
	
	/**
	 * 是否支持年匹配
	 * @return <code>true</code>使用，<code>false</code>不使用
	 */
	public boolean isMatchYear(){
		return this.matchYear;
	}
	
	/**
	 * 设置是否支持年匹配，默认不使用
	 * @param isMatchYear <code>true</code>支持，<code>false</code>不支持
	 * @return this
	 */
	public Scheduler setMatchYear(boolean isMatchYear) {
		this.matchYear = isMatchYear;
		return this;
	}
	
	/**
	 * 增加监听器
	 * @param listener {@link TaskListener}
	 * @return this
	 */
	public Scheduler addListener(TaskListener listener){
		this.listenerManager.addListener(listener);
		return this;
	}
	
	/**
	 * 移除监听器
	 * @param listener {@link TaskListener}
	 * @return this
	 */
	public Scheduler removeListener(TaskListener listener){
		this.listenerManager.removeListener(listener);
		return this;
	}
	// --------------------------------------------------------- Getters and Setters end

	// -------------------------------------------------------------------- shcedule start
	/**
	 * 新增Task，使用随机UUID
	 * 
	 * @param pattern {@link CronPattern}对应的String表达式
	 * @param task {@link Runnable}
	 * @return ID
	 */
	public String schedule(String pattern, Runnable task) {
		return schedule(pattern, new RunnableTask(task));
	}

	/**
	 * 新增Task，使用随机UUID
	 * 
	 * @param pattern {@link CronPattern}对应的String表达式
	 * @param task {@link Task}
	 * @return ID
	 */
	public String schedule(String pattern, Task task) {
		String id = UUID.randomUUID().toString();
		schedule(id, pattern, task);
		return id;
	}

	/**
	 * 新增Task
	 * 
	 * @param id ID，为每一个Task定义一个ID
	 * @param pattern {@link CronPattern}对应的String表达式
	 * @param task {@link Runnable}
	 * @return this
	 */
	public Scheduler schedule(String id, String pattern, Runnable task) {
		return schedule(id, new CronPattern(pattern), new RunnableTask(task));
	}

	/**
	 * 新增Task
	 * 
	 * @param id ID，为每一个Task定义一个ID
	 * @param pattern {@link CronPattern}对应的String表达式
	 * @param task {@link Task}
	 * @return this
	 */
	public Scheduler schedule(String id, String pattern, Task task) {
		return schedule(id, new CronPattern(pattern), task);
	}

	/**
	 * 新增Task
	 * 
	 * @param id ID，为每一个Task定义一个ID
	 * @param pattern {@link CronPattern}
	 * @param task {@link Task}
	 * @return this
	 */
	public Scheduler schedule(String id, CronPattern pattern, Task task) {
		taskTable.add(id, pattern, task);
		return this;
	}

	/**
	 * 移除Task
	 * 
	 * @param id Task的ID
	 */
	public synchronized void deschedule(String id) throws IndexOutOfBoundsException {
		this.taskTable.remove(id);
	}
	// -------------------------------------------------------------------- shcedule end

	/**
	 * @return 是否已经启动
	 */
	public boolean isStarted() {
		return this.started;
	}

	/**
	 * 启动
	 * 
	 * @return this
	 */
	public Scheduler start() {
		synchronized (lock) {
			if (this.started) {
				throw new CronException("Schedule is started!");
			}

			this.launchers = new ArrayList<>();
			this.executors = new ArrayList<>();
			
			// Start CronTimer
			timer = new CronTimer(this, this.matchSecond);
			timer.setDaemon(this.daemon);
			timer.start();
			this.started = true;
		}
		return this;
	}

	/**
	 * 停止定时任务
	 * 
	 * @return this
	 */
	public Scheduler stop() {
		synchronized (lock) {
			if (!started) {
				throw new IllegalStateException("Scheduler not started");
			}

			// 停止CronTimer
			ThreadUtil.interupt(this.timer, true);
			// 停止所有TaskLauncher
			synchronized (this.launchers) {
				for (TaskLauncher taskLauncher : launchers) {
					ThreadUtil.interupt(taskLauncher, true);
				}
			}
			this.launchers =null;
			
			// 停止所有TaskExecutor
			synchronized (this.executors) {
				for (TaskExecutor taskExecutor : executors) {
					ThreadUtil.interupt(taskExecutor, true);
				}
			}
			this.executors = null;

			//修改标志
			started = false;
		}
		return this;
	}
	
	/**
	 * 启动 TaskLauncher
	 * @param millis 触发事件的毫秒数
	 * @return {@link TaskLauncher}
	 */
	protected TaskLauncher spawnLauncher(long millis) {
		final TaskLauncher launcher = new TaskLauncher(this, millis);
		synchronized (this.launchers) {
			this.launchers.add(launcher);
		}
		launcher.setDaemon(this.daemon);
		launcher.start();
		return launcher;
	}
	
	/**
	 * 启动 TaskExecutor
	 * @param task {@link Task}
	 * @return {@link TaskExecutor}
	 */
	protected TaskExecutor spawnExecutor(Task task) {
		final TaskExecutor executor = new TaskExecutor(this, task);
		synchronized (this.executors) {
			this.executors.add(executor);
		}
		executor.setDaemon(this.daemon);
		executor.start();
		return executor;
	}
	
	// -------------------------------------------------------------------- notify start
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
	 * 执行器执行完毕调用此方法，将执行器从执行器列表移除
	 * @param executor 执行器 {@link TaskExecutor}
	 */
	protected void notifyExecutorCompleted(TaskExecutor executor) {
		synchronized (executors) {
			executors.remove(executor);
		}
	}
	// -------------------------------------------------------------------- notify end
}
