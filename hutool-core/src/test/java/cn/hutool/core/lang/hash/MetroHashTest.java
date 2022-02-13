package cn.hutool.core.lang.hash;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * https://gitee.com/dromara/hutool/pulls/532
 */
public class MetroHashTest {

	@Test
	public void testEmpty() {
		Assert.assertEquals("31290877cceaea29", HexUtil.toHex(MetroHash.hash64(StrUtil.utf8Bytes(""), 0)));
	}

	@Test
	public void metroHash64Test() {
		byte[] str = "我是一段测试123".getBytes(CharsetUtil.CHARSET_UTF_8);
		final long hash64 = MetroHash.hash64(str);
		Assert.assertEquals(62920234463891865L, hash64);
	}

	@Test
	public void metroHash128Test() {
		byte[] str = "我是一段测试123".getBytes(CharsetUtil.CHARSET_UTF_8);
		final long[] hash128 = MetroHash.hash128(str).getLongArray();
		Assert.assertEquals(4956592424592439349L, hash128[0]);
		Assert.assertEquals(6301214698325086246L, hash128[1]);
	}

	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	@Ignore
	public void bulkHashing64Test() {
		String[] strArray = getRandomStringArray();
		long startCity = System.currentTimeMillis();
		for (String s : strArray) {
			CityHash.hash64(s.getBytes());
		}
		long endCity = System.currentTimeMillis();

		long startMetro = System.currentTimeMillis();
		for (String s : strArray) {
			MetroHash.hash64(StrUtil.utf8Bytes(s));
		}
		long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	/**
	 * 数据量越大 MetroHash 优势越明显，
	 */
	@Test
	@Ignore
	public void bulkHashing128Test() {
		String[] strArray = getRandomStringArray();
		long startCity = System.currentTimeMillis();
		for (String s : strArray) {
			CityHash.hash128(s.getBytes());
		}
		long endCity = System.currentTimeMillis();

		long startMetro = System.currentTimeMillis();
		for (String s : strArray) {
			MetroHash.hash128(StrUtil.utf8Bytes(s));
		}
		long endMetro = System.currentTimeMillis();

		System.out.println("metroHash =============" + (endMetro - startMetro));
		System.out.println("cityHash =============" + (endCity - startCity));
	}


	private static String[] getRandomStringArray() {
		String[] result = new String[10000000];
		int index = 0;
		while (index < 10000000) {
			result[index++] = RandomUtil.randomString(RandomUtil.randomInt(64));
		}
		return result;
	}
}
