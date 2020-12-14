package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class MonthTest {

	@SuppressWarnings("ConstantConditions")
	@Test
	public void getLastDayTest(){
		int lastDay = Month.of(Calendar.JANUARY).getLastDay(false);
		Assert.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.FEBRUARY).getLastDay(false);
		Assert.assertEquals(28, lastDay);
		lastDay = Month.of(Calendar.FEBRUARY).getLastDay(true);
		Assert.assertEquals(29, lastDay);
		lastDay = Month.of(Calendar.MARCH).getLastDay(true);
		Assert.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.APRIL).getLastDay(true);
		Assert.assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.MAY).getLastDay(true);
		Assert.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.JUNE).getLastDay(true);
		Assert.assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.JULY).getLastDay(true);
		Assert.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.AUGUST).getLastDay(true);
		Assert.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.SEPTEMBER).getLastDay(true);
		Assert.assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.OCTOBER).getLastDay(true);
		Assert.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.NOVEMBER).getLastDay(true);
		Assert.assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.DECEMBER).getLastDay(true);
		Assert.assertEquals(31, lastDay);
	}
}
