package com.xiaoleilu.hutool.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.xiaoleilu.hutool.cron.matcher.AlwaysTrueValueMatcher;
import com.xiaoleilu.hutool.cron.matcher.DayOfMonthValueMatcher;
import com.xiaoleilu.hutool.cron.matcher.ValueMatcher;
import com.xiaoleilu.hutool.cron.matcher.ValueMatcherBuilder;
import com.xiaoleilu.hutool.cron.parser.DayOfMonthValueParser;
import com.xiaoleilu.hutool.cron.parser.DayOfWeekValueParser;
import com.xiaoleilu.hutool.cron.parser.HourValueParser;
import com.xiaoleilu.hutool.cron.parser.MinuteValueParser;
import com.xiaoleilu.hutool.cron.parser.MonthValueParser;
import com.xiaoleilu.hutool.cron.parser.SecondValueParser;
import com.xiaoleilu.hutool.cron.parser.ValueParser;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 定时任务表达式<br>
 * 表达式类似于Linux的crontab表达式，表达式使用空格分成5个部分：
 * <ol>
 * <li><strong>分</strong>：范围：0~59</li>
 * <li><strong>时</strong>：范围：0~23</li>
 * <li><strong>日</strong>：范围：1~31，<strong>"L"</strong>表示月的最后一天</li>
 * <li><strong>月</strong>：范围：1~12，同时支持别名："jan","feb", "mar", "apr", "may","jun", "jul", "aug", "sep","oct", "nov", "dec"</li>
 * <li><strong>周</strong>：范围：0 (Sunday)~6(Saturday)，7也可以表示周日，同时支持别名："sun","mon", "tue", "wed", "thu","fri", "sat"</li>
 * </ol>
 * 当定时任务运行到的时间匹配这5个子表达式后，任务被启动。<br>
 * 对于每一个子表达式，同样支持以下形式：
 * <ul>
 * <li><strong>*</strong>：表示匹配这个位置所有的时间</li>
 * <li><strong>*&#47;2</strong>：表示步进，例如在分上，表示每两分钟，同样*可以使用数字列表代替，逗号分隔</li>
 * <li><strong>2-8</strong>：表示范围，例如在分上，表示2,3,4,5,6,7,8分</li>
 * <li><strong>cronA | cronB</strong>：表示多个定时表达式</li>
 * </ul>
 * 
 * 一些例子：
 * <ul>
 * <li><strong>5 * * * *</strong>：每个点钟的5分执行，00:05,01:05……</li>
 * <li><strong>* * * * *</strong>：每分钟执行</li>
 * <li><strong>*&#47;2 * * * *</strong>：每两小时执行</li>
 * <li><strong>* 12 * * *</strong>：12点的每分钟执行</li>
 * <li><strong>59 11 * * 1,2</strong>：每周一和周二的11:59执行</li>
 * <li><strong>3-18&#47;5 * * * *</strong>：3~18分，每5分钟执行一次，既0:03, 0:08, 0:13, 0:18, 1:03, 1:08……</li>
 * </ul>
 * 
 * @author Looly
 *
 */
public class CronPattern {

	private static final ValueParser SECOND_VALUE_PARSER = new SecondValueParser();
	private static final ValueParser MINUTE_VALUE_PARSER = new MinuteValueParser();
	private static final ValueParser HOUR_VALUE_PARSER = new HourValueParser();
	private static final ValueParser DAY_OF_MONTH_VALUE_PARSER = new DayOfMonthValueParser();
	private static final ValueParser MONTH_VALUE_PARSER = new MonthValueParser();
	private static final ValueParser DAY_OF_WEEK_VALUE_PARSER = new DayOfWeekValueParser();

	private String pattern;

	/** 秒字段匹配列表 */
	private List<ValueMatcher> secondMatchers = new ArrayList<>();
	/** 分字段匹配列表 */
	private List<ValueMatcher> minuteMatchers = new ArrayList<>();
	/** 时字段匹配列表 */
	private List<ValueMatcher> hourMatchers = new ArrayList<>();
	/** 每月几号字段匹配列表 */
	private List<ValueMatcher> dayOfMonthMatchers = new ArrayList<>();
	/** 月字段匹配列表 */
	private List<ValueMatcher> monthMatchers = new ArrayList<>();
	/** 星期字段匹配列表 */
	private List<ValueMatcher> dayOfWeekMatchers = new ArrayList<>();
	/** 匹配器个数，取决于复合任务表达式中的单一表达式个数 */
	private int matcherSize;

