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

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.thread.NamedThreadFactory;

import java.util.concurrent.*;

/**
 * 基于{@link Semaphore} 实现的限流器<br>
 * 此算法实现了：固定窗口(Fixed Window)计数法，即设置固定窗口<br>
 * 窗口时间为{@link RateLimiterConfig#getLimitRefreshPeriod()}，每次窗口内请求数不超过{@link RateLimiterConfig#getLimitForPeriod()}<br>
 * 在窗口期允许的请求数是固定的，请求结束后拒绝访问，直到下一个窗口开始则重新开始计数。<br>
 * 参考：https://github.com/TFdream/juice/blob/master/juice-ratelimiter/src/main/java/juice/ratelimiter/internal/SemaphoreBasedRateLimiter.java
 *
 * <ul>
 *     <li>优点：内存占用小，实现简单</li>
 *     <li>缺点：不够平滑，在窗口期开始时可能请求暴增，窗口结束时大量请求丢失，即“突刺现象”。</li>
 * </ul>
 *
 * @author Ricky Fung
 * @since 6.0.0
 */
public class SemaphoreRateLimiter implements RateLimiter {

	private final RateLimiterConfig config;
	private final ScheduledExecutorService scheduler;
	private final Semaphore semaphore;

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
	 * @param semaphore {@link Semaphore}
	 */
	public SemaphoreRateLimiter(final RateLimiterConfig config, final Semaphore semaphore) {
		this(config, semaphore, null);
	}

	/**
	 * 构造
	 *
	 * @param config    限流配置
	 * @param semaphore {@link Semaphore}
	 * @param scheduler 定时器
	 */
	public SemaphoreRateLimiter(final RateLimiterConfig config, final Semaphore semaphore, final ScheduledExecutorService scheduler) {
		this.config = Assert.notNull(config);
		this.semaphore = Opt.ofNullable(semaphore).orElseGet(() -> new Semaphore(config.getLimitForPeriod()));
		this.scheduler = Opt.ofNullable(scheduler).orElseGet(this::configureScheduler);
		//启动定时器
		scheduleLimitRefresh();
	}

	@Override
	public boolean tryAcquire(final int permits) {
		return semaphore.tryAcquire(permits);
	}

	@Override
	public void acquire(final int permits) {
		semaphore.acquireUninterruptibly(permits);
	}

	/**
	 * 刷新限制，填满许可数为{@link RateLimiterConfig#getLimitForPeriod()}<br>
	 * 用户可手动调用此方法填满许可
	 *
	 * @see RateLimiterConfig#getLimitForPeriod()
	 */
	public void refreshLimit() {
		semaphore.release(this.config.getLimitForPeriod() - semaphore.availablePermits());
	}

	/**
	 * 创建定时器
	 *
	 * @return 定时器
	 */
	private ScheduledExecutorService configureScheduler() {
		final ThreadFactory threadFactory = new NamedThreadFactory("SemaphoreRateLimiterScheduler-", true);
		return new ScheduledThreadPoolExecutor(1, threadFactory);
	}

	/**
	 * 启动定时器
	 */
	private void scheduleLimitRefresh() {
		final long limitRefreshPeriod = this.config.getLimitRefreshPeriod().toNanos();
		scheduler.scheduleAtFixedRate(
			this::refreshLimit,
			limitRefreshPeriod,
			limitRefreshPeriod,
			TimeUnit.NANOSECONDS
		);
	}
}
