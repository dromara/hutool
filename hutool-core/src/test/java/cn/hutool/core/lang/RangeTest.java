package cn.hutool.core.lang;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@link Range} 单元测试
 * @author Looly
 *
 */
public class RangeTest {

	@Test
	public void dateRangeTest() {
		DateTime start = DateUtil.parse("2017-01-01");
		DateTime end = DateUtil.parse("2017-01-02");

		final Range<DateTime> range = new Range<>(start, end, (current, end1, index) -> {
			if (current.isAfterOrEquals(end1)) {
				return null;
			}
			return current.offsetNew(DateField.DAY_OF_YEAR, 1);
		});

		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(DateUtil.parse("2017-01-01"), range.next());
		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(DateUtil.parse("2017-01-02"), range.next());
		Assert.assertFalse(range.hasNext());
	}

	@Test
	public void dateRangeTest2() {
		DateTime start = DateUtil.parse("2021-01-31");
		DateTime end = DateUtil.parse("2021-03-31");

		final DateRange range = DateUtil.range(start, end, DateField.MONTH);

		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(DateUtil.parse("2021-01-31"), range.next());
		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(DateUtil.parse("2021-02-28"), range.next());
		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(DateUtil.parse("2021-03-31"), range.next());
		Assert.assertFalse(range.hasNext());
	}

	@Test
	public void intRangeTest() {
		final Range<Integer> range = new Range<>(1, 1, (current, end, index) -> current >= end ? null : current + 10);

		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(Integer.valueOf(1), range.next());
		Assert.assertFalse(range.hasNext());
	}
}
