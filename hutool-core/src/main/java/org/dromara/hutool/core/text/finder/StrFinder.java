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

package org.dromara.hutool.core.text.finder;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.CharSequenceUtil;

/**
 * 字符串查找器
 *
 * @author looly
 * @since 5.7.14
 */
public class StrFinder extends TextFinder {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建查找器，构造后须调用{@link #setText(CharSequence)} 设置被查找的文本
	 *
	 * @param strToFind       查找的字符串
	 * @param caseInsensitive 是否忽略大小写
	 * @return {@code StrFinder}
	 */
	public static StrFinder of(final CharSequence strToFind, final boolean caseInsensitive) {
		return new StrFinder(strToFind, caseInsensitive);
	}

	private final CharSequence strToFind;
	private final boolean caseInsensitive;

	/**
	 * 构造
	 *
	 * @param strToFind       查找的字符串
	 * @param caseInsensitive 是否忽略大小写
	 */
	public StrFinder(final CharSequence strToFind, final boolean caseInsensitive) {
		Assert.notEmpty(strToFind);
		this.strToFind = strToFind;
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public int start(int from) {
		Assert.notNull(this.text, "Text to find must be not null!");
		final int subLen = strToFind.length();

		if (from < 0) {
			from = 0;
		}
		int endLimit = getValidEndIndex();
		if (negative) {
			for (int i = from; i > endLimit; i--) {
				if (CharSequenceUtil.isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
					return i;
				}
			}
		} else {
			endLimit = endLimit - subLen + 1;
			for (int i = from; i < endLimit; i++) {
				if (CharSequenceUtil.isSubEquals(text, i, strToFind, 0, subLen, caseInsensitive)) {
					return i;
				}
			}
		}

		return INDEX_NOT_FOUND;
	}

	@Override
	public int end(final int start) {
		if (start < 0) {
			return -1;
		}
		return start + strToFind.length();
	}
}
