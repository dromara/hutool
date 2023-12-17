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

package org.dromara.hutool.core.text.escape;

import org.dromara.hutool.core.text.StrUtil;

import java.util.function.Predicate;

/**
 * 转义和反转义工具类Escape / Unescape<br>
 * escape采用ISO Latin字符集对指定的字符串进行编码。<br>
 * 所有的空格符、标点符号、特殊字符以及其他非ASCII字符都将被转化成%xx格式的字符编码(xx等于该字符在字符集表里面的编码的16进制数字)。
 *
 * @author Looly
 */
public class EscapeUtil {

	/**
	 * 不转义的符号编码
	 */
	private static final String NOT_ESCAPE_CHARS = "*@-_+./";
	private static final Predicate<Character> JS_ESCAPE_FILTER = c -> !(
			Character.isDigit(c)
					|| Character.isLowerCase(c)
					|| Character.isUpperCase(c)
					|| StrUtil.contains(NOT_ESCAPE_CHARS, c)
	);

	/**
	 * 转义XML中的特殊字符<br>
	 * <pre>
	 * 	 &amp; (ampersand) 替换为 &amp;amp;
	 * 	 &lt; (less than) 替换为 &amp;lt;
	 * 	 &gt; (greater than) 替换为 &amp;gt;
	 * 	 &quot; (double quote) 替换为 &amp;quot;
	 * 	 ' (single quote / apostrophe) 替换为 &amp;apos;
	 * </pre>
	 *
	 * @param xml XML文本
	 * @return 转义后的文本
	 * @since 5.7.2
	 */
	public static String escapeXml(final CharSequence xml) {
		final XmlEscape escape = new XmlEscape();
		return escape.apply(xml).toString();
	}

	/**
	 * 反转义XML中的特殊字符
	 *
	 * @param xml XML文本
	 * @return 转义后的文本
	 * @since 5.7.2
	 */
	public static String unescapeXml(final CharSequence xml) {
		final XmlUnescape unescape = new XmlUnescape();
		return unescape.apply(xml).toString();
	}

	/**
	 * 转义HTML4中的特殊字符
	 *
	 * @param html HTML文本
	 * @return 转义后的文本
	 * @since 4.1.5
	 */
	public static String escapeHtml4(final CharSequence html) {
		final Html4Escape escape = new Html4Escape();
		return escape.apply(html).toString();
	}

	/**
	 * 反转义HTML4中的特殊字符
	 *
	 * @param html HTML文本
	 * @return 转义后的文本
	 * @since 4.1.5
	 */
	public static String unescapeHtml4(final CharSequence html) {
		final Html4Unescape unescape = new Html4Unescape();
		return unescape.apply(html).toString();
	}

	/**
	 * Escape编码（Unicode）（等同于JS的escape()方法）<br>
	 * 该方法不会对 ASCII 字母和数字进行编码，也不会对下面这些 ASCII 标点符号进行编码： * @ - _ + . / <br>
	 * 其他所有的字符都会被转义序列替换。
	 *
	 * @param content 被转义的内容
	 * @return 编码后的字符串
	 */
	public static String escape(final CharSequence content) {
		return escape(content, JS_ESCAPE_FILTER);
	}

	/**
	 * Escape编码（Unicode）<br>
	 * 该方法不会对 ASCII 字母和数字进行编码。其他所有的字符都会被转义序列替换。
	 *
	 * @param content 被转义的内容
	 * @return 编码后的字符串
	 */
	public static String escapeAll(final CharSequence content) {
		return escape(content, c -> true);
	}

	/**
	 * Escape编码（Unicode）<br>
	 * 该方法不会对 ASCII 字母和数字进行编码。其他所有的字符都会被转义序列替换。
	 *
	 * @param content 被转义的内容
	 * @param filter  编码过滤器，对于过滤器中accept为false的字符不做编码
	 * @return 编码后的字符串
	 */
	public static String escape(final CharSequence content, final Predicate<Character> filter) {
		if (StrUtil.isEmpty(content)) {
			return StrUtil.str(content);
		}

		final StringBuilder tmp = new StringBuilder(content.length() * 6);
		char c;
		for (int i = 0; i < content.length(); i++) {
			c = content.charAt(i);
			if (!filter.test(c)) {
				tmp.append(c);
			} else if (c < 256) {
				tmp.append("%");
				if (c < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(c, 16));
			} else {
				tmp.append("%u");
				if(c <= 0xfff){
					// issue#I49JU8@Gitee
					tmp.append("0");
				}
				tmp.append(Integer.toString(c, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * Escape解码
	 *
	 * @param content 被转义的内容
	 * @return 解码后的字符串
	 */
	public static String unescape(final String content) {
		if (StrUtil.isBlank(content)) {
			return content;
		}

		final StringBuilder tmp = new StringBuilder(content.length());
		int lastPos = 0;
		int pos;
		char ch;
		while (lastPos < content.length()) {
			pos = content.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (content.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(content.substring(lastPos));
					lastPos = content.length();
				} else {
					tmp.append(content, lastPos, pos);
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 安全的unescape文本，当文本不是被escape的时候，返回原文。
	 *
	 * @param content 内容
	 * @return 解码后的字符串，如果解码失败返回原字符串
	 */
	public static String safeUnescape(final String content) {
		try {
			return unescape(content);
		} catch (final Exception e) {
			// Ignore Exception
		}
		return content;
	}
}
