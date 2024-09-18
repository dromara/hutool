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

package org.dromara.hutool.json;

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.convert.JSONConverter;
import org.dromara.hutool.json.mapper.JSONObjectMapper;
import org.dromara.hutool.json.writer.JSONWriter;
import org.dromara.hutool.json.writer.ValueWriter;
import org.dromara.hutool.json.writer.ValueWriterManager;
import org.dromara.hutool.json.xml.JSONXMLUtil;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.function.Predicate;

/**
 * JSON工具类
 *
 * @author Looly
 */
public class JSONUtil {

	// region ----- of
	/**
	 * 创建JSONObject
	 *
	 * @return JSONObject
	 */
	public static JSONObject ofObj() {
		return new JSONObject();
	}

	/**
	 * 创建JSONObject
	 *
	 * @param config JSON配置
	 * @return JSONObject
	 * @since 5.2.5
	 */
	public static JSONObject ofObj(final JSONConfig config) {
		return new JSONObject(config);
	}

	/**
	 * 创建 JSONArray
	 *
	 * @return JSONArray
	 */
	public static JSONArray ofArray() {
		return new JSONArray();
	}

	/**
	 * 创建 JSONArray
	 *
	 * @param config JSON配置
	 * @return JSONArray
	 * @since 5.2.5
	 */
	public static JSONArray ofArray(final JSONConfig config) {
		return new JSONArray(config);
	}
	// endregion

	// region ----- parse
	/**
	 * JSON字符串转JSONObject对象<br>
	 * 此方法会忽略空值，但是对JSON字符串不影响
	 *
	 * @param obj Bean对象或者Map
	 * @return JSONObject
	 */
	public static JSONObject parseObj(final Object obj) {
		return parseObj(obj, JSONConfig.of(), null);
	}

	/**
	 * JSON字符串转JSONObject对象
	 *
	 * @param obj             Bean对象或者Map
	 * @param ignoreNullValue 是否忽略空值，如果source为JSON字符串，不忽略空值
	 * @return JSONObject
	 * @since 3.0.9
	 */
	public static JSONObject parseObj(final Object obj, final boolean ignoreNullValue) {
		return parseObj(obj, JSONConfig.of().setIgnoreNullValue(ignoreNullValue));
	}

	/**
	 * JSON字符串转JSONObject对象<br>
	 * 此方法会忽略空值，但是对JSON字符串不影响
	 *
	 * @param obj    Bean对象或者Map
	 * @param config JSON配置
	 * @return JSONObject
	 */
	public static JSONObject parseObj(final Object obj, final JSONConfig config) {
		return parseObj(obj, config, null);
	}

	/**
	 * JSON字符串转JSONObject对象<br>
	 * 此方法会忽略空值，但是对JSON字符串不影响
	 *
	 * @param obj       Bean对象或者Map
	 * @param config    JSON配置
	 * @param predicate 键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@link Predicate#test(Object)}为{@code true}保留
	 * @return JSONObject
	 */
	public static JSONObject parseObj(final Object obj, final JSONConfig config, final Predicate<MutableEntry<Object, Object>> predicate) {
		final JSONObject jsonObject = new JSONObject(config);
		JSONObjectMapper.of(obj, predicate).mapTo(jsonObject);
		return jsonObject;
	}

	/**
	 * JSON字符串转JSONArray
	 *
	 * @param arrayOrCollection 数组或集合对象
	 * @return JSONArray
	 * @since 3.0.8
	 */
	public static JSONArray parseArray(final Object arrayOrCollection) {
		return new JSONArray(arrayOrCollection);
	}

