package cn.hutool.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Editor;
import cn.hutool.core.lang.Filter;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * {@link ArrayUtil} 数组工具单元测试
 *
 * @author Looly
 */
public class ArrayUtilTest {

	@Test
	public void isEmptyTest() {
		int[] a = {};
		Assert.assertTrue(ArrayUtil.isEmpty(a));
		Assert.assertTrue(ArrayUtil.isEmpty((Object) a));
		int[] b = null;
		//noinspection ConstantConditions
		Assert.assertTrue(ArrayUtil.isEmpty(b));
		Object c = null;
		//noinspection ConstantConditions
		Assert.assertTrue(ArrayUtil.isEmpty(c));

		Object d = new Object[]{"1", "2", 3, 4D};
		boolean isEmpty = ArrayUtil.isEmpty(d);
		Assert.assertFalse(isEmpty);
		d = new Object[0];
		isEmpty = ArrayUtil.isEmpty(d);
		Assert.assertTrue(isEmpty);
		d = null;
		//noinspection ConstantConditions
		isEmpty = ArrayUtil.isEmpty(d);
		//noinspection ConstantConditions
		Assert.assertTrue(isEmpty);

		// Object数组
		Object[] e = new Object[]{"1", "2", 3, 4D};
		final boolean empty = ArrayUtil.isEmpty(e);
		Assert.assertFalse(empty);
	}

	@Test
	public void isNotEmptyTest() {
		int[] a = {1, 2};
		Assert.assertTrue(ArrayUtil.isNotEmpty(a));
	}

	@Test
	public void newArrayTest() {
		String[] newArray = ArrayUtil.newArray(String.class, 3);
		Assert.assertEquals(3, newArray.length);
	}

	@Test
	public void cloneTest() {
		Integer[] b = {1, 2, 3};
		Integer[] cloneB = ArrayUtil.clone(b);
		Assert.assertArrayEquals(b, cloneB);

		int[] a = {1, 2, 3};
		int[] clone = ArrayUtil.clone(a);
		Assert.assertArrayEquals(a, clone);
	}

	@Test
	public void filterTest() {
		Integer[] a = {1, 2, 3, 4, 5, 6};
		Integer[] filter = ArrayUtil.filter(a, (Editor<Integer>) t -> (t % 2 == 0) ? t : null);
		Assert.assertArrayEquals(filter, new Integer[]{2, 4, 6});
	}

	@Test
	public void filterTestForFilter() {
		Integer[] a = {1, 2, 3, 4, 5, 6};
		Integer[] filter = ArrayUtil.filter(a, (Filter<Integer>) t -> t % 2 == 0);
		Assert.assertArrayEquals(filter, new Integer[]{2, 4, 6});
	}

	@Test
	public void filterTestForEditor() {
		Integer[] a = {1, 2, 3, 4, 5, 6};
		Integer[] filter = ArrayUtil.filter(a, (Editor<Integer>) t -> (t % 2 == 0) ? t * 10 : t);
		Assert.assertArrayEquals(filter, new Integer[]{1, 20, 3, 40, 5, 60});
	}

	@Test
	public void indexOfTest() {
		Integer[] a = {1, 2, 3, 4, 5, 6};
		int index = ArrayUtil.indexOf(a, 3);
		Assert.assertEquals(2, index);

		long[] b = {1, 2, 3, 4, 5, 6};
		int index2 = ArrayUtil.indexOf(b, 3);
		Assert.assertEquals(2, index2);
	}

	@Test
	public void lastIndexOfTest() {
		Integer[] a = {1, 2, 3, 4, 3, 6};
		int index = ArrayUtil.lastIndexOf(a, 3);
		Assert.assertEquals(4, index);

		long[] b = {1, 2, 3, 4, 3, 6};
		int index2 = ArrayUtil.lastIndexOf(b, 3);
		Assert.assertEquals(4, index2);
	}

	@Test
	public void containsTest() {
		Integer[] a = {1, 2, 3, 4, 3, 6};
		boolean contains = ArrayUtil.contains(a, 3);
		Assert.assertTrue(contains);

		long[] b = {1, 2, 3, 4, 3, 6};
		boolean contains2 = ArrayUtil.contains(b, 3);
		Assert.assertTrue(contains2);
	}

	@Test
	public void containsAnyTest() {
		Integer[] a = {1, 2, 3, 4, 3, 6};
		boolean contains = ArrayUtil.containsAny(a, 4, 10, 40);
		Assert.assertTrue(contains);

		contains = ArrayUtil.containsAny(a, 10, 40);
		Assert.assertFalse(contains);
	}

	@Test
	public void containsAllTest() {
		Integer[] a = {1, 2, 3, 4, 3, 6};
		boolean contains = ArrayUtil.containsAll(a, 4, 2, 6);
		Assert.assertTrue(contains);

		contains = ArrayUtil.containsAll(a, 1, 2, 3, 5);
		Assert.assertFalse(contains);
	}

