package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.matcher.MatcherTable;

/**
 * 月份值处理<br>
 * 限定于1-12，1表示一月，支持别名（忽略大小写），如一月是{@code jan}
 *
 * @author Looly
 */
public class MonthValueParser extends AbsValueParser {

	/**
	 * Months aliases.
	 */
	private static final String[] ALIASES = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"};

	public MonthValueParser() {
		super(1, 12);
	}

	@Override
	public int parse(String value) throws CronException {
		try {
			return super.parse(value);
		} catch (Exception e) {
			return parseAlias(value);
		}
	}

	@Override
	public void parseTo(MatcherTable matcherTable, String pattern) {
		try {
			matcherTable.monthMatchers.add(parseAsValueMatcher(pattern));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'month' field error!", pattern);
		}
	}

	/**
	 * 解析别名
	 *
	 * @param value 别名值
	 * @return 月份int值
	 * @throws CronException 无效月别名抛出此异常
	 */
	private int parseAlias(String value) throws CronException {
		for (int i = 0; i < ALIASES.length; i++) {
			if (ALIASES[i].equalsIgnoreCase(value)) {
				return i + 1;
			}
		}
		throw new CronException("Invalid month alias: {}", value);
	}
}
