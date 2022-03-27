package cn.hutool.cron.pattern.matcher;

import cn.hutool.core.lang.mutable.MutableBool;
import cn.hutool.cron.pattern.Part;

import java.time.Year;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 单一表达式的匹配器，匹配器由7个{@link PartMatcher}组成，分别是：
 * <pre>
 *         0      1     2        3         4       5        6
 *      SECOND MINUTE HOUR DAY_OF_MONTH MONTH DAY_OF_WEEK YEAR
 * </pre>
 *
 * @author looly
 * @since 5.8.0
 */
public class PatternMatcher {

	private final PartMatcher[] matchers;

	/**
	 * 构造
	 *
	 * @param secondMatcher     秒匹配器
	 * @param minuteMatcher     分匹配器
	 * @param hourMatcher       时匹配器
	 * @param dayOfMonthMatcher 日匹配器
	 * @param monthMatcher      月匹配器
	 * @param dayOfWeekMatcher  周匹配器
	 * @param yearMatcher       年匹配器
	 */
	public PatternMatcher(PartMatcher secondMatcher,
						  PartMatcher minuteMatcher,
						  PartMatcher hourMatcher,
						  PartMatcher dayOfMonthMatcher,
						  PartMatcher monthMatcher,
						  PartMatcher dayOfWeekMatcher,
						  PartMatcher yearMatcher) {

		matchers = new PartMatcher[]{
				secondMatcher,
				minuteMatcher,
				hourMatcher,
				dayOfMonthMatcher,
				monthMatcher,
				dayOfWeekMatcher,
				yearMatcher
		};
	}

	/**
	 * 根据表达式位置，获取对应的{@link PartMatcher}
	 *
	 * @param part 表达式位置
	 * @return {@link PartMatcher}
	 */
	public PartMatcher get(Part part) {
		return matchers[part.ordinal()];
	}

	//region match

	/**
	 * 给定时间是否匹配定时任务表达式
	 *
	 * @param second     秒数，-1表示不匹配此项
	 * @param minute     分钟
	 * @param hour       小时
	 * @param dayOfMonth 天
	 * @param month      月，从1开始
	 * @param dayOfWeek  周，从0开始，0和7都表示周日
	 * @param year       年
	 * @return 如果匹配返回 {@code true}, 否则返回 {@code false}
	 */
	public boolean match(int second, int minute, int hour, int dayOfMonth, int month, int dayOfWeek, int year) {
		return ((second < 0) || matchers[0].match(second)) // 匹配秒（非秒匹配模式下始终返回true）
				&& matchers[1].match(minute)// 匹配分
				&& matchers[2].match(hour)// 匹配时
				&& isMatchDayOfMonth(matchers[3], dayOfMonth, month, Year.isLeap(year))// 匹配日
				&& matchers[4].match(month) // 匹配月
				&& matchers[5].match(dayOfWeek)// 匹配周
				&& matchers[6].match(year);// 匹配年
	}

	/**
	 * 是否匹配日（指定月份的第几天）
	 *
	 * @param matcher    {@link PartMatcher}
	 * @param dayOfMonth 日
	 * @param month      月
	 * @param isLeapYear 是否闰年
	 * @return 是否匹配
	 */
	private static boolean isMatchDayOfMonth(PartMatcher matcher, int dayOfMonth, int month, boolean isLeapYear) {
		return ((matcher instanceof DayOfMonthMatcher) //
				? ((DayOfMonthMatcher) matcher).match(dayOfMonth, month, isLeapYear) //
				: matcher.match(dayOfMonth));
	}
	//endregion

	//region nextMatchAfter

	/**
	 * 获取下一个匹配日期时间
	 *
	 * @param second     秒
	 * @param minute     分
	 * @param hour       时
	 * @param dayOfMonth 天
	 * @param month      月（从1开始）
	 * @param dayOfWeek  周（从0开始, 0表示周日）
	 * @param year       年
	 * @param zone       时区
	 * @return {@link Calendar}
	 */
	public Calendar nextMatchAfter(int second, int minute, int hour,
								   int dayOfMonth, int month, int dayOfWeek, int year, TimeZone zone) {

		Calendar calendar = Calendar.getInstance(zone);

		// 上一个字段不一致，说明产生了新值，下一个字段使用最小值
		MutableBool isNextEquals = new MutableBool(true);
		// 年
		final int nextYear = nextAfter(get(Part.YEAR), year, isNextEquals);
		calendar.set(Calendar.YEAR, nextYear);

		// 周
		final int nextDayOfWeek = nextAfter(get(Part.DAY_OF_WEEK), dayOfWeek, isNextEquals);
		calendar.set(Calendar.DAY_OF_WEEK, nextDayOfWeek + 1);

		// 月
		final int nextMonth = nextAfter(get(Part.MONTH), month, isNextEquals);
		calendar.set(Calendar.MONTH, nextMonth - 1);

		// 日
		final int nextDayOfMonth = nextAfter(get(Part.DAY_OF_MONTH), dayOfMonth, isNextEquals);
		calendar.set(Calendar.DAY_OF_MONTH, nextDayOfMonth);

		// 时
		final int nextHour = nextAfter(get(Part.HOUR), hour, isNextEquals);
		calendar.set(Calendar.HOUR_OF_DAY, nextHour);

		// 分
		int nextMinute = nextAfter(get(Part.MINUTE), minute, isNextEquals);
		calendar.set(Calendar.MINUTE, nextMinute);

		// 秒
		final int nextSecond = nextAfter(get(Part.SECOND), second, isNextEquals);
		calendar.set(Calendar.SECOND, nextSecond);

		return calendar;
	}

	/**
	 * 获取对应字段匹配器的下一个值
	 *
	 * @param matcher      匹配器
	 * @param value        值
	 * @param isNextEquals 是否下一个值和值相同。不同获取初始值，相同获取下一值，然后修改。
	 * @return 下一个值，-1标识匹配所有值的情况，应获取整个字段的最小值
	 */
	private static int nextAfter(PartMatcher matcher, int value, MutableBool isNextEquals) {
		int nextValue;
		if (isNextEquals.get()) {
			// 上一层级得到相同值，下级获取下个值
			nextValue = matcher.nextAfter(value);
			isNextEquals.set(nextValue == value);
		} else {
			// 上一层级的值得到了不同值，下级的所有值使用最小值
			if (matcher instanceof AlwaysTrueMatcher) {
				nextValue = value;
			} else if (matcher instanceof BoolArrayMatcher) {
				nextValue = ((BoolArrayMatcher) matcher).getMinValue();
			} else {
				throw new IllegalArgumentException("Invalid matcher: " + matcher.getClass().getName());
			}
		}
		return nextValue;
	}
	//endregion
}
