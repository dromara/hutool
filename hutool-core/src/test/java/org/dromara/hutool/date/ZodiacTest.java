package org.dromara.hutool.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

public class ZodiacTest {

	@Test
	public void getZodiacTest() {
		Assertions.assertEquals("摩羯座", Zodiac.getZodiac(Month.JANUARY, 19));
		Assertions.assertEquals("水瓶座", Zodiac.getZodiac(Month.JANUARY, 20));
		Assertions.assertEquals("巨蟹座", Zodiac.getZodiac(6, 17));
		final Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.JULY, 17);
		Assertions.assertEquals("巨蟹座", Zodiac.getZodiac(calendar.getTime()));
		Assertions.assertEquals("巨蟹座", Zodiac.getZodiac(calendar));
		Assertions.assertNull(Zodiac.getZodiac((Calendar) null));
	}

	@Test
	public void getChineseZodiacTest() {
		Assertions.assertEquals("狗", Zodiac.getChineseZodiac(1994));
		Assertions.assertEquals("狗", Zodiac.getChineseZodiac(2018));
		Assertions.assertEquals("猪", Zodiac.getChineseZodiac(2019));
		final Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.JULY, 17);
		Assertions.assertEquals("虎", Zodiac.getChineseZodiac(calendar.getTime()));
		Assertions.assertEquals("虎", Zodiac.getChineseZodiac(calendar));
		Assertions.assertNull(Zodiac.getChineseZodiac(1899));
		Assertions.assertNull(Zodiac.getChineseZodiac((Calendar) null));
	}

	@Test
	public void getZodiacOutOfRangeTest() {
		// https://github.com/dromara/hutool/issues/3036
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			DateUtil.getZodiac(Month.UNDECIMBER.getValue(), 10);
		});
	}
}
