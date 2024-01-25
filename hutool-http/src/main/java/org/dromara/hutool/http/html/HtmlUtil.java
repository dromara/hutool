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

package org.dromara.hutool.http.html;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.escape.EscapeUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.xml.XmlUtil;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * HTML工具类
 *
 * <p>
 * 比如去掉指定标签（例如广告栏等）、去除JS、去掉样式等等，这些操作都可以使用此工具类完成。
 *
 * @author Looly
 */
public class HtmlUtil {

	/**
	 * HTML标签正则
	 */
	public static final Pattern RE_HTML_MARK = Pattern.compile("(<[^<]*?>)|(<\\s*?/[^<]*?>)|(<[^<]*?/\\s*?>)", Pattern.CASE_INSENSITIVE);
	/**
	 * script标签正则
	 */
	public static final Pattern RE_SCRIPT = Pattern.compile("<\\s*?script[^>]*?>.*?<\\s*?/\\s*?script\\s*?>", Pattern.CASE_INSENSITIVE);
	/**
	 * 正则：匹配meta标签的编码信息
	 */
	public static final Pattern META_CHARSET_PATTERN = Pattern.compile("<meta[^>]*?charset\\s*=\\s*['\"]?([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

	private static final char[][] TEXT = new char[256][];

	static {
		// ascii码值最大的是【0x7f=127】，扩展ascii码值最大的是【0xFF=255】，
		// 因为ASCII码使用指定的7位或8位二进制数组合来表示128或256种可能的字符，标准ASCII码也叫基础ASCII码。
		for (int i = 0; i < 256; i++) {
			TEXT[i] = new char[]{(char) i};
		}

		// special HTML characters
		TEXT['\''] = "&#039;".toCharArray(); // 单引号 ('&apos;' doesn't work - it is not by the w3 specs)
		TEXT['"'] = XmlUtil.QUOTE.toCharArray(); // 双引号
		TEXT['&'] = XmlUtil.AMP.toCharArray(); // &符
		TEXT['<'] = XmlUtil.LT.toCharArray(); // 小于号
		TEXT['>'] = XmlUtil.GT.toCharArray(); // 大于号
		TEXT[' '] = XmlUtil.NBSP.toCharArray(); // 不断开空格（non-breaking space，缩写nbsp。ASCII值是32：是用键盘输入的空格；ASCII值是160：不间断空格，即 &nbsp，所产生的空格，作用是在页面换行时不被打断）
	}

	/**
	 * 转义文本中的HTML字符为安全的字符，以下字符被转义：
	 * <ul>
	 * <li>' 替换为 &amp;#039; (&amp;apos; doesn't work in HTML4)</li>
	 * <li>" 替换为 &amp;quot;</li>
	 * <li>&amp; 替换为 &amp;amp;</li>
	 * <li>&lt; 替换为 &amp;lt;</li>
	 * <li>&gt; 替换为 &amp;gt;</li>
	 * </ul>
	 *
	 * @param text 被转义的文本
	 * @return 转义后的文本
	 */
	public static String escape(final String text) {
		return encode(text);
	}

	/**
	 * 还原被转义的HTML特殊字符
	 *
	 * @param htmlStr 包含转义符的HTML内容
	 * @return 转换后的字符串
	 */
	public static String unescape(final String htmlStr) {
		if (StrUtil.isBlank(htmlStr)) {
			return htmlStr;
		}

		return EscapeUtil.unescapeHtml4(htmlStr);
	}

	// ---------------------------------------------------------------- encode text

	/**
	 * 清除所有HTML标签，但是不删除标签内的内容
	 *
	 * @param content 文本
	 * @return 清除标签后的文本
	 */
	public static String cleanHtmlTag(final String content) {
		return ReUtil.replaceAll(content, RE_HTML_MARK, StrUtil.EMPTY);
	}

	/**
	 * 清除所有script标签，包括内容
	 *
	 * @param content 文本
	 * @return 清除标签后的文本
	 */
	public static String removeScriptTag(final String content) {
		return ReUtil.replaceAll(content, RE_SCRIPT, StrUtil.EMPTY);
	}

	/**
	 * 清除指定HTML标签和被标签包围的内容<br>
	 * 不区分大小写
	 *
	 * @param content  文本
	 * @param tagNames 要清除的标签
	 * @return 去除标签后的文本
	 */
	public static String removeHtmlTag(final String content, final String... tagNames) {
		return removeHtmlTag(content, true, tagNames);
	}

	/**
	 * 清除指定HTML标签，不包括内容<br>
	 * 不区分大小写
	 *
	 * @param content  文本
	 * @param tagNames 要清除的标签
	 * @return 去除标签后的文本
	 */
	public static String unwrapHtmlTag(final String content, final String... tagNames) {
		return removeHtmlTag(content, false, tagNames);
	}

	/**
	 * 清除指定HTML标签<br>
	 * 不区分大小写
	 *
	 * @param content        文本
	 * @param withTagContent 是否去掉被包含在标签中的内容
	 * @param tagNames       要清除的标签
	 * @return 去除标签后的文本
	 */
	public static String removeHtmlTag(String content, final boolean withTagContent, final String... tagNames) {
		String regex;
		for (String tagName : tagNames) {
			if (StrUtil.isBlank(tagName)) {
				continue;
			}
			tagName = tagName.trim();
			// (?i)表示其后面的表达式忽略大小写
			if (withTagContent) {
				// 标签及其包含内容
				regex = StrUtil.format("(?i)<{}(\\s+[^>]*?)?/?>(.*?</{}>)?", tagName, tagName);
			} else {
				// 标签不包含内容
				regex = StrUtil.format("(?i)<{}(\\s+[^>]*?)?/?>|</?{}>", tagName, tagName);
			}

			content = ReUtil.delAll(regex, content); // 非自闭标签小写
		}
		return content;
	}

	/**
	 * 去除HTML标签中的属性，如果多个标签有相同属性，都去除
	 *
	 * @param content 文本
	 * @param attrs   属性名（不区分大小写）
	 * @return 处理后的文本
	 */
	public static String removeHtmlAttr(String content, final String... attrs) {
		String regex;
		for (final String attr : attrs) {
			// (?i)     表示忽略大小写
			// \s*      属性名前后的空白符去除
			// [^>]+?   属性值，至少有一个非>的字符，>表示标签结束
			// \s+(?=>) 表示属性值后跟空格加>，即末尾的属性，此时去掉空格
			// (?=\s|>) 表示属性值后跟空格（属性后还有别的属性）或者跟>（最后一个属性）
			regex = StrUtil.format("(?i)(\\s*{}\\s*=\\s*)" +
				"(" +
				// name="xxxx"
				"([\"][^\"]+?[\"])|" +
				// name=xxx > 或者 name=xxx> 或者 name=xxx name2=xxx
				"([^>]+?\\s*(?=\\s|>))" +
				")", attr);
			content = content.replaceAll(regex, StrUtil.EMPTY);
		}

		// issue#I8YV0K 去除尾部空格
		content = ReUtil.replaceAll(content, "\\s+(>|/>)", "$1");

		return content;
	}

	/**
	 * 去除指定标签的所有属性
	 *
	 * @param content  内容
	 * @param tagNames 指定标签
	 * @return 处理后的文本
	 */
	public static String removeAllHtmlAttr(String content, final String... tagNames) {
		String regex;
		for (final String tagName : tagNames) {
			regex = StrUtil.format("(?i)<{}[^>]*?>", tagName);
			content = content.replaceAll(regex, StrUtil.format("<{}>", tagName));
		}
		return content;
	}

	/**
	 * Encoder
	 *
	 * @param text 被编码的文本
	 * @return 编码后的字符
	 */
	private static String encode(final String text) {
		final int len;
		if ((text == null) || ((len = text.length()) == 0)) {
			return StrUtil.EMPTY;
		}
		final StringBuilder buffer = new StringBuilder(len + (len >> 2));
		char c;
		for (int i = 0; i < len; i++) {
			c = text.charAt(i);
			if (c < 256) {
				buffer.append(TEXT[c]);
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/**
	 * 过滤HTML文本，防止XSS攻击
	 *
	 * @param htmlContent HTML内容
	 * @return 过滤后的内容
	 */
	public static String filter(final String htmlContent) {
		return new HTMLFilter().filter(htmlContent);
	}

	/**
	 * 从流中读取内容<br>
	 * 首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码；
	 *
	 * @param in                      输入流
	 * @param charset                 字符集
	 * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
	 * @return 内容
	 */
	public static String getString(final InputStream in, final Charset charset, final boolean isGetCharsetFromContent) {
		final byte[] contentBytes = IoUtil.readBytes(in);
		return getString(contentBytes, charset, isGetCharsetFromContent);
	}

	/**
	 * 从流中读取内容<br>
	 * 首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码；
	 *
	 * @param contentBytes            内容byte数组
	 * @param charset                 字符集
	 * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
	 * @return 内容
	 */
	public static String getString(final byte[] contentBytes, Charset charset, final boolean isGetCharsetFromContent) {
		if (null == contentBytes) {
			return null;
		}

		if (null == charset) {
			charset = CharsetUtil.UTF_8;
		}
		String content = new String(contentBytes, charset);
		if (isGetCharsetFromContent) {
			final String charsetInContentStr = ReUtil.get(META_CHARSET_PATTERN, content, 1);
			if (StrUtil.isNotBlank(charsetInContentStr)) {
				Charset charsetInContent = null;
				try {
					charsetInContent = Charset.forName(charsetInContentStr);
				} catch (final Exception e) {
					if (StrUtil.containsIgnoreCase(charsetInContentStr, "utf-8") || StrUtil.containsIgnoreCase(charsetInContentStr, "utf8")) {
						charsetInContent = CharsetUtil.UTF_8;
					} else if (StrUtil.containsIgnoreCase(charsetInContentStr, "gbk")) {
						charsetInContent = CharsetUtil.GBK;
					}
					// ignore
				}
				if (null != charsetInContent && !charset.equals(charsetInContent)) {
					content = new String(contentBytes, charsetInContent);
				}
			}
		}
		return content;
	}
}
