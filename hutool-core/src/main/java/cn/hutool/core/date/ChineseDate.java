package cn.hutool.core.date;

import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.date.chinese.ChineseMonth;
import cn.hutool.core.date.chinese.GanZhi;
import cn.hutool.core.date.chinese.LunarFestival;
import cn.hutool.core.date.chinese.LunarInfo;
import cn.hutool.core.util.StrUtil;

import java.util.Date;


/**
 * 农历日期工具，最大支持到2055年
 *
 * @author zjw, looly
 * @since 5.1.1
 */
public class ChineseDate {

	//农历年
	private final int year;
	//农历月
	private final int month;
	//农历日
	private final int day;

	//公历年
	private final int gyear;
	//公历月
	private final int gmonth;
	//公里日
	private final int gday;

	//是否闰年
	private boolean leap;

	/**
	 * 构造方法传入日期
	 *
	 * @param date 日期
	 */
	public ChineseDate(Date date) {
		// 求出和1900年1月31日相差的天数
		int offset = (int) ((DateUtil.beginOfDay(date).getTime() / DateUnit.DAY.getMillis()) - LunarInfo.BASE_DAY);
		// 计算农历年份
		// 用offset减去每农历年的天数，计算当天是农历第几天，offset是当年的第几天
		int daysOfYear;
		int iYear = LunarInfo.BASE_YEAR;
		for (; iYear <= LunarInfo.MAX_YEAR; iYear++) {
			daysOfYear = LunarInfo.yearDays(iYear);
			if (offset < daysOfYear) {
				break;
			}
			offset -= daysOfYear;
		}

		year = iYear;
		// 计算农历月份
		int leapMonth = LunarInfo.leapMonth(iYear); // 闰哪个月,1-12
		// 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
		int iMonth;
		int daysOfMonth = 0;
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			// 闰月
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && false == leap) {
				--iMonth;
				leap = true;
				daysOfMonth = LunarInfo.leapDays(year);
			} else {
				daysOfMonth = LunarInfo.monthDays(year, iMonth);
			}

