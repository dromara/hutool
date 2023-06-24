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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"NumericOverflow", "divzero"})
public class RetryUtilTest {

	@Test
	@Disabled
	void test() {
		//自定义根据异常重试
		final CompletableFuture<RetryableTask<String>> task = RetryableTask.retryForExceptions(() -> {
				Console.log("1231231");
				final int a = 1 / 0;
				return String.valueOf(a);
			}, ArithmeticException.class)
			.delay(Duration.ofSeconds(1))
			.maxAttempts(3)
			.asyncExecute();

		Assertions.assertFalse(task.isDone());
		Assertions.assertEquals("兜底", task.join().get().orElseGet(() -> "兜底"));
		Assertions.assertTrue(task.isDone());

	}

	@SuppressWarnings("unchecked")
	@Test
	@Disabled
	public void noReturnTest() {
		//根据异常重试，没有返回值
		RetryUtil.ofException(
			() -> {
				Console.log(123);
				final int a = 1 / 0;
				Console.log(a);
			},
			3,
			Duration.ofSeconds(1),
			() -> {
				Console.log("兜底");
			},
			ArithmeticException.class
		);
	}


	@Test
	@Disabled
	public void hasReturnTest() {
		//根据自定义策略重试
		final String result = RetryUtil.ofPredicate(
			() -> {
				Console.log(123);
//				int a = 1 / 0;
				return "ok";
			},
			5,
			Duration.ofSeconds(1),
			() -> {
				Console.log("兜底");
				return "do";
			},
			(r, e) -> {
				Console.log("r = " + r);
				Console.log("e = " + e);
				return r.equals("ok");
			}
		);

		Assertions.assertEquals("ok", result);
	}

}
