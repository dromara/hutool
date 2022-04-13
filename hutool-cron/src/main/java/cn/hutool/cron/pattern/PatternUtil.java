package cn.hutool.cron.pattern;

import java.util.Calendar;

/**
 * 表达式工具，内部使用
 *
 * @author looly
 * @since 5.8.0
 */
class PatternUtil {

	/**
	 * 获取处理后的字段列表<br>
	 * 月份从1开始，周从0开始
	 *
	 * @param calendar      {@link Calendar}
	 * @param isMatchSecond 是否匹配秒，{@link false}则秒返回-1
	 * @return 字段值列表
	 * @since 5.8.0
	 */
	static int[] getFields(Calendar calendar, boolean isMatchSecond) {
		final int second = isMatchSecond ? calendar.get(Calendar.SECOND) : -1;
		final int minute = calendar.get(Calendar.MINUTE);
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		final int month = calendar.get(Calendar.MONTH) + 1;// 月份从1开始
		final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 星期从0开始，0和7都表示周日
		final int year = calendar.get(Calendar.YEAR);
		return new int[]{second, minute, hour, dayOfMonth, month, dayOfWeek, year};
	}
}
