package cn.hutool.core.util;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.math.NumberUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class RandomUtilTest {

	@Test
	public void randomEleSetTest(){
		final Set<Integer> set = RandomUtil.randomEleSet(ListUtil.of(1, 2, 3, 4, 5, 6), 2);
		Assert.assertEquals(set.size(), 2);
	}

	@Test
	public void randomElesTest(){
		final List<Integer> result = RandomUtil.randomEles(ListUtil.of(1, 2, 3, 4, 5, 6), 2);
		Assert.assertEquals(result.size(), 2);
	}

	@Test
	public void randomDoubleTest() {
		final double randomDouble = RandomUtil.randomDouble(0, 1, 0, RoundingMode.HALF_UP);
		Assert.assertTrue(randomDouble <= 1);
	}

	@Test
	@Ignore
	public void randomBooleanTest() {
		Console.log(RandomUtil.randomBoolean());
	}

	@Test
	public void randomNumberTest() {
		final char c = RandomUtil.randomNumber();
		Assert.assertTrue(c <= '9');
	}

	@Test
	public void randomIntTest() {
		final int c = RandomUtil.randomInt(10, 100);
		Assert.assertTrue(c >= 10 && c < 100);
	}

	@Test
	public void randomBytesTest() {
		final byte[] c = RandomUtil.randomBytes(10);
		Assert.assertNotNull(c);
	}

	@Test
	public void randomChineseTest(){
		final char c = RandomUtil.randomChinese();
		Assert.assertTrue(c > 0);
	}

	@Test
	@Ignore
	public void randomStringWithoutStrTest() {
		for (int i = 0; i < 100; i++) {
			final String s = RandomUtil.randomStringWithoutStr(8, "0IPOL");
			System.out.println(s);
			for (final char c : "0IPOL".toCharArray()) {
				Assert.assertFalse(s.contains((String.valueOf(c).toLowerCase(Locale.ROOT))));
			}
		}
	}

	@Test
	public void generateRandomNumberTest(){
		final int[] ints = RandomUtil.randomPickInts(5, NumberUtil.range(5, 20));
		Assert.assertEquals(5, ints.length);
		final Set<?> set = Convert.convert(Set.class, ints);
		Assert.assertEquals(5, set.size());
	}
}
