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

package org.dromara.hutool.core.thread.threadlocal;

/**
 * 带有Name标识的 {@link InheritableThreadLocal}，调用toString返回name
 *
 * @param <T> 值类型
 * @author looly
 * @since 4.1.4
 */
public class NamedInheritableThreadLocal<T> extends InheritableThreadLocal<T> {

	private final String name;

	/**
	 * 构造
	 *
	 * @param name 名字
	 */
	public NamedInheritableThreadLocal(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
