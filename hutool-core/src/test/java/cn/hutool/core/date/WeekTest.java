package cn.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Calendar;

public class WeekTest {

	@Test
	public void ofTest(){
		//测试别名及大小写
		Assertions.assertEquals(Week.SUNDAY, Week.of("sun"));
		Assertions.assertEquals(Week.SUNDAY, Week.of("SUN"));
		Assertions.assertEquals(Week.SUNDAY, Week.of("Sun"));
		//测试全名及大小写
		Assertions.assertEquals(Week.SUNDAY, Week.of("sunday"));
		Assertions.assertEquals(Week.SUNDAY, Week.of("Sunday"));
		Assertions.assertEquals(Week.SUNDAY, Week.of("SUNDAY"));

		Assertions.assertEquals(Week.MONDAY, Week.of("Mon"));
		Assertions.assertEquals(Week.MONDAY, Week.of("Monday"));

		Assertions.assertEquals(Week.TUESDAY, Week.of("tue"));
		Assertions.assertEquals(Week.TUESDAY, Week.of("tuesday"));

		Assertions.assertEquals(Week.WEDNESDAY, Week.of("wed"));
		Assertions.assertEquals(Week.WEDNESDAY, Week.of("WEDNESDAY"));

		Assertions.assertEquals(Week.THURSDAY, Week.of("thu"));
		Assertions.assertEquals(Week.THURSDAY, Week.of("THURSDAY"));

		Assertions.assertEquals(Week.FRIDAY, Week.of("fri"));
		Assertions.assertEquals(Week.FRIDAY, Week.of("FRIDAY"));

		Assertions.assertEquals(Week.SATURDAY, Week.of("sat"));
		Assertions.assertEquals(Week.SATURDAY, Week.of("SATURDAY"));
	}

	@Test
	public void ofTest2(){
		Assertions.assertEquals(Week.SUNDAY, Week.of(DayOfWeek.SUNDAY));
		Assertions.assertEquals(Week.MONDAY, Week.of(DayOfWeek.MONDAY));
		Assertions.assertEquals(Week.TUESDAY, Week.of(DayOfWeek.TUESDAY));
		Assertions.assertEquals(Week.WEDNESDAY, Week.of(DayOfWeek.WEDNESDAY));
		Assertions.assertEquals(Week.THURSDAY, Week.of(DayOfWeek.THURSDAY));
		Assertions.assertEquals(Week.FRIDAY, Week.of(DayOfWeek.FRIDAY));
		Assertions.assertEquals(Week.SATURDAY, Week.of(DayOfWeek.SATURDAY));
		Assertions.assertEquals(Week.SATURDAY, Week.of(Calendar.SATURDAY));
		Assertions.assertNull(Week.of(10));
		Assertions.assertNull(Week.of(-1));
	}

	@Test
	public void toJdkDayOfWeekTest(){
		Assertions.assertEquals(DayOfWeek.MONDAY, Week.MONDAY.toJdkDayOfWeek());
		Assertions.assertEquals(DayOfWeek.TUESDAY, Week.TUESDAY.toJdkDayOfWeek());
		Assertions.assertEquals(DayOfWeek.WEDNESDAY, Week.WEDNESDAY.toJdkDayOfWeek());
		Assertions.assertEquals(DayOfWeek.THURSDAY, Week.THURSDAY.toJdkDayOfWeek());
		Assertions.assertEquals(DayOfWeek.FRIDAY, Week.FRIDAY.toJdkDayOfWeek());
		Assertions.assertEquals(DayOfWeek.SATURDAY, Week.SATURDAY.toJdkDayOfWeek());
		Assertions.assertEquals(DayOfWeek.SUNDAY, Week.SUNDAY.toJdkDayOfWeek());
	}

	@Test
	public void toChineseTest(){
		Assertions.assertEquals("周一",Week.MONDAY.toChinese("周"));
		Assertions.assertEquals("星期一",Week.MONDAY.toChinese("星期"));
		Assertions.assertEquals("星期二",Week.TUESDAY.toChinese("星期"));
		Assertions.assertEquals("星期三",Week.WEDNESDAY.toChinese("星期"));
		Assertions.assertEquals("星期四",Week.THURSDAY.toChinese("星期"));
		Assertions.assertEquals("星期五",Week.FRIDAY.toChinese("星期"));
		Assertions.assertEquals("星期六",Week.SATURDAY.toChinese("星期"));
		Assertions.assertEquals("星期日",Week.SUNDAY.toChinese("星期"));
		Assertions.assertEquals("星期一",Week.MONDAY.toChinese());
	}
}
