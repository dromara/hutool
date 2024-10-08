/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.lang.event;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.loader.LazyFunLoader;
import org.dromara.hutool.core.lang.loader.Loader;
import org.dromara.hutool.core.thread.ThreadUtil;
import org.dromara.hutool.core.util.ObjUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 简单的事件发布者实现，基于{@link Subscriber}和{@link Event}实现
 *
 * @author looly
 * @since 6.0.0
 */
public class SimpleEventPublisher implements EventPublisher {

	/**
	 * 创建一个默认的{@code SimpleEventPublisher}，默认线程池为{@link ThreadUtil#newExecutor()}
	 *
	 * @return {@code SimpleEventPublisher}
	 */
	public static SimpleEventPublisher of() {
		return of(null);
	}

	/**
	 * 创建一个默认的{@code SimpleEventPublisher}，默认线程池为{@link ThreadUtil#newExecutor()}
	 *
	 * @param subscribers 订阅者列表，也可以传入空列表后调用{@link #register(Subscriber)}添加
	 * @return {@code SimpleEventPublisher}
	 */
	public static SimpleEventPublisher of(final List<Subscriber> subscribers) {
		return new SimpleEventPublisher(subscribers, null);
	}

	private final List<Subscriber> subscribers;
	private Loader<ExecutorService> executorServiceLoader;

	/**
	 * 构造
	 *
	 * @param subscribers           订阅者列表
	 * @param executorServiceLoader 线程池加载器，用于异步执行，默认为{@link ThreadUtil#newExecutor()}
	 */
	public SimpleEventPublisher(final List<Subscriber> subscribers, final Loader<ExecutorService> executorServiceLoader) {
		this.subscribers = ObjUtil.defaultIfNull(subscribers, ArrayList::new);
		this.executorServiceLoader = ObjUtil.defaultIfNull(executorServiceLoader, LazyFunLoader.of(ThreadUtil::newExecutor));
	}

	/**
	 * 设置自定义的{@link ExecutorService}线程池，默认为{@link ThreadUtil#newExecutor()}
	 *
	 * @param executorService {@link ExecutorService}，不能为空
	 * @return this
	 */
	public SimpleEventPublisher setExecutorService(final ExecutorService executorService) {
		this.executorServiceLoader = () -> Assert.notNull(executorService);
		return this;
	}

	@Override
	public EventPublisher register(final Subscriber subscriber) {
		subscribers.add(subscriber);
		Collections.sort(subscribers);
		return this;
	}

	@Override
	public void publish(final Event event) {
		for (final Subscriber subscriber : subscribers) {
			if (subscriber.async()) {
				executorServiceLoader.get().submit(() -> subscriber.update(event));
			} else {
				subscriber.update(event);
			}
		}
	}
}
