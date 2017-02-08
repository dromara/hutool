package com.xiaoleilu.hutool.cron.parser;

import com.xiaoleilu.hutool.cron.CronException;

/**
 * 星期值处理<br>
 * 1表示星期一，2表示星期二，依次类推，0和7都可以表示星期日
 * 
 * @author Looly
 *
 */
public class DayOfWeekValueParser extends SimpleValueParser {
	
	/** Weeks aliases. */
	private static final String[] ALIASES = { "sun", "mon", "tue", "wed", "thu", "fri", "sat" };

	public DayOfWeekValueParser() {
		super(0, 7);
	}
	
	/**
	 * 对于星期提供转换<br>
	 * 1表示星期一，2表示星期二，依次类推，0和7都可以表示星期日
	 */
	@Override
	public int parse(String value) throws CronException {
		try {
			return super.parse(value) % 7;
		} catch (Exception e) {
			return parseAlias(value);
		}
	}

	private int parseAlias(String value) throws CronException {
		for (int i = 0; i < ALIASES.length; i++) {
			if (ALIASES[i].equalsIgnoreCase(value)) {
				return i;
			}
		}
		throw new CronException("Invalid month alias: {}", value);
	}
}
