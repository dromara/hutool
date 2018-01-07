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
		DateTime end = DateUtil.parse("2017-01-31");
		
		final Range<DateTime> range = new Range<DateTime>(start, end, new Range.Steper<DateTime>(){

			@Override
			public DateTime step(DateTime current, DateTime end, int index) {
				if(current.isAfterOrEquals(end)) {
					return null;
				}
				return current.offsetNew(DateField.DAY_OF_YEAR, 1);
			}
			
		});
		
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-01"));
		Assert.assertEquals(range.next(), DateUtil.parse("2017-01-02"));
	}
}
