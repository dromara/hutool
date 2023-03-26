package cn.hutool.core.text;

import cn.hutool.core.text.split.SplitUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SplitUtilTest {

	@Test
	public void issueI6FKSITest(){
		// issue:I6FKSI
		Assert.assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitByLength("test length 0", 0));
	}

	@Test
	public void splitToLongTest() {
		final String str = "1,2,3,4, 5";
		long[] longArray = SplitUtil.splitTo(str, ",", long[].class);
		Assert.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);

		longArray = SplitUtil.splitTo(str, ",", long[].class);
		Assert.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);
	}

	@Test
	public void splitToIntTest() {
		final String str = "1,2,3,4, 5";
		int[] intArray = SplitUtil.splitTo(str, ",", int[].class);
		Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);

		intArray = SplitUtil.splitTo(str, ",", int[].class);
		Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);
	}

	@Test
	public void splitTest() {
		final String str = "a,b ,c,d,,e";
		final List<String> split = SplitUtil.split(str, ",", -1, true, true);
		// 测试空是否被去掉
		Assert.assertEquals(5, split.size());
		// 测试去掉两边空白符是否生效
		Assert.assertEquals("b", split.get(1));

		final String[] strings = SplitUtil.splitToArray("abc/", StrUtil.SLASH);
		Assert.assertEquals(2, strings.length);
	}

	@Test
	public void splitEmptyTest() {
		final String str = "";
		final List<String> split = SplitUtil.split(str, ",", -1, true, true);
		// 测试空是否被去掉
		Assert.assertEquals(0, split.size());
	}

	@Test
	public void splitToArrayNullTest() {
		final String[] strings = SplitUtil.splitToArray(null, ".");
		Assert.assertNotNull(strings);
		Assert.assertEquals(0, strings.length);
	}

}
