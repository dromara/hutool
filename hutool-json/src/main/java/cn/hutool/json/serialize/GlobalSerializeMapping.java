/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package cn.hutool.json.serialize;

import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.reflect.NullType;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSON;

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
		if (null == serializerMap) {
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
		if (null == deserializerMap) {
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
