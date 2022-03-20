package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.matcher.MatcherTable;

/**
 * 秒值处理<br>
 * 限定于0-59
 *
 * @author Looly
 */
public class SecondValueParser extends MinuteValueParser {

	@Override
	public void parseTo(MatcherTable matcherTable, String pattern) {
		try {
			matcherTable.secondMatchers.add(parseAsValueMatcher(pattern));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'second' field error!", pattern);
		}
	}
}
