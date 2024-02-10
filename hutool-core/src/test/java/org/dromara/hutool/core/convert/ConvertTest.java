/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.codec.binary.HexUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.date.DateException;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.util.ByteUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * 类型转换工具单元测试
 *
 * @author Looly
 *
 */
public class ConvertTest {

	@Test
	public void toObjectTest() {
		final Object result = Convert.convert(Object.class, "aaaa");
		Assertions.assertEquals("aaaa", result);
	}

	/**
	 * 调取对象的toString方法会抛异常的测试类
	 */
	@Data
	private static class TestExceptionClass {
		@Override
		public String toString() {
			throw new RuntimeException();
		}
	}

	@Test
	public void toStrTest() {
		final int a = 1;
		final long[] b = { 1, 2, 3, 4, 5 };

		Assertions.assertEquals("[1, 2, 3, 4, 5]", Convert.convert(String.class, b));

		final String aStr = Convert.toStr(a);
		Assertions.assertEquals("1", aStr);
		final String bStr = Convert.toStr(b);
		Assertions.assertEquals("[1, 2, 3, 4, 5]", Convert.toStr(bStr));
	}

	@Test
	public void toStrTest2() {
		final String result = Convert.convert(String.class, "aaaa");
		Assertions.assertEquals("aaaa", result);
	}

	@Test
	public void toStrTest3() {
		final char a = 'a';
		final String result = Convert.convert(String.class, a);
		Assertions.assertEquals("a", result);
	}

	@Test
	public void toStrTest4() {
		// 被当作八进制
		@SuppressWarnings("OctalInteger")
		final String result = Convert.toStr(001200);
		Assertions.assertEquals("640", result);
	}

	@Test
	public void toStrTest5() {
		// 被转化的对象有值，正常转换
		final String a = "aaaa";
		final String aDefaultValue = "aDefault";
		final String aResult = Convert.toStr(a, aDefaultValue);
		Assertions.assertEquals(aResult, a);

		// 被转化的对象为null，返回默认值
		final String b = null;
		final String bDefaultValue = "bDefault";
		final String bResult = Convert.toStr(b, bDefaultValue);
		Assertions.assertEquals(bResult, bDefaultValue);

		// 转换失败，返回默认值
		final TestExceptionClass c = new TestExceptionClass();
		final String cDefaultValue = "cDefault";
		final String cResult = Convert.toStr(c, cDefaultValue);
		Assertions.assertEquals(cResult, cDefaultValue);
	}

	@Test
	public void toIntTest() {
		final String a = " 34232";
		final Integer aInteger = Convert.toInt(a);
		Assertions.assertEquals(Integer.valueOf(34232), aInteger);
		final int aInt = (int) CompositeConverter.getInstance().convert(int.class, a);
		Assertions.assertEquals(34232, aInt);

		// 带小数测试
		final String b = " 34232.00";
		final Integer bInteger = Convert.toInt(b);
		Assertions.assertEquals(Integer.valueOf(34232), bInteger);
		final int bInt = (int) CompositeConverter.getInstance().convert(int.class, b);
		Assertions.assertEquals(34232, bInt);

		// boolean测试
		final boolean c = true;
		final Integer cInteger = Convert.toInt(c);
		Assertions.assertEquals(Integer.valueOf(1), cInteger);
		final int cInt = (int) CompositeConverter.getInstance().convert(int.class, c);
		Assertions.assertEquals(1, cInt);

		// boolean测试
		final String d = "08";
		final Integer dInteger = Convert.toInt(d);
		Assertions.assertEquals(Integer.valueOf(8), dInteger);
		final int dInt = (int) CompositeConverter.getInstance().convert(int.class, d);
		Assertions.assertEquals(8, dInt);
	}

	@Test
	public void toIntTest2() {
		final ArrayList<String> array = new ArrayList<>();
		final Integer aInt = Convert.convertQuietly(Integer.class, array, -1);
		Assertions.assertEquals(Integer.valueOf(-1), aInt);
	}

	@Test
	public void toIntOfExceptionTest(){
		Assertions.assertThrows(NumberFormatException.class, ()->{
			final Integer d = Convert.convert(Integer.class, "d");
			Assertions.assertNotNull(d);
		});
	}

