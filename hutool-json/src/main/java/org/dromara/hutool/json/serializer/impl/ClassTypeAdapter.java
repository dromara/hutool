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

package org.dromara.hutool.json.serializer.impl;

import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.JSONDeserializer;
import org.dromara.hutool.json.serializer.JSONSerializer;

import java.lang.reflect.Type;

/**
 * Class类型适配器，用于将Class对象序列化为字符串，反序列化为Class对象<br>
 * 注意：考虑安全问题，此类并不作为默认的适配器，如需启用，需：
 * <pre>{@code
 *   final JSONFactory factory = JSONFactory.of(null, null);
 *   factory.register(Class<?>.class, ClassTypeAdapter.INSTANCE);
 * }</pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class ClassTypeAdapter implements JSONSerializer<Class<?>>, JSONDeserializer<Class<?>> {

	/**
	 * 单例
	 */
	public static final ClassTypeAdapter INSTANCE = new ClassTypeAdapter();

	@Override
	public JSON serialize(final Class<?> bean, final JSONContext context) {
		return context.getOrCreatePrimitive(bean.getName());
	}

	@Override
	public Class<?> deserialize(final JSON json, final Type deserializeType) {
		return ClassUtil.forName((String) json.asJSONPrimitive().getValue(), true, null);
	}
}
