package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.matcher.DayOfMonthValueMatcher;
import cn.hutool.cron.pattern.matcher.MatcherTable;
import cn.hutool.cron.pattern.matcher.ValueMatcher;

import java.util.List;

/**
 * 每月的几号值处理<br>
 * 每月最多31天，32和“L”都表示最后一天
 *
 * @author Looly
 *
 */
public class DayOfMonthValueParser extends AbsValueParser {

	/**
	 * 构造
	 */
	public DayOfMonthValueParser() {
		super(1, 31);
	}

	@Override
	public int parse(String value) throws CronException {
		if ("L".equalsIgnoreCase(value) || "32".equals(value)) {// 每月最后一天
			return 32;
		} else {
			return super.parse(value);
		}
	}

	@Override
	public void parseTo(MatcherTable matcherTable, String pattern) {
		try {
			matcherTable.dayOfMonthMatchers.add(parseAsValueMatcher(pattern));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'day of month' field error!", pattern);
		}
	}

	@Override
	protected ValueMatcher buildValueMatcher(List<Integer> values) {
		//考虑每月的天数不同，且存在闰年情况，日匹配单独使用
		return new DayOfMonthValueMatcher(values);
	}
}
