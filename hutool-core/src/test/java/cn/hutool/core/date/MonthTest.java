package cn.hutool.core.date;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonthTest {

	@SuppressWarnings("ConstantConditions")
	@Test
	public void getLastDayTest(){
		int lastDay = Month.of(Calendar.JANUARY).getLastDay(false);
		assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.FEBRUARY).getLastDay(false);
		assertEquals(28, lastDay);
		lastDay = Month.of(Calendar.FEBRUARY).getLastDay(true);
		assertEquals(29, lastDay);
		lastDay = Month.of(Calendar.MARCH).getLastDay(true);
		assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.APRIL).getLastDay(true);
		assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.MAY).getLastDay(true);
		assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.JUNE).getLastDay(true);
		assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.JULY).getLastDay(true);
		assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.AUGUST).getLastDay(true);
		assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.SEPTEMBER).getLastDay(true);
		assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.OCTOBER).getLastDay(true);
		assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.NOVEMBER).getLastDay(true);
		assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.DECEMBER).getLastDay(true);
		assertEquals(31, lastDay);
	}

	@Test
	public void toJdkMonthTest(){
		final java.time.Month month = Month.AUGUST.toJdkMonth();
		assertEquals(java.time.Month.AUGUST, month);
	}

	@Test
	public void toJdkMonthTest2(){
		assertThrows(IllegalArgumentException.class, Month.UNDECIMBER::toJdkMonth);
	}

	@Test
	public void ofTest(){
		Month month = Month.of("Jan");
		assertEquals(Month.JANUARY, month);

		month = Month.of("JAN");
		assertEquals(Month.JANUARY, month);

		month = Month.of("FEBRUARY");
		assertEquals(Month.FEBRUARY, month);

		month = Month.of("February");
		assertEquals(Month.FEBRUARY, month);

		month = Month.of(java.time.Month.FEBRUARY);
		assertEquals(Month.FEBRUARY, month);
	}
}
