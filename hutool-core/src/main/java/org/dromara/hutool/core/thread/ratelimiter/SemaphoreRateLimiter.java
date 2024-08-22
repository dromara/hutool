/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Opt;

import java.util.concurrent.Semaphore;

/**
 * 基于{@link Semaphore} 实现的限流器
 *
 * @author Ricky Fung
 * @author Looly
 * @since 6.0.0
 */
public abstract class SemaphoreRateLimiter implements RateLimiter {

	protected final RateLimiterConfig config;
	protected final Semaphore semaphore;

	// region ----- Constructor
	/**
	 * 构造
	 *
	 * @param config 限流配置
	 */
	public SemaphoreRateLimiter(final RateLimiterConfig config) {
		this(config, null);
	}

	/**
	 * 构造
	 *
	 * @param config    限流配置
	 * @param semaphore {@link Semaphore}，默认使用{@link RateLimiterConfig#getCapacity()}创建
	 */
	public SemaphoreRateLimiter(final RateLimiterConfig config, final Semaphore semaphore) {
		this.config = Assert.notNull(config);
		this.semaphore = Opt.ofNullable(semaphore).orElseGet(() -> new Semaphore(config.getCapacity()));
	}
	// endregion

	@Override
	public boolean tryAcquire(final int permits) {
		return semaphore.tryAcquire(permits);
	}

	@Override
	public void acquire(final int permits) {
		semaphore.acquireUninterruptibly(permits);
	}

	/**
	 * 刷新限制，用户可重写此方法，改变填充许可方式，如：
	 * <ul>
	 *     <li>填满窗口，一般用于固定窗口（Fixed Window）</li>
	 *     <li>固定频率填充，如每个周期只填充1个，配合{@link RateLimiterConfig#getRefreshPeriod()}，可实现令牌桶（Token Bucket）</li>
	 * </ul>
	 * 同样，用户可通过调用此方法手动刷新<br>
	 * 注意：重写此方法前需判断许可是否已满
	 *
	 * @see RateLimiterConfig#getCapacity()
	 */
	public abstract void refreshLimit();
}
