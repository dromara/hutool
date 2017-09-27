package com.xiaoleilu.hutool.core.convert;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.date.DateUtil;

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
	public void toIntTest(){
		String a = "34232";
		Integer value = Convert.toInt(a);
		Assert.assertEquals(Integer.valueOf(34232), value);
		
		int value2 = ConverterRegistry.getInstance().convert(int.class, a);
		Assert.assertTrue(34232 == value2);
	}
	
	@Test
	public void toCharTest(){
		String str = "aadfdsfs";
		Character c = Convert.toChar(str);
		Assert.assertEquals(Character.valueOf('a'), c);
		
		//转换失败
		Object str2 = "";
		Character c2 = Convert.toChar(str2);
		Assert.assertNull(c2);
	}
	
	@Test
	public void toNumberTest(){
		Object a = "12.45";
		Number number = Convert.toNumber(a);
		Assert.assertEquals(12.45D, number);
	}

	@Test
	public void toDateTest(){
		String a = "2017-05-06";
		Date value = Convert.convert(Date.class, a);
		Assert.assertEquals(a, DateUtil.formatDate(value));
	}
	
	@Test
	public void toSqlDateTest(){
		String a = "2017-05-06";
		java.sql.Date value = Convert.convert(java.sql.Date.class, a);
		Assert.assertEquals("2017-05-06", value.toString());
	}
}
