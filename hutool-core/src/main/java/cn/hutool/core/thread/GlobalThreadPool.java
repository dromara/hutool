package cn.hutool.core.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import cn.hutool.core.exceptions.UtilException;

/**
 * 全局公共线程池<br>
 * 此线程池是一个无限线程池，即加入的线程不等待任何线程，直接执行
 *
 * @author Looly
 *
 */
public class GlobalThreadPool {
	private static ExecutorService executor;

	private GlobalThreadPool() {
	}

	static {
		init();
	}

	/**
	 * 初始化全局线程池
	 */
	synchronized public static void init() {
		if (null != executor) {
			executor.shutdownNow();
		}
		executor = ExecutorBuilder.create().useSynchronousQueue().build();
	}

	/**
	 * 关闭公共线程池
	 *
	 * @param isNow 是否立即关闭而不等待正在执行的线程
	 */
	synchronized public static void shutdown(boolean isNow) {
		if (null != executor) {
			if (isNow) {
				executor.shutdownNow();
			} else {
				executor.shutdown();
			}
		}
	}

	/**
	 * 获得 {@link ExecutorService}
	 *
	 * @return {@link ExecutorService}
	 */
	public static ExecutorService getExecutor() {
		return executor;
	}

	/**
	 * 直接在公共线程池中执行线程
	 *
	 * @param runnable 可运行对象
	 */
	public static void execute(Runnable runnable) {
		try {
			executor.execute(runnable);
		} catch (Exception e) {
			throw new UtilException(e, "Exception when running task!");
		}
	}

	/**
	 * 执行有返回值的异步方法<br>
	 * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
	 *
	 * @param <T> 执行的Task
	 * @param task {@link Callable}
	 * @return Future
	 */
	public static <T> Future<T> submit(Callable<T> task) {
		return executor.submit(task);
	}

	/**
	 * 执行有返回值的异步方法<br>
	 * Future代表一个异步执行的操作，通过get()方法可以获得操作的结果，如果异步操作还没有完成，则，get()会使当前线程阻塞
	 *
	 * @param runnable 可运行对象
	 * @return {@link Future}
	 * @since 3.0.5
	 */
	public static Future<?> submit(Runnable runnable) {
		return executor.submit(runnable);
	}
}
