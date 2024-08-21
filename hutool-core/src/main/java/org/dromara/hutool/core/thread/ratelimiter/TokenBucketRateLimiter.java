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

import org.dromara.hutool.core.thread.NamedThreadFactory;

import java.io.Closeable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶（Token Bucket）限流器<br>
 * 令牌桶算法能够在限制数据的平均传输速率的同时还允许某种程度的突发传输，概念见：https://zhuanlan.zhihu.com/p/110596981
 *
 * <p>
 * 令牌发放：通过scheduler定时器，定时向令牌桶中添加令牌，直到令牌桶满。<br>
 * 令牌发放周期为{@link RateLimiterConfig#getRefreshPeriod()}，周期内发放个数为{@link RateLimiterConfig#getMaxReleaseCount()}
 * </p>
 *
 * <p>
 * 令牌请求：通过{@link #tryAcquire(int)} 方法请求令牌，如果令牌桶中数量不足，则返回false，表示请求失败。
 * </p>
 *
 * @author looly
 * @since 6.0.0
 */
public class TokenBucketRateLimiter extends SemaphoreRateLimiter implements Closeable {

	protected final ScheduledExecutorService scheduler;

	/**
	 * 构造
	 *
	 * @param config 配置
	 */
	public TokenBucketRateLimiter(final RateLimiterConfig config) {
		super(config, null);
		this.scheduler = configureScheduler();
		//启动定时器
		scheduleLimitRefresh();
	}

	@Override
	public void refreshLimit() {
		final int permitsToFill = this.config.getCapacity() - semaphore.availablePermits();
		if (permitsToFill > 0) {
			// 只有在周期内不满时，才填充
			semaphore.release(Math.min(permitsToFill, config.getMaxReleaseCount()));
		}
	}

	@Override
	public void close() {
		scheduler.shutdown();
	}

	/**
	 * 创建定时器
	 *
	 * @return 定时器
	 */
	private static ScheduledExecutorService configureScheduler() {
		final ThreadFactory threadFactory = new NamedThreadFactory("TokenBucketLimiterScheduler-", true);
		return new ScheduledThreadPoolExecutor(1, threadFactory);
	}

	/**
	 * 启动定时器，未定义则不启动
	 */
	private void scheduleLimitRefresh() {
		if (null == this.scheduler) {
			return;
		}
		final long limitRefreshPeriod = this.config.getRefreshPeriod().toNanos();
		scheduler.scheduleAtFixedRate(
			this::refreshLimit,
			limitRefreshPeriod,
			limitRefreshPeriod,
			TimeUnit.NANOSECONDS
		);
	}
}
