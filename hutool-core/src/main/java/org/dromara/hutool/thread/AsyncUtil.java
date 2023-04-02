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

package org.dromara.hutool.thread;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * {@link CompletableFuture}异步工具类<br>
 * {@link CompletableFuture} 是 Future 的改进，可以通过传入回调对象，在任务完成后调用之
 *
 * @author achao1441470436@gmail.com
 * @since 5.7.17
 */
public class AsyncUtil {

	/**
	 * 等待所有任务执行完毕，包裹了异常
	 *
	 * @param tasks 并行任务
	 * @throws UndeclaredThrowableException 未受检异常
	 */
	public static void waitAll(final CompletableFuture<?>... tasks) {
		try {
			CompletableFuture.allOf(tasks).get();
		} catch (final InterruptedException | ExecutionException e) {
			throw new ThreadException(e);
		}
	}

	/**
	 * 等待任意一个任务执行完毕，包裹了异常
	 *
	 * @param <T>  任务返回值类型
	 * @param tasks 并行任务
	 * @return 执行结束的任务返回值
	 * @throws UndeclaredThrowableException 未受检异常
	 */
	@SuppressWarnings("unchecked")
	public static <T> T waitAny(final CompletableFuture<?>... tasks) {
		try {
			return (T) CompletableFuture.anyOf(tasks).get();
		} catch (final InterruptedException | ExecutionException e) {
			throw new ThreadException(e);
		}
	}

	/**
	 * 获取异步任务结果，包裹了异常
	 *
	 * @param <T>  任务返回值类型
	 * @param task 异步任务
	 * @return 任务返回值
	 * @throws RuntimeException 未受检异常
	 */
	public static <T> T get(final CompletableFuture<T> task) {
		try {
			return task.get();
		} catch (final InterruptedException | ExecutionException e) {
			throw new ThreadException(e);
		}
	}

}
