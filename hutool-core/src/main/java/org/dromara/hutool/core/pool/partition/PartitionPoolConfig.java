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
