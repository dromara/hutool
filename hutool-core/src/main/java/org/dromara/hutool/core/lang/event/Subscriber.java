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

import org.dromara.hutool.core.comparator.CompareUtil;

import java.util.EventListener;

/**
 * 订阅者接口
 *
 * @author Looly
 */
public interface Subscriber extends EventListener, Comparable<Subscriber> {

	/**
	 * 当事件发生时的操作
	 *
	 * @param event 事件对象，根据不同事件，可选是否执行
	 */
	void update(Event event);

	/**
	 * 获取事件执行顺序，值越小越先执行
	 *
	 * @return 执行顺序
	 */
	default int order() {
		return 1000;
	}

	@Override
	default int compareTo(final Subscriber o) {
		return CompareUtil.compare(this.order(), o.order());
	}

	/**
	 * 是否异步执行，默认为false，同步执行
	 *
	 * @return 是否异步执行
	 */
	default boolean async() {
		return false;
	}
}
