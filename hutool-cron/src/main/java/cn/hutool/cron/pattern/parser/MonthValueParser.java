package cn.hutool.cron.pattern.parser;

import cn.hutool.core.date.Month;
import cn.hutool.core.lang.Assert;
import cn.hutool.cron.CronException;

/**
 * 月份值处理<br>
 * 限定于1-12，1表示一月，支持别名（忽略大小写），如一月是{@code jan}
 *
 * @author Looly
 */
public class MonthValueParser extends AbsValueParser {

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

	/**
	 * 解析别名
	 *
	 * @param monthName 别名值
	 * @return 月份int值，从1开始
	 * @throws CronException 无效月别名抛出此异常
	 */
	private int parseAlias(String monthName) throws CronException {
		final Month month = Month.of(monthName);
		Assert.notNull(month, () -> new CronException("Invalid month alias: {}", monthName));
		return month.getValueBaseOne();
	}
}
