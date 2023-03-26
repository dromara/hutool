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

package cn.hutool.core.thread;

import cn.hutool.core.util.RuntimeUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 简单单线程任务调度器<br>
 * 通过自定义Job定时执行任务，通过{@link #getResult()} 可以获取调取时的执行结果
 *
 * @param <T> 结果类型
 */
public class SimpleScheduler<T> {
	private final Job<T> job;

	/**
	 * 构造
	 *
	 * @param job    任务
	 * @param period 任务间隔，单位毫秒
	 */
	public SimpleScheduler(final Job<T> job, final long period) {
		this(job, 0, period, true);
	}

	/**
	 * 构造
	 *
	 * @param job                   任务
	 * @param initialDelay          初始延迟，单位毫秒
	 * @param period                执行周期，单位毫秒
	 * @param fixedRateOrFixedDelay {@code true}表示fixedRate模式，{@code false}表示fixedDelay模式
	 */
	public SimpleScheduler(final Job<T> job, final long initialDelay, final long period, final boolean fixedRateOrFixedDelay) {
		this.job = job;

		final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		// 启动定时任务
		ThreadUtil.schedule(scheduler, job, initialDelay, period, fixedRateOrFixedDelay);
		// 定时任务在程序结束时结束
		RuntimeUtil.addShutdownHook(scheduler::shutdown);
	}

	/**
	 * 获取执行任务的阶段性结果
	 *
	 * @return 结果
	 */
	public T getResult() {
		return this.job.getResult();
	}

	/**
	 * 带有结果计算的任务<br>
	 * 用户实现此接口，通过{@link #run()}实现定时任务的内容，定时任务每次执行或多次执行都可以产生一个结果<br>
	 * 这个结果存储在一个volatile的对象属性中，通过{@link #getResult()}来读取这一阶段的结果。
	 *
	 * @param <T> 结果类型
	 */
	public interface Job<T> extends Runnable {
		/**
		 * 获取执行结果
		 *
		 * @return 执行结果
		 */
		T getResult();
	}
}
