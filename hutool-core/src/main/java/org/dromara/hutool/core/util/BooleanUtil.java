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

package org.dromara.hutool.core.util;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Set;

/**
 * Boolean类型相关工具类
 *
 * @author looly
 * @since 4.1.16
 */
public class BooleanUtil {

	/** 表示为真的字符串 */
	private static final Set<String> TRUE_SET = SetUtil.of("true", "yes", "y", "t", "ok", "1", "on", "是", "对", "真", "對", "√");
	/** 表示为假的字符串 */
	private static final Set<String> FALSE_SET = SetUtil.of("false", "no", "n", "f", "0", "off", "否", "错", "假", "錯", "×");

	/**
	 * 取相反值
	 *
	 * @param bool Boolean值
	 * @return 相反的Boolean值，如果传入 {@code null} 则返回 {@code null}
	 */
	public static Boolean negate(final Boolean bool) {
		if (bool == null) {
			return null;
		}
		return bool ? Boolean.FALSE : Boolean.TRUE;
	}

	/**
	 * 检查 {@code Boolean} 值是否为 {@code true}
	 *
	 * <pre>
	 *   BooleanUtil.isTrue(Boolean.TRUE)  = true
	 *   BooleanUtil.isTrue(Boolean.FALSE) = false
	 *   BooleanUtil.isTrue(null)          = false
	 * </pre>
	 *
	 * @param bool 被检查的Boolean值
	 * @return 当值非 {@code null} 且为 {@code true} 时返回 {@code true}
	 */
	public static boolean isTrue(final Boolean bool) {
		return Boolean.TRUE.equals(bool);
	}

	/**
	 * 检查 {@code Boolean} 值是否为 {@code false}
	 *
	 * <pre>
	 *   BooleanUtil.isFalse(Boolean.TRUE)  = false
	 *   BooleanUtil.isFalse(Boolean.FALSE) = true
	 *   BooleanUtil.isFalse(null)          = false
	 * </pre>
	 *
	 * @param bool 被检查的Boolean值
	 * @return 当值非 {@code null} 且为 {@code false} 时返回 {@code true}
	 */
	public static boolean isFalse(final Boolean bool) {
		return Boolean.FALSE.equals(bool);
	}

	/**
	 * 取相反值
	 *
	 * @param bool boolean值
	 * @return 相反的boolean值
	 */
	public static boolean negate(final boolean bool) {
		return !bool;
	}

	/**
	 * 转换字符串为boolean值
	 * <p>该字符串 是否在 {@link #TRUE_SET} 中，存在则为 {@code true}，否则为 {@code false}</p>
	 *
	 * @param valueStr 字符串，不区分大小写，前后可以有空格 {@link String#trim()}
	 * @return boolean值
	 */
	public static boolean toBoolean(final String valueStr) {
		if (StrUtil.isNotBlank(valueStr)) {
			return TRUE_SET.contains(valueStr.trim().toLowerCase());
		}
		return false;
	}

	/**
	 * 转换字符串为Boolean值<br>
	 * 如果字符串在 {@link #TRUE_SET} 中，返回 {@link Boolean#TRUE}<br>
	 * 如果字符串在 {@link #FALSE_SET} 中，返回 {@link Boolean#FALSE}<br>
	 * 其他情况返回{@code null}
	 *
	 * @param valueStr 字符串，不区分大小写，前后可以有空格 {@link String#trim()}
	 * @return boolean值
	 * @since 5.8.1
	 */
	public static Boolean toBooleanObject(String valueStr) {
		if (StrUtil.isNotBlank(valueStr)) {
			valueStr = valueStr.trim().toLowerCase();
			if (TRUE_SET.contains(valueStr)) {
				return Boolean.TRUE;
			} else if (FALSE_SET.contains(valueStr)) {
				return Boolean.FALSE;
			}
		}
		return null;
	}

