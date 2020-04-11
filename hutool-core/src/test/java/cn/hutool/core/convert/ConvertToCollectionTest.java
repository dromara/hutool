package cn.hutool.core.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
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
		Object[] a = { "a", "你", "好", "", 1 };
		List<?> list = (List<?>) Convert.convert(Collection.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}

	@Test
	public void toListTest() {
		Object[] a = { "a", "你", "好", "", 1 };
		List<?> list = Convert.toList(a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}
	
	@Test
	public void toListTest2() {
		Object[] a = { "a", "你", "好", "", 1 };
		List<String> list = Convert.toList(String.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals("1", list.get(4));
	}
	
	@Test
	public void toListTest3() {
		Object[] a = { "a", "你", "好", "", 1 };
		List<String> list = Convert.toList(String.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals("1", list.get(4));
	}
	
	@Test
	public void toListTest4() {
		Object[] a = { "a", "你", "好", "", 1 };
		List<String> list = Convert.convert(new TypeReference<List<String>>() {}, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals("1", list.get(4));
	}

	@Test
	public void strToListTest() {
		String a = "a,你,好,123";
		List<?> list = Convert.toList(a);
		Assert.assertEquals(4, list.size());
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("123", list.get(3));

		String b = "a";
		List<?> list2 = Convert.toList(b);
		Assert.assertEquals(1, list2.size());
		Assert.assertEquals("a", list2.get(0));
	}
	
	@Test
	public void strToListTest2() {
		String a = "a,你,好,123";
		List<String> list = Convert.toList(String.class, a);
		Assert.assertEquals(4, list.size());
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("123", list.get(3));
	}

	@Test
	public void numberToListTest() {
		Integer i = 1;
		ArrayList<?> list = Convert.convert(ArrayList.class, i);
		Assert.assertSame(i, list.get(0));

		BigDecimal b = BigDecimal.ONE;
		ArrayList<?> list2 = Convert.convert(ArrayList.class, b);
		Assert.assertEquals(b, list2.get(0));
	}

	@Test
	public void toLinkedListTest() {
		Object[] a = { "a", "你", "好", "", 1 };
		List<?> list = Convert.convert(LinkedList.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}

	@Test
	public void toSetTest() {
		Object[] a = { "a", "你", "好", "", 1 };
		LinkedHashSet<?> set = Convert.convert(LinkedHashSet.class, a);
		ArrayList<?> list = CollUtil.newArrayList(set);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}
}
