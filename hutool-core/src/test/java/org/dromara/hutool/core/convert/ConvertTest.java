/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 类型转换工具单元测试
 *
 * @author Looly
 *
 */
public class ConvertTest {

	@Test
	public void toObjectTest() {
		final Object result = ConvertUtil.convert(Object.class, "aaaa");
		assertEquals("aaaa", result);
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

		assertEquals("[1, 2, 3, 4, 5]", ConvertUtil.convert(String.class, b));

		final String aStr = ConvertUtil.toStr(a);
		assertEquals("1", aStr);
		final String bStr = ConvertUtil.toStr(b);
		assertEquals("[1, 2, 3, 4, 5]", ConvertUtil.toStr(bStr));
	}

	@Test
	public void toStrTest2() {
		final String result = ConvertUtil.convert(String.class, "aaaa");
		assertEquals("aaaa", result);
	}

	@Test
	public void toStrTest3() {
		final char a = 'a';
		final String result = ConvertUtil.convert(String.class, a);
		assertEquals("a", result);
	}

	@Test
	public void toStrTest4() {
		// 被当作八进制
		@SuppressWarnings("OctalInteger")
		final String result = ConvertUtil.toStr(001200);
		assertEquals("640", result);
	}

	@Test
	public void toStrTest5() {
		// 被转化的对象有值，正常转换
		final String a = "aaaa";
		final String aDefaultValue = "aDefault";
		final String aResult = ConvertUtil.toStr(a, aDefaultValue);
		assertEquals(aResult, a);

		// 被转化的对象为null，返回默认值
		final String b = null;
		final String bDefaultValue = "bDefault";
		final String bResult = ConvertUtil.toStr(b, bDefaultValue);
		assertEquals(bResult, bDefaultValue);

		// 转换失败，返回默认值
		final TestExceptionClass c = new TestExceptionClass();
		final String cDefaultValue = "cDefault";
		final String cResult = ConvertUtil.toStr(c, cDefaultValue);
		assertEquals(cResult, cDefaultValue);
	}

	@Test
	public void toIntTest() {
		final String a = " 34232";
		final Integer aInteger = ConvertUtil.toInt(a);
		assertEquals(Integer.valueOf(34232), aInteger);
		final int aInt = CompositeConverter.getInstance().convert(int.class, a);
		assertEquals(34232, aInt);

		// 带小数测试
		final String b = " 34232.00";
		final Integer bInteger = ConvertUtil.toInt(b);
		assertEquals(Integer.valueOf(34232), bInteger);
		final int bInt = CompositeConverter.getInstance().convert(int.class, b);
		assertEquals(34232, bInt);

		// boolean测试
		final boolean c = true;
		final Integer cInteger = ConvertUtil.toInt(c);
		assertEquals(Integer.valueOf(1), cInteger);
		final int cInt = CompositeConverter.getInstance().convert(int.class, c);
		assertEquals(1, cInt);

		// boolean测试
		final String d = "08";
		final Integer dInteger = ConvertUtil.toInt(d);
		assertEquals(Integer.valueOf(8), dInteger);
		final int dInt = CompositeConverter.getInstance().convert(int.class, d);
		assertEquals(8, dInt);
	}

	@Test
	public void toIntTest2() {
		final ArrayList<String> array = new ArrayList<>();
		final Integer aInt = ConvertUtil.convertQuietly(Integer.class, array, -1);
		assertEquals(Integer.valueOf(-1), aInt);
	}

	@Test
	public void toIntOfExceptionTest(){
		Assertions.assertThrows(NumberFormatException.class, ()->{
			final Integer d = ConvertUtil.convert(Integer.class, "d");
			Assertions.assertNotNull(d);
		});
	}

	@Test
	public void toLongTest() {
		final String a = " 342324545435435";
		final Long aLong = ConvertUtil.toLong(a);
		assertEquals(Long.valueOf(342324545435435L), aLong);
		final long aLong2 = CompositeConverter.getInstance().convert(long.class, a);
		assertEquals(342324545435435L, aLong2);

		// 带小数测试
		final String b = " 342324545435435.245435435";
		final Long bLong = ConvertUtil.toLong(b);
		assertEquals(Long.valueOf(342324545435435L), bLong);
		final long bLong2 = CompositeConverter.getInstance().convert(long.class, b);
		assertEquals(342324545435435L, bLong2);

		// boolean测试
		final boolean c = true;
		final Long cLong = ConvertUtil.toLong(c);
		assertEquals(Long.valueOf(1), cLong);
		final long cLong2 = CompositeConverter.getInstance().convert(long.class, c);
		assertEquals(1, cLong2);

		// boolean测试
		final String d = "08";
		final Long dLong = ConvertUtil.toLong(d);
		assertEquals(Long.valueOf(8), dLong);
		final long dLong2 = CompositeConverter.getInstance().convert(long.class, d);
		assertEquals(8, dLong2);
	}

