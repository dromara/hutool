package com.xiaoleilu.hutool.core.util;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.lang.Editor;
import com.xiaoleilu.hutool.util.ArrayUtil;

/**
 * {@link ArrayUtil} 数组工具单元测试
 * @author Looly
 *
 */
public class ArrayUtilTest {
	
	@Test
	public void isEmptyTest(){
		int[] a = {};
		int[] b = null;
		Assert.assertTrue(ArrayUtil.isEmpty(a));
		Assert.assertTrue(ArrayUtil.isEmpty(b));
	}
	
	@Test
	public void isNotEmptyTest(){
		int[] a = {1,2};
		Assert.assertTrue(ArrayUtil.isNotEmpty(a));
	}
	
	@Test
	public void newArrayTest(){
		String[] newArray = ArrayUtil.newArray(String.class, 3);
		Assert.assertEquals(3, newArray.length);
	}
	
	@Test
	public void cloneTest(){
		Integer[] b = {1,2,3};
		Integer[] cloneB = ArrayUtil.clone(b);
		Assert.assertArrayEquals(b, cloneB);
		
		int[] a = {1,2,3};
		int[] clone = ArrayUtil.clone(a);
		Assert.assertArrayEquals(a, clone);
	}
	
	@Test
	public void filterTest(){
		Integer[] a = {1,2,3,4,5,6};
		Integer[] filter = ArrayUtil.filter(a, new Editor<Integer>(){
			@Override
			public Integer edit(Integer t) {
				return (t % 2 == 0) ? t : null;
			}});
		Assert.assertArrayEquals(filter, new Integer[]{2,4,6});
	}
	
	@Test
	public void indexOfTest(){
		Integer[] a = {1,2,3,4,5,6};
		int index = ArrayUtil.indexOf(a, 3);
		Assert.assertEquals(2, index);
		
		long[] b = {1,2,3,4,5,6};
		int index2 = ArrayUtil.indexOf(b, 3);
		Assert.assertEquals(2, index2);
	}
	
	@Test
	public void lastIndexOfTest(){
		Integer[] a = {1,2,3,4,3,6};
		int index = ArrayUtil.lastIndexOf(a, 3);
		Assert.assertEquals(4, index);
		
		long[] b = {1,2,3,4,3,6};
		int index2 = ArrayUtil.lastIndexOf(b, 3);
		Assert.assertEquals(4, index2);
	}
	
	@Test
	public void containsTest(){
		Integer[] a = {1,2,3,4,3,6};
		boolean contains = ArrayUtil.contains(a, 3);
		Assert.assertTrue(contains);
		
		long[] b = {1,2,3,4,3,6};
		boolean contains2 = ArrayUtil.contains(b, 3);
		Assert.assertTrue(contains2);
	}
	
	@Test
	public void mapTest(){
		String[] keys = {"a", "b", "c"};
		Integer[] values = {1,2,3};
		Map<String, Integer> map = ArrayUtil.zip(keys, values, true);
		Assert.assertEquals(map.toString(), "{a=1, b=2, c=3}");
	}
}
