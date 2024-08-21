/*
 * Copyright (c) 2024 Hutool Team.
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

package org.dromara.hutool.core.thread.ratelimiter;

import java.time.Duration;

/**
 * 限流通用配置
 *
 * @author Looly
 * @since 6.0.0
 */
public class RateLimiterConfig {

	/**
	 * 创建限流配置
	 *
	 * @return RateLimiterConfig
	 */
	public static RateLimiterConfig of() {
		return new RateLimiterConfig();
	}

	/**
	 * 超时时间，即超过这个时间没有获取到许可，则返回false
	 */
	private Duration timeout;
	/**
	 * 限制刷新周期，即每多少时间刷新一次
	 */
	private Duration refreshPeriod;
	/**
	 * 容量，可以是总容量，或者每个周期的容量
	 */
	private int capacity;
	/**
	 * 在刷新周期内释放的最大数量，不能超过{@link #capacity}
	 */
	private int maxReleaseCount;

	/**
	 * 超时时间，即超过这个时间没有获取到许可，则返回false
	 *
	 * @return 超时时间
	 */
	public Duration getTimeout() {
		return timeout;
	}

	/**
	 * 设置超时时间，即超过这个时间没有获取到许可，则返回false
	 *
	 * @param timeout 超时时间
	 * @return this
	 */
	public RateLimiterConfig setTimeout(final Duration timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * 刷新周期，即每多少时间刷新一次
	 *
	 * @return 刷新周期
	 */
	public Duration getRefreshPeriod() {
		return refreshPeriod;
	}

	/**
	 * 设置刷新周期，即每多少时间刷新一次，单位毫秒
	 *
	 * @param refreshPeriod 刷新周期
	 * @return this
	 */
	public RateLimiterConfig setRefreshPeriod(final Duration refreshPeriod) {
		this.refreshPeriod = refreshPeriod;
		return this;
	}

	/**
	 * 容量，可以是总容量，或者每个周期的容量
	 *
	 * @return 容量
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * 设置容量，可以是总容量，或者每个周期的容量
	 * @param capacity 容量
	 * @return this
	 */
	public RateLimiterConfig setCapacity(final int capacity) {
		this.capacity = capacity;
		return this;
	}

	/**
	 * 在刷新周期内释放的最大数量，不能超过{@link #capacity}
	 *
	 * @return 在刷新周期内释放的最大数量
	 */
	public int getMaxReleaseCount() {
		return maxReleaseCount;
	}

	/**
	 * 设置在刷新周期内释放的最大数量，不能超过{@link #capacity}
	 *
	 * @param maxReleaseCount 在刷新周期内释放的最大数量
	 * @return this
	 */
	public RateLimiterConfig setMaxReleaseCount(final int maxReleaseCount) {
		this.maxReleaseCount = maxReleaseCount;
		return this;
	}
}
