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

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class StopWatchTest {

	/**
	 * https://gitee.com/dromara/hutool/issues/I6HSBG
	 */
	@Test
	public void prettyPrintTest() {
		final StopWatch stopWatch = new StopWatch();
		stopWatch.start("任务1");
		ThreadUtil.sleep(1);
		stopWatch.stop();
		stopWatch.start("任务2");
		ThreadUtil.sleep(200);
		stopWatch.stop();

		Console.log(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
	}
}
