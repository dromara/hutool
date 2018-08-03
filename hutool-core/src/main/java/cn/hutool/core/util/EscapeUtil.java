package cn.hutool.core.util;

import cn.hutool.core.text.escape.Html4Escape;
import cn.hutool.core.text.escape.Html4Unescape;

/**
 * 转义和反转义工具类Escape / Unescape<br>
 * escape采用ISO Latin字符集对指定的字符串进行编码。<br>
 * 所有的空格符、标点符号、特殊字符以及其他非ASCII字符都将被转化成%xx格式的字符编码(xx等于该字符在字符集表里面的编码的16进制数字)。
 * 
 * @author xiaoleilu
 */
public class EscapeUtil {

	/**
	 * 转义HTML4中的特殊字符
	 * 
	 * @param html HTML文本
	 * @return 转义后的文本
	 * @since 4.1.5
	 */
	public static String escapeHtml4(String html) {
		Html4Escape escape = new Html4Escape();
		return escape.replace(html).toString();
	}

	/**
	 * 反转义HTML4中的特殊字符
	 * 
	 * @param html HTML文本
	 * @return 转义后的文本
	 * @since 4.1.5
	 */
	public static String unescapeHtml4(String html) {
		Html4Unescape unescape = new Html4Unescape();
		return unescape.replace(html).toString();
	}

	/**
	 * Escape编码（Unicode）<br>
	 * 该方法不会对 ASCII 字母和数字进行编码，也不会对下面这些 ASCII 标点符号进行编码： * @ - _ + . / 。其他所有的字符都会被转义序列替换。
	 * 
	 * @param content 被转义的内容
	 * @return 编码后的字符串
	 */
	public static String escape(String content) {
		if (StrUtil.isBlank(content)) {
			return content;
		}

		int i;
		char j;
		StringBuilder tmp = new StringBuilder();
		tmp.ensureCapacity(content.length() * 6);

		for (i = 0; i < content.length(); i++) {

			j = content.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)) {
				tmp.append(j);
			} else if (j < 256) {
				tmp.append("%");
				if (j < 16) {
					tmp.append("0");
				}
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
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
	public static String unescape(String content) {
		if (StrUtil.isBlank(content)) {
			return content;
		}

		StringBuilder tmp = new StringBuilder(content.length());
		int lastPos = 0, pos = 0;
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
					tmp.append(content.substring(lastPos, pos));
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
	public static String safeUnescape(String content) {
		try {
			return unescape(content);
		} catch (Exception e) {
			// Ignore Exception
		}
		return content;
	}
}
