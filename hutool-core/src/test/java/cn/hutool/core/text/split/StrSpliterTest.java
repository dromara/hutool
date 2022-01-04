package cn.hutool.core.text.split;

import cn.hutool.core.text.StrSplitter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * {@link StrSplitter} 单元测试
 * @author Looly
 *
 */
public class StrSpliterTest {

	@Test
	public void splitByCharTest(){
		String str1 = "a, ,efedsfs,   ddf";
		List<String> split = StrSplitter.split(str1, ',', 0, true, true);

		Assertions.assertEquals("ddf", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitByStrTest(){
		String str1 = "aabbccaaddaaee";
		List<String> split = StrSplitter.split(str1, "aa", 0, true, true);
		Assertions.assertEquals("ee", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitByBlankTest(){
		String str1 = "aa bbccaa     ddaaee";
		List<String> split = StrSplitter.split(str1, 0);
		Assertions.assertEquals("ddaaee", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitPathTest(){
		String str1 = "/use/local/bin";
		List<String> split = StrSplitter.splitPath(str1, 0);
		Assertions.assertEquals("bin", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitMappingTest() {
		String str = "1.2.";
		List<Long> split = StrSplitter.split(str, '.', 0, true, true, Long::parseLong);
		Assertions.assertEquals(2, split.size());
		Assertions.assertEquals(Long.valueOf(1L), split.get(0));
		Assertions.assertEquals(Long.valueOf(2L), split.get(1));
	}

	@Test
	public void splitEmptyTest(){
		String str = "";
		final String[] split = str.split(",");
		final String[] strings = StrSplitter.splitToArray(str, ",", -1, false, false);
		Assertions.assertArrayEquals(split, strings);
	}
}
