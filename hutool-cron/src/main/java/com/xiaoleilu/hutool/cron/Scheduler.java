package com.xiaoleilu.hutool.cron;

import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.UUID;

import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.cron.listener.TaskListener;
import com.xiaoleilu.hutool.cron.listener.TaskListenerManager;
import com.xiaoleilu.hutool.cron.pattern.CronPattern;
import com.xiaoleilu.hutool.cron.task.InvokeTask;
import com.xiaoleilu.hutool.cron.task.RunnableTask;
import com.xiaoleilu.hutool.cron.task.Task;
import com.xiaoleilu.hutool.setting.Setting;
import com.xiaoleilu.hutool.thread.ThreadUtil;

/**
 * 任务调度器<br>
 * 
 * 调度器启动流程：<br>
 * 
 * <pre>
 * 启动Timer =》 启动TaskLauncher =》 启动TaskExecutor
 * </pre>
 * 
 * 调度器关闭流程:<br>
 * 
 * <pre>
 * 关闭Timer =》 关闭所有运行中的TaskLauncher =》 关闭所有运行中的TaskExecutor
 * </pre>
 * 
 * 其中：
 * 
 * <pre>
 * <strong>TaskLauncher</strong>：定时器每分钟调用一次（如果{@link Scheduler#isMatchSecond()}为<code>true</code>每秒调用一次），
 * 负责检查<strong>TaskTable</strong>是否有匹配到此时间运行的Task
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
	/** 启动器管理器 */
	protected TaskLauncherManager taskLauncherManager;
	/** 执行器管理器 */
	protected TaskExecutorManager taskExecutorManager;
	/** 监听管理器列表 */
	protected TaskListenerManager listenerManager = new TaskListenerManager();

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
	 * 设置是否为守护线程<br>
	 * 默认非守护线程
	 * 
	 * @param on <code>true</code>为守护线程，否则非守护线程
	 * @return this
	 * @throws CronException 定时任务已经启动抛出此异常
	 */
	public Scheduler setDaemon(boolean on) throws CronException {
		synchronized (lock) {
			if (started) {
				throw new CronException("Scheduler already started!");
			}
			this.daemon = on;
		}
		return this;
	}

	/**
	 * 是否为守护线程
	 * 
	 * @return 是否为守护线程
	 */
	public boolean isDeamon() {
		return this.daemon;
	}

	/**
	 * 是否支持秒匹配
	 * 
	 * @return <code>true</code>使用，<code>false</code>不使用
	 */
	public boolean isMatchSecond() {
		return this.matchSecond;
	}

	/**
	 * 设置是否支持秒匹配，默认不使用
	 * 
	 * @param isMatchSecond <code>true</code>支持，<code>false</code>不支持
	 * @return this
	 */
	public Scheduler setMatchSecond(boolean isMatchSecond) {
		this.matchSecond = isMatchSecond;
		return this;
	}

	/**
	 * 是否支持年匹配
	 * 
	 * @return <code>true</code>使用，<code>false</code>不使用
	 */
	public boolean isMatchYear() {
		return this.matchYear;
	}

	/**
	 * 设置是否支持年匹配，默认不使用
	 * 
	 * @param isMatchYear <code>true</code>支持，<code>false</code>不支持
	 * @return this
	 */
	public Scheduler setMatchYear(boolean isMatchYear) {
		this.matchYear = isMatchYear;
		return this;
	}

	/**
	 * 增加监听器
	 * 
	 * @param listener {@link TaskListener}
	 * @return this
	 */
	public Scheduler addListener(TaskListener listener) {
		this.listenerManager.addListener(listener);
		return this;
	}

	/**
	 * 移除监听器
	 * 
	 * @param listener {@link TaskListener}
	 * @return this
	 */
	public Scheduler removeListener(TaskListener listener) {
		this.listenerManager.removeListener(listener);
		return this;
	}
	// --------------------------------------------------------- Getters and Setters end

	// -------------------------------------------------------------------- shcedule start
	/**
	 * 批量加入配置文件中的定时任务<br>
	 * 配置文件格式为： xxx.xxx.xxx.Class.method = * * * * *
	 * 
	 * @param cronSetting 定时任务设置文件
	 * @return this
	 */
	public Scheduler schedule(Setting cronSetting) {
		if (CollectionUtil.isNotEmpty(cronSetting)) {
			for (Entry<Object, Object> entry : cronSetting.entrySet()) {
				final String jobClass = Convert.toStr(entry.getKey());
				final String pattern = Convert.toStr(entry.getValue());
				try {
					schedule(pattern, new InvokeTask(jobClass));
				} catch (Exception e) {
					throw new CronException(e, "Schedule [{}] [{}] error!", pattern, jobClass);
				}
			}
		}
		return this;
	}

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
	 * @return this
	 */
	public Scheduler deschedule(String id) {
		this.taskTable.remove(id);
		return this;
	}

	/**
	 * 获得指定id的{@link CronPattern}
	 * 
	 * @param id ID
	 * @return {@link CronPattern}
	 * @since 3.1.1
	 */
	public CronPattern getPattern(String id) {
		return this.taskTable.getPattern(id);
	}

	/**
	 * 获得指定id的{@link Task}
	 * 
	 * @param id ID
	 * @return {@link Task}
	 * @since 3.1.1
	 */
	public Task getTask(String id) {
		return this.taskTable.getTask(id);
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

			this.taskLauncherManager = new TaskLauncherManager(this);
			this.taskExecutorManager = new TaskExecutorManager(this);

			// Start CronTimer
			timer = new CronTimer(this);
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
			taskLauncherManager.destroy();
			// 停止所有TaskExecutor
			this.taskExecutorManager.destroy();

			// 修改标志
			started = false;
		}
		return this;
	}

	// -------------------------------------------------------------------- notify start
	// -------------------------------------------------------------------- notify end
}
