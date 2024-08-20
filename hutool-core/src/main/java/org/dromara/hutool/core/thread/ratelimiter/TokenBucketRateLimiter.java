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

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * 令牌桶（Token Bucket）限流器<br>
 * 令牌桶算法能够在限制数据的平均传输速率的同时还允许某种程度的突发传输<br>
 * 概念见：https://zhuanlan.zhihu.com/p/110596981<br>
 * 此限流器通过{@link #refreshLimit()}方法配合{@link RateLimiterConfig#getRefreshPeriod()} 实现按照固定速率填充令牌到桶中。
 *
 * @author looly
 * @since 6.0.0
 */
public class TokenBucketRateLimiter extends SemaphoreRateLimiter {

	/**
	 * 构造
	 *
	 * @param config 配置
	 */
	public TokenBucketRateLimiter(final RateLimiterConfig config) {
		super(config, null, configureScheduler());
	}

	@Override
	public void refreshLimit() {
		if (this.config.getCapacity() - semaphore.availablePermits() > 0) {
			// 只有在周期内不满时，才填充
			// 令牌桶的填充主要依靠刷新周期调整令牌填充速度，每次只填充1个令牌
			semaphore.release(1);
		}
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
}
