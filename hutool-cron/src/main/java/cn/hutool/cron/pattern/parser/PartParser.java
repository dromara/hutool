package cn.hutool.cron.pattern.parser;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.Month;
import cn.hutool.core.date.Week;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.Part;
import cn.hutool.cron.pattern.matcher.AlwaysTrueMatcher;
import cn.hutool.cron.pattern.matcher.BoolArrayMatcher;
import cn.hutool.cron.pattern.matcher.DayOfMonthMatcher;
import cn.hutool.cron.pattern.matcher.PartMatcher;
import cn.hutool.cron.pattern.matcher.YearValueMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时任务表达式各个部分的解析器，根据{@link Part}指定不同部分，解析为{@link PartMatcher}<br>
 * 每个部分支持：
 * <ul>
 *   <li><strong>*</strong> ：表示匹配这个位置所有的时间</li>
 *   <li><strong>?</strong> ：表示匹配这个位置任意的时间（与"*"作用一致）</li>
 *   <li><strong>L</strong> ：表示匹配这个位置允许的最大值</li>
 *   <li><strong>*&#47;2</strong> ：表示间隔时间，例如在分上，表示每两分钟，同样*可以使用数字列表代替，逗号分隔</li>
 *   <li><strong>2-8</strong> ：表示连续区间，例如在分上，表示2,3,4,5,6,7,8分</li>
 *   <li><strong>2,3,5,8</strong> ：表示列表</li>
 *   <li><strong>wed</strong> ：表示周别名</li>
 *   <li><strong>jan</strong> ：表示月别名</li>
 * </ul>
 *
 * @author looly
 * @since 5.8.0
 */
public class PartParser {

	private final Part part;

	/**
	 * 创建解析器
	 *
	 * @param part 对应解析的部分枚举
	 * @return 解析器
	 */
	public static PartParser of(Part part) {
		return new PartParser(part);
	}

	/**
	 * 构造
	 * @param part 对应解析的部分枚举
	 */
	public PartParser(Part part) {
		this.part = part;
	}

	/**
	 * 将表达式解析为{@link PartMatcher}<br>
	 * <ul>
	 *     <li>* 或者 ? 返回{@link AlwaysTrueMatcher}</li>
	 *     <li>{@link Part#DAY_OF_MONTH} 返回{@link DayOfMonthMatcher}</li>
	 *     <li>{@link Part#YEAR} 返回{@link YearValueMatcher}</li>
	 *     <li>其他 返回{@link BoolArrayMatcher}</li>
	 * </ul>
	 *
	 * @param value 表达式
	 * @return {@link PartMatcher}
	 */
	public PartMatcher parse(String value) {
		if (isMatchAllStr(value)) {
			//兼容Quartz的"?"表达式，不会出现互斥情况，与"*"作用相同
			return new AlwaysTrueMatcher();
		}

		final List<Integer> values = parseArray(value);
		if (values.size() == 0) {
			throw new CronException("Invalid part value: [{}]", value);
		}

		switch (this.part) {
			case DAY_OF_MONTH:
				return new DayOfMonthMatcher(values);
			case YEAR:
				return new YearValueMatcher(values);
			default:
				return new BoolArrayMatcher(values);
		}
	}

