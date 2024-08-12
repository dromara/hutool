/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.json.serialize;

import org.dromara.hutool.core.lang.wrapper.Wrapper;

/**
 * {@code JSONString}接口定义了一个{@code toJSONString()}<br>
 * 实现此接口的类可以通过实现{@code toJSONString()}方法来改变转JSON字符串的方式。
 *
 * @author Looly
 *
 */
@FunctionalInterface
public interface JSONStringer extends Wrapper<Object> {

	/**
	 * 自定义转JSON字符串的方法
	 *
	 * @return JSON字符串
	 */
	String toJSONString();

	/**
	 * 获取原始的对象，默认为this
	 *
	 * @return 原始对象
	 */
	@Override
	default Object getRaw() {
		return this;
	}
}
