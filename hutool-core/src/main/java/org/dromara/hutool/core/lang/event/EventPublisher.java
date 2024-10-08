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

/**
 * 事件发布者接口，用于发布事件
 *
 * @author Looly
 */
public interface EventPublisher {

	/**
	 * 注册订阅者，订阅者将接收到所有发布者发布的事件
	 *
	 * @param subscriber 订阅者
	 * @return this
	 */
	EventPublisher register(Subscriber subscriber);

	/**
	 * 发布事件，事件发布者将事件发布给所有订阅者
	 *
	 * @param event 事件对象
	 */
	void publish(Event event);
}
