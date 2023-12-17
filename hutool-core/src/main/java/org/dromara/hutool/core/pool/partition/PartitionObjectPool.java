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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.pool.*;
import org.dromara.hutool.core.thread.ThreadUtil;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 分区对象池实现<br>
 * 来自：https://github.com/DanielYWoo/fast-object-pool/blob/master/src/main/java/cn/danielw/fop/ObjectPool.java
 *
 * @param <T> 对象类型
 * @author Daniel, Looly
 */
public class PartitionObjectPool<T> implements ObjectPool<T> {
	private static final long serialVersionUID = 1L;

	private final PartitionPoolConfig config;
	// 分区，创建后不再变更，线程安全
	private final PoolPartition<T>[] partitions;

	private boolean closed;

	/**
	 * 构造
	 *
	 * @param config  配置
	 * @param factory 对象工厂，用于创建、验证和销毁对象
	 */
	@SuppressWarnings({"unchecked", "resource"})
	public PartitionObjectPool(final PartitionPoolConfig config, final ObjectFactory<T> factory) {
		this.config = config;

		final int partitionSize = config.getPartitionSize();
		this.partitions = new PoolPartition[partitionSize];
		for (int i = 0; i < partitionSize; i++) {
			partitions[i] = new PoolPartition<>(config, createBlockingQueue(config), factory);
		}
	}

	/**
	 * 获取持有对象总数
	 *
	 * @return 总数
	 */
	public int getTotal() {
		int size = 0;
		for (final PoolPartition<T> subPool : partitions) {
			size += subPool.getTotal();
		}
		return size;
	}

	@Override
	public Poolable<T> borrowObject() {
		checkClosed();
		final int partitionIndex = (int) (ThreadUtil.currentThreadId() % config.getPartitionSize());
		return this.partitions[partitionIndex].borrowObject();
	}

	public PartitionObjectPool<T> returnObject(final Poolable<T> obj) {
		checkClosed();
		final int partitionIndex = (int) (ThreadUtil.currentThreadId() % config.getPartitionSize());
		this.partitions[partitionIndex].returnObject(obj);
		return this;
	}

	@Override
	public void close() throws IOException {
		this.closed = true;
		IoUtil.closeQuietly(this.partitions);
	}

	protected BlockingQueue<Poolable<T>> createBlockingQueue(final PoolConfig poolConfig) {
		return new ArrayBlockingQueue<>(poolConfig.getMaxSize());
	}

	private void checkClosed() {
		if (this.closed) {
			throw new IllegalStateException("Object Pool is closed!");
		}
	}
}
