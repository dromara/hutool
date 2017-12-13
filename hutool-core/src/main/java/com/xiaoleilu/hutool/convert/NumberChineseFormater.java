package com.xiaoleilu.hutool.convert;

/**
 * 数字转中文类
 * 
 * @author fanqun
 **/
public class NumberChineseFormater {
	
	/** 简体中文形式 **/
	private static final String[] simpleDigits = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
	/** 繁体中文形式 **/
	private static final String[] traditionalDigits = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	/**
	 * 阿拉伯数字转换成中文,小数点后四舍五入保留两位. 使用于整数、小数的转换.
	 *
	 * @param amount 数字
	 * @return 中文
	 */
	public static String format(double amount, boolean isUserTraditional) {
		final String[] numArray = isUserTraditional ? traditionalDigits : simpleDigits;

		if (amount > 99999999999999.99 || amount < -99999999999999.99) {
			throw new IllegalArgumentException("参数值超出允许范围 (-99999999999999.99 ～ 99999999999999.99)！");
		}

		boolean negative = false;
		if (amount < 0) {
			negative = true;
			amount = amount * (-1);
		}

		long temp = Math.round(amount * 100);
		int numFen = (int) (temp % 10);
		temp = temp / 10;
		int numJiao = (int) (temp % 10);
		temp = temp / 10;

		int[] parts = new int[20];
		int numParts = 0;
		for (int i = 0;; i++) {
			if (temp == 0)
				break;
			int part = (int) (temp % 10000);
			parts[i] = part;
			numParts++;
			temp = temp / 10000;
		}

		boolean beforeWanIsZero = true; // 标志“万”下面一级是不是 0

		String chineseStr = "";
		for (int i = 0; i < numParts; i++) {
			String partChinese = toChinese(parts[i], numArray);
			if (i % 2 == 0) {
				if ("".equals(partChinese))
					beforeWanIsZero = true;
				else
					beforeWanIsZero = false;
			}

			if (i != 0) {
				if (i % 2 == 0)
					chineseStr = "亿" + chineseStr;
				else {
					if ("".equals(partChinese) && !beforeWanIsZero) // 如果“万”对应的 part 为 0，而“万”下面一级不为 0，则不加“万”，而加“零”
						chineseStr = "零" + chineseStr;
					else {
						if (parts[i - 1] < 1000 && parts[i - 1] > 0) // 如果"万"的部分不为 0, 而"万"前面的部分小于 1000 大于 0， 则万后面应该跟“零”
							chineseStr = "零" + chineseStr;
						chineseStr = "万" + chineseStr;
					}
				}
			}
			chineseStr = partChinese + chineseStr;
		}

		if ("".equals(chineseStr)) // 整数部分为 0, 则表达为"零"
			chineseStr = numArray[0];
		else if (negative) // 整数部分不为 0
			chineseStr = "负" + chineseStr;

		chineseStr = chineseStr + "";

		if (numFen == 0 && numJiao == 0) {
			chineseStr = chineseStr + "";
		} else if (numFen == 0) {
			chineseStr = chineseStr + "点" + numArray[numJiao] + "";
		} else { // “分”数不为 0
			if (numJiao == 0)
				chineseStr = chineseStr + "点零" + numArray[numFen] + "";
			else
				chineseStr = chineseStr + "点" + numArray[numJiao] + numArray[numFen] + "";
		}

		return chineseStr;

	}

	/**
	 * 把一个 0~9999 之间的整数转换为汉字的字符串，如果是 0 则返回 ""
	 *
	 * @param amountPart 数字部分
	 * @param numArray 字典
	 * @return 转换后的汉字
	 */
	private static String toChinese(int amountPart, String[] numArray) {

		if (amountPart < 0 || amountPart > 10000) {
			throw new IllegalArgumentException("参数必须是大于等于 0，小于 10000 的整数！");
		}

		String[] units = new String[] { "", "十", "百", "千" };

		int temp = amountPart;

		String amountStr = new Integer(amountPart).toString();
		int amountStrLength = amountStr.length();
		boolean lastIsZero = true; // 在从低位往高位循环时，记录上一位数字是不是 0
		String chineseStr = "";

		for (int i = 0; i < amountStrLength; i++) {
			if (temp == 0) // 高位已无数据
				break;
			int digit = temp % 10;
			if (digit == 0) { // 取到的数字为 0
				if (!lastIsZero) // 前一个数字不是 0，则在当前汉字串前加“零”字;
					chineseStr = "零" + chineseStr;
				lastIsZero = true;
			} else { // 取到的数字不是 0
				chineseStr = numArray[digit] + units[i] + chineseStr;
				lastIsZero = false;
			}
			temp = temp / 10;
		}
		return chineseStr;
	}

	public static void main(String[] args) {
		System.out.println("-----------数字测算开始--------------");
		System.out.println("10889.72356: " + format(10889.72356, false));
		System.out.println("12653: " + format(12653, false));
		System.out.println("215.6387: " + format(215.6387, false));
		System.out.println("1024: " + format(1024, false));
		System.out.println("100350089: " + format(100350089, false));
		System.out.println("1200: " + format(1200.0, false));
		System.out.println("12: " + format(12, false));
		System.out.println("1.05: " + format(0.05, false));

		System.out.println("-----------数字测算结束--------------");
		
		System.out.println("-----------数字测算开始--------------");
		System.out.println("10889.72356: " + format(10889.72356, true));
		System.out.println("12653: " + format(12653, true));
		System.out.println("215.6387: " + format(215.6387, true));
		System.out.println("1024: " + format(1024, true));
		System.out.println("100350089: " + format(100350089, true));
		System.out.println("1200: " + format(1200.0, true));
		System.out.println("12: " + format(12, true));
		System.out.println("1.05: " + format(0.05, true));
		
		System.out.println("-----------数字测算结束--------------");

	}
}
