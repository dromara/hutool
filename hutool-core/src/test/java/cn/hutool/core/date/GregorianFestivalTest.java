package cn.hutool.core.date;

import cn.hutool.core.date.chinese.GregorianFestival;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

/**
 * @author shl
 */
public class GregorianFestivalTest {

	@Test
	public void getPayTripleOfficialHoliday() {

		int year = 2023;
		List<LocalDate> dateList = GregorianFestival.getPayTripleOfficialHoliday(year);
		//春节大年初一
		Assert.assertTrue(dateList.contains(LocalDate.of(year, 1, 22)));
		//端午
		Assert.assertTrue(dateList.contains(LocalDate.of(year, 6, 22)));
		//中秋
		Assert.assertTrue(dateList.contains(LocalDate.of(year, 9, 29)));

		LocalDate date = GregorianFestival.getTombSweepingDate(year);
		Assert.assertEquals(LocalDate.of(year, 4, 5), date);

		year = 2024;
		date = GregorianFestival.getTombSweepingDate(year);
		Assert.assertEquals(LocalDate.of(year, 4, 4), date);

	}

}
