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
 * 限流接口<br>
 * 通过实现此接口以实现不同的限流策略，如令牌桶、分布式限流等
 *
 * @author Looly
 * @since 6.0.0
 */
public interface RateLimiter {

	/**
	 * 尝试获取许可，非阻塞方法
	 *
	 * @return {@code true}表示成功获取，{@code false}表示无足够的许可可获取，此时需要等待给定的时间
	 */
	default boolean tryAcquire() {
		return tryAcquire(1);
	}

	/**
	 * 尝试获取许可，非阻塞方法
	 *
	 * @param permits 需要的许可数
	 * @return {@code true}表示成功获取，{@code false}表示无足够的许可可获取，此时需要等待给定的时间
	 */
	boolean tryAcquire(int permits);

	/**
	 * 获取许可，阻塞方法，如果没有足够的许可，则阻塞等待
	 */
	default void acquire() {
		acquire(1);
	}

	/**
	 * 获取许可，阻塞方法，如果没有足够的许可，则阻塞等待
	 *
	 * @param permits 需要的许可数
	 */
	void acquire(int permits);
}
