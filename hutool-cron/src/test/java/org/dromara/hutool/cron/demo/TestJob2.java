/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.cron.demo;

import java.util.concurrent.TimeUnit;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.thread.ThreadUtil;

/**
 * 测试定时任务，当触发到定时的时间点时，执行doTest方法
 *
 * @author looly
 *
 */
public class TestJob2 {

	/**
	 * 执行定时任务内容
	 */
	public void doTest() {
		Console.log("TestJob2.doTest开始执行…… at [{}]", DateUtil.formatNow());
		ThreadUtil.sleep(20, TimeUnit.SECONDS);
		Console.log("延迟20s打印testJob2");
	}
}
