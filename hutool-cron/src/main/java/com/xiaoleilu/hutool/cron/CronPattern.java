package com.xiaoleilu.hutool.cron;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import com.xiaoleilu.hutool.cron.matcher.AlwaysTrueValueMatcher;
import com.xiaoleilu.hutool.cron.matcher.DayOfMonthValueMatcher;
import com.xiaoleilu.hutool.cron.matcher.IntArrayValueMatcher;
import com.xiaoleilu.hutool.cron.matcher.ValueMatcher;
import com.xiaoleilu.hutool.cron.parser.DayOfMonthValueParser;
import com.xiaoleilu.hutool.cron.parser.DayOfWeekValueParser;
import com.xiaoleilu.hutool.cron.parser.HourValueParser;
import com.xiaoleilu.hutool.cron.parser.MinuteValueParser;
import com.xiaoleilu.hutool.cron.parser.MonthValueParser;
import com.xiaoleilu.hutool.cron.parser.ValueParser;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.NumberUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 定时任务表达式<br>
 * 定义并解析定时任务表达式，通过match方法判断是否给定的时间匹配这个表达式，匹配则表示需要在这个时间点执行Task
 * @author Looly
 *
 */
public class CronPattern {
	
	private static final ValueParser MINUTE_VALUE_PARSER = new MinuteValueParser();
	private static final ValueParser HOUR_VALUE_PARSER = new HourValueParser();
	private static final ValueParser DAY_OF_MONTH_VALUE_PARSER = new DayOfMonthValueParser();
	private static final ValueParser MONTH_VALUE_PARSER = new MonthValueParser();
	private static final ValueParser DAY_OF_WEEK_VALUE_PARSER = new DayOfWeekValueParser();
	
	private String pattern;
	
