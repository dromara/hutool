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

package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.serializer.JSONMapper;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.function.Predicate;

/**
 * JSON工厂类，用于JSON创建、解析、转换为Bean等功能
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONFactory {

	/**
	 * 创建JSON工厂
	 *
	 * @param config    JSON配置
	 * @param predicate 键值对过滤器，{@code null}表示不过滤
	 * @return JSON工厂
	 */
	public static JSONFactory of(final JSONConfig config, final Predicate<MutableEntry<Object, Object>> predicate) {
		return new JSONFactory(config, predicate);
	}

	private final JSONConfig config;
	private final Predicate<MutableEntry<Object, Object>> predicate;
	private final JSONMapper mapper;

	/**
	 * 构造
	 *
	 * @param config    配置项
	 * @param predicate 键值对过滤器，用于过滤掉不需要的键值对，例如：过滤掉值为null的键值对
	 */
	public JSONFactory(final JSONConfig config, final Predicate<MutableEntry<Object, Object>> predicate) {
		this.config = ObjUtil.defaultIfNull(config, JSONConfig::of);
		this.predicate = predicate;
		this.mapper = JSONMapper.of(config, predicate);
	}

	/**
	 * 获取{@link JSONMapper}
	 *
	 * @return {@link JSONMapper}
	 */
	public JSONMapper getMapper() {
		return mapper;
	}

	// region ----- of

	/**
	 * 创建JSONObject
	 *
	 * @return JSONObject
	 */
	public JSONObject ofObj() {
		return new JSONObject(this.config);
	}

	/**
	 * 创建JSONArray
	 *
	 * @return JSONArray
	 */
	public JSONArray ofArray() {
		return new JSONArray(this.config);
	}

	/**
	 * 创建JSONPrimitive
	 *
	 * @param value 值
	 * @return JSONPrimitive
	 */
	public JSONPrimitive ofPrimitive(final Object value) {
		return new JSONPrimitive(value, this.config);
	}
	// endregion

	// region ----- parse

	/**
	 * JSON字符串转JSONObject对象
	 *
	 * @param obj Bean对象或者Map
	 * @return JSONObject
	 */
	public JSONObject parseObj(Object obj) {
		if (obj instanceof byte[]) {
			obj = new ByteArrayInputStream((byte[]) obj);
		}

		final JSONMapper jsonMapper = JSONMapper.of(config, predicate);
		if (obj instanceof CharSequence) {
			return (JSONObject) jsonMapper.map((CharSequence) obj);
		}
		return jsonMapper.mapObj(obj);
	}

	/**
	 * 对象转{@link JSONArray}，支持：
	 * <ul>
	 *     <li>{@link JSONObject} 遍历Entry，结果为：[{k1:v1}, {k2: v2}, ...]</li>
	 *     <li>{@link CharSequence}，解析[...]字符串</li>
	 *     <li>其它支持和自定义的对象（如集合、数组等）</li>
	 * </ul>
	 *
	 * @param obj       数组或集合对象
	 * @return JSONArray
	 */
	public JSONArray parseArray(final Object obj) {
		if (obj instanceof JSONObject) {
			final JSONMapper jsonMapper = JSONMapper.of(config, predicate);
			return jsonMapper.mapFromJSONObject((JSONObject) obj);
		}

		final JSONMapper jsonMapper = JSONMapper.of(config, predicate);
		if (obj instanceof CharSequence) {
			return (JSONArray) jsonMapper.map((CharSequence) obj);
		}
		return jsonMapper.mapArray(obj);
	}

	/**
	 * 转换对象为JSON，如果用户不配置JSONConfig，则JSON的有序与否与传入对象有关。<br>
	 * 支持的对象：
	 * <ul>
	 *     <li>String: 转换为相应的对象</li>
	 *     <li>Array、Iterable、Iterator：转换为JSONArray</li>
	 *     <li>Bean对象：转为JSONObject</li>
	 * </ul>
	 *
	 * @param obj 对象
	 * @return JSON（JSONObject or JSONArray）
	 */
	public JSON parse(final Object obj) {
		if (obj instanceof CharSequence) {
			return mapper.map((CharSequence) obj);
		}
		return mapper.map(obj);
	}
	// endregion

	// region ----- toBean

	/**
	 * 将JSON转换为指定类型的Bean对象
	 *
	 * @param json JSON
	 * @param type Bean类型，泛型对象使用{@link org.dromara.hutool.core.reflect.TypeReference}
	 * @param <T>  泛型类型
	 * @return Bean对象
	 */
	public <T> T toBean(final JSON json, final Type type) {
		return mapper.toBean(json, type);
	}
	// endregion


}
