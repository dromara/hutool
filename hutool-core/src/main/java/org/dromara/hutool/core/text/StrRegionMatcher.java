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
			return (! ignoreEquals) || (! StrUtil.equals(str, strToCheck, ignoreCase));
		}
		return false;
	}
}
