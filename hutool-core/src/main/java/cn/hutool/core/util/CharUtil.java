package cn.hutool.core.util;

import cn.hutool.core.text.ASCIIStrCache;

/**
 * 字符工具类<br>
 * 部分工具来自于Apache Commons系列
 * 
 * @author looly
 * @since 4.0.1
 */
public class CharUtil {

	public static final char SPACE = ' ';
	public static final char TAB = '	';
	public static final char DOT = '.';
	public static final char SLASH = '/';
	public static final char BACKSLASH = '\\';
	public static final char CR = '\r';
	public static final char LF = '\n';
	public static final char UNDERLINE = '_';
	public static final char DASHED = '-';
	public static final char COMMA = ',';
	public static final char DELIM_START = '{';
	public static final char DELIM_END = '}';
	public static final char BRACKET_START = '[';
	public static final char BRACKET_END = ']';
	public static final char COLON = ':';
	public static final char DOUBLE_QUOTES = '"';
	public static final char SINGLE_QUOTE = '\'';
	public static final char AMP = '&';

	/**
	 * 是否为ASCII字符，ASCII字符位于0~127之间
	 *
	 * <pre>
	 *   CharUtil.isAscii('a')  = true
	 *   CharUtil.isAscii('A')  = true
	 *   CharUtil.isAscii('3')  = true
	 *   CharUtil.isAscii('-')  = true
	 *   CharUtil.isAscii('\n') = true
	 *   CharUtil.isAscii('&copy;') = false
	 * </pre>
	 * 
	 * @param ch 被检查的字符处
	 * @return true表示为ASCII字符，ASCII字符位于0~127之间
	 */
	public static boolean isAscii(char ch) {
		return ch < 128;
	}

	/**
	 * 是否为可见ASCII字符，可见字符位于32~126之间
	 *
	 * <pre>
	 *   CharUtil.isAsciiPrintable('a')  = true
	 *   CharUtil.isAsciiPrintable('A')  = true
	 *   CharUtil.isAsciiPrintable('3')  = true
	 *   CharUtil.isAsciiPrintable('-')  = true
	 *   CharUtil.isAsciiPrintable('\n') = false
	 *   CharUtil.isAsciiPrintable('&copy;') = false
	 * </pre>
	 * 
	 * @param ch 被检查的字符处
	 * @return true表示为ASCII可见字符，可见字符位于32~126之间
	 */
	public static boolean isAsciiPrintable(char ch) {
		return ch >= 32 && ch < 127;
	}

	/**
	 * 是否为ASCII控制符（不可见字符），控制符位于0~31和127
	 *
	 * <pre>
	 *   CharUtil.isAsciiControl('a')  = false
	 *   CharUtil.isAsciiControl('A')  = false
	 *   CharUtil.isAsciiControl('3')  = false
	 *   CharUtil.isAsciiControl('-')  = false
	 *   CharUtil.isAsciiControl('\n') = true
	 *   CharUtil.isAsciiControl('&copy;') = false
	 * </pre>
	 * 
	 * @param ch 被检查的字符
	 * @return true表示为控制符，控制符位于0~31和127
	 */
	public static boolean isAsciiControl(final char ch) {
		return ch < 32 || ch == 127;
	}

	/**
	 * 判断是否为字母（包括大写字母和小写字母）<br>
	 * 字母包括A~Z和a~z
	 *
	 * <pre>
	 *   CharUtil.isLetter('a')  = true
	 *   CharUtil.isLetter('A')  = true
	 *   CharUtil.isLetter('3')  = false
	 *   CharUtil.isLetter('-')  = false
	 *   CharUtil.isLetter('\n') = false
	 *   CharUtil.isLetter('&copy;') = false
	 * </pre>
	 * 
	 * @param ch 被检查的字符
	 * @return true表示为字母（包括大写字母和小写字母）字母包括A~Z和a~z
	 */
	public static boolean isLetter(char ch) {
		return isLetterUpper(ch) || isLetterLower(ch);
	}

	/**
	 * <p>
	 * 判断是否为大写字母，大写字母包括A~Z
	 * </p>
	 *
	 * <pre>
	 *   CharUtil.isLetterUpper('a')  = false
	 *   CharUtil.isLetterUpper('A')  = true
	 *   CharUtil.isLetterUpper('3')  = false
	 *   CharUtil.isLetterUpper('-')  = false
	 *   CharUtil.isLetterUpper('\n') = false
	 *   CharUtil.isLetterUpper('&copy;') = false
	 * </pre>
	 * 
	 * @param ch 被检查的字符
	 * @return true表示为大写字母，大写字母包括A~Z
	 */
	public static boolean isLetterUpper(final char ch) {
		return ch >= 'A' && ch <= 'Z';
	}

	/**
	 * <p>
	 * 检查字符是否为小写字母，小写字母指a~z
	 * </p>
	 *
	 * <pre>
	 *   CharUtil.isLetterLower('a')  = true
	 *   CharUtil.isLetterLower('A')  = false
	 *   CharUtil.isLetterLower('3')  = false
	 *   CharUtil.isLetterLower('-')  = false
	 *   CharUtil.isLetterLower('\n') = false
	 *   CharUtil.isLetterLower('&copy;') = false
	 * </pre>
	 * 
	 * @param ch 被检查的字符
	 * @return true表示为小写字母，小写字母指a~z
	 */
	public static boolean isLetterLower(final char ch) {
		return ch >= 'a' && ch <= 'z';
	}

