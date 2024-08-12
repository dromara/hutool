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

package org.dromara.hutool.core.lang.range;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 片段默认实现
 *
 * @param <T> 数字类型，用于表示位置index
 * @author looly
 * @since 5.5.3
 */
public class DefaultSegment<T extends Number> implements Segment<T> {

	/**
	 * 起始位置
	 */
	protected T beginIndex;
	/**
	 * 结束位置
	 */
	protected T endIndex;

	/**
	 * 构造
	 *
	 * @param beginIndex 起始位置
	 * @param endIndex   结束位置
	 */
	public DefaultSegment(final T beginIndex, final T endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	@Override
	public T getBeginIndex() {
		return this.beginIndex;
	}

	@Override
	public T getEndIndex() {
		return this.endIndex;
	}

	@Override
	public String toString() {
		return StrUtil.format("[{}, {}]", beginIndex, endIndex);
	}
}
