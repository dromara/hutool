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

package org.dromara.hutool.core.lang.mutable;

import java.io.Serializable;

/**
 * 可变 {@code boolean} 类型
 *
 * @see Boolean
 * @since 3.0.1
 */
public class MutableBool implements Comparable<MutableBool>, Mutable<Boolean>, Serializable {
	private static final long serialVersionUID = 1L;

	private boolean value;

	/**
	 * 构造，默认值0
	 */
	public MutableBool() {
	}

	/**
	 * 构造
	 * @param value 值
	 */
	public MutableBool(final boolean value) {
		this.value = value;
	}

	/**
	 * 构造
	 * @param value String值
	 * @throws NumberFormatException 转为Boolean错误
	 */
	public MutableBool(final String value) throws NumberFormatException {
		this.value = Boolean.parseBoolean(value);
	}

	@Override
	public Boolean get() {
		return this.value;
	}

	/**
	 * 设置值
	 * @param value 值
	 */
	public void set(final boolean value) {
		this.value = value;
	}

	@Override
	public void set(final Boolean value) {
		this.value = value;
	}

	// -----------------------------------------------------------------------
	/**
	 * 相等需同时满足如下条件：
	 * <ol>
	 * 	<li>非空</li>
	 * 	<li>类型为 MutableBool</li>
	 * 	<li>值相等</li>
	 * </ol>
	 *
	 * @param obj 比对的对象
	 * @return 相同返回<code>true</code>，否则 {@code false}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MutableBool) {
			return value == ((MutableBool) obj).value;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
	}

	// -----------------------------------------------------------------------
	/**
	 * 比较
	 *
	 * @param other 其它 MutableBool 对象
	 * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
	 */
	@Override
	public int compareTo(final MutableBool other) {
		return Boolean.compare(this.value, other.value);
	}

	// -----------------------------------------------------------------------
	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
