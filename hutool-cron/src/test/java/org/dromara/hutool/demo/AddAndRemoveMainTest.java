package org.dromara.hutool.demo;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.thread.ThreadUtil;
import org.dromara.hutool.CronUtil;

public class AddAndRemoveMainTest {

	public static void main(final String[] args) {
		CronUtil.setMatchSecond(true);
		CronUtil.start(false);
		CronUtil.getScheduler().clear();
		final String id = CronUtil.schedule("*/2 * * * * *", (Runnable) () -> Console.log("task running : 2s"));
		ThreadUtil.sleep(3000);
		CronUtil.remove(id);
		Console.log("Task Removed");

		CronUtil.schedule("*/3 * * * * *", (Runnable) () -> Console.log("New task add running : 3s"));
		Console.log("New Task added.");
	}
}
