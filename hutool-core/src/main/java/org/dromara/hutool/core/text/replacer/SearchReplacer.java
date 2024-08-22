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

package org.dromara.hutool.core.text.replacer;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.finder.Finder;

/**
 * 查找替换器<br>
 * 查找给定的字符串，并全部替换为新的字符串，其它字符不变
 *
 * @author looly
 * @since 6.0.0
 */
public class SearchReplacer extends StrReplacer {
	private static final long serialVersionUID = 1L;

	private static final int INDEX_NOT_FOUND = Finder.INDEX_NOT_FOUND;

	private final int fromIndex;
	private final CharSequence searchStr;
	private final int searchStrLength;
	private final CharSequence replacement;
	private final boolean ignoreCase;

	/**
	 * 构造
	 *
	 * @param fromIndex   开始位置（包括）
	 * @param searchStr   被查找的字符串
	 * @param replacement 被替换的字符串
	 * @param ignoreCase  是否忽略大小写
	 */
	public SearchReplacer(final int fromIndex, final CharSequence searchStr, final CharSequence replacement, final boolean ignoreCase) {
		this.fromIndex = Math.max(fromIndex, 0);
		this.searchStr = Assert.notEmpty(searchStr, "'searchStr' must be not empty!");
		this.searchStrLength = searchStr.length();
		this.replacement = StrUtil.toStringOrEmpty(replacement);
		this.ignoreCase = ignoreCase;
	}

	@Override
	public String apply(final CharSequence str) {
		if (StrUtil.isEmpty(str)) {
			return StrUtil.toStringOrNull(str);
		}

		final int strLength = str.length();
		if (strLength < this.searchStrLength) {
			// issue#I4M16G@Gitee
			return StrUtil.toStringOrNull(str);
		}

		final int fromIndex = this.fromIndex;
		if (fromIndex > strLength) {
			// 越界截断
			return StrUtil.EMPTY;
		}

		final StringBuilder result = new StringBuilder(
				strLength - this.searchStrLength + this.replacement.length());
		if (0 != fromIndex) {
			// 开始部分
			result.append(str.subSequence(0, fromIndex));
		}

		// 替换部分
		int pos = fromIndex;
		int consumed;//处理过的字符数
		while ((consumed = replace(str, pos, result)) > 0) {
			pos += consumed;
		}

		if (pos < strLength) {
			// 结尾部分
			result.append(str.subSequence(pos, strLength));
		}
		return result.toString();
	}

	@Override
	protected int replace(final CharSequence str, final int pos, final StringBuilder out) {
		final int index = StrUtil.indexOf(str, this.searchStr, pos, this.ignoreCase);
		if (index > INDEX_NOT_FOUND) {
			// 无需替换的部分
			out.append(str.subSequence(pos, index));
			// 替换的部分
			out.append(replacement);

			//已经处理的长度 = 无需替换的长度（查找字符串位置 - 开始的位置） + 替换的长度
			return index - pos + searchStrLength;
		}

		// 未找到
		return INDEX_NOT_FOUND;
	}
}
