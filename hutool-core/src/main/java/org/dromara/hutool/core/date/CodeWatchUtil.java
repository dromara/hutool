package org.dromara.hutool.core.date;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.thread.ThreadUtil;

import java.util.concurrent.TimeUnit;

/**
 * 统计代码运行时间工具<br>
 * 此工具用于存储一组任务的耗时时间，并一次性打印对比，也可以打印每个任务运行时间<br>
 *
 * <p>
 * 使用方法如下：
 * <pre>{@code
 * CodeWatchUtil.init("任务id");
 *
 * // 任务1
 * CodeWatchUtil.start("任务一");
 * Thread.sleep(1000);
 *
 * // 任务2
 * CodeWatchUtil.start("任务二");
 * Thread.sleep(2000);
 * Console.log(CodeWatchUtil.printCurrentTask());
 *
 * // 打印出耗时
 * Console.log(CodeWatchUtil.prettyPrint());
 *
 * }</pre>
 *
 * @author liufuqiang
 */
public class CodeWatchUtil {

	private static ThreadLocal<StopWatch> threadLocal = null;

	/**
	 * 初始化计时任务
	 *
	 * @param id 用于标识秒表的唯一ID
	 */
	public static void init(String id) {
		threadLocal = ThreadUtil.createThreadLocal(() -> StopWatch.of(id));
	}

	/**
	 * 创建计时任务<br>
	 * 如果上一个任务未停止，可自动停止
	 *
	 * @param taskName 任务名称
	 */
	public static void start(String taskName) {
		if (threadLocal == null) {
			return;
		}

		StopWatch stopWatch = threadLocal.get();
		if (stopWatch == null) {
			return;
		}
		if (stopWatch.isRunning()) {
			stopWatch.stop();
		}
		stopWatch.start(taskName);
	}

	/**
	 * 停止当前任务
	 */
	public static void stop() {
		if (threadLocal == null) {
			return;
		}

		StopWatch stopWatch = threadLocal.get();
		if (stopWatch == null) {
			return;
		}
		if (stopWatch.isRunning()) {
			stopWatch.stop();
		}
	}

	/**
	 * 生成所有任务的一个任务花费时间表，单位纳秒
	 *
	 * @return 任务时间表
	 */
	public static String prettyPrint() {
		return prettyPrint(null);
	}

	/**
	 * 生成所有任务的一个任务花费时间表
	 *
	 * @param unit 时间单位，{@code null}则默认{@link TimeUnit#NANOSECONDS} 纳秒
	 * @return 任务时间表
	 */
	public static String prettyPrint(TimeUnit unit) {
		if (threadLocal == null) {
			return null;
		}

		StopWatch stopWatch = threadLocal.get();
		if (stopWatch == null) {
			return null;
		}

		if (stopWatch.isRunning()) {
			stopWatch.stop();
		}
		threadLocal.remove();
		return stopWatch.prettyPrint(unit);
	}

	/**
	 * 打印当前任务执行时间
	 *
	 * @return 当前任务运行时间
	 */
	public static String printCurrentTask() {
		return printCurrentTask(TimeUnit.NANOSECONDS);
	}

	/**
	 * 打印当前任务执行的时间
	 *
	 * @param unit 时间单位
	 * @return 当前任务运行时间
	 */
	public static String printCurrentTask(TimeUnit unit) {
		if (threadLocal == null) {
			return null;
		}

		StopWatch stopWatch = threadLocal.get();
		if (stopWatch == null) {
			return null;
		}
		StopWatch.TaskInfo taskInfo = stopWatch.getLastTaskInfo();
		return StrUtil.format("StopWatch '{}:' taskName:{} running time = {} {}", stopWatch.getId(),
				taskInfo.getTaskName(), taskInfo.getTime(unit), DateUtil.getShortName(unit));
	}
}