	@Test
	public void toLongTest() {
		final String a = " 342324545435435";
		final Long aLong = Convert.toLong(a);
		Assertions.assertEquals(Long.valueOf(342324545435435L), aLong);
		final long aLong2 = (long) CompositeConverter.getInstance().convert(long.class, a);
		Assertions.assertEquals(342324545435435L, aLong2);

		// 带小数测试
		final String b = " 342324545435435.245435435";
		final Long bLong = Convert.toLong(b);
		Assertions.assertEquals(Long.valueOf(342324545435435L), bLong);
		final long bLong2 = (long) CompositeConverter.getInstance().convert(long.class, b);
		Assertions.assertEquals(342324545435435L, bLong2);

		// boolean测试
		final boolean c = true;
		final Long cLong = Convert.toLong(c);
		Assertions.assertEquals(Long.valueOf(1), cLong);
		final long cLong2 = (long) CompositeConverter.getInstance().convert(long.class, c);
		Assertions.assertEquals(1, cLong2);

		// boolean测试
		final String d = "08";
		final Long dLong = Convert.toLong(d);
		Assertions.assertEquals(Long.valueOf(8), dLong);
		final long dLong2 = (long) CompositeConverter.getInstance().convert(long.class, d);
		Assertions.assertEquals(8, dLong2);
	}

	@Test
	public void toCharTest() {
		final String str = "aadfdsfs";
		final Character c = Convert.toChar(str);
		Assertions.assertEquals(Character.valueOf('a'), c);

		// 转换失败
		final Object str2 = "";
		final Character c2 = Convert.toChar(str2);
		Assertions.assertNull(c2);
	}

	@Test
	public void toNumberTest() {
		final Object a = "12.45";
		final Number number = Convert.toNumber(a);
		Assertions.assertEquals(12.45D, number.doubleValue(), 0);
	}

	@Test
	public void emptyToNumberTest() {
		final Object a = "";
		final Number number = Convert.toNumber(a);
		Assertions.assertNull(number);
	}

	@Test
	public void intAndByteConvertTest() {
		// 测试 int 转 byte
		final int int0 = 234;
		final byte byte0 = Convert.intToByte(int0);
		Assertions.assertEquals(-22, byte0);

		final int int1 = Convert.byteToUnsignedInt(byte0);
		Assertions.assertEquals(int0, int1);
	}

	@Test
	public void intAndBytesTest() {
		// 测试 int 转 byte 数组
		final int int2 = 1417;
		final byte[] bytesInt = Convert.intToBytes(int2);

		// 测试 byte 数组转 int
		final int int3 = Convert.bytesToInt(bytesInt);
		Assertions.assertEquals(int2, int3);
	}

	@Test
	public void longAndBytesTest() {
		// 测试 long 转 byte 数组
		final long long1 = 2223;

		final byte[] bytesLong = Convert.longToBytes(long1);
		final long long2 = Convert.bytesToLong(bytesLong);

		Assertions.assertEquals(long1, long2);
	}

	@Test
	public void shortAndBytesTest() {
		final short short1 = 122;
		final byte[] bytes = Convert.shortToBytes(short1);
		final short short2 = Convert.bytesToShort(bytes);

		Assertions.assertEquals(short2, short1);
	}

	@Test
	public void toListTest() {
		final List<String> list = Arrays.asList("1", "2");
		final String str = Convert.toStr(list);
		final List<String> list2 = Convert.toList(String.class, str);
		Assertions.assertEquals("1", list2.get(0));
		Assertions.assertEquals("2", list2.get(1));

		final List<Integer> list3 = Convert.toList(Integer.class, str);
		Assertions.assertEquals(1, list3.get(0).intValue());
		Assertions.assertEquals(2, list3.get(1).intValue());
	}

	@Test
	public void toListTest2(){
		final String str = "1,2";
		final List<String> list2 = Convert.toList(String.class, str);
		Assertions.assertEquals("1", list2.get(0));
		Assertions.assertEquals("2", list2.get(1));

		final List<Integer> list3 = Convert.toList(Integer.class, str);
		Assertions.assertEquals(1, list3.get(0).intValue());
		Assertions.assertEquals(2, list3.get(1).intValue());
	}

	@Test
	public void toByteArrayTest(){
		// 测试Serializable转换为bytes，调用序列化转换
		final byte[] bytes = Convert.toPrimitiveByteArray(new Product("zhangsan", "张三", "5.1.1"));
		Assertions.assertNotNull(bytes);

		final Product product = Convert.convert(Product.class, bytes);
		Assertions.assertEquals("zhangsan", product.getName());
		Assertions.assertEquals("张三", product.getCName());
		Assertions.assertEquals("5.1.1", product.getVersion());
	}

