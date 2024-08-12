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

import org.dromara.hutool.core.pool.PoolConfig;

/**
 * 分局对象池配置
 *
 * @author looly
 */
public class PartitionPoolConfig extends PoolConfig {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建{@code PartitionPoolConfig}
	 *
	 * @return {@code PartitionPoolConfig}
	 */
	public static PartitionPoolConfig of() {
		return new PartitionPoolConfig();
	}

	private int partitionSize = 4;

	/**
	 * 获取分区大小
	 *
	 * @return 分区大小
	 */
	public int getPartitionSize() {
		return partitionSize;
	}

	/**
	 * 设置分区大小
	 *
	 * @param partitionSize 分区大小
	 * @return this
	 */
	public PartitionPoolConfig setPartitionSize(final int partitionSize) {
		this.partitionSize = partitionSize;
		return this;
	}
}
