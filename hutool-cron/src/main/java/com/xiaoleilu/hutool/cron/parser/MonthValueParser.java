package com.xiaoleilu.hutool.cron.parser;

import com.xiaoleilu.hutool.cron.CronException;

/**
 * 月份值处理
 * 
 * @author Looly
 *
 */
public class MonthValueParser extends SimpleValueParser {

	/** Months aliases. */
	private static final String[] ALIASES = { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" };

	public MonthValueParser() {
		super(1, 12);
	}
	
	public int parse(String value) throws CronException {
		try {
			return super.parse(value);
		} catch (Exception e) {
			return parseAlias(value);
		}
	}

	private int parseAlias(String value) throws CronException {
		for (int i = 0; i < ALIASES.length; i++) {
			if (ALIASES[i].equalsIgnoreCase(value)) {
				return i + 1;
			}
		}
		throw new CronException("Invalid month alias: {}", value);
	}
}
