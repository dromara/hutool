package cn.hutool.core.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.DoubleAdder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 类型转换工具单元测试
 *
 * @author Looly
 */
public class ConvertTest {

	@Test
	public void toObjectTest() {
		final Object result = Convert.convert(Object.class, "aaaa");
		assertEquals("aaaa", result);
	}

	@Test
	public void toStrTest() {
		final int a = 1;
		final long[] b = {1, 2, 3, 4, 5};

		assertEquals("[1, 2, 3, 4, 5]", Convert.convert(String.class, b));

		final String aStr = Convert.toStr(a);
		assertEquals("1", aStr);
		final String bStr = Convert.toStr(b);
		assertEquals("[1, 2, 3, 4, 5]", Convert.toStr(bStr));
	}

	@Test
	public void toStrTest2() {
		final String result = Convert.convert(String.class, "aaaa");
		assertEquals("aaaa", result);
	}

	@Test
	public void toStrTest3() {
		final char a = 'a';
		final String result = Convert.convert(String.class, a);
		assertEquals("a", result);
	}

	@Test
	public void toStrTest4() {
		// 被当作八进制
		@SuppressWarnings("OctalInteger") final String result = Convert.toStr(001200);
		assertEquals("640", result);
	}

	@Test
	public void toIntTest() {
		final String a = " 34232";
		final Integer aInteger = Convert.toInt(a);
		assertEquals(Integer.valueOf(34232), aInteger);
		final int aInt = ConverterRegistry.getInstance().convert(int.class, a);
		assertEquals(34232, aInt);

		// 带小数测试
		final String b = " 34232.00";
		final Integer bInteger = Convert.toInt(b);
		assertEquals(Integer.valueOf(34232), bInteger);
		final int bInt = ConverterRegistry.getInstance().convert(int.class, b);
		assertEquals(34232, bInt);

		// boolean测试
		final boolean c = true;
		final Integer cInteger = Convert.toInt(c);
		assertEquals(Integer.valueOf(1), cInteger);
		final int cInt = ConverterRegistry.getInstance().convert(int.class, c);
		assertEquals(1, cInt);

		// boolean测试
		final String d = "08";
		final Integer dInteger = Convert.toInt(d);
		assertEquals(Integer.valueOf(8), dInteger);
		final int dInt = ConverterRegistry.getInstance().convert(int.class, d);
		assertEquals(8, dInt);
	}

	@Test
	public void toIntTest2() {
		final ArrayList<String> array = new ArrayList<>();
		final Integer aInt = Convert.convertQuietly(Integer.class, array, -1);
		assertEquals(Integer.valueOf(-1), aInt);
	}

	@Test
	public void toLongTest() {
		final String a = " 342324545435435";
		final Long aLong = Convert.toLong(a);
		assertEquals(Long.valueOf(342324545435435L), aLong);
		final long aLong2 = ConverterRegistry.getInstance().convert(long.class, a);
		assertEquals(342324545435435L, aLong2);

		// 带小数测试
		final String b = " 342324545435435.245435435";
		final Long bLong = Convert.toLong(b);
		assertEquals(Long.valueOf(342324545435435L), bLong);
		final long bLong2 = ConverterRegistry.getInstance().convert(long.class, b);
		assertEquals(342324545435435L, bLong2);

		// boolean测试
		final boolean c = true;
		final Long cLong = Convert.toLong(c);
		assertEquals(Long.valueOf(1), cLong);
		final long cLong2 = ConverterRegistry.getInstance().convert(long.class, c);
		assertEquals(1, cLong2);

		// boolean测试
		final String d = "08";
		final Long dLong = Convert.toLong(d);
		assertEquals(Long.valueOf(8), dLong);
		final long dLong2 = ConverterRegistry.getInstance().convert(long.class, d);
		assertEquals(8, dLong2);
	}

	@Test
	public void toLongFromNumberWithFormatTest() {
		final NumberWithFormat value = new NumberWithFormat(1678285713935L, null);
		final Long aLong = Convert.convertWithCheck(Long.class, value, null, false);
		assertEquals(new Long(1678285713935L), aLong);
	}

	@Test
	public void toCharTest() {
		final String str = "aadfdsfs";
		final Character c = Convert.toChar(str);
		assertEquals(Character.valueOf('a'), c);

		// 转换失败
		final Object str2 = "";
		final Character c2 = Convert.toChar(str2);
		assertNull(c2);
	}

	@Test
	public void toNumberTest() {
		final Object a = "12.45";
		final Number number = Convert.toNumber(a);
		assertEquals(12.45D, number.doubleValue(), 0);
	}

