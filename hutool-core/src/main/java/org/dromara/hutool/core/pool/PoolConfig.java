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

package org.dromara.hutool.core.pool;

/**
 * 对象池配置，提供基本的配置项，包括：
 * <ul>
 *     <li>最小池大小（初始大小）</li>
 *     <li>最大池大小</li>
 *     <li>最大池大小</li>
 *     <li>最长等待时间</li>
 *     <li>最长空闲时间</li>
 * </ul>
 */
public class PoolConfig {
	private int minSize;
	private int maxSize;

	private long maxWait;
	private long maxIdle;

	public int getMinSize() {
		return minSize;
	}

	public PoolConfig setMinSize(final int minSize) {
		this.minSize = minSize;
		return this;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public PoolConfig setMaxSize(final int maxSize) {
		this.maxSize = maxSize;
		return this;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public PoolConfig setMaxWait(final long maxWait) {
		this.maxWait = maxWait;
		return this;
	}

	public long getMaxIdle() {
		return maxIdle;
	}

	public PoolConfig setMaxIdle(final long maxIdle) {
		this.maxIdle = maxIdle;
		return this;
	}
}
