package cn.hutool.core.text.split;

import cn.hutool.core.text.StrSplitter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * {@link StrSplitter} 单元测试
 * @author Looly
 *
 */
public class StrSplitterTest {

	@Test
	public void splitByCharTest(){
		String str1 = "a, ,efedsfs,   ddf";
		List<String> split = StrSplitter.split(str1, ',', 0, true, true);

		Assert.assertEquals("ddf", split.get(2));
		Assert.assertEquals(3, split.size());
	}

	@Test
	public void splitByStrTest(){
		String str1 = "aabbccaaddaaee";
		List<String> split = StrSplitter.split(str1, "aa", 0, true, true);
		Assert.assertEquals("ee", split.get(2));
		Assert.assertEquals(3, split.size());
	}

	@Test
	public void splitByBlankTest(){
		String str1 = "aa bbccaa     ddaaee";
		List<String> split = StrSplitter.split(str1, 0);
		Assert.assertEquals("ddaaee", split.get(2));
		Assert.assertEquals(3, split.size());
	}

	@Test
	public void splitPathTest(){
		String str1 = "/use/local/bin";
		List<String> split = StrSplitter.splitPath(str1, 0);
		Assert.assertEquals("bin", split.get(2));
		Assert.assertEquals(3, split.size());
	}

	@Test
	public void splitMappingTest() {
		String str = "1.2.";
		List<Long> split = StrSplitter.split(str, '.', 0, true, true, Long::parseLong);
		Assert.assertEquals(2, split.size());
		Assert.assertEquals(Long.valueOf(1L), split.get(0));
		Assert.assertEquals(Long.valueOf(2L), split.get(1));
	}

	@Test
	public void splitEmptyTest(){
		String str = "";
		final String[] split = str.split(",");
		final String[] strings = StrSplitter.splitToArray(str, ",", -1, false, false);
		Assert.assertNotNull(strings);
		Assert.assertArrayEquals(split, strings);
	}

	@Test
	public void splitNullTest(){
		String str = null;
		final String[] strings = StrSplitter.splitToArray(str, ",", -1, false, false);
		Assert.assertNotNull(strings);
		Assert.assertEquals(0, strings.length);
	}

	/**
	 * https://github.com/dromara/hutool/issues/2099
	 */
	@Test
	public void splitByRegexTest(){
		String text = "01  821   34567890182345617821";
		List<String> strings = StrSplitter.splitByRegex(text, "21", 0, false, true);
		Assert.assertEquals(2, strings.size());
		Assert.assertEquals("01  8", strings.get(0));
		Assert.assertEquals("   345678901823456178", strings.get(1));

		strings = StrSplitter.splitByRegex(text, "21", 0, false, false);
		Assert.assertEquals(3, strings.size());
		Assert.assertEquals("01  8", strings.get(0));
		Assert.assertEquals("   345678901823456178", strings.get(1));
		Assert.assertEquals("", strings.get(2));
	}
}
