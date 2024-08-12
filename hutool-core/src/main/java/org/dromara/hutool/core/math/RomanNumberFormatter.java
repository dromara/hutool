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

package org.dromara.hutool.core.math;

import org.dromara.hutool.core.text.StrUtil;

/**
 * 数字和罗马数字转换
 *
 * @author dazer
 * @since 6.0.0
 */
public class RomanNumberFormatter {

	/**
	 * 整数转罗马数字<br>
	 * 限制：[1,3999]的正整数
	 * <ul>
	 *     <li>I 1</li>
	 *     <li>V 5</li>
	 *     <li>X 10</li>
	 *     <li>L 50</li>
	 *     <li>C 100</li>
	 *     <li>D 500</li>
	 *     <li>M 1000</li>
	 * </ul>
	 *
	 * @param num [1,3999]的正整数
	 * @return 罗马数字
	 * @author dazer
	 * @since 6.0.0
	 */
	public static String intToRoman(final int num) {
		if (num > 3999 || num < 1) {
			return StrUtil.EMPTY;
		}
		final String[] thousands = {"", "M", "MM", "MMM"};
		final String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
		final String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
		final String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

		return thousands[num / 1000] +
			hundreds[(num % 1000) / 100] +
			tens[(num % 100) / 10] +
			ones[num % 10];
	}

	/**
	 * 罗马数字转整数<br>
	 *
	 * @param roman 罗马字符
	 * @return 整数
	 * @throws IllegalArgumentException 如果传入非罗马字符串，抛出异常
	 * @author dazer
	 * @since 6.0.0
	 */
	public static int romanToInt(final String roman) {
		int result = 0;
		int prevValue = 0;
		int currValue;

		for (int i = roman.length() - 1; i >= 0; i--) {
			final char c = roman.charAt(i);
			switch (c) {
				case 'I':
					currValue = 1;
					break;
				case 'V':
					currValue = 5;
					break;
				case 'X':
					currValue = 10;
					break;
				case 'L':
					currValue = 50;
					break;
				case 'C':
					currValue = 100;
					break;
				case 'D':
					currValue = 500;
					break;
				case 'M':
					currValue = 1000;
					break;
				default:
					throw new IllegalArgumentException("Invalid Roman character: " + c);
			}
			if (currValue < prevValue) {
				result -= currValue;
			} else {
				result += currValue;
			}

			prevValue = currValue;
		}
		return result;
	}
}
