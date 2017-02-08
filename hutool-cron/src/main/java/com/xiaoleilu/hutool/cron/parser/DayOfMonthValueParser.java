package com.xiaoleilu.hutool.cron.parser;

import com.xiaoleilu.hutool.cron.CronException;

/**
 * 每月的几号值处理
 * @author Looly
 *
 */
public class DayOfMonthValueParser extends SimpleValueParser{
	
	public DayOfMonthValueParser() {
		super(1, 31);
	}

	@Override
	public int parse(String value) throws CronException{
		if (value.equalsIgnoreCase("L")) {//每月最后一天
			return 32;
		} else {
			return super.parse(value);
		}
	}
}
