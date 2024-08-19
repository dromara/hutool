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
	 * @param timeoutDuration    超时时间，即超过这个时间没有获取到许可，则返回false
	 * @param limitRefreshPeriod 限制刷新周期，即每多少时间刷新一次
	 * @param limitForPeriod     个周期的许可数
	 * @return RateLimiterConfig
	 */
	public static RateLimiterConfig of(final long timeoutDuration, final long limitRefreshPeriod, final int limitForPeriod) {
		return new RateLimiterConfig(timeoutDuration, limitRefreshPeriod, limitForPeriod);
	}

	/**
	 * 超时时间，即超过这个时间没有获取到许可，则返回false，单位毫秒
	 */
	private final long timeoutDuration;
	/**
	 * 限制刷新周期，即每多少时间刷新一次，单位毫秒
	 */
	private final long limitRefreshPeriod;
	/**
	 * 每个周期的许可数
	 */
	private final int limitForPeriod;

	/**
	 * 构造
	 *
	 * @param timeoutDuration    超时时间，即超过这个时间没有获取到许可，则返回false
	 * @param limitRefreshPeriod 限制刷新周期，即每多少时间刷新一次
	 * @param limitForPeriod     个周期的许可数
	 */
	public RateLimiterConfig(final long timeoutDuration, final long limitRefreshPeriod, final int limitForPeriod) {
		this.timeoutDuration = timeoutDuration;
		this.limitRefreshPeriod = limitRefreshPeriod;
		this.limitForPeriod = limitForPeriod;
	}

	/**
	 * 超时时间，即超过这个时间没有获取到许可，则返回false，单位毫秒
	 *
	 * @return 超时时间，单位毫秒
	 */
	public long getTimeoutDuration() {
		return timeoutDuration;
	}

	/**
	 * 限制刷新周期，即每多少时间刷新一次，单位毫秒
	 *
	 * @return 限制刷新周期，单位毫秒
	 */
	public long getLimitRefreshPeriod() {
		return limitRefreshPeriod;
	}

	/**
	 * 每个周期的许可数
	 *
	 * @return 每个周期的许可数
	 */
	public int getLimitForPeriod() {
		return limitForPeriod;
	}
}
