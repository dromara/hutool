package cn.hutool.core.convert;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;

/**
 * ConverterRegistry 单元测试
 * @author Looly
 *
 */
public class CompositeConverterTest {

	@Test
	public void getConverterTest() {
		final Converter converter = CompositeConverter.getInstance().getConverter(CharSequence.class, false);
		Assert.assertNotNull(converter);
	}

	@Test
	public void customTest(){
		final int a = 454553;
		final CompositeConverter compositeConverter = CompositeConverter.getInstance();

		CharSequence result = (CharSequence) compositeConverter.convert(CharSequence.class, a);
		Assert.assertEquals("454553", result);

		//此处做为示例自定义CharSequence转换，因为Hutool中已经提供CharSequence转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖CharSequence转换会影响全局）
		compositeConverter.putCustom(CharSequence.class, new CustomConverter());
		result = (CharSequence) compositeConverter.convert(CharSequence.class, a);
		Assert.assertEquals("Custom: 454553", result);
	}

	public static class CustomConverter implements Converter{
		@Override
		public Object convert(final Type targetType, final Object value) throws ConvertException {
			return "Custom: " + value.toString();
		}
	}
}
