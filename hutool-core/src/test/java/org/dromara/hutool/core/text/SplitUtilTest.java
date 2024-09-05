/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
