package cn.hutool.core.date;

import cn.hutool.core.convert.NumberChineseFormatter;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static cn.hutool.core.util.NumberUtil.parseInt;


/**
 * 农历日期工具，最大支持到2055年
 *
 * @author zjw, looly
 * @since 5.1.1
 */
public class ChineseDate {
//	private static final Date baseDate = DateUtil.parseDate("1900-01-31");
	/**
	 * 1900-01-31
	 */
	private static final long baseDate = -2206425943000L;
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
	private final String[] chineseNumber = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
	private final String[] chineseNumberName = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "腊"};
	/**
	 * 此表来自：https://github.com/jjonline/calendar.js/blob/master/calendar.js
	 * 农历表示：
	 * 1.  表示当年有无闰年，有的话，为闰月的月份，没有的话，为0。
	 * 2-4.为除了闰月外的正常月份是大月还是小月，1为30天，0为29天。
	 * 5.  表示闰月是大月还是小月，仅当存在闰月的情况下有意义。
	 */
	private final long[] lunarInfo = new long[]{
			0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,//1900-1909
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,//1910-1919
			0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,//1920-1929
			0x06566, 0x0d4a0, 0x0ea50, 0x16a95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,//1930-1939
			0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,//1940-1949
			0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0,//1950-1959
			0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,//1960-1969
			0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6,//1970-1979
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,//1980-1989
			0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x05ac0, 0x0ab60, 0x096d5, 0x092e0,//1990-1999
			0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,//2000-2009
			0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,//2010-2019
			0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,//2020-2029
			0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,//2030-2039
			0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0,//2040-2049
			0x14b63, 0x09370, 0x049f8, 0x04970, 0x064b0, 0x168a6, 0x0ea50, 0x06b20, 0x1a6c4, 0x0aae0,//2050-2059
			0x092e0, 0x0d2e3, 0x0c960, 0x0d557, 0x0d4a0, 0x0da50, 0x05d55, 0x056a0, 0x0a6d0, 0x055d4,//2060-2069
			0x052d0, 0x0a9b8, 0x0a950, 0x0b4a0, 0x0b6a6, 0x0ad50, 0x055a0, 0x0aba4, 0x0a5b0, 0x052b0,//2070-2079
			0x0b273, 0x06930, 0x07337, 0x06aa0, 0x0ad50, 0x14b55, 0x04b60, 0x0a570, 0x054e4, 0x0d160,//2080-2089
			0x0e968, 0x0d520, 0x0daa0, 0x16aa6, 0x056d0, 0x04ae0, 0x0a9d4, 0x0a2d0, 0x0d150, 0x0f252,//2090-2099
	};
	/**
	 * 1900-2100各年的24节气日期速查表
	 * 此表来自：https://github.com/jjonline/calendar.js/blob/master/calendar.js
	 */
	private final String[] sTermInfo = new String[]{
			"9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf97c3598082c95f8c965cc920f",
			"97bd0b06bdb0722c965ce1cfcc920f", "b027097bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf97c359801ec95f8c965cc920f", "97bd0b06bdb0722c965ce1cfcc920f", "b027097bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f", "97bd0b06bdb0722c965ce1cfcc920f",
			"b027097bd097c36b0b6fc9274c91aa", "9778397bd19801ec9210c965cc920e", "97b6b97bd19801ec95f8c965cc920f",
			"97bd09801d98082c95f8e1cfcc920f", "97bd097bd097c36b0b6fc9210c8dc2", "9778397bd197c36c9210c9274c91aa",
			"97b6b97bd19801ec95f8c965cc920e", "97bd09801d98082c95f8e1cfcc920f", "97bd097bd097c36b0b6fc9210c8dc2",
			"9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec95f8c965cc920e", "97bcf97c3598082c95f8e1cfcc920f",
			"97bd097bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf97c3598082c95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c965cc920e", "97bcf97c3598082c95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722",
			"9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f",
			"97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf97c359801ec95f8c965cc920f", "97bd097bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c965cc920e", "97bcf97c359801ec95f8c965cc920f", "97bd097bd07f595b0b6fc920fb0722",
			"9778397bd097c36b0b6fc9210c8dc2", "9778397bd19801ec9210c9274c920e", "97b6b97bd19801ec95f8c965cc920f",
			"97bd07f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c920e",
			"97b6b97bd19801ec95f8c965cc920f", "97bd07f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2",
			"9778397bd097c36c9210c9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bd07f1487f595b0b0bc920fb0722",
			"7f0e397bd097c36b0b6fc9210c8dc2", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf7f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c965cc920e", "97bcf7f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
			"9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e", "97bcf7f1487f531b0b0bb0b6fb0722",
			"7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b97bd19801ec9210c965cc920e",
			"97bcf7f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b97bd19801ec9210c9274c920e", "97bcf7f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
			"9778397bd097c36b0b6fc9210c91aa", "97b6b97bd197c36c9210c9274c920e", "97bcf7f0e47f531b0b0bb0b6fb0722",
			"7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9210c8dc2", "9778397bd097c36c9210c9274c920e",
			"97b6b7f0e47f531b0723b0b6fb0722", "7f0e37f5307f595b0b0bc920fb0722", "7f0e397bd097c36b0b6fc9210c8dc2",
			"9778397bd097c36b0b70c9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721", "7f0e37f1487f595b0b0bb0b6fb0722",
			"7f0e397bd097c35b0b6fc9210c8dc2", "9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721",
			"7f0e27f1487f595b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
			"9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
			"7f0e397bd097c35b0b6fc920fb0722", "9778397bd097c36b0b6fc9274c91aa", "97b6b7f0e47f531b0723b0b6fb0721",
			"7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9274c91aa",
			"97b6b7f0e47f531b0723b0787b0721", "7f0e27f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
			"9778397bd097c36b0b6fc9210c91aa", "97b6b7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
			"7f0e397bd07f595b0b0bc920fb0722", "9778397bd097c36b0b6fc9210c8dc2", "977837f0e37f149b0723b0787b0721",
			"7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f5307f595b0b0bc920fb0722", "7f0e397bd097c35b0b6fc9210c8dc2",
			"977837f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e37f1487f595b0b0bb0b6fb0722",
			"7f0e397bd097c35b0b6fc9210c8dc2", "977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
			"7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722", "977837f0e37f14998082b0787b06bd",
			"7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd097c35b0b6fc920fb0722",
			"977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
			"7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
			"7f0e27f1487f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14998082b0787b06bd",
			"7f07e7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0b0bb0b6fb0722", "7f0e397bd07f595b0b0bc920fb0722",
			"977837f0e37f14998082b0723b06bd", "7f07e7f0e37f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
			"7f0e397bd07f595b0b0bc920fb0722", "977837f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b0721",
			"7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f1487f595b0b0bb0b6fb0722", "7f0e37f0e37f14898082b0723b02d5",
			"7ec967f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0722", "7f0e37f1487f531b0b0bb0b6fb0722",
			"7f0e37f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
			"7f0e37f1487f531b0b0bb0b6fb0722", "7f0e37f0e37f14898082b072297c35", "7ec967f0e37f14998082b0787b06bd",
			"7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722", "7f0e37f0e37f14898082b072297c35",
			"7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722",
			"7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f149b0723b0787b0721",
			"7f0e27f1487f531b0b0bb0b6fb0722", "7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14998082b0723b06bd",
			"7f07e7f0e47f149b0723b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722", "7f0e37f0e366aa89801eb072297c35",
			"7ec967f0e37f14998082b0723b06bd", "7f07e7f0e37f14998083b0787b0721", "7f0e27f0e47f531b0723b0b6fb0722",
			"7f0e37f0e366aa89801eb072297c35", "7ec967f0e37f14898082b0723b02d5", "7f07e7f0e37f14998082b0787b0721",
			"7f07e7f0e47f531b0723b0b6fb0722", "7f0e36665b66aa89801e9808297c35", "665f67f0e37f14898082b0723b02d5",
			"7ec967f0e37f14998082b0787b0721", "7f07e7f0e47f531b0723b0b6fb0722", "7f0e36665b66a449801e9808297c35",
			"665f67f0e37f14898082b0723b02d5", "7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721",
			"7f0e36665b66a449801e9808297c35", "665f67f0e37f14898082b072297c35", "7ec967f0e37f14998082b0787b06bd",
			"7f07e7f0e47f531b0723b0b6fb0721", "7f0e26665b66a449801e9808297c35", "665f67f0e37f1489801eb072297c35",
			"7ec967f0e37f14998082b0787b06bd", "7f07e7f0e47f531b0723b0b6fb0721", "7f0e27f1487f531b0b0bb0b6fb0722"};

	//农历节日  *表示放假日
	private final String[] lFtv = new String[]{
			"0101 春节", "0102 大年初二", "0103 大年初三", "0104 大年初四",
			"0105 大年初五", "0106 大年初六", "0107 大年初七", "0105 路神生日",
			"0115 元宵节", "0202 龙抬头", "0219 观世音圣诞", "0404 寒食节",
			"0408 佛诞节 ", "0505 端午节", "0606 天贶节 姑姑节", "0624 彝族火把节",
			"0707 七夕情人节", "0714 鬼节(南方)", "0715 盂兰节", "0730 地藏节",
			"0815 中秋节", "0909 重阳节", "1001 祭祖节", "1117 阿弥陀佛圣诞",
			"1208 腊八节 释迦如来成道日", "1223 过小年", "1229 腊月二十九", "1230 除夕"
	};

	/**
	 * 构造方法传入日期
	 *
	 * @param date 日期
	 */
	public ChineseDate(Date date) {
		// 求出和1900年1月31日相差的天数
		int offset = (int) ((date.getTime() - baseDate) / DateUnit.DAY.getMillis());
		// 计算农历年份
		// 用offset减去每农历年的天数，计算当天是农历第几天，offset是当年的第几天
		int daysOfYear;
		int iYear = 1900;
		final int maxYear = iYear + lunarInfo.length - 1;
		for (; iYear <= maxYear; iYear++) {
			daysOfYear = yearDays(iYear);
			if (offset < daysOfYear) {
				break;
			}
			offset -= daysOfYear;
		}
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		gyear = instance.get(Calendar.YEAR);
		year = iYear;

		// 计算农历月份
		int leapMonth = leapMonth(iYear); // 闰哪个月,1-12
		// 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
		int iMonth;
		int daysOfMonth = 0;
		for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
			// 闰月
			if (leapMonth > 0 && iMonth == (leapMonth + 1) && false == leap) {
				--iMonth;
				leap = true;
				daysOfMonth = leapDays(year);
			} else {
				daysOfMonth = monthDays(year, iMonth);
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
		gmonth = instance.get(Calendar.MARCH) + 1;
		day = offset + 1;
		gday = instance.get(Calendar.DATE);
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
		int leapMonth = this.leapMonth(chineseYear);
		Integer[] integers = lunar2solar(chineseYear, chineseMonth, chineseDay, chineseMonth == leapMonth);
		if (integers != null) {
			//初始化公历年
			this.gday = integers[2];
			//初始化公历月
			this.gmonth = integers[1];
			//初始化公历日
			this.gyear = integers[0];
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
	 * 干支纪年信息
	 *
	 * @return 获得天干地支的年月日信息
	 */
	public String getCyclicalYMD() {
		if (gyear >= 1900 && gmonth > 0 && gday > 0)
			return (cyclicalm(gyear, gmonth, gday));
		return "";
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
	 * 传入 月日的offset 传回干支, 0=甲子
	 */
	private static String cyclicalm(int num) {
		final String[] Gan = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
		final String[] Zhi = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	/**
	 * 这里同步处理年月日的天干地支信息
	 *
	 * @param Y
	 * @param M
	 * @param D
	 * @return
	 */
	private String cyclicalm(int Y, int M, int D) {
		String gzyear = cyclicalm(year - 1900 + 36);
		//天干地支处理
		//返回当月「节」为几日开始
		int firstNode = this.getTerm(Y, (M * 2 - 1));
		// 依据12节气修正干支月
		String gzM = this.cyclicalm((Y - 1900) * 12 + M + 11);
		if (D >= firstNode) {
			gzM = this.cyclicalm((Y - 1900) * 12 + M + 12);
		}
		int dayCyclical = (int) ((DateUtil.parseDate(Y + "-" + M + "-" + "1").getTime() - baseDate + 2592000000L) / DateUnit.DAY.getMillis()) + 10;
		String gzD = this.cyclicalm(dayCyclical + D - 1);
		return gzyear + "年" + gzM + "月" + gzD + "日";
	}

	/**
	 * 根据节气修正干支月
	 *
	 * @param y
	 * @param n
	 * @return
	 */
	private int getTerm(int y, int n) {
		if (y < 1900 || y > 2100) {
			return -1;
		}
		if (n < 1 || n > 24) {
			return -1;
		}
		String _table = this.sTermInfo[y - 1900];
		Integer[] _info = new Integer[6];
		for (int i = 0; i < 6; i++) {
			_info[i] = parseInt("0x" + _table.substring(i * 5, 5 * (i + 1)));
		}
		String[] _calday = new String[24];
		for (int i = 0; i < 6; i++) {
			_calday[4*i] = _info[i].toString().substring(0, 1);
			_calday[4*i+1] = _info[i].toString().substring(1, 3);
			_calday[4*i+2] = _info[i].toString().substring(3, 4);
			_calday[4*i+3] = _info[i].toString().substring(4, 6);
		}
		return parseInt(_calday[n - 1]);
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
		}

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

	/**
	 * 通过农历年月日信息 返回公历信息 提供给构造函数
	 *
	 * @param chineseYear  农历年
	 * @param chineseMonth 农历月
	 * @param chineseDay   农历日
	 * @param isLeapMonth  传入的月是不是闰月
	 * @return
	 */
	private Integer[] lunar2solar(int chineseYear, int chineseMonth, int chineseDay, boolean isLeapMonth) {
		//超出了最大极限值
		if (chineseYear == 2100 && chineseMonth == 12 && chineseDay > 1 || chineseYear == 1900 && chineseMonth == 1 && chineseDay < 31) {
			return null;
		}
		int day = this.monthDays(chineseYear, chineseMonth);
		int _day = day;
		if (isLeapMonth) {
			_day = this.leapDays(chineseYear);
		}
		//参数合法性效验
		if (chineseYear < 1900 || chineseYear > 2100 || chineseDay > _day) {
			return null;
		}
		//计算农历的时间差
		int offset = 0;
		for (int i = 1900; i < chineseYear; i++) {
			offset += this.yearDays(i);
		}
		int leap = 0;
		boolean isAdd = false;
		for (int i = 1; i < chineseMonth; i++) {
			leap = this.leapMonth(chineseYear);
			if (!isAdd) {//处理闰月
				if (leap <= i && leap > 0) {
					offset += this.leapDays(chineseYear);
					isAdd = true;
				}
			}
			offset += this.monthDays(chineseYear, i);
		}
		//转换闰月农历 需补充该年闰月的前一个月的时差
		if (isLeapMonth) {
			offset += day;
		}
		//1900年农历正月一日的公历时间为1900年1月30日0时0分0秒(该时间也是本农历的最开始起始点) -2203804800000
		DateTime date = DateUtil.date(((offset + chineseDay - 31) * 86400000L) - 2203804800000L);
		int year = date.year();
		int month = date.month() + 1;
		int d = date.dayOfMonth();
		return new Integer[]{year, month, d};
	}

	// ------------------------------------------------------- private method end

}