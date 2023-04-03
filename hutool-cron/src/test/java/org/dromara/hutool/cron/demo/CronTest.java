package org.dromara.hutool.cron.demo;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.cron.CronUtil;
import org.dromara.hutool.cron.TaskExecutor;
import org.dromara.hutool.cron.listener.TaskListener;
import org.dromara.hutool.cron.task.Task;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 定时任务样例
 */
public class CronTest {

	@Test
	@Disabled
	public void customCronTest() {
		CronUtil.schedule("*/2 * * * * *", (Task) () -> Console.log("Task excuted."));

		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.start();

		ThreadUtil.waitForDie();
		Console.log("Exit.");
	}

	@Test
	@Disabled
	public void cronTest() {
		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.getScheduler().setDaemon(false);
		CronUtil.start();

		ThreadUtil.waitForDie();
		CronUtil.stop();
	}

	@Test
	@Disabled
	public void cronWithListenerTest() {
		CronUtil.getScheduler().addListener(new TaskListener() {
			@Override
			public void onStart(final TaskExecutor executor) {
				Console.log("Found task:[{}] start!", executor.getCronTask().getId());
			}

			@Override
			public void onSucceeded(final TaskExecutor executor) {
				Console.log("Found task:[{}] success!", executor.getCronTask().getId());
			}

			@Override
			public void onFailed(final TaskExecutor executor, final Throwable exception) {
				Console.error("Found task:[{}] failed!", executor.getCronTask().getId());
			}
		});

		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.start();

		ThreadUtil.waitForDie();
		Console.log("Exit.");
	}

	@Test
	@Disabled
	public void addAndRemoveTest() {
		final String id = CronUtil.schedule("*/2 * * * * *", (Runnable) () -> Console.log("task running : 2s"));

		Console.log(id);
		CronUtil.remove(id);

		// 支持秒级别定时任务
		CronUtil.setMatchSecond(true);
		CronUtil.start();
	}
}
