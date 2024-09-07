/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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
import java.util.function.UnaryOperator;

/**
 * 字符串裁剪器，用于裁剪字符串前后缀<br>
 * 强调去除两边或某一边的<b>指定字符串</b>，如果一边不存在，另一边不影响去除
 *
 * @author Looly
 * @since 5.8.0
 */
public class StrStripper implements UnaryOperator<CharSequence>, Serializable {
	private static final long serialVersionUID = 1L;

	private final CharSequence prefix;
	private final CharSequence suffix;
	private final boolean ignoreCase;
	private final boolean stripAll;

	/**
	 * 构造
	 *
	 * @param prefix     前缀，{@code null}忽略
	 * @param suffix     后缀，{@code null}忽略
	 * @param ignoreCase 是否忽略大小写
	 * @param stripAll   是否去除全部
	 */
	public StrStripper(final CharSequence prefix, final CharSequence suffix, final boolean ignoreCase, final boolean stripAll) {
		this.prefix = prefix;
		this.suffix = suffix;
		this.ignoreCase = ignoreCase;
		this.stripAll = stripAll;
	}

	@Override
	public String apply(final CharSequence charSequence) {
		return this.stripAll ? stripAll(charSequence) : stripOnce(charSequence);
	}

	/**
	 * 去除两边的指定字符串<br>
	 * 两边字符如果存在，则去除，不存在不做处理
	 * <pre>{@code
	 * "aaa_STRIPPED_bbb", "a", "b"  -> "aa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", null, null  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", ""  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", "b"  -> "aaa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", null, "b"  -> "aaa_STRIPPED_bb"
	 * "aaa_STRIPPED_bbb", "a", ""  -> "aa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "a", null  -> "aa_STRIPPED_bbb"
	 *
	 * "a", "a", "a"  -> ""
	 * }</pre>
	 *
	 * @param charSequence 被处理的字符串
	 * @return 处理后的字符串
	 */
	private String stripOnce(final CharSequence charSequence) {
		if (StrUtil.isEmpty(charSequence)) {
			return StrUtil.toStringOrNull(charSequence);
		}

		final String str = charSequence.toString();
		int from = 0;
		int to = str.length();

		if (StrUtil.isNotEmpty(this.prefix) && startWith(str, this.prefix, 0)) {
			from = this.prefix.length();
			if (from == to) {
				// "a", "a", "a"  -> ""
				return StrUtil.EMPTY;
			}
		}
		if (endWithSuffix(str)) {
			to -= this.suffix.length();
			if (from == to) {
				// "a", "a", "a"  -> ""
				return StrUtil.EMPTY;
			} else if (to < from) {
				// pre去除后和suffix有重叠，如 ("aba", "ab", "ba") -> "a"
				to += this.suffix.length();
			}
		}

		return str.substring(from, to);
	}

	/**
	 * 去除两边<u><b>所有</b></u>的指定字符串
	 *
	 * <pre>{@code
	 * "aaa_STRIPPED_bbb", "a", "b"  -> "_STRIPPED_"
	 * "aaa_STRIPPED_bbb", null, null  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", ""  -> "aaa_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "", "b"  -> "aaa_STRIPPED_"
	 * "aaa_STRIPPED_bbb", null, "b"  -> "aaa_STRIPPED_"
	 * "aaa_STRIPPED_bbb", "a", ""  -> "_STRIPPED_bbb"
	 * "aaa_STRIPPED_bbb", "a", null  -> "_STRIPPED_bbb"
	 *
	 * // special test
	 * "aaaaaabbb", "aaa", null  -> "bbb"
	 * "aaaaaaabbb", "aa", null  -> "abbb"
	 *
	 * "aaaaaaaaa", "aaa", "aa"  -> ""
	 * "a", "a", "a"  -> ""
	 * }</pre>
	 *
	 * @param charSequence 被处理的字符串
	 * @return 处理后的字符串
	 * @since 5.8.30
	 */
	private String stripAll(final CharSequence charSequence) {
		if (StrUtil.isEmpty(charSequence)) {
			return StrUtil.toStringOrNull(charSequence);
		}

		final String str = charSequence.toString();
		int from = 0;
		int to = str.length();

		if (StrUtil.isNotEmpty(this.prefix)) {
			while (startWith(str, this.prefix, from)) {
				from += this.prefix.length();
				if (from == to) {
					// "a", "a", "a"  -> ""
					return StrUtil.EMPTY;
				}
			}
		}
		if (StrUtil.isNotEmpty(suffix)) {
			final int suffixLength = this.suffix.length();
			while (startWith(str, suffix, to - suffixLength)) {
				to -= suffixLength;
				if (from == to) {
					// "a", "a", "a"  -> ""
					return StrUtil.EMPTY;
				} else if (to < from) {
					// pre去除后和suffix有重叠，如 ("aba", "ab", "ba") -> "a"
					to += suffixLength;
					break;
				}
			}
		}

		return str.substring(from, to);
	}

	/**
	 * 判断是否以指定前缀开头
	 *
	 * @param charSequence 被检查的字符串
	 * @param from         开始位置
	 * @return 是否以指定前缀开头
	 */
	private boolean startWith(final CharSequence charSequence, final CharSequence strToCheck, final int from) {
		return new StrRegionMatcher(this.ignoreCase, false, from)
			.test(charSequence, strToCheck);
	}

	/**
	 * 判断是否以指定后缀结尾
	 *
	 * @param charSequence 被检查的字符串
	 * @return 是否以指定后缀结尾
	 */
	private boolean endWithSuffix(final CharSequence charSequence) {
		return StrUtil.isNotEmpty(suffix) && StrUtil.endWith(charSequence, suffix, ignoreCase);
	}
}
