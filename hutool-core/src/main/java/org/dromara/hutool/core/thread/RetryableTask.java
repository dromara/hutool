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
 * @author kongweiguang
 * @since 6.0.0
 */
public class RetryableTask<T> {
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
	 * @param predicate 策略 {@link BiPredicate}
	 */
	private RetryableTask(Supplier<T> sup, BiPredicate<T, Throwable> predicate) {
		Assert.notNull(sup, "task parameter cannot be null");
		Assert.notNull(predicate, "predicate parameter cannot be null");

		this.predicate = predicate;
		this.sup = sup;
	}


	/**
	 * 重试根据指定的异常，没有返回值
	 *
	 * @param <T> 返回值类型
	 * @param run 执行的方法 {@link Runnable}
	 * @param ths 指定异常 {@link Throwable}
	 * @return 当前对象 {@link RetryableTask}
	 */
	@SafeVarargs
	public static <T> RetryableTask<T> retryForExceptions(Runnable run, Class<? extends Throwable>... ths) {
		return retryForExceptions(() -> {
			run.run();
			return null;
		}, ths);
	}

	/**
	 * 重试根据指定的策略，没有返回值
	 *
	 * @param <T>       返回值类型
	 * @param run       执行的方法 {@link Runnable}
	 * @param predicate 策略 {@link BiPredicate}
	 * @return 当前对象 {@link RetryableTask}
	 */
	public static <T> RetryableTask<T> retryForPredicate(Runnable run, BiPredicate<T, Throwable> predicate) {
		return retryForPredicate(() -> {
			run.run();
			return null;
		}, predicate);
	}

	/**
	 * 重试根据指定的异常，有返回值
	 *
	 * @param <T> 返回值类型
	 * @param sup 执行的方法 {@link Supplier}
	 * @param ths 指定异常 {@link Throwable}
	 * @return 当前对象 {@link RetryableTask}
	 */
	@SafeVarargs
	public static <T> RetryableTask<T> retryForExceptions(Supplier<T> sup, Class<? extends Throwable>... ths) {
		Assert.isTrue(ths.length != 0, "exs cannot be empty");

		BiPredicate<T, Throwable> strategy = (t, e) -> {
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
	 * @param sup       执行的方法 {@link  Supplier}
	 * @param predicate 策略 {@link BiPredicate}
	 * @return 当前对象 {@link RetryableTask}
	 */
	public static <T> RetryableTask<T> retryForPredicate(Supplier<T> sup, BiPredicate<T, Throwable> predicate) {
		return new RetryableTask<>(sup, predicate);
	}


	/**
	 * 最大重试次数
	 *
	 * @param maxAttempts 次数
	 * @return 当前对象 {@link RetryableTask}
	 */
	public RetryableTask<T> maxAttempts(long maxAttempts) {
		Assert.isTrue(this.maxAttempts > 0, "maxAttempts must be greater than 0");

		this.maxAttempts = maxAttempts;
		return this;
	}

	/**
	 * 重试间隔时间
	 *
	 * @param delay 间隔时间
	 * @return 当前对象 {@link RetryableTask}
	 */
	public RetryableTask<T> delay(Duration delay) {
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
	 * @return 当前对象 {@link RetryableTask}
	 */
	public RetryableTask<T> execute() {
		return doExecute();
	}

	/**
	 * 开始重试
	 *
	 * @return 当前对象 {@link RetryableTask}
	 **/
	private RetryableTask<T> doExecute() {
		Throwable th = null;

		while (--this.maxAttempts >= 0) {

			try {
				this.result = this.sup.get();
			} catch (Throwable t) {
				th = t;
			} finally {
				//判断重试
				if (this.predicate.test(this.result, th)) {
					ThreadUtil.sleep(delay.toMillis());
				} else {
					break;
				}
			}
		}

		return this;
	}

}