	@Test
	public void emptyToNumberTest() {
		final Object a = "";
		final Number number = Convert.toNumber(a);
		assertNull(number);
	}

	@Test
	public void intAndByteConvertTest() {
		// 测试 int 转 byte
		final int int0 = 234;
		final byte byte0 = Convert.intToByte(int0);
		assertEquals(-22, byte0);

		final int int1 = Convert.byteToUnsignedInt(byte0);
		assertEquals(int0, int1);
	}

	@Test
	public void intAndBytesTest() {
		// 测试 int 转 byte 数组
		final int int2 = 1417;
		final byte[] bytesInt = Convert.intToBytes(int2);

		// 测试 byte 数组转 int
		final int int3 = Convert.bytesToInt(bytesInt);
		assertEquals(int2, int3);
	}

	@Test
	public void longAndBytesTest() {
		// 测试 long 转 byte 数组
		final long long1 = 2223;

		final byte[] bytesLong = Convert.longToBytes(long1);
		final long long2 = Convert.bytesToLong(bytesLong);

		assertEquals(long1, long2);
	}

	@Test
	public void shortAndBytesTest() {
		final short short1 = 122;
		final byte[] bytes = Convert.shortToBytes(short1);
		final short short2 = Convert.bytesToShort(bytes);

		assertEquals(short2, short1);
	}

	@Test
	public void toListTest() {
		final List<String> list = Arrays.asList("1", "2");
		final String str = Convert.toStr(list);
		final List<String> list2 = Convert.toList(String.class, str);
		assertEquals("1", list2.get(0));
		assertEquals("2", list2.get(1));

		final List<Integer> list3 = Convert.toList(Integer.class, str);
		assertEquals(1, list3.get(0).intValue());
		assertEquals(2, list3.get(1).intValue());
	}

	@Test
	public void toListTest2() {
		final String str = "1,2";
		final List<String> list2 = Convert.toList(String.class, str);
		assertEquals("1", list2.get(0));
		assertEquals("2", list2.get(1));

		final List<Integer> list3 = Convert.toList(Integer.class, str);
		assertEquals(1, list3.get(0).intValue());
		assertEquals(2, list3.get(1).intValue());
	}

	@Test
	public void toByteArrayTest() {
		// 测试Serializable转换为bytes，调用序列化转换
		final byte[] bytes = Convert.toPrimitiveByteArray(new Product("zhangsan", "张三", "5.1.1"));
		assertNotNull(bytes);

		final Product product = Convert.convert(Product.class, bytes);
		assertEquals("zhangsan", product.getName());
		assertEquals("张三", product.getCName());
		assertEquals("5.1.1", product.getVersion());
	}

	@Test
	public void numberToByteArrayTest() {
		// 测试Serializable转换为bytes，调用序列化转换
		final byte[] bytes = Convert.toPrimitiveByteArray(12L);
		assertArrayEquals(ByteUtil.longToBytes(12L), bytes);
	}

	@Test
	public void toAtomicIntegerArrayTest() {
		final String str = "1,2";
		final AtomicIntegerArray atomicIntegerArray = Convert.convert(AtomicIntegerArray.class, str);
		assertEquals("[1, 2]", atomicIntegerArray.toString());
	}

	@Test
	public void toAtomicLongArrayTest() {
		final String str = "1,2";
		final AtomicLongArray atomicLongArray = Convert.convert(AtomicLongArray.class, str);
		assertEquals("[1, 2]", atomicLongArray.toString());
	}

