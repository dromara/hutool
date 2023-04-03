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
package org.dromara.hutool.core.collection;

import org.dromara.hutool.core.collection.queue.CheckedLinkedBlockingQueue;
import org.dromara.hutool.core.thread.SimpleScheduler;
import org.dromara.hutool.core.util.RuntimeUtil;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

/**
 * 内存安全的{@link LinkedBlockingQueue}，可以解决OOM问题。<br>
 * 原理是通过Runtime#freeMemory()获取剩余内存，当剩余内存低于指定的阈值时，不再加入。
 *
 * <p>
 * 此类来自于：<a href="https://github.com/apache/incubator-shenyu/blob/master/shenyu-common/src/main/java/org/apache/shenyu/common/concurrent/MemorySafeLinkedBlockingQueue.java">
 * Apache incubator-shenyu</a>
 * </p>
 *
 * @author incubator-shenyu
 * @since 6.0.0
 */
public class MemorySafeLinkedBlockingQueue<E> extends CheckedLinkedBlockingQueue<E> {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param maxFreeMemory 最大剩余内存大小，当实际内存小于这个值时，不再加入元素
	 */
	public MemorySafeLinkedBlockingQueue(final long maxFreeMemory) {
		super(new MemoryChecker<>(maxFreeMemory));
	}

	/**
	 * 构造
	 *
	 * @param c             初始集合
	 * @param maxFreeMemory 最大剩余内存大小，当实际内存小于这个值时，不再加入元素
	 */
	public MemorySafeLinkedBlockingQueue(final Collection<? extends E> c,
										 final long maxFreeMemory) {
		super(c, new MemoryChecker<>(maxFreeMemory));
	}

	/**
	 * set the max free memory.
	 *
	 * @param maxFreeMemory the max free memory
	 */
	public void setMaxFreeMemory(final int maxFreeMemory) {
		((MemoryChecker<E>) this.checker).maxFreeMemory = maxFreeMemory;
	}

	/**
	 * get the max free memory.
	 *
	 * @return the max free memory limit
	 */
	public long getMaxFreeMemory() {
		return ((MemoryChecker<E>) this.checker).maxFreeMemory;
	}

	/**
	 * 根据剩余内存判定的检查器
	 *
	 * @param <E> 元素类型
	 */
	private static class MemoryChecker<E> implements Predicate<E> {

		private long maxFreeMemory;

		private MemoryChecker(final long maxFreeMemory) {
			this.maxFreeMemory = maxFreeMemory;
		}

		@Override
		public boolean test(final E e) {
			return FreeMemoryCalculator.INSTANCE.getResult() > maxFreeMemory;
		}
	}

	/**
	 * 获取内存剩余大小的定时任务
	 */
	private static class FreeMemoryCalculator extends SimpleScheduler<Long> {
		private static final FreeMemoryCalculator INSTANCE = new FreeMemoryCalculator();

		FreeMemoryCalculator() {
			super(new SimpleScheduler.Job<Long>() {
				private volatile long maxAvailable;

				@Override
				public Long getResult() {
					return this.maxAvailable;
				}

				@Override
				public void run() {
					this.maxAvailable = RuntimeUtil.getFreeMemory();
				}
			}, 50);
		}
	}
}
