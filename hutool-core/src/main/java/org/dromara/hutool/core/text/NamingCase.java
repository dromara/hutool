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

package org.dromara.hutool.core.text;

/**
 * 命名规则封装，主要是针对驼峰风格命名、连接符命名等的封装
 *
 * @author looly
 * @since 5.7.10
 */
public class NamingCase {

	/**
	 * 将驼峰式命名的字符串转换为下划线方式，又称SnakeCase、underScoreCase。<br>
	 * 如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
	 * 规则为：
	 * <ul>
	 *     <li>单字之间以下划线隔开</li>
	 *     <li>每个单字的首字母亦用小写字母</li>
	 * </ul>
	 * 例如：
	 *
	 * <pre>
	 * HelloWorld=》hello_world
	 * Hello_World=》hello_world
	 * HelloWorld_test=》hello_world_test
	 * </pre>
	 *
	 * @param str 转换前的驼峰式命名的字符串，也可以为下划线形式
	 * @return 转换后下划线方式命名的字符串
	 */
	public static String toUnderlineCase(final CharSequence str) {
		return toSymbolCase(str, CharUtil.UNDERLINE);
	}

	/**
	 * 将驼峰式命名的字符串转换为短横连接方式。<br>
	 * 如果转换前的驼峰式命名的字符串为空，则返回空字符串。<br>
	 * 规则为：
	 * <ul>
	 *     <li>单字之间横线线隔开</li>
	 *     <li>每个单字的首字母亦用小写字母</li>
	 * </ul>
	 * 例如：
	 *
	 * <pre>
	 * HelloWorld=》hello-world
	 * Hello_World=》hello-world
	 * HelloWorld_test=》hello-world-test
	 * </pre>
	 *
	 * @param str 转换前的驼峰式命名的字符串，也可以为下划线形式
	 * @return 转换后下划线方式命名的字符串
	 */
	public static String toKebabCase(final CharSequence str) {
		return toSymbolCase(str, CharUtil.DASHED);
	}

