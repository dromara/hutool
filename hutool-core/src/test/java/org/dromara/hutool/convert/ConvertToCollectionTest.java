package org.dromara.hutool.convert;

import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.reflect.TypeReference;
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
		final List<?> list = (List<?>) Convert.convert(Collection.class, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<?> list = Convert.toList(a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest2() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = Convert.toList(String.class, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals("1", list.get(4));
	}

	@Test
	public void toListTest3() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = Convert.toList(String.class, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals("1", list.get(4));
	}

	@Test
	public void toListTest4() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = Convert.convert(new TypeReference<List<String>>() {}, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals("1", list.get(4));
	}

	@Test
	public void strToListTest() {
		final String a = "a,你,好,123";
		final List<?> list = Convert.toList(a);
		Assertions.assertEquals(4, list.size());
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("123", list.get(3));

		final String b = "a";
		final List<?> list2 = Convert.toList(b);
		Assertions.assertEquals(1, list2.size());
		Assertions.assertEquals("a", list2.get(0));
	}

	@Test
	public void strToListTest2() {
		final String a = "a,你,好,123";
		final List<String> list = Convert.toList(String.class, a);
		Assertions.assertEquals(4, list.size());
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("123", list.get(3));
	}

	@Test
	public void numberToListTest() {
		final Integer i = 1;
		final ArrayList<?> list = Convert.convert(ArrayList.class, i);
		Assertions.assertSame(i, list.get(0));

		final BigDecimal b = BigDecimal.ONE;
		final ArrayList<?> list2 = Convert.convert(ArrayList.class, b);
		Assertions.assertEquals(b, list2.get(0));
	}

	@Test
	public void toLinkedListTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<?> list = Convert.convert(LinkedList.class, a);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals(1, list.get(4));
	}

	@Test
	public void toSetTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final LinkedHashSet<?> set = Convert.convert(LinkedHashSet.class, a);
		final ArrayList<?> list = ListUtil.of(set);
		Assertions.assertEquals("a", list.get(0));
		Assertions.assertEquals("你", list.get(1));
		Assertions.assertEquals("好", list.get(2));
		Assertions.assertEquals("", list.get(3));
		Assertions.assertEquals(1, list.get(4));
	}
}
