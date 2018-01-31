package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Month;
import cn.hutool.core.date.Season;

/**
 * DateTime单元测试
 * @author Looly
 *
 */
public class DateTimeTest {
	
	@Test
	public void datetimeTest(){
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		
		//年
		int year = dateTime.year();
		Assert.assertEquals(2017, year);
		
		//季度（非季节）
		Season season = dateTime.seasonEnum();
		Assert.assertEquals(Season.SPRING, season);
		
		//月份
		Month month = dateTime.monthEnum();
		Assert.assertEquals(Month.JANUARY, month);
		
		//日
		int day = dateTime.dayOfMonth();
		Assert.assertEquals(5, day);
	}
	
	@Test
	public void seasonTest(){
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		//季度（非季节）
		Season season = dateTime.seasonEnum();
		Assert.assertEquals(Season.SPRING, season);
		
		dateTime = new DateTime("2017-04-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		//季度（非季节）
		season = dateTime.seasonEnum();
		Assert.assertEquals(Season.SUMMER, season);
		
		dateTime = new DateTime("2017-09-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		//季度（非季节）
		season = dateTime.seasonEnum();
		Assert.assertEquals(Season.AUTUMN, season);
		
		dateTime = new DateTime("2017-12-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		//季度（非季节）
		season = dateTime.seasonEnum();
		Assert.assertEquals(Season.WINTER, season);
	}
	
	@Test
	public void mutableTest(){
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		
		//默认情况下DateTime为可变对象
		DateTime offsite = dateTime.offset(DateField.YEAR, 0);
		Assert.assertTrue(offsite == dateTime);
		
		//设置为不可变对象后变动将返回新对象
		dateTime.setMutable(false);
		offsite = dateTime.offset(DateField.YEAR, 0);
		Assert.assertFalse(offsite == dateTime);
	}
	
	@Test
	public void toStringTest(){
		DateTime dateTime = new DateTime("2017-01-05 12:34:23", DatePattern.NORM_DATETIME_FORMAT);
		Assert.assertEquals("2017-01-05 12:34:23", dateTime.toString());
		
		String dateStr = dateTime.toString("yyyy/MM/dd");
		Assert.assertEquals("2017/01/05", dateStr);
	}
	
	@Test
	public void monthTest() {
		int month = DateUtil.parse("2017-07-01").month();
		Assert.assertEquals(6, month);
	}
}
