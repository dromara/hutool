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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
		Assertions.assertNotNull(converter);
	}

	@Test
	public void customTest(){
		final int a = 454553;
		final CompositeConverter compositeConverter = CompositeConverter.getInstance();

		CharSequence result = (CharSequence) compositeConverter.convert(CharSequence.class, a);
		Assertions.assertEquals("454553", result);

		//此处做为示例自定义CharSequence转换，因为Hutool中已经提供CharSequence转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖CharSequence转换会影响全局）
		compositeConverter.putCustom(CharSequence.class, new CustomConverter());
		result = (CharSequence) compositeConverter.convert(CharSequence.class, a);
		Assertions.assertEquals("Custom: 454553", result);
	}

	public static class CustomConverter implements Converter{
		@Override
		public Object convert(final Type targetType, final Object value) throws ConvertException {
			return "Custom: " + value.toString();
		}
	}
}
