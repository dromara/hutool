package cn.hutool.cron;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.listener.TaskListener;
import cn.hutool.cron.listener.TaskListenerManager;
import cn.hutool.cron.pattern.CronPattern;
import cn.hutool.cron.task.InvokeTask;
import cn.hutool.cron.task.RunnableTask;
import cn.hutool.cron.task.Task;
import cn.hutool.log.StaticLog;
import cn.hutool.setting.Setting;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
public class Scheduler implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Lock lock = new ReentrantLock();

	/** 定时任务配置 */
	protected CronConfig config = new CronConfig();
	/** 是否已经启动 */
	private boolean started = false;
	/** 是否为守护线程 */
	protected boolean daemon;

	/** 定时器 */
	private CronTimer timer;
	/** 定时任务表 */
	protected TaskTable taskTable = new TaskTable();
	/** 启动器管理器 */
	protected TaskLauncherManager taskLauncherManager;
	/** 执行器管理器 */
	protected TaskExecutorManager taskExecutorManager;
	/** 监听管理器列表 */
	protected TaskListenerManager listenerManager = new TaskListenerManager();
	/** 线程池，用于执行TaskLauncher和TaskExecutor */
	protected ExecutorService threadExecutor;

	// --------------------------------------------------------- Getters and Setters start
	/**
	 * 设置时区
	 * 
	 * @param timeZone 时区
	 * @return this
	 */
	public Scheduler setTimeZone(TimeZone timeZone) {
		this.config.setTimeZone(timeZone);
		return this;
	}

	/**
	 * 获得时区，默认为 {@link TimeZone#getDefault()}
	 * 
	 * @return 时区
	 */
	public TimeZone getTimeZone() {
		return this.config.getTimeZone();
	}

	/**
	 * 设置是否为守护线程<br>
	 * 如果为true，则在调用{@link #stop()}方法后执行的定时任务立即结束，否则等待执行完毕才结束。默认非守护线程
	 * 
	 * @param on <code>true</code>为守护线程，否则非守护线程
	 * @return this
	 * @throws CronException 定时任务已经启动抛出此异常
	 */
	public Scheduler setDaemon(boolean on) throws CronException {
		lock.lock();
		try {
			if (this.started) {
				throw new CronException("Scheduler already started!");
			}
			this.daemon = on;
		} finally {
			lock.unlock();
		}
		return this;
	}

	/**
	 * 是否为守护线程
	 * 
	 * @return 是否为守护线程
	 */
	public boolean isDaemon() {
		return this.daemon;
	}

	/**
	 * 是否支持秒匹配
	 * 
	 * @return <code>true</code>使用，<code>false</code>不使用
	 */
	public boolean isMatchSecond() {
		return this.config.isMatchSecond();
	}

	/**
	 * 设置是否支持秒匹配，默认不使用
	 * 
	 * @param isMatchSecond <code>true</code>支持，<code>false</code>不支持
	 * @return this
	 */
	public Scheduler setMatchSecond(boolean isMatchSecond) {
		this.config.setMatchSecond(isMatchSecond);
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
		if (CollUtil.isNotEmpty(cronSetting)) {
			String group;
			for (Entry<String, LinkedHashMap<String, String>> groupedEntry : cronSetting.getGroupedMap().entrySet()) {
				group = groupedEntry.getKey();
				for (Entry<String, String> entry : groupedEntry.getValue().entrySet()) {
					String jobClass = entry.getKey();
					if (StrUtil.isNotBlank(group)) {
						jobClass = group + CharUtil.DOT + jobClass;
					}
					final String pattern = entry.getValue();
					StaticLog.debug("Load job: {} {}", pattern, jobClass);
					try {
						schedule(pattern, new InvokeTask(jobClass));
					} catch (Exception e) {
						throw new CronException(e, "Schedule [{}] [{}] error!", pattern, jobClass);
					}
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
		String id = IdUtil.fastUUID();
		schedule(id, pattern, task);
		return id;
	}

	/**
	 * 新增Task，如果任务ID已经存在，抛出异常
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
	 * 新增Task，如果任务ID已经存在，抛出异常
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
	 * 新增Task，如果任务ID已经存在，抛出异常
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
	 * 更新Task执行的时间规则
	 * 
	 * @param id Task的ID
	 * @param pattern {@link CronPattern}
	 * @return this
	 * @since 4.0.10
	 */
	public Scheduler updatePattern(String id, CronPattern pattern) {
		this.taskTable.updatePattern(id, pattern);
		return this;
	}

	/**
	 * 获取定时任务表，注意此方法返回非复制对象，对返回对象的修改将影响已有定时任务
	 *
	 * @return 定时任务表{@link TaskTable}
	 * @since 4.6.7
	 */
	public TaskTable getTaskTable() {
		return this.taskTable;
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

	/**
	 * 是否无任务
	 * 
	 * @return true表示无任务
	 * @since 4.0.2
	 */
	public boolean isEmpty() {
		return this.taskTable.isEmpty();
	}

	/**
	 * 当前任务数
	 * 
	 * @return 当前任务数
	 * @since 4.0.2
	 */
	public int size() {
		return this.taskTable.size();
	}
	
	/**
	 * 清空任务表
	 * @return this
	 * @since 4.1.17
	 */
	public Scheduler clear() {
		this.taskTable = new TaskTable();
		return this;
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
	 * @param isDaemon 是否以守护线程方式启动，如果为true，则在调用{@link #stop()}方法后执行的定时任务立即结束，否则等待执行完毕才结束。
	 * @return this
	 */
	public Scheduler start(boolean isDaemon) {
		this.daemon = isDaemon;
		return start();
	}

	/**
	 * 启动
	 * 
	 * @return this
	 */
	public Scheduler start() {
		lock.lock();
		try {
			if (this.started) {
				throw new CronException("Schedule is started!");
			}

			// 无界线程池，确保每一个需要执行的线程都可以及时运行，同时复用已有现成避免线程重复创建
			this.threadExecutor = ExecutorBuilder.create().useSynchronousQueue().setThreadFactory(//
					ThreadFactoryBuilder.create().setNamePrefix("hutool-cron-").setDaemon(this.daemon).build()//
			).build();
			this.taskLauncherManager = new TaskLauncherManager(this);
			this.taskExecutorManager = new TaskExecutorManager(this);

			// Start CronTimer
			timer = new CronTimer(this);
			timer.setDaemon(this.daemon);
			timer.start();
			this.started = true;
		} finally {
			lock.unlock();
		}
		return this;
	}
	
	/**
	 * 停止定时任务<br>
	 * 此方法调用后会将定时器进程立即结束，如果为守护线程模式，则正在执行的作业也会自动结束，否则作业线程将在执行完成后结束。<br>
	 * 此方法并不会清除任务表中的任务，请调用{@link #clear()} 方法清空任务或者使用{@link #stop(boolean)}方法可选是否清空
	 * 
	 * @return this
	 */
	public Scheduler stop() {
		return stop(false);
	}

	/**
	 * 停止定时任务<br>
	 * 此方法调用后会将定时器进程立即结束，如果为守护线程模式，则正在执行的作业也会自动结束，否则作业线程将在执行完成后结束。
	 *
	 * @param clearTasks 是否清除所有任务
	 * @return this
	 * @since 4.1.17
	 */
	public Scheduler stop(boolean clearTasks) {
		lock.lock();
		try {
			if (false == started) {
				throw new IllegalStateException("Scheduler not started !");
			}

			// 停止CronTimer
			this.timer.stopTimer();
			this.timer = null;
			
			//停止线程池
			this.threadExecutor.shutdown();
			this.threadExecutor = null;
			
			//可选是否清空任务表
			if(clearTasks) {
				clear();
			}

			// 修改标志
			started = false;
		} finally {
			lock.unlock();
		}
		return this;
	}

}