	@Test
	public void mapTest() {
		String[] keys = {"a", "b", "c"};
		Integer[] values = {1, 2, 3};
		Map<String, Integer> map = ArrayUtil.zip(keys, values, true);
		Assert.assertEquals(Objects.requireNonNull(map).toString(), "{a=1, b=2, c=3}");
	}

	@Test
	public void castTest() {
		Object[] values = {"1", "2", "3"};
		String[] cast = (String[]) ArrayUtil.cast(String.class, values);
		Assert.assertEquals(values[0], cast[0]);
		Assert.assertEquals(values[1], cast[1]);
		Assert.assertEquals(values[2], cast[2]);
	}

	@Test
	public void rangeTest() {
		int[] range = ArrayUtil.range(0, 10);
		Assert.assertEquals(0, range[0]);
		Assert.assertEquals(1, range[1]);
		Assert.assertEquals(2, range[2]);
		Assert.assertEquals(3, range[3]);
		Assert.assertEquals(4, range[4]);
		Assert.assertEquals(5, range[5]);
		Assert.assertEquals(6, range[6]);
		Assert.assertEquals(7, range[7]);
		Assert.assertEquals(8, range[8]);
		Assert.assertEquals(9, range[9]);
	}

	@Test
	public void maxTest() {
		int max = ArrayUtil.max(1, 2, 13, 4, 5);
		Assert.assertEquals(13, max);

		long maxLong = ArrayUtil.max(1L, 2L, 13L, 4L, 5L);
		Assert.assertEquals(13, maxLong);

		double maxDouble = ArrayUtil.max(1D, 2.4D, 13.0D, 4.55D, 5D);
		Assert.assertEquals(13.0, maxDouble, 2);

		BigDecimal one = new BigDecimal("1.00");
		BigDecimal two = new BigDecimal("2.0");
		BigDecimal three = new BigDecimal("3");
		BigDecimal[] bigDecimals = {two, one, three};

		BigDecimal minAccuracy = ArrayUtil.min(bigDecimals, Comparator.comparingInt(BigDecimal::scale));
		Assert.assertEquals(minAccuracy, three);

		BigDecimal maxAccuracy = ArrayUtil.max(bigDecimals, Comparator.comparingInt(BigDecimal::scale));
		Assert.assertEquals(maxAccuracy, one);
	}

	@Test
	public void minTest() {
		int min = ArrayUtil.min(1, 2, 13, 4, 5);
		Assert.assertEquals(1, min);

		long minLong = ArrayUtil.min(1L, 2L, 13L, 4L, 5L);
		Assert.assertEquals(1, minLong);

		double minDouble = ArrayUtil.min(1D, 2.4D, 13.0D, 4.55D, 5D);
		Assert.assertEquals(1.0, minDouble, 2);
	}

	@Test
	public void appendTest() {
		String[] a = {"1", "2", "3", "4"};
		String[] b = {"a", "b", "c"};

		String[] result = ArrayUtil.append(a, b);
		Assert.assertArrayEquals(new String[]{"1", "2", "3", "4", "a", "b", "c"}, result);
	}

	@Test
	public void insertTest() {
		String[] a = {"1", "2", "3", "4"};
		String[] b = {"a", "b", "c"};

		// 在-1位置插入，相当于在3位置插入
		String[] result = ArrayUtil.insert(a, -1, b);
		Assert.assertArrayEquals(new String[]{"1", "2", "3", "a", "b", "c", "4"}, result);

		// 在第0个位置插入，即在数组前追加
		result = ArrayUtil.insert(a, 0, b);
		Assert.assertArrayEquals(new String[]{"a", "b", "c", "1", "2", "3", "4"}, result);

		// 在第2个位置插入，即"3"之前
		result = ArrayUtil.insert(a, 2, b);
		Assert.assertArrayEquals(new String[]{"1", "2", "a", "b", "c", "3", "4"}, result);

		// 在第4个位置插入，即"4"之后，相当于追加
		result = ArrayUtil.insert(a, 4, b);
		Assert.assertArrayEquals(new String[]{"1", "2", "3", "4", "a", "b", "c"}, result);

		// 在第5个位置插入，由于数组长度为4，因此补null
		result = ArrayUtil.insert(a, 5, b);
		Assert.assertArrayEquals(new String[]{"1", "2", "3", "4", null, "a", "b", "c"}, result);
	}

	@Test
	public void joinTest() {
		String[] array = {"aa", "bb", "cc", "dd"};
		String join = ArrayUtil.join(array, ",", "[", "]");
		Assert.assertEquals("[aa],[bb],[cc],[dd]", join);
	}

	@Test
	public void getArrayTypeTest() {
		Class<?> arrayType = ArrayUtil.getArrayType(int.class);
		Assert.assertEquals(int[].class, arrayType);

		arrayType = ArrayUtil.getArrayType(String.class);
		Assert.assertEquals(String[].class, arrayType);
	}