	/**
	 * JSON字符串转JSONArray
	 *
	 * @param arrayOrCollection 数组或集合对象
	 * @param config            JSON配置
	 * @return JSONArray
	 * @since 5.3.1
	 */
	public static JSONArray parseArray(final Object arrayOrCollection, final JSONConfig config) {
		return new JSONArray(arrayOrCollection, config);
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
	 * @return JSON
	 */
	public static JSON parse(final Object obj) {
		return parse(obj, null);
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
	 * @param obj    对象
	 * @param config JSON配置，{@code null}使用默认配置
	 * @return JSON（JSONObject or JSONArray）
	 */
	public static JSON parse(final Object obj, final JSONConfig config) {
		if (null == config) {
			return JSONConverter.INSTANCE.toJSON(obj);
		}
		return JSONConverter.of(config).toJSON(obj);
	}

	/**
	 * XML字符串转为JSONObject
	 *
	 * @param xmlStr XML字符串
	 * @return JSONObject
	 */
	public static JSONObject parseFromXml(final String xmlStr) {
		return JSONXMLUtil.toJSONObject(xmlStr);
	}
	// endregion

	// -------------------------------------------------------------------- Read start

	/**
	 * 读取JSON
	 *
	 * @param file    JSON文件
	 * @param charset 编码
	 * @return JSON（包括JSONObject和JSONArray）
	 * @throws IORuntimeException IO异常
	 */
	public static JSON readJSON(final File file, final Charset charset) throws IORuntimeException {
		return (JSON) FileUtil.read(file, charset, JSONUtil::parse);
	}

	/**
	 * 读取JSONObject
	 *
	 * @param file    JSON文件
	 * @param charset 编码
	 * @return JSONObject
	 * @throws IORuntimeException IO异常
	 */
	public static JSONObject readJSONObject(final File file, final Charset charset) throws IORuntimeException {
		return FileUtil.read(file, charset, JSONUtil::parseObj);
	}

	/**
	 * 读取JSONArray
	 *
	 * @param file    JSON文件
	 * @param charset 编码
	 * @return JSONArray
	 * @throws IORuntimeException IO异常
	 */
	public static JSONArray readJSONArray(final File file, final Charset charset) throws IORuntimeException {
		return FileUtil.read(file, charset, JSONUtil::parseArray);
	}
	// -------------------------------------------------------------------- Read end

	// -------------------------------------------------------------------- toString start

	/**
	 * 转换为格式化后的JSON字符串
	 *
	 * @param obj Bean对象
	 * @return JSON字符串
	 */
	public static String toJsonPrettyStr(final Object obj) {
		return parse(obj).toStringPretty();
	}

	/**
	 * 转换为JSON字符串
	 *
	 * @param obj 被转为JSON的对象
	 * @return JSON字符串
	 */
	public static String toJsonStr(final Object obj) {
		return toJsonStr(obj, (JSONConfig) null);
	}

	/**
	 * 转换为JSON字符串
	 *
	 * @param obj        被转为JSON的对象
	 * @param jsonConfig JSON配置
	 * @return JSON字符串
	 * @since 5.7.12
	 */
	public static String toJsonStr(final Object obj, final JSONConfig jsonConfig) {
		// 自定义规则，优先级高于全局规则
		final ValueWriter valueWriter = ValueWriterManager.getInstance().get(obj);
		if (null != valueWriter) {
			final StringWriter stringWriter = new StringWriter();
			final JSONWriter jsonWriter = JSONWriter.of(stringWriter, 0, 0, null);
			// 用户对象自定义实现了JSONValueWriter接口，理解为需要自定义输出
			valueWriter.write(jsonWriter, obj);
			return stringWriter.toString();
		}

		if (null == obj) {
			return null;
		}
		return parse(obj, jsonConfig).toString();
	}

	/**
	 * 转换为JSON字符串并写出到writer
	 *
	 * @param obj    被转为JSON的对象
	 * @param writer Writer
	 * @since 5.3.3
	 */
	public static void toJsonStr(final Object obj, final Writer writer) {
		if (null != obj) {
			parse(obj).write(JSONWriter.of(writer, 0, 0, null));
		}
	}

	/**
	 * 转换为XML字符串
	 *
	 * @param json JSON
	 * @return XML字符串
	 */
	public static String toXmlStr(final JSON json) {
		return JSONXMLUtil.toXml(json);
	}

	/**
	 * XML转JSONObject<br>
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 *
	 * @param xml XML字符串
	 * @return JSONObject
	 * @since 4.0.8
	 */
	public static JSONObject xmlToJson(final String xml) {
		return JSONXMLUtil.toJSONObject(xml);
	}
	// -------------------------------------------------------------------- toString end

	// -------------------------------------------------------------------- toBean start

	/**
	 * 转为实体类对象
	 *
	 * @param <T>   Bean类型
	 * @param json  JSONObject
	 * @param clazz 实体类
	 * @return 实体类对象
	 * @since 4.6.2
	 */
	public static <T> T toBean(final Object json, final Class<T> clazz) {
		Assert.notNull(clazz);
		return toBean(json, (Type) clazz);
	}

	/**
	 * 转为实体类对象
	 *
	 * @param <T>           Bean类型
	 * @param json          JSONObject
	 * @param typeReference {@link TypeReference}类型参考子类，可以获取其泛型参数中的Type类型
	 * @return 实体类对象
	 * @since 4.6.2
	 */
	public static <T> T toBean(final Object json, final TypeReference<T> typeReference) {
		Assert.notNull(typeReference);
		return toBean(json, typeReference.getType());
	}

	/**
	 * 转为实体类对象
	 *
	 * @param <T>  Bean类型
	 * @param json JSONObject
	 * @param type 实体类对象类型
	 * @return 实体类对象
	 * @since 4.3.2
	 */
	public static <T> T toBean(final Object json, final Type type) {
		return toBean(json, null, type);
	}

	/**
	 * 转为实体类对象
	 *
	 * @param <T>    Bean类型
	 * @param obj    JSONObject
	 * @param config JSON配置
	 * @param type   实体类对象类型
	 * @return 实体类对象
	 * @since 4.3.2
	 */
	public static <T> T toBean(final Object obj, final JSONConfig config, Type type) {
		if (null == obj) {
			return null;
		}
		final JSON json = parse(obj, config);
		if (type instanceof TypeReference) {
			type = ((TypeReference<?>) type).getType();
		}
		return json.toBean(type);
	}
	// -------------------------------------------------------------------- toBean end

	/**
	 * 将JSONArray字符串转换为Bean的List，默认为ArrayList
	 *
	 * @param <T>         Bean类型
	 * @param jsonArray   JSONArray字符串
	 * @param elementType List中元素类型
	 * @return List
	 * @since 5.5.2
	 */
	public static <T> List<T> toList(final String jsonArray, final Class<T> elementType) {
		return toList(parseArray(jsonArray), elementType);
	}

	/**
	 * 将JSONArray转换为Bean的List，默认为ArrayList
	 *
	 * @param <T>         Bean类型
	 * @param jsonArray   {@link JSONArray}
	 * @param elementType List中元素类型
	 * @return List
	 * @since 4.0.7
	 */
	public static <T> List<T> toList(final JSONArray jsonArray, final Class<T> elementType) {
		return null == jsonArray ? null : jsonArray.toList(elementType);
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 *
	 * @param json       {@link JSON}
	 * @param expression 表达式
	 * @return 对象
	 * @see JSON#getByPath(String)
	 */
	public static Object getByPath(final JSON json, final String expression) {
		return getByPath(json, expression, null);
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 *
	 * @param <T>          值类型
	 * @param json         {@link JSON}
	 * @param expression   表达式
	 * @param defaultValue 默认值
	 * @return 对象
	 * @see JSON#getByPath(String)
	 * @since 5.6.0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getByPath(final JSON json, final String expression, final T defaultValue) {
		if ((null == json || StrUtil.isBlank(expression))) {
			return defaultValue;
		}

		if (null != defaultValue) {
			final Class<T> type = (Class<T>) defaultValue.getClass();
			return ObjUtil.defaultIfNull(json.getByPath(expression, type), defaultValue);
		}
		return (T) json.getByPath(expression);
	}

	/**
	 * 格式化JSON字符串，此方法并不严格检查JSON的格式正确与否
	 *
	 * @param jsonStr JSON字符串
	 * @return 格式化后的字符串
	 * @since 3.1.2
	 */
	public static String formatJsonStr(final String jsonStr) {
		return JSONStrFormatter.INSTANCE.format(jsonStr);
	}

	/**
	 * JSON对象是否为空，以下情况返回true<br>
	 * <ul>
	 *     <li>null</li>
	 *     <li>{@link JSON#isEmpty()}</li>
	 * </ul>
	 *
	 * @param json JSONObject或JSONArray
	 * @return 是否为空
	 */
	public static boolean isEmpty(final JSON json) {
		if (null == json) {
			return true;
		}
		return json.isEmpty();
	}

	/**
	 * 是否为JSON类型字符串，首尾都为大括号或中括号判定为JSON字符串
	 *
	 * @param str 字符串
	 * @return 是否为JSON类型字符串
	 * @since 5.7.22
	 */
	public static boolean isTypeJSON(final String str) {
		return isTypeJSONObject(str) || isTypeJSONArray(str);
	}

	/**
	 * 是否为JSONObject类型字符串，首尾都为大括号判定为JSONObject字符串
	 *
	 * @param str 字符串
	 * @return 是否为JSON字符串
	 * @since 5.7.22
	 */
	public static boolean isTypeJSONObject(final String str) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		return StrUtil.isWrap(StrUtil.trim(str), '{', '}');
	}

	/**
	 * 是否为JSONArray类型的字符串，首尾都为中括号判定为JSONArray字符串
	 *
	 * @param str 字符串
	 * @return 是否为JSONArray类型字符串
	 * @since 5.7.22
	 */
	public static boolean isTypeJSONArray(final String str) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		return StrUtil.isWrap(StrUtil.trim(str), '[', ']');
	}
}
