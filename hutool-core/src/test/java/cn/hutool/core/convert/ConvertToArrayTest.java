package cn.hutool.core.convert;

import cn.hutool.core.convert.impl.ArrayConverter;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
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
		String[] b = { "1", "2", "3", "4" };

		Integer[] integerArray = Convert.toIntArray(b);
		Assertions.assertArrayEquals(integerArray, new Integer[]{1,2,3,4});

		int[] intArray = Convert.convert(int[].class, b);
		Assertions.assertArrayEquals(intArray, new int[]{1,2,3,4});

		long[] c = {1,2,3,4,5};
		Integer[] intArray2 = Convert.toIntArray(c);
		Assertions.assertArrayEquals(intArray2, new Integer[]{1,2,3,4,5});
	}

	@Test
	public void toIntArrayTestIgnoreComponentErrorTest() {
		String[] b = { "a", "1" };

		final ArrayConverter arrayConverter = new ArrayConverter(Integer[].class, true);
		Integer[] integerArray = (Integer[]) arrayConverter.convert(b, null);
		Assertions.assertArrayEquals(integerArray, new Integer[]{null, 1});
	}

	@Test
	public void toLongArrayTest() {
		String[] b = { "1", "2", "3", "4" };

		Long[] longArray = Convert.toLongArray(b);
		Assertions.assertArrayEquals(longArray, new Long[]{1L,2L,3L,4L});

		long[] longArray2 = Convert.convert(long[].class, b);
		Assertions.assertArrayEquals(longArray2, new long[]{1L,2L,3L,4L});

		int[] c = {1,2,3,4,5};
		Long[] intArray2 = Convert.toLongArray(c);
		Assertions.assertArrayEquals(intArray2, new Long[]{1L,2L,3L,4L,5L});
	}

	@Test
	public void toDoubleArrayTest() {
		String[] b = { "1", "2", "3", "4" };

		Double[] doubleArray = Convert.toDoubleArray(b);
		Assertions.assertArrayEquals(doubleArray, new Double[]{1D,2D,3D,4D});

		double[] doubleArray2 = Convert.convert(double[].class, b);
		Assertions.assertArrayEquals(doubleArray2, new double[]{1D,2D,3D,4D}, 2);

		int[] c = {1,2,3,4,5};
		Double[] intArray2 = Convert.toDoubleArray(c);
		Assertions.assertArrayEquals(intArray2, new Double[]{1D,2D,3D,4D,5D});
	}

	@Test
	public void toPrimitiveArrayTest(){

		//数组转数组测试
		int[] a = new int[]{1,2,3,4};
		long[] result = ConverterRegistry.getInstance().convert(long[].class, a);
		Assertions.assertArrayEquals(new long[]{1L, 2L, 3L, 4L}, result);

		//数组转数组测试
		byte[] resultBytes = ConverterRegistry.getInstance().convert(byte[].class, a);
		Assertions.assertArrayEquals(new byte[]{1, 2, 3, 4}, resultBytes);

		//字符串转数组
		String arrayStr = "1,2,3,4,5";
		//获取Converter类的方法2，自己实例化相应Converter对象
		ArrayConverter c3 = new ArrayConverter(int[].class);
		int[] result3 = (int[]) c3.convert(arrayStr, null);
		Assertions.assertArrayEquals(new int[]{1,2,3,4,5}, result3);
	}

	@Test
	public void collectionToArrayTest() {
		ArrayList<Object> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");

		String[] result = Convert.toStrArray(list);
		Assertions.assertEquals(list.get(0), result[0]);
		Assertions.assertEquals(list.get(1), result[1]);
		Assertions.assertEquals(list.get(2), result[2]);
	}

	@Test
	public void strToCharArrayTest() {
		String testStr = "abcde";
		Character[] array = Convert.toCharArray(testStr);

		//包装类型数组
		Assertions.assertEquals(new Character('a'), array[0]);
		Assertions.assertEquals(new Character('b'), array[1]);
		Assertions.assertEquals(new Character('c'), array[2]);
		Assertions.assertEquals(new Character('d'), array[3]);
		Assertions.assertEquals(new Character('e'), array[4]);

		//原始类型数组
		char[] array2 = Convert.convert(char[].class, testStr);
		Assertions.assertEquals('a', array2[0]);
		Assertions.assertEquals('b', array2[1]);
		Assertions.assertEquals('c', array2[2]);
		Assertions.assertEquals('d', array2[3]);
		Assertions.assertEquals('e', array2[4]);

	}

	@Test
	@Disabled
	public void toUrlArrayTest() {
		File[] files = FileUtil.file("D:\\workspace").listFiles();

		URL[] urls = Convert.convert(URL[].class, files);

		for (URL url : urls) {
			Console.log(url.getPath());
		}
	}
}
