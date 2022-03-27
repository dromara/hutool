package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;

public class WeekTest {

	@Test
	public void ofTest(){
		//测试别名及大小写
		Assert.assertEquals(Week.SUNDAY, Week.of("sun"));
		Assert.assertEquals(Week.SUNDAY, Week.of("SUN"));
		Assert.assertEquals(Week.SUNDAY, Week.of("Sun"));
		//测试全名及大小写
		Assert.assertEquals(Week.SUNDAY, Week.of("sunday"));
		Assert.assertEquals(Week.SUNDAY, Week.of("Sunday"));
		Assert.assertEquals(Week.SUNDAY, Week.of("SUNDAY"));

		Assert.assertEquals(Week.MONDAY, Week.of("Mon"));
		Assert.assertEquals(Week.MONDAY, Week.of("Monday"));

		Assert.assertEquals(Week.TUESDAY, Week.of("tue"));
		Assert.assertEquals(Week.TUESDAY, Week.of("tuesday"));

		Assert.assertEquals(Week.WEDNESDAY, Week.of("wed"));
		Assert.assertEquals(Week.WEDNESDAY, Week.of("WEDNESDAY"));

		Assert.assertEquals(Week.THURSDAY, Week.of("thu"));
		Assert.assertEquals(Week.THURSDAY, Week.of("THURSDAY"));

		Assert.assertEquals(Week.FRIDAY, Week.of("fri"));
		Assert.assertEquals(Week.FRIDAY, Week.of("FRIDAY"));

		Assert.assertEquals(Week.SATURDAY, Week.of("sat"));
		Assert.assertEquals(Week.SATURDAY, Week.of("SATURDAY"));
	}

	@Test
	public void ofTest2(){
		Assert.assertEquals(Week.SUNDAY, Week.of(DayOfWeek.SUNDAY));
		Assert.assertEquals(Week.MONDAY, Week.of(DayOfWeek.MONDAY));
		Assert.assertEquals(Week.TUESDAY, Week.of(DayOfWeek.TUESDAY));
		Assert.assertEquals(Week.WEDNESDAY, Week.of(DayOfWeek.WEDNESDAY));
		Assert.assertEquals(Week.THURSDAY, Week.of(DayOfWeek.THURSDAY));
		Assert.assertEquals(Week.FRIDAY, Week.of(DayOfWeek.FRIDAY));
		Assert.assertEquals(Week.SATURDAY, Week.of(DayOfWeek.SATURDAY));
	}

	@Test
	public void toJdkDayOfWeekTest(){
		Assert.assertEquals(DayOfWeek.MONDAY, Week.MONDAY.toJdkDayOfWeek());
		Assert.assertEquals(DayOfWeek.TUESDAY, Week.TUESDAY.toJdkDayOfWeek());
		Assert.assertEquals(DayOfWeek.WEDNESDAY, Week.WEDNESDAY.toJdkDayOfWeek());
		Assert.assertEquals(DayOfWeek.THURSDAY, Week.THURSDAY.toJdkDayOfWeek());
		Assert.assertEquals(DayOfWeek.FRIDAY, Week.FRIDAY.toJdkDayOfWeek());
		Assert.assertEquals(DayOfWeek.SATURDAY, Week.SATURDAY.toJdkDayOfWeek());
		Assert.assertEquals(DayOfWeek.SUNDAY, Week.SUNDAY.toJdkDayOfWeek());
	}
}
