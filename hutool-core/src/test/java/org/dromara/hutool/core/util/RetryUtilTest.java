package org.dromara.hutool.core.util;

import org.dromara.hutool.core.thread.RetryableTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class RetryUtilTest {

	@Test
	void test() {
		//自定义根据异常重试
		CompletableFuture<RetryableTask<String>> task = RetryableTask.retryForExceptions(() -> {
				System.out.println("1231231");
				int a = 1 / 0;
				return "qqqq";
			}, ArithmeticException.class)
			.delay(Duration.ofSeconds(1))
			.maxAttempts(3)
			.asyncExecute();

		Assertions.assertFalse(task.isDone());
		Assertions.assertEquals("兜底", task.join().get().orElseGet(() -> {
			return "兜底";
		}));
		Assertions.assertTrue(task.isDone());

	}

	@Test
	public void noReturnTest() {
		//根据异常重试，没有返回值
		RetryUtil.ofException(
			() -> {
				System.out.println(123);
				int a = 1 / 0;
			},
			3,
			Duration.ofSeconds(1),
			() -> {
				System.out.println("兜底");
			},
			ArithmeticException.class
		);
	}


	@Test
	public void hasReturnTest() {
		//根据自定义策略重试
		String result = RetryUtil.ofPredicate(
			() -> {
				System.out.println(123);
//				int a = 1 / 0;
				return "ok";
			},
			5,
			Duration.ofSeconds(1),
			() -> {
				System.out.println("兜底");
				return "do";
			},
			(r, e) -> {
				System.out.println("r = " + r);
				System.out.println("e = " + e);
				return r.equals("ok");
			}
		);

		Assertions.assertEquals("ok", result);
	}


	@Test
	public void neverStop() {
		//异步一直执行
		RetryUtil.ofNeverStopAsync(() -> {
			System.out.println("async -->");
		}, Duration.ofSeconds(1), true);

		System.out.println(" ================ ");
		//同步一直执行
		RetryUtil.ofNeverStop(() -> {
			System.out.println(123);
		}, Duration.ofSeconds(3), true);


	}
}
