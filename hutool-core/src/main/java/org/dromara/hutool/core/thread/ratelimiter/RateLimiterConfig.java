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
	 * @param timeout            超时时间，即超过这个时间没有获取到许可，则返回false
	 * @param limitRefreshPeriod 限制刷新周期，即每多少时间刷新一次
	 * @param limitForPeriod     个周期的许可数
	 * @return RateLimiterConfig
	 */
	public static RateLimiterConfig of(final Duration timeout, final Duration limitRefreshPeriod, final int limitForPeriod) {
		return new RateLimiterConfig(timeout, limitRefreshPeriod, limitForPeriod);
	}

	/**
	 * 超时时间，即超过这个时间没有获取到许可，则返回false
	 */
	private final Duration timeout;
	/**
	 * 限制刷新周期，即每多少时间刷新一次
	 */
	private final Duration refreshPeriod;
	/**
	 * 容量，可以是总容量，或者每个周期的容量
	 */
	private final int capacity;

	/**
	 * 构造
	 *
	 * @param timeout       超时时间，即超过这个时间没有获取到许可，则返回false
	 * @param refreshPeriod 刷新周期，即每多少时间刷新一次
	 * @param capacity      容量
	 */
	public RateLimiterConfig(final Duration timeout, final Duration refreshPeriod, final int capacity) {
		this.timeout = timeout;
		this.refreshPeriod = refreshPeriod;
		this.capacity = capacity;
	}

	/**
	 * 超时时间，即超过这个时间没有获取到许可，则返回false，单位毫秒
	 *
	 * @return 超时时间，单位毫秒
	 */
	public Duration getTimeout() {
		return timeout;
	}

	/**
	 * 刷新周期，即每多少时间刷新一次，单位毫秒
	 *
	 * @return 刷新周期，单位毫秒
	 */
	public Duration getRefreshPeriod() {
		return refreshPeriod;
	}

	/**
	 * 容量，可以是总容量，或者每个周期的容量
	 *
	 * @return 容量
	 */
	public int getCapacity() {
		return capacity;
	}
}
