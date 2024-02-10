/*
 * Copyright (c) 2023-2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.codec.binary;

import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.awt.Color;
import java.math.BigInteger;

/**
 * 十六进制（简写为hex或下标16）在数学中是一种逢16进1的进位制，一般用数字0到9和字母A到F表示（其中:A~F即10~15）。<br>
 * 例如十进制数57，在二进制写作111001，在16进制写作39。<br>
 * 像java,c这样的语言为了区分十六进制和十进制数值,会在十六进制数的前面加上 0x,比如0x20是十进制的32,而不是十进制的20<br>
 * <p>
 *
 * 此工具类为16进制组合工具类，除了继承{@link Hex}实现编码解码外，提供其它转换类和识别类工具。
 *
 * @author Looly
 * @see Hex
 */
public class HexUtil extends Hex{
	// region ----- Color

	/**
	 * 将{@link Color}编码为Hex形式
	 *
	 * @param color {@link Color}
	 * @return Hex字符串
	 * @since 3.0.8
	 */
	public static String encodeColor(final Color color) {
		return encodeColor(color, "#");
	}

	/**
	 * 将{@link Color}编码为Hex形式
	 *
	 * @param color  {@link Color}
	 * @param prefix 前缀字符串，可以是#、0x等
	 * @return Hex字符串
	 * @since 3.0.8
	 */
	public static String encodeColor(final Color color, final String prefix) {
		final StringBuilder builder = new StringBuilder(prefix);
		String colorHex;
		colorHex = Integer.toHexString(color.getRed());
		if (1 == colorHex.length()) {
			builder.append('0');
		}
		builder.append(colorHex);
		colorHex = Integer.toHexString(color.getGreen());
		if (1 == colorHex.length()) {
			builder.append('0');
		}
		builder.append(colorHex);
		colorHex = Integer.toHexString(color.getBlue());
		if (1 == colorHex.length()) {
			builder.append('0');
		}
		builder.append(colorHex);
		return builder.toString();
	}

	/**
	 * 将Hex颜色值转为
	 *
	 * @param hexColor 16进制颜色值，可以以#开头，也可以用0x开头
	 * @return {@link Color}
	 * @since 3.0.8
	 */
	public static Color decodeColor(final String hexColor) {
		return Color.decode(hexColor);
	}

	// endregion

	/**
	 * 判断给定字符串是否为16进制数<br>
	 * 如果是，需要使用对应数字类型对象的{@code decode}方法解码<br>
	 * 例如：{@code Integer.decode}方法解码int类型的16进制数字
	 *
	 * @param value 值
	 * @return 是否为16进制
	 */
	public static boolean isHexNumber(final String value) {
		if (StrUtil.startWith(value, '-')) {
			// issue#2875
			return false;
		}
		int index = 0;
		if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
			index += 2;
		} else if (value.startsWith("#", index)) {
			index++;
		}
		try {
			new BigInteger(value.substring(index), 16);
		} catch (final NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * 将指定int值转换为Unicode字符串形式，常用于特殊字符（例如汉字）转Unicode形式<br>
	 * 转换的字符串如果u后不足4位，则前面用0填充，例如：
	 *
	 * <pre>
	 * 你 =》 &#92;u4f60
	 * </pre>
	 *
	 * @param value int值，也可以是char
	 * @return Unicode表现形式
	 */
	public static String toUnicodeHex(final int value) {
		final StringBuilder builder = new StringBuilder(6);

		builder.append("\\u");
		final String hex = toHex(value);
		final int len = hex.length();
		if (len < 4) {
			builder.append("0000", 0, 4 - len);// 不足4位补0
		}
		builder.append(hex);

		return builder.toString();
	}

	/**
	 * 将指定char值转换为Unicode字符串形式，常用于特殊字符（例如汉字）转Unicode形式<br>
	 * 转换的字符串如果u后不足4位，则前面用0填充，例如：
	 *
	 * <pre>
	 * 你 =》 &#92;u4f60
	 * </pre>
	 *
	 * @param ch char值
	 * @return Unicode表现形式
	 * @since 4.0.1
	 */
	public static String toUnicodeHex(final char ch) {
		return Base16Codec.CODEC_LOWER.toUnicodeHex(ch);
	}

	/**
	 * 转为16进制字符串
	 *
	 * @param value int值
	 * @return 16进制字符串
	 * @since 4.4.1
	 */
	public static String toHex(final int value) {
		return Integer.toHexString(value);
	}

	/**
	 * 16进制字符串转为int
	 *
	 * @param value 16进制字符串
	 * @return 16进制字符串int值
	 * @since 5.7.4
	 */
	public static int hexToInt(final String value) {
		return Integer.parseInt(value, 16);
	}

	/**
	 * 转为16进制字符串
	 *
	 * @param value int值
	 * @return 16进制字符串
	 * @since 4.4.1
	 */
	public static String toHex(final long value) {
		return Long.toHexString(value);
	}

	/**
	 * 16进制字符串转为long
	 *
	 * @param value 16进制字符串
	 * @return long值
	 * @since 5.7.4
	 */
	public static long hexToLong(final String value) {
		return Long.parseLong(value, 16);
	}

	/**
	 * 将byte值转为16进制并添加到{@link StringBuilder}中
	 *
	 * @param builder     {@link StringBuilder}
	 * @param b           byte
	 * @param toLowerCase 是否使用小写
	 * @since 4.4.1
	 */
	public static void appendHex(final StringBuilder builder, final byte b, final boolean toLowerCase) {
		(toLowerCase ? Base16Codec.CODEC_LOWER : Base16Codec.CODEC_UPPER).appendHex(builder, b);
	}

	/**
	 * Hex（16进制）字符串转为BigInteger
	 *
	 * @param hexStr Hex(16进制字符串)
	 * @return {@link BigInteger}
	 * @since 5.2.0
	 */
	public static BigInteger toBigInteger(final String hexStr) {
		if (null == hexStr) {
			return null;
		}
		return new BigInteger(hexStr, 16);
	}

	/**
	 * 格式化Hex字符串，结果为每2位加一个空格，类似于：
	 * <pre>
	 *     e8 8c 67 03 80 cb 22 00 95 26 8f
	 * </pre>
	 *
	 * @param hexStr Hex字符串
	 * @return 格式化后的字符串
	 */
	public static String format(final String hexStr) {
		final int length = hexStr.length();
		final StringBuilder builder = StrUtil.builder(length + length / 2);
		builder.append(hexStr.charAt(0)).append(hexStr.charAt(1));
		for (int i = 2; i < length - 1; i += 2) {
			builder.append(CharUtil.SPACE).append(hexStr.charAt(i)).append(hexStr.charAt(i + 1));
		}
		return builder.toString();
	}

}
