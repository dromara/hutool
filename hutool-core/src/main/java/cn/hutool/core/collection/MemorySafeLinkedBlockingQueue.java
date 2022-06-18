/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.hutool.core.collection;

import cn.hutool.core.thread.SimpleScheduler;
import cn.hutool.core.util.RuntimeUtil;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
public class MemorySafeLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {
	private static final long serialVersionUID = 8032578371749960142L;

	private long maxFreeMemory;

	/**
	 * 构造
	 *
	 * @param maxFreeMemory 最大剩余内存大小，当实际内存小于这个值时，不再加入元素
	 */
	public MemorySafeLinkedBlockingQueue(final long maxFreeMemory) {
		super(Integer.MAX_VALUE);
		this.maxFreeMemory = maxFreeMemory;
	}

	/**
	 * 构造
	 *
	 * @param c             初始集合
	 * @param maxFreeMemory 最大剩余内存大小，当实际内存小于这个值时，不再加入元素
	 */
	public MemorySafeLinkedBlockingQueue(final Collection<? extends E> c,
										 final long maxFreeMemory) {
		super(c);
		this.maxFreeMemory = maxFreeMemory;
	}

	/**
	 * set the max free memory.
	 *
	 * @param maxFreeMemory the max free memory
	 */
	public void setMaxFreeMemory(final int maxFreeMemory) {
		this.maxFreeMemory = maxFreeMemory;
	}

	/**
	 * get the max free memory.
	 *
	 * @return the max free memory limit
	 */
	public long getMaxFreeMemory() {
		return maxFreeMemory;
	}

	/**
	 * determine if there is any remaining free memory.<br>
	 * 剩余内存是否大于给定阈值
	 *
	 * @return {@code true}则表示大于阈值，可以加入元素，否则无法加入
	 */
	public boolean hasRemainedMemory() {
		return FreeMemoryCalculator.INSTANCE.getResult() > maxFreeMemory;
	}

	@Override
	public void put(final E e) throws InterruptedException {
		if (hasRemainedMemory()) {
			super.put(e);
		}
	}

	@Override
	public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
		return hasRemainedMemory() && super.offer(e, timeout, unit);
	}

	@Override
	public boolean offer(final E e) {
		return hasRemainedMemory() && super.offer(e);
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
