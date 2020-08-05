package cn.hutool.core.collection;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListUtilTest {

	@Test
	public void filterTest(){
		List<String> a = ListUtil.toLinkedList("1", "2", "3");
		final List<String> filter = ListUtil.filter(a, str -> "edit" + str);
		Assert.assertEquals("edit1", filter.get(0));
		Assert.assertEquals("edit2", filter.get(1));
		Assert.assertEquals("edit3", filter.get(2));
	}

	@Test
	public void indexOfAll() {
		List<String> a = ListUtil.toLinkedList("1", "2", "3", "4", "3", "2", "1");
		final int[] indexArray = ListUtil.indexOfAll(a, "2"::equals);
		Assert.assertArrayEquals(new int[]{1,5}, indexArray);
		final int[] indexArray2 = ListUtil.indexOfAll(a, "1"::equals);
		Assert.assertArrayEquals(new int[]{0,6}, indexArray2);
	}

	@Test
	public void splitByCountTest() {
		List<String> a = ListUtil.toList("1", "2", "3", "4", "3", "2", "1");
		List<List<String>> listsOriginal = new ArrayList<>(4);
		listsOriginal.add(ListUtil.toList("1","2"));
		listsOriginal.add(ListUtil.toList("3","4"));
		listsOriginal.add(ListUtil.toList("3","2"));
		listsOriginal.add(ListUtil.toList("1"));
		Console.log(listsOriginal);
		List<List<String>> listsBySplits = ListUtil.splitByCount(a, 2);
		Assert.assertEquals(listsOriginal,listsBySplits);
	}
}
