package org.dromara.hutool.cron.demo;

import org.dromara.hutool.cron.CronUtil;

/**
 * 定时任务样例
 */
public class JobMainTest {

	public static void main(final String[] args) {
		CronUtil.setMatchSecond(true);
		CronUtil.start(false);
	}
}
