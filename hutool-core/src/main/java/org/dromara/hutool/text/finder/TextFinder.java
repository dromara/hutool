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

package org.dromara.hutool.text.finder;

import org.dromara.hutool.lang.Assert;

import java.io.Serializable;

/**
 * 文本查找抽象类
 *
 * @author looly
 * @since 5.7.14
 */
public abstract class TextFinder implements Finder, Serializable {
	private static final long serialVersionUID = 1L;

	protected CharSequence text;
	protected int endIndex = -1;
	protected boolean negative;

	/**
	 * 设置被查找的文本
	 *
	 * @param text 文本
	 * @return this
	 */
	public TextFinder setText(final CharSequence text) {
		this.text = Assert.notNull(text, "Text must be not null!");
		return this;
	}

	/**
	 * 设置查找的结束位置<br>
	 * 如果从前向后查找，结束位置最大为text.length()<br>
	 * 如果从后向前，结束位置为-1
	 *
	 * @param endIndex 结束位置（不包括）
	 * @return this
	 */
	public TextFinder setEndIndex(final int endIndex) {
		this.endIndex = endIndex;
		return this;
	}

	/**
	 * 设置是否反向查找，{@code true}表示从后向前查找
	 *
	 * @param negative 结束位置（不包括）
	 * @return this
	 */
	public TextFinder setNegative(final boolean negative) {
		this.negative = negative;
		return this;
	}

	/**
	 * 获取有效结束位置<br>
	 * 如果{@link #endIndex}小于0，在反向模式下是开头（-1），正向模式是结尾（text.length()）
	 *
	 * @return 有效结束位置
	 */
	protected int getValidEndIndex() {
		if(negative && -1 == endIndex){
			// 反向查找模式下，-1表示0前面的位置，即字符串反向末尾的位置
			return -1;
		}
		final int limit;
		if (endIndex < 0) {
			limit = endIndex + text.length() + 1;
		} else {
			limit = Math.min(endIndex, text.length());
		}
		return limit;
	}
}
