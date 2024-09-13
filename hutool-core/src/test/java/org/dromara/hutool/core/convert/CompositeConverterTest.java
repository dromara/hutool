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

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * ConverterRegistry 单元测试
 *
 * @author Looly
 */
public class CompositeConverterTest {
	@Test
	void convertEmptyTest() {
		final Object convert = CompositeConverter.getInstance().convert(EmptyBean.class, "");
		assertNotNull(convert);
		assertEquals(new EmptyBean(), convert);
	}

	@Data
	public static class EmptyBean {}

	@Test
	public void customTest() {
		final int a = 454553;
		final CompositeConverter compositeConverter = CompositeConverter.getInstance();

		CharSequence result = (CharSequence) compositeConverter.convert(CharSequence.class, a);
		assertEquals("454553", result);

		//此处做为示例自定义CharSequence转换，因为Hutool中已经提供CharSequence转换，请尽量不要替换
		//替换可能引发关联转换异常（例如覆盖CharSequence转换会影响全局）
		compositeConverter.register(CharSequence.class, new CustomConverter());
		result = (CharSequence) compositeConverter.convert(CharSequence.class, a);
		assertEquals("Custom: 454553", result);
	}

	public static class CustomConverter implements Converter {
		@Override
		public Object convert(final Type targetType, final Object value) throws ConvertException {
			return "Custom: " + value.toString();
		}
	}
}
