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
