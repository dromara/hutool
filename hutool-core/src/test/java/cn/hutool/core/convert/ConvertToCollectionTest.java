package cn.hutool.core.convert;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.reflect.TypeReference;
import org.junit.Assert;
import org.junit.Test;

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
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<?> list = Convert.toList(a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest2() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = Convert.toList(String.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals("1", list.get(4));
	}

	@Test
	public void toListTest3() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = Convert.toList(String.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals("1", list.get(4));
	}

	@Test
	public void toListTest4() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<String> list = Convert.convert(new TypeReference<List<String>>() {}, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals("1", list.get(4));
	}

	@Test
	public void strToListTest() {
		final String a = "a,你,好,123";
		final List<?> list = Convert.toList(a);
		Assert.assertEquals(4, list.size());
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("123", list.get(3));

		final String b = "a";
		final List<?> list2 = Convert.toList(b);
		Assert.assertEquals(1, list2.size());
		Assert.assertEquals("a", list2.get(0));
	}

	@Test
	public void strToListTest2() {
		final String a = "a,你,好,123";
		final List<String> list = Convert.toList(String.class, a);
		Assert.assertEquals(4, list.size());
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("123", list.get(3));
	}

	@Test
	public void numberToListTest() {
		final Integer i = 1;
		final ArrayList<?> list = Convert.convert(ArrayList.class, i);
		Assert.assertSame(i, list.get(0));

		final BigDecimal b = BigDecimal.ONE;
		final ArrayList<?> list2 = Convert.convert(ArrayList.class, b);
		Assert.assertEquals(b, list2.get(0));
	}

	@Test
	public void toLinkedListTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final List<?> list = Convert.convert(LinkedList.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}

	@Test
	public void toSetTest() {
		final Object[] a = { "a", "你", "好", "", 1 };
		final LinkedHashSet<?> set = Convert.convert(LinkedHashSet.class, a);
		final ArrayList<?> list = ListUtil.toList(set);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}
}
