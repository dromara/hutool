package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RandomUtilTest {

	@Test
	public void randomEleSetTest(){
		Set<Integer> set = RandomUtil.randomEleSet(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		assertEquals(set.size(), 2);
	}

	@Test
	public void randomElesTest(){
		List<Integer> result = RandomUtil.randomEles(CollUtil.newArrayList(1, 2, 3, 4, 5, 6), 2);
		assertEquals(result.size(), 2);
	}

	@Test
	public void randomDoubleTest() {
		double randomDouble = RandomUtil.randomDouble(0, 1, 0, RoundingMode.HALF_UP);
		assertTrue(randomDouble <= 1);
	}

	@Test
	@Disabled
	public void randomBooleanTest() {
		Console.log(RandomUtil.randomBoolean());
	}

	@Test
	public void randomNumberTest() {
		final char c = RandomUtil.randomNumber();
		assertTrue(c <= '9');
	}

	@Test
	public void randomIntTest() {
		final int c = RandomUtil.randomInt(10, 100);
		assertTrue(c >= 10 && c < 100);
	}

	@Test
	public void randomBytesTest() {
		final byte[] c = RandomUtil.randomBytes(10);
		assertNotNull(c);
	}

	@Test
	public void randomChineseTest(){
		char c = RandomUtil.randomChinese();
		assertTrue(c > 0);
	}

	@Test
	@Disabled
	public void randomStringWithoutStrTest() {
		for (int i = 0; i < 100; i++) {
			final String s = RandomUtil.randomStringWithoutStr(8, "0IPOL");
			System.out.println(s);
			for (char c : "0IPOL".toCharArray()) {
				assertFalse(s.contains((String.valueOf(c).toLowerCase(Locale.ROOT))));
			}
		}
	}
}