	/**
	 * <p>
	 * 检查是否为数字字符，数字字符指0~9
	 * </p>
	 *
	 * <pre>
	 *   CharUtil.isNumber('a')  = false
	 *   CharUtil.isNumber('A')  = false
	 *   CharUtil.isNumber('3')  = true
	 *   CharUtil.isNumber('-')  = false
	 *   CharUtil.isNumber('\n') = false
	 *   CharUtil.isNumber('&copy;') = false
	 * </pre>
	 * 
	 * @param ch 被检查的字符
	 * @return true表示为数字字符，数字字符指0~9
	 */
	public static boolean isNumber(char ch) {
		return ch >= '0' && ch <= '9';
	}
	
	/**
	 * 是否为16进制规范的字符，判断是否为如下字符
	 * <pre>
	 * 1. 0~9
	 * 2. a~f
	 * 4. A~F
	 * </pre>
	 * 
	 * @param c 字符
	 * @return 是否为16进制规范的字符
	 * @since 4.1.5
	 */
	public static boolean isHexChar(char c) {
		return isNumber(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
	}

	/**
	 * 是否为字符或数字，包括A~Z、a~z、0~9
	 *
	 * <pre>
	 *   CharUtil.isLetterOrNumber('a')  = true
	 *   CharUtil.isLetterOrNumber('A')  = true
	 *   CharUtil.isLetterOrNumber('3')  = true
	 *   CharUtil.isLetterOrNumber('-')  = false
	 *   CharUtil.isLetterOrNumber('\n') = false
	 *   CharUtil.isLetterOrNumber('&copy;') = false
	 * </pre>
	 * 
	 * @param ch 被检查的字符
	 * @return true表示为字符或数字，包括A~Z、a~z、0~9
	 */
	public static boolean isLetterOrNumber(final char ch) {
		return isLetter(ch) || isNumber(ch);
	}

	/**
	 * 字符转为字符串<br>
	 * 如果为ASCII字符，使用缓存
	 * 
	 * @param c 字符
	 * @return 字符串
	 * @see ASCIIStrCache#toString(char)
	 */
	public static String toString(char c) {
		return ASCIIStrCache.toString(c);
	}

	/**
	 * 给定类名是否为字符类，字符类包括：
	 * 
	 * <pre>
	 * Character.class
	 * char.class
	 * </pre>
	 * 
	 * @param clazz 被检查的类
	 * @return true表示为字符类
	 */
	public static boolean isCharClass(Class<?> clazz) {
		return clazz == Character.class || clazz == char.class;
	}

	/**
	 * 给定对象对应的类是否为字符类，字符类包括：
	 * 
	 * <pre>
	 * Character.class
	 * char.class
	 * </pre>
	 * 
	 * @param value 被检查的对象
	 * @return true表示为字符类
	 */
	public static boolean isChar(Object value) {
		return value instanceof Character || value.getClass() == char.class;
	}

	/**
	 * 是否空白符<br>
	 * 空白符包括空格、制表符、全角空格和不间断空格<br>
	 * 
	 * @param c 字符
	 * @return 是否空白符
	 * @see Character#isWhitespace(int)
	 * @see Character#isSpaceChar(int)
	 * @since 4.0.10
	 */
	public static boolean isBlankChar(char c) {
		return isBlankChar((int) c);
	}
	
	/**
	 * 是否空白符<br>
	 * 空白符包括空格、制表符、全角空格和不间断空格<br>
	 * 
	 * @see Character#isWhitespace(int)
	 * @see Character#isSpaceChar(int)
	 * @param c 字符
	 * @return 是否空白符
	 * @since 4.0.10
	 */
	public static boolean isBlankChar(int c) {
		return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a';
	}

	/**
	 * 判断是否为emoji表情符<br>
	 * 
	 * @param c 字符
	 * @return 是否为emoji
	 * @since 4.0.8
	 */
	public static boolean isEmoji(char c) {
		return false ==  ((c == 0x0) || //
				(c == 0x9) || //
				(c == 0xA) || //
				(c == 0xD) || //
				((c >= 0x20) && (c <= 0xD7FF)) || //
				((c >= 0xE000) && (c <= 0xFFFD)) || //
				((c >= 0x10000) && (c <= 0x10FFFF)));
	}
	
	/**
	 * 是否为Windows或者Linux（Unix）文件分隔符<br>
	 * Windows平台下分隔符为\，Linux（Unix）为/
	 * 
	 * @param c 字符
	 * @return 是否为Windows或者Linux（Unix）文件分隔符
	 * @since 4.1.11
	 */
	public static boolean isFileSeparator(char c) {
		return SLASH == c || BACKSLASH == c;
	}

	/**
	 * 比较两个字符是否相同
	 * 
	 * @param c1 字符1
	 * @param c2 字符2
	 * @param ignoreCase 是否忽略大小写
	 * @return 是否相同
	 * @since 4.0.3
	 */
	public static boolean equals(char c1, char c2, boolean ignoreCase) {
		if (ignoreCase) {
			return Character.toLowerCase(c1) == Character.toLowerCase(c2);
		}
		return c1 == c2;
	}
}
