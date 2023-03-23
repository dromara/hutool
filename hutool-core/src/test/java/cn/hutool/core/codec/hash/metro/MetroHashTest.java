package cn.hutool.core.codec.hash.metro;


import cn.hutool.core.codec.HexUtil;
import cn.hutool.core.codec.hash.CityHash;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * https://gitee.com/dromara/hutool/pulls/532
 */
public class MetroHashTest {

	@Test
	public void testEmpty() {
		Assert.assertEquals("705fb008071e967d", HexUtil.toHex(MetroHash64.of(0).hash64(ByteUtil.toUtf8Bytes(""))));
	}

	@Test
	public void test1Low() {
		Assert.assertEquals("AF6F242B7ED32BCB", h64("a"));
	}

	@Test
	public void test1High() {
		Assert.assertEquals("D51BA21D219C37B3", h64("é"));
	}

	@Test
	public void metroHash64Test() {
		final byte[] str = "我是一段测试123".getBytes(CharsetUtil.UTF_8);
		final long hash64 = MetroHash64.of(0).hash64(str);
		Assert.assertEquals(147395857347476456L, hash64);
	}

	@Test
	public void metroHash128Test() {
		final byte[] str = "我是一段测试123".getBytes(CharsetUtil.UTF_8);
		final long[] hash128 = MetroHash128.of(0).hash128(str).getLongArray();
		Assert.assertEquals(228255164667538345L, hash128[0]);
		Assert.assertEquals(-6394585948993412256L, hash128[1]);
	}

	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	@Ignore
	public void bulkHashing64Test() {
		final String[] strArray = getRandomStringArray();
		final long startCity = System.currentTimeMillis();
		for (final String s : strArray) {
			CityHash.INSTANCE.hash64(s.getBytes());
		}
		final long endCity = System.currentTimeMillis();

		final long startMetro = System.currentTimeMillis();
		for (final String s : strArray) {
			MetroHash64.of(0).hash64(ByteUtil.toUtf8Bytes(s));
		}
		final long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	@Ignore
	public void bulkHashing128Test() {
		final String[] strArray = getRandomStringArray();
		final long startCity = System.currentTimeMillis();
		for (final String s : strArray) {
			CityHash.INSTANCE.hash128(s.getBytes());
		}
		final long endCity = System.currentTimeMillis();

		final long startMetro = System.currentTimeMillis();
		for (final String s : strArray) {
			MetroHash128.of(0).hash128(ByteUtil.toUtf8Bytes(s));
		}
		final long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	private static String[] getRandomStringArray() {
		final String[] result = new String[10000000];
		int index = 0;
		while (index < 10000000) {
			result[index++] = RandomUtil.randomString(RandomUtil.randomInt(64));
		}
		return result;
	}

	private String h64(final String value) {
		return HexUtil.toHex(MetroHash64.of(0).hash64(ByteUtil.toUtf8Bytes(value))).toUpperCase();
	}
}
