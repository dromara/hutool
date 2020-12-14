package cn.hutool.cron.demo;

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;

public class AddAndRemoveMainTest {
	
	public static void main(String[] args) {
		CronUtil.setMatchSecond(true);
		CronUtil.start(false);
		CronUtil.getScheduler().clear();
		String id = CronUtil.schedule("*/2 * * * * *", (Runnable) () -> Console.log("task running : 2s"));
		ThreadUtil.sleep(3000);
		CronUtil.remove(id);
		Console.log("Task Removed");

		CronUtil.schedule("*/3 * * * * *", (Runnable) () -> Console.log("New task add running : 3s"));
		Console.log("New Task added.");
	}
}
