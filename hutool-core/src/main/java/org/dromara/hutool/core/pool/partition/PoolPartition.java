/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.pool.partition;

import org.dromara.hutool.core.exception.HutoolException;
import org.dromara.hutool.core.pool.ObjectFactory;
import org.dromara.hutool.core.pool.PoolConfig;
import org.dromara.hutool.core.pool.Poolable;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PoolPartition<T> implements Closeable {

	private final PoolConfig config;
	private final ObjectFactory<T> objectFactory;

	private BlockingQueue<Poolable<T>> queue;
	// 记录对象总数（包括借出对象）
	private int total;

	public PoolPartition(PoolConfig config, BlockingQueue<Poolable<T>> queue, ObjectFactory<T> objectFactory) {
		this.config = config;
		this.queue = queue;
		this.objectFactory = objectFactory;

		final int minSize = config.getMinSize();
		for (int i = 0; i < minSize; i++) {
			queue.add(createPoolable());
		}
		total = minSize;
	}

	public Poolable<T> borrowObject() {
		// 非阻塞获取
		Poolable<T> poolable = this.queue.poll();
		if (null != poolable) {
			return poolable;
		}

		// 扩容
		if (increase(1) <= 0) {
			// 池分区已满，只能等待是否有返还的对象
			poolable = waitingPoll();
			if (null == poolable) {
				// 池空间达到最大值，但是无可用对象
				throw new HutoolException("Pool exhausted!");
			}
		}

		// 扩容成功，继续借对象
		return borrowObject();
	}

	/**
	 * 归还对象
	 *
	 * @param obj 归还的对象
	 * @return this
	 */
	public PoolPartition<T> returnObject(final Poolable<T> obj) {
		try {
			this.queue.put(obj);
		} catch (InterruptedException e) {
			throw new HutoolException(e);
		}
		return this;
	}

	public synchronized int increase(int increaseSize) {
		if (increaseSize + total > config.getMaxSize()) {
			increaseSize = config.getMaxSize() - total;
		}

		try {
			for (int i = 0; i < increaseSize; i++) {
				queue.put(createPoolable());
			}
			total += increaseSize;
		} catch (final InterruptedException e) {
			throw new HutoolException(e);
		}
		return increaseSize;
	}

	/**
	 * 销毁对象，注意此方法操作的对象必须在队列外
	 *
	 * @param obj 被销毁的对象
	 * @return this
	 */
	public synchronized PoolPartition<T> free(final Poolable<T> obj) {
		objectFactory.destroy(obj.getRaw());
		total--;
		return this;
	}

	/**
	 * 获取对象总数，包括借出对象数
	 *
	 * @return 对象数
	 */
	public int getTotal() {
		return this.total;
	}

	@Override
	public void close() throws IOException {
		this.queue.forEach(this::free);
		this.queue.clear();
		this.queue = null;
	}

	protected Poolable<T> createPoolable() {
		return new PartitionPoolable<>(objectFactory.create(), this);
	}

	private Poolable<T> waitingPoll() {
		final long maxWait = this.config.getMaxWait();
		try {
			if (maxWait <= 0) {
				return this.queue.take();
			}
			return this.queue.poll(maxWait, TimeUnit.MILLISECONDS);
		} catch (final InterruptedException e) {
			throw new HutoolException(e);
		}
	}
}
