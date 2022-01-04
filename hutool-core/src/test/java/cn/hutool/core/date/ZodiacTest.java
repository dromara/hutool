package cn.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ZodiacTest {

	@Test
	public void getZodiacTest() {
		Assertions.assertEquals("摩羯座", Zodiac.getZodiac(Month.JANUARY, 19));
		Assertions.assertEquals("水瓶座", Zodiac.getZodiac(Month.JANUARY, 20));
		Assertions.assertEquals("巨蟹座", Zodiac.getZodiac(6, 17));
	}

	@Test
	public void getChineseZodiacTest() {
		Assertions.assertEquals("狗", Zodiac.getChineseZodiac(1994));
		Assertions.assertEquals("狗", Zodiac.getChineseZodiac(2018));
		Assertions.assertEquals("猪", Zodiac.getChineseZodiac(2019));
	}
}
