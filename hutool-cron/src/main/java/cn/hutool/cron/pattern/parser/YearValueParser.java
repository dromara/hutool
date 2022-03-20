package cn.hutool.cron.pattern.parser;

/**
 * 年值处理<br>
 * 年的限定在1970-2099年
 *
 * @author Looly
 */
public class YearValueParser extends SimpleValueParser {

	public YearValueParser() {
		super(1970, 2099);
	}

}
