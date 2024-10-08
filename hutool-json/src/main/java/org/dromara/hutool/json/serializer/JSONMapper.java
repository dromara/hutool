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

package org.dromara.hutool.json.serializer;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.lang.loader.LazyFunLoader;
import org.dromara.hutool.core.lang.loader.Loader;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.*;
import org.dromara.hutool.json.serializer.impl.CharSequenceTypeAdapter;
import org.dromara.hutool.json.serializer.impl.DefaultDeserializer;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * 对象和JSON值映射器，用于Java对象和JSON对象互转<br>
 * <ul>
 *     <li>Java对象转JSON：{@link #toJSON(Object, boolean)}</li>
 *     <li>JSON转Java对象：{@link #toBean(JSON, Type)}</li>
 * </ul>
 * <p>
 * 转换依赖于{@link JSONSerializer}和{@link JSONDeserializer}的实现，通过{@link TypeAdapterManager}统一管理<br>
 * 序列化和反序列化定义于两个作用域，首先查找本类中定义的，如果没有，使用{@link TypeAdapterManager#getInstance()} 查找全局定义的。
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONMapper implements Serializable {

	private static final long serialVersionUID = -6714488573738940582L;

	/**
	 * 创建JSONMapper
	 *
	 * @param factory {@link JSONFactory}
	 * @return ObjectMapper
	 */
	public static JSONMapper of(final JSONFactory factory) {
		return new JSONMapper(factory);
	}

	private final JSONFactory factory;
	private Loader<TypeAdapterManager> typeAdapterManagerLoader;

	/**
	 * 构造
	 *
	 * @param factory {@link JSONFactory}
	 */
	public JSONMapper(final JSONFactory factory) {
		this.factory = factory;
		this.typeAdapterManagerLoader = LazyFunLoader.of(TypeAdapterManager::of);
	}

	// region ----- typeAdapterManager

	/**
	 * 获取自定义类型转换器，用于将自定义类型转换为JSONObject
	 *
	 * @return 类型转换器管理器
	 */
	public TypeAdapterManager getTypeAdapterManager() {
		return typeAdapterManagerLoader.get();
	}

	/**
	 * 设置自定义类型转换器，用于将自定义类型转换为JSONObject
	 *
	 * @param typeAdapterManager 类型转换器管理器，不能为空
	 * @return this
	 */
	public JSONMapper setTypeAdapterManager(final TypeAdapterManager typeAdapterManager) {
		this.typeAdapterManagerLoader = () -> Assert.notNull(typeAdapterManager);
		return this;
	}

	/**
	 * 注册自定义类型适配器，用于自定义对象序列化和反序列化
	 *
	 * @param type        类型
	 * @param typeAdapter 自定义序列化器，{@code null}表示移除
	 * @return this
	 */
	public JSONMapper register(final Type type, final TypeAdapter typeAdapter) {
		getTypeAdapterManager().register(type, typeAdapter);
		return this;
	}

	/**
	 * 注册自定义类型适配器，用于自定义对象序列化和反序列化<br>
	 * 提供的适配器必须为实现{@link MatcherJSONSerializer}或{@link MatcherJSONDeserializer}接口<br>
	 * 当两个接口都实现时，同时注册序列化和反序列化器
	 *
	 * @param typeAdapter 自定义类型适配器
	 * @return this
	 */
	public JSONMapper register(final TypeAdapter typeAdapter) {
		getTypeAdapterManager().register(typeAdapter);
		return this;
	}
	//endregion

	/**
	 * 转为实体类对象
	 *
	 * @param <T>  Bean类型
	 * @param json JSON
	 * @param type {@link Type}
	 * @return 实体类对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T toBean(final JSON json, Type type) {
		if (type instanceof TypeReference) {
			type = ((TypeReference<?>) type).getType();
		}

		final JSONDeserializer<Object> deserializer = getDeserializer(json, type);
		try {
			return (T) deserializer.deserialize(json, type);
		} catch (final Exception e) {
			if (ObjUtil.defaultIfNull(this.factory.getConfig(), JSONConfig::isIgnoreError, false)) {
				return null;
			}
			throw e;
		}
	}

	// region ----- toJSON
	/**
	 * 在需要的时候转换映射对象<br>
	 * 包装包括：
	 * <ul>
	 *   <li>array or collection =》 {@link JSONArray}</li>
	 *   <li>map or bean =》 {@link JSONObject}</li>
	 *   <li>standard property (number boolean or char) =》 {@link JSONPrimitive}</li>
	 *   <li>String =》 parseIfString为{@code true}时解析为{@link JSON}，{@code false}直接包装为{@link JSONPrimitive}</li>
	 *   <li>其它 =》 尝试包装为JSONObject，否则返回{@code null}</li>
	 * </ul>
	 *
	 * @param obj 被映射的对象
	 * @param parseIfString 如果提供的是字符串，是否解析为JSON，{@code false}则直接包装为{@link JSONPrimitive}
	 * @return 映射后的值，null表示此值需被忽略
	 */
	public JSON toJSON(final Object obj, final boolean parseIfString) {
		return mapTo(obj, null, parseIfString);
	}

	/**
	 * 在需要的时候转换映射对象<br>
	 * 包装包括：
	 * <ul>
	 *   <li>map =》 JSONObject</li>
	 *   <li>其它 =》 尝试包装为JSONObject，否则返回{@code null}</li>
	 * </ul>
	 *
	 * @param obj 被映射的对象
	 * @return 映射后的值，null表示此值需被忽略
	 */
	public JSONObject toJSONObject(final Object obj) {
		return mapTo(obj, factory.ofObj(), false);
	}

	/**
	 * 在需要的时候转换映射对象<br>
	 * 包装包括：
	 * <ul>
	 *   <li>array or collection =》 JSONArray</li>
	 * </ul>
	 *
	 * @param obj 被映射的对象
	 * @return 映射后的值，null表示此值需被忽略
	 */
	public JSONArray toJSONArray(final Object obj) {
		return mapTo(obj, factory.ofArray(), false);
	}
	// endregion

	/**
	 * 在需要的时候转换映射对象<br>
	 * 包装包括：
	 * <ul>
	 *   <li>array or collection =》 {@link JSONArray}</li>
	 *   <li>map or bean =》 {@link JSONObject}</li>
	 *   <li>standard property (number boolean or char) =》 {@link JSONPrimitive}</li>
	 *   <li>String =》 parseIfString为{@code true}时解析为{@link JSON}，{@code false}直接包装为{@link JSONPrimitive}</li>
	 *   <li>其它 =》 尝试包装为JSONObject，否则返回{@code null}</li>
	 * </ul>
	 *
	 * @param obj           被映射的对象
	 * @param json          被映射的到的对象，{@code null}表示根据序列化器自动识别
	 * @param parseIfString 如果提供的是字符串，是否解析为JSON，{@code false}则直接包装为{@link JSONPrimitive},
	 *                      只有json参数为{@code null}时有效
	 * @param <T>           JSON类型
	 * @return 映射后的值，null表示此值需被忽略
	 */
	@SuppressWarnings({"unchecked"})
	private <T extends JSON> T mapTo(Object obj, final T json, final boolean parseIfString) {
		if (null == obj) {
			return null;
		}

		if (obj instanceof Optional) {
			obj = ((Optional<?>) obj).orElse(null);
			if (null == obj) {
				return null;
			}
		} else if (obj instanceof Opt) {
			obj = ((Opt<?>) obj).getOrNull();
			if (null == obj) {
				return null;
			}
		}

		// JSONPrimitive对象
		// 考虑性能问题，默认原始类型对象直接包装为JSONPrimitive，不再查找TypeAdapter
		// 如果原始类型想转为其他JSON类型，依旧可以查找TypeAdapter
		if (JSONPrimitive.isTypeForJSONPrimitive(obj)) {
			if (null == json) {
				// 未指定转换的JSON类型，对于String产生二义性
				// 通过parseIfString参数决定是解析字符串还是直接转为原始类型
				if (parseIfString && obj instanceof String) {
					return (T) CharSequenceTypeAdapter.INSTANCE.serialize((String)obj,
						new SimpleJSONContext(null, factory));
				}
				return (T) factory.ofPrimitive(obj);
			}
			if (json instanceof JSONPrimitive) {
				return (T) factory.ofPrimitive(obj);
			}
			// 用户想将Primitive对象转为特定JSON对象，则需要查找TypeAdapter
		}

		// JSON对象如果与预期结果类型一致，则直接返回
		if (obj instanceof JSON) {
			if (null != json) {
				if (obj.getClass() == json.getClass()) {
					return (T) obj;
				}
			} else {
				return (T) obj;
			}
		}

		final JSONSerializer<Object> serializer = getSerializer(obj, obj.getClass());
		final boolean ignoreError = ObjUtil.defaultIfNull(this.factory.getConfig(), JSONConfig::isIgnoreError, false);
		if (null == serializer) {
			if (ignoreError) {
				return null;
			}
			throw new JSONException("No deserializer for type: " + obj.getClass());
		}

		final JSON result;
		try {
			result = serializer.serialize(obj, new SimpleJSONContext(json, this.factory));
		} catch (final Exception e) {
			if (ignoreError) {
				return null;
			}
			throw e;
		}

		if (null == json || result.getClass() == json.getClass()) {
			return (T) result;
		}

		if (ignoreError) {
			return null;
		}
		throw new JSONException("JSON type not match, expect: {}, actual: {}",
			json.getClass().getName(), result.getClass().getName());
	}

	/**
	 * 获取JSON对象对应的序列化器，先查找局部自定义，如果没有则查找全局自定义
	 *
	 * @param obj   对象
	 * @param clazz 对象类型
	 * @return {@link JSONSerializer}
	 */
	private JSONSerializer<Object> getSerializer(final Object obj, final Class<?> clazz) {
		JSONSerializer<Object> serializer = null;
		// 自定义序列化
		if (this.typeAdapterManagerLoader.isInitialized()) {
			serializer = getTypeAdapterManager().getSerializer(obj, clazz);
		}
		// 全局自定义序列化
		if (null == serializer) {
			serializer = TypeAdapterManager.getInstance().getSerializer(obj, clazz);
		}
		return serializer;
	}

	/**
	 * 获取JSON对象对应的反序列化器，先查找局部自定义，如果没有则查找全局自定义，如果都没有则使用默认反序列化器
	 *
	 * @param json JSON对象
	 * @param type 反序列化目标类型
	 * @return {@link JSONDeserializer}
	 */
	private JSONDeserializer<Object> getDeserializer(final JSON json, final Type type) {
		JSONDeserializer<Object> deserializer = null;
		// 自定义反序列化
		if (this.typeAdapterManagerLoader.isInitialized()) {
			deserializer = getTypeAdapterManager().getDeserializer(json, type);
		}
		// 全局自定义反序列化
		if (null == deserializer) {
			deserializer = TypeAdapterManager.getInstance().getDeserializer(json, type);
		}
		// 默认反序列化
		if (null == deserializer) {
			deserializer = DefaultDeserializer.INSTANCE;
		}
		return deserializer;
	}
}
