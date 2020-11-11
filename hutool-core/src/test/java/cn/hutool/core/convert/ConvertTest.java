package cn.hutool.core.convert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * 类型转换工具单元测试
 *
 * @author Looly
 *
 */
public class ConvertTest {

	@Test
	public void toObjectTest() {
		Object result = Convert.convert(Object.class, "aaaa");
		Assert.assertEquals("aaaa", result);
	}

	@Test
	public void toStrTest() {
		int a = 1;
		long[] b = { 1, 2, 3, 4, 5 };

		Assert.assertEquals("[1, 2, 3, 4, 5]", Convert.convert(String.class, b));

		String aStr = Convert.toStr(a);
		Assert.assertEquals("1", aStr);
		String bStr = Convert.toStr(b);
		Assert.assertEquals("[1, 2, 3, 4, 5]", Convert.toStr(bStr));
	}

	@Test
	public void toStrTest2() {
		String result = Convert.convert(String.class, "aaaa");
		Assert.assertEquals("aaaa", result);
	}

	@Test
	public void toStrTest3() {
		char a = 'a';
		String result = Convert.convert(String.class, a);
		Assert.assertEquals("a", result);
	}

	@Test
	public void toIntTest() {
		String a = " 34232";
		Integer aInteger = Convert.toInt(a);
		Assert.assertEquals(Integer.valueOf(34232), aInteger);
		int aInt = ConverterRegistry.getInstance().convert(int.class, a);
		Assert.assertEquals(34232, aInt);

		// 带小数测试
		String b = " 34232.00";
		Integer bInteger = Convert.toInt(b);
		Assert.assertEquals(Integer.valueOf(34232), bInteger);
		int bInt = ConverterRegistry.getInstance().convert(int.class, b);
		Assert.assertEquals(34232, bInt);

		// boolean测试
		boolean c = true;
		Integer cInteger = Convert.toInt(c);
		Assert.assertEquals(Integer.valueOf(1), cInteger);
		int cInt = ConverterRegistry.getInstance().convert(int.class, c);
		Assert.assertEquals(1, cInt);

		// boolean测试
		String d = "08";
		Integer dInteger = Convert.toInt(d);
		Assert.assertEquals(Integer.valueOf(8), dInteger);
		int dInt = ConverterRegistry.getInstance().convert(int.class, d);
		Assert.assertEquals(8, dInt);
	}

	@Test
	public void toIntTest2() {
		ArrayList<String> array = new ArrayList<>();
		Integer aInt = Convert.convertQuietly(Integer.class, array, -1);
		Assert.assertEquals(Integer.valueOf(-1), aInt);
	}

	@Test
	public void toLongTest() {
		String a = " 342324545435435";
		Long aLong = Convert.toLong(a);
		Assert.assertEquals(Long.valueOf(342324545435435L), aLong);
		long aLong2 = ConverterRegistry.getInstance().convert(long.class, a);
		Assert.assertEquals(342324545435435L, aLong2);

		// 带小数测试
		String b = " 342324545435435.245435435";
		Long bLong = Convert.toLong(b);
		Assert.assertEquals(Long.valueOf(342324545435435L), bLong);
		long bLong2 = ConverterRegistry.getInstance().convert(long.class, b);
		Assert.assertEquals(342324545435435L, bLong2);

		// boolean测试
		boolean c = true;
		Long cLong = Convert.toLong(c);
		Assert.assertEquals(Long.valueOf(1), cLong);
		long cLong2 = ConverterRegistry.getInstance().convert(long.class, c);
		Assert.assertEquals(1, cLong2);

		// boolean测试
		String d = "08";
		Long dLong = Convert.toLong(d);
		Assert.assertEquals(Long.valueOf(8), dLong);
		long dLong2 = ConverterRegistry.getInstance().convert(long.class, d);
		Assert.assertEquals(8, dLong2);
	}

	@Test
	public void toCharTest() {
		String str = "aadfdsfs";
		Character c = Convert.toChar(str);
		Assert.assertEquals(Character.valueOf('a'), c);

		// 转换失败
		Object str2 = "";
		Character c2 = Convert.toChar(str2);
		Assert.assertNull(c2);
	}

	@Test
	public void toNumberTest() {
		Object a = "12.45";
		Number number = Convert.toNumber(a);
		Assert.assertEquals(12.45D, number);
	}

