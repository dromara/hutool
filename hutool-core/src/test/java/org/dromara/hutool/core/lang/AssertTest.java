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

package org.dromara.hutool.core.lang;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssertTest {

	@SuppressWarnings("ConstantValue")
	@Test
	public void isNullTest() {
		final String a = null;
		Assert.isNull(a);
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void notNullTest() {
		final String a = null;
		Assert.isNull(a);
	}

	@Test
	public void notEmptyTest() {
		final String a = " ";
		final String s = Assert.notEmpty(a);
		Assertions.assertSame(a, s);
	}

	@Test
	public void notEmptyForArrayTest() {
		// 虽然包含空字符串或者null，但是这大概数组由于有元素，则认定为非empty
		String[] a = new String[]{""};
		String[] s = Assert.notEmpty(a);
		Assertions.assertSame(a, s);

		a = new String[]{null};
		s = Assert.notEmpty(a);
		Assertions.assertSame(a, s);
	}

	@Test
	public void notEmptyForCollectionTest() {
		// 虽然包含空字符串或者null，但是这大概数组由于有元素，则认定为非empty
		List<String> a = ListUtil.of("");
		List<String> s = Assert.notEmpty(a);
		Assertions.assertSame(a, s);

		a = ListUtil.of((String) null);
		s = Assert.notEmpty(a);
		Assertions.assertSame(a, s);
	}

	@Test
	public void notEmptyForMapTest() {
		// 虽然包含空字符串或者null，但是这大概数组由于有元素，则认定为非empty
		Map<String, String> a = MapUtil.of("", "");
		Map<String, String> s = Assert.notEmpty(a);
		Assertions.assertSame(a, s);

		a = MapUtil.of(null, null);
		s = Assert.notEmpty(a);
		Assertions.assertSame(a, s);
	}

	@Test
	public void noNullElementsTest() {
		final String[] a = new String[]{""};
		final String[] s = Assert.noNullElements(a);
		Assertions.assertSame(a, s);
	}

	@Test
	public void noNullElementsTest2() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			final String[] a = new String[]{null};
			Assert.noNullElements(a);
		});
	}

	@Test
	public void noNullElementsTest3() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			final String[] a = new String[]{"a", null};
			Assert.noNullElements(a);
		});
	}

	@Test
	public void notBlankTest() {
		final String a = "a";
		final String s = Assert.notBlank(a);
		Assertions.assertSame(a, s);
	}

	@Test
	public void isTrueTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			final int i = 0;
			//noinspection ConstantConditions
			Assert.isTrue(i > 0, IllegalArgumentException::new);
		});
	}

	@Test
	public void isTrueTest2() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, ()->{
			final int i = -1;
			//noinspection ConstantConditions
			Assert.isTrue(i >= 0, IndexOutOfBoundsException::new);
		});
	}

	@Test
	public void isTrueTest3() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, ()->{
			final int i = -1;
			//noinspection ConstantConditions
			Assert.isTrue(i > 0, () -> new IndexOutOfBoundsException("relation message to return"));
		});
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void equalsTest() {
		final String a = null;
		final String b = null;
		Assert.equals(a, b);
		Assert.equals(a, b, "{}不等于{}", a, b);
		Assert.equals(a, b, () -> new RuntimeException(StrUtil.format("{}和{}不相等", a, b)));
	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void notEqualsTest() {
		final String c = null;
		final String d = "null";
		Assert.notEquals(c, d, () -> new RuntimeException(StrUtil.format("{}和{}相等", c, d)));

	}

	@SuppressWarnings("ConstantValue")
	@Test
	public void notEqualsTest2() {
		final Object c = null;
		final Object d = "null";
		Assert.notEquals(c, d);
	}

	@Test
	public void checkBetweenTest() {
		final int a = 12;
		final int i = Assert.checkBetween(a, 1, 12);
		Assertions.assertSame(a, i);
	}

	@Test
	public void checkBetweenTest2() {
		final double a = 12;
		final double i = Assert.checkBetween(a, 1, 12);
		Assertions.assertEquals(a, i, 0.00);
	}

	@Test
	public void checkBetweenTest3() {
		final Number a = 12;
		final Number i = Assert.checkBetween(a, 1, 12);
		Assertions.assertSame(a, i);
	}

	@Test
	public void notContainTest() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			final String sub = "a";
			final String a = Assert.notContain("abc", sub);
			Assertions.assertSame(sub, a);
		});
	}

	@Test
	public void notContainTest2() {
		final String sub = "d";
		final String a = Assert.notContain("abc", sub);
		Assertions.assertSame(sub, a);
	}

	@Test
	public void isInstanceOfTest() {
		final String a = "a";
		final String s = Assert.isInstanceOf(String.class, a);
		Assertions.assertSame(a, s);
	}

	@Test
	public void isAssignableTest() {
		Assert.isAssignable(Map.class, HashMap.class);
	}

	@Test
	public void checkIndexTest() {
		final int i = Assert.checkIndex(1, 10);
		Assertions.assertEquals(1, i);
	}
}
