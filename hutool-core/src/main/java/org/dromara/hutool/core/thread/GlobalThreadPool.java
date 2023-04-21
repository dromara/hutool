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

import org.dromara.hutool.core.exceptions.HutoolException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

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
		executor = ExecutorBuilder.of().useSynchronousQueue().build();
	}

	/**
	 * 关闭公共线程池
	 *
	 * @param isNow 是否立即关闭而不等待正在执行的线程
	 */
	synchronized public static void shutdown(final boolean isNow) {
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
	public static void execute(final Runnable runnable) {
		try {
			executor.execute(runnable);
		} catch (final Exception e) {
			throw new HutoolException(e, "Exception when running task!");
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
	public static <T> Future<T> submit(final Callable<T> task) {
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
	public static Future<?> submit(final Runnable runnable) {
		return executor.submit(runnable);
	}
}
