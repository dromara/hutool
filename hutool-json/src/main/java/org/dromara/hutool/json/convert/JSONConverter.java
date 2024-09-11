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

package org.dromara.hutool.json.convert;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.bean.BeanUtil;
import org.dromara.hutool.core.bean.copier.BeanCopier;
import org.dromara.hutool.core.convert.*;
import org.dromara.hutool.core.convert.impl.DateConverter;
import org.dromara.hutool.core.convert.impl.TemporalAccessorConverter;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.map.MapWrapper;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.core.reflect.kotlin.KClassUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.*;
import org.dromara.hutool.json.reader.JSONParser;
import org.dromara.hutool.json.reader.JSONTokener;
import org.dromara.hutool.json.serialize.JSONDeserializer;
import org.dromara.hutool.json.serialize.JSONStringer;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

/**
 * JSON转换器，实现Object对象转换为{@link JSON}，支持的对象：
 * <ul>
 *     <li>任意支持的对象，转换为JSON</li>
 *     <li>JSON转换为指定对象Bean</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONConverter implements Converter, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final JSONConverter INSTANCE = new JSONConverter(null);

	static {
		final RegisterConverter converter = RegisterConverter.getInstance();
		converter.register(JSONObject.class, INSTANCE);
		converter.register(JSONArray.class, INSTANCE);
		converter.register(JSONPrimitive.class, INSTANCE);
	}

	/**
	 * 创建JSON转换器
	 *
	 * @param config JSON配置
	 * @return JSONConverter
	 */
	public static JSONConverter of(final JSONConfig config) {
		return new JSONConverter(config);
	}

	private final JSONConfig config;

	/**
	 * 构造
	 *
	 * @param config JSON配置
	 */
	public JSONConverter(final JSONConfig config) {
		this.config = config;
	}

	@Override
	public Object convert(Type targetType, Object value) throws ConvertException {
		if (null == value) {
			return null;
		}
		if (value instanceof JSONStringer) {
			// 被JSONString包装的对象，获取其原始类型
			value = ((JSONStringer) value).getRaw();
		}

		// JSON转对象
		if (value instanceof JSON) {
			if (targetType instanceof TypeReference) {
				// 还原原始类型
				targetType = ((TypeReference<?>) targetType).getType();
			}
			return toBean(targetType, (JSON) value);
		}

		// 对象转JSON
		final Class<?> targetClass = TypeUtil.getClass(targetType);
		if (null != targetClass) {
			if (JSON.class.isAssignableFrom(targetClass)) {
				return toJSON(value);
			}
			// 自定义日期格式
			if (Date.class.isAssignableFrom(targetClass) || TemporalAccessor.class.isAssignableFrom(targetClass)) {
				final Object date = toDateWithFormat(targetClass, value);
				if (null != date) {
					return date;
				}
			}
		}

		return ConvertUtil.convertWithCheck(targetType, value, null, config.isIgnoreError());
	}

	/**
	 * 实现Object对象转换为JSON对象，根据RFC8259规范，支持的对象：
	 * <ul>
	 *     <li>String: 转换为相应的对象，"和'包围的字符串返回原字符串，""返回{@code null}</li>
	 *     <li>Array、Iterable、Iterator：转换为JSONArray</li>
	 *     <li>Bean对象：转为JSONObject</li>
	 *     <li>Number、Boolean：返回原对象</li>
	 *     <li>null：返回{@code null}</li>
	 * </ul>
	 *
	 * @param obj 被转换的对象
	 * @return 转换后的对象
	 * @throws JSONException 转换异常
	 */
	public JSON toJSON(Object obj) throws JSONException {
		if (null == obj) {
			return null;
		}

		if (obj instanceof Optional) {
			obj = ((Optional<?>) obj).orElse(null);
		} else if (obj instanceof Opt) {
			obj = ((Opt<?>) obj).getOrNull();
		}

		if(obj instanceof JSON){
			return (JSON) obj;
		}

		if (obj instanceof Number || obj instanceof Boolean) {
			// RFC8259规范的原始类型数据
			return new JSONPrimitive(obj, config);
		}

		final JSON json;
		if (obj instanceof CharSequence) {
			return toJSON((CharSequence) obj);
		} else if (obj instanceof MapWrapper) {
			// MapWrapper实现了Iterable会被当作JSONArray，此处做修正
			json = new JSONObject(obj, config);
		} else if (obj instanceof Iterable || obj instanceof Iterator || ArrayUtil.isArray(obj)) {// 列表
			json = new JSONArray(obj, config);
		} else {// 对象
			json = new JSONObject(obj, config);
		}

		return json;
	}

	/**
	 * 实现{@link CharSequence}转换为JSON对象，根据RFC8259规范<br>
	 * 转换为相应的对象，"和'包围的字符串返回原字符串，""返回{@code null}
	 *
	 * @param str 被转换的字符串
	 * @return 转换后的对象
	 * @throws JSONException 转换异常
	 */
	public JSON toJSON(final CharSequence str) throws JSONException {
		if (null == str) {
			return null;
		}

		final String jsonStr = StrUtil.trim(str);
		if (jsonStr.isEmpty()) {
			// https://www.rfc-editor.org/rfc/rfc8259#section-7
			// 未被包装的空串理解为null
			return null;
		}

		// RFC8259，JSON字符串值、number, boolean, or null
		final JSONParser jsonParser = JSONParser.of(new JSONTokener(jsonStr), config);
		final Object value = jsonParser.nextValue();
		if (jsonParser.getTokener().nextClean() != JSONTokener.EOF) {
			// 对于用户提供的未转义字符串导致解析未结束，报错
			throw new JSONException("JSON format error: {}", jsonStr);
		}
		if(null == value || value instanceof JSON){
			return (JSON) value;
		}
		return new JSONPrimitive(value, config);
	}

	// ----------------------------------------------------------- Private method start

	/**
	 * JSON转Bean，流程为：
	 * <pre>{@code
	 *     自定义反序列化 --> 尝试转Kotlin --> 基于注册的标准转换器 --> Collection、Map等含有泛型的特殊转换器 --> 普通Bean转换器
	 * }</pre>
	 *
	 * @param <T>        目标类型
	 * @param targetType 目标类型，
	 * @param json       JSON
	 * @return bean
	 */
	@SuppressWarnings("unchecked")
	private <T> T toBean(final Type targetType, final JSON json) {
		// 自定义对象反序列化
		final JSONDeserializer<Object> deserializer = InternalJSONUtil.getDeserializer(targetType);

		if (null != deserializer) {
			return (T) deserializer.deserialize(json);
		}

		// 当目标类型不确定时，返回原JSON
		final Class<T> rawType = (Class<T>) TypeUtil.getClass(targetType);
		if (null == rawType) {
			return (T) json;
			//throw new JSONException("Can not get class from type: {}", targetType);
		}

		// issue#I5WDP0 对于Kotlin对象，由于参数可能非空限制，导致无法创建一个默认的对象再赋值
		if (KClassUtil.isKotlinClass(rawType) && json instanceof JSONGetter) {
			return KClassUtil.newInstance(rawType, new JSONGetterValueProvider<>((JSONGetter<String>) json));
		}

		final Object value;
		// JSON原始类型
		if(json instanceof JSONPrimitive){
			value = ((JSONPrimitive) json).getValue();
		} else {
			value = json;
		}

		// 标准转换器
		final Converter converter = RegisterConverter.getInstance().getConverter(targetType, value, true);
		if (null != converter) {
			return (T) converter.convert(targetType, value);
		}

		// 特殊类型转换，包括Collection、Map、强转、Array等
		final T result = (T) SpecialConverter.getInstance().convert(targetType, rawType, value);
		if (null != result) {
			return result;
		}

		// 尝试转Bean
		if (BeanUtil.isWritableBean(rawType)) {
			return BeanCopier.of(value,
				ConstructorUtil.newInstanceIfPossible(rawType), targetType,
				InternalJSONUtil.toCopyOptions(json.config())).copy();
		}

		// 跳过异常时返回null
		if (json.config().isIgnoreError()) {
			return null;
		}

		// 无法转换
		throw new JSONException("Can not convert from '{}': {} to '{}'",
			json.getClass().getName(), json, targetType.getTypeName());
	}

	private Object toDateWithFormat(final Class<?> targetClass, final Object value) {
		// 日期转换，支持自定义日期格式
		final String format = config.getDateFormat();
		if (StrUtil.isNotBlank(format)) {
			if (Date.class.isAssignableFrom(targetClass)) {
				return new DateConverter(format).convert(targetClass, value);
			} else {
				return new TemporalAccessorConverter(format).convert(targetClass, value);
			}
		}
		return null;
	}
	// ----------------------------------------------------------- Private method end
}
