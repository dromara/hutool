package cn.hutool.cron.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;

/**
 * 测试定时任务，当触发到定时的时间点时，执行doTest方法
 * 
 * @author looly
 *
 */
public class TestJob {
	
	private final String jobId = IdUtil.simpleUUID();

	/**
	 * 执行定时任务内容
	 */
	public void doTest() {
//		String name = Thread.currentThread().getName();
		Console.log("Test Job {} running... at {}", jobId, DateUtil.now());
	}

	/**
	 * 执行循环定时任务，测试在定时任务结束时作为deamon线程是否能正常结束
	 */
	@SuppressWarnings("InfiniteLoopStatement")
	public void doWhileTest() {
		String name = Thread.currentThread().getName();
		while (true) {
			Console.log("Job {} while running...", name);
			ThreadUtil.sleep(2000);
		}
	}
}
