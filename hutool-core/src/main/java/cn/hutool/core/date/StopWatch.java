package cn.hutool.core.date;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 秒表封装<br>
 * 此工具用于存储一组任务的耗时时间，并一次性打印对比。<br>
 * 比如：我们可以记录多段代码耗时时间，然后一次性打印（StopWatch提供了一个prettyString()函数用于按照指定格式打印出耗时）
 *
 * <p>
 * 此工具来自：https://github.com/spring-projects/spring-framework/blob/master/spring-core/src/main/java/org/springframework/util/StopWatch.java
 *
 * <p>
 * 使用方法如下：
 *
 * <pre>
 * StopWatch stopWatch = new StopWatch("任务名称");
 *
 * // 任务1
 * stopWatch.start("任务一");
 * Thread.sleep(1000);
 * stopWatch.stop();
 *
 * // 任务2
 * stopWatch.start("任务一");
 * Thread.sleep(2000);
 * stopWatch.stop();
 *
 * // 打印出耗时
 * Console.log(stopWatch.prettyPrint());
 *
 * </pre>
 *
 * @author Spring Framework, Looly
 * @since 4.6.6
 */
public class StopWatch {

	/**
	 * 创建计时任务（秒表）
	 *
	 * @param id 用于标识秒表的唯一ID
	 * @return StopWatch
	 * @since 5.5.2
	 */
	public static StopWatch create(String id){
		return new StopWatch(id);
	}

	/**
	 * 秒表唯一标识，用于多个秒表对象的区分
	 */
	private final String id;
	private List<TaskInfo> taskList;

	/**
	 * 任务名称
	 */
	private String currentTaskName;
	/**
	 * 开始时间
	 */
	private long startTimeNanos;

	/**
	 * 最后一次任务对象
	 */
	private TaskInfo lastTaskInfo;
	/**
	 * 总任务数
	 */
	private int taskCount;
	/**
	 * 总运行时间
	 */
	private long totalTimeNanos;
	// ------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，不启动任何任务
	 */
	public StopWatch() {
		this(StrUtil.EMPTY);
	}

	/**
	 * 构造，不启动任何任务
	 *
	 * @param id 用于标识秒表的唯一ID
	 */
	public StopWatch(String id) {
		this(id, true);
	}

	/**
	 * 构造，不启动任何任务
	 *
	 * @param id           用于标识秒表的唯一ID
	 * @param keepTaskList 是否在停止后保留任务，{@code false} 表示停止运行后不保留任务
	 */
	public StopWatch(String id, boolean keepTaskList) {
		this.id = id;
		if (keepTaskList) {
			this.taskList = new ArrayList<>();
		}
	}
	// ------------------------------------------------------------------------------------------- Constructor end

	/**
	 * 获取StopWatch 的ID，用于多个秒表对象的区分
	 *
	 * @return the ID 空字符串为
	 * @see #StopWatch(String)
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * 设置是否在停止后保留任务，{@code false} 表示停止运行后不保留任务
	 *
	 * @param keepTaskList 是否在停止后保留任务
	 */
	public void setKeepTaskList(boolean keepTaskList) {
		if (keepTaskList) {
			if (null == this.taskList) {
				this.taskList = new ArrayList<>();
			}
		} else {
			this.taskList = null;
		}
	}

	/**
	 * 开始默认的新任务
	 *
	 * @throws IllegalStateException 前一个任务没有结束
	 */
	public void start() throws IllegalStateException {
		start(StrUtil.EMPTY);
	}

	/**
	 * 开始指定名称的新任务
	 *
	 * @param taskName 新开始的任务名称
	 * @throws IllegalStateException 前一个任务没有结束
	 */
	public void start(String taskName) throws IllegalStateException {
		if (null != this.currentTaskName) {
			throw new IllegalStateException("Can't start StopWatch: it's already running");
		}
		this.currentTaskName = taskName;
		this.startTimeNanos = System.nanoTime();
	}

	/**
	 * 停止当前任务
	 *
	 * @throws IllegalStateException 任务没有开始
	 */
	public void stop() throws IllegalStateException {
		if (null == this.currentTaskName) {
			throw new IllegalStateException("Can't stop StopWatch: it's not running");
		}

		final long lastTime = System.nanoTime() - this.startTimeNanos;
		this.totalTimeNanos += lastTime;
		this.lastTaskInfo = new TaskInfo(this.currentTaskName, lastTime);
		if (null != this.taskList) {
			this.taskList.add(this.lastTaskInfo);
		}
		++this.taskCount;
		this.currentTaskName = null;
	}

	/**
	 * 检查是否有正在运行的任务
	 *
	 * @return 是否有正在运行的任务
	 * @see #currentTaskName()
	 */
	public boolean isRunning() {
		return (this.currentTaskName != null);
	}

	/**
	 * 获取当前任务名，{@code null} 表示无任务
	 *
	 * @return 当前任务名，{@code null} 表示无任务
	 * @see #isRunning()
	 */
	public String currentTaskName() {
		return this.currentTaskName;
	}

