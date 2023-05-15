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

package cn.hutool.core.thread;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class SyncFinisherTest {
	/**
	 * https://gitee.com/dromara/hutool/issues/I716SX
	 * 设置ExceptionHandler捕获异常
	 */
	@Test
	public void executeExceptionTest() {
		final AtomicBoolean hasException = new AtomicBoolean(false);
		final SyncFinisher syncFinisher = new SyncFinisher(10);
		syncFinisher.addWorker(()->{
			Console.log(Integer.parseInt("XYZ"));//这里会抛RuntimeException
		});

		syncFinisher.setExceptionHandler((t, e) -> {
			hasException.set(true);
			Assert.assertEquals("For input string: \"XYZ\"", e.getMessage());
		});

		syncFinisher.start();
		IoUtil.close(syncFinisher);
		ThreadUtil.sleep(300);
		Assert.assertTrue(hasException.get());
	}

	/**
	 * https://gitee.com/dromara/hutool/issues/I716SX
	 * 默认情况下吞掉异常
	 */
	@Test
	public void executeExceptionTest2() {
		final SyncFinisher syncFinisher = new SyncFinisher(10);
		syncFinisher.addWorker(()->{
			Console.log(Integer.parseInt("XYZ"));//这里会忽略RuntimeException
		});

		syncFinisher.start();
		IoUtil.close(syncFinisher);
	}
}
