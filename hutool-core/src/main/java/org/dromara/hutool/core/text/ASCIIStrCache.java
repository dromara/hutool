/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.core.text;

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
