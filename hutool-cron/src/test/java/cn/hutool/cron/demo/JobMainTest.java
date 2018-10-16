package cn.hutool.cron.demo;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;

/**
 * 定时任务样例
 */
public class JobMainTest {

	public static void main(String[] args) {
		CronUtil.setMatchSecond(true);
		CronUtil.start(true);

		ThreadUtil.sleep(Integer.MAX_VALUE);
	}
}
