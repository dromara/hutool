package org.dromara.hutool.convert;

import org.dromara.hutool.convert.impl.ArrayConverter;
import org.dromara.hutool.io.file.FileUtil;
import org.dromara.hutool.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * 类型转换工具单元测试<br>
 * 转换为数组
 *
 * @author Looly
 *
 */
public class ConvertToArrayTest {

	@Test
	public void toIntArrayTest() {
		final String[] b = { "1", "2", "3", "4" };

		final Integer[] integerArray = Convert.toIntArray(b);
		Assertions.assertArrayEquals(integerArray, new Integer[]{1,2,3,4});

		final int[] intArray = Convert.convert(int[].class, b);
		Assertions.assertArrayEquals(intArray, new int[]{1,2,3,4});

		final long[] c = {1,2,3,4,5};
		final Integer[] intArray2 = Convert.toIntArray(c);
		Assertions.assertArrayEquals(intArray2, new Integer[]{1,2,3,4,5});
	}

	@Test
	public void toIntArrayTestIgnoreComponentErrorTest() {
		final String[] b = { "a", "1" };

		final ArrayConverter arrayConverter = new ArrayConverter(true);
		final Integer[] integerArray = arrayConverter.convert(Integer[].class, b, null);
		Assertions.assertArrayEquals(integerArray, new Integer[]{null, 1});
	}

	@Test
	public void toLongArrayTest() {
		final String[] b = { "1", "2", "3", "4" };

		final Long[] longArray = Convert.toLongArray(b);
		Assertions.assertArrayEquals(longArray, new Long[]{1L,2L,3L,4L});

		final long[] longArray2 = Convert.convert(long[].class, b);
		Assertions.assertArrayEquals(longArray2, new long[]{1L,2L,3L,4L});

		final int[] c = {1,2,3,4,5};
		final Long[] intArray2 = Convert.toLongArray(c);
		Assertions.assertArrayEquals(intArray2, new Long[]{1L,2L,3L,4L,5L});
	}

	@Test
	public void toDoubleArrayTest() {
		final String[] b = { "1", "2", "3", "4" };

		final Double[] doubleArray = Convert.toDoubleArray(b);
		Assertions.assertArrayEquals(doubleArray, new Double[]{1D,2D,3D,4D});

		final double[] doubleArray2 = Convert.convert(double[].class, b);
		Assertions.assertArrayEquals(doubleArray2, new double[]{1D,2D,3D,4D}, 2);

		final int[] c = {1,2,3,4,5};
		final Double[] intArray2 = Convert.toDoubleArray(c);
		Assertions.assertArrayEquals(intArray2, new Double[]{1D,2D,3D,4D,5D});
	}

	@Test
	public void toPrimitiveArrayTest(){

		//数组转数组测试
		final int[] a = new int[]{1,2,3,4};
		final long[] result = (long[]) CompositeConverter.getInstance().convert(long[].class, a);
		Assertions.assertArrayEquals(new long[]{1L, 2L, 3L, 4L}, result);

		//数组转数组测试
		final byte[] resultBytes = (byte[]) CompositeConverter.getInstance().convert(byte[].class, a);
		Assertions.assertArrayEquals(new byte[]{1, 2, 3, 4}, resultBytes);

		//字符串转数组
		final String arrayStr = "1,2,3,4,5";
		//获取Converter类的方法2，自己实例化相应Converter对象
		final ArrayConverter c3 = new ArrayConverter();
		final int[] result3 = c3.convert(int[].class, arrayStr, null);
		Assertions.assertArrayEquals(new int[]{1,2,3,4,5}, result3);
	}

	@Test
	public void collectionToArrayTest() {
		final ArrayList<Object> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");

		final String[] result = Convert.toStrArray(list);
		Assertions.assertEquals(list.get(0), result[0]);
		Assertions.assertEquals(list.get(1), result[1]);
		Assertions.assertEquals(list.get(2), result[2]);
	}

	@Test
	public void strToCharArrayTest() {
		final String testStr = "abcde";
		final Character[] array = Convert.toCharArray(testStr);

		//包装类型数组
		Assertions.assertEquals(new Character('a'), array[0]);
		Assertions.assertEquals(new Character('b'), array[1]);
		Assertions.assertEquals(new Character('c'), array[2]);
		Assertions.assertEquals(new Character('d'), array[3]);
		Assertions.assertEquals(new Character('e'), array[4]);

		//原始类型数组
		final char[] array2 = Convert.convert(char[].class, testStr);
		Assertions.assertEquals('a', array2[0]);
		Assertions.assertEquals('b', array2[1]);
		Assertions.assertEquals('c', array2[2]);
		Assertions.assertEquals('d', array2[3]);
		Assertions.assertEquals('e', array2[4]);

	}

	@Test
	@Disabled
	public void toUrlArrayTest() {
		final File[] files = FileUtil.file("D:\\workspace").listFiles();

		final URL[] urls = Convert.convert(URL[].class, files);

		for (final URL url : urls) {
			Console.log(url.getPath());
		}
	}
}
