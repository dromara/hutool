/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
