package cn.hutool.core.date;

import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 农历日期工具
 *
 * @author zjw, looly
 * @since 5.1.1
 */
public class ChineseDate {

	private static final Date baseDate = DateUtil.parseDate("1900-01-31");

	//农历年
	private final int year;
	//农历月
	private final int month;
	//农历日
	private final int day;
	//是否闰年
	private boolean leap;
	private final String[] chineseNumber = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
	private final String[] chineseNumberName = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "腊"};
	private final long[] lunarInfo = new long[]{0x04bd8, 0x04ae0, 0x0a570,
			0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
			0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
			0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
			0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
			0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
			0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
			0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
			0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
			0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
			0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
			0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
			0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
			0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
			0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
			0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
			0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
			0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
			0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
			0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0};
	//农历节日  *表示放假日
	private final String[] lFtv = new String[]{
			"0101 春节", "0102 大年初二", "0103 大年初三", "0104 大年初四",
			"0105 大年初五", "0106 大年初六", "0107 大年初七", "0105 路神生日",
			"0115 元宵节", "0202 龙抬头", "0219 观世音圣诞", "0404 寒食节",
			"0408 佛诞节 ", "0505 端午节", "0606 天贶节 姑姑节", "0624 彝族火把节",
			"0707 七夕情人节", "0714 鬼节(南方)", "0715 盂兰节", "0730 地藏节",
			"0815 中秋节", "0909 重阳节", "1001 祭祖节", "1117 阿弥陀佛圣诞",
			"1208 腊八节 释迦如来成道日", "1223 过小年", "1229 腊月二十九", "1230 除夕"};

	/**
	 * 构造方法传入日期
	 *
	 * @param date 日期
	 */
	public ChineseDate(Date date) {
		// -------------------- private --------------------
		int yearCyl;
		int monCyl;
		int dayCyl;
		int leapMonth;
		// 求出和1900年1月31日相差的天数
		int offset = (int) ((date.getTime() - baseDate.getTime()) / 86400000L);
		monCyl = 14;
		// 用offset减去每农历年的天数
		// 计算当天是农历第几天
		// i最终结果是农历的年份
		// offset是当年的第几天
		int iYear;
		int daysOfYear = 0;
		for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
			daysOfYear = yearDays(iYear);
			offset -= daysOfYear;
			monCyl += 12;
		}
		if (offset < 0) {
			offset += daysOfYear;
			iYear--;
			monCyl -= 12;
		}
		// 农历年份
		year = iYear;
		leapMonth = leapMonth(iYear); // 闰哪个月,1-12
		leap = false;
		// 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
		int iMonth, daysOfMonth = 0;
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			// 闰月
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
				--iMonth;
				leap = true;
				daysOfMonth = leapDays(year);
			} else
				daysOfMonth = monthDays(year, iMonth);
			offset -= daysOfMonth;
			// 解除闰月
			if (leap && iMonth == (leapMonth + 1))
				leap = false;
			if (!leap)
				monCyl++;
		}
		// offset为0时，并且刚才计算的月份是闰月，要校正
		if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
			if (leap) {
				leap = false;
			} else {
				leap = true;
				--iMonth;
				--monCyl;
			}
		}
		// offset小于0时，也要校正
		if (offset < 0) {
			offset += daysOfMonth;
			--iMonth;
			--monCyl;
		}
		month = iMonth;
		day = offset + 1;
	}

	/**
	 * 构造方法传入日期
	 *
	 * @param chineseYear  农历年
	 * @param chineseMonth 农历月，1表示一月（正月）
	 * @param chineseDay   农历日，1表示初一
	 * @since 5.2.4
	 */
	public ChineseDate(int chineseYear, int chineseMonth, int chineseDay) {
		this.day = chineseDay;
		this.month = chineseMonth;
		this.year = chineseYear;
		this.leap = DateUtil.isLeapYear(chineseYear);
	}

	/**
	 * 获得农历年份
	 *
	 * @return 返回农历年份
	 */
	public int getChineseYear() {
		return this.year;
	}

	/**
	 * 获取农历的月，从1开始计数
	 *
	 * @return 农历的月
	 * @since 5.2.4
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * 获得农历月份（中文，例如二月，十二月，或者润一月）
	 *
	 * @return 返回农历月份
	 */
	public String getChineseMonth() {
		return (leap ? "闰" : "") + NumberChineseFormatter.format(this.month, false) + "月";
	}

	/**
	 * 获得农历月称呼（中文，例如二月，腊月，或者润正月）
	 *
	 * @return 返回农历月份称呼
	 */
	public String getChineseMonthName() {
		return (leap ? "闰" : "") + chineseNumberName[month - 1] + "月";
	}

	/**
	 * 获取农历的日，从1开始计数
	 *
	 * @return 农历的日，从1开始计数
	 * @since 5.2.4
	 */
	public int getDay() {
		return this.day;
	}

	/**
	 * 获得农历日
	 *
	 * @return 获得农历日
	 */
	public String getChineseDay() {
		String[] chineseTen = {"初", "十", "廿", "卅"};
		int n = (day % 10 == 0) ? 9 : (day % 10 - 1);
		if (day > 30) {
			return "";
		}
		switch (day) {
			case 10:
				return "初十";
			case 20:
				return "二十";
			case 30:
				return "三十";
			default:
				return chineseTen[day / 10] + NumberChineseFormatter.format(n + 1, false);
		}
	}


	/**
	 * 获得节日
	 *
	 * @return 获得农历节日
	 */
	public String getFestivals() {
		StringBuilder currentChineseDate = new StringBuilder();
		if (month < 10) {
			currentChineseDate.append('0');
		}
		currentChineseDate.append(this.month);

		if (day < 10) {
			currentChineseDate.append('0');
		}
		currentChineseDate.append(this.day);

		List<String> getFestivalsList = new ArrayList<>();
		for (String fv : lFtv) {
			final List<String> split = StrUtil.split(fv, ' ');
			if (split.get(0).contentEquals(currentChineseDate)) {
				getFestivalsList.add(split.get(1));
			}
		}
		return StrUtil.join(",", getFestivalsList);
	}

	/**
	 * 获得年份生肖
	 *
	 * @return 获得年份生肖
	 */
	public String getChineseZodiac() {
		return Zodiac.getChineseZodiac(this.year);
	}


	/**
	 * 获得天干地支
	 *
	 * @return 获得天干地支
	 */
	public String getCyclical() {
		int num = year - 1900 + 36;
		return (cyclicalm(num));
	}

	/**
	 * 转换为标准的日期格式来表示农历日期，例如2020-01-13
	 *
	 * @return 标准的日期格式
	 * @since 5.2.4
	 */
	public String toStringNormal(){
		return String.format("%04d-%02d-%02d", this.year, this.month, this.day);
	}

	@Override
	public String toString() {
		return String.format("%s%s年 %s%s", getCyclical(), getChineseZodiac(), getChineseMonthName(), getChineseDay());
	}

	// ------------------------------------------------------- private method start

	/**
	 * 传入 月日的offset 传回干支, 0=甲子
	 */
	private static String cyclicalm(int num) {
		final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
		final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	/**
	 * 传回农历 y年的总天数
	 *
	 * @param y 年
	 * @return 总天数
	 */
	private int yearDays(int y) {
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(y));
	}

	/**
	 * 传回农历 y年闰月的天数
	 *
	 * @param y 年
	 * @return 闰月的天数
	 */
	private int leapDays(int y) {
		if (leapMonth(y) != 0) {
			return (lunarInfo[y - 1900] & 0x10000) != 0 ? 30 : 29;
		} else
			return 0;
	}


	/**
	 * 传回农历 y年m月的总天数
	 *
	 * @param y 年
	 * @param m 月
	 * @return 总天数
	 */
	private int monthDays(int y, int m) {
		return (lunarInfo[y - 1900] & (0x10000 >> m)) == 0 ? 29 : 30;
	}

	/**
	 * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
	 *
	 * @param y 年
	 * @return 润的月
	 */
	private int leapMonth(int y) {
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	// ------------------------------------------------------- private method end
}