	/**
	 * 处理数组形式表达式<br>
	 * 处理的形式包括：
	 * <ul>
	 * <li><strong>a</strong> 或 <strong>*</strong></li>
	 * <li><strong>a,b,c,d</strong></li>
	 * </ul>
	 *
	 * @param value 子表达式值
	 * @return 值列表
	 */
	private List<Integer> parseArray(String value) {
		final List<Integer> values = new ArrayList<>();

		final List<String> parts = StrUtil.split(value, StrUtil.C_COMMA);
		for (String part : parts) {
			CollUtil.addAllIfNotContains(values, parseStep(part));
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
	 * @return List
	 */
	private List<Integer> parseStep(String value) {
		final List<String> parts = StrUtil.split(value, StrUtil.C_SLASH);
		int size = parts.size();

		List<Integer> results;
		if (size == 1) {// 普通形式
			results = parseRange(value, -1);
		} else if (size == 2) {// 间隔形式
			final int step = parseNumber(parts.get(1));
			if (step < 1) {
				throw new CronException("Non positive divisor for field: [{}]", value);
			}
			results = parseRange(parts.get(0), step);
		} else {
			throw new CronException("Invalid syntax of field: [{}]", value);
		}
		return results;
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
	 * @param step  步进
	 * @return List
	 */
	private List<Integer> parseRange(String value, int step) {
		final List<Integer> results = new ArrayList<>();

		// 全部匹配形式
		if (value.length() <= 2) {
			//根据步进的第一个数字确定起始时间，类似于 12/3则从12（秒、分等）开始
			int minValue = part.getMin();
			if (false == isMatchAllStr(value)) {
				minValue = Math.max(minValue, parseNumber(value));
			} else {
				//在全匹配模式下，如果步进不存在，表示步进为1
				if (step < 1) {
					step = 1;
				}
			}
			if (step > 0) {
				final int maxValue = part.getMax();
				if (minValue > maxValue) {
					throw new CronException("Invalid value {} > {}", minValue, maxValue);
				}
				//有步进
				for (int i = minValue; i <= maxValue; i += step) {
					results.add(i);
				}
			} else {
				//固定时间
				results.add(minValue);
			}
			return results;
		}

		//Range模式
		List<String> parts = StrUtil.split(value, '-');
		int size = parts.size();
		if (size == 1) {// 普通值
			final int v1 = parseNumber(value);
			if (step > 0) {//类似 20/2的形式
				NumberUtil.appendRange(v1, part.getMax(), step, results);
			} else {
				results.add(v1);
			}
		} else if (size == 2) {// range值
			final int v1 = parseNumber(parts.get(0));
			final int v2 = parseNumber(parts.get(1));
			if (step < 1) {
				//在range模式下，如果步进不存在，表示步进为1
				step = 1;
			}
			if (v1 < v2) {// 正常范围，例如：2-5
				NumberUtil.appendRange(v1, v2, step, results);
			} else if (v1 > v2) {// 逆向范围，反选模式，例如：5-2
				NumberUtil.appendRange(v1, part.getMax(), step, results);
				NumberUtil.appendRange(part.getMin(), v2, step, results);
			} else {// v1 == v2，此时与单值模式一致
				NumberUtil.appendRange(v1, part.getMax(), step, results);
			}
		} else {
			throw new CronException("Invalid syntax of field: [{}]", value);
		}
		return results;
	}

	/**
	 * 是否为全匹配符<br>
	 * 全匹配符指 * 或者 ?
	 *
	 * @param value 被检查的值
	 * @return 是否为全匹配符
	 * @since 4.1.18
	 */
	private static boolean isMatchAllStr(String value) {
		return (1 == value.length()) && ("*".equals(value) || "?".equals(value));
	}

	/**
	 * 解析单个int值，支持别名
	 *
	 * @param value 被解析的值
	 * @return 解析结果
	 * @throws CronException 当无效数字或无效别名时抛出
	 */
	private int parseNumber(String value) throws CronException {
		int i;
		try {
			i = Integer.parseInt(value);
		} catch (NumberFormatException ignore) {
			i = parseAlias(value);
		}

		// 支持负数
		if(i < 0){
			i += part.getMax();
		}

		// 周日可以用0或7表示，统一转换为0
		if(Part.DAY_OF_WEEK.equals(this.part) && Week.SUNDAY.getIso8601Value() == i){
			i = Week.SUNDAY.ordinal();
		}

		return part.checkValue(i);
	}

	/**
	 * 解析别名支持包括：<br>
	 * <ul>
	 *     <li><strong>L 表示最大值</strong></li>
	 *     <li>{@link Part#MONTH}和{@link Part#DAY_OF_WEEK}别名</li>
	 * </ul>
	 *
	 * @param name 别名
	 * @return 解析int值
	 * @throws CronException 无匹配别名时抛出异常
	 */
	private int parseAlias(String name) throws CronException {
		if ("L".equalsIgnoreCase(name)) {
			// L表示最大值
			return part.getMax();
		}

		switch (this.part) {
			case MONTH:
				// 月份从1开始
				return Month.of(name).getValueBaseOne();
			case DAY_OF_WEEK:
				// 周从0开始，0表示周日
				return Week.of(name).ordinal();
		}

		throw new CronException("Invalid alias value: [{}]", name);
	}
}
