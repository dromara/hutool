package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

public class ZodiacTest {
	
	@Test
	public void getZodiacTest() {
		Assert.assertEquals("处女座", Zodiac.getZodiac(7, 28));
	}
	
	@Test
	public void getChineseZodiacTest() {
		Assert.assertEquals("狗", Zodiac.getChineseZodiac(1994));
		Assert.assertEquals("狗", Zodiac.getChineseZodiac(2018));
		Assert.assertEquals("猪", Zodiac.getChineseZodiac(2019));
	}
}
