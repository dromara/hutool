package com.xiaoleilu.hutool.cron.parser;

/**
 * {@link ValueParser} 工厂类
 * @author Looly
 *
 */
public class ValueParserFactory {
	private static final ValueParser[] PARSERS = {
			new MinuteValueParser(),
			new HourValueParser(),
			new DayOfMonthValueParser(),
			new MonthValueParser(),
			new DayOfWeekValueParser()
	};
	
	/**
	 * 获得表达式中相应位置的{@link ValueParser}
	 * @param position 位置
	 * @return {@link ValueParser}
	 */
	public static ValueParser getParser(int position){
		return PARSERS[position];
	}
}
