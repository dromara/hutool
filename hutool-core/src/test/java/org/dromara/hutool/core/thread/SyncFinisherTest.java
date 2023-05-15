/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SyncFinisherTest {

	/**
	 * https://gitee.com/dromara/hutool/issues/I716SX
	 */
	@Test
	void executeExceptionTest() {
		final SyncFinisher syncFinisher = new SyncFinisher(10);
		syncFinisher.addWorker(()->{
			Console.log(Integer.parseInt("XYZ"));//这里会抛RuntimeException
		});

		syncFinisher.setExceptionHandler((t, e) -> {
			Assertions.assertEquals("For input string: \"XYZ\"", e.getMessage());
		});

		syncFinisher.start();
		syncFinisher.close();
	}
}