	/**
	 * boolean值转为int
	 *
	 * @param value boolean值
	 * @return int值
	 */
	public static int toInt(final boolean value) {
		return value ? 1 : 0;
	}

	/**
	 * boolean值转为Integer
	 *
	 * @param value boolean值
	 * @return Integer值
	 */
	public static Integer toInteger(final boolean value) {
		return toInt(value);
	}

	/**
	 * boolean值转为char
	 *
	 * @param value boolean值
	 * @return char值
	 */
	public static char toChar(final boolean value) {
		return (char) toInt(value);
	}

	/**
	 * boolean值转为Character
	 *
	 * @param value boolean值
	 * @return Character值
	 */
	public static Character toCharacter(final boolean value) {
		return toChar(value);
	}

	/**
	 * boolean值转为byte
	 *
	 * @param value boolean值
	 * @return byte值
	 */
	public static byte toByte(final boolean value) {
		return (byte) toInt(value);
	}

	/**
	 * boolean值转为Byte
	 *
	 * @param value boolean值
	 * @return Byte值
	 */
	public static Byte toByteObj(final boolean value) {
		return toByte(value);
	}

	/**
	 * boolean值转为long
	 *
	 * @param value boolean值
	 * @return long值
	 */
	public static long toLong(final boolean value) {
		return toInt(value);
	}

	/**
	 * boolean值转为Long
	 *
	 * @param value boolean值
	 * @return Long值
	 */
	public static Long toLongObj(final boolean value) {
		return toLong(value);
	}

	/**
	 * boolean值转为short
	 *
	 * @param value boolean值
	 * @return short值
	 */
	public static short toShort(final boolean value) {
		return (short) toInt(value);
	}

	/**
	 * boolean值转为Short
	 *
	 * @param value boolean值
	 * @return Short值
	 */
	public static Short toShortObj(final boolean value) {
		return toShort(value);
	}

	/**
	 * boolean值转为float
	 *
	 * @param value boolean值
	 * @return float值
	 */
	public static float toFloat(final boolean value) {
		return (float) toInt(value);
	}

	/**
	 * boolean值转为Float
	 *
	 * @param value boolean值
	 * @return Float值
	 */
	public static Float toFloatObj(final boolean value) {
		return toFloat(value);
	}

	/**
	 * boolean值转为double
	 *
	 * @param value boolean值
	 * @return double值
	 */
	public static double toDouble(final boolean value) {
		return toInt(value);
	}

	/**
	 * boolean值转为Double
	 *
	 * @param value boolean值
	 * @return Double值
	 */
	public static Double toDoubleObj(final boolean value) {
		return toDouble(value);
	}

	/**
	 * 将boolean转换为字符串 {@code 'true'} 或者 {@code 'false'}.
	 *
	 * <pre>
	 *   BooleanUtil.toStringTrueFalse(true)   = "true"
	 *   BooleanUtil.toStringTrueFalse(false)  = "false"
	 * </pre>
	 *
	 * @param bool boolean值
	 * @return {@code 'true'}, {@code 'false'}
	 */
	public static String toStringTrueFalse(final boolean bool) {
		return toString(bool, "true", "false");
	}

	/**
	 * 将boolean转换为字符串 {@code 'on'} 或者 {@code 'off'}.
	 *
	 * <pre>
	 *   BooleanUtil.toStringOnOff(true)   = "on"
	 *   BooleanUtil.toStringOnOff(false)  = "off"
	 * </pre>
	 *
	 * @param bool boolean值
	 * @return {@code 'on'}, {@code 'off'}
	 */
	public static String toStringOnOff(final boolean bool) {
		return toString(bool, "on", "off");
	}

	/**
	 * 将boolean转换为字符串 {@code 'yes'} 或者 {@code 'no'}.
	 *
	 * <pre>
	 *   BooleanUtil.toStringYesNo(true)   = "yes"
	 *   BooleanUtil.toStringYesNo(false)  = "no"
	 * </pre>
	 *
	 * @param bool boolean值
	 * @return {@code 'yes'}, {@code 'no'}
	 */
	public static String toStringYesNo(final boolean bool) {
		return toString(bool, "yes", "no");
	}

