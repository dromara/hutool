package org.dromara.hutool.thread;

import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.util.RuntimeUtil;

/**
 * 简单定时任务测试
 */
public class SimpleSchedulerTest {
	public static void main(final String[] args) {

		// 新建一个定时任务，定时获取内存信息
		final SimpleScheduler<Long> scheduler = new SimpleScheduler<>(new SimpleScheduler.Job<Long>() {
			private volatile long maxAvailable;

			@Override
			public Long getResult() {
				return this.maxAvailable;
			}

			@Override
			public void run() {
				this.maxAvailable = RuntimeUtil.getFreeMemory();
			}
		}, 50);

		// 另一个线程不停获取内存结果计算值
		ThreadUtil.execAsync(() -> {
			//noinspection InfiniteLoopStatement
			while (true) {
				Console.log(FileUtil.readableFileSize(scheduler.getResult()));
				ThreadUtil.sleep(1000);
			}
		});
	}
}
