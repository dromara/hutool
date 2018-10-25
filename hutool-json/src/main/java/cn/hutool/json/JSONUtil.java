package cn.hutool.json;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * JSON工具类
 * 
 * @author Looly
 *
 */
public final class JSONUtil {

	// -------------------------------------------------------------------- Pause start
	/**
	 * 创建JSONObject
	 * 
	 * @return JSONObject
	 */
	public static JSONObject createObj() {
		return new JSONObject();
	}

	/**
	 * 创建 JSONArray
	 * 
	 * @return JSONArray
	 */
	public static JSONArray createArray() {
		return new JSONArray();
	}

	/**
	 * JSON字符串转JSONObject对象
	 * 
	 * @param jsonStr JSON字符串
	 * @return JSONObject
	 */
	public static JSONObject parseObj(String jsonStr) {
		return new JSONObject(jsonStr);
	}

	/**
	 * JSON字符串转JSONObject对象<br>
	 * 此方法会忽略空值，但是对JSON字符串不影响
	 * 
	 * @param obj Bean对象或者Map
	 * @return JSONObject
	 */
	public static JSONObject parseObj(Object obj) {
		return new JSONObject(obj);
	}

	/**
	 * JSON字符串转JSONObject对象
	 * 
	 * @param obj Bean对象或者Map
	 * @param ignoreNullValue 是否忽略空值，如果source为JSON字符串，不忽略空值
	 * @return JSONObject
	 * @since 3.0.9
	 */
	public static JSONObject parseObj(Object obj, boolean ignoreNullValue) {
		return new JSONObject(obj, ignoreNullValue);
	}

	/**
	 * JSON字符串转JSONArray
	 * 
	 * @param jsonStr JSON字符串
	 * @return JSONArray
	 */
	public static JSONArray parseArray(String jsonStr) {
		return new JSONArray(jsonStr);
	}

	/**
	 * JSON字符串转JSONArray
	 * 
	 * @param arrayOrCollection 数组或集合对象
	 * @return JSONArray
	 * @since 3.0.8
	 */
	public static JSONArray parseArray(Object arrayOrCollection) {
		return new JSONArray(arrayOrCollection);
	}

	/**
	 * JSON字符串转JSONArray
	 * 
	 * @param arrayOrCollection 数组或集合对象
	 * @param ignoreNullValue 是否忽略空值
	 * @return JSONArray
	 * @since 3.2.3
	 */
	public static JSONArray parseArray(Object arrayOrCollection, boolean ignoreNullValue) {
		return new JSONArray(arrayOrCollection, ignoreNullValue);
	}

	/**
	 * 转换对象为JSON<br>
	 * 支持的对象：<br>
	 * String: 转换为相应的对象<br>
	 * Array Collection：转换为JSONArray<br>
	 * Bean对象：转为JSONObject
	 * 
	 * @param obj 对象
	 * @return JSON
	 */
	public static JSON parse(Object obj) {
		if (null == obj) {
			return null;
		}

		JSON json = null;
		if (obj instanceof JSON) {
			json = (JSON) obj;
		} else if (obj instanceof String) {
			String jsonStr = ((String) obj).trim();
			if (jsonStr.startsWith("[")) {
				json = parseArray(jsonStr);
			} else {
				json = parseObj(jsonStr);
			}
		} else if (obj instanceof Collection || obj.getClass().isArray()) {// 列表
			json = new JSONArray(obj);
		} else {// 对象
			json = new JSONObject(obj);
		}

		return json;
	}

	/**
	 * XML字符串转为JSONObject
	 * 
	 * @param xmlStr XML字符串
	 * @return JSONObject
	 */
	public static JSONObject parseFromXml(String xmlStr) {
		return XML.toJSONObject(xmlStr);
	}

	/**
	 * Map转化为JSONObject
	 * 
	 * @param map {@link Map}
	 * @return JSONObject
	 */
	public static JSONObject parseFromMap(Map<?, ?> map) {
		return new JSONObject(map);
	}

