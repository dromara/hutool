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

package cn.hutool.core.text;

/**
 * ASCII字符对应的字符串缓存
 *
 * @author looly
 * @since 4.0.1
 *
 */
public class ASCIIStrCache {

	private static final int ASCII_LENGTH = 128;
	private static final String[] CACHE = new String[ASCII_LENGTH];
	static {
		for (char c = 0; c < ASCII_LENGTH; c++) {
			CACHE[c] = String.valueOf(c);
		}
	}

	/**
	 * 字符转为字符串<br>
	 * 如果为ASCII字符，使用缓存
	 *
	 * @param c 字符
	 * @return 字符串
	 */
	public static String toString(final char c) {
		return c < ASCII_LENGTH ? CACHE[c] : String.valueOf(c);
	}
}
