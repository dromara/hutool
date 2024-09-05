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
