package cn.hutool.cron.pattern.parser;

/**
 * 分钟值处理<br>
 * 限定于0-59
 *
 * @author Looly
 */
public class MinuteValueParser extends SimpleValueParser {

	public MinuteValueParser() {
		super(0, 59);
	}

}
