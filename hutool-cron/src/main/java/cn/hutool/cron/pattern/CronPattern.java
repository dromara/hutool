package cn.hutool.cron.pattern;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.pattern.matcher.PatternMatcher;
import cn.hutool.cron.pattern.parser.PatternParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 定时任务表达式<br>
 * 表达式类似于Linux的crontab表达式，表达式使用空格分成5个部分，按顺序依次为：
 * <ol>
 * <li><strong>分</strong> ：范围：0~59</li>
 * <li><strong>时</strong> ：范围：0~23</li>
 * <li><strong>日</strong> ：范围：1~31，<strong>"L"</strong> 表示月的最后一天</li>
 * <li><strong>月</strong> ：范围：1~12，同时支持不区分大小写的别名："jan","feb", "mar", "apr", "may","jun", "jul", "aug", "sep","oct", "nov", "dec"</li>
 * <li><strong>周</strong> ：范围：0 (Sunday)~6(Saturday)，7也可以表示周日，同时支持不区分大小写的别名："sun","mon", "tue", "wed", "thu","fri", "sat"，<strong>"L"</strong> 表示周六</li>
 * </ol>
 * <p>
 * 为了兼容Quartz表达式，同时支持6位和7位表达式，其中：<br>
 *
 * <pre>
 * 当为6位时，第一位表示<strong>秒</strong> ，范围0~59，但是第一位不做匹配
 * 当为7位时，最后一位表示<strong>年</strong> ，范围1970~2099，但是第7位不做解析，也不做匹配
 * </pre>
 * <p>
 * 当定时任务运行到的时间匹配这些表达式后，任务被启动。<br>
 * 注意：
 *
 * <pre>
 * 当isMatchSecond为{@code true}时才会匹配秒部分
 * 默认都是关闭的
 * </pre>
 * <p>
 * 对于每一个子表达式，同样支持以下形式：
 * <ul>
 * <li><strong>*</strong> ：表示匹配这个位置所有的时间</li>
 * <li><strong>?</strong> ：表示匹配这个位置任意的时间（与"*"作用一致）</li>
 * <li><strong>*&#47;2</strong> ：表示间隔时间，例如在分上，表示每两分钟，同样*可以使用数字列表代替，逗号分隔</li>
 * <li><strong>2-8</strong> ：表示连续区间，例如在分上，表示2,3,4,5,6,7,8分</li>
 * <li><strong>2,3,5,8</strong> ：表示列表</li>
 * <li><strong>cronA | cronB</strong> ：表示多个定时表达式</li>
 * </ul>
 * 注意：在每一个子表达式中优先级：
 *
 * <pre>
 * 间隔（/） &gt; 区间（-） &gt; 列表（,）
 * </pre>
 * <p>
 * 例如 2,3,6/3中，由于“/”优先级高，因此相当于2,3,(6/3)，结果与 2,3,6等价<br>
 * <br>
 * <p>
 * 一些例子：
 * <ul>
 * <li><strong>5 * * * *</strong> ：每个点钟的5分执行，00:05,01:05……</li>
 * <li><strong>* * * * *</strong> ：每分钟执行</li>
 * <li><strong>*&#47;2 * * * *</strong> ：每两分钟执行</li>
 * <li><strong>* 12 * * *</strong> ：12点的每分钟执行</li>
 * <li><strong>59 11 * * 1,2</strong> ：每周一和周二的11:59执行</li>
 * <li><strong>3-18&#47;5 * * * *</strong> ：3~18分，每5分钟执行一次，即0:03, 0:08, 0:13, 0:18, 1:03, 1:08……</li>
 * </ul>
 *
 * @author Looly
 */
public class CronPattern {

	private final String pattern;
	private final List<PatternMatcher> matchers;

	/**
	 * 解析表达式为 CronPattern
	 *
	 * @param pattern 表达式
	 * @return CronPattern
	 * @since 5.8.0
	 */
	public static CronPattern of(String pattern) {
		return new CronPattern(pattern);
	}

	/**
	 * 构造
	 *
	 * @param pattern 表达式
	 */
	public CronPattern(String pattern) {
		this.pattern = pattern;
		this.matchers = PatternParser.parse(pattern);
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 *
	 * @param millis        时间毫秒数
	 * @param isMatchSecond 是否匹配秒
	 * @return 如果匹配返回 {@code true}, 否则返回 {@code false}
	 */
	public boolean match(long millis, boolean isMatchSecond) {
		return match(TimeZone.getDefault(), millis, isMatchSecond);
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 *
	 * @param timezone      时区 {@link TimeZone}
	 * @param millis        时间毫秒数
	 * @param isMatchSecond 是否匹配秒
	 * @return 如果匹配返回 {@code true}, 否则返回 {@code false}
	 */
	public boolean match(TimeZone timezone, long millis, boolean isMatchSecond) {
		final GregorianCalendar calendar = new GregorianCalendar(timezone);
		calendar.setTimeInMillis(millis);
		return match(calendar, isMatchSecond);
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 *
	 * @param calendar      时间
	 * @param isMatchSecond 是否匹配秒
	 * @return 如果匹配返回 {@code true}, 否则返回 {@code false}
	 */
	public boolean match(Calendar calendar, boolean isMatchSecond) {
		final int[] fields = getFields(calendar, isMatchSecond);
		return match(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]);
	}

	/**
	 * 返回匹配到的下一个时间<br>
	 * TODO 周定义后，结果错误，需改进
	 *
	 * @param calendar 时间
	 * @return 匹配到的下一个时间
	 */
	public Calendar nextMatchAfter(Calendar calendar) {
		return nextMatchAfter(getFields(calendar, true), calendar.getTimeZone());
	}

	@Override
	public String toString() {
		return this.pattern;
	}

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
	private boolean match(int second, int minute, int hour, int dayOfMonth, int month, int dayOfWeek, int year) {
		for (PatternMatcher matcher : matchers) {
			if (matcher.match(second, minute, hour, dayOfMonth, month, dayOfWeek, year)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取下一个最近的匹配日期时间
	 *
	 * @param values 时间字段值，{second, minute, hour, dayOfMonth, month, dayOfWeek, year}
	 * @param zone   时区
	 * @return {@link Calendar}，毫秒数为0
	 */
	private Calendar nextMatchAfter(int[] values, TimeZone zone) {
		final List<Calendar> nextMatches = new ArrayList<>(matchers.size());
		for (PatternMatcher matcher : matchers) {
			nextMatches.add(matcher.nextMatchAfter(values, zone));
		}
		// 返回匹配到的最早日期
		return CollUtil.min(nextMatches);
	}

	/**
	 * 获取处理后的字段列表<br>
	 * 月份从1开始，周从0开始
	 *
	 * @param calendar      {@link Calendar}
	 * @param isMatchSecond 是否匹配秒，{@link false}则秒返回-1
	 * @return 字段值列表
	 * @since 5.8.0
	 */
	private int[] getFields(Calendar calendar, boolean isMatchSecond) {
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
