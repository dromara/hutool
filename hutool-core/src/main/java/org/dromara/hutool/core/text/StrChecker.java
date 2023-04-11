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

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.util.CharUtil;

import java.util.function.Predicate;

/**
 * 字符串检查工具类，提供字符串的blank和empty等检查<br>
 * <ul>
 *     <li>empty定义：{@code null} or 空字符串：{@code ""}</li>
 *     <li>blank定义：{@code null} or 空字符串：{@code ""} or 空格、全角空格、制表符、换行符，等不可见字符</li>
 * </ul>
 */
public class StrChecker {

	/**
	 * 字符串常量：{@code "null"} <br>
	 * 注意：{@code "null" != null}
	 */
	public static final String NULL = "null";

	/**
	 * 字符串常量：空字符串 {@code ""}
	 */
	public static final String EMPTY = "";

	/**
	 * <p>字符串是否为空白，空白的定义如下：</p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>空字符串：{@code ""}</li>
	 *     <li>空格、全角空格、制表符、换行符，等不可见字符</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isBlank(null)     // true}</li>
	 *     <li>{@code StrUtil.isBlank("")       // true}</li>
	 *     <li>{@code StrUtil.isBlank(" \t\n")  // true}</li>
	 *     <li>{@code StrUtil.isBlank("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isEmpty(CharSequence)} 的区别是：
	 * 该方法会校验空白字符，且性能相对于 {@link #isEmpty(CharSequence)} 略慢。</p>
	 * <br>
	 *
	 * <p>建议：</p>
	 * <ul>
	 *     <li>该方法建议仅对于客户端（或第三方接口）传入的参数使用该方法。</li>
	 *     <li>需要同时校验多个字符串时，建议采用 {@link #hasBlank(CharSequence...)} 或 {@link #isAllBlank(CharSequence...)}</li>
	 * </ul>
	 *
	 * @param str 被检测的字符串
	 * @return 若为空白，则返回 true
	 * @see #isEmpty(CharSequence)
	 */
	public static boolean isBlank(final CharSequence str) {
		final int length;

		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			// 只要有一个非空字符即为非空字符串
			if (! CharUtil.isBlankChar(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * <p>字符串是否为非空白，非空白的定义如下： </p>
	 * <ol>
	 *     <li>不为 {@code null}</li>
	 *     <li>不为空字符串：{@code ""}</li>
	 *     <li>不为空格、全角空格、制表符、换行符，等不可见字符</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isNotBlank(null)     // false}</li>
	 *     <li>{@code StrUtil.isNotBlank("")       // false}</li>
	 *     <li>{@code StrUtil.isNotBlank(" \t\n")  // false}</li>
	 *     <li>{@code StrUtil.isNotBlank("abc")    // true}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isNotEmpty(CharSequence)} 的区别是：
	 * 该方法会校验空白字符，且性能相对于 {@link #isNotEmpty(CharSequence)} 略慢。</p>
	 * <p>建议：仅对于客户端（或第三方接口）传入的参数使用该方法。</p>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 * @see #isBlank(CharSequence)
	 */
	public static boolean isNotBlank(final CharSequence str) {
		final int length;

		if ((str == null) || ((length = str.length()) == 0)) {
			// empty
			return false;
		}

		for (int i = 0; i < length; i++) {
			// 只要有一个非空字符即为非空字符串
			if (! CharUtil.isBlankChar(str.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * <p>指定字符串数组中，是否包含空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.hasBlank()                  // true}</li>
	 *     <li>{@code StrUtil.hasBlank("", null, " ")     // true}</li>
	 *     <li>{@code StrUtil.hasBlank("123", " ")        // true}</li>
	 *     <li>{@code StrUtil.hasBlank("123", "abc")      // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isAllBlank(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>hasBlank(CharSequence...)            等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
	 *     <li>{@link #isAllBlank(CharSequence...)} 等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasBlank(final CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (final CharSequence str : strs) {
			if (isBlank(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 指定字符串集合中，是否包含空字符串。
	 *
	 * @param strs 字符串列表
	 * @return 批量判断字符串是否全部为空白
	 * @see CharSequenceUtil#hasBlank(CharSequence...)
	 * @since 6.0.0
	 */
	public static boolean hasBlank(final Iterable<? extends CharSequence> strs) {
		if (CollUtil.isEmpty(strs)) {
			return true;
		}
		for (final CharSequence str : strs) {
			if (isBlank(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isAllBlank()                  // true}</li>
	 *     <li>{@code StrUtil.isAllBlank("", null, " ")     // true}</li>
	 *     <li>{@code StrUtil.isAllBlank("123", " ")        // false}</li>
	 *     <li>{@code StrUtil.isAllBlank("123", "abc")      // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #hasBlank(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>{@link #hasBlank(CharSequence...)}   等价于 {@code isBlank(...) || isBlank(...) || ...}</li>
	 *     <li>isAllBlank(CharSequence...)          等价于 {@code isBlank(...) && isBlank(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 所有字符串是否为空白
	 */
	public static boolean isAllBlank(final CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (final CharSequence str : strs) {
			if (isNotBlank(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param strs 字符串列表
	 * @return 批量判断字符串是否全部为空白
	 * @see CharSequenceUtil#isAllBlank(CharSequence...)
	 * @since 6.0.1
	 */
	public static boolean isAllBlank(final Iterable<? extends CharSequence> strs) {
		if (CollUtil.isNotEmpty(strs)) {
			for (final CharSequence str : strs) {
				if (isNotBlank(str)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * <p>字符串是否为空，空的定义如下：</p>
	 * <ol>
	 *     <li>{@code null}</li>
	 *     <li>空字符串：{@code ""}</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isEmpty(null)     // true}</li>
	 *     <li>{@code StrUtil.isEmpty("")       // true}</li>
	 *     <li>{@code StrUtil.isEmpty(" \t\n")  // false}</li>
	 *     <li>{@code StrUtil.isEmpty("abc")    // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
	 * <p>建议：</p>
	 * <ul>
	 *     <li>该方法建议用于工具类或任何可以预期的方法参数的校验中。</li>
	 *     <li>需要同时校验多个字符串时，建议采用 {@link #hasEmpty(CharSequence...)} 或 {@link #isAllEmpty(CharSequence...)}</li>
	 * </ul>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为空
	 * @see #isBlank(CharSequence)
	 */
	public static boolean isEmpty(final CharSequence str) {
		return str == null || str.length() == 0;
	}

	/**
	 * <p>字符串是否为非空白，非空白的定义如下： </p>
	 * <ol>
	 *     <li>不为 {@code null}</li>
	 *     <li>不为空字符串：{@code ""}</li>
	 * </ol>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isNotEmpty(null)     // false}</li>
	 *     <li>{@code StrUtil.isNotEmpty("")       // false}</li>
	 *     <li>{@code StrUtil.isNotEmpty(" \t\n")  // true}</li>
	 *     <li>{@code StrUtil.isNotEmpty("abc")    // true}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isNotBlank(CharSequence)} 的区别是：该方法不校验空白字符。</p>
	 * <p>建议：该方法建议用于工具类或任何可以预期的方法参数的校验中。</p>
	 *
	 * @param str 被检测的字符串
	 * @return 是否为非空
	 * @see #isEmpty(CharSequence)
	 */
	public static boolean isNotEmpty(final CharSequence str) {
		return !isEmpty(str);
	}

	/**
	 * <p>是否包含空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.hasEmpty()                  // true}</li>
	 *     <li>{@code StrUtil.hasEmpty("", null)          // true}</li>
	 *     <li>{@code StrUtil.hasEmpty("123", "")         // true}</li>
	 *     <li>{@code StrUtil.hasEmpty("123", "abc")      // false}</li>
	 *     <li>{@code StrUtil.hasEmpty(" ", "\t", "\n")   // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isAllEmpty(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>hasEmpty(CharSequence...)            等价于 {@code isEmpty(...) || isEmpty(...) || ...}</li>
	 *     <li>{@link #isAllEmpty(CharSequence...)} 等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 */
	public static boolean hasEmpty(final CharSequence... strs) {
		if (ArrayUtil.isEmpty(strs)) {
			return true;
		}

		for (final CharSequence str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>是否包含空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者其中的任意一个元素是空字符串，则返回 true。</p>
	 *
	 * @param strs 字符串列表
	 * @return 是否包含空字符串
	 * @since 6.0.0
	 */
	public static boolean hasEmpty(final Iterable<? extends CharSequence> strs) {
		if (CollUtil.isEmpty(strs)) {
			return true;
		}

		for (final CharSequence str : strs) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isAllEmpty()                  // true}</li>
	 *     <li>{@code StrUtil.isAllEmpty("", null)          // true}</li>
	 *     <li>{@code StrUtil.isAllEmpty("123", "")         // false}</li>
	 *     <li>{@code StrUtil.isAllEmpty("123", "abc")      // false}</li>
	 *     <li>{@code StrUtil.isAllEmpty(" ", "\t", "\n")   // false}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #hasEmpty(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>{@link #hasEmpty(CharSequence...)}   等价于 {@code isEmpty(...) || isEmpty(...) || ...}</li>
	 *     <li>isAllEmpty(CharSequence...)          等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param strs 字符串列表
	 * @return 所有字符串是否为空白
	 */
	public static boolean isAllEmpty(final CharSequence... strs) {
		if (ArrayUtil.isNotEmpty(strs)) {
			for (final CharSequence str : strs) {
				if (isNotEmpty(str)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * <p>指定字符串数组中的元素，是否全部为空字符串。</p>
	 * <p>如果指定的字符串数组的长度为 0，或者所有元素都是空字符串，则返回 true。</p>
	 *
	 * @param strs 字符串列表
	 * @return 所有字符串是否为空白
	 */
	public static boolean isAllEmpty(final Iterable<? extends CharSequence> strs) {
		if (CollUtil.isNotEmpty(strs)) {
			for (final CharSequence str : strs) {
				if (isNotEmpty(str)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * <p>指定字符串数组中的元素，是否都不为空字符串。</p>
	 * <p>如果指定的字符串数组的长度不为 0，或者所有元素都不是空字符串，则返回 true。</p>
	 * <br>
	 *
	 * <p>例：</p>
	 * <ul>
	 *     <li>{@code StrUtil.isAllNotEmpty()                  // false}</li>
	 *     <li>{@code StrUtil.isAllNotEmpty("", null)          // false}</li>
	 *     <li>{@code StrUtil.isAllNotEmpty("123", "")         // false}</li>
	 *     <li>{@code StrUtil.isAllNotEmpty("123", "abc")      // true}</li>
	 *     <li>{@code StrUtil.isAllNotEmpty(" ", "\t", "\n")   // true}</li>
	 * </ul>
	 *
	 * <p>注意：该方法与 {@link #isAllEmpty(CharSequence...)} 的区别在于：</p>
	 * <ul>
	 *     <li>{@link #isAllEmpty(CharSequence...)}    等价于 {@code isEmpty(...) && isEmpty(...) && ...}</li>
	 *     <li>isAllNotEmpty(CharSequence...)          等价于 {@code !isEmpty(...) && !isEmpty(...) && ...}</li>
	 * </ul>
	 *
	 * @param args 字符串数组
	 * @return 所有字符串是否都不为为空白
	 * @since 5.3.6
	 */
	public static boolean isAllNotEmpty(final CharSequence... args) {
		return ! hasEmpty(args);
	}

	/**
	 * 是否存都不为{@code null}或空对象或空白符的对象，通过{@link #hasBlank(CharSequence...)} 判断元素
	 *
	 * @param args 被检查的对象,一个或者多个
	 * @return 是否都不为空
	 * @since 5.3.6
	 */
	public static boolean isAllNotBlank(final CharSequence... args) {
		return ! hasBlank(args);
	}

	/**
	 * 检查字符串是否为null、“null”、“undefined”
	 *
	 * @param str 被检查的字符串
	 * @return 是否为null、“null”、“undefined”
	 * @since 4.0.10
	 */
	public static boolean isNullOrUndefined(final CharSequence str) {
		if (null == str) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 检查字符串是否为null、“”、“null”、“undefined”
	 *
	 * @param str 被检查的字符串
	 * @return 是否为null、“”、“null”、“undefined”
	 * @since 4.0.10
	 */
	public static boolean isEmptyOrUndefined(final CharSequence str) {
		if (isEmpty(str)) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 检查字符串是否为null、空白串、“null”、“undefined”
	 *
	 * @param str 被检查的字符串
	 * @return 是否为null、空白串、“null”、“undefined”
	 * @since 4.0.10
	 */
	public static boolean isBlankOrUndefined(final CharSequence str) {
		if (isBlank(str)) {
			return true;
		}
		return isNullOrUndefinedStr(str);
	}

	/**
	 * 是否为“null”、“undefined”，不做空指针检查
	 *
	 * @param str 字符串
	 * @return 是否为“null”、“undefined”
	 */
	private static boolean isNullOrUndefinedStr(final CharSequence str) {
		final String strString = str.toString().trim();
		return NULL.equals(strString) || "undefined".equals(strString);
	}

	/**
	 * 字符串的每一个字符是否都与定义的匹配器匹配
	 *
	 * @param value   字符串
	 * @param matcher 匹配器
	 * @return 是否全部匹配
	 * @since 3.2.3
	 */
	public static boolean isAllCharMatch(final CharSequence value, final Predicate<Character> matcher) {
		if (isBlank(value)) {
			return false;
		}
		for (int i = value.length(); --i >= 0; ) {
			if (! matcher.test(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
