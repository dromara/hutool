package cn.hutool.core.lang.range;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateRange;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * {@link Range} 单元测试
 *
 * @author Looly
 */
public class RangeTest {

	@Test
	public void dateRangeTest() {
		final DateTime start = DateUtil.parse("2017-01-01");
		final DateTime end = DateUtil.parse("2017-01-02");

		final Range<DateTime> range = new Range<>(start, end, (current, end1, index) -> {
			if (current.isAfterOrEquals(end1)) {
				return null;
			}
			return current.offsetNew(DateField.DAY_OF_YEAR, 1);
		});

		Assertions.assertTrue(range.hasNext());
		Assertions.assertEquals(DateUtil.parse("2017-01-01"), range.next());
		Assertions.assertTrue(range.hasNext());
		Assertions.assertEquals(DateUtil.parse("2017-01-02"), range.next());
		Assertions.assertFalse(range.hasNext());
	}

	@Test
	public void dateRangeFuncTest() {
		final DateTime start = DateUtil.parse("2021-01-01");
		final DateTime end = DateUtil.parse("2021-01-03");

		final List<Integer> dayOfMonthList = DateUtil.rangeFunc(start, end, DateField.DAY_OF_YEAR, a -> DateTime.of(a).dayOfMonth());
		Assertions.assertArrayEquals(dayOfMonthList.toArray(new Integer[]{}), new Integer[]{1, 2, 3});

		final List<Integer> dayOfMonthList2 = DateUtil.rangeFunc(null, null, DateField.DAY_OF_YEAR, a -> DateTime.of(a).dayOfMonth());
		Assertions.assertArrayEquals(dayOfMonthList2.toArray(new Integer[]{}), new Integer[]{});
	}

	@Test
	public void dateRangeConsumeTest() {
		final DateTime start = DateUtil.parse("2021-01-01");
		final DateTime end = DateUtil.parse("2021-01-03");

		final StringBuilder sb = new StringBuilder();
		DateUtil.rangeConsume(start, end, DateField.DAY_OF_YEAR, a -> sb.append(DateTime.of(a).dayOfMonth()).append("#"));
		Assertions.assertEquals(sb.toString(), "1#2#3#");

		final StringBuilder sb2 = new StringBuilder();
		DateUtil.rangeConsume(null, null, DateField.DAY_OF_YEAR, a -> sb2.append(DateTime.of(a).dayOfMonth()).append("#"));
		Assertions.assertEquals(sb2.toString(), StrUtil.EMPTY);
	}

	@Test
	public void dateRangeTest2() {
		final DateTime start = DateUtil.parse("2021-01-31");
		final DateTime end = DateUtil.parse("2021-03-31");

		final DateRange range = DateUtil.range(start, end, DateField.MONTH);

		Assertions.assertTrue(range.hasNext());
		Assertions.assertEquals(DateUtil.parse("2021-01-31"), range.next());
		Assertions.assertTrue(range.hasNext());
		Assertions.assertEquals(DateUtil.parse("2021-02-28"), range.next());
		Assertions.assertTrue(range.hasNext());
		Assertions.assertEquals(DateUtil.parse("2021-03-31"), range.next());
		Assertions.assertFalse(range.hasNext());
	}

	@Test
	public void intRangeTest() {
		final Range<Integer> range = new Range<>(1, 1, (current, end, index) -> current >= end ? null : current + 10);

		Assertions.assertTrue(range.hasNext());
		Assertions.assertEquals(Integer.valueOf(1), range.next());
		Assertions.assertFalse(range.hasNext());
	}

	@Test
	public void rangeByStepTest() {
		final DateTime start = DateUtil.parse("2017-01-01");
		final DateTime end = DateUtil.parse("2017-01-03");

		// 测试包含开始和结束情况下步进为1的情况
		DateRange range = DateUtil.range(start, end, DateField.DAY_OF_YEAR);
		Assertions.assertEquals(range.next(), DateUtil.parse("2017-01-01"));
		Assertions.assertEquals(range.next(), DateUtil.parse("2017-01-02"));
		Assertions.assertEquals(range.next(), DateUtil.parse("2017-01-03"));
		try {
			range.next();
			Assertions.fail("已超过边界，下一个元素不应该存在！");
		} catch (final NoSuchElementException ignored) {
		}

		// 测试多步进的情况
		range = new DateRange(start, end, DateField.DAY_OF_YEAR, 2);
		Assertions.assertEquals(DateUtil.parse("2017-01-01"), range.next());
		Assertions.assertEquals(DateUtil.parse("2017-01-03"), range.next());
	}

	@Test
	public void rangeDayOfYearTest() {
		final DateTime start = DateUtil.parse("2017-01-01");
		final DateTime end = DateUtil.parse("2017-01-05");

		// 测试不包含开始结束时间的情况
		final DateRange range = new DateRange(start, end, DateField.DAY_OF_YEAR, 1, false, false);
		Assertions.assertEquals(DateUtil.parse("2017-01-02"), range.next());
		Assertions.assertEquals(DateUtil.parse("2017-01-03"), range.next());
		Assertions.assertEquals(DateUtil.parse("2017-01-04"), range.next());
		try {
			range.next();
			Assertions.fail("不包含结束时间情况下，下一个元素不应该存在！");
		} catch (final NoSuchElementException ignored) {
		}
	}

	@Test
	public void rangeToListTest() {
		final DateTime start = DateUtil.parse("2017-01-01");
		final DateTime end = DateUtil.parse("2017-01-31");

		final List<DateTime> rangeToList = DateUtil.rangeToList(start, end, DateField.DAY_OF_YEAR);
		Assertions.assertEquals(DateUtil.parse("2017-01-01"), rangeToList.get(0));
		Assertions.assertEquals(DateUtil.parse("2017-01-02"), rangeToList.get(1));
	}


	@Test
	public void rangeContains() {
		// 开始区间
		final DateTime start = DateUtil.parse("2017-01-01");
		final DateTime end = DateUtil.parse("2017-01-31");
		final DateRange startRange = DateUtil.range(start, end, DateField.DAY_OF_YEAR);
		// 结束区间
		final DateTime start1 = DateUtil.parse("2017-01-31");
		final DateTime end1 = DateUtil.parse("2017-02-02");
		final DateRange endRange = DateUtil.range(start1, end1, DateField.DAY_OF_YEAR);
		// 交集
		final List<DateTime> dateTimes = DateUtil.rangeContains(startRange, endRange);
		Assertions.assertEquals(1, dateTimes.size());
		Assertions.assertEquals(DateUtil.parse("2017-01-31"), dateTimes.get(0));
	}

	@Test
	public void rangeNotContains() {
		// 开始区间
		final DateTime start = DateUtil.parse("2017-01-01");
		final DateTime end = DateUtil.parse("2017-01-30");
		final DateRange startRange = DateUtil.range(start, end, DateField.DAY_OF_YEAR);
		// 结束区间
		final DateTime start1 = DateUtil.parse("2017-01-01");
		final DateTime end1 = DateUtil.parse("2017-01-31");
		final DateRange endRange = DateUtil.range(start1, end1, DateField.DAY_OF_YEAR);
		// 差集
		final List<DateTime> dateTimes1 = DateUtil.rangeNotContains(startRange, endRange);

		Assertions.assertEquals(1, dateTimes1.size());
		Assertions.assertEquals(DateUtil.parse("2017-01-31"), dateTimes1.get(0));
	}

}
