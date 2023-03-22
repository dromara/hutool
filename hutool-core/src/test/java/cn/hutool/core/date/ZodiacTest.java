package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class ZodiacTest {

	@Test
	public void getZodiacTest() {
		Assert.assertEquals("摩羯座", Zodiac.getZodiac(Month.JANUARY, 19));
		Assert.assertEquals("水瓶座", Zodiac.getZodiac(Month.JANUARY, 20));
		Assert.assertEquals("巨蟹座", Zodiac.getZodiac(6, 17));

		final Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.JULY, 17);
		Assert.assertEquals("巨蟹座", Zodiac.getZodiac(calendar.getTime()));
		Assert.assertEquals("巨蟹座", Zodiac.getZodiac(calendar));
		Assert.assertNull(Zodiac.getZodiac((Calendar) null));
	}

	@Test
	public void getChineseZodiacTest() {
		Assert.assertEquals("狗", Zodiac.getChineseZodiac(1994));
		Assert.assertEquals("狗", Zodiac.getChineseZodiac(2018));
		Assert.assertEquals("猪", Zodiac.getChineseZodiac(2019));

		final Calendar calendar = Calendar.getInstance();
		calendar.set(2022, Calendar.JULY, 17);
		Assert.assertEquals("虎", Zodiac.getChineseZodiac(calendar.getTime()));
		Assert.assertEquals("虎", Zodiac.getChineseZodiac(calendar));
		Assert.assertNull(Zodiac.getChineseZodiac(1899));
		Assert.assertNull(Zodiac.getChineseZodiac((Calendar) null));
	}
}
