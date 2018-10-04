package cn.hutool.cron.demo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;

/**
 * 定时任务样例
 */
public class JobMainTest {

	public static void main(String[] args) {
		CronUtil.setMatchSecond(true);
		CronUtil.schedule("3/7 * * * * ?", new Runnable() {
			
			@Override
			public void run() {
				Console.log("### {}", DateUtil.date());
			}
		});
		CronUtil.start(true);

		ThreadUtil.sleep(Integer.MAX_VALUE);
	}
}
