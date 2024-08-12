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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 中文数字或金额解析类
 *
 * @author fanqun, looly
 * @since 6.0.0
 */
public class ChineseNumberParser {

	/**
	 * 汉字转阿拉伯数字的
	 */
	private static final ChineseUnit[] CHINESE_NAME_VALUE = {
		new ChineseUnit(' ', 1, false),
		new ChineseUnit('十', 10, false),
		new ChineseUnit('拾', 10, false),
		new ChineseUnit('百', 100, false),
		new ChineseUnit('佰', 100, false),
		new ChineseUnit('千', 1000, false),
		new ChineseUnit('仟', 1000, false),
		new ChineseUnit('万', 1_0000, true),
		new ChineseUnit('亿', 1_0000_0000, true),
	};

	/**
	 * 把中文转换为数字 如 二百二十 -》 220<br>
	 * <ul>
	 *     <li>一百一十二 -》 112</li>
	 *     <li>一千零一十二 -》 1012</li>
	 * </ul>
	 *
	 * @param chinese 中文字符
	 * @return 数字
	 * @since 5.6.0
	 */
	public static BigDecimal parseFromChinese(final String chinese) {
		if (StrUtil.containsAny(chinese, '元', '圆', '角', '分')) {
			return parseFromChineseMoney(chinese);
		}

		return parseFromChineseNumber(chinese);
	}

	/**
	 * 把中文转换为数字<br>
	 * <ul>
	 *     <li>一百一十二 -》 112</li>
	 *     <li>一千零一十二 -》 1012</li>
	 *     <li>十二点二三 -》 12.23</li>
	 *     <li>三点一四一五九二六五四 -》 3.141592654</li>
	 * </ul>
	 *
	 * @param chinese 中文字符
	 * @return 数字
	 * @since 5.6.0
	 */
	public static BigDecimal parseFromChineseNumber(final String chinese) {
		Assert.notBlank(chinese, "Chinese number is blank!");
		final int dotIndex = chinese.indexOf('点');

		// 整数部分
		BigDecimal result = NumberUtil.toBigDecimal(parseLongFromChineseNumber(chinese, dotIndex > 0 ? dotIndex : chinese.length()));

		// 小数部分
		if (dotIndex > 0) {
			final int length = chinese.length();
			for (int i = dotIndex + 1; i < length; i++) {
				// 保留位数取决于实际数字的位数
				// result + (numberChar / 10^(i-dotIndex))
				result = result.add(NumberUtil.div(chineseToNumber(chinese.charAt(i)), BigDecimal.TEN.pow(i-dotIndex), (length - dotIndex + 1)));
			}
		}

		return result.stripTrailingZeros();
	}

