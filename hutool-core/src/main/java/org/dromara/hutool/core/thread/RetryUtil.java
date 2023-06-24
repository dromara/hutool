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

import org.dromara.hutool.core.array.ArrayUtil;

import java.time.Duration;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * 重试工具类
 * 自定义功能请使用{@link RetryableTask}类
 *
 * @author kongweiguang
 * @see RetryableTask
 * @since 6.0.0
 */
public class RetryUtil {

	/**
	 * 根据异常信息进行重试
	 * 没有返回值，重试执行方法
	 *
	 * @param run         执行方法
	 * @param maxAttempts 最大的重试次数
	 * @param delay       重试间隔
	 * @param recover     达到最大重试次数后执行的备用方法，入参是重试过程中的异常
	 * @param exs         指定的异常类型需要重试
	 */
	public static void ofException(final Runnable run, final long maxAttempts, final Duration delay,
								   final Runnable recover, Class<? extends Throwable>... exs) {
		if (ArrayUtil.isEmpty(exs)) {
			exs = ArrayUtil.append(exs, RuntimeException.class);
		}
		RetryableTask.retryForExceptions(run, exs)
			.maxAttempts(maxAttempts)
			.delay(delay)
			.execute()
			.get()
			.orElseGet(() -> {
				recover.run();
				return null;
			});
	}

	/**
	 * 根据异常信息进行重试
	 * 有返回值，重试执行方法
	 *
	 * @param sup         执行方法
	 * @param maxAttempts 最大的重试次数
	 * @param delay       重试间隔
	 * @param recover     达到最大重试次数后执行的备用方法，入参是重试过程中的异常
	 * @param exs         指定的异常类型需要重试
	 * @param <T>         结果类型
	 * @return 执行结果
	 */
	public static <T> T ofException(final Supplier<T> sup, final long maxAttempts, final Duration delay,
									final Supplier<T> recover, Class<? extends Throwable>... exs) {
		if (ArrayUtil.isEmpty(exs)) {
			exs = ArrayUtil.append(exs, RuntimeException.class);
		}
		return RetryableTask.retryForExceptions(sup, exs)
			.maxAttempts(maxAttempts)
			.delay(delay)
			.execute()
			.get()
			.orElseGet(recover);
	}

	/**
	 * 根据自定义结果进行重试
	 * 没有返回值，重试执行方法
	 *
	 * @param run         执行方法
	 * @param maxAttempts 最大的重试次数
	 * @param delay       重试间隔
	 * @param recover     达到最大重试次数后执行的备用方法，入参是重试过程中的异常
	 * @param predicate   自定义重试条件
	 */
	public static void ofPredicate(final Runnable run, final long maxAttempts, final Duration delay,
								   final Supplier<Void> recover, final BiPredicate<Void, Throwable> predicate) {
		RetryableTask.retryForPredicate(run, predicate)
			.delay(delay)
			.maxAttempts(maxAttempts)
			.execute()
			.get()
			.orElseGet(recover);
	}


	/**
	 * 根据异常信息进行重试
	 * 有返回值，重试执行方法
	 *
	 * @param sup         执行方法
	 * @param maxAttempts 最大的重试次数
	 * @param delay       重试间隔
	 * @param recover     达到最大重试次数后执行的备用方法，入参是重试过程中的异常
	 * @param predicate   自定义重试条件
	 * @param <T>         结果类型
	 * @return 执行结果
	 */
	public static <T> T ofPredicate(final Supplier<T> sup, final long maxAttempts, final Duration delay,
									final Supplier<T> recover, final BiPredicate<T, Throwable> predicate) {
		return RetryableTask.retryForPredicate(sup, predicate)
			.delay(delay)
			.maxAttempts(maxAttempts)
			.execute()
			.get()
			.orElseGet(recover);
	}

}
