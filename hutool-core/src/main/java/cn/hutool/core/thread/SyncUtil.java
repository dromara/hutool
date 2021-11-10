package cn.hutool.core.thread;

import cn.hutool.core.exceptions.ExceptionUtil;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * CompletableFuture工具类，叫CompletableFutureUtil太长
 *
 * @author <achao1441470436@gmail.com>
 * @since 2021/11/10 0010 20:55
 */
public class SyncUtil {

	private SyncUtil() {
		/* Do not new me! */
	}

	/**
	 * 等待所有任务执行完毕，包裹了异常
	 *
	 * @param tasks 并行任务
	 * @throws UndeclaredThrowableException 未受检异常
	 */
	public static void wait(CompletableFuture<?>... tasks) {
		try {
			CompletableFuture.allOf(tasks).get();
		} catch (InterruptedException | ExecutionException e) {
			ExceptionUtil.wrapAndThrow(e);
		}
	}

	/**
	 * 获取异步任务结果，包裹了异常
	 *
	 * @param task 异步任务
	 * @param <T>  任务返回值类型
	 * @return 任务返回值
	 * @throws RuntimeException 未受检异常
	 */
	public static <T> T get(CompletableFuture<T> task) {
		RuntimeException exception;
		try {
			return task.get();
		} catch (InterruptedException | ExecutionException e) {
			exception = ExceptionUtil.wrapRuntime(e);
		}
		throw exception;
	}

}
