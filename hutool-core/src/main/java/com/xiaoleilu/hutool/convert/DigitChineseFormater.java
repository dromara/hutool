package com.xiaoleilu.hutool.convert;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.xiaoleilu.hutool.util.NumberUtil;

/**
 * 金额中文格式化类
 * 
 * @author looly
 * @since 3.2.3
 */
public class DigitChineseFormater {

	private static final String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	private static final String unit1[] = { "", "拾", "佰", "仟" };// 把钱数分成段,每四个一段,实际上得到的是一个二维数组
	private static final String unit2[] = { "元", "万", "亿", "万亿" }; // 把钱数分成段,每四个一段,实际上得到的是一个二维数组
	
	/**
	 * 执行格式化<br>
	 * 仟 佰 拾 ' ' ' ' $4 $3 $2 $1 万 $8 $7 $6 $5 亿 $12 $11 $10 $9
	 * 
	 * @param num 数字
	 * @return 结果
	 */
	public static String format(Number num) {
		return format(num.toString());
	}

	/**
	 * 执行格式化<br>
	 * 仟 佰 拾 ' ' ' ' $4 $3 $2 $1 万 $8 $7 $6 $5 亿 $12 $11 $10 $9
	 * 
	 * @param num 数字
	 * @return 结果
	 */
	public static String format(String num) {
		BigDecimal bigDecimal = NumberUtil.mul(num, "100");
		String strVal = bigDecimal.toBigInteger().toString();
		String head = strVal.substring(0, strVal.length() - 2); // 整数部分
		String end = strVal.substring(strVal.length() - 2); // 小数部分
		String endMoney = "";
		String headMoney = "";
		if ("00".equals(end)) {
			endMoney = "整";
		} else {
			if (!end.substring(0, 1).equals("0")) {
				endMoney += digit[Integer.valueOf(end.substring(0, 1))] + "角";
			} else if (end.substring(0, 1).equals("0") && !end.substring(1, 2).equals("0")) {
				endMoney += "零";
			}
			if (!end.substring(1, 2).equals("0")) {
				endMoney += digit[Integer.valueOf(end.substring(1, 2))] + "分";
			}
		}
		char[] chars = head.toCharArray();
		Map<String, Boolean> map = new HashMap<>();// 段位置是否已出现zero
		boolean zeroKeepFlag = false;// 0连续出现标志
		int vidxtemp = 0;
		for (int i = 0; i < chars.length; i++) {
			int idx = (chars.length - 1 - i) % 4;// 段内位置 unit1
			int vidx = (chars.length - 1 - i) / 4;// 段位置 unit2
			String s = digit[Integer.valueOf(String.valueOf(chars[i]))];
			if (false == "零".equals(s)) {
				headMoney += s + unit1[idx] + unit2[vidx];
				zeroKeepFlag = false;
			} else if (i == chars.length - 1 || map.get("zero" + vidx) != null) {
				headMoney += "";
			} else {
				headMoney += s;
				zeroKeepFlag = true;
				map.put("zero" + vidx, true);// 该段位已经出现0；
			}
			if (vidxtemp != vidx || i == chars.length - 1) {
				headMoney = headMoney.replaceAll(unit2[vidx], "");
				headMoney += unit2[vidx];
			}
			if (zeroKeepFlag && (chars.length - 1 - i) % 4 == 0) {
				headMoney = headMoney.replaceAll("零", "");
			}
		}
		return headMoney + endMoney;
	}
}
