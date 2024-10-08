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

/**
 * 发布订阅模式封装，发布/订阅是一种消息范式<br>
 * 消息的发送者（EventPublisher）将事件或消息（Event）广播出去，订阅者（Subscriber）接收到消息后处理。<br>
 * Hutool中的事件或消息（Event）是一个无方法接口，可以通过实现此接口灵活的定义不同消息类型。<br>
 * 消息的发送者（EventPublisher）可以通过register方法注册订阅者，调用publish发布消息
 *
 * <pre>{@code
 *           publish                /----->Subscriber
 *   Event  -------- EventPublisher- ----->Subscriber
 *                                  \----->Subscriber
 * }</pre>
 *
 * @author Looly
 */
package org.dromara.hutool.core.lang.event;
