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

package org.dromara.hutool.core.math;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.StrUtil;

/**
 * 数字转中文类<br>
 * 包括：
 * <pre>
 * 1. 数字转中文大写形式，比如一百二十一
 * 2. 数字转金额用的大写形式，比如：壹佰贰拾壹
 * 3. 转金额形式，比如：壹佰贰拾壹整
 * </pre>
 *
 * @author fanqun, looly
 **/
public class ChineseNumberFormatter {

	/**
	 * 阿拉伯数字（支持正负整数）四舍五入后转换成中文节权位简洁计数单位，例如 -5_5555 =》 -5.56万
	 *
	 * @param amount 数字
	 * @return 中文
	 */
	public static String formatSimple(final long amount) {
		if (amount < 1_0000 && amount > -1_0000) {
			return String.valueOf(amount);
		}
		final String res;
		if (amount < 1_0000_0000 && amount > -1_0000_0000) {
			res = NumberUtil.div(amount, 1_0000, 2) + "万";
		} else if (amount < 1_0000_0000_0000L && amount > -1_0000_0000_0000L) {
			res = NumberUtil.div(amount, 1_0000_0000, 2) + "亿";
		} else {
			res = NumberUtil.div(amount, 1_0000_0000_0000L, 2) + "万亿";
		}
		return res;
	}

	/**
	 * 数字字符转中文，非数字字符原样返回
	 *
	 * @param c                数字字符
	 * @param isUseTraditional 是否繁体
	 * @return 中文字符
	 * @since 5.3.9
	 */
	public static char formatChar(final char c, final boolean isUseTraditional) {
		if (c < '0' || c > '9') {
			return c;
		}
		return numberToChinese(c - '0', isUseTraditional);
	}

	/**
	 * 获取 NumberChineseFormatter 默认对象
	 *
	 * @return NumberChineseFormatter
	 */
	public static ChineseNumberFormatter of() {
		return new ChineseNumberFormatter();
	}

	/**
	 * 中文形式，奇数位置是简体，偶数位置是记账繁体，0共用<br>
	 * 使用混合数组提高效率和数组复用
	 **/
	static final char[] DIGITS = {'零', '一', '壹', '二', '贰', '三', '叁', '四', '肆', '五', '伍',
		'六', '陆', '七', '柒', '八', '捌', '九', '玖'};

	// region ----- 配置项
	private boolean useTraditional;
	private boolean moneyMode;
	private boolean colloquialMode;
	private String negativeName = "负";
	private String unitName = "元";

	/**
	 * 是否使用繁体，即金额表示模式，如：壹拾贰圆叁角贰分
	 *
	 * @param useTraditional 是否使用繁体
	 * @return this
	 */
	public ChineseNumberFormatter setUseTraditional(final boolean useTraditional) {
		this.useTraditional = useTraditional;
		return this;
	}

	/**
	 * 是否使用金额模式，，如：壹拾贰圆
	 *
	 * @param moneyMode 是否使用金额模式
	 * @return this
	 */
	public ChineseNumberFormatter setMoneyMode(final boolean moneyMode) {
		this.moneyMode = moneyMode;
		return this;
	}

	/**
	 * 是否使用口语模式，此模式下的数字更加简化，如“一十一”会表示为“十一”
	 * @param colloquialMode 是否口语模式
	 * @return this
	 */
	public ChineseNumberFormatter setColloquialMode(final boolean colloquialMode) {
		this.colloquialMode = colloquialMode;
		return this;
	}

	/**
	 * 设置负数的表示名称，如"负"
	 *
	 * @param negativeName 负数表示名称，非空
	 * @return this
	 */
	public ChineseNumberFormatter setNegativeName(final String negativeName) {
		this.negativeName = Assert.notNull(negativeName);
		return this;
	}

	/**
	 * 设置金额单位名称，如：“元”或“圆”
	 *
	 * @param unitName 金额单位名称
	 * @return this
	 */
	public ChineseNumberFormatter setUnitName(final String unitName) {
		this.unitName = Assert.notNull(unitName);;
		return this;
	}
	// endregion

	/**
	 * 阿拉伯数字转换成中文.
	 *
	 * <p>主要是对发票票面金额转换的扩展
	 * <p>如：-12.32
	 * <p>发票票面转换为：(负数)壹拾贰圆叁角贰分
	 * <p>而非：负壹拾贰元叁角贰分
	 * <p>共两点不同：1、(负数) 而非 负；2、圆 而非 元
	 * 2022/3/9
	 *
	 * @param amount 数字
	 * @return 格式化后的字符串
	 * @author machuanpeng
	 * @since 5.7.23
	 */
	public String format(double amount) {
		if (0 == amount) {
			return this.moneyMode ? "零元整" : "零";
		}
		Assert.checkBetween(amount, -99_9999_9999_9999.99, 99_9999_9999_9999.99,
			"Number support only: (-99999999999999.99 ~ 99999999999999.99)！");

		final StringBuilder chineseStr = new StringBuilder();

		// 负数
		if (amount < 0) {
			chineseStr.append(this.negativeName);
			amount = -amount;
		}

		long yuan = Math.round(amount * 100);
		final int fen = (int) (yuan % 10);
		yuan = yuan / 10;
		final int jiao = (int) (yuan % 10);
		yuan = yuan / 10;

		final boolean isMoneyMode = this.moneyMode;
		// 元
		if (!isMoneyMode || 0 != yuan) {
			// 金额模式下，无需“零元”
			chineseStr.append(longToChinese(yuan));
			if (isMoneyMode) {
				chineseStr.append(this.unitName);
			}
		}

		if (0 == jiao && 0 == fen) {
			//无小数部分的金额结尾
			if (isMoneyMode) {
				chineseStr.append("整");
			}
			return chineseStr.toString();
		}

		// 小数部分
		if (!isMoneyMode) {
			chineseStr.append("点");
		}

		// 角
		if (0 == yuan && 0 == jiao) {
			// 元和角都为0时，只有非金额模式下补“零”
			if (!isMoneyMode) {
				chineseStr.append("零");
			}
		} else {
			chineseStr.append(numberToChinese(jiao, this.useTraditional));
			if (isMoneyMode && 0 != jiao) {
				chineseStr.append("角");
			}
		}

		// 分
		if (0 != fen) {
			chineseStr.append(numberToChinese(fen, this.useTraditional));
			if (isMoneyMode) {
				chineseStr.append("分");
			}
		}

		return chineseStr.toString();
	}

