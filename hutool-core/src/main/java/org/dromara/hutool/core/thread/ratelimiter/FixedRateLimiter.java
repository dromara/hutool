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
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

/**
 * 基于{@link Semaphore} 实现的限流器<br>
 * 此算法实现了：固定窗口(Fixed Window)计数法，即设置固定窗口<br>
 * 窗口时间为{@link RateLimiterConfig#getRefreshPeriod()}，每次窗口内请求数不超过{@link RateLimiterConfig#getCapacity()}<br>
 * 在窗口期允许的请求数是固定的，请求结束后拒绝访问，直到下一个窗口开始则重新开始计数。<br>
 * 参考：https://github.com/TFdream/juice/blob/master/juice-ratelimiter/src/main/java/juice/ratelimiter/internal/SemaphoreBasedRateLimiter.java
 *
 * <ul>
 *     <li>优点：内存占用小，实现简单</li>
 *     <li>缺点：不够平滑，在窗口期开始时可能请求暴增，窗口结束时大量请求丢失，即“突刺现象”。</li>
 * </ul>
 *
 * @author Ricky Fung
 * @author Looly
 * @since 6.0.0
 */
public class FixedRateLimiter extends SemaphoreRateLimiter {

	/**
	 * 构造
	 *
	 * @param config 配置
	 */
	public FixedRateLimiter(final RateLimiterConfig config) {
		super(config, null, configureScheduler());
	}

	/**
	 * 刷新限制，填满许可数为{@link RateLimiterConfig#getCapacity()}<br>
	 * 用户可手动调用此方法填满许可<br>
	 *
	 * @see RateLimiterConfig#getCapacity()
	 */
	@Override
	public void refreshLimit() {
		final int permitsToFill = this.config.getCapacity() - semaphore.availablePermits();
		if (permitsToFill > 0) {
			// 只有在周期内不满时，才填充
			semaphore.release(permitsToFill);
		}
	}

	/**
	 * 创建定时器
	 *
	 * @return 定时器
	 */
	private static ScheduledExecutorService configureScheduler() {
		final ThreadFactory threadFactory = new NamedThreadFactory("FixedRateLimiterScheduler-", true);
		return new ScheduledThreadPoolExecutor(1, threadFactory);
	}
}
