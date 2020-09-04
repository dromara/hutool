package cn.hutool.core.date.chinese;

/**
 * 天干地支类
 *
 * @author looly
 * @since 5.4.1
 */
public class GanZhi {

	private static final String[] GAN = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
	private static final String[] ZHI = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

	/**
	 * 传入 月日的offset 传回干支, 0=甲子
	 *
	 * @param num 月日的offset
	 * @return 干支
	 */
	public static String cyclicalm(int num) {
		return (GAN[num % 10] + ZHI[num % 12]);
	}
}
