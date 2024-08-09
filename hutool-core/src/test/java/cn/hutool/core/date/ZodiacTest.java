package cn.hutool.core.date;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

public class ZodiacTest {

	@Test
	public void getZodiacTest() {
		assertEquals("摩羯座", Zodiac.getZodiac(Month.JANUARY, 19));
		assertEquals("水瓶座", Zodiac.getZodiac(Month.JANUARY, 20));
		assertEquals("巨蟹座", Zodiac.getZodiac(6, 17));

		final Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.JULY, 17);
		assertEquals("巨蟹座", Zodiac.getZodiac(calendar.getTime()));
		assertEquals("巨蟹座", Zodiac.getZodiac(calendar));
		assertNull(Zodiac.getZodiac((Calendar) null));
	}

	@Test
	public void getChineseZodiacTest() {
		assertEquals("狗", Zodiac.getChineseZodiac(1994));
		assertEquals("狗", Zodiac.getChineseZodiac(2018));
		assertEquals("猪", Zodiac.getChineseZodiac(2019));

		final Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.JULY, 17);
		assertEquals("虎", Zodiac.getChineseZodiac(calendar.getTime()));
		assertEquals("虎", Zodiac.getChineseZodiac(calendar));
		assertNull(Zodiac.getChineseZodiac(1899));
		assertNull(Zodiac.getChineseZodiac((Calendar) null));
	}
}
