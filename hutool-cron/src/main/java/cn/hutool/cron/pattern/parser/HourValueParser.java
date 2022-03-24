package cn.hutool.cron.pattern.parser;

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
}