	@Test
	public void toCharTest() {
		final String str = "aadfdsfs";
		final Character c = ConvertUtil.toChar(str);
		assertEquals(Character.valueOf('a'), c);

		// 转换失败
		final Object str2 = "";
		final Character c2 = ConvertUtil.toChar(str2);
		Assertions.assertNull(c2);
	}

	@Test
	public void toNumberTest() {
		final Object a = "12.45";
		final Number number = ConvertUtil.toNumber(a);
		assertEquals(12.45D, number.doubleValue(), 0);
	}

	@Test
	public void emptyToNumberTest() {
		final Object a = "";
		final Number number = ConvertUtil.toNumber(a);
		Assertions.assertNull(number);
	}

	@Test
	public void intAndByteConvertTest() {
		// 测试 int 转 byte
		final int int0 = 234;
		final byte byte0 = ConvertUtil.intToByte(int0);
		assertEquals(-22, byte0);

		final int int1 = ConvertUtil.byteToUnsignedInt(byte0);
		assertEquals(int0, int1);
	}

	@Test
	public void intAndBytesTest() {
		// 测试 int 转 byte 数组
		final int int2 = 1417;
		final byte[] bytesInt = ConvertUtil.intToBytes(int2);

		// 测试 byte 数组转 int
		final int int3 = ConvertUtil.bytesToInt(bytesInt);
		assertEquals(int2, int3);
	}

	@Test
	public void longAndBytesTest() {
		// 测试 long 转 byte 数组
		final long long1 = 2223;

		final byte[] bytesLong = ConvertUtil.longToBytes(long1);
		final long long2 = ConvertUtil.bytesToLong(bytesLong);

		assertEquals(long1, long2);
	}

	@Test
	public void shortAndBytesTest() {
		final short short1 = 122;
		final byte[] bytes = ConvertUtil.shortToBytes(short1);
		final short short2 = ConvertUtil.bytesToShort(bytes);

		assertEquals(short2, short1);
	}

	@Test
	public void toListTest() {
		final List<String> list = Arrays.asList("1", "2");
		final String str = ConvertUtil.toStr(list);
		final List<String> list2 = ConvertUtil.toList(String.class, str);
		assertEquals("1", list2.get(0));
		assertEquals("2", list2.get(1));

		final List<Integer> list3 = ConvertUtil.toList(Integer.class, str);
		assertEquals(1, list3.get(0).intValue());
		assertEquals(2, list3.get(1).intValue());
	}

	@Test
	public void toListTest2(){
		final String str = "1,2";
		final List<String> list2 = ConvertUtil.toList(String.class, str);
		assertEquals("1", list2.get(0));
		assertEquals("2", list2.get(1));

		final List<Integer> list3 = ConvertUtil.toList(Integer.class, str);
		assertEquals(1, list3.get(0).intValue());
		assertEquals(2, list3.get(1).intValue());
	}

	@Test
	public void toByteArrayTest(){
		// 测试Serializable转换为bytes，调用序列化转换
		final byte[] bytes = ConvertUtil.toPrimitiveByteArray(new Product("zhangsan", "张三", "5.1.1"));
		Assertions.assertNotNull(bytes);

		final Product product = ConvertUtil.convert(Product.class, bytes);
		assertEquals("zhangsan", product.getName());
		assertEquals("张三", product.getCName());
		assertEquals("5.1.1", product.getVersion());
	}

	@Test
	public void numberToByteArrayTest(){
		// 测试Serializable转换为bytes，调用序列化转换
		final byte[] bytes = ConvertUtil.toPrimitiveByteArray(12L);
		Assertions.assertArrayEquals(ByteUtil.toBytes(12L), bytes);
	}

