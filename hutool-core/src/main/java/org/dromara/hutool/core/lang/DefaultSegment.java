/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 片段默认实现
 *
 * @param <T> 数字类型，用于表示位置index
 * @author looly
 * @since 5.5.3
 */
public class DefaultSegment<T extends Number> implements Segment<T> {

	protected T beginIndex;
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
