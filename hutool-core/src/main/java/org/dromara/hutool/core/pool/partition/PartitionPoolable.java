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

package org.dromara.hutool.core.pool.partition;

import org.dromara.hutool.core.pool.SimplePoolable;

/**
 * 分区可池化对象，此对象会同时持有原始对象和所在的分区
 *
 * @param <T> 对象类型
 */
public class PartitionPoolable<T> extends SimplePoolable<T> {

	private final PoolPartition<T> partition;

	/**
	 * 构造
	 *
	 * @param raw       原始对象
	 * @param partition 对象所在分区
	 */
	public PartitionPoolable(final T raw, final PoolPartition<T> partition) {
		super(raw);
		this.partition = partition;
	}

	/**
	 * 归还对象
	 */
	public void returnObject() {
		this.partition.returnObject(this.getRaw());
	}

	/**
	 * 释放对象
	 */
	public void free() {
		this.partition.free(this.getRaw());
	}
}
