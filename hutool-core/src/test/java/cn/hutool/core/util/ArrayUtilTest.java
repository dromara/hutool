package cn.hutool.core.util;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ArrayUtil;

/**
 * {@link ArrayUtil} 数组工具单元测试
 * 
 * @author Looly
 *
 */
public class ArrayUtilTest {

	@Test
	public void isEmptyTest() {
		int[] a = {};
		Assert.assertTrue(ArrayUtil.isEmpty(a));
		Assert.assertTrue(ArrayUtil.isEmpty((Object) a));
		int[] b = null;
		Assert.assertTrue(ArrayUtil.isEmpty(b));
		Object c = null;
		Assert.assertTrue(ArrayUtil.isEmpty(c));
	}

	@Test
	public void isNotEmptyTest() {
		int[] a = { 1, 2 };
		Assert.assertTrue(ArrayUtil.isNotEmpty(a));
	}

	@Test
	public void newArrayTest() {
		String[] newArray = ArrayUtil.newArray(String.class, 3);
		Assert.assertEquals(3, newArray.length);
	}

	@Test
	public void cloneTest() {
		Integer[] b = { 1, 2, 3 };
		Integer[] cloneB = ArrayUtil.clone(b);
		Assert.assertArrayEquals(b, cloneB);

		int[] a = { 1, 2, 3 };
		int[] clone = ArrayUtil.clone(a);
		Assert.assertArrayEquals(a, clone);
	}

	@Test
	public void filterTest() {
		Integer[] a = { 1, 2, 3, 4, 5, 6 };
		Integer[] filter = ArrayUtil.filter(a, new Editor<Integer>() {
			@Override
			public Integer edit(Integer t) {
				return (t % 2 == 0) ? t : null;
			}
		});
		Assert.assertArrayEquals(filter, new Integer[] { 2, 4, 6 });
	}
	
	@Test
	public void filterTestForFilter() {
		Integer[] a = { 1, 2, 3, 4, 5, 6 };
		Integer[] filter = ArrayUtil.filter(a, new Filter<Integer>() {
			@Override
			public boolean accept(Integer t) {
				return t % 2 == 0;
			}});
		Assert.assertArrayEquals(filter, new Integer[] { 2, 4, 6 });
	}

	@Test
	public void filterTestForEditor() {
		Integer[] a = { 1, 2, 3, 4, 5, 6 };
		Integer[] filter = ArrayUtil.filter(a, new Editor<Integer>() {
			@Override
			public Integer edit(Integer t) {
				return (t % 2 == 0) ? t * 10 : t;
			}
		});
		Assert.assertArrayEquals(filter, new Integer[] { 1, 20, 3, 40, 5, 60 });
	}

	@Test
	public void indexOfTest() {
		Integer[] a = { 1, 2, 3, 4, 5, 6 };
		int index = ArrayUtil.indexOf(a, 3);
		Assert.assertEquals(2, index);

		long[] b = { 1, 2, 3, 4, 5, 6 };
		int index2 = ArrayUtil.indexOf(b, 3);
		Assert.assertEquals(2, index2);
	}

	@Test
	public void lastIndexOfTest() {
		Integer[] a = { 1, 2, 3, 4, 3, 6 };
		int index = ArrayUtil.lastIndexOf(a, 3);
		Assert.assertEquals(4, index);

		long[] b = { 1, 2, 3, 4, 3, 6 };
		int index2 = ArrayUtil.lastIndexOf(b, 3);
		Assert.assertEquals(4, index2);
	}

	@Test
	public void containsTest() {
		Integer[] a = { 1, 2, 3, 4, 3, 6 };
		boolean contains = ArrayUtil.contains(a, 3);
		Assert.assertTrue(contains);

		long[] b = { 1, 2, 3, 4, 3, 6 };
		boolean contains2 = ArrayUtil.contains(b, 3);
		Assert.assertTrue(contains2);
	}

	@Test
	public void mapTest() {
		String[] keys = { "a", "b", "c" };
		Integer[] values = { 1, 2, 3 };
		Map<String, Integer> map = ArrayUtil.zip(keys, values, true);
		Assert.assertEquals(map.toString(), "{a=1, b=2, c=3}");
	}

	@Test
	public void castTest() {
		Object[] values = { "1", "2", "3" };
		String[] cast = (String[]) ArrayUtil.cast(String.class, values);
		Assert.assertEquals(values[0], cast[0]);
		Assert.assertEquals(values[1], cast[1]);
		Assert.assertEquals(values[2], cast[2]);
	}
	
	@Test
	public void rangeTest() {
		for(int i: ArrayUtil.range(0, 10)) {
			Console.log(i);
		}
	}
	
	@Test
	public void maxTest() {
		int max = ArrayUtil.max(1,2,13,4,5);
		Assert.assertEquals(13, max);
		
		long maxLong = ArrayUtil.max(1L,2L,13L,4L,5L);
		Assert.assertEquals(13, maxLong);
		
		double maxDouble = ArrayUtil.max(1D,2.4D,13.0D,4.55D,5D);
		Assert.assertEquals(13.0, maxDouble, 2);
	}
	
	@Test
	public void minTest() {
		int min = ArrayUtil.max(1,2,13,4,5);
		Assert.assertEquals(1, min);
		
		long minLong = ArrayUtil.max(1L,2L,13L,4L,5L);
		Assert.assertEquals(1, minLong);
		
		double minDouble = ArrayUtil.max(1D,2.4D,13.0D,4.55D,5D);
		Assert.assertEquals(1.0, minDouble, 2);
	}
}