	@Test
	public void toClassTest() {
		final Class<?> convert = Convert.convert(Class.class, "cn.hutool.core.convert.ConvertTest.Product");
		assertSame(Product.class, convert);
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
	public void enumToIntTest() {
		final Integer integer = Convert.toInt(BuildingType.CUO);
		assertEquals(1, integer.intValue());
	}

	@Test
	public void toSetTest() {
		final Set<Integer> result = Convert.convert(new TypeReference<Set<Integer>>() {
		}, "1,2,3");
		assertEquals(CollUtil.set(false, 1, 2, 3), result);
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

		BuildingType(final int id, final String name) {
			this.id = id;
			this.name = name;
		}
	}

	@Test
	public void toDateTest() {
		assertThrows(DateException.class, () -> {
			// 默认转换失败报错而不是返回null
			Convert.convert(Date.class, "aaaa");
		});
	}

	@Test
	public void toDateTest2() {
		final Date date = Convert.toDate("2021-01");
		assertNull(date);
	}

	@Test
	public void toSqlDateTest() {
		final java.sql.Date date = Convert.convert(java.sql.Date.class, DateUtil.parse("2021-07-28"));
		assertEquals("2021-07-28", date.toString());
	}

	@Test
	public void toHashtableTest() {
		final Map<String, String> map = MapUtil.newHashMap();
		map.put("a1", "v1");
		map.put("a2", "v2");
		map.put("a3", "v3");

		@SuppressWarnings("unchecked") final Hashtable<String, String> hashtable = Convert.convert(Hashtable.class, map);
		assertEquals("v1", hashtable.get("a1"));
		assertEquals("v2", hashtable.get("a2"));
		assertEquals("v3", hashtable.get("a3"));
	}

	@Test
	public void toBigDecimalTest() {
		// https://github.com/dromara/hutool/issues/1818
		final String str = "33020000210909112800000124";
		final BigDecimal bigDecimal = Convert.toBigDecimal(str);
		assertEquals(str, bigDecimal.toPlainString());
	}

	@Test
	public void toFloatTest() {
		// https://gitee.com/dromara/hutool/issues/I4M0E4
		final String hex2 = "CD0CCB43";
		final byte[] value = HexUtil.decodeHex(hex2);
		final float f = Convert.toFloat(value);
		assertEquals(406.1F, f, 0);
	}

	@Test
	public void floatToDoubleTest() {
		final float a = 0.45f;
		final double b = Convert.toDouble(a);
		assertEquals(0.45D, b, 0);
	}

	@Test
	public void floatToDoubleAddrTest() {
		final float a = 0.45f;
		final DoubleAdder adder = Convert.convert(DoubleAdder.class, a);
		assertEquals(0.45D, adder.doubleValue(), 0);
	}

	@Test
	public void doubleToFloatTest() {
		final double a = 0.45f;
		final float b = Convert.toFloat(a);
		assertEquals(a, b, 0);
	}

	@Test
	public void localDateTimeToLocalDateTest() {
		final LocalDateTime localDateTime = LocalDateTime.now();
		final LocalDate convert = Convert.convert(LocalDate.class, localDateTime);
		assertEquals(localDateTime.toLocalDate(), convert);
	}

	@Test
	public void toSBCTest() {
		final String s = Convert.toSBC(null);
		assertNull(s);
	}

	@Test
	public void toDBCTest() {
		final String s = Convert.toDBC(null);
		assertNull(s);
	}

	@Test
	public void testChineseMoneyToNumber() {
		/*
		 * s=陆万柒仟伍佰伍拾陆圆, n=67556
		 * s=陆万柒仟伍佰伍拾陆元, n=67556
		 * s=叁角, n=0.3
		 * s=贰分, n=0.02
		 * s=陆万柒仟伍佰伍拾陆元叁角, n=67556.3
		 * s=陆万柒仟伍佰伍拾陆元贰分, n=67556.02
		 * s=叁角贰分, n=0.32
		 * s=陆万柒仟伍佰伍拾陆元叁角贰分, n=67556.32
		 */
		assertEquals(67556, Convert.chineseMoneyToNumber("陆万柒仟伍佰伍拾陆圆").longValue());
		assertEquals(67556, Convert.chineseMoneyToNumber("陆万柒仟伍佰伍拾陆元").longValue());
		assertEquals(0.3D, Convert.chineseMoneyToNumber("叁角").doubleValue(), 0);
		assertEquals(0.02, Convert.chineseMoneyToNumber("贰分").doubleValue(), 0);
		assertEquals(67556.3, Convert.chineseMoneyToNumber("陆万柒仟伍佰伍拾陆元叁角").doubleValue(), 0);
		assertEquals(67556.02, Convert.chineseMoneyToNumber("陆万柒仟伍佰伍拾陆元贰分").doubleValue(), 0);
		assertEquals(0.32, Convert.chineseMoneyToNumber("叁角贰分").doubleValue(), 0);
		assertEquals(67556.32, Convert.chineseMoneyToNumber("陆万柒仟伍佰伍拾陆元叁角贰分").doubleValue(), 0);
	}

	@Test
	public void convertQuietlyTest() {
		assertThrows(Exception.class, () -> {
			final String a = "12";
			final Object s = Convert.convert(int.class, a, a);
			assertEquals(12, s);
		});
	}

	@Test
	public void issue3662Test() {
		String s = Convert.digitToChinese(0);
		assertEquals("零元整", s);

		s = Convert.digitToChinese(null);
		assertEquals("零元整", s);
	}
}
