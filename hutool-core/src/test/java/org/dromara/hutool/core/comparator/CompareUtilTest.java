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

package org.dromara.hutool.core.comparator;

import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CompareUtilTest {

	@Test
	public void compareTest(){
		int compare = CompareUtil.compare(null, "a", true);
		Assertions.assertTrue(compare > 0);

		compare = CompareUtil.compare(null, "a", false);
		Assertions.assertTrue(compare < 0);
	}

	@Test
	public void comparingPinyin() {
		final List<String> list = ListUtil.of("成都", "北京", "上海", "深圳");

		final List<String> ascendingOrderResult = ListUtil.view("北京", "成都", "上海", "深圳");
		final List<String> descendingOrderResult = ListUtil.view("深圳", "上海", "成都", "北京");

		// 正序
		list.sort(CompareUtil.comparingPinyin(e -> e));
		Assertions.assertEquals(list, ascendingOrderResult);

		// 反序
		list.sort(CompareUtil.comparingPinyin(e -> e, true));
		Assertions.assertEquals(list, descendingOrderResult);
	}

	@Test
	public void comparingIndexedTest() {
		final List<String> data = ListUtil.of("1", "2", "3", "4", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
		final List<String> index = ListUtil.view("2", "1", "3", "4");

		data.sort(CompareUtil.comparingIndexed(e -> e, index));
		//[1, 2, 3, 4, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
		Assertions.assertEquals(ListUtil.view("5", "6", "7", "8", "9", "10", "2", "2", "1", "1", "3", "3", "4", "4"), data);
	}

	@Test
	public void comparingIndexedTest2() {
		final List<String> data = ListUtil.of("1", "2", "3", "4", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
		final List<String> index = ListUtil.view("2", "1", "3", "4");

		//正确排序，index.toArray()
		data.sort(CompareUtil.comparingIndexed(e -> e, index.toArray()));
		//[5, 6, 7, 8, 9, 10, 2, 2, 1, 1, 3, 3, 4, 4]
		Assertions.assertEquals(ListUtil.view("5", "6", "7", "8", "9", "10", "2", "2", "1", "1", "3", "3", "4", "4"), data);
	}
	@Test
	public void comparingIndexedTest3() {
		final List<String> data = ListUtil.of("1", "2", "3", "4", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
		final String[] indexArray = new String[] {"2", "1", "3", "4"};

		//正确排序，array
		data.sort(CompareUtil.comparingIndexed(e -> e, indexArray));
		//[5, 6, 7, 8, 9, 10, 2, 2, 1, 1, 3, 3, 4, 4]
		Assertions.assertEquals(data, ListUtil.view("5", "6", "7", "8", "9", "10", "2", "2", "1", "1", "3", "3", "4", "4"));
	}

	@Test
	public void compareNullTest() {
		Assertions.assertEquals(0, CompareUtil.compare(1, 1));
		Assertions.assertEquals(1, CompareUtil.compare(1, null));
		Assertions.assertEquals(-1, CompareUtil.compare(null, 1));

		Assertions.assertEquals(-1, CompareUtil.compare(1, null, true));
		Assertions.assertEquals(1, CompareUtil.compare(null, 1, true));
	}
}
