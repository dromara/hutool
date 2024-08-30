/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.serialize;

import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;
import org.dromara.hutool.core.reflect.NullType;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.JSON;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局的序列化和反序列化器映射<br>
 * 在JSON和Java对象转换过程中，优先使用注册于此处的自定义转换<br>
 * 分别定义{@link JSONObjectSerializer}和{@link JSONArraySerializer}的原因是，实际加入对象到JSON中时，无法区分是JSONObject还是JSONArray
 *
 * @author Looly
 */
public class GlobalSerializeMapping {

	private static Map<Type, JSONSerializer<? extends JSON, ?>> serializerMap;
	private static Map<Type, JSONDeserializer<?>> deserializerMap;

	static {
		serializerMap = new SafeConcurrentHashMap<>();
		deserializerMap = new SafeConcurrentHashMap<>();

		final TemporalAccessorSerializer localDateSerializer = new TemporalAccessorSerializer(LocalDate.class);
		serializerMap.put(LocalDate.class, localDateSerializer);
		deserializerMap.put(LocalDate.class, localDateSerializer);

		final TemporalAccessorSerializer localDateTimeSerializer = new TemporalAccessorSerializer(LocalDateTime.class);
		serializerMap.put(LocalDateTime.class, localDateTimeSerializer);
		deserializerMap.put(LocalDateTime.class, localDateTimeSerializer);

		final TemporalAccessorSerializer localTimeSerializer = new TemporalAccessorSerializer(LocalTime.class);
		serializerMap.put(LocalTime.class, localTimeSerializer);
		deserializerMap.put(LocalTime.class, localTimeSerializer);
	}

	/**
	 * 加入自定义的JSONArray序列化器
	 *
	 * @param type       对象类型
	 * @param serializer 序列化器实现
	 */
	public static void putSerializer(final Type type, final JSONArraySerializer<?> serializer) {
		putInternal(type, serializer);
	}

	/**
	 * 加入自定义的JSONObject序列化器
	 *
	 * @param type       对象类型
	 * @param serializer 序列化器实现
	 */
	public static void putSerializer(final Type type, final JSONObjectSerializer<?> serializer) {
		putInternal(type, serializer);
	}

	/**
	 * 加入自定义的反序列化器
	 *
	 * @param type         对象类型
	 * @param deserializer 反序列化器实现
	 */
	synchronized public static void putDeserializer(final Type type, final JSONDeserializer<?> deserializer) {
		if (null == deserializerMap) {
			deserializerMap = new ConcurrentHashMap<>();
		}
		deserializerMap.put(ObjUtil.defaultIfNull(type, NullType.INSTANCE), deserializer);
	}

	/**
	 * 获取自定义的序列化器，如果未定义返回{@code null}
	 *
	 * @param type 类型
	 * @return 自定义的序列化器或者{@code null}
	 */
	public static JSONSerializer<? extends JSON, ?> getSerializer(final Type type) {
		if (null == serializerMap || null == type) {
			return null;
		}
		return serializerMap.get(ObjUtil.defaultIfNull(type, NullType.INSTANCE));
	}

	/**
	 * 获取自定义的反序列化器，如果未定义返回{@code null}
	 *
	 * @param type 类型
	 * @return 自定义的反序列化器或者{@code null}
	 */
	public static JSONDeserializer<?> getDeserializer(final Type type) {
		if (null == deserializerMap || null == type) {
			return null;
		}
		return deserializerMap.get(ObjUtil.defaultIfNull(type, NullType.INSTANCE));
	}

	/**
	 * 加入自定义的序列化器
	 *
	 * @param type       对象类型
	 * @param serializer 序列化器实现
	 */
	synchronized private static void putInternal(final Type type, final JSONSerializer<? extends JSON, ?> serializer) {
		if (null == serializerMap) {
			serializerMap = new ConcurrentHashMap<>();
		}
		serializerMap.put(ObjUtil.defaultIfNull(type, NullType.INSTANCE), serializer);
	}
}
