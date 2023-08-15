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

package org.dromara.hutool.json;

import org.dromara.hutool.core.convert.Convert;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.convert.JSONConverter;
import org.dromara.hutool.json.serialize.GlobalSerializeMapping;
import org.dromara.hutool.json.serialize.JSONArraySerializer;
import org.dromara.hutool.json.serialize.JSONDeserializer;
import org.dromara.hutool.json.serialize.JSONObjectSerializer;
import org.dromara.hutool.json.writer.JSONValueWriter;
import org.dromara.hutool.json.writer.JSONWriter;
import org.dromara.hutool.json.xml.JSONXMLUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

/**
 * JSON工具类
 *
 * @author Looly
 */
public class JSONUtil {

	// -------------------------------------------------------------------- Pause start

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

	/**
	 * JSON字符串转JSONObject对象<br>
	 * 此方法会忽略空值，但是对JSON字符串不影响
	 *
	 * @param obj Bean对象或者Map
	 * @return JSONObject
	 */
	public static JSONObject parseObj(final Object obj) {
		return new JSONObject(obj);
	}

	/**
	 * JSON字符串转JSONObject对象<br>
	 * 此方法会忽略空值，但是对JSON字符串不影响
	 *
	 * @param obj    Bean对象或者Map
	 * @param config JSON配置
	 * @return JSONObject
	 * @since 5.3.1
	 */
	public static JSONObject parseObj(final Object obj, final JSONConfig config) {
		return new JSONObject(obj, config);
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
		return new JSONObject(obj, JSONConfig.of().setIgnoreNullValue(ignoreNullValue));
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
	public static Object parse(final Object obj) {
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
	public static Object parse(final Object obj, final JSONConfig config) {
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
	// -------------------------------------------------------------------- Parse end

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
	public static String toJsonPrettyStr(Object obj) {
		obj = parse(obj);
		if (obj instanceof JSON) {
			return ((JSON) obj).toStringPretty();
		}
		return StrUtil.toStringOrNull(obj);
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
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static String toJsonStr(final Object obj, final JSONConfig jsonConfig) {
		// 自定义规则，优先级高于全局规则
		final JSONValueWriter valueWriter = InternalJSONUtil.getValueWriter(obj);
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
	public static void toJsonStr(Object obj, final Writer writer) {
		if (null != obj) {
			obj = parse(obj);
			if (obj instanceof JSON) {
				((JSON) obj).write(writer);
			}

			// 普通值
			try {
				writer.write(obj.toString());
			} catch (final IOException e) {
				throw new IORuntimeException(e);
			}
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
		return toBean(json, (Type)clazz);
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
	 * @param json   JSONObject
	 * @param config JSON配置
	 * @param type   实体类对象类型
	 * @return 实体类对象
	 * @since 4.3.2
	 */
	public static <T> T toBean(Object json, final JSONConfig config, Type type) {
		if (null == json) {
			return null;
		}
		json = parse(json, config);
		if (json instanceof JSON) {
			if (type instanceof TypeReference) {
				type = ((TypeReference<?>) type).getType();
			}
			return ((JSON) json).toBean(type);
		}

		//issue#I7CW27，其他类型使用默认转换
		return Convert.convert(type, json);
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
		return JSONStrFormatter.format(jsonStr);
	}

	/**
	 * JSON对象是否为空，以下情况返回true<br>
	 * <ul>
	 *     <li>null</li>
	 *     <li>{@link JSONArray#isEmpty()}</li>
	 *     <li>{@link JSONObject#isEmpty()}</li>
	 * </ul>
	 *
	 * @param json JSONObject或JSONArray
	 * @return 是否为空
	 */
	public static boolean isEmpty(final JSON json) {
		if (null == json) {
			return true;
		}
		if (json instanceof JSONObject) {
			return ((JSONObject) json).isEmpty();
		} else if (json instanceof JSONArray) {
			return ((JSONArray) json).isEmpty();
		}
		return false;
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

	/**
	 * 加入自定义的序列化器
	 *
	 * @param type       对象类型
	 * @param serializer 序列化器实现
	 * @see GlobalSerializeMapping#putSerializer(Type, JSONObjectSerializer)
	 * @since 6.0.0
	 */
	public static void putSerializer(final Type type, final JSONObjectSerializer<?> serializer) {
		GlobalSerializeMapping.putSerializer(type, serializer);
	}

	/**
	 * 加入自定义的序列化器
	 *
	 * @param type       对象类型
	 * @param serializer 序列化器实现
	 * @see GlobalSerializeMapping#putSerializer(Type, JSONArraySerializer)
	 * @since 6.0.0
	 */
	public static void putSerializer(final Type type, final JSONArraySerializer<?> serializer) {
		GlobalSerializeMapping.putSerializer(type, serializer);
	}

	/**
	 * 加入自定义的反序列化器
	 *
	 * @param type         对象类型
	 * @param deserializer 反序列化器实现
	 * @see GlobalSerializeMapping#putDeserializer(Type, JSONDeserializer)
	 * @since 4.6.5
	 */
	public static void putDeserializer(final Type type, final JSONDeserializer<?> deserializer) {
		GlobalSerializeMapping.putDeserializer(type, deserializer);
	}
}
