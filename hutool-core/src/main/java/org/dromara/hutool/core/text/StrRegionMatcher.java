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

package org.dromara.hutool.core.text;

import java.io.Serializable;
import java.util.function.BiPredicate;

/**
 * 字符串区域匹配器，用于匹配字串是头部匹配还是尾部匹配，亦或者是某个位置的匹配。<br>
 * offset用于锚定开始或结束位置，正数表示从开始偏移，负数表示从后偏移
 * <pre>
 *     a  b  c  d  e  f
 *     |  |        |  |
 *     0  1  c  d -2 -1
 * </pre>
 * <p>
 * 例如以下匹配都为{@code true}：
 * <pre>
 *     offset    str     strToCheck
 *       0      abcdef       ab
 *       1      abcdef       bc
 *      -1      abcdef       ef
 *      -2      abcdef       de
 * </pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class StrRegionMatcher implements BiPredicate<CharSequence, CharSequence>, Serializable {
	private static final long serialVersionUID = 1L;

	private final boolean ignoreCase;
	private final boolean ignoreEquals;
	/**
	 * 匹配位置，正数表示从开始偏移，负数表示从后偏移
	 */
	private final int offset;

	/**
	 * 构造
	 *
	 * @param ignoreCase   是否忽略大小写
	 * @param ignoreEquals 是否忽略字符串相等的情况
	 * @param isPrefix     {@code true}表示检查开头匹配，{@code false}检查末尾匹配
	 */
	public StrRegionMatcher(final boolean ignoreCase, final boolean ignoreEquals, final boolean isPrefix) {
		this(ignoreCase, ignoreEquals, isPrefix ? 0 : -1);
	}

	/**
	 * 构造
	 *
	 * @param ignoreCase   是否忽略大小写
	 * @param ignoreEquals 是否忽略字符串相等的情况
	 * @param offset       匹配位置，正数表示从开始偏移，负数表示从后偏移
	 */
	public StrRegionMatcher(final boolean ignoreCase, final boolean ignoreEquals, final int offset) {
		this.ignoreCase = ignoreCase;
		this.ignoreEquals = ignoreEquals;
		this.offset = offset;
	}

	@Override
	public boolean test(final CharSequence str, final CharSequence strToCheck) {
		if (null == str || null == strToCheck) {
			if (ignoreEquals) {
				return false;
			}
			return null == str && null == strToCheck;
		}

		final int strToCheckLength = strToCheck.length();
		final int toffset = this.offset >= 0 ?
				this.offset : str.length() - strToCheckLength + this.offset + 1;
		final boolean matches = str.toString()
				.regionMatches(ignoreCase, toffset, strToCheck.toString(), 0, strToCheckLength);

		if (matches) {
			return (!ignoreEquals) || (!StrUtil.equals(str, strToCheck, ignoreCase));
		}
		return false;
	}
}
