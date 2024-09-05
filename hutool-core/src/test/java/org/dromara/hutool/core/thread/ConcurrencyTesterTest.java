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

package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ConcurrencyTesterTest {

	@Test
	@Disabled
	public void concurrencyTesterTest() {
		final ConcurrencyTester tester = ThreadUtil.concurrencyTest(100, () -> {
			final long delay = RandomUtil.randomLong(100, 1000);
			ThreadUtil.sleep(delay);
			Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
		});
		Console.log(tester.getInterval());
	}

	@Test
	@Disabled
	public void multiTest(){
		final ConcurrencyTester ct = new ConcurrencyTester(5);
		for(int i=0;i<3;i++){
			Console.log("开始执行第{}个",i);
			ct.test(() -> {
				// 需要并发测试的业务代码
				Console.log("当前执行线程：" + Thread.currentThread().getName()+" 产生时间 "+ DateUtil.formatNow());
				ThreadUtil.sleep(RandomUtil.randomInt(1000, 3000));
			});
		}
		Console.log("全部线程执行完毕 "+DateUtil.formatNow());
	}
}
