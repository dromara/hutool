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

import java.util.Arrays;

/**
 * 字符串或字符重复器<br>
 * 用于将给定字符串或字符赋值count次，然后拼接
 *
 * @author looly
 * @since 6.0.0
 */
public class StrRepeater {
	private final int countOrLength;

	/**
	 * 创建StrRepeater
	 *
	 * @param countOrLength 重复次数或固定长度
	 * @return StrRepeater
	 */
	public static StrRepeater of(final int countOrLength) {
		return new StrRepeater(countOrLength);
	}

	/**
	 * 构造
	 *
	 * @param countOrLength 重复次数或固定长度
	 */
	public StrRepeater(final int countOrLength) {
		this.countOrLength = countOrLength;
	}

	/**
	 * 重复某个字符
	 *
	 * <pre>
	 * repeat('e', 0)  = ""
	 * repeat('e', 3)  = "eee"
	 * repeat('e', -2) = ""
	 * </pre>
	 *
	 * @param c 被重复的字符
	 * @return 重复字符字符串
	 */
	public String repeat(final char c) {
		final int count = this.countOrLength;
		if (count <= 0) {
			return StrUtil.EMPTY;
		}

		final char[] result = new char[count];
		Arrays.fill(result, c);
		return new String(result);
	}

	/**
	 * 重复某个字符串
	 *
	 * @param str 被重复的字符
	 * @return 重复字符字符串
	 */
	public String repeat(final CharSequence str) {
		if (null == str) {
			return null;
		}

		final int count = this.countOrLength;
		if (count <= 0 || str.length() == 0) {
			return StrUtil.EMPTY;
		}
		if (count == 1) {
			return str.toString();
		}

		// 检查
		final int len = str.length();
		final long longSize = (long) len * (long) count;
		final int size = (int) longSize;
		if (size != longSize) {
			throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
		}

		// 相比使用StringBuilder更高效
		final char[] array = new char[size];
		str.toString().getChars(0, len, array, 0);
		int n;
		for (n = len; n < size - n; n <<= 1) {// n <<= 1相当于n *2
			System.arraycopy(array, 0, array, n, n);
		}
		System.arraycopy(array, 0, array, n, size - n);
		return new String(array);
	}

	/**
	 * 重复某个字符串到指定长度
	 * <ul>
	 *     <li>如果指定长度非指定字符串的整数倍，截断到固定长度</li>
	 *     <li>如果指定长度小于字符串本身的长度，截断之</li>
	 * </ul>
	 *
	 * @param str 被重复的字符
	 * @return 重复字符字符串
	 * @since 4.3.2
	 */
	public String repeatByLength(final CharSequence str) {
		if (null == str) {
			return null;
		}

		final int padLen = this.countOrLength;
		if (padLen <= 0) {
			return StrUtil.EMPTY;
		}
		final int strLen = str.length();
		if (strLen == padLen) {
			return str.toString();
		} else if (strLen > padLen) {
			return StrUtil.subPre(str, padLen);
		}

		// 重复，直到达到指定长度
		final char[] padding = new char[padLen];
		for (int i = 0; i < padLen; i++) {
			padding[i] = str.charAt(i % strLen);
		}
		return new String(padding);
	}

	/**
	 * 重复某个字符串并通过分界符连接
	 *
	 * <pre>
	 * repeatAndJoin("?", 5, ",")   = "?,?,?,?,?"
	 * repeatAndJoin("?", 0, ",")   = ""
	 * repeatAndJoin("?", 5, null) = "?????"
	 * </pre>
	 *
	 * @param str       被重复的字符串
	 * @param delimiter 分界符
	 * @return 连接后的字符串
	 * @since 4.0.1
	 */
	public String repeatAndJoin(final CharSequence str, final CharSequence delimiter) {
		int count = this.countOrLength;
		if (count <= 0) {
			return StrUtil.EMPTY;
		}
		if(StrUtil.isEmpty(delimiter)){
			return repeat(str);
		}

		// 初始大小 = 所有重复字符串长度 + 分界符总长度
		final StringBuilder builder = new StringBuilder(
				str.length() * count + delimiter.length() * (count - 1));
		builder.append(str);
		count--;

		while (count-- > 0) {
			builder.append(delimiter).append(str);
		}
		return builder.toString();
	}
}
