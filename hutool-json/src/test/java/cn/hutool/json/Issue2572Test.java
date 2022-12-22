package cn.hutool.json;

import cn.hutool.core.lang.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Issue2572Test {

	@Test
	public void putDayOfWeekTest(){
		final Set<DayOfWeek> weeks = new HashSet<>();
		weeks.add(DayOfWeek.MONDAY);
		final JSONObject obj = new JSONObject();
		obj.set("weeks", weeks);
		Assert.assertEquals("{\"weeks\":[1]}", obj.toString());

		final Map<String, Set<DayOfWeek>> monthDays1 = obj.toBean(new TypeReference<Map<String, Set<DayOfWeek>>>() {
		});
		Assert.assertEquals("{weeks=[MONDAY]}", monthDays1.toString());
	}

	@Test
	public void putMonthTest(){
		final Set<Month> months = new HashSet<>();
		months.add(Month.DECEMBER);
		final JSONObject obj = new JSONObject();
		obj.set("months", months);
		Assert.assertEquals("{\"months\":[12]}", obj.toString());

		final Map<String, Set<Month>> monthDays1 = obj.toBean(new TypeReference<Map<String, Set<Month>>>() {
		});
		Assert.assertEquals("{months=[DECEMBER]}", monthDays1.toString());
	}

	@Test
	public void putMonthDayTest(){
		final Set<MonthDay> monthDays = new HashSet<>();
		monthDays.add(MonthDay.of(Month.DECEMBER, 1));
		final JSONObject obj = new JSONObject();
		obj.set("monthDays", monthDays);
		Assert.assertEquals("{\"monthDays\":[\"--12-01\"]}", obj.toString());

		final Map<String, Set<MonthDay>> monthDays1 = obj.toBean(new TypeReference<Map<String, Set<MonthDay>>>() {
		});
		Assert.assertEquals("{monthDays=[--12-01]}", monthDays1.toString());
	}
}
