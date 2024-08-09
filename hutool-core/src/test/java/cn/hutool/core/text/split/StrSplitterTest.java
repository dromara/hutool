package cn.hutool.core.text.split;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.StrSplitter;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * {@link StrSplitter} 单元测试
 * @author Looly
 *
 */
public class StrSplitterTest {

	@Test
	public void splitByCharTest(){
		final String str1 = "a, ,efedsfs,   ddf";
		final List<String> split = StrSplitter.split(str1, ',', 0, true, true);

		assertEquals("ddf", split.get(2));
		assertEquals(3, split.size());
	}

	@Test
	public void splitByStrTest(){
		final String str1 = "aabbccaaddaaee";
		final List<String> split = StrSplitter.split(str1, "aa", 0, true, true);
		assertEquals("ee", split.get(2));
		assertEquals(3, split.size());
	}

	@Test
	public void splitByBlankTest(){
		final String str1 = "aa bbccaa     ddaaee";
		final List<String> split = StrSplitter.split(str1, 0);
		assertEquals("ddaaee", split.get(2));
		assertEquals(3, split.size());
	}

	@Test
	public void splitPathTest(){
		final String str1 = "/use/local/bin";
		final List<String> split = StrSplitter.splitPath(str1, 0);
		assertEquals("bin", split.get(2));
		assertEquals(3, split.size());
	}

	@Test
	public void splitMappingTest() {
		final String str = "1.2.";
		final List<Long> split = StrSplitter.split(str, '.', 0, true, true, Long::parseLong);
		assertEquals(2, split.size());
		assertEquals(Long.valueOf(1L), split.get(0));
		assertEquals(Long.valueOf(2L), split.get(1));
	}

	@Test
	public void splitEmptyTest(){
		final String str = "";
		final String[] split = str.split(",");
		final String[] strings = StrSplitter.splitToArray(str, ",", -1, false, false);
		assertNotNull(strings);
		assertArrayEquals(split, strings);
	}

	@Test
	public void splitNullTest(){
		final String str = null;
		final String[] strings = StrSplitter.splitToArray(str, ",", -1, false, false);
		assertNotNull(strings);
		assertEquals(0, strings.length);
	}

	/**
	 * https://github.com/dromara/hutool/issues/2099
	 */
	@Test
	public void splitByRegexTest(){
		final String text = "01  821   34567890182345617821";
		List<String> strings = StrSplitter.splitByRegex(text, "21", 0, false, true);
		assertEquals(2, strings.size());
		assertEquals("01  8", strings.get(0));
		assertEquals("   345678901823456178", strings.get(1));

		strings = StrSplitter.splitByRegex(text, "21", 0, false, false);
		assertEquals(3, strings.size());
		assertEquals("01  8", strings.get(0));
		assertEquals("   345678901823456178", strings.get(1));
		assertEquals("", strings.get(2));
	}

	@Test
	public void issue3421Test() {
		List<String> strings = StrSplitter.splitByRegex("", "", 0, false, false);
		assertEquals(ListUtil.of(""), strings);

		strings = StrSplitter.splitByRegex("aaa", "", 0, false, false);
		assertEquals(ListUtil.of("aaa"), strings);

		strings = StrSplitter.splitByRegex("", "aaa", 0, false, false);
		assertEquals(ListUtil.of(""), strings);

		strings = StrSplitter.splitByRegex("", "", 0, false, true);
		assertEquals(ListUtil.of(), strings);
	}
}
