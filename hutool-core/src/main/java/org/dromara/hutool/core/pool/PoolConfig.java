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

package org.dromara.hutool.core.pool;

import java.io.Serializable;

/**
 * 对象池配置，提供基本的配置项，包括：
 * <ul>
 *     <li>最小池大小（初始大小）</li>
 *     <li>最大池大小</li>
 *     <li>最长等待时间</li>
 *     <li>最长空闲时间</li>
 * </ul>
 *
 * @author Daniel, Looly
 */
public class PoolConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建{@code PoolConfig}
	 *
	 * @return {@code PoolConfig}
	 */
	public static PoolConfig of() {
		return new PoolConfig();
	}

	/**
	 * 最小（初始）池大小
	 */
	private int minSize = 5;
	/**
	 * 最大池大小
	 */
	private int maxSize = 20;

	/**
	 * 最长等待时间，用于在借出对象时，等待最长时间（默认5秒）。
	 */
	private long maxWait = 5000;
	/**
	 * 最长空闲时间（在池中时间）
	 */
	private long maxIdle;

	/**
	 * 获取最小（初始）池大小
	 *
	 * @return 最小（初始）池大小
	 */
	public int getMinSize() {
		return minSize;
	}

	/**
	 * 设置最小（初始）池大小
	 *
	 * @param minSize 最小（初始）池大小
	 * @return this
	 */
	public PoolConfig setMinSize(final int minSize) {
		this.minSize = minSize;
		return this;
	}

	/**
	 * 获取最大池大小
	 *
	 * @return 最大池大小
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * 设置最大池大小
	 *
	 * @param maxSize 最大池大小
	 * @return this
	 */
	public PoolConfig setMaxSize(final int maxSize) {
		this.maxSize = maxSize;
		return this;
	}

	/**
	 * 获取最长等待时间，用于在借出对象时，等待最长时间。
	 *
	 * @return 最长等待时间，用于在借出对象时，等待最长时间。
	 */
	public long getMaxWait() {
		return maxWait;
	}

	/**
	 * 设置最长等待时间，用于在借出对象时，等待最长时间。
	 *
	 * @param maxWait 最长等待时间，用于在借出对象时，等待最长时间。
	 * @return this
	 */
	public PoolConfig setMaxWait(final long maxWait) {
		this.maxWait = maxWait;
		return this;
	}

	/**
	 * 获取最长空闲时间（在池中时间）
	 *
	 * @return 最长空闲时间（在池中时间）,小于等于0表示不限制
	 */
	public long getMaxIdle() {
		return maxIdle;
	}

	/**
	 * 设置最长空闲时间（在池中时间）
	 *
	 * @param maxIdle 最长空闲时间（在池中时间）,小于等于0表示不限制
	 * @return this
	 */
	public PoolConfig setMaxIdle(final long maxIdle) {
		this.maxIdle = maxIdle;
		return this;
	}
}
