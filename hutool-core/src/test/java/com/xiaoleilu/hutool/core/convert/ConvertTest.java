package com.xiaoleilu.hutool.core.convert;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.CharsetUtil;

/**
 * 类型转换工具单元测试
 * 
 * @author Looly
 *
 */
public class ConvertTest {

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
	public void toIntArrayTest() {
		String[] b = { "1", "2", "3", "4" };
		
		Integer[] intArray = Convert.toIntArray(b);
		Assert.assertArrayEquals(intArray, new Integer[]{1,2,3,4});
		
		long[] c = {1L,2L,3L,4L,5L};
		Integer[] intArray2 = Convert.toIntArray(c);
		Assert.assertArrayEquals(intArray2, new Integer[]{1,2,3,4,5});
	}
	
	@Test
	public void toLongArrayTest() {
		String[] b = { "1", "2", "3", "4" };
		
		Long[] intArray = Convert.toLongArray(b);
		Assert.assertArrayEquals(intArray, new Long[]{1L,2L,3L,4L});
		
		int[] c = {1,2,3,4,5};
		Long[] intArray2 = Convert.toLongArray(c);
		Assert.assertArrayEquals(intArray2, new Long[]{1L,2L,3L,4L,5L});
	}
	
	@Test
	public void toDoubleArrayTest() {
		String[] b = { "1", "2", "3", "4" };
		
		Double[] intArray = Convert.toDoubleArray(b);
		Assert.assertArrayEquals(intArray, new Double[]{1D,2D,3D,4D});
		
		int[] c = {1,2,3,4,5};
		Double[] intArray2 = Convert.toDoubleArray(c);
		Assert.assertArrayEquals(intArray2, new Double[]{1D,2D,3D,4D,5D});
	}
	
	@Test
	public void toSBCTest() {
		String a = "123456789";
		String sbc = Convert.toSBC(a);
		Assert.assertEquals("１２３４５６７８９", sbc);
	}
	
	@Test
	public void toDBCTest() {
		String a = "１２３４５６７８９";
		String dbc = Convert.toDBC(a);
		Assert.assertEquals("123456789", dbc);
	}
	
	@Test
	public void hexTest(){
		String a = "我是一个小小的可爱的字符串";
		String hex = Convert.toHex(a, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("e68891e698afe4b880e4b8aae5b08fe5b08fe79a84e58fafe788b1e79a84e5ad97e7aca6e4b8b2", hex);
		
		String raw = Convert.hexStrToStr(hex, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(a, raw);
	}
	
	@Test
	public void unicodeTest(){
		String a = "我是一个小小的可爱的字符串";
		
		String unicode = Convert.strToUnicode(a);
		Assert.assertEquals("\\u6211\\u662f\\u4e00\\u4e2a\\u5c0f\\u5c0f\\u7684\\u53ef\\u7231\\u7684\\u5b57\\u7b26\\u4e32", unicode);
		
		String raw = Convert.unicodeToStr(unicode);
		Assert.assertEquals(raw, a);
	}
	
	@Test
	public void convertCharsetTest(){
		String a = "我不是乱码";
		//转换后result为乱码
		String result = Convert.convertCharset(a, CharsetUtil.UTF_8, CharsetUtil.ISO_8859_1);
		String raw = Convert.convertCharset(result, CharsetUtil.ISO_8859_1, "UTF-8");
		Assert.assertEquals(raw, a);
	}
	
	@Test
	public void convertTimeTest(){
		long a = 4535345;
		long minutes = Convert.convertTime(a, TimeUnit.MILLISECONDS, TimeUnit.MINUTES);
		Assert.assertEquals(75, minutes);
	}
	
	@Test
	public void digitUppercaseTest(){
		double a = 67556.32;
		String digitUppercase = Convert.digitUppercase(a);
		Assert.assertEquals("陆万柒仟伍佰伍拾陆元叁角贰分", digitUppercase);
	}
	
	@Test
	public void wrapUnwrapTest(){
		//去包装
		Class<?> wrapClass = Integer.class;
		Class<?> unWraped = Convert.unWrap(wrapClass);
		Assert.assertEquals(int.class, unWraped);
		
		//包装
		Class<?> primitiveClass = long.class;
		Class<?> wraped = Convert.wrap(primitiveClass);
		Assert.assertEquals(Long.class, wraped);
	}
}
