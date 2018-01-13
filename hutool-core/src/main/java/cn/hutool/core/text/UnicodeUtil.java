package cn.hutool.core.text;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 提供Unicode字符串和普通字符串之间的转换
 * 
 * @author 兜兜毛毛, looly
 * @since 4.0.0
 *
 */
public class UnicodeUtil {

	/**
	 * Unicode字符串转为普通字符串<br>
	 * Unicode字符串的表现方式为：\\uXXXX
	 * 
	 * @param unicode Unicode字符串
	 * @return 普通字符串
	 */
	public static String toString(String unicode) {
		if (StrUtil.isBlank(unicode)) {
			return unicode;
		}

		final int len = unicode.length();
		StrBuilder sb = StrBuilder.create(len);
		int i = -1;
		int pos = 0;
		while ((i = unicode.indexOf("\\u", pos)) != -1) {
			sb.append(unicode.substring(pos, i));
			if (i + 5 < len) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
			}
		}
		return sb.toString();
	}
	
	/**
	 * 字符串编码为Unicode形式
	 * 
	 * @param str 被编码的字符串
	 * @return Unicode字符串
	 */
	public static String toUnicode(String str) {
		return toUnicode(str, true);
	}

	/**
	 * 字符串编码为Unicode形式
	 * 
	 * @param str 被编码的字符串
	 * @param isSkipAscii 是否跳过ASCII字符（只跳过可见字符）
	 * @return Unicode字符串
	 */
	public static String toUnicode(String str, boolean isSkipAscii) {
		if (StrUtil.isBlank(str)) {
			return str;
		}

		final int len = str.length();
		final StrBuilder unicode = StrBuilder.create(str.length() * 6);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			if(isSkipAscii && CharUtil.isAsciiPrintable(c) ) {
				unicode.append(c);
			}else {
				unicode.append(HexUtil.toUnicodeHex(c));
			}
		}
		return unicode.toString();
	}
}