	@Test
	public void distinctTest() {
		String[] array = {"aa", "bb", "cc", "dd", "bb", "dd"};
		String[] distinct = ArrayUtil.distinct(array);
		Assert.assertArrayEquals(new String[]{"aa", "bb", "cc", "dd"}, distinct);
	}

	@Test
	public void toStingTest() {
		int[] a = {1, 3, 56, 6, 7};
		Assert.assertEquals("[1, 3, 56, 6, 7]", ArrayUtil.toString(a));
		long[] b = {1, 3, 56, 6, 7};
		Assert.assertEquals("[1, 3, 56, 6, 7]", ArrayUtil.toString(b));
		short[] c = {1, 3, 56, 6, 7};
		Assert.assertEquals("[1, 3, 56, 6, 7]", ArrayUtil.toString(c));
		double[] d = {1, 3, 56, 6, 7};
		Assert.assertEquals("[1.0, 3.0, 56.0, 6.0, 7.0]", ArrayUtil.toString(d));
		byte[] e = {1, 3, 56, 6, 7};
		Assert.assertEquals("[1, 3, 56, 6, 7]", ArrayUtil.toString(e));
		boolean[] f = {true, false, true, true, true};
		Assert.assertEquals("[true, false, true, true, true]", ArrayUtil.toString(f));
		float[] g = {1, 3, 56, 6, 7};
		Assert.assertEquals("[1.0, 3.0, 56.0, 6.0, 7.0]", ArrayUtil.toString(g));
		char[] h = {'a', 'b', '你', '好', '1'};
		Assert.assertEquals("[a, b, 你, 好, 1]", ArrayUtil.toString(h));

		String[] array = {"aa", "bb", "cc", "dd", "bb", "dd"};
		Assert.assertEquals("[aa, bb, cc, dd, bb, dd]", ArrayUtil.toString(array));
	}

	@Test
	public void toArrayTest() {
		final ArrayList<String> list = CollUtil.newArrayList("A", "B", "C", "D");
		final String[] array = ArrayUtil.toArray(list, String.class);
		Assert.assertEquals("A", array[0]);
		Assert.assertEquals("B", array[1]);
		Assert.assertEquals("C", array[2]);
		Assert.assertEquals("D", array[3]);
	}

	@Test
	public void addAllTest() {
		final int[] ints = ArrayUtil.addAll(new int[]{1, 2, 3}, new int[]{4, 5, 6});
		Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, ints);
	}

	@Test
	public void isAllNotNullTest() {
		String[] allNotNull = {"aa", "bb", "cc", "dd", "bb", "dd"};
		Assert.assertTrue(ArrayUtil.isAllNotNull(allNotNull));
		String[] hasNull = {"aa", "bb", "cc", null, "bb", "dd"};
		Assert.assertFalse(ArrayUtil.isAllNotNull(hasNull));
	}

	@Test
	public void indexOfSubTest() {
		Integer[] a = {0x12, 0x34, 0x56, 0x78, 0x9A};
		Integer[] b = {0x56, 0x78};
		Integer[] c = {0x12, 0x56};
		Integer[] d = {0x78, 0x9A};
		Integer[] e = {0x78, 0x9A, 0x10};

		int i = ArrayUtil.indexOfSub(a, b);
		Assert.assertEquals(2, i);

		i = ArrayUtil.indexOfSub(a, c);
		Assert.assertEquals(-1, i);

		i = ArrayUtil.indexOfSub(a, d);
		Assert.assertEquals(3, i);

		i = ArrayUtil.indexOfSub(a, e);
		Assert.assertEquals(-1, i);

		i = ArrayUtil.indexOfSub(a, null);
		Assert.assertEquals(-1, i);

		i = ArrayUtil.indexOfSub(null, null);
		Assert.assertEquals(-1, i);

		i = ArrayUtil.indexOfSub(null, b);
		Assert.assertEquals(-1, i);
	}

	@Test
	public void lastIndexOfSubTest() {
		Integer[] a = {0x12, 0x34, 0x56, 0x78, 0x9A};
		Integer[] b = {0x56, 0x78};
		Integer[] c = {0x12, 0x56};
		Integer[] d = {0x78, 0x9A};
		Integer[] e = {0x78, 0x9A, 0x10};

		int i = ArrayUtil.lastIndexOfSub(a, b);
		Assert.assertEquals(2, i);

		i = ArrayUtil.lastIndexOfSub(a, c);
		Assert.assertEquals(-1, i);

		i = ArrayUtil.lastIndexOfSub(a, d);
		Assert.assertEquals(3, i);

		i = ArrayUtil.lastIndexOfSub(a, e);
		Assert.assertEquals(-1, i);

		i = ArrayUtil.lastIndexOfSub(a, null);
		Assert.assertEquals(-1, i);

		i = ArrayUtil.lastIndexOfSub(null, null);
		Assert.assertEquals(-1, i);

		i = ArrayUtil.lastIndexOfSub(null, b);
		Assert.assertEquals(-1, i);
	}
}
