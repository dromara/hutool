package org.dromara.hutool.util;

import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.math.NumberUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RandomUtilTest {

	@Test
	public void randomEleSetTest(){
		final Set<Integer> set = RandomUtil.randomEleSet(ListUtil.of(1, 2, 3, 4, 5, 6), 2);
		Assertions.assertEquals(set.size(), 2);
	}

	@Test
	public void randomElesTest(){
		final List<Integer> result = RandomUtil.randomEles(ListUtil.of(1, 2, 3, 4, 5, 6), 2);
		Assertions.assertEquals(result.size(), 2);
	}

	@Test
	public void randomDoubleTest() {
		final double randomDouble = RandomUtil.randomDouble(0, 1, 0, RoundingMode.HALF_UP);
		Assertions.assertTrue(randomDouble <= 1);
	}

	@Test
	@Disabled
	public void randomBooleanTest() {
		Console.log(RandomUtil.randomBoolean());
	}

	@Test
	public void randomNumberTest() {
		final char c = RandomUtil.randomNumber();
		Assertions.assertTrue(c <= '9');
	}

	@Test
	public void randomIntTest() {
		final int c = RandomUtil.randomInt(10, 100);
		Assertions.assertTrue(c >= 10 && c < 100);
	}

	@Test
	public void randomBytesTest() {
		final byte[] c = RandomUtil.randomBytes(10);
		Assertions.assertNotNull(c);
	}

	@Test
	public void randomChineseTest(){
		final char c = RandomUtil.randomChinese();
		Assertions.assertTrue(c > 0);
	}

	@Test
	@Disabled
	public void randomStringWithoutStrTest() {
		for (int i = 0; i < 100; i++) {
			final String s = RandomUtil.randomStringWithoutStr(8, "0IPOL");
			System.out.println(s);
			for (final char c : "0IPOL".toCharArray()) {
				Assertions.assertFalse(s.contains((String.valueOf(c).toLowerCase(Locale.ROOT))));
			}
		}
	}

	@Test
	public void randomStringOfLengthTest(){
		final String s = RandomUtil.randomString("123", -1);
		Assertions.assertNotNull(s);
	}

	@Test
	public void generateRandomNumberTest(){
		final int[] ints = RandomUtil.randomPickInts(5, NumberUtil.range(5, 20));
		Assertions.assertEquals(5, ints.length);
		final Set<?> set = Convert.convert(Set.class, ints);
		Assertions.assertEquals(5, set.size());
	}
}
