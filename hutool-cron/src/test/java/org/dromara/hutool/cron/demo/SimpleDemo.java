package org.dromara.hutool.cron.demo;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.cron.CronUtil;

public class SimpleDemo {
	public static void main(String[] args) {
		// 打开秒匹配
		CronUtil.setMatchSecond(true);
		// 添加任务
		CronUtil.schedule("*/2 * * * * *",
			() -> Console.log("Hutool task running!"));
		// 启动
		CronUtil.start();
	}
}
