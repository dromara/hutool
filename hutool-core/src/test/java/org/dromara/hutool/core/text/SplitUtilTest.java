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

package org.dromara.hutool.core.text;

import org.dromara.hutool.core.text.split.SplitUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SplitUtilTest {

	@Test
	public void issueI6FKSITest(){
		// issue:I6FKSI
		Assertions.assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitByLength("test length 0", 0));
	}

	@Test
	public void splitToLongTest() {
		final String str = "1,2,3,4, 5";
		long[] longArray = SplitUtil.splitTo(str, ",", long[].class);
		Assertions.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);

		longArray = SplitUtil.splitTo(str, ",", long[].class);
		Assertions.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);
	}

	@Test
	public void splitToIntTest() {
		final String str = "1,2,3,4, 5";
		int[] intArray = SplitUtil.splitTo(str, ",", int[].class);
		Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);

		intArray = SplitUtil.splitTo(str, ",", int[].class);
		Assertions.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);
	}

	@Test
	public void splitTest() {
		final String str = "a,b ,c,d,,e";
		final List<String> split = SplitUtil.split(str, ",", -1, true, true);
		// 测试空是否被去掉
		Assertions.assertEquals(5, split.size());
		// 测试去掉两边空白符是否生效
		Assertions.assertEquals("b", split.get(1));

		final String[] strings = SplitUtil.splitToArray("abc/", StrUtil.SLASH);
		Assertions.assertEquals(2, strings.length);
	}

	@Test
	public void splitEmptyTest() {
		final String str = "";
		final List<String> split = SplitUtil.split(str, ",", -1, true, true);
		// 测试空是否被去掉
		Assertions.assertEquals(0, split.size());
	}

	@Test
	public void splitToArrayNullTest() {
		final String[] strings = SplitUtil.splitToArray(null, ".");
		Assertions.assertNotNull(strings);
		Assertions.assertEquals(0, strings.length);
	}

}
