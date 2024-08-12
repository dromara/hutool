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

package org.dromara.hutool.core.text.replacer;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 区间字符串替换，指定区间，将区间中的所有字符去除，替换为指定的字符，字符重复次数为区间长度，即替换后字符串长度不变<br>
 * 此方法使用{@link String#codePoints()}完成拆分替换
 *
 * @author Looly
 */
public class RangeReplacerByChar extends StrReplacer {
	private static final long serialVersionUID = 1L;

	private final int beginInclude;
	private final int endExclude;
	private final char replacedChar;
	private final boolean isCodePoint;

	/**
	 * 构造
	 *
	 * @param beginInclude 开始位置（包含）
	 * @param endExclude   结束位置（不包含）
	 * @param replacedChar 被替换的字符串
	 * @param isCodePoint  是否code point模式，此模式下emoji等会被作为单独的字符
	 */
	public RangeReplacerByChar(final int beginInclude, final int endExclude, final char replacedChar, final boolean isCodePoint) {
		this.beginInclude = beginInclude;
		this.endExclude = endExclude;
		this.replacedChar = replacedChar;
		this.isCodePoint = isCodePoint;
	}

	@Override
	public String apply(final CharSequence str) {
		if (StrUtil.isEmpty(str)) {
			return StrUtil.toStringOrNull(str);
		}

		final String originalStr = str.toString();
		final int[] chars = StrUtil.toChars(originalStr, this.isCodePoint);
		final int strLength = chars.length;

		final int beginInclude = this.beginInclude;
		if (beginInclude > strLength) {
			return originalStr;
		}
		int endExclude = this.endExclude;
		if (endExclude > strLength) {
			endExclude = strLength;
		}
		if (beginInclude > endExclude) {
			// 如果起始位置大于结束位置，不替换
			return originalStr;
		}

		// 新字符串长度不变
		final StringBuilder stringBuilder = new StringBuilder(originalStr.length());
		for (int i = 0; i < strLength; i++) {
			if (i >= beginInclude && i < endExclude) {
				// 区间内的字符全部替换
				replace(originalStr, i, stringBuilder);
			} else {
				// 其它字符保留
				append(stringBuilder, chars[i]);
			}
		}
		return stringBuilder.toString();
	}

	@Override
	protected int replace(final CharSequence str, final int pos, final StringBuilder out) {
		out.appendCodePoint(replacedChar);
		return pos;
	}

	/**
	 * 追加字符
	 *
	 * @param stringBuilder {@link StringBuilder}
	 * @param c             字符
	 */
	private void append(final StringBuilder stringBuilder, final int c) {
		if (isCodePoint) {
			stringBuilder.appendCodePoint(c);
		} else {
			stringBuilder.append((char) c);
		}
	}
}
