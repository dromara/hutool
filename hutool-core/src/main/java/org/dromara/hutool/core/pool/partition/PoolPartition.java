/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.pool.partition;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.pool.*;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 对象池分区<br>
 * 一个分区实际为一个小的对象池，持有一个阻塞队列。<br>
 * 初始化时创建{@link PoolConfig#getMinSize()}个对象作为初始池对象.
 *
 * <p>
 * 当借出对象时，从队列头部取出并验证，验证通过后使用，验证不通过直接调用{@link #free(Object)} 销毁并重新获取，
 * 当池中对象都被借出（空了），创建新的对象并入队列，直到队列满为止，当满时等待归还，超时则报错。
 * </p>
 *
 * <p>
 * 当归还对象时，验证对象，不可用销毁之，可用入队列。
 * </p>
 *
 * <p>
 * 一个分区队列的实际
 * </p>
 *
 * @param <T> 对象类型
 */
public class PoolPartition<T> implements ObjectPool<T> {
	private static final long serialVersionUID = 1L;

	private final PoolConfig config;
	private final ObjectFactory<T> objectFactory;

	private BlockingQueue<Poolable<T>> queue;
	// 记录对象总数（包括借出对象）
	private int total;

	/**
	 * 构造
	 *
	 * @param config        池配置
	 * @param queue         阻塞队列类型
	 * @param objectFactory 对象工厂，用于管理对象创建、检查和销毁
	 */
	public PoolPartition(final PoolConfig config, final BlockingQueue<Poolable<T>> queue, final ObjectFactory<T> objectFactory) {
		this.config = config;
		this.queue = queue;
		this.objectFactory = objectFactory;

		// 初始化，按照配置最小大小创建对象
		doIncrease(config.getMinSize());
	}

	@SuppressWarnings("resource")
	@Override
	public T borrowObject() {
		// 非阻塞获取
		Poolable<T> poolable = this.queue.poll();
		if (null != poolable) {
			final T obj = poolable.getRaw();
			// 检查对象是否可用
			if (this.objectFactory.validate(obj)) {
				// 检查是否超过最长空闲时间
				final long maxIdle = this.config.getMaxIdle();
				if (maxIdle <= 0 || poolable.getIdle() <= maxIdle) {
					return poolable.getRaw();
				}
			}

			// 对象不可用或超过最长空闲期，销毁之
			free(obj);
			// 继续借，而不扩容
			return borrowObject();
		}

		// 池中无对象，扩容
		if (increase(1) <= 0) {
			// 池分区已满，只能等待是否有返还的对象
			poolable = waitingPoll();
			if (null == poolable) {
				// 池空间达到最大值，但是无可用对象
				throw new PoolException("Pool exhausted!");
			}
		}

		// 扩容成功，继续借对象
		// 如果线程1扩容，但是被线程2借走，则继续递归扩容获取对象，直到获取到或全部借走为止
		return borrowObject();
	}

	/**
	 * 归还对象
	 *
	 * @param obj 归还的对象
	 * @return this
	 */
	@SuppressWarnings("resource")
	@Override
	public PoolPartition<T> returnObject(final T obj) {
		// 检查对象可用性
		if (this.objectFactory.validate(obj)) {
			try {
				this.queue.put(wrapPoolable(obj));
			} catch (final InterruptedException e) {
				throw new PoolException(e);
			}
		} else {
			// 对象不可用
			free(obj);
		}

		return this;
	}

	/**
	 * 扩容并填充对象池队列<br>
	 * 如果传入的扩容大小大于可用大小（即扩容大小加现有大小大于最大大小，则实际扩容到最大）
	 *
	 * @param increaseSize 扩容大小
	 * @return 实际扩容大小，0表示已经达到最大，未成功扩容
	 */
	public synchronized int increase(final int increaseSize) {
		return doIncrease(increaseSize);
	}

	/**
	 * 销毁对象，注意此方法操作的对象必须在队列外
	 *
	 * @param obj 被销毁的对象
	 * @return this
	 */
	@Override
	public synchronized PoolPartition<T> free(final T obj) {
		objectFactory.destroy(obj);
		total--;
		return this;
	}

	@Override
	public int getTotal() {
		return this.total;
	}

	@Override
	public int getIdleCount() {
		return this.queue.size();
	}

	@Override
	public int getActiveCount() {
		return getTotal() - getIdleCount();
	}

	@Override
	synchronized public void close() throws IOException {
		this.queue.forEach((poolable) -> objectFactory.destroy(poolable.getRaw()));
		this.queue.clear();
		this.queue = null;
		this.total = 0;
	}

	/**
	 * 创建{@link PartitionPoolable}
	 *
	 * @return {@link PartitionPoolable}
	 */
	protected Poolable<T> createPoolable() {
		final T t = objectFactory.create();
		Assert.notNull(t, "Null object created and not allow!");
		return wrapPoolable(t);
	}

	@SuppressWarnings("unchecked")
	private Poolable<T> wrapPoolable(final T t) {
		if (t instanceof Poolable) {
			return (Poolable<T>) t;
		}
		return new PartitionPoolable<>(t, this);
	}

	/**
	 * 非线程安全的扩容并填充对象池队列<br>
	 * 如果传入的扩容大小大于可用大小（即扩容大小加现有大小大于最大大小，则实际扩容到最大）
	 *
	 * @param increaseSize 扩容大小
	 * @return 实际扩容大小，0表示已经达到最大，未成功扩容
	 */
	private int doIncrease(int increaseSize) {
		final int maxSize = config.getMaxSize();
		if (increaseSize + total > maxSize) {
			increaseSize = maxSize - total;
		}

		try {
			for (int i = 0; i < increaseSize; i++) {
				queue.put(createPoolable());
			}
			total += increaseSize;
		} catch (final InterruptedException e) {
			throw new PoolException(e);
		}
		return increaseSize;
	}

	/**
	 * 从队列中取出头部的对象，如果队列为空，则等待<br>
	 * 等待的时间取决于{@link PoolConfig#getMaxWait()}，小于等于0时一直等待，否则等待给定毫秒数
	 *
	 * @return 取出的池对象
	 * @throws PoolException 中断异常
	 */
	private Poolable<T> waitingPoll() throws PoolException {
		final long maxWait = this.config.getMaxWait();
		try {
			if (maxWait <= 0) {
				return this.queue.take();
			}
			return this.queue.poll(maxWait, TimeUnit.MILLISECONDS);
		} catch (final InterruptedException e) {
			throw new PoolException(e);
		}
	}
}
