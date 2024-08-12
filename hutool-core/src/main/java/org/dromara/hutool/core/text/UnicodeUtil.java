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

package org.dromara.hutool.core.text;

import org.dromara.hutool.core.codec.binary.HexUtil;

/**
 * 提供Unicode字符串和普通字符串之间的转换
 *
 * @author 兜兜毛毛, looly
 * @since 4.0.0
 */
public class UnicodeUtil {

	/**
	 * Unicode字符串转为普通字符串<br>
	 * Unicode字符串的表现方式为：\\uXXXX
	 *
	 * @param unicode Unicode字符串
	 * @return 普通字符串
	 */
	public static String toString(final String unicode) {
		if (StrUtil.isBlank(unicode)) {
			return unicode;
		}

		final int len = unicode.length();
		final StringBuilder sb = new StringBuilder(len);
		int i;
		int pos = 0;
		while ((i = StrUtil.indexOfIgnoreCase(unicode, "\\u", pos)) != -1) {
			sb.append(unicode, pos, i);//写入Unicode符之前的部分
			pos = i;
			if (i + 5 < len) {
				final char c;
				try {
					c = (char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16);
					sb.append(c);
					pos = i + 6;//跳过整个Unicode符
				} catch (final NumberFormatException e) {
					//非法Unicode符，跳过
					sb.append(unicode, pos, i + 2);//写入"\\u"
					pos = i + 2;
				}
			} else {
				//非Unicode符，结束
				break;
			}
		}

		if (pos < len) {
			sb.append(unicode, pos, len);
		}
		return sb.toString();
	}

	/**
	 * 字符编码为Unicode形式
	 *
	 * @param c 被编码的字符
	 * @return Unicode字符串
	 * @since 5.6.2
	 * @see HexUtil#toUnicodeHex(char)
	 */
	public static String toUnicode(final char c) {
		return HexUtil.toUnicodeHex(c);
	}

	/**
	 * 字符编码为Unicode形式
	 *
	 * @param c 被编码的字符
	 * @return Unicode字符串
	 * @since 5.6.2
	 * @see HexUtil#toUnicodeHex(int)
	 */
	public static String toUnicode(final int c) {
		return HexUtil.toUnicodeHex(c);
	}

	/**
	 * 字符串编码为Unicode形式
	 *
	 * @param str 被编码的字符串
	 * @return Unicode字符串
	 */
	public static String toUnicode(final String str) {
		return toUnicode(str, true);
	}

	/**
	 * 字符串编码为Unicode形式
	 *
	 * @param str         被编码的字符串
	 * @param isSkipAscii 是否跳过ASCII字符（只跳过可见字符）
	 * @return Unicode字符串
	 */
	public static String toUnicode(final String str, final boolean isSkipAscii) {
		if (StrUtil.isEmpty(str)) {
			return str;
		}

		final int len = str.length();
		final StringBuilder unicode = new StringBuilder(str.length() * 6);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if (isSkipAscii && CharUtil.isAsciiPrintable(c)) {
				unicode.append(c);
			} else {
				unicode.append(HexUtil.toUnicodeHex(c));
			}
		}
		return unicode.toString();
	}
}