	/**
	 * 将boolean转换为字符串
	 *
	 * <pre>
	 *   BooleanUtil.toString(true, "true", "false")   = "true"
	 *   BooleanUtil.toString(false, "true", "false")  = "false"
	 * </pre>
	 *
	 * @param bool boolean值
	 * @param trueString 当值为 {@code true}时返回此字符串, 可能为 {@code null}
	 * @param falseString 当值为 {@code false}时返回此字符串, 可能为 {@code null}
	 * @return 结果值
	 */
	public static String toString(final boolean bool, final String trueString, final String falseString) {
		return bool ? trueString : falseString;
	}

	/**
	 * 将boolean转换为字符串
	 *
	 * <pre>
	 *   BooleanUtil.toString(true, "true", "false", null) = "true"
	 *   BooleanUtil.toString(false, "true", "false", null) = "false"
	 *   BooleanUtil.toString(null, "true", "false", null) = null
	 * </pre>
	 *
	 * @param bool Boolean值
	 * @param trueString 当值为 {@code true}时返回此字符串, 可能为 {@code null}
	 * @param falseString 当值为 {@code false}时返回此字符串, 可能为 {@code null}
	 * @param nullString 当值为 {@code null}时返回此字符串, 可能为 {@code null}
	 * @return 结果值
	 */
	public static String toString(final Boolean bool, final String trueString, final String falseString, final String nullString) {
		if (bool == null) {
			return nullString;
		}
		return bool ? trueString : falseString;
	}

