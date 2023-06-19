package org.dromara.hutool.core.util;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.thread.GlobalThreadPool;
import org.dromara.hutool.core.thread.RetryableTask;
import org.dromara.hutool.core.thread.ThreadUtil;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
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
	@SafeVarargs
	public static void ofException(Runnable run, long maxAttempts, Duration delay, Runnable recover, Class<? extends Throwable>... exs) {
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
	@SafeVarargs
	public static <T> T ofException(Supplier<T> sup, long maxAttempts, Duration delay, Supplier<T> recover, Class<? extends Throwable>... exs) {
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
	public static void ofPredicate(Runnable run, long maxAttempts, Duration delay, Supplier<Void> recover, BiPredicate<Void, Throwable> predicate) {
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
	public static <T> T ofPredicate(Supplier<T> sup, long maxAttempts, Duration delay, Supplier<T> recover, BiPredicate<T, Throwable> predicate) {
		return RetryableTask.retryForPredicate(sup, predicate)
			.delay(delay)
			.maxAttempts(maxAttempts)
			.execute()
			.get()
			.orElseGet(recover);
	}


	/**
	 * 从不停止的执行方法
	 *
	 * @param run   执行方法
	 * @param delay 间隔时间
	 * @param isEx  true：出现异常继续执行；false：则出现异常跳出执行。
	 */
	public static void ofNeverStop(Runnable run, Duration delay, boolean isEx) {
		while (true) {
			try {
				run.run();
			} catch (Throwable e) {
				if (!isEx) {
					break;
				}
			} finally {
				ThreadUtil.sleep(delay.toMillis());
			}
		}
	}

	/**
	 * 从不停止的执行方法，异步执行
	 *
	 * @param run   执行方法
	 * @param delay 间隔时间
	 * @param isEx  true：出现异常继续执行；false：则出现异常跳出执行。
	 */
	public static void ofNeverStopAsync(Runnable run, Duration delay, boolean isEx) {
		CompletableFuture.runAsync(() -> ofNeverStop(run, delay, isEx), GlobalThreadPool.getExecutor());
	}

}
