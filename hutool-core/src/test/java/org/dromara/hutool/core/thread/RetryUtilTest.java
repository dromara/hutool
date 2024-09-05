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
