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

import java.util.EventObject;

/**
 * 基于事件源的事件实现
 *
 * @author looly
 * @since 6.0.0
 */
public class SourceEvent extends EventObject implements Event {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 *
	 * @param source 事件源
	 */
	public SourceEvent(final Object source) {
		super(source);
	}
}
