package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class Issue3011Test {
	@Test
	public void isSameMonthTest() {
		// https://github.com/dromara/hutool/issues/3011
		// 判断是否同一个月，还需考虑公元前和公元后的的情况
		// 此处公元前2020年和公元2021年返回年都是2021
		final Calendar calendar1 = Calendar.getInstance();
		calendar1.set(-2020, Calendar.FEBRUARY, 12);

		final Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2021, Calendar.FEBRUARY, 12);


		Assert.assertFalse(DateUtil.isSameMonth(calendar1, calendar2));
	}
}
