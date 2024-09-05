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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * 转换为集合测试
 *
 * @author looly
 *
 */
public class ConvertToCollectionTest {

	@Test
	public void toCollectionTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<?> list = (List<?>) ConvertUtil.convert(Collection.class, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<?> list = ConvertUtil.toList(a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest2() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = ConvertUtil.toList(String.class, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals("1", list.get(4));
	}

	@Test
	public void toListTest3() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = ConvertUtil.toList(String.class, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals("1", list.get(4));
	}

	@Test
	public void toListTest4() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = ConvertUtil.convert(new TypeReference<List<String>>() {}, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals("1", list.get(4));
	}

	@Test
	public void strToListTest() {
		final String a = "a,你,好,123";
		final List<?> list = ConvertUtil.toList(a);
		Assertions.assertEquals(4, list.size());
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("123", list.get(3));

		final String b = "a";
		final List<?> list2 = ConvertUtil.toList(b);
		Assertions.assertEquals(1, list2.size());
		Assertions.assertEquals("a", list2.get(0));
	}

	@Test
	public void strToListTest2() {
		final String a = "a,你,好,123";
		final List<String> list = ConvertUtil.toList(String.class, a);
		Assertions.assertEquals(4, list.size());
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("123", list.get(3));
	}

	@Test
	public void numberToListTest() {
		final Integer i = 1;
		final ArrayList<?> list = ConvertUtil.convert(ArrayList.class, i);
		Assertions.assertSame(i, list.get(0));

		final BigDecimal b = BigDecimal.ONE;
		final ArrayList<?> list2 = ConvertUtil.convert(ArrayList.class, b);
		Assertions.assertEquals(b, list2.get(0));
	}

	@Test
	public void toLinkedListTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<?> list = ConvertUtil.convert(LinkedList.class, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals(1, list.get(4));
	}

	@Test
	public void toSetTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final LinkedHashSet<?> set = ConvertUtil.convert(LinkedHashSet.class, a);
		final ArrayList<?> list = ListUtil.of(set);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals(1, list.get(4));
	}
}
