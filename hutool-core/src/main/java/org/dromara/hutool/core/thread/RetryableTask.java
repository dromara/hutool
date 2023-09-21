/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.lang.Assert;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

/**
 * 重试任务类
 *
 * @param <T> 任务结果类型
 * @author kongweiguang
 * @since 6.0.0
 */
public class RetryableTask<T> {

	// region ----- retryFor

	/**
	 * 重试根据指定的异常，没有返回值
	 *
	 * @param <T> 返回值类型
	 * @param run 执行的方法 {@link Runnable}
	 * @param ths 指定异常 {@link Throwable}，匹配任意一个异常时重试
	 * @return 当前对象
	 */
	@SafeVarargs
	public static <T> RetryableTask<T> retryForExceptions(final Runnable run, final Class<? extends Throwable>... ths) {
		return retryForExceptions(() -> {
			run.run();
			return null;
		}, ths);
	}

	/**
	 * 重试根据指定的异常，有返回值
	 *
	 * @param <T> 返回值类型
	 * @param sup 执行的方法 {@link Supplier}
	 * @param ths 指定异常 {@link Throwable}，匹配任意一个异常时重试
	 * @return 当前对象
	 */
	@SafeVarargs
	public static <T> RetryableTask<T> retryForExceptions(final Supplier<T> sup, final Class<? extends Throwable>... ths) {
		Assert.isTrue(ths.length != 0, "exs cannot be empty");

		final BiPredicate<T, Throwable> strategy = (t, e) -> {
			if (nonNull(e)) {
				return Arrays.stream(ths).anyMatch(ex -> ex.isAssignableFrom(e.getClass()));
			}
			return false;
		};

		return new RetryableTask<>(sup, strategy);
	}

	/**
	 * 重试根据指定的策略，没有返回值
	 *
	 * @param <T>       返回值类型
	 * @param run       执行的方法 {@link Runnable}
	 * @param predicate 策略 {@link BiPredicate}，返回{@code true}时表示重试
	 * @return 当前对象
	 */
	public static <T> RetryableTask<T> retryForPredicate(final Runnable run, final BiPredicate<T, Throwable> predicate) {
		return retryForPredicate(() -> {
			run.run();
			return null;
		}, predicate);
	}

	/**
	 * 重试根据指定的策略，没有返回值
	 *
	 * @param <T>       返回值类型
	 * @param sup       执行的方法 {@link  Supplier}
	 * @param predicate 策略 {@link BiPredicate}，返回{@code true}时表示重试
	 * @return 当前对象
	 */
	public static <T> RetryableTask<T> retryForPredicate(final Supplier<T> sup, final BiPredicate<T, Throwable> predicate) {
		return new RetryableTask<>(sup, predicate);
	}
	// endregion

	/**
	 * 执行结果
	 */
	private T result;
	/**
	 * 执行法方法
	 */
	private final Supplier<T> sup;
	/**
	 * 重试策略
	 */
	private final BiPredicate<T, Throwable> predicate;
	/**
	 * 重试次数，默认3次
	 */
	private long maxAttempts = 3;
	/**
	 * 重试间隔，默认1秒
	 */
	private Duration delay = Duration.ofSeconds(1);

	/**
	 * 构造方法，内部使用，调用请使用请用ofXXX
	 *
	 * @param sup       执行的方法
	 * @param predicate 策略 {@link BiPredicate}，返回{@code true}时表示重试
	 */
	private RetryableTask(final Supplier<T> sup, final BiPredicate<T, Throwable> predicate) {
		Assert.notNull(sup, "task parameter cannot be null");
		Assert.notNull(predicate, "predicate parameter cannot be null");

		this.predicate = predicate;
		this.sup = sup;
	}

	/**
	 * 最大重试次数
	 *
	 * @param maxAttempts 次数
	 * @return 当前对象
	 */
	public RetryableTask<T> maxAttempts(final long maxAttempts) {
		Assert.isTrue(this.maxAttempts > 0, "maxAttempts must be greater than 0");

		this.maxAttempts = maxAttempts;
		return this;
	}

	/**
	 * 重试间隔时间
	 *
	 * @param delay 间隔时间
	 * @return 当前对象
	 */
	public RetryableTask<T> delay(final Duration delay) {
		Assert.notNull(this.delay, "delay parameter cannot be null");

		this.delay = delay;
		return this;
	}

	/**
	 * 获取结果
	 *
	 * @return 返回包装了结果的 {@link Optional}对象
	 */
	public Optional<T> get() {
		return Optional.ofNullable(this.result);
	}

	/**
	 * 异步执行重试方法
	 *
	 * @return 返回一个异步对象 {@link CompletableFuture}
	 */
	public CompletableFuture<RetryableTask<T>> asyncExecute() {
		return CompletableFuture.supplyAsync(this::doExecute, GlobalThreadPool.getExecutor());
	}

	/**
	 * 同步执行重试方法
	 *
	 * @return 当前对象
	 */
	public RetryableTask<T> execute() {
		return doExecute();
	}

	/**
	 * 开始重试
	 *
	 * @return 当前对象
	 **/
	private RetryableTask<T> doExecute() {
		Throwable th = null;

		while (--this.maxAttempts >= 0) {
			try {
				this.result = this.sup.get();
			} catch (final Throwable t) {
				th = t;
			}

			//判断重试
			if (!this.predicate.test(this.result, th)) {
				// 条件不满足时，跳出
				break;
			}

			ThreadUtil.sleep(delay.toMillis());
		}

		return this;
	}

}