	@Test
	public void numberToByteArrayTest(){
		// 测试Serializable转换为bytes，调用序列化转换
		final byte[] bytes = Convert.toPrimitiveByteArray(12L);
		Assertions.assertArrayEquals(ByteUtil.toBytes(12L), bytes);
	}

	@Test
	public void toAtomicIntegerArrayTest(){
		final String str = "1,2";
		final AtomicIntegerArray atomicIntegerArray = Convert.convert(AtomicIntegerArray.class, str);
		Assertions.assertEquals("[1, 2]", atomicIntegerArray.toString());
	}

	@Test
	public void toAtomicLongArrayTest(){
		final String str = "1,2";
		final AtomicLongArray atomicLongArray = Convert.convert(AtomicLongArray.class, str);
		Assertions.assertEquals("[1, 2]", atomicLongArray.toString());
	}

	@Test
	public void toClassTest(){
		final Class<?> convert = Convert.convert(Class.class, "org.dromara.hutool.core.convert.ConvertTest.Product");
		Assertions.assertSame(Product.class, convert);
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
		Assertions.assertEquals(1, integer.intValue());
	}

	@Test
	public void toSetTest(){
		final Set<Integer> result = Convert.convert(new TypeReference<Set<Integer>>() {
		}, "1,2,3");
		Assertions.assertEquals(SetUtil.of(1,2,3), result);
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

		BuildingType(final int id, final String name){
			this.id = id;
			this.name = name;
		}
	}

	@Test
	public void toDateTest(){
		Assertions.assertThrows(DateException.class, ()->{
			// 默认转换失败报错而不是返回null
			Convert.convert(Date.class, "aaaa");
		});
	}

	@Test
	public void toDateTest2(){
		final Date date = Convert.toDate("2021-01");
		Assertions.assertNull(date);
	}

	@Test
	public void toSqlDateTest(){
		final java.sql.Date date = Convert.convert(java.sql.Date.class, DateUtil.parse("2021-07-28"));
		Assertions.assertEquals("2021-07-28", date.toString());
	}

	@Test
	public void toHashtableTest(){
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a1", "v1");
		map.put("a2", "v2");
		map.put("a3", "v3");

		@SuppressWarnings("unchecked")
		final Hashtable<String, String> hashtable = Convert.convert(Hashtable.class, map);
		Assertions.assertEquals("v1", hashtable.get("a1"));
		Assertions.assertEquals("v2", hashtable.get("a2"));
		Assertions.assertEquals("v3", hashtable.get("a3"));
	}

	@Test
	public void toBigDecimalTest(){
		// https://github.com/dromara/hutool/issues/1818
		final String str = "33020000210909112800000124";
		final BigDecimal bigDecimal = Convert.toBigDecimal(str);
		Assertions.assertEquals(str, bigDecimal.toPlainString());

		Assertions.assertNull(Convert.toBigDecimal("  "));
	}

	@Test
	public void toFloatTest(){
		// https://gitee.com/dromara/hutool/issues/I4M0E4
		final String hex2 = "CD0CCB43";
		final byte[] value = HexUtil.decode(hex2);
		final float f = Convert.toFloat(value);
		Assertions.assertEquals(406.1F, f, 0);
	}

	@Test
	public void floatToDoubleTest(){
		final float a = 0.45f;
		final double b = Convert.toDouble(a);
		Assertions.assertEquals(0.45D, b, 0);
	}

	@Test
	public void floatToDoubleAddrTest(){
		final float a = 0.45f;
		final DoubleAdder adder = Convert.convert(DoubleAdder.class, a);
		Assertions.assertEquals(0.45D, adder.doubleValue(), 0);
	}

	@Test
	public void doubleToFloatTest(){
		final double a = 0.45f;
		final float b = Convert.toFloat(a);
		Assertions.assertEquals(a, b, 0);
	}

	@Test
	public void localDateTimeToLocalDateTest(){
		final LocalDateTime localDateTime = LocalDateTime.now();
		final LocalDate convert = Convert.convert(LocalDate.class, localDateTime);
		Assertions.assertEquals(localDateTime.toLocalDate(), convert);
	}

	@Test
	public void toSBCTest(){
		final String s = Convert.toSBC(null);
		Assertions.assertNull(s);
	}

	@Test
	public void toDBCTest(){
		final String s = Convert.toDBC(null);
		Assertions.assertNull(s);
	}

	@Test
	public void convertQuietlyTest(){
		final String a = "12";
		final Object s = Convert.convertQuietly(int.class, a, a);
		Assertions.assertEquals(12, s);
	}
}