	/**
	 * 阿拉伯数字整数部分转换成中文，只支持正数
	 *
	 * @param amount           数字
	 * @return 中文
	 */
	private String longToChinese(long amount) {
		if (0 == amount) {
			return "零";
		}

		// 对于10~20，可选口语模式，如一十一，口语模式下为十一
		if (amount < 20 && amount >= 10) {
			final String chinese = thousandToChinese((int) amount);
			// "十一"而非"一十一"
			return this.colloquialMode ? chinese.substring(1) : chinese;
		}

		//将数字以万为单位分为多份
		final int[] parts = new int[4];
		for (int i = 0; amount != 0; i++) {
			parts[i] = (int) (amount % 10000);
			amount = amount / 10000;
		}

		final StringBuilder chineseStr = new StringBuilder();
		int partValue;
		String partChinese;

		// 千
		partValue = parts[0];
		if (partValue > 0) {
			partChinese = thousandToChinese(partValue);
			chineseStr.insert(0, partChinese);

			if (partValue < 1000) {
				// 和万位之间空0，则补零，如一万零三百
				addPreZero(chineseStr);
			}
		}

		// 万
		partValue = parts[1];
		if (partValue > 0) {
			if ((partValue % 10 == 0 && parts[0] > 0)) {
				// 如果"万"的个位是0，则补零，如十万零八千
				addPreZero(chineseStr);
			}
			partChinese = thousandToChinese(partValue);
			chineseStr.insert(0, partChinese + "万");

			if (partValue < 1000) {
				// 和亿位之间空0，则补零，如一亿零三百万
				addPreZero(chineseStr);
			}
		} else {
			addPreZero(chineseStr);
		}

		// 亿
		partValue = parts[2];
		if (partValue > 0) {
			if ((partValue % 10 == 0 && parts[1] > 0)) {
				// 如果"万"的个位是0，则补零，如十万零八千
				addPreZero(chineseStr);
			}

			partChinese = thousandToChinese(partValue);
			chineseStr.insert(0, partChinese + "亿");

			if (partValue < 1000) {
				// 和万亿位之间空0，则补零，如一万亿零三百亿
				addPreZero(chineseStr);
			}
		} else {
			addPreZero(chineseStr);
		}

		// 万亿
		partValue = parts[3];
		if (partValue > 0) {
			if (parts[2] == 0) {
				chineseStr.insert(0, "亿");
			}
			partChinese = thousandToChinese(partValue);
			chineseStr.insert(0, partChinese + "万");
		}

		if (StrUtil.isNotEmpty(chineseStr) && '零' == chineseStr.charAt(0)) {
			return chineseStr.substring(1);
		}

		return chineseStr.toString();
	}

	/**
	 * 把一个 0~9999 之间的整数转换为汉字的字符串，如果是 0 则返回 ""
	 *
	 * @param amountPart       数字部分
	 * @return 转换后的汉字
	 */
	private String thousandToChinese(final int amountPart) {
		if (amountPart == 0) {
			// issue#I4R92H@Gitee
			return String.valueOf(DIGITS[0]);
		}

		int temp = amountPart;

		final StringBuilder chineseStr = new StringBuilder();
		boolean lastIsZero = true; // 在从低位往高位循环时，记录上一位数字是不是 0
		for (int i = 0; temp > 0; i++) {
			final int digit = temp % 10;
			if (digit == 0) { // 取到的数字为 0
				if (!lastIsZero) {
					// 前一个数字不是 0，则在当前汉字串前加“零”字;
					chineseStr.insert(0, "零");
				}
				lastIsZero = true;
			} else { // 取到的数字不是 0
				final boolean isUseTraditional = this.useTraditional;
				chineseStr.insert(0, numberToChinese(digit, isUseTraditional) + ChineseNumberParser.getUnitName(i, isUseTraditional));
				lastIsZero = false;
			}
			temp = temp / 10;
		}
		return chineseStr.toString();
	}

	/**
	 * 单个数字转汉字
	 *
	 * @param number           数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 汉字
	 */
	private static char numberToChinese(final int number, final boolean isUseTraditional) {
		if (0 == number) {
			return DIGITS[0];
		}
		return DIGITS[number * 2 - (isUseTraditional ? 0 : 1)];
	}

	private static void addPreZero(final StringBuilder chineseStr) {
		if (StrUtil.isEmpty(chineseStr)) {
			return;
		}
		if ('零' != chineseStr.charAt(0)) {
			chineseStr.insert(0, '零');
		}
	}
}
