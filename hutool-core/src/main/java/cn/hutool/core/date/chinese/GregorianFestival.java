package cn.hutool.core.date.chinese;

import cn.hutool.core.date.ChineseDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 节假日（公历）封装
 *
 * @author shl
 */
public class GregorianFestival {


	/**
	 * 获取需要支付三倍薪资的法定假日
	 * 说明：
	 * 根据国务院2013年12月11日发布，自2014年1月1日起施行的《全国年节及纪念日放假办法》（第三次修订版）
	 * 我国现行的、全体公民放假的法定节假日，包括以下:
	 * 1.新年，放假1天（1月1日）；
	 * 2.春节，放假3天（农历正月初一、初二、初三）；
	 * 3.清明节，放假1天（农历清明当日）；
	 * 4.劳动节，放假1天（5月1日）；
	 * 5.端午节，放假1天（农历端午当日）；
	 * 6.中秋节，放假1天（农历中秋当日）；
	 * 7.国庆节，放假3天（10月1日、2日、3日）
	 *
	 * @param year 年
	 * @return 返回公历的日期
	 */
	public static List<LocalDate> getPayTripleOfficialHoliday(int year) {
		List<LocalDate> result = new ArrayList<>();
		//元旦
		result.add(LocalDate.of(year, 1, 1));
		//春节
		result.add(chineseToGregorianCalendar(year, 1, 1));
		result.add(chineseToGregorianCalendar(year, 1, 2));
		result.add(chineseToGregorianCalendar(year, 1, 3));
		//清明节
		result.add(getTombSweepingDate(year));
		//劳动节
		result.add(LocalDate.of(year, 5, 1));
		//端午节
		result.add(chineseToGregorianCalendar(year, 5, 5));
		//中秋节
		result.add(chineseToGregorianCalendar(year, 8, 15));
		//国庆节
		result.add(LocalDate.of(year, 10, 1));
		result.add(LocalDate.of(year, 10, 2));
		result.add(LocalDate.of(year, 10, 3));
		return result.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * 获取清明节日期
	 * 说明：
	 * 计算公示：
	 * (Y*D+C)-L
	 * 公式解读:Y=年数后2位，D=0.2422，L=闰年数，21世纪C=4.81，20世纪C=5.59
	 *
	 * @param year 年
	 * @return 返回公历的日期
	 */
	public static LocalDate getTombSweepingDate(int year) {
		int y = year % 100;
		double day = (y * 0.2422 + 4.81) - (y / 4);
		return LocalDate.of(year, 4, (int) day);
	}

	/**
	 * 农历转公历
	 *
	 * @param year  年，如：2023
	 * @param month 月，如：1-12
	 * @param day   日, 如：1-31
	 * @return 返回公历的日期
	 */
	private static LocalDate chineseToGregorianCalendar(int year, int month, int day) {
		ChineseDate chineseDate = new ChineseDate(year, month, day);
		return LocalDate.of(chineseDate.getGregorianYear(), chineseDate.getGregorianMonth() + 1, chineseDate.getGregorianDay());
	}

}
