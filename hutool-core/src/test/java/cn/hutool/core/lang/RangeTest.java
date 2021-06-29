package cn.hutool.core.lang;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

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

	@Test
	public void rangeByStepTest() {
		DateTime start = DateUtil.parse("2017-01-01");
		DateTime end = DateUtil.parse("2017-01-03");

		// 测试包含开始和结束情况下步进为1的情况
		DateRange range = DateUtil.range(start, end, DateField.DAY_OF_YEAR);
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-01"));
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-02"));
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-03"));
		try {
			range.next();
			Assert.fail("已超过边界，下一个元素不应该存在！");
		} catch (NoSuchElementException ignored) {
		}

		// 测试多步进的情况
		range = new DateRange(start, end, DateField.DAY_OF_YEAR, 2);
		Assert.assertEquals(DateUtil.parse("2017-01-01"), range.next());
		Assert.assertEquals(DateUtil.parse("2017-01-03"), range.next());
	}

	@Test
	public void rangeDayOfYearTest(){
		DateTime start = DateUtil.parse("2017-01-01");
		DateTime end = DateUtil.parse("2017-01-05");

		// 测试不包含开始结束时间的情况
		DateRange range = new DateRange(start, end, DateField.DAY_OF_YEAR, 1, false, false);
		Assert.assertEquals(DateUtil.parse("2017-01-02"), range.next());
		Assert.assertEquals(DateUtil.parse("2017-01-03"), range.next());
		Assert.assertEquals(DateUtil.parse("2017-01-04"), range.next());
		try {
			range.next();
			Assert.fail("不包含结束时间情况下，下一个元素不应该存在！");
		} catch (NoSuchElementException ignored) {
		}
	}

	@Test
	public void rangeToListTest() {
		DateTime start = DateUtil.parse("2017-01-01");
		DateTime end = DateUtil.parse("2017-01-31");

		List<DateTime> rangeToList = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
		Assert.assertEquals(DateUtil.parse("2017-01-01"), rangeToList.get(0));
		Assert.assertEquals(DateUtil.parse("2017-01-02"), rangeToList.get(1));
	}
}
