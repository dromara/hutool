package cn.hutool.core.lang;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Range;

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
		
		final Range<DateTime> range = new Range<DateTime>(start, end, new Range.Steper<DateTime>(){

			@Override
			public DateTime step(DateTime current, DateTime end, int index) {
				if(current.isAfterOrEquals(end)) {
					return null;
				}
				return current.offsetNew(DateField.DAY_OF_YEAR, 1);
			}
			
		});
		
		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-01"));
		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-02"));
		Assert.assertFalse(range.hasNext());
	}
	
	@Test
	public void intRangeTest() {
		final Range<Integer> range = new Range<Integer>(1, 1, new Range.Steper<Integer>(){
			
			@Override
			public Integer step(Integer current, Integer end, int index) {
				return current >= end ? null : current +10;
			}
			
		});
		
		Assert.assertTrue(range.hasNext());
		Assert.assertEquals(Integer.valueOf(1), range.next());
		Assert.assertFalse(range.hasNext());
	}
}
