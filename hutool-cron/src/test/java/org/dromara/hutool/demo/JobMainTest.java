package org.dromara.hutool.demo;

import org.dromara.hutool.CronUtil;

/**
 * 定时任务样例
 */
public class JobMainTest {

	public static void main(final String[] args) {
		CronUtil.setMatchSecond(true);
		CronUtil.start(false);
	}
}