	/**
	 * 中文大写数字金额转换为数字，返回结果以元为单位的BigDecimal类型数字<br>
	 * 如：
	 * “陆万柒仟伍佰伍拾陆元叁角贰分”返回“67556.32”
	 * “叁角贰分”返回“0.32”
	 *
	 * @param chineseMoneyAmount 中文大写数字金额
	 * @return 返回结果以元为单位的BigDecimal类型数字
	 */
	public static BigDecimal parseFromChineseMoney(final String chineseMoneyAmount) {
		if (StrUtil.isBlank(chineseMoneyAmount)) {
			return null;
		}

		int yi = chineseMoneyAmount.indexOf("元");
		if (yi == -1) {
			yi = chineseMoneyAmount.indexOf("圆");
		}
		final int ji = chineseMoneyAmount.indexOf("角");
		final int fi = chineseMoneyAmount.indexOf("分");

		// 先找到单位为元的数字
		String yStr = null;
		if (yi > 0) {
			yStr = chineseMoneyAmount.substring(0, yi);
		}

		// 再找到单位为角的数字
		String jStr = null;
		if (ji > 0) {
			if (yi >= 0) {
				//前面有元,角肯定要在元后面
				if (ji > yi) {
					jStr = chineseMoneyAmount.substring(yi + 1, ji);
				}
			} else {
				//没有元，只有角
				jStr = chineseMoneyAmount.substring(0, ji);
			}
		}

		// 再找到单位为分的数字
		String fStr = null;
		if (fi > 0) {
			if (ji >= 0) {
				//有角，分肯定在角后面
				if (fi > ji) {
					fStr = chineseMoneyAmount.substring(ji + 1, fi);
				}
			} else if (yi > 0) {
				//没有角，有元，那就坐元后面找
				if (fi > yi) {
					fStr = chineseMoneyAmount.substring(yi + 1, fi);
				}
			} else {
				//没有元、角，只有分
				fStr = chineseMoneyAmount.substring(0, fi);
			}
		}

		//元、角、分
		long y = 0, j = 0, f = 0;
		if (StrUtil.isNotBlank(yStr)) {
			y = parseLongFromChineseNumber(yStr, yStr.length());
		}
		if (StrUtil.isNotBlank(jStr)) {
			j = parseLongFromChineseNumber(jStr, jStr.length());
		}
		if (StrUtil.isNotBlank(fStr)) {
			f = parseLongFromChineseNumber(fStr, fStr.length());
		}

		BigDecimal amount = new BigDecimal(y);
		amount = amount.add(BigDecimal.valueOf(j).divide(BigDecimal.TEN, 2, RoundingMode.HALF_UP));
		amount = amount.add(BigDecimal.valueOf(f).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
		return amount;
	}

	/**
	 * 把中文整数转换为数字 如 二百二十 220<br>
	 * <ul>
	 *     <li>一百一十二 -》 112</li>
	 *     <li>一千零一十二 -》 1012</li>
	 * </ul>
	 *
	 * @param chinese 中文字符
	 * @param toIndex 结束位置（不包括），如果提供的是整数，这个为length()，小数则是“点”的位置
	 * @return 数字
	 */
	public static long parseLongFromChineseNumber(final String chinese, final int toIndex) {
		long result = 0;

		// 节总和
		long section = 0;
		long number = 0;
		ChineseUnit unit = null;
		char c;
		for (int i = 0; i < toIndex; i++) {
			c = chinese.charAt(i);
			final int num = chineseToNumber(c);
			if (num >= 0) {
				if (num == 0) {
					// 遇到零时节结束，权位失效，比如两万二零一十
					if (number > 0 && null != unit) {
						section += number * (unit.value / 10);
					}
					unit = null;
				} else if (number > 0) {
					// 多个数字同时出现，报错
					throw new IllegalArgumentException(StrUtil.format("Bad number '{}{}' at: {}", chinese.charAt(i - 1), c, i));
				}
				// 普通数字
				number = num;
			} else {
				unit = chineseToUnit(c);
				if (null == unit) {
					// 出现非法字符
					throw new IllegalArgumentException(StrUtil.format("Unknown unit '{}' at: {}", c, i));
				}

				//单位
				if (unit.secUnit) {
					// 节单位，按照节求和
					section = (section + number) * unit.value;
					result += section;
					section = 0;
				} else {
					// 非节单位，和单位前的单数字组合为值
					long unitNumber = number;
					if (0 == number && 0 == i) {
						// issue#1726，对于单位开头的数组，默认赋予1
						// 十二 -> 一十二
						// 百二 -> 一百二
						unitNumber = 1;
					}
					section += (unitNumber * unit.value);
				}
				number = 0;
			}
		}

		if (number > 0 && null != unit) {
			number = number * (unit.value / 10);
		}

		return result + section + number;
	}

	/**
	 * 查找对应的权对象
	 *
	 * @param chinese 中文权位名
	 * @return 权对象
	 */
	private static ChineseUnit chineseToUnit(final char chinese) {
		for (final ChineseUnit chineseNameValue : CHINESE_NAME_VALUE) {
			if (chineseNameValue.name == chinese) {
				return chineseNameValue;
			}
		}
		return null;
	}

	/**
	 * 将汉字单个数字转换为int类型数字
	 *
	 * @param chinese 汉字数字，支持简体和繁体
	 * @return 数字，-1表示未找到
	 * @since 5.6.4
	 */
	private static int chineseToNumber(char chinese) {
		if ('两' == chinese) {
			// 口语纠正
			chinese = '二';
		}
		final int i = ArrayUtil.indexOf(ChineseNumberFormatter.DIGITS, chinese);
		if (i > 0) {
			return (i + 1) / 2;
		}
		return i;
	}

	/**
	 * 获取对应级别的单位
	 *
	 * @param index            级别，0表示各位，1表示十位，2表示百位，以此类推
	 * @param isUseTraditional 是否使用繁体
	 * @return 单位
	 */
	static String getUnitName(final int index, final boolean isUseTraditional) {
		if (0 == index) {
			return StrUtil.EMPTY;
		}
		return String.valueOf(CHINESE_NAME_VALUE[index * 2 - (isUseTraditional ? 0 : 1)].name);
	}

	/**
	 * 权位
	 *
	 * @author totalo
	 * @since 5.6.0
	 */
	private static class ChineseUnit {
		/**
		 * 中文权名称
		 */
		private final char name;
		/**
		 * 10的倍数值
		 */
		private final int value;
		/**
		 * 是否为节权位，它不是与之相邻的数字的倍数，而是整个小节的倍数。<br>
		 * 例如二十三万，万是节权位，与三无关，而和二十三关联
		 */
		private final boolean secUnit;

		/**
		 * 构造
		 *
		 * @param name    名称
		 * @param value   值，即10的倍数
		 * @param secUnit 是否为节权位
		 */
		public ChineseUnit(final char name, final int value, final boolean secUnit) {
			this.name = name;
			this.value = value;
			this.secUnit = secUnit;
		}
	}
}