	/**
	 * 获取最后任务的花费时间（纳秒）
	 *
	 * @return 任务的花费时间（纳秒）
	 * @throws IllegalStateException 无任务
	 */
	public long getLastTaskTimeNanos() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task interval");
		}
		return this.lastTaskInfo.getTimeNanos();
	}

	/**
	 * 获取最后任务的花费时间（毫秒）
	 *
	 * @return 任务的花费时间（毫秒）
	 * @throws IllegalStateException 无任务
	 */
	public long getLastTaskTimeMillis() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task interval");
		}
		return this.lastTaskInfo.getTimeMillis();
	}

	/**
	 * 获取最后的任务名
	 *
	 * @return 任务名
	 * @throws IllegalStateException 无任务
	 */
	public String getLastTaskName() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task name");
		}
		return this.lastTaskInfo.getTaskName();
	}

	/**
	 * 获取最后的任务对象
	 *
	 * @return {@link TaskInfo} 任务对象，包括任务名和花费时间
	 * @throws IllegalStateException 无任务
	 */
	public TaskInfo getLastTaskInfo() throws IllegalStateException {
		if (this.lastTaskInfo == null) {
			throw new IllegalStateException("No tasks run: can't get last task info");
		}
		return this.lastTaskInfo;
	}

	/**
	 * 获取所有任务的总花费时间（纳秒）
	 *
	 * @return 所有任务的总花费时间（纳秒）
	 * @see #getTotalTimeMillis()
	 * @see #getTotalTimeSeconds()
	 */
	public long getTotalTimeNanos() {
		return this.totalTimeNanos;
	}

	/**
	 * 获取所有任务的总花费时间（毫秒）
	 *
	 * @return 所有任务的总花费时间（毫秒）
	 * @see #getTotalTimeNanos()
	 * @see #getTotalTimeSeconds()
	 */
	public long getTotalTimeMillis() {
		return DateUtil.nanosToMillis(this.totalTimeNanos);
	}

	/**
	 * 获取所有任务的总花费时间（秒）
	 *
	 * @return 所有任务的总花费时间（秒）
	 * @see #getTotalTimeNanos()
	 * @see #getTotalTimeMillis()
	 */
	public double getTotalTimeSeconds() {
		return DateUtil.nanosToSeconds(this.totalTimeNanos);
	}

	/**
	 * 获取任务数
	 *
	 * @return 任务数
	 */
	public int getTaskCount() {
		return this.taskCount;
	}

	/**
	 * 获取任务列表
	 *
	 * @return 任务列表
	 */
	public TaskInfo[] getTaskInfo() {
		if (null == this.taskList) {
			throw new UnsupportedOperationException("Task info is not being kept!");
		}
		return this.taskList.toArray(new TaskInfo[0]);
	}

	/**
	 * 获取任务信息
	 *
	 * @return 任务信息
	 */
	public String shortSummary() {
		return StrUtil.format("StopWatch '{}': running time = {} ns", this.id, this.totalTimeNanos);
	}

	/**
	 * 生成所有任务的一个任务花费时间表
	 *
	 * @return 任务时间表
	 */
	public String prettyPrint() {
		StringBuilder sb = new StringBuilder(shortSummary());
		sb.append(FileUtil.getLineSeparator());
		if (null == this.taskList) {
			sb.append("No task info kept");
		} else {
			sb.append("---------------------------------------------").append(FileUtil.getLineSeparator());
			sb.append("ns         %     Task name").append(FileUtil.getLineSeparator());
			sb.append("---------------------------------------------").append(FileUtil.getLineSeparator());

			final NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumIntegerDigits(9);
			nf.setGroupingUsed(false);

			final NumberFormat pf = NumberFormat.getPercentInstance();
			pf.setMinimumIntegerDigits(3);
			pf.setGroupingUsed(false);
			for (TaskInfo task : getTaskInfo()) {
				sb.append(nf.format(task.getTimeNanos())).append("  ");
				sb.append(pf.format((double) task.getTimeNanos() / getTotalTimeNanos())).append("  ");
				sb.append(task.getTaskName()).append(FileUtil.getLineSeparator());
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(shortSummary());
		if (null != this.taskList) {
			for (TaskInfo task : this.taskList) {
				sb.append("; [").append(task.getTaskName()).append("] took ").append(task.getTimeNanos()).append(" ns");
				long percent = Math.round(100.0 * task.getTimeNanos() / getTotalTimeNanos());
				sb.append(" = ").append(percent).append("%");
			}
		} else {
			sb.append("; no task info kept");
		}
		return sb.toString();
	}

	/**
	 * 存放任务名称和花费时间对象
	 *
	 * @author Looly
	 */
	public static final class TaskInfo {

		private final String taskName;
		private final long timeNanos;

		TaskInfo(String taskName, long timeNanos) {
			this.taskName = taskName;
			this.timeNanos = timeNanos;
		}

		/**
		 * 获取任务名
		 *
		 * @return 任务名
		 */
		public String getTaskName() {
			return this.taskName;
		}

		/**
		 * 获取任务花费时间（单位：纳秒）
		 *
		 * @return 任务花费时间（单位：纳秒）
		 * @see #getTimeMillis()
		 * @see #getTimeSeconds()
		 */
		public long getTimeNanos() {
			return this.timeNanos;
		}

		/**
		 * 获取任务花费时间（单位：毫秒）
		 *
		 * @return 任务花费时间（单位：毫秒）
		 * @see #getTimeNanos()
		 * @see #getTimeSeconds()
		 */
		public long getTimeMillis() {
			return DateUtil.nanosToMillis(this.timeNanos);
		}

		/**
		 * 获取任务花费时间（单位：秒）
		 *
		 * @return 任务花费时间（单位：秒）
		 * @see #getTimeMillis()
		 * @see #getTimeNanos()
		 */
		public double getTimeSeconds() {
			return DateUtil.nanosToSeconds(this.timeNanos);
		}
	}
}
