/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程拒绝策略枚举
 *
 * <p>
 * 如果设置了maxSize, 当总线程数达到上限, 会调用RejectedExecutionHandler进行处理，此枚举为JDK预定义的几种策略枚举表示
 *
 * @author looly
 * @since 4.1.13
 */
public enum RejectPolicy {

	/** 处理程序遭到拒绝将抛出RejectedExecutionException */
	ABORT(new ThreadPoolExecutor.AbortPolicy()),
	/** 放弃当前任务 */
	DISCARD(new ThreadPoolExecutor.DiscardPolicy()),
	/** 如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程） */
	DISCARD_OLDEST(new ThreadPoolExecutor.DiscardOldestPolicy()),
	/** 调用者线程来执行被丢弃的任务；一般可能是由主线程来直接执行 */
	CALLER_RUNS(new ThreadPoolExecutor.CallerRunsPolicy()),
	/** 当任务队列过长时处于阻塞状态，直到添加到队列中，固定并发数去访问，并且不希望丢弃任务时使用此策略 */
	BLOCK(new BlockPolicy());

	private final RejectedExecutionHandler value;

	RejectPolicy(final RejectedExecutionHandler handler) {
		this.value = handler;
	}

	/**
	 * 获取RejectedExecutionHandler枚举值
	 *
	 * @return RejectedExecutionHandler
	 */
	public RejectedExecutionHandler getValue() {
		return this.value;
	}
}
