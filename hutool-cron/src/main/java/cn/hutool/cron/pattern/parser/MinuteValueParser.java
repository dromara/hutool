package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.matcher.MatcherTable;

/**
 * 分钟值处理<br>
 * 限定于0-59
 *
 * @author Looly
 */
public class MinuteValueParser extends AbsValueParser {

	/**
	 * 构造
	 */
	public MinuteValueParser() {
		super(0, 59);
	}

	@Override
	public void parseTo(MatcherTable matcherTable, String pattern) {
		try {
			matcherTable.minuteMatchers.add(parseAsValueMatcher(pattern));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'minute' field error!", pattern);
		}
	}
}
