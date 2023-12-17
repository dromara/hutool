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

import org.dromara.hutool.core.pool.Poolable;

/**
 * 分区可池化对象，此对象会同时持有原始对象和所在的分区
 *
 * @param <T> 对象类型
 */
public class PartitionPoolable<T> implements Poolable<T> {

	private final T raw;
	private final PoolPartition<T> partition;
	private long lastBorrow;

	/**
	 * 构造
	 *
	 * @param raw       原始对象
	 * @param partition 对象所在分区
	 */
	public PartitionPoolable(final T raw, final PoolPartition<T> partition) {
		this.raw = raw;
		this.partition = partition;
		this.lastBorrow = System.currentTimeMillis();
	}

	@Override
	public T getRaw() {
		return this.raw;
	}

	/**
	 * 归还对象
	 */
	public void returnObject() {
		this.partition.returnObject(this);
	}

	@Override
	public long getLastBorrow() {
		return lastBorrow;
	}

	@Override
	public void setLastBorrow(final long lastBorrow) {
		this.lastBorrow = lastBorrow;
	}
}
