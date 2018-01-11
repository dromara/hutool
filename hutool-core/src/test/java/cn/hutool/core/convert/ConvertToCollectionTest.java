package cn.hutool.core.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.collection.CollUtil;

/**
 * 转换为集合测试
 * @author looly
 *
 */
public class ConvertToCollectionTest {
	
	@Test
	public void toCollectionTest() {
		Object[] a = {"a", "你", "好", "", 1};
		List<?> list = (List<?>) Convert.convert(Collection.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}
	
	@Test
	public void toListTest() {
		Object[] a = {"a", "你", "好", "", 1};
		List<?> list = Convert.convert(List.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}
	
	@Test
	public void toLinkedListTest() {
		Object[] a = {"a", "你", "好", "", 1};
		List<?> list = Convert.convert(LinkedList.class, a);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}
	
	@Test
	public void toSetTest() {
		Object[] a = {"a", "你", "好", "", 1};
		LinkedHashSet<?> set = Convert.convert(LinkedHashSet.class, a);
		ArrayList<?> list = CollUtil.newArrayList(set);
		Assert.assertEquals("a", list.get(0));
		Assert.assertEquals("你", list.get(1));
		Assert.assertEquals("好", list.get(2));
		Assert.assertEquals("", list.get(3));
		Assert.assertEquals(1, list.get(4));
	}
}
