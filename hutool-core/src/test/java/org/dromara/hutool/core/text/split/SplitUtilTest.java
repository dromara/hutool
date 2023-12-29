/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.text.split;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.text.finder.PatternFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link SplitUtil} 单元测试
 * @author Looly
 *
 */
public class SplitUtilTest {

	@Test
	public void splitByCharTest(){
		final String str1 = "a, ,efedsfs,   ddf";
		final List<String> split = SplitUtil.split(str1, ",", 0, true, true);

		Assertions.assertEquals("ddf", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitByStrTest(){
		final String str1 = "aabbccaaddaaee";
		final List<String> split = SplitUtil.split(str1, "aa", 0, true, true);
		Assertions.assertEquals("ee", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitByBlankTest(){
		final String str1 = "aa bbccaa     ddaaee";
		final List<String> split = SplitUtil.splitByBlank(str1);
		Assertions.assertEquals("ddaaee", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitPathTest(){
		final String str1 = "/use/local\\bin";
		final List<String> split = SplitUtil.splitPath(str1);
		Assertions.assertEquals("bin", split.get(2));
		Assertions.assertEquals(3, split.size());
	}

	@Test
	public void splitMappingTest() {
		final String str = "1.2.";
		final List<Long> split = SplitUtil.split(str, ".", 0, true, true, Long::parseLong);
		Assertions.assertEquals(2, split.size());
		Assertions.assertEquals(Long.valueOf(1L), split.get(0));
		Assertions.assertEquals(Long.valueOf(2L), split.get(1));
	}

	@SuppressWarnings("MismatchedReadAndWriteOfArray")
	@Test
	public void splitEmptyTest(){
		final String str = "";
		final String[] split = str.split(",");
		final String[] strings = SplitUtil.split(str, ",", -1, false, false)
				.toArray(new String[0]);

		Assertions.assertNotNull(strings);
		Assertions.assertArrayEquals(split, strings);

		final String[] strings2 = SplitUtil.split(str, ",", -1, false, true)
				.toArray(new String[0]);
		Assertions.assertEquals(0, strings2.length);
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void splitNullTest(){
		final String str = null;
		final String[] strings = SplitUtil.split(str, ",", -1, false, false)
				.toArray(new String[0]);
		Assertions.assertNotNull(strings);
		Assertions.assertEquals(0, strings.length);

		final String[] strings2 = SplitUtil.split(str, ",", -1, false, true)
				.toArray(new String[0]);
		Assertions.assertNotNull(strings2);
		Assertions.assertEquals(0, strings2.length);
	}

	/**
	 * https://github.com/dromara/hutool/issues/2099
	 */
	@Test
	public void splitByRegexTest(){
		final String text = "01  821   34567890182345617821";
		List<String> strings = SplitUtil.splitByRegex(text, "21", 0, false, true);
		Assertions.assertEquals(2, strings.size());
		Assertions.assertEquals("01  8", strings.get(0));
		Assertions.assertEquals("   345678901823456178", strings.get(1));

		strings = SplitUtil.splitByRegex(text, "21", 0, false, false);
		Assertions.assertEquals(3, strings.size());
		Assertions.assertEquals("01  8", strings.get(0));
		Assertions.assertEquals("   345678901823456178", strings.get(1));
		Assertions.assertEquals("", strings.get(2));
	}

	@Test
	void issue3421Test() {
		List<String> strings = SplitUtil.splitByRegex("", "", 0, false, false);
		Assertions.assertEquals(ListUtil.of(""), strings);

		strings = SplitUtil.splitByRegex("aaa", "", 0, false, false);
		Assertions.assertEquals(ListUtil.of("aaa"), strings);

		strings = SplitUtil.splitByRegex("", "aaa", 0, false, false);
		Assertions.assertEquals(ListUtil.of(""), strings);

		strings = SplitUtil.splitByRegex("", "", 0, false, true);
		Assertions.assertEquals(ListUtil.of(), strings);
	}

	@Test
	void issue3421Test2() {
		// 测试在无前置判断时，是否死循环
		final SplitIter splitIter = new SplitIter("", new PatternFinder(Pattern.compile("")), -1, false);
		final List<String> list = splitIter.toList(false);
		Assertions.assertEquals(ListUtil.of(""), list);
	}
}
