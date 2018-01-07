package cn.hutool.http;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

/**
 * HTML工具类
 * 
 * @author xiaoleilu
 * 
 */
public class HtmlUtil {

	public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";
	public static final String RE_SCRIPT = "<[\\s]*?script[^>]*?>.*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

	private static final char[][] TEXT = new char[64][];

	static {
		for (int i = 0; i < 64; i++) {
			TEXT[i] = new char[] { (char) i };
		}

		// special HTML characters
		TEXT['\''] = "&#039;".toCharArray(); // 单引号 ('&apos;' doesn't work - it is not by the w3 specs)
		TEXT['"'] = StrUtil.HTML_QUOTE.toCharArray(); // 双引号
		TEXT['&'] = StrUtil.HTML_AMP.toCharArray(); // &符
		TEXT['<'] = StrUtil.HTML_LT.toCharArray(); // 小于号
		TEXT['>'] = StrUtil.HTML_GT.toCharArray(); // 大于号
	}

	/**
	 * 基本功能：替换标记以正常显示
	 * 
	 * @param input 输入的HTML字符串
	 * @return String
	 * @since 3.2.3
	 */
	public static String escape(String input) {
		if (StrUtil.isBlank(input)) {
			return input;
		}
		final StringBuilder filtered = StrUtil.builder(input.length());
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			switch (c) {
			case '<':
				filtered.append("&lt;");
				break;
			case '>':
				filtered.append("&gt;");
				break;
			case '"':
				filtered.append("&quot;");
				break;
			case '&':
				filtered.append("&amp;");
				break;
			default:
				filtered.append(c);
			}
		}
		return filtered.toString();
	}

	/**
	 * 还原被转义的HTML特殊字符
	 * 
	 * @param htmlStr 包含转义符的HTML内容
	 * @return 转换后的字符串
	 */
	public static String restoreEscaped(String htmlStr) {
		if (StrUtil.isBlank(htmlStr)) {
			return htmlStr;
		}
		return htmlStr.replace(StrUtil.HTML_APOS, "'")//
				.replace(StrUtil.HTML_LT, "<")//
				.replace(StrUtil.HTML_GT, ">")//
				.replace(StrUtil.HTML_QUOTE, "\"")//
				.replace(StrUtil.HTML_AMP, "&")//
				.replace(StrUtil.HTML_NBSP, " "//
		);
	}

	// ---------------------------------------------------------------- encode text

	/**
	 * 转义文本中的HTML字符为安全的字符，以下字符被转义：
	 * <ul>
	 * <li>' with &amp;#039; (&amp;apos; doesn't work in HTML4)</li>
	 * <li>" with &amp;quot;</li>
	 * <li>&amp; with &amp;amp;</li>
	 * <li>&lt; with &amp;lt;</li>
	 * <li>&gt; with &amp;gt;</li>
	 * </ul>
	 * 
	 * @param text 被转义的文本
	 * @return 转义后的文本
	 */
	public static String encode(String text) {
		return encode(text, TEXT);
	}

	/**
	 * 清除所有HTML标签
	 * 
	 * @param content 文本
	 * @return 清除标签后的文本
	 */
	public static String cleanHtmlTag(String content) {
		return content.replaceAll(RE_HTML_MARK, "");
	}

	/**
	 * 清除指定HTML标签和被标签包围的内容<br>
	 * 不区分大小写
	 * 
	 * @param content 文本
	 * @param tagNames 要清除的标签
	 * @return 去除标签后的文本
	 */
	public static String removeHtmlTag(String content, String... tagNames) {
		return removeHtmlTag(content, true, tagNames);
	}

	/**
	 * 清除指定HTML标签，不包括内容<br>
	 * 不区分大小写
	 * 
	 * @param content 文本
	 * @param tagNames 要清除的标签
	 * @return 去除标签后的文本
	 */
	public static String unwrapHtmlTag(String content, String... tagNames) {
		return removeHtmlTag(content, false, tagNames);
	}

	/**
	 * 清除指定HTML标签<br>
	 * 不区分大小写
	 * 
	 * @param content 文本
	 * @param withTagContent 是否去掉被包含在标签中的内容
	 * @param tagNames 要清除的标签
	 * @return 去除标签后的文本
	 */
	public static String removeHtmlTag(String content, boolean withTagContent, String... tagNames) {
		String regex = null;
		for (String tagName : tagNames) {
			if (StrUtil.isBlank(tagName)) {
				continue;
			}
			tagName = tagName.trim();
			// (?i)表示其后面的表达式忽略大小写
			if (withTagContent) {
				// 标签及其包含内容
				regex = StrUtil.format("(?i)<{}\\s*?[^>]*?/?>(.*?</{}>)?", tagName, tagName);
			} else {
				// 标签不包含内容
				regex = StrUtil.format("(?i)<{}\\s*?[^>]*?>|</{}>", tagName, tagName);
			}

			content = ReUtil.delAll(regex, content); // 非自闭标签小写
		}
		return content;
	}

	/**
	 * 去除HTML标签中的属性
	 * 
	 * @param content 文本
	 * @param attrs 属性名（不区分大小写）
	 * @return 处理后的文本
	 */
	public static String removeHtmlAttr(String content, String... attrs) {
		String regex = null;
		for (String attr : attrs) {
			regex = StrUtil.format("(?i)\\s*{}=([\"']).*?\\1", attr);
			content = content.replaceAll(regex, StrUtil.EMPTY);
		}
		return content;
	}

	/**
	 * 去除指定标签的所有属性
	 * 
	 * @param content 内容
	 * @param tagNames 指定标签
	 * @return 处理后的文本
	 */
	public static String removeAllHtmlAttr(String content, String... tagNames) {
		String regex = null;
		for (String tagName : tagNames) {
			regex = StrUtil.format("(?i)<{}[^>]*?>", tagName);
			content = content.replaceAll(regex, StrUtil.format("<{}>", tagName));
		}
		return content;
	}

	/**
	 * Encoder
	 * 
	 * @param text 被编码的文本
	 * @param array 特殊字符集合
	 * @return 编码后的字符
	 */
	private static String encode(String text, char[][] array) {
		int len;
		if ((text == null) || ((len = text.length()) == 0)) {
			return StrUtil.EMPTY;
		}
		StringBuilder buffer = new StringBuilder(len + (len >> 2));
		char c;
		for (int i = 0; i < len; i++) {
			c = text.charAt(i);
			if (c < 64) {
				buffer.append(array[c]);
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
	public static String filter(String htmlContent) {
		return new HTMLFilter().filter(htmlContent);
	}
}
