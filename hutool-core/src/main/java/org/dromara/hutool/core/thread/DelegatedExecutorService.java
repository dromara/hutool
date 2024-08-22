/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.thread;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;

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
public class DelegatedExecutorService extends AbstractExecutorService implements Wrapper<ExecutorService> {
	private final ExecutorService raw;

	/**
	 * 构造
	 *
	 * @param executor {@link ExecutorService}
	 */
	public DelegatedExecutorService(final ExecutorService executor) {
		raw = Assert.notNull(executor, "executor must be not null !");
	}

	@Override
	public ExecutorService getRaw() {
		return this.raw;
	}

	@Override
	public void execute(final Runnable command) {
		raw.execute(command);
	}

	@Override
	public void shutdown() {
		raw.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return raw.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return raw.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return raw.isTerminated();
	}

	@Override
	public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
		return raw.awaitTermination(timeout, unit);
	}

	@Override
	public Future<?> submit(final Runnable task) {
		return raw.submit(task);
	}

	@Override
	public <T> Future<T> submit(final Callable<T> task) {
		return raw.submit(task);
	}

	@Override
	public <T> Future<T> submit(final Runnable task, final T result) {
		return raw.submit(task, result);
	}

	@Override
	public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return raw.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit)
			throws InterruptedException {
		return raw.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(final Collection<? extends Callable<T>> tasks)
			throws InterruptedException, ExecutionException {
		return raw.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(final Collection<? extends Callable<T>> tasks, final long timeout, final TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return raw.invokeAny(tasks, timeout, unit);
	}
}