	/**
	 * 构造
	 * @see CronPattern
	 * 
	 * @param pattern 表达式
	 */
	public CronPattern(String pattern) {
		this.pattern = pattern;
		parseGroupPattern(pattern);
	}

	@Override
	public String toString() {
		return this.pattern;
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 * 
	 * @param millis 时间毫秒数
	 * @return 如果匹配返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public boolean match(long millis) {
		return match(TimeZone.getDefault(), millis);
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 * 
	 * @param timezone 时区 {@link TimeZone}
	 * @param millis 时间毫秒数
	 * @return 如果匹配返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public boolean match(TimeZone timezone, long millis) {
		GregorianCalendar calendar = new GregorianCalendar(timezone);
		calendar.setTimeInMillis(millis);
		return match(calendar);
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 * 
	 * @param calendar 时间
	 * @return 如果匹配返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public boolean match(GregorianCalendar calendar) {
		int second = calendar.get(Calendar.SECOND);
		int minute = calendar.get(Calendar.MINUTE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int year = calendar.get(Calendar.YEAR);

		ValueMatcher dayOfMonthMatcher;
		for (int i = 0; i < matcherSize; i++) {
			dayOfMonthMatcher = dayOfMonthMatchers.get(i);
			boolean eval = secondMatchers.get(i).match(second)//匹配秒
					&& minuteMatchers.get(i).match(minute)//匹配分
					&& hourMatchers.get(i).match(hour)//匹配时
					&& ((dayOfMonthMatcher instanceof DayOfMonthValueMatcher) ? ((DayOfMonthValueMatcher) dayOfMonthMatcher)
							.match(dayOfMonth, month, calendar.isLeapYear(year))
							: dayOfMonthMatcher.match(dayOfMonth))//匹配日
					&& monthMatchers.get(i).match(month) //匹配月
					&& dayOfWeekMatchers.get(i).match(dayOfWeek);//匹配周
			if (eval) {
				return true;
			}
		}
		return false;
	}

	// -------------------------------------------------------------------------------------- Private method start
	/**
	 * 解析复合任务表达式
	 * 
	 * @param groupPattern 复合表达式
	 */
	private void parseGroupPattern(String groupPattern) {
		List<String> patternList = StrUtil.split(groupPattern, '|');
		for (String pattern : patternList) {
			parseSinglePattern(pattern);
		}
	}

	/**
	 * 解析单一定时任务表达式
	 * 
	 * @param pattern 表达式
	 */
	private void parseSinglePattern(String pattern) {
		final String[] parts = pattern.split("\\s");
		
		int offset = 0;//偏移量用于兼容Quartz表达式，当表达式有6或7项时，第一项为秒
		if (parts.length == 6 || parts.length == 7) {
			offset = 1;
		}else if(parts.length != 5){
			throw new CronException("Pattern [{}] is invalid, it must be 5 parts!", pattern);
		}
		
		//秒
		if(1 == offset){//支持秒的表达式
			this.secondMatchers.add(ValueMatcherBuilder.build(parts[0], SECOND_VALUE_PARSER));
		}else{//不支持秒的表达式，全部匹配
			this.secondMatchers.add(new AlwaysTrueValueMatcher());
		}
		// 分
		try {
			this.minuteMatchers.add(ValueMatcherBuilder.build(parts[0+offset], MINUTE_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'minute' field error!", pattern);
		}
		// 小时
		try {
			this.hourMatchers.add(ValueMatcherBuilder.build(parts[1+offset], HOUR_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'hour' field error!", pattern);
		}
		// 每月第几天
		try {
			this.dayOfMonthMatchers.add(ValueMatcherBuilder.build(parts[2+offset], DAY_OF_MONTH_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'day of month' field error!", pattern);
		}
		// 月
		try {
			this.monthMatchers.add(ValueMatcherBuilder.build(parts[3+offset], MONTH_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'month' field error!", pattern);
		}
		// 星期几
		try {
			this.dayOfWeekMatchers.add(ValueMatcherBuilder.build(parts[4+offset], DAY_OF_WEEK_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'day of week' field error!", pattern);
		}
		matcherSize++;
	}
	// -------------------------------------------------------------------------------------- Private method end
	
	public static void main(String[] args) {
		CronPattern c = new CronPattern("1-8/2,2-10/3 * * * *");
		Console.log(c.minuteMatchers);
	}
}
