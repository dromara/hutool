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
		this.partition.returnObject(this.getRaw());
	}

	@Override
	public long getLastReturn() {
		return lastBorrow;
	}

	@Override
	public void setLastReturn(final long lastReturn) {
		this.lastBorrow = lastReturn;
	}
}
