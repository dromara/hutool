package cn.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class DateRangeTest {
	@Test
	void issue3783Test() {
		final Date start = DateUtil.parse("2024-01-01");
		final Date end = DateUtil.parse("2024-02-01");
		final List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_MONTH, 0);
		Assertions.assertEquals(1, dateTimes.size());
		Assertions.assertEquals("2024-01-01 00:00:00", dateTimes.get(0).toString());
	}

	@Test
	void issue3783Test2() {
		final Date start = DateUtil.parse("2024-01-01");
		final Date end = DateUtil.parse("2024-02-01");
		final List<DateTime> dateTimes = DateUtil.rangeToList(start, end, DateField.DAY_OF_MONTH, -2);
		Assertions.assertEquals(1, dateTimes.size());
		Assertions.assertEquals("2024-01-01 00:00:00", dateTimes.get(0).toString());
	}
}
