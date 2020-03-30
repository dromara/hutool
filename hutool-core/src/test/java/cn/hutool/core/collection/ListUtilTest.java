package cn.hutool.core.collection;

import org.junit.Assert;
import org.junit.Test;

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
}
