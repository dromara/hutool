package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.matcher.MatcherTable;

/**
 * 小时值处理<br>
 * 小时被限定在0-23
 *
 * @author Looly
 */
public class HourValueParser extends AbsValueParser {

	public HourValueParser() {
		super(0, 23);
	}

	@Override
	public void parseTo(MatcherTable matcherTable, String pattern) {
		try {
			matcherTable.hourMatchers.add(parseAsValueMatcher(pattern));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'hour' field error!", pattern);
		}
	}
}
