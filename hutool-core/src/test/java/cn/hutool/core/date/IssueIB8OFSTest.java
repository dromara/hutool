package cn.hutool.core.date;

import cn.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class IssueIB8OFSTest {
	@Test
	void rangeTest() {
		DateRange startRange = DateUtil.range(
			DateUtil.parse("2017-01-01"),
			DateUtil.parse("2017-01-31"), DateField.DAY_OF_YEAR);
		DateRange endRange = DateUtil.range(
			DateUtil.parse("2017-01-31"),
			DateUtil.parse("2017-02-02"), DateField.DAY_OF_YEAR);

		List<DateTime> dateTimes = DateUtil.rangeContains(startRange, endRange);
		Assertions.assertEquals(1, dateTimes.size());

		List<DateTime> dateNotTimes = DateUtil.rangeNotContains(startRange, endRange);
		Assertions.assertEquals(2, dateNotTimes.size());
	}
}
