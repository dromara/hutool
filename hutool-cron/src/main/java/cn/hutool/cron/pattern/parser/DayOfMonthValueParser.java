package cn.hutool.cron.pattern.parser;

import cn.hutool.cron.CronException;

/**
 * 每月的几号值处理<br>
 * 每月最多31天，32和“L”都表示最后一天
 * @author Looly
 *
 */
public class DayOfMonthValueParser extends SimpleValueParser{
	
	public DayOfMonthValueParser() {
		super(1, 31);
	}

	@Override
	public int parse(String value) throws CronException{
		if (value.equalsIgnoreCase("L") || value.equals("32")) {//每月最后一天
			return 32;
		} else {
			return super.parse(value);
		}
	}
}