			offset -= daysOfMonth;
			// 解除闰月
			if (leap && iMonth == (leapMonth + 1)) {
				leap = false;
			}
		}

		// offset为0时，并且刚才计算的月份是闰月，要校正
		if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
			if (leap) {
				leap = false;
			} else {
				leap = true;
				--iMonth;
			}
		}
		// offset小于0时，也要校正
		if (offset < 0) {
			offset += daysOfMonth;
			--iMonth;
		}

		month = iMonth;
		day = offset + 1;

		// 公历
		final DateTime dt = DateUtil.date(date);
		gyear = dt.year();
		gmonth = dt.month() + 1;
		gday = dt.dayOfMonth();
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
		//先判断传入的月份是不是闰月
		int leapMonth = LunarInfo.leapMonth(chineseYear);

		final DateTime dateTime = lunar2solar(chineseYear, chineseMonth, chineseDay, chineseMonth == leapMonth);
		if (null != dateTime) {
			//初始化公历年
			this.gday = dateTime.dayOfMonth();
			//初始化公历月
			this.gmonth = dateTime.month() + 1;
			//初始化公历日
			this.gyear = dateTime.year();
		} else {
			//初始化公历年
			this.gday = -1;
			//初始化公历月
			this.gmonth = -1;
			//初始化公历日
			this.gyear = -1;
		}
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
	 * 当前农历月份是否为闰月
	 *
	 * @return 是否为闰月
	 * @since 5.4.2
	 */
	public boolean isLeapMonth() {
		return ChineseMonth.isLeapMonth(this.year, this.month);
	}


	/**
	 * 获得农历月份（中文，例如二月，十二月，或者润一月）
	 *
	 * @return 返回农历月份
	 */
	public String getChineseMonth() {
		return ChineseMonth.getChineseMonthName(isLeapMonth(), this.month, false);
	}

	/**
	 * 获得农历月称呼（中文，例如二月，腊月，或者润正月）
	 *
	 * @return 返回农历月份称呼
	 */
	public String getChineseMonthName() {
		return ChineseMonth.getChineseMonthName(isLeapMonth(), this.month, true);
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
		return StrUtil.join(",", LunarFestival.getFestivals(this.year, this.month, day));
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
	 * 获得年的天干地支
	 *
	 * @return 获得天干地支
	 */
	public String getCyclical() {
		return GanZhi.getGanzhiOfYear(this.year);
	}

	/**
	 * 干支纪年信息
	 *
	 * @return 获得天干地支的年月日信息
	 */
	public String getCyclicalYMD() {
		if (gyear >= LunarInfo.BASE_YEAR && gmonth > 0 && gday > 0) {
			return cyclicalm(gyear, gmonth, gday);
		}
		return null;
	}

	/**
	 * 转换为标准的日期格式来表示农历日期，例如2020-01-13
	 *
	 * @return 标准的日期格式
	 * @since 5.2.4
	 */
	public String toStringNormal() {
		return String.format("%04d-%02d-%02d", this.year, this.month, this.day);
	}

	@Override
	public String toString() {
		return String.format("%s%s年 %s%s", getCyclical(), getChineseZodiac(), getChineseMonthName(), getChineseDay());
	}

	// ------------------------------------------------------- private method start

	/**
	 * 这里同步处理年月日的天干地支信息
	 *
	 * @param year  公历年
	 * @param month 公历月，从1开始
	 * @param day   公历日
	 * @return 天干地支信息
	 */
	private String cyclicalm(int year, int month, int day) {
		return StrUtil.format("{}年{}月{}日",
				GanZhi.getGanzhiOfYear(this.year),
				GanZhi.getGanzhiOfMonth(year, month, day),
				GanZhi.getGanzhiOfDay(year, month, day));
	}

	/**
	 * 通过农历年月日信息 返回公历信息 提供给构造函数
	 *
	 * @param chineseYear  农历年
	 * @param chineseMonth 农历月
	 * @param chineseDay   农历日
	 * @param isLeapMonth  传入的月是不是闰月
	 * @return 公历信息
	 */
	private DateTime lunar2solar(int chineseYear, int chineseMonth, int chineseDay, boolean isLeapMonth) {
		//超出了最大极限值
		if ((chineseYear == 2100 && chineseMonth == 12 && chineseDay > 1) ||
				(chineseYear == LunarInfo.BASE_YEAR && chineseMonth == 1 && chineseDay < 31)) {
			return null;
		}
		int day = LunarInfo.monthDays(chineseYear, chineseMonth);
		int _day = day;
		if (isLeapMonth) {
			_day = LunarInfo.leapDays(chineseYear);
		}
		//参数合法性效验
		if (chineseYear < LunarInfo.BASE_YEAR || chineseYear > 2100 || chineseDay > _day) {
			return null;
		}
		//计算农历的时间差
		int offset = 0;
		for (int i = LunarInfo.BASE_YEAR; i < chineseYear; i++) {
			offset += LunarInfo.yearDays(i);
		}
		int leap;
		boolean isAdd = false;
		for (int i = 1; i < chineseMonth; i++) {
			leap = LunarInfo.leapMonth(chineseYear);
			if (false == isAdd) {//处理闰月
				if (leap <= i && leap > 0) {
					offset += LunarInfo.leapDays(chineseYear);
					isAdd = true;
				}
			}
			offset += LunarInfo.monthDays(chineseYear, i);
		}
		//转换闰月农历 需补充该年闰月的前一个月的时差
		if (isLeapMonth) {
			offset += day;
		}
		//1900年农历正月一日的公历时间为1900年1月30日0时0分0秒(该时间也是本农历的最开始起始点) -2203804800000
		return DateUtil.date(((offset + chineseDay - 31) * 86400000L) - 2203804800000L);
	}

	// ------------------------------------------------------- private method end

}