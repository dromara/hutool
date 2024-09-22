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

package org.dromara.hutool.json.mapper;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.MapWrapper;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.*;
import org.dromara.hutool.json.reader.JSONParser;
import org.dromara.hutool.json.reader.JSONTokener;
import org.dromara.hutool.json.serializer.JSONSerializer;
import org.dromara.hutool.json.serializer.SimpleJSONContext;
import org.dromara.hutool.json.serializer.TypeAdapterManager;
import org.dromara.hutool.json.xml.JSONXMLParser;
import org.dromara.hutool.json.xml.ParseConfig;

import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 对象和JSON值映射器，用于转换对象为JSON对象中的值<br>
 * 有效的JSON值包括：
 * <ul>
 *     <li>JSONObject</li>
 *     <li>JSONArray</li>
 *     <li>String</li>
 *     <li>数字（int、long等）</li>
 *     <li>Boolean值，如true或false</li>
 *     <li>{@code null}</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONValueMapper implements Serializable {

	private static final long serialVersionUID = -6714488573738940582L;

	/**
	 * 创建ObjectMapper
	 *
	 * @param jsonConfig 来源对象
	 * @param predicate  键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@link Predicate#test(Object)}为{@code true}保留
	 * @return ObjectMapper
	 */
	public static JSONValueMapper of(final JSONConfig jsonConfig, final Predicate<MutableEntry<Object, Object>> predicate) {
		return new JSONValueMapper(jsonConfig, predicate);
	}

	private final JSONConfig jsonConfig;
	private final Predicate<MutableEntry<Object, Object>> predicate;

	/**
	 * 构造
	 *
	 * @param jsonConfig JSON配置
	 * @param predicate  键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public JSONValueMapper(final JSONConfig jsonConfig, final Predicate<MutableEntry<Object, Object>> predicate) {
		this.jsonConfig = ObjUtil.defaultIfNull(jsonConfig, JSONConfig::of);
		this.predicate = predicate;
	}

	/**
	 * 将JSONObject转换为JSONArray
	 *
	 * @param jsonObject JSONObject
	 * @return JSONArray
	 */
	public JSONArray mapFromJSONObject(final JSONObject jsonObject){
		final JSONArray array = new JSONArray(jsonConfig);
		JSONArrayMapper.of(jsonObject, this.predicate)
			.mapTo(array);
		return array;
	}

	/**
	 * 解析JSON字符串或XML字符串为JSON结构
	 *
	 * @param source JSON字符串或XML字符串
	 * @return JSON对象
	 */
	public JSON map(final CharSequence source) {
		final String jsonStr = StrUtil.trim(source);
		if (StrUtil.startWith(jsonStr, '<')) {
			// 可能为XML
			final JSONObject jsonObject = new JSONObject(jsonConfig);
			JSONXMLParser.of(ParseConfig.of(), this.predicate).parseJSONObject(jsonStr, jsonObject);
			return jsonObject;
		}

		return mapFromTokener(new JSONTokener(source));
	}

	/**
	 * 在需要的时候转换映射对象<br>
	 * 包装包括：
	 * <ul>
	 * <li>array or collection =》 JSONArray</li>
	 * <li>map =》 JSONObject</li>
	 * <li>standard property (Double, String, et al) =》 原对象</li>
	 * <li>来自于java包 =》 字符串</li>
	 * <li>其它 =》 尝试包装为JSONObject，否则返回{@code null}</li>
	 * </ul>
	 *
	 * @param obj 被映射的对象
	 * @return 映射后的值，null表示此值需被忽略
	 */
	public JSON map(Object obj) {
		if (null == obj) {
			return null;
		}

		if (obj instanceof Optional) {
			obj = ((Optional<?>) obj).orElse(null);
		} else if (obj instanceof Opt) {
			obj = ((Opt<?>) obj).getOrNull();
		}

		if (obj instanceof JSON) {
			return (JSON) obj;
		}

		// 读取JSON流
		if(obj instanceof JSONTokener){
			return mapFromTokener((JSONTokener) obj);
		}else if(obj instanceof JSONParser){
			return ((JSONParser)obj).parse();
		} else if (obj instanceof Reader) {
			return mapFromTokener(new JSONTokener((Reader) obj));
		} else if (obj instanceof InputStream) {
			return mapFromTokener(new JSONTokener((InputStream) obj));
		}

		// 自定义序列化
		final JSONSerializer<Object> serializer = TypeAdapterManager.getInstance()
			.getSerializer(obj, obj.getClass());
		if (null != serializer) {
			return serializer.serialize(obj, new SimpleJSONContext(null, this.jsonConfig));
		}

		// 原始类型
		if (JSONPrimitive.isTypeForJSONPrimitive(obj)) {
			return new JSONPrimitive(obj, jsonConfig);
		}

		// 特定对象转换
		try {
			// JSONArray
			if (// MapWrapper实现了Iterable会被当作JSONArray，此处做修正
				!(obj instanceof MapWrapper) &&
					(obj instanceof Iterable || ArrayUtil.isArray(obj))) {
				final JSONArray jsonArray = new JSONArray(jsonConfig);
				JSONArrayMapper.of(obj, predicate).mapTo(jsonArray);
				return jsonArray;
			}

			// 默认按照JSONObject对待
			final JSONObject jsonObject = new JSONObject(jsonConfig);
			JSONObjectMapper.of(obj, predicate).mapTo(jsonObject);
			return jsonObject;
		} catch (final Exception exception) {
			if (jsonConfig.isIgnoreError()) {
				return null;
			}
			throw exception instanceof JSONException ? (JSONException) exception : new JSONException(exception);
		}
	}

	/**
	 * 从{@link JSONTokener} 中读取JSON字符串，并转换为JSON
	 *
	 * @param tokener {@link JSONTokener}
	 * @return JSON
	 */
	private JSON mapFromTokener(final JSONTokener tokener) {
		return JSONParser.of(tokener, jsonConfig).setPredicate(this.predicate).parse();
	}
}
