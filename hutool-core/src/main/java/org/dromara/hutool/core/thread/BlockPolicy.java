/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.thread;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

/**
 * 当任务队列过长时处于阻塞状态，直到添加到队列中
 * 如果阻塞过程中被中断，就会抛出{@link InterruptedException}异常<br>
 * 有时候在线程池内访问第三方接口，只希望固定并发数去访问，并且不希望丢弃任务时使用此策略，队列满的时候会处于阻塞状态(例如刷库的场景)
 * <p>
 * 其他系统内置的拒绝策略，见hutool定义的枚举 {@link RejectPolicy} 线程拒绝策略枚举.
 *
 * @author luozongle
 * @since 5.8.0
 */
public class BlockPolicy implements RejectedExecutionHandler {

	/**
	 * 线程池关闭时，为避免任务丢失，留下处理方法
	 * 如果需要由调用方来运行，可以{@code new BlockPolicy(Runnable::run)}
	 */
	private final Consumer<Runnable> handlerwhenshutdown;

	/**
	 * 构造
	 */
	public BlockPolicy() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param handlerwhenshutdown 线程池关闭后的执行策略
	 */
	public BlockPolicy(final Consumer<Runnable> handlerwhenshutdown) {
		this.handlerwhenshutdown = handlerwhenshutdown;
	}

	@Override
	public void rejectedExecution(final Runnable r, final ThreadPoolExecutor e) {
		// 线程池未关闭时，阻塞等待
		if (!e.isShutdown()) {
			try {
				e.getQueue().put(r);
			} catch (final InterruptedException ex) {
				throw new RejectedExecutionException("Task " + r + " rejected from " + e);
			}
		} else if (null != handlerwhenshutdown) {
			// 当设置了关闭时候的处理
			handlerwhenshutdown.accept(r);
		}

		// 线程池关闭后，丢弃任务
	}
}