	@Test
	public void emptyToNumberTest() {
		Object a = "";
		Number number = Convert.toNumber(a);
		Assert.assertNull(number);
	}

	@Test
	public void intAndByteConvertTest() {
		// 测试 int 转 byte
		int int0 = 234;
		byte byte0 = Convert.intToByte(int0);
		Assert.assertEquals(-22, byte0);

		int int1 = Convert.byteToUnsignedInt(byte0);
		Assert.assertEquals(int0, int1);
	}

	@Test
	public void intAndBytesTest() {
		// 测试 int 转 byte 数组
		int int2 = 1417;
		byte[] bytesInt = Convert.intToBytes(int2);

		// 测试 byte 数组转 int
		int int3 = Convert.bytesToInt(bytesInt);
		Assert.assertEquals(int2, int3);
	}

	@Test
	public void longAndBytesTest() {
		// 测试 long 转 byte 数组
		long long1 = 2223;

		byte[] bytesLong = Convert.longToBytes(long1);
		long long2 = Convert.bytesToLong(bytesLong);

		Assert.assertEquals(long1, long2);
	}

	@Test
	public void shortAndBytesTest() {
		short short1 = 122;
		byte[] bytes = Convert.shortToBytes(short1);
		short short2 = Convert.bytesToShort(bytes);

		Assert.assertEquals(short2, short1);
	}

	@Test
	public void toListTest(){
		List<String> list = Arrays.asList("1","2");
		String str = Convert.toStr(list);
		List<String> list2 = Convert.toList(String.class, str);
		Assert.assertEquals("1", list2.get(0));
		Assert.assertEquals("2", list2.get(1));

		List<Integer> list3 = Convert.toList(Integer.class, str);
		Assert.assertEquals(1, list3.get(0).intValue());
		Assert.assertEquals(2, list3.get(1).intValue());
	}

	@Test
	public void toListTest2(){
		String str = "1,2";
		List<String> list2 = Convert.toList(String.class, str);
		Assert.assertEquals("1", list2.get(0));
		Assert.assertEquals("2", list2.get(1));

		List<Integer> list3 = Convert.toList(Integer.class, str);
		Assert.assertEquals(1, list3.get(0).intValue());
		Assert.assertEquals(2, list3.get(1).intValue());
	}

	@Test
	public void toByteArrayTest(){
		// 测试Serializable转换为bytes，调用序列化转换
		final byte[] bytes = Convert.toPrimitiveByteArray(new Product("zhangsan", "张三", "5.1.1"));
		Assert.assertNotNull(bytes);

		final Product product = Convert.convert(Product.class, bytes);
		Assert.assertEquals("zhangsan", product.getName());
		Assert.assertEquals("张三", product.getCName());
		Assert.assertEquals("5.1.1", product.getVersion());
	}

	@Test
	public void toAtomicIntegerArrayTest(){
		String str = "1,2";
		final AtomicIntegerArray atomicIntegerArray = Convert.convert(AtomicIntegerArray.class, str);
		Assert.assertEquals("[1, 2]", atomicIntegerArray.toString());
	}

	@Test
	public void toAtomicLongArrayTest(){
		String str = "1,2";
		final AtomicLongArray atomicLongArray = Convert.convert(AtomicLongArray.class, str);
		Assert.assertEquals("[1, 2]", atomicLongArray.toString());
	}

	@Test
	public void toClassTest(){
		final Class<?> convert = Convert.convert(Class.class, "cn.hutool.core.convert.ConvertTest.Product");
		Assert.assertEquals(Product.class, convert);
	}

	@Data
	@AllArgsConstructor
	public static class Product implements Serializable {
		private static final long serialVersionUID = 1L;

		private String name;
		private String cName;
		private String version;
	}

	@Test
	public void enumToIntTest(){
		final Integer integer = Convert.toInt(BuildingType.CUO);
		Assert.assertEquals(1, integer.intValue());
	}

	@Getter
	public enum BuildingType {
		PING(1, "平层"),
		CUO(2, "错层"),
		YUE(3, "跃层"),
		FUSHI(4, "复式"),
		KAIJIAN(5, "开间"),
		OTHER(6, "其他");

		private final int id;
		private final String name;

		BuildingType(int id, String name){
			this.id = id;
			this.name = name;
		}
	}
}
