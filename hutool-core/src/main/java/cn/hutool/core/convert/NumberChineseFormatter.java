package cn.hutool.core.convert;

import cn.hutool.core.util.StrUtil;

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
public class NumberChineseFormatter {

	/**
	 * 简体中文形式
	 **/
	private static final String[] SIMPLE_DIGITS = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
	/**
	 * 繁体中文形式
	 **/
	private static final String[] TRADITIONAL_DIGITS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

	/**
	 * 简体中文单位
	 **/
	private static final String[] SIMPLE_UNITS = {"", "十", "百", "千"};
	/**
	 * 繁体中文单位
	 **/
	private static final String[] TRADITIONAL_UNITS = {"", "拾", "佰", "仟"};

	/**
	 * 阿拉伯数字转换成中文,小数点后四舍五入保留两位. 使用于整数、小数的转换.
	 *
	 * @param amount           数字
	 * @param isUseTraditional 是否使用繁体
	 * @return 中文
	 */
	public static String format(double amount, boolean isUseTraditional) {
		return format(amount, isUseTraditional, false);
	}

	/**
	 * 阿拉伯数字转换成中文,小数点后四舍五入保留两位. 使用于整数、小数的转换.
	 *
	 * @param amount           数字
	 * @param isUseTraditional 是否使用繁体
	 * @param isMoneyMode      是否为金额模式
	 * @return 中文
	 */
	public static String format(double amount, boolean isUseTraditional, boolean isMoneyMode) {
		final String[] numArray = isUseTraditional ? TRADITIONAL_DIGITS : SIMPLE_DIGITS;

		if (amount > 99999999999999.99 || amount < -99999999999999.99) {
			throw new IllegalArgumentException("Number support only: (-99999999999999.99 ～ 99999999999999.99)！");
		}

		boolean negative = false;
		if (amount < 0) {
			negative = true;
			amount = -amount;
		}

		long temp = Math.round(amount * 100);
		int numFen = (int) (temp % 10);
		temp = temp / 10;
		int numJiao = (int) (temp % 10);
		temp = temp / 10;

		//将数字以万为单位分为多份
		int[] parts = new int[20];
		int numParts = 0;
		for (int i = 0; temp != 0; i++) {
			int part = (int) (temp % 10000);
			parts[i] = part;
			numParts++;
			temp = temp / 10000;
		}

		boolean beforeWanIsZero = true; // 标志“万”下面一级是不是 0

		StringBuilder chineseStr = new StringBuilder();
		for (int i = 0; i < numParts; i++) {
			String partChinese = toChinese(parts[i], isUseTraditional);
			if (i % 2 == 0) {
				beforeWanIsZero = StrUtil.isEmpty(partChinese);
			}

			if (i != 0) {
				if (i % 2 == 0) {
					chineseStr.insert(0, "亿");
				} else {
					if ("".equals(partChinese) && false == beforeWanIsZero) {
						// 如果“万”对应的 part 为 0，而“万”下面一级不为 0，则不加“万”，而加“零”
						chineseStr.insert(0, "零");
					} else {
						if (parts[i - 1] < 1000 && parts[i - 1] > 0) {
							// 如果"万"的部分不为 0, 而"万"前面的部分小于 1000 大于 0， 则万后面应该跟“零”
							chineseStr.insert(0, "零");
						}
						if (parts[i] > 0) {
							// 如果"万"的部分不为 0 则增加万
							chineseStr.insert(0, "万");
						}
					}
				}
			}
			chineseStr.insert(0, partChinese);
		}

		// 整数部分为 0, 则表达为"零"
		if (StrUtil.EMPTY.equals(chineseStr.toString())) {
			chineseStr = new StringBuilder(numArray[0]);
		}
		//负数
		if (negative) { // 整数部分不为 0
			chineseStr.insert(0, "负");
		}

		// 小数部分
		if (numFen != 0 || numJiao != 0) {
			if (numFen == 0) {
				chineseStr.append(isMoneyMode ? "元" : "点").append(numArray[numJiao]).append(isMoneyMode ? "角" : "");
			} else { // “分”数不为 0
				if (numJiao == 0) {
					chineseStr.append(isMoneyMode ? "元零" : "点零").append(numArray[numFen]).append(isMoneyMode ? "分" : "");
				} else {
					chineseStr.append(isMoneyMode ? "元" : "点").append(numArray[numJiao]).append(isMoneyMode ? "角" : "").append(numArray[numFen]).append(isMoneyMode ? "分" : "");
				}
			}
		} else if (isMoneyMode) {
			//无小数部分的金额结尾
			chineseStr.append("元整");
		}

		return chineseStr.toString();

	}

	/**
	 * 数字字符转中文，非数字字符原样返回
	 *
	 * @param c                数字字符
	 * @param isUseTraditional 是否繁体
	 * @return 中文字符
	 * @since 5.3.9
	 */
	public static String numberCharToChinese(char c, boolean isUseTraditional) {
		String[] numArray = isUseTraditional ? TRADITIONAL_DIGITS : SIMPLE_DIGITS;
		int index = c - 48;
		if (index < 0 || index >= numArray.length) {
			return String.valueOf(c);
		}
		return numArray[index];
	}

	/**
	 * 把一个 0~9999 之间的整数转换为汉字的字符串，如果是 0 则返回 ""
	 *
	 * @param amountPart       数字部分
	 * @param isUseTraditional 是否使用繁体单位
	 * @return 转换后的汉字
	 */
	private static String toChinese(int amountPart, boolean isUseTraditional) {
//		if (amountPart < 0 || amountPart > 10000) {
//			throw new IllegalArgumentException("Number must 0 < num < 10000！");
//		}

		String[] numArray = isUseTraditional ? TRADITIONAL_DIGITS : SIMPLE_DIGITS;
		String[] units = isUseTraditional ? TRADITIONAL_UNITS : SIMPLE_UNITS;

		int temp = amountPart;

		StringBuilder chineseStr = new StringBuilder();
		boolean lastIsZero = true; // 在从低位往高位循环时，记录上一位数字是不是 0
		for (int i = 0; temp > 0; i++) {
			int digit = temp % 10;
			if (digit == 0) { // 取到的数字为 0
				if (false == lastIsZero) {
					// 前一个数字不是 0，则在当前汉字串前加“零”字;
					chineseStr.insert(0, "零");
				}
				lastIsZero = true;
			} else { // 取到的数字不是 0
				chineseStr.insert(0, numArray[digit] + units[i]);
				lastIsZero = false;
			}
			temp = temp / 10;
		}
		return chineseStr.toString();
	}
}
