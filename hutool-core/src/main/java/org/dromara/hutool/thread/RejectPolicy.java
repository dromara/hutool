/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.thread;

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
