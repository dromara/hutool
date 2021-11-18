package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

public class ZodiacTest {

	@Test
	public void getZodiacTest() {
		Assert.assertEquals("摩羯座", Zodiac.getZodiac(Month.JANUARY, 19));
		Assert.assertEquals("水瓶座", Zodiac.getZodiac(Month.JANUARY, 20));
		Assert.assertEquals("巨蟹座", Zodiac.getZodiac(6, 17));
	}

	@Test
	public void getChineseZodiacTest() {
		Assert.assertEquals("狗", Zodiac.getChineseZodiac(1994));
		Assert.assertEquals("狗", Zodiac.getChineseZodiac(2018));
		Assert.assertEquals("猪", Zodiac.getChineseZodiac(2019));
	}
}
