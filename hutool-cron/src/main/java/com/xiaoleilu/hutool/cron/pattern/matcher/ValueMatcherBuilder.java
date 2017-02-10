package com.xiaoleilu.hutool.cron.pattern.matcher;

import java.util.ArrayList;
import java.util.List;

import com.xiaoleilu.hutool.cron.CronException;
import com.xiaoleilu.hutool.cron.pattern.parser.DayOfMonthValueParser;
import com.xiaoleilu.hutool.cron.pattern.parser.ValueParser;
import com.xiaoleilu.hutool.cron.pattern.parser.YearValueParser;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.NumberUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * {@link ValueMatcher} 构建器
 * @author Looly
 *
 */
public class ValueMatcherBuilder {
	/**
	 * 处理定时任务表达式每个时间字段<br>
	 * 多个时间使用逗号分隔
	 * 
	 * @param value 某个时间字段
	 * @param parser 针对这个时间字段的解析器
	 * @return List
	 */
	public static ValueMatcher build(String value, ValueParser parser) {
		if (value.length() == 1 && value.equals("*")) {
			return new AlwaysTrueValueMatcher();
		}

		List<Integer> values = parseArray(value, parser);
		if (values.size() == 0) {
			throw new CronException("Invalid field: [{}]", value);
		}

		if (parser instanceof DayOfMonthValueParser) {
			//考虑每月的天数不同，切存在闰年情况，日匹配单独使用
			return new DayOfMonthValueMatcher(values);
		}else if(parser instanceof YearValueParser){
			//考虑年数字太大，不适合boolean数组，单独使用列表遍历匹配
			return new YearValueMatcher(values);
		}else {
			return new BoolArrayValueMatcher(values);
		}
	}
	
	/**
	 * 处理数组形式表达式<br>
	 * 处理的形式包括：
	 * <ul>
	 * <li><strong>a</strong> 或 <strong>*</strong></li>
	 * <li><strong>a,b,c,d</strong></li>
	 * </ul>
	 * @param value 子表达式值
	 * @param parser 针对这个字段的解析器
	 * @return
	 */
	private static List<Integer> parseArray(String value, ValueParser parser){
		final List<Integer> values = new ArrayList<>();
	
		final List<String> parts = StrUtil.split(value, StrUtil.C_COMMA);
		for (String part : parts) {
			CollectionUtil.addAllIfNotContains(values, parseStep(part, parser));
		}
		return values;
	}

	/**
	 * 处理间隔形式的表达式<br>
	 * 处理的形式包括：
	 * <ul>
	 * <li><strong>a</strong> 或 <strong>*</strong></li>
	 * <li><strong>a&#47;b</strong> 或 <strong>*&#47;b</strong></li>
	 * <li><strong>a-b/2</strong></li>
	 * </ul>
	 * 
	 * @param value 表达式值
	 * @param parser 针对这个时间字段的解析器
	 * @return List
	 */
	private static List<Integer> parseStep(String value, ValueParser parser) {
		List<String> parts = StrUtil.split(value, StrUtil.C_SLASH);
		int size = parts.size();
		if (size == 1) {// 普通形式
			return parseRange(value, parser);
		} else if (size == 2) {// 间隔形式
			final List<Integer> rangeValues = parseRange(parts.get(0), parser);
			int step = parser.parse(parts.get(1));
			if (step < 1) {
				throw new CronException("Non positive divisor for field: [{}]", value);
			}
			// 根据定义的间隔值，返回重新生成的时间值列表
			List<Integer> values2 = new ArrayList<>();
			for (int i = 0; i < rangeValues.size(); i += step) {
				values2.add(rangeValues.get(i));
			}
			return values2;
		} else {
			throw new CronException("Invalid syntax of field: [{}]", value);
		}
	}

	/**
	 * 处理表达式中范围表达式 处理的形式包括：
	 * <ul>
	 * <li>*</li>
	 * <li>2</li>
	 * <li>3-8</li>
	 * <li>8-3</li>
	 * <li>3-3</li>
	 * </ul>
	 * 
	 * @param value 范围表达式
	 * @param parser 针对这个时间字段的解析器
	 * @return List
	 */
	private static List<Integer> parseRange(String value, ValueParser parser) {
		// 全部匹配形式
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
		if (size == 1) {// 普通值
			values.add(parser.parse(value));
			return values;
		} else if (size == 2) {// range值
			int v1 = parser.parse(parts.get(0));
			int v2 = parser.parse(parts.get(1));
			if (v1 < v2) {// 正常范围，例如：2-5
				NumberUtil.appendRange(v1, v2, values);
			} else if (v1 > v2) {// 逆向范围，反选模式，例如：5-2
				NumberUtil.appendRange(v1, parser.getMax(), values);
				NumberUtil.appendRange(parser.getMin(), v2, values);
			} else {// v1 == v2
				values.add(v1);
			}
		} else {
			throw new CronException("Invalid syntax of field: [{}]", value);
		}
		return values;
	}
}
