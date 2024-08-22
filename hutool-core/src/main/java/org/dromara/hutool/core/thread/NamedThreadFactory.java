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

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.dromara.hutool.core.text.StrUtil;

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
		if (!t.isDaemon()) {
			if (isDaemon) {
				// 原线程为非守护则设置为守护
				t.setDaemon(true);
			}
		} else if (!isDaemon) {
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
