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

import org.dromara.hutool.core.exception.HutoolException;

import java.io.Closeable;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 线程同步结束器<br>
 * 在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
 *
 * <pre>
 * ps:
 * //模拟1000个线程并发
 * SyncFinisher sf = new SyncFinisher(1000);
 * sf.addWorker(() -&gt; {
 *      // 需要并发测试的业务代码
 * });
 * sf.start()
 * </pre>
 *
 * @author Looly
 * @since 4.1.15
 */
public class SyncFinisher implements Closeable {

	private final Set<Worker> workers;
	private final int threadSize;
	private ExecutorService executorService;

	private boolean isBeginAtSameTime;
	/**
	 * 启动同步器，用于保证所有worker线程同时开始
	 */
	private final CountDownLatch beginLatch;
	/**
	 * 结束同步器，用于等待所有worker线程同时结束
	 */
	private CountDownLatch endLatch;

	/**
	 * 异常处理
	 */
	private Thread.UncaughtExceptionHandler exceptionHandler;

	/**
	 * 构造
	 *
	 * @param threadSize 线程数
	 */
	public SyncFinisher(final int threadSize) {
		this.beginLatch = new CountDownLatch(1);
		this.threadSize = threadSize;
		this.workers = new LinkedHashSet<>();
	}

	/**
	 * 设置是否所有worker线程同时开始
	 *
	 * @param isBeginAtSameTime 是否所有worker线程同时开始
	 * @return this
	 */
	public SyncFinisher setBeginAtSameTime(final boolean isBeginAtSameTime) {
		this.isBeginAtSameTime = isBeginAtSameTime;
		return this;
	}

	/**
	 * 设置异常处理
	 *
	 * @param exceptionHandler 异常处理器
	 * @return this
	 */
	public SyncFinisher setExceptionHandler(final Thread.UncaughtExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
		return this;
	}

	/**
	 * 增加定义的线程数同等数量的worker
	 *
	 * @param runnable 工作线程
	 * @return this
	 */
	@SuppressWarnings("resource")
	public SyncFinisher addRepeatWorker(final Runnable runnable) {
		for (int i = 0; i < this.threadSize; i++) {
			addWorker(new Worker() {
				@Override
				public void work() {
					runnable.run();
				}
			});
		}
		return this;
	}

	/**
	 * 增加工作线程
	 *
	 * @param runnable 工作线程
	 * @return this
	 */
	public SyncFinisher addWorker(final Runnable runnable) {
		return addWorker(new Worker() {
			@Override
			public void work() {
				runnable.run();
			}
		});
	}

	/**
	 * 增加工作线程
	 *
	 * @param worker 工作线程
	 * @return this
	 */
	synchronized public SyncFinisher addWorker(final Worker worker) {
		workers.add(worker);
		return this;
	}

	/**
	 * 开始工作<br>
	 * 执行此方法后如果不再重复使用此对象，需调用{@link #stop()}关闭回收资源。
	 */
	public void start() {
		start(true);
	}

	/**
	 * 开始工作<br>
	 * 执行此方法后如果不再重复使用此对象，需调用{@link #stop()}关闭回收资源。
	 *
	 * @param sync 是否阻塞等待
	 * @since 4.5.8
	 */
	public void start(final boolean sync) {
		endLatch = new CountDownLatch(workers.size());

		if (null == this.executorService || this.executorService.isShutdown()) {
			this.executorService = buildExecutor();
		}
		for (final Worker worker : workers) {
			executorService.execute(worker);
		}
		// 保证所有worker同时开始
		this.beginLatch.countDown();

		if (sync) {
			try {
				this.endLatch.await();
			} catch (final InterruptedException e) {
				throw new HutoolException(e);
			}
		}
	}

	/**
	 * 结束线程池。此方法执行两种情况：
	 * <ol>
	 *     <li>执行start(true)后，调用此方法结束线程池回收资源</li>
	 *     <li>执行start(false)后，用户自行判断结束点执行此方法</li>
	 * </ol>
	 *
	 * @since 5.6.6
	 */
	public void stop() {
		stop(false);
	}

	/**
	 * 结束线程池。此方法执行两种情况：
	 * <ol>
	 *     <li>执行start(true)后，调用此方法结束线程池回收资源</li>
	 *     <li>执行start(false)后，用户自行判断结束点执行此方法</li>
	 * </ol>
	 *
	 * @param isStopNow 是否立即结束所有线程（包括正在执行的）
	 * @since 5.6.6
	 */
	public void stop(final boolean isStopNow) {
		if (null != this.executorService) {
			if (isStopNow) {
				this.executorService.shutdownNow();
			} else {
				this.executorService.shutdown();
			}
			this.executorService = null;
		}

		clearWorker();
	}

	/**
	 * 清空工作线程对象
	 */
	public void clearWorker() {
		workers.clear();
	}

	/**
	 * 剩余任务数
	 *
	 * @return 剩余任务数
	 */
	public long count() {
		return endLatch.getCount();
	}

	@Override
	public void close() {
		stop();
	}

	/**
	 * 工作者，为一个线程
	 *
	 * @author Looly
	 */
	public abstract class Worker implements Runnable {

		@Override
		public void run() {
			if (isBeginAtSameTime) {
				try {
					beginLatch.await();
				} catch (final InterruptedException e) {
					throw new HutoolException(e);
				}
			}
			try {
				work();
			} finally {
				endLatch.countDown();
			}
		}

		/**
		 * 任务内容
		 */
		public abstract void work();
	}

	/**
	 * 构建线程池，加入了自定义的异常处理
	 *
	 * @return {@link ExecutorService}
	 */
	private ExecutorService buildExecutor() {
		return ExecutorBuilder.of()
			.setCorePoolSize(threadSize)
			.setThreadFactory(new NamedThreadFactory("hutool-", null, false, exceptionHandler))
			.build();
	}
}
