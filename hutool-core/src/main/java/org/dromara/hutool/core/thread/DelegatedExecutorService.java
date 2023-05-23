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

import org.dromara.hutool.core.lang.Assert;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * ExecutorService代理
 *
 * @author loolly
 */
public class DelegatedExecutorService extends AbstractExecutorService {
	private final ExecutorService e;

	/**
	 * 构造
	 *
	 * @param executor {@link ExecutorService}
	 */
	public DelegatedExecutorService(final ExecutorService executor) {
		Assert.notNull(executor, "executor must be not null !");
		e = executor;
	}

	@Override
	public void execute(final Runnable command) {
		e.execute(command);
	}

	@Override
	public void shutdown() {
		e.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return e.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return e.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return e.isTerminated();
	}

	@Override
	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
		return e.awaitTermination(timeout, unit);
	}

	@Override
	public Future<?> submit(final Runnable task) {
		return e.submit(task);
	}

	@Override
	public <T> Future<T> submit(final Callable<T> task) {
		return e.submit(task);
	}

	@Override
	public <T> Future<T> submit(final Runnable task, final T result) {
		return e.submit(task, result);
	}

	@Override
	public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return e.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit)
			throws InterruptedException {
		return e.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(final Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		return e.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return e.invokeAny(tasks, timeout, unit);
	}
}
