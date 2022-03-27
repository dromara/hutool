package cn.hutool.cron.pattern;

import cn.hutool.core.date.Month;
import cn.hutool.core.date.Week;
import cn.hutool.core.lang.Assert;
import cn.hutool.cron.CronException;

/**
 * 表达式各个部分的枚举，用于限定位置和规则<br>
 * {@link #ordinal()}表示此部分在表达式中的位置，如0表示秒
 *
 * @author looly
 * @since 5.8.0
 */
public enum Part {
	SECOND(0, 59),
	MINUTE(0, 59),
	HOUR(0, 23),
	DAY_OF_MONTH(1, 31),
	MONTH(Month.JANUARY.getValueBaseOne(), Month.DECEMBER.getValueBaseOne()),
	DAY_OF_WEEK(Week.SUNDAY.ordinal(), Week.SATURDAY.ordinal()),
	YEAR(1970, 2099);

	private final int min;
	private final int max;

	/**
	 * 构造
	 *
	 * @param min 限定最小值（包含）
	 * @param max 限定最大值（包含）
	 */
	Part(int min, int max) {
		if (min > max) {
			this.min = max;
			this.max = min;
		} else {
			this.min = min;
			this.max = max;
		}
	}

	/**
	 * 获取最小值
	 *
	 * @return 最小值
	 */
	public int getMin() {
		return this.min;
	}

	/**
	 * 获取最大值
	 *
	 * @return 最大值
	 */
	public int getMax() {
		return this.max;
	}

	/**
	 * 检查单个值是否有效
	 *
	 * @param value 值
	 * @return 检查后的值
	 * @throws CronException 检查无效抛出此异常
	 */
	public int checkValue(int value) throws CronException {
		Assert.checkBetween(value, min, max,
				() -> new CronException("Value {} out of range: [{} , {}]", value, min, max));
		return value;
	}
}
