/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
		this.replacement = StrUtil.emptyIfNull(replacement);
		this.ignoreCase = ignoreCase;
	}

	@Override
	public String apply(final CharSequence str) {
		if (StrUtil.isEmpty(str)) {
			return StrUtil.str(str);
		}

		final int strLength = str.length();
		if (strLength < this.searchStrLength) {
			// issue#I4M16G@Gitee
			return StrUtil.str(str);
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
