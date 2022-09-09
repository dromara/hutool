package cn.hutool.json;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.reflect.TypeReference;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.convert.JSONConverter;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONArraySerializer;
import cn.hutool.json.serialize.JSONDeserializer;
import cn.hutool.json.serialize.JSONObjectSerializer;
import cn.hutool.json.xml.JSONXMLUtil;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
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
	public static JSON parse(final Object obj) {
		return JSONConverter.INSTANCE.toJSON(obj);
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
	 * @return JSON
	 * @since 5.3.1
	 */
	public static JSON parse(final Object obj, final JSONConfig config) {
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
		try (final Reader reader = FileUtil.getReader(file, charset)) {
			return parse(reader);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
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
		try (final Reader reader = FileUtil.getReader(file, charset)) {
			return parseObj(reader);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
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
		try (final Reader reader = FileUtil.getReader(file, charset)) {
			return parseArray(reader);
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}
	// -------------------------------------------------------------------- Read end

	// -------------------------------------------------------------------- toString start

	/**
	 * 转为JSON字符串
	 *
	 * @param json         JSON
	 * @param indentFactor 每一级别的缩进
	 * @return JSON字符串
	 */
	public static String toJsonStr(final JSON json, final int indentFactor) {
		if (null == json) {
			return null;
		}
		return json.toJSONString(indentFactor);
	}

	/**
	 * 转为JSON字符串
	 *
	 * @param json JSON
	 * @return JSON字符串
	 */
	public static String toJsonStr(final JSON json) {
		if (null == json) {
			return null;
		}
		return json.toJSONString(0);
	}

	/**
	 * 转为JSON字符串，并写出到write
	 *
	 * @param json   JSON
	 * @param writer Writer
	 * @since 5.3.3
	 */
	public static void toJsonStr(final JSON json, final Writer writer) {
		if (null != json) {
			json.write(writer);
		}
	}

	/**
	 * 转为JSON字符串
	 *
	 * @param json JSON
	 * @return JSON字符串
	 */
	public static String toJsonPrettyStr(final JSON json) {
		if (null == json) {
			return null;
		}
		return json.toStringPretty();
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
		if (null == obj) {
			return null;
		}
		if (obj instanceof CharSequence) {
			return StrUtil.str((CharSequence) obj);
		}

		if (obj instanceof Number) {
			return obj.toString();
		}
		return toJsonStr(parse(obj, jsonConfig));
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
			toJsonStr(parse(obj), writer);
		}
	}

	/**
	 * 转换为格式化后的JSON字符串
	 *
	 * @param obj Bean对象
	 * @return JSON字符串
	 */
	public static String toJsonPrettyStr(final Object obj) {
		return toJsonPrettyStr(parse(obj));
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
	 * JSON字符串转为实体类对象，转换异常将被抛出
	 *
	 * @param <T>        Bean类型
	 * @param jsonString JSON字符串
	 * @param beanClass  实体类对象
	 * @return 实体类对象
	 * @since 3.1.2
	 */
	public static <T> T toBean(final String jsonString, final Class<T> beanClass) {
		return toBean(parse(jsonString), beanClass);
	}

	/**
	 * JSON字符串转为实体类对象，转换异常将被抛出<br>
	 * 通过{@link JSONConfig}可选是否忽略大小写、忽略null等配置
	 *
	 * @param <T>        Bean类型
	 * @param jsonString JSON字符串
	 * @param config     JSON配置
	 * @param beanClass  实体类对象
	 * @return 实体类对象
	 * @since 5.8.0
	 */
	public static <T> T toBean(final String jsonString, final JSONConfig config, final Class<T> beanClass) {
		return toBean(parse(jsonString, config), beanClass);
	}

	/**
	 * JSON字符串转为实体类对象，转换异常将被抛出<br>
	 * 通过{@link JSONConfig}可选是否忽略大小写、忽略null等配置
	 *
	 * @param <T>        Bean类型
	 * @param jsonString JSON字符串
	 * @param config     JSON配置
	 * @param type       Bean类型
	 * @return 实体类对象
	 */
	public static <T> T toBean(final String jsonString, final JSONConfig config, final Type type) {
		return toBean(parse(jsonString, config), type);
	}

	/**
	 * 转为实体类对象，转换异常将被抛出
	 *
	 * @param <T>       Bean类型
	 * @param json      JSONObject
	 * @param beanClass 实体类对象
	 * @return 实体类对象
	 */
	public static <T> T toBean(final JSONObject json, final Class<T> beanClass) {
		return null == json ? null : json.toBean(beanClass);
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
	public static <T> T toBean(final JSON json, final TypeReference<T> typeReference) {
		return toBean(json, typeReference.getType());
	}

	/**
	 * 转为实体类对象
	 *
	 * @param <T>      Bean类型
	 * @param json     JSONObject
	 * @param beanType 实体类对象类型
	 * @return 实体类对象
	 * @since 4.3.2
	 */
	public static <T> T toBean(final JSON json, final Type beanType) {
		if (null == json) {
			return null;
		}
		return json.toBean(beanType);
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
	public static boolean isEmpty(final JSON json){
		if(null == json){
			return true;
		}
		if(json instanceof JSONObject){
			return ((JSONObject) json).isEmpty();
		} else if(json instanceof JSONArray){
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
