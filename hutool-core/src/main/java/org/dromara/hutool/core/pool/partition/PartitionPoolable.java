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

public class PartitionPoolable<T> implements Poolable<T> {

	private final T raw;
	private final PoolPartition<T> partition;

	public PartitionPoolable(final T raw, PoolPartition<T> partition) {
		this.raw = raw;
		this.partition = partition;
	}

	@Override
	public T getRaw() {
		return this.raw;
	}

	@Override
	public void returnObject() {
		this.partition.returnObject(this);
	}
}
