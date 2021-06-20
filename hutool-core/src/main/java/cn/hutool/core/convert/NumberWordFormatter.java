package cn.hutool.core.convert;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 将浮点数类型的number转换成英语的表达方式 <br>
 * 参考博客：http://blog.csdn.net/eric_sunah/article/details/8713226
 *
 * @author Looly,totalo
 * @since 3.0.9
 */
public class NumberWordFormatter {

	private static final String[] NUMBER = new String[]{"", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN",
			"EIGHT", "NINE"};
	private static final String[] NUMBER_TEEN = new String[]{"TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN",
			"FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN"};
	private static final String[] NUMBER_TEN = new String[]{"TEN", "TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY",
			"SEVENTY", "EIGHTY", "NINETY"};
	private static final String[] NUMBER_MORE = new String[]{"", "THOUSAND", "MILLION", "BILLION"};

	private static final String[] NUMBER_SUFFIX = new String[]{"k", "w", "", "m", "", "", "b", "", "", "t", "", "", "p", "", "", "e"};

	/**
	 * 将阿拉伯数字转为英文表达式
	 *
	 * @param x 阿拉伯数字，可以为{@link Number}对象，也可以是普通对象，最后会使用字符串方式处理
	 * @return 英文表达式
	 */
	public static String format(Object x) {
		if (x != null) {
			return format(x.toString());
		} else {
			return StrUtil.EMPTY;
		}
	}

	/**
	 * 将阿拉伯数字转化为简洁计数单位，例如 2100 =》 2.1k
	 * 范围默认只到w
	 *
	 * @param value 被格式化的数字
	 * @return 格式化后的数字
	 * @since 5.5.9
	 */
	public static String formatSimple(long value) {
		return formatSimple(value, true);
	}

	/**
	 * 将阿拉伯数字转化为简介计数单位，例如 2100 =》 2.1k
	 *
	 * @param value 对应数字的值
	 * @param isTwo 控制是否为只为k、w，例如当为{@code false}时返回4.38m，{@code true}返回438.43w
	 * @return 格式化后的数字
	 * @since 5.5.9
	 */
	public static String formatSimple(long value, boolean isTwo) {
		if (value < 1000) {
			return String.valueOf(value);
		}
		int index = -1;
		double res = value;
		while (res > 10 && (false == isTwo || index < 1)) {
			if (res > 1000) {
				res = res / 1000;
				index++;
			}
			if (res > 10) {
				res = res / 10;
				index++;
			}
		}
		return String.format("%s%s", NumberUtil.decimalFormat("#.##", res), NUMBER_SUFFIX[index]);
	}

	/**
	 * 将阿拉伯数字转为英文表达式
	 *
	 * @param x 阿拉伯数字字符串
	 * @return 英文表达式
	 */
	private static String format(String x) {
		int z = x.indexOf("."); // 取小数点位置
		String lstr, rstr = "";
		if (z > -1) { // 看是否有小数，如果有，则分别取左边和右边
			lstr = x.substring(0, z);
			rstr = x.substring(z + 1);
		} else {
			// 否则就是全部
			lstr = x;
		}

		String lstrrev = StrUtil.reverse(lstr); // 对左边的字串取反
		String[] a = new String[5]; // 定义5个字串变量来存放解析出来的叁位一组的字串

		switch (lstrrev.length() % 3) {
			case 1:
				lstrrev += "00";
				break;
			case 2:
				lstrrev += "0";
				break;
		}
		StringBuilder lm = new StringBuilder(); // 用来存放转换后的整数部分
		for (int i = 0; i < lstrrev.length() / 3; i++) {
			a[i] = StrUtil.reverse(lstrrev.substring(3 * i, 3 * i + 3)); // 截取第一个三位
			if (false == "000".equals(a[i])) { // 用来避免这种情况：1000000 = one million
				// thousand only
				if (i != 0) {
					lm.insert(0, transThree(a[i]) + " " + parseMore(i) + " "); // 加:
					// thousand、million、billion
				} else {
					// 防止i=0时， 在多加两个空格.
					lm = new StringBuilder(transThree(a[i]));
				}
			} else {
				lm.append(transThree(a[i]));
			}
		}

		String xs = ""; // 用来存放转换后小数部分
		if (z > -1) {
			xs = "AND CENTS " + transTwo(rstr) + " "; // 小数部分存在时转换小数
		}

		return lm.toString().trim() + " " + xs + "ONLY";
	}

	private static String parseFirst(String s) {
		return NUMBER[Integer.parseInt(s.substring(s.length() - 1))];
	}

	private static String parseTeen(String s) {
		return NUMBER_TEEN[Integer.parseInt(s) - 10];
	}

	private static String parseTen(String s) {
		return NUMBER_TEN[Integer.parseInt(s.substring(0, 1)) - 1];
	}

	private static String parseMore(int i) {
		return NUMBER_MORE[i];
	}

	// 两位
	private static String transTwo(String s) {
		String value;
		// 判断位数
		if (s.length() > 2) {
			s = s.substring(0, 2);
		} else if (s.length() < 2) {
			s = "0" + s;
		}

		if (s.startsWith("0")) {// 07 - seven 是否小於10
			value = parseFirst(s);
		} else if (s.startsWith("1")) {// 17 seventeen 是否在10和20之间
			value = parseTeen(s);
		} else if (s.endsWith("0")) {// 是否在10与100之间的能被10整除的数
			value = parseTen(s);
		} else {
			value = parseTen(s) + " " + parseFirst(s);
		}
		return value;
	}

	// 制作叁位的数
	// s.length = 3
	private static String transThree(String s) {
		String value;
		if (s.startsWith("0")) {// 是否小於100
			value = transTwo(s.substring(1));
		} else if ("00".equals(s.substring(1))) {// 是否被100整除
			value = parseFirst(s.substring(0, 1)) + " HUNDRED";
		} else {
			value = parseFirst(s.substring(0, 1)) + " HUNDRED AND " + transTwo(s.substring(1));
		}
		return value;
	}
}
