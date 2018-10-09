package cn.hutool.core.convert;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import cn.hutool.core.date.DateUtil;

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
		Assert.assertTrue(34232 == aInt);

		// 带小数测试
		String b = " 34232.00";
		Integer bInteger = Convert.toInt(b);
		Assert.assertEquals(Integer.valueOf(34232), bInteger);
		int bInt = ConverterRegistry.getInstance().convert(int.class, b);
		Assert.assertTrue(34232 == bInt);

		// boolean测试
		boolean c = true;
		Integer cInteger = Convert.toInt(c);
		Assert.assertEquals(Integer.valueOf(1), cInteger);
		int cInt = ConverterRegistry.getInstance().convert(int.class, c);
		Assert.assertTrue(1 == cInt);
	}

	@Test
	public void toLongTest() {
		String a = " 342324545435435";
		Long aLong = Convert.toLong(a);
		Assert.assertEquals(Long.valueOf(342324545435435L), aLong);
		long aLong2 = ConverterRegistry.getInstance().convert(long.class, a);
		Assert.assertTrue(342324545435435L == aLong2);

		// 带小数测试
		String b = " 342324545435435.245435435";
		Long bLong = Convert.toLong(b);
		Assert.assertEquals(Long.valueOf(342324545435435L), bLong);
		long bLong2 = ConverterRegistry.getInstance().convert(long.class, b);
		Assert.assertTrue(342324545435435L == bLong2);

		// boolean测试
		boolean c = true;
		Long cLong = Convert.toLong(c);
		Assert.assertEquals(Long.valueOf(1), cLong);
		long cLong2 = ConverterRegistry.getInstance().convert(long.class, c);
		Assert.assertTrue(1 == cLong2);
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
	public void toDateTest() {
		String a = "2017-05-06";
		Date value = Convert.toDate(a);
		Assert.assertEquals(a, DateUtil.formatDate(value));

		long timeLong = DateUtil.date().getTime();
		Date value2 = Convert.toDate(timeLong);
		Assert.assertEquals(timeLong, value2.getTime());
	}

	@Test
	public void toSqlDateTest() {
		String a = "2017-05-06";
		java.sql.Date value = Convert.convert(java.sql.Date.class, a);
		Assert.assertEquals("2017-05-06", value.toString());

		long timeLong = DateUtil.date().getTime();
		java.sql.Date value2 = Convert.convert(java.sql.Date.class, timeLong);
		Assert.assertEquals(timeLong, value2.getTime());
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
}
