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

package org.dromara.hutool.core.codec;

import org.dromara.hutool.core.regex.PatternPool;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * Luhn算法，也称为“模10”算法，是一种简单的校验和（Checksum）算法，在ISO/IEC 7812-1中定义，校验步骤如下：
 * <ol>
 *     <li>从右边第1个数字（校验数字）开始偶数位乘以2，如果小于10，直接返回，否则将个位数和十位数相加</li>
 *     <li>把步骤1种获得的乘积的各位数字与原号码中未乘2的各位数字相加</li>
 *     <li>如果步骤2得到的总和模10为0，则校验通过</li>
 * </ol>
 */
public class Luhn {

	/**
	 * 校验字符串
	 *
	 * @param strWithCheckDigit 含校验数字的字符串
	 * @return true - 校验通过，false-校验不通过
	 * @throws IllegalArgumentException 如果字符串为空或不是8~19位的数字
	 */
	public static boolean check(final String strWithCheckDigit) {
		if (StrUtil.isBlank(strWithCheckDigit)) {
			return false;
		}
		if (!ReUtil.isMatch(PatternPool.NUMBERS, strWithCheckDigit)) {
			// 必须为全数字
			return false;
		}
		return sum(strWithCheckDigit) % 10 == 0;
	}

	/**
	 * 计算校验位数字<br>
	 * 忽略已有的校验位数字，根据前N位计算最后一位校验位数字
	 *
	 * @param str            被检查的数字
	 * @param withCheckDigit 是否含有校验位
	 * @return 校验位数字
	 */
	public static int getCheckDigit(String str, final boolean withCheckDigit) {
		if (withCheckDigit) {
			str = str.substring(0, str.length() - 1);
		}
		return 10 - (sum(str + "0") % 10);
	}

	/**
	 * 根据Luhn算法计算字符串各位数字之和
	 *
	 * @param str 需要校验的数字字符串
	 * @return 数字之和
	 */
	private static int sum(final String str) {
		final char[] strArray = str.toCharArray();
		final int n = strArray.length;
		int sum = strArray[n - 1] - '0';
		;
		for (int i = 2; i <= n; i++) {
			int a = strArray[n - i] - '0';
			// 偶数位乘以2
			if ((i & 1) == 0) {
				a *= 2;
			}
			// 十位数和个位数相加，如果不是偶数位，不乘以2，则十位数为0
			sum += a / 10 + a % 10;
		}
		return sum;
	}
}
