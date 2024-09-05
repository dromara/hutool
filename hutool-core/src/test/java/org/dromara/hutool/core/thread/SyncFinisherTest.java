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

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class SyncFinisherTest {

	/**
	 * https://gitee.com/dromara/hutool/issues/I716SX
	 */
	@SuppressWarnings("DataFlowIssue")
	@Test
	void executeExceptionTest() {
		final AtomicBoolean hasException = new AtomicBoolean(false);
		final SyncFinisher syncFinisher = new SyncFinisher(10);
		syncFinisher.addWorker(()->{
			Console.log(Integer.parseInt("XYZ"));//这里会抛RuntimeException
		});

		syncFinisher.setExceptionHandler((t, e) -> {
			hasException.set(true);
			Assertions.assertEquals("For input string: \"XYZ\"", e.getMessage());
		});

		syncFinisher.start();
		syncFinisher.close();
		ThreadUtil.sleep(300);
		Assertions.assertTrue(hasException.get());
	}

}