	/**
	 * 将驼峰式命名的字符串转换为使用符号连接方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。
	 *
	 * @param str    转换前的驼峰式命名的字符串，也可以为符号连接形式
	 * @param symbol 连接符
	 * @return 转换后符号连接方式命名的字符串
	 * @since 4.0.10
	 */
	public static String toSymbolCase(final CharSequence str, final char symbol) {
		if (str == null) {
			return null;
		}

		final int length = str.length();
		final StringBuilder sb = new StringBuilder();
		char c;
		for (int i = 0; i < length; i++) {
			c = str.charAt(i);
			if (Character.isUpperCase(c)) {
				final Character preChar = (i > 0) ? str.charAt(i - 1) : null;
				final Character nextChar = (i < str.length() - 1) ? str.charAt(i + 1) : null;

				if (null != preChar) {
					if (symbol == preChar) {
						// 前一个为分隔符
						if (null == nextChar || Character.isLowerCase(nextChar)) {
							//普通首字母大写，如_Abb -> _abb
							c = Character.toLowerCase(c);
						}
						//后一个为大写，按照专有名词对待，如_AB -> _AB
					} else if (Character.isLowerCase(preChar)) {
						// 前一个为小写
						sb.append(symbol);
						if (null == nextChar || Character.isLowerCase(nextChar) || CharUtil.isNumber(nextChar)) {
							//普通首字母大写，如aBcc -> a_bcc
							c = Character.toLowerCase(c);
						}
						// 后一个为大写，按照专有名词对待，如aBC -> a_BC
					} else {
						//前一个为大写
						if (null != nextChar && Character.isLowerCase(nextChar)) {
							// 普通首字母大写，如ABcc -> A_bcc
							sb.append(symbol);
							c = Character.toLowerCase(c);
						}
						// 后一个为大写，按照专有名词对待，如ABC -> ABC
					}
				} else {
					// 首字母，需要根据后一个判断是否转为小写
					if (null == nextChar || Character.isLowerCase(nextChar)) {
						// 普通首字母大写，如Abc -> abc
						c = Character.toLowerCase(c);
					}
					// 后一个为大写，按照专有名词对待，如ABC -> ABC
				}
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * 将下划线方式命名的字符串转换为帕斯卡式。<br>
	 * 规则为：
	 * <ul>
	 *     <li>单字之间不以空格或任何连接符断开</li>
	 *     <li>第一个单字首字母采用大写字母</li>
	 *     <li>后续单字的首字母亦用大写字母</li>
	 * </ul>
	 * 如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
	 * 例如：hello_world=》HelloWorld
	 *
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String toPascalCase(final CharSequence name) {
		return StrUtil.upperFirst(toCamelCase(name));
	}

	/**
	 * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
	 * 规则为：
	 * <ul>
	 *     <li>单字之间不以空格或任何连接符断开</li>
	 *     <li>第一个单字首字母采用小写字母</li>
	 *     <li>后续单字的首字母亦用大写字母</li>
	 * </ul>
	 * 例如：hello_world=》helloWorld
	 *
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String toCamelCase(final CharSequence name) {
		return toCamelCase(name, CharUtil.UNDERLINE);
	}

	/**
	 * 将连接符方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
	 *
	 * @param name   转换前的自定义方式命名的字符串
	 * @param symbol 原字符串中的连接符连接符
	 * @return 转换后的驼峰式命名的字符串
	 * @since 5.7.17
	 */
	public static String toCamelCase(final CharSequence name, final char symbol) {
		return toCamelCase(name, symbol, true);
	}

	/**
	 * 将连接符方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。<br>
	 * 当otherCharToLower为{@code true}时，分为以下情况：
	 * <ul>
	 *     <li>如果给定字符串全部为大写，转换为小写，如NAME转为name。</li>
	 *     <li>如果给定字符串大小写混合，认定为字符串已经是驼峰模式，只小写首字母，如TableName转换为tableName。</li>
	 * </ul>
	 *
	 * @param name             转换前的自定义方式命名的字符串
	 * @param symbol           原字符串中的连接符连接符
	 * @param otherCharToLower 其他非连接符后的字符是否需要转为小写
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String toCamelCase(final CharSequence name, final char symbol, final boolean otherCharToLower) {
		if (null == name) {
			return null;
		}

		final String name2 = name.toString();
		if (StrUtil.contains(name2, symbol)) {
			final int length = name2.length();
			final StringBuilder sb = new StringBuilder(length);
			boolean upperCase = false;
			for (int i = 0; i < length; i++) {
				final char c = name2.charAt(i);

				if (c == symbol) {
					upperCase = true;
				} else if (upperCase) {
					sb.append(Character.toUpperCase(c));
					upperCase = false;
				} else {
					sb.append(otherCharToLower ? Character.toLowerCase(c) : c);
				}
			}
			return sb.toString();
		} else {
			if(otherCharToLower){
				if(StrUtil.isUpperCase(name2)){
					return name2.toLowerCase();
				}
				return StrUtil.lowerFirst(name2);
			}

			return name2;
		}
	}

	/**
	 * 给定字符串中的字母是否全部为大写，判断依据如下：
	 *
	 * <pre>
	 * 1. 大写字母包括A-Z
	 * 2. 其它非字母的Unicode符都算作大写
	 * </pre>
	 *
	 * @param str 被检查的字符串
	 * @return 是否全部为大写
	 * @since 4.2.2
	 */
	public static boolean isUpperCase(final CharSequence str) {
		if (null == str) {
			return false;
		}
		final int len = str.length();
		for (int i = 0; i < len; i++) {
			if (Character.isLowerCase(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 给定字符串中的字母是否全部为小写，判断依据如下：
	 *
	 * <pre>
	 * 1. 小写字母包括a-z
	 * 2. 其它非字母的Unicode符都算作小写
	 * </pre>
	 *
	 * @param str 被检查的字符串
	 * @return 是否全部为小写
	 * @since 4.2.2
	 */
	public static boolean isLowerCase(final CharSequence str) {
		if (null == str) {
			return false;
		}
		final int len = str.length();
		for (int i = 0; i < len; i++) {
			if (Character.isUpperCase(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 切换给定字符串中的大小写。大写转小写，小写转大写。
	 *
	 * <pre>
	 * StrUtil.swapCase(null)                 = null
	 * StrUtil.swapCase("")                   = ""
	 * StrUtil.swapCase("The dog has a BONE") = "tHE DOG HAS A bone"
	 * </pre>
	 *
	 * @param str 字符串
	 * @return 交换后的字符串
	 * @since 4.3.2
	 */
	public static String swapCase(final String str) {
		if (StrUtil.isEmpty(str)) {
			return str;
		}

		final char[] buffer = str.toCharArray();

		for (int i = 0; i < buffer.length; i++) {
			final char ch = buffer[i];
			if (Character.isUpperCase(ch) || Character.isTitleCase(ch)) {
				buffer[i] = Character.toLowerCase(ch);
			} else if (Character.isLowerCase(ch)) {
				buffer[i] = Character.toUpperCase(ch);
			}
		}
		return new String(buffer);
	}
}
