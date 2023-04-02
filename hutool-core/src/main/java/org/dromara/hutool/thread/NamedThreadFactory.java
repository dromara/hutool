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

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.dromara.hutool.text.StrUtil;

/**
 * 线程创建工厂类，此工厂可选配置：
 *
 * <pre>
 * 1. 自定义线程命名前缀
 * 2. 自定义是否守护线程
 * </pre>
 *
 * @author looly
 * @since 4.0.0
 */
public class NamedThreadFactory implements ThreadFactory {

	/** 命名前缀 */
	private final String prefix;
	/** 线程组 */
	private final ThreadGroup group;
	/** 线程组 */
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	/** 是否守护线程 */
	private final boolean isDaemon;
	/** 无法捕获的异常统一处理 */
	private final UncaughtExceptionHandler handler;

	/**
	 * 构造
	 *
	 * @param prefix 线程名前缀
	 * @param isDaemon 是否守护线程
	 */
	public NamedThreadFactory(final String prefix, final boolean isDaemon) {
		this(prefix, null, isDaemon);
	}

	/**
	 * 构造
	 *
	 * @param prefix 线程名前缀
	 * @param threadGroup 线程组，可以为null
	 * @param isDaemon 是否守护线程
	 */
	public NamedThreadFactory(final String prefix, final ThreadGroup threadGroup, final boolean isDaemon) {
		this(prefix, threadGroup, isDaemon, null);
	}

	/**
	 * 构造
	 *
	 * @param prefix 线程名前缀
	 * @param threadGroup 线程组，可以为null
	 * @param isDaemon 是否守护线程
	 * @param handler 未捕获异常处理
	 */
	public NamedThreadFactory(final String prefix, ThreadGroup threadGroup, final boolean isDaemon, final UncaughtExceptionHandler handler) {
		this.prefix = StrUtil.isBlank(prefix) ? "Hutool" : prefix;
		if (null == threadGroup) {
			threadGroup = ThreadUtil.currentThreadGroup();
		}
		this.group = threadGroup;
		this.isDaemon = isDaemon;
		this.handler = handler;
	}

	@Override
	public Thread newThread(final Runnable r) {
		final Thread t = new Thread(this.group, r, StrUtil.format("{}{}", prefix, threadNumber.getAndIncrement()));

		//守护线程
		if (false == t.isDaemon()) {
			if (isDaemon) {
				// 原线程为非守护则设置为守护
				t.setDaemon(true);
			}
		} else if (false == isDaemon) {
			// 原线程为守护则还原为非守护
			t.setDaemon(false);
		}
		//异常处理
		if(null != this.handler) {
			t.setUncaughtExceptionHandler(handler);
		}
		//优先级
		if (Thread.NORM_PRIORITY != t.getPriority()) {
			// 标准优先级
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}

}