	@Test
	public void toAtomicIntegerArrayTest(){
		final String str = "1,2";
		final AtomicIntegerArray atomicIntegerArray = ConvertUtil.convert(AtomicIntegerArray.class, str);
		assertEquals("[1, 2]", atomicIntegerArray.toString());
	}

	@Test
	public void toAtomicLongArrayTest(){
		final String str = "1,2";
		final AtomicLongArray atomicLongArray = ConvertUtil.convert(AtomicLongArray.class, str);
		assertEquals("[1, 2]", atomicLongArray.toString());
	}

	@Test
	public void toClassTest(){
		final Class<?> convert = ConvertUtil.convert(Class.class, "org.dromara.hutool.core.support.ConvertTest.Product");
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
		final Integer integer = ConvertUtil.toInt(BuildingType.CUO);
		assertEquals(1, integer.intValue());
	}

	@Test
	public void toSetTest(){
		final Set<Integer> result = ConvertUtil.convert(new TypeReference<Set<Integer>>() {
		}, "1,2,3");
		assertEquals(SetUtil.of(1,2,3), result);
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
			ConvertUtil.convert(Date.class, "aaaa");
		});
	}

	@Test
	public void toDateTest2(){
		final Date date = ConvertUtil.toDate("2021-01");
		assertEquals("2021-01-01", DateUtil.formatDate(date));
	}

	@Test
	public void toSqlDateTest(){
		final java.sql.Date date = ConvertUtil.convert(java.sql.Date.class, DateUtil.parse("2021-07-28"));
		assertEquals("2021-07-28", date.toString());
	}

	@Test
	public void toHashtableTest(){
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a1", "v1");
		map.put("a2", "v2");
		map.put("a3", "v3");

		@SuppressWarnings("unchecked")
		final Hashtable<String, String> hashtable = ConvertUtil.convert(Hashtable.class, map);
		assertEquals("v1", hashtable.get("a1"));
		assertEquals("v2", hashtable.get("a2"));
		assertEquals("v3", hashtable.get("a3"));
	}

	@Test
	public void toBigDecimalTest(){
		// https://github.com/dromara/hutool/issues/1818
		final String str = "33020000210909112800000124";
		final BigDecimal bigDecimal = ConvertUtil.toBigDecimal(str);
		assertEquals(str, bigDecimal.toPlainString());

		Assertions.assertNull(ConvertUtil.toBigDecimal("  "));
	}

	@Test
	public void toFloatTest(){
		// https://gitee.com/dromara/hutool/issues/I4M0E4
		final String hex2 = "CD0CCB43";
		final byte[] value = HexUtil.decode(hex2);
		final float f = ConvertUtil.toFloat(value);
		assertEquals(406.1F, f, 0);
	}

	@Test
	public void floatToDoubleTest(){
		final float a = 0.45f;
		final double b = ConvertUtil.toDouble(a);
		assertEquals(0.45D, b, 0);
	}

	@Test
	public void floatToDoubleAddrTest(){
		final float a = 0.45f;
		final DoubleAdder adder = ConvertUtil.convert(DoubleAdder.class, a);
		assertEquals(0.45D, adder.doubleValue(), 0);
	}

	@Test
	public void doubleToFloatTest(){
		final double a = 0.45f;
		final float b = ConvertUtil.toFloat(a);
		assertEquals(a, b, 0);
	}

	@Test
	public void localDateTimeToLocalDateTest(){
		final LocalDateTime localDateTime = LocalDateTime.now();
		final LocalDate convert = ConvertUtil.convert(LocalDate.class, localDateTime);
		assertEquals(localDateTime.toLocalDate(), convert);
	}

	@Test
	public void toSBCTest(){
		final String s = ConvertUtil.toSBC(null);
		Assertions.assertNull(s);
	}

	@Test
	public void toDBCTest(){
		final String s = ConvertUtil.toDBC(null);
		Assertions.assertNull(s);
	}

	@Test
	public void convertQuietlyTest(){
		final String a = "12";
		final Object s = ConvertUtil.convertQuietly(int.class, a, a);
		assertEquals(12, s);
	}

	@Test
	public void issue3662Test() {
		String s = ConvertUtil.digitToChinese(0);
		assertEquals("零元整", s);

		s = ConvertUtil.digitToChinese(null);
		assertEquals("零元整", s);
	}
}
