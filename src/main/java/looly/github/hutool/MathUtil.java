package looly.github.hutool;

import java.math.BigDecimal;


public class MathUtil {
	/**
	 * 保留小数位
	 * @param number 被保留小数的数字
	 * @param digit 保留的小数位数
	 * @return
	 */
	public static String roundStr(double number, int digit) {
		return String.format("%."+digit + 'f', number);
	}
	
	/**
	 * 保留小数位
	 * @param number 被保留小数的数字
	 * @param digit 保留的小数位数
	 * @return
	 */
	public static double round(double number, int digit) {
		final BigDecimal bg = new BigDecimal(number);
		return bg.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