	/** 分钟字段匹配列表 */
	private List<ValueMatcher> minuteMatchers = new ArrayList<>();
	/** 小时字段匹配列表 */
	private List<ValueMatcher> hourMatchers = new ArrayList<>();
	/** 每月几号字段匹配列表 */
	private List<DayOfMonthValueMatcher> dayOfMonthMatchers = new ArrayList<>();
	/** 月份字段匹配列表 */
	private List<ValueMatcher> monthMatchers = new ArrayList<>();
	/** 星期字段匹配列表 */
	private List<ValueMatcher> dayOfWeekMatchers = new ArrayList<>();
	/** 匹配器个数，取决于复合任务表达式中的单一表达式个数 */
	private int matcherSize;
	
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
	 * @param millis 时间毫秒数
	 * @return 如果匹配返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public boolean match(long millis){
		return match(TimeZone.getDefault(), millis);
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 * @param timezone 时区 {@link TimeZone}
	 * @param millis 时间毫秒数
	 * @return 如果匹配返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public boolean match(TimeZone timezone, long millis){
		GregorianCalendar calendar = new GregorianCalendar(timezone);
		calendar.setTimeInMillis(millis);
		return match(calendar);
	}
	
	/**
	 * 给定时间是否匹配定时任务表达式
	 * @param calendar 时间
	 * @return 如果匹配返回 <code>true</code>, 否则返回 <code>false</code>
	 */
	public boolean match(GregorianCalendar calendar){
		int minute = calendar.get(Calendar.MINUTE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int year = calendar.get(Calendar.YEAR);
		
		for (int i = 0; i < matcherSize; i++) {
			boolean eval = minuteMatchers.get(i).match(minute)
					&& hourMatchers.get(i).match(hour)
					&& dayOfMonthMatchers.get(i).match(dayOfMonth, month, calendar.isLeapYear(year))
					&& monthMatchers.get(i).match(month)
					&& dayOfWeekMatchers.get(i).match(dayOfWeek);
			if (eval) {
				return true;
			}
		}
		return false;
	}
	
	
	//-------------------------------------------------------------------------------------- Private method start
	/**
	 * 解析复合任务表达式
	 * @param groupPattern 复合表达式
	 */
	private void parseGroupPattern(String groupPattern){
		List<String> patternList = StrUtil.split(groupPattern, '|');
		for (String pattern : patternList) {
			parseSinglePattern(pattern);
		}
	}
	
	/**
	 * 解析单一定时任务表达式
	 * @param pattern 表达式
	 */
	private void parseSinglePattern(String pattern){
		List<String> partList = StrUtil.split(pattern, StrUtil.C_SPACE);
		if(partList.size() != 5){
			throw new CronException("Pattern [{}] is invalid, it must be 5 parts!", pattern);
		}
		
		//分
		try {
			this.minuteMatchers.add(buildValueMatcher(partList.get(0), MINUTE_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing minute field error!", pattern);
		}
		//小时
		try {
			this.hourMatchers.add(buildValueMatcher(partList.get(1), HOUR_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing hour field error!", pattern);
		}
		//每月第几天
		try {
			this.dayOfMonthMatchers.add((DayOfMonthValueMatcher)buildValueMatcher(partList.get(2), DAY_OF_MONTH_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing day of month field error!", pattern);
		}
		//月
		try {
			this.monthMatchers.add(buildValueMatcher(partList.get(3), MONTH_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing month field error!", pattern);
		}
		//星期几
		try {
			this.dayOfWeekMatchers.add(buildValueMatcher(partList.get(4), DAY_OF_WEEK_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing day of week field error!", pattern);
		}
		matcherSize++;
	}
	
	/**
	 * 处理定时任务表达式每个时间字段<br>
	 * 多个时间使用逗号分隔
	 * 
	 * @param value 某个时间字段
	 * @param parser 针对这个时间字段的解析器
	 * @return List
	 */
	private ValueMatcher buildValueMatcher(String value, ValueParser parser){
		if (value.length() == 1 && value.equals("*")) {
			return new AlwaysTrueValueMatcher();
		}
		
		List<Integer> values = new ArrayList<>();
		
		List<String> parts = StrUtil.split(value, StrUtil.C_COMMA);
		for (String part : parts) {
			CollectionUtil.addAllIfNotContains(values, parseDivisor(part, parser));
		}
		if(values.size() == 0){
			throw new CronException("Invalid field: [{}]", value);
		}
		
		if(parser instanceof DayOfMonthValueParser){
			return new DayOfMonthValueMatcher(values);
		}else{
			return new IntArrayValueMatcher(values);
		}
	}
	
	/**
	 * 处理分数形式的表达式（间隔多久的形式）<br>
	 * 处理的形式包括：
	 * <ul>
	 * 		<li>3</li>
	 * 		<li>* / 2</li>
	 * 		<li>3-8/2</li>
	 * </ul>
	 * @param value 逗号分隔的表达式
	 * @param parser 针对这个时间字段的解析器
	 * @return List
	 */
	private List<Integer> parseDivisor(String value, ValueParser parser){
		List<String> parts = StrUtil.split(value, StrUtil.C_SLASH);
		int size = parts.size();
		if(size == 1){//普通形式
			return parseRange(value, parser);
		}else if(size == 2){//间隔形式
			final List<Integer> values = parseRange(parts.get(0), parser);
			int div = str2Int(parts.get(1), "divisor");
			if(div < 1){
				throw new CronException("Non positive divisor for field: [{}]", value);
			}
			//根据定义的间隔值，返回重新生成的时间值列表
			List<Integer> values2 = new ArrayList<>();
			for(int i = 0; i < values.size(); i += div){
				values2.add(values.get(i));
			}
			return values2;
		}else{
			throw new CronException("Invalid syntax of field: [{}]", value);
		}
	}
	
	/**
	 * 处理表达式中范围表达式
	 * 处理的形式包括：
	 * <ul>
	 * 		<li>*</li>
	 * 		<li>2</li>
	 * 		<li>3-8</li>
	 * 		<li>8-3</li>
	 * 		<li>3-3</li>
	 * </ul>
	 * @param value 范围表达式
	 * @param parser 针对这个时间字段的解析器
	 * @return List
	 */
	private List<Integer> parseRange(String value, ValueParser parser){
		//全部匹配形式
		if (value.length() == 1 && value.equals("*")) {
			List<Integer> values = new ArrayList<>();
			for (int i = parser.getMin(); i <= parser.getMax(); i++) {
				values.add(i);
			}
			return values;
		}
		
		List<String> parts = StrUtil.split(value, '-');
		int size = parts.size();
		List<Integer> values = new ArrayList<>();
		if(size == 1){//普通值
			values.add(str2Int(value, "value"));
			return values;
		}else if(size == 2){//range值
			int v1 = str2Int(parts.get(0), "left value");
			int v2 = str2Int(parts.get(1), "right value");
			if(v1 < v2){//正常范围，例如：2-5
				NumberUtil.appendRange(v1, v2, values);
			}else if(v1 > v2){//逆向范围，反选模式，例如：5-2
				NumberUtil.appendRange(v1, parser.getMax(), values);
				NumberUtil.appendRange(parser.getMin(), v2, values);
			}else{// v1 == v2
				values.add(v1);
			}
		}else{
			throw new CronException("Invalid syntax of field: [{}]", value);
		}
		return values;
	}
	
	/**
	 * String转int<br>
	 * 转换错误变为表达式语法错误
	 * @param str String值
	 * @param name 转换失败后提示的失败字段名
	 * @return 转换后的int
	 */
	private static int str2Int(String str, String name){
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new CronException("Invalid {} for field: [{}]", name, str);
		}
	}
	//-------------------------------------------------------------------------------------- Private method end
}