	/**
	 * ResourceBundle转化为JSONObject
	 * 
	 * @param bundle ResourceBundle文件
	 * @return JSONObject
	 */
	public static JSONObject parseFromResourceBundle(ResourceBundle bundle) {
		JSONObject jsonObject = new JSONObject();
		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if (key != null) {
				InternalJSONUtil.propertyPut(jsonObject, key, bundle.getString(key));
			}
		}
		return jsonObject;
	}
	// -------------------------------------------------------------------- Pause end

	// -------------------------------------------------------------------- Read start
	/**
	 * 读取JSON
	 * 
	 * @param file JSON文件
	 * @param charset 编码
	 * @return JSON（包括JSONObject和JSONArray）
	 * @throws IORuntimeException IO异常
	 */
	public static JSON readJSON(File file, Charset charset) throws IORuntimeException {
		return parse(FileReader.create(file, charset).readString());
	}

	/**
	 * 读取JSONObject
	 * 
	 * @param file JSON文件
	 * @param charset 编码
	 * @return JSONObject
	 * @throws IORuntimeException IO异常
	 */
	public static JSONObject readJSONObject(File file, Charset charset) throws IORuntimeException {
		return parseObj(FileReader.create(file, charset).readString());
	}

	/**
	 * 读取JSONArray
	 * 
	 * @param file JSON文件
	 * @param charset 编码
	 * @return JSONArray
	 * @throws IORuntimeException IO异常
	 */
	public static JSONArray readJSONArray(File file, Charset charset) throws IORuntimeException {
		return parseArray(FileReader.create(file, charset).readString());
	}
	// -------------------------------------------------------------------- Read end

	// -------------------------------------------------------------------- toString start
	/**
	 * 转为JSON字符串
	 * 
	 * @param json JSON
	 * @param indentFactor 每一级别的缩进
	 * @return JSON字符串
	 */
	public static String toJsonStr(JSON json, int indentFactor) {
		return json.toJSONString(indentFactor);
	}

	/**
	 * 转为JSON字符串
	 * 
	 * @param json JSON
	 * @return JSON字符串
	 */
	public static String toJsonStr(JSON json) {
		return json.toJSONString(0);
	}

	/**
	 * 转为JSON字符串
	 * 
	 * @param json JSON
	 * @return JSON字符串
	 */
	public static String toJsonPrettyStr(JSON json) {
		return json.toJSONString(4);
	}

	/**
	 * 转换为JSON字符串
	 * 
	 * @param obj 被转为JSON的对象
	 * @return JSON字符串
	 */
	public static String toJsonStr(Object obj) {
		if (obj instanceof String) {
			return (String) obj;
		}
		return toJsonStr(parse(obj));
	}

	/**
	 * 转换为格式化后的JSON字符串
	 * 
	 * @param obj Bean对象
	 * @return JSON字符串
	 */
	public static String toJsonPrettyStr(Object obj) {
		return toJsonPrettyStr(parse(obj));
	}

	/**
	 * 转换为XML字符串
	 * 
	 * @param json JSON
	 * @return XML字符串
	 */
	public static String toXmlStr(JSON json) {
		return XML.toXml(json);
	}
	// -------------------------------------------------------------------- toString end

	// -------------------------------------------------------------------- toBean start

	/**
	 * JSON字符串转为实体类对象，转换异常将被抛出
	 * 
	 * @param <T> Bean类型
	 * @param jsonString JSON字符串
	 * @param beanClass 实体类对象
	 * @return 实体类对象
	 * @since 3.1.2
	 */
	public static <T> T toBean(String jsonString, Class<T> beanClass) {
		return toBean(parseObj(jsonString), beanClass, false);
	}

	/**
	 * 转为实体类对象，转换异常将被抛出
	 * 
	 * @param <T> Bean类型
	 * @param json JSONObject
	 * @param beanClass 实体类对象
	 * @return 实体类对象
	 */
	public static <T> T toBean(JSONObject json, Class<T> beanClass) {
		return toBean(json, beanClass, false);
	}

	/**
	 * 转为实体类对象
	 * 
	 * @param <T> Bean类型
	 * @param json JSONObject
	 * @param beanClass 实体类对象
	 * @param ignoreError 是否忽略转换过程中某个字段的转换异常
	 * @return 实体类对象
	 */
	public static <T> T toBean(JSONObject json, Class<T> beanClass, boolean ignoreError) {
		return null == json ? null : json.toBean(beanClass, ignoreError);
	}
	// -------------------------------------------------------------------- toBean end
	
	/**
	 * 将JSONArray转换为Bean的List，默认为ArrayList
	 * 
	 * @param jsonArray JSONArray
	 * @param elementType List中元素类型
	 * @return List
	 * @since 4.0.7
	 */
	public static <T> List<T> toList(JSONArray jsonArray, Class<T> elementType){
		return null == jsonArray ? null : jsonArray.toList(elementType);
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * 
	 * 表达式栗子：
	 * 
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * 
	 * @param json {@link JSON}
	 * @param expression 表达式
	 * @return 对象
	 * @see JSON#getByExp(String)
	 * @deprecated 请使用{@link JSONUtil#getByPath(JSON, String)}
	 */
	@Deprecated
	public static Object getByExp(JSON json, String expression) {
		return (null == json || StrUtil.isBlank(expression)) ? null : json.getByPath(expression);
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * 
	 * 表达式栗子：
	 * 
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * 
	 * @param json {@link JSON}
	 * @param expression 表达式
	 * @return 对象
	 * @see JSON#getByExp(String)
	 */
	public static Object getByPath(JSON json, String expression) {
		return (null == json || StrUtil.isBlank(expression)) ? null : json.getByPath(expression);
	}

	/**
	 * 设置表达式指定位置（或filed对应）的值<br>
	 * 若表达式指向一个JSONArray则设置其坐标对应位置的值，若指向JSONObject则put对应key的值<br>
	 * 注意：如果为JSONArray，则设置值得下标不能大于已有JSONArray的长度<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * 
	 * 表达式栗子：
	 * 
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * 
	 * @param json JSON，可以为JSONObject或JSONArray
	 * @param expression 表达式
	 * @param value 值
	 */
	public static void putByPath(JSON json, String expression, Object value) {
		json.putByPath(expression, value);
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param string 字符串
	 * @return 适合在JSON中显示的字符串
	 */
	public static String quote(String string) {
		return quote(string, true);
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param string 字符串
	 * @param isWrap 是否使用双引号包装字符串
	 * @return 适合在JSON中显示的字符串
	 * @since 3.3.1
	 */
	public static String quote(String string, boolean isWrap) {
		StringWriter sw = new StringWriter();
		try {
			return quote(string, sw, isWrap).toString();
		} catch (IOException ignored) {
			// will never happen - we are writing to a string writer
			return StrUtil.EMPTY;
		}
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 * 
	 * @param str 字符串
	 * @param writer Writer
	 * @return Writer
	 * @throws IOException IO异常
	 */
	public static Writer quote(String str, Writer writer) throws IOException {
		return quote(str, writer, true);
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 * 
	 * @param str 字符串
	 * @param writer Writer
	 * @param isWrap 是否使用双引号包装字符串
	 * @return Writer
	 * @throws IOException IO异常
	 * @since 3.3.1
	 */
	public static Writer quote(String str, Writer writer, boolean isWrap) throws IOException {
		if (StrUtil.isEmpty(str)) {
			if (isWrap) {
				writer.write("\"\"");
			}
			return writer;
		}

		char b; // 前一个字符
		char c = 0; // 当前字符
		int len = str.length();
		if (isWrap) {
			writer.write('"');
		}
		for (int i = 0; i < len; i++) {
			b = c;
			c = str.charAt(i);
			switch (c) {
			case '\\':
			case '"':
				writer.write("\\");
				writer.write(c);
				break;
			case '/':
				if (b == '<') {
					writer.write('\\');
				}
				writer.write(c);
				break;
			case '\b':
				writer.write("\\b");
				break;
			case '\t':
				writer.write("\\t");
				break;
			case '\n':
				writer.write("\\n");
				break;
			case '\f':
				writer.write("\\f");
				break;
			case '\r':
				writer.write("\\r");
				break;
			default:
				if (c < StrUtil.C_SPACE || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
					writer.write(HexUtil.toUnicodeHex(c));
				} else {
					writer.write(c);
				}
			}
		}
		if (isWrap) {
			writer.write('"');
		}
		return writer;
	}

	/**
	 * 转义显示不可见字符
	 * 
	 * @param str 字符串
	 * @return 转义后的字符串
	 */
	public static String escape(String str) {
		if (StrUtil.isEmpty(str)) {
			return str;
		}

		final int len = str.length();
		final StringBuilder builder = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = str.charAt(i);
			switch (c) {
			case '\b':
				builder.append("\\b");
				break;
			case '\t':
				builder.append("\\t");
				break;
			case '\n':
				builder.append("\\n");
				break;
			case '\f':
				builder.append("\\f");
				break;
			case '\r':
				builder.append("\\r");
				break;
			default:
				//无法显示字符转为Unicode符：https://en.wikibooks.org/wiki/Unicode/Character_reference/0000-0FFF
				if (c < StrUtil.C_SPACE || //
						(c >= '\u0080' && c <= '\u00a0') || //
						(c >= '\u2000' && c <= '\u2010') ||//
						(c >= '\u2028' && c <= '\u202F') ||//
						(c >= '\u2066' && c <= '\u206F')//
				) {
					builder.append(HexUtil.toUnicodeHex(c));
				} else {
					builder.append(c);
				}
			}
		}
		return builder.toString();
	}

	/**
	 * 在需要的时候包装对象<br>
	 * 包装包括：
	 * <ul>
	 * <li><code>null</code> =》 <code>JSONNull.NULL</code></li>
	 * <li>array or collection =》 JSONArray</li>
	 * <li>map =》 JSONObject</li>
	 * <li>standard property (Double, String, et al) =》 原对象</li>
	 * <li>来自于java包 =》 字符串</li>
	 * <li>其它 =》 尝试包装为JSONObject，否则返回<code>null</code></li>
	 * </ul>
	 * 
	 * @param object 被包装的对象
	 * @param ignoreNullValue 是否忽略{@code null} 值
	 * @return 包装后的值，null表示此值需被忽略
	 */
	public static Object wrap(Object object, boolean ignoreNullValue) {
		if (object == null) {
			return ignoreNullValue ? null : JSONNull.NULL;
		}
		if (object instanceof JSON //
				|| JSONNull.NULL.equals(object) //
				|| object instanceof JSONString //
				|| object instanceof CharSequence //
				|| object instanceof Number //
				|| ObjectUtil.isBasicType(object) //
		) {
			return object;
		}

		try {
			// JSONArray
			if (object instanceof Iterable || ArrayUtil.isArray(object)) {
				return new JSONArray(object, ignoreNullValue);
			}
			// JSONObject
			if (object instanceof Map) {
				return new JSONObject(object, ignoreNullValue);
			}

			// 日期类型原样保存，便于格式化
			if (object instanceof Date || object instanceof Calendar) {
				return object;
			}
			if (object instanceof Calendar) {
				return ((Calendar) object).getTimeInMillis();
			}
			// 枚举类保存其字符串形式（4.0.2新增）
			if (object instanceof Enum) {
				return object.toString();
			}

			// Java内部类不做转换
			final Class<?> objectClass = object.getClass();
			final Package objectPackage = objectClass.getPackage();
			final String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
			if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || objectClass.getClassLoader() == null) {
				return object.toString();
			}

			// 默认按照JSONObject对待
			return new JSONObject(object, ignoreNullValue);
		} catch (Exception exception) {
			return null;
		}
	}

	/**
	 * 格式化JSON字符串，此方法并不严格检查JSON的格式正确与否
	 * 
	 * @param jsonStr JSON字符串
	 * @return 格式化后的字符串
	 * @since 3.1.2
	 */
	public static String formatJsonStr(String jsonStr) {
		return JSONStrFormater.format(jsonStr);
	}

	/**
	 * 是否为JSON字符串，首尾都为大括号或中括号判定为JSON字符串
	 * 
	 * @param str 字符串
	 * @return 是否为JSON字符串
	 * @since 3.3.0
	 */
	public static boolean isJson(String str) {
		return isJsonObj(str) || isJsonArray(str);
	}

	/**
	 * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
	 * 
	 * @param str 字符串
	 * @return 是否为JSON字符串
	 * @since 3.3.0
	 */
	public static boolean isJsonObj(String str) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		return StrUtil.isWrap(str.trim(), '{', '}');
	}

	/**
	 * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
	 * 
	 * @param str 字符串
	 * @return 是否为JSON字符串
	 * @since 3.3.0
	 */
	public static boolean isJsonArray(String str) {
		if (StrUtil.isBlank(str)) {
			return false;
		}
		return StrUtil.isWrap(str.trim(), '[', ']');
	}
	
	/**
	 * XML转JSONObject<br>
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 * 
	 * @param xml XML字符串
	 * @return JSONObject
	 * @since 4.0.8
	 */
	public static JSONObject xmlToJson(String xml) {
		return XML.toJSONObject(xml);
	}
}