	/**
	 * boolean数组所有元素相 与 的结果
	 *
	 * <pre>
	 *   BooleanUtil.and(true, true)         = true
	 *   BooleanUtil.and(false, false)       = false
	 *   BooleanUtil.and(true, false)        = false
	 *   BooleanUtil.and(true, true, false)  = false
	 *   BooleanUtil.and(true, true, true)   = true
	 * </pre>
	 *
	 * @param array {@code boolean}数组
	 * @return 数组所有元素相 与 的结果
	 * @throws IllegalArgumentException 如果数组为空
	 */
	public static boolean and(final boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		for (final boolean element : array) {
			if (!element) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Boolean数组所有元素相 与 的结果
	 * <p>注意：{@code null} 元素 被当作 {@code true}</p>
	 *
	 * <pre>
	 *   BooleanUtil.and(Boolean.TRUE, Boolean.TRUE)                 = Boolean.TRUE
	 *   BooleanUtil.and(Boolean.FALSE, Boolean.FALSE)               = Boolean.FALSE
	 *   BooleanUtil.and(Boolean.TRUE, Boolean.FALSE)                = Boolean.FALSE
	 *   BooleanUtil.and(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)   = Boolean.TRUE
	 *   BooleanUtil.and(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE) = Boolean.FALSE
	 *   BooleanUtil.and(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)  = Boolean.FALSE
	 *   BooleanUtil.and(Boolean.TRUE, null)                         = Boolean.TRUE
	 * </pre>
	 *
	 * @param array {@code Boolean}数组
	 * @return 数组所有元素相 与 的结果
	 * @throws IllegalArgumentException 如果数组为空
	 */
	public static Boolean andOfWrap(final Boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}

		for (final Boolean b : array) {
			if(isFalse(b)){
				return false;
			}
		}
		return true;
	}

	/**
	 * boolean数组所有元素 或 的结果
	 *
	 * <pre>
	 *   BooleanUtil.or(true, true)          = true
	 *   BooleanUtil.or(false, false)        = false
	 *   BooleanUtil.or(true, false)         = true
	 *   BooleanUtil.or(true, true, false)   = true
	 *   BooleanUtil.or(true, true, true)    = true
	 *   BooleanUtil.or(false, false, false) = false
	 * </pre>
	 *
	 * @param array {@code boolean}数组
	 * @return 数组所有元素 或 的结果
	 * @throws IllegalArgumentException 如果数组为空
	 */
	public static boolean or(final boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}
		for (final boolean element : array) {
			if (element) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Boolean数组所有元素 或 的结果
	 * <p>注意：{@code null} 元素 被当作 {@code false}</p>
	 *
	 * <pre>
	 *   BooleanUtil.or(Boolean.TRUE, Boolean.TRUE)                  = Boolean.TRUE
	 *   BooleanUtil.or(Boolean.FALSE, Boolean.FALSE)                = Boolean.FALSE
	 *   BooleanUtil.or(Boolean.TRUE, Boolean.FALSE)                 = Boolean.TRUE
	 *   BooleanUtil.or(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)    = Boolean.TRUE
	 *   BooleanUtil.or(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE)  = Boolean.TRUE
	 *   BooleanUtil.or(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE)   = Boolean.TRUE
	 *   BooleanUtil.or(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE) = Boolean.FALSE
	 *   BooleanUtil.or(Boolean.FALSE, null)                         = Boolean.FALSE
	 * </pre>
	 *
	 * @param array {@code Boolean}数组
	 * @return 数组所有元素 或 的结果
	 * @throws IllegalArgumentException 如果数组为空
	 */
	public static Boolean orOfWrap(final Boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}

		for (final Boolean b : array) {
			if(isTrue(b)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 对boolean数组取异或
	 *
	 * <pre>
	 *   BooleanUtil.xor(true, true)   = false
	 *   BooleanUtil.xor(false, false) = false
	 *   BooleanUtil.xor(true, false)  = true
	 *   BooleanUtil.xor(true, true, true)   = true
	 *   BooleanUtil.xor(false, false, false) = false
	 *   BooleanUtil.xor(true, true, false)  = false
	 *   BooleanUtil.xor(true, false, false)  = true
	 * </pre>
	 *
	 * @param array {@code boolean}数组
	 * @return 如果异或计算为true返回 {@code true}
	 * @throws IllegalArgumentException 如果数组为空
	 */
	public static boolean xor(final boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty");
		}

		boolean result = false;
		for (final boolean element : array) {
			result ^= element;
		}

		return result;
	}

	/**
	 * 对Boolean数组取异或
	 *
	 * <pre>
	 *   BooleanUtil.xor(Boolean.TRUE, Boolean.TRUE)                  = Boolean.FALSE
	 *   BooleanUtil.xor(Boolean.FALSE, Boolean.FALSE)                = Boolean.FALSE
	 *   BooleanUtil.xor(Boolean.TRUE, Boolean.FALSE)                 = Boolean.TRUE
	 *   BooleanUtil.xor(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE)    = Boolean.TRUE
	 *   BooleanUtil.xor(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE) = Boolean.FALSE
	 *   BooleanUtil.xor(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE)   = Boolean.FALSE
	 *   BooleanUtil.xor(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE)  = Boolean.TRUE
	 * </pre>
	 *
	 * @param array {@code Boolean} 数组
	 * @return 异或为真取 {@code true}
	 * @throws IllegalArgumentException 如果数组为空
	 * @see #xor(boolean...)
	 */
	public static Boolean xorOfWrap(final Boolean... array) {
		if (ArrayUtil.isEmpty(array)) {
			throw new IllegalArgumentException("The Array must not be empty !");
		}

		boolean result = false;
		for (final Boolean element : array) {
			result ^= element;
		}

		return result;
	}

	/**
	 * 给定类是否为Boolean或者boolean
	 *
	 * @param clazz 类
	 * @return 是否为Boolean或者boolean
	 * @since 4.5.2
	 */
	public static boolean isBoolean(final Class<?> clazz) {
		return (clazz == Boolean.class || clazz == boolean.class);
	}
}
