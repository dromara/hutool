package com.xiaoleilu.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoleilu.hutool.convert.Converter;
import com.xiaoleilu.hutool.convert.ConverterRegistry;
import com.xiaoleilu.hutool.convert.impl.ArrayConverter;

/**
 * ConverterRegistry 单元测试
 * @author Looly
 *
 */
public class ConverterRegistryTest {
	
	@Test
	public void convertTest(){
		int[] a = new int[]{1,2,3,4};
		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
		long[] result = converterRegistry.convert(long[].class, a);
		Assert.assertArrayEquals(new long[]{1L, 2L, 3L, 4L}, result);
	}
	
	@Test
	public void arrayConverterTest(){
		String arrayStr = "1,2,3,4,5";
		
		//获取Converter类的方法1，从ConverterRegistry单例对象中查找
		Converter<Integer[]> c = ConverterRegistry.getInstance().getDefaultConverter(Integer[].class);
		Integer[] result = c.convert(arrayStr, null);
		Assert.assertArrayEquals(new Integer[]{1,2,3,4,5}, result);
		
		//获取Converter类的方法2，自己实例化相应Converter对象
		ArrayConverter c2 = new ArrayConverter(Integer.class);
		Integer[] result2 = (Integer[]) c2.convert(arrayStr, null);
		Assert.assertArrayEquals(new Integer[]{1,2,3,4,5}, result2);
		
	}
	
	@Test
	public void primitiveArrayTest(){
		String arrayStr = "1,2,3,4,5";
		
		//获取Converter类的方法2，自己实例化相应Converter对象
		ArrayConverter c3 = new ArrayConverter(int.class);
		int[] result3 = (int[]) c3.convert(arrayStr, null);
		Assert.assertArrayEquals(new int[]{1,2,3,4,5}, result3);
	}
	
	@Test
	public void customTest(){
		int a = 454553;
		ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
		//此处做为示例自定义CharSequence转换，因为Hutool中已经提供CharSequence转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖CharSequence转换会影响全局）
		converterRegistry.putCustom(CharSequence.class, CustomConverter.class);
		CharSequence result = converterRegistry.convert(CharSequence.class, a);
		Assert.assertEquals("Custom: 454553", result);
	}
	
	public static class CustomConverter implements Converter<CharSequence>{
		@Override
		public CharSequence convert(Object value, CharSequence defaultValue) throws IllegalArgumentException {
			return "Custom: " + value.toString();
		}
	}
}
