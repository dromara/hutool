package com.xiaoleilu.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import com.xiaoleilu.hutool.util.ObjectUtil;

/**
 * JSON工具类
 * 
 * @author Looly
 *
 */
public class JSONUtil {
	
	/**
	 * JSON字符串转JSONObject对象
	 * @param jsonStr JSON字符串
	 * @return JSONObject
	 */
	public static JSONObject parse(String jsonStr) {
		return new JSONObject(jsonStr);
	}

	/**
	 * JSON字符串转JSONArray
	 * @param jsonStr JSON字符串
	 * @return JSONArray
	 */
	public static JSONArray parseArray(String jsonStr) {
		return new JSONArray(jsonStr);
	}

	/**
	 * 转为JSON字符串
	 * @param jsonObject JSONObject
	 * @return JSON字符串
	 */
	public static String toJsonStr(JSONObject jsonObject) {
		return jsonObject.toString();
	}
	
	/**
	 * 转为JSON字符串
	 * @param json JSONArray
	 * @return JSON字符串
	 */
	public static String toJsonStr(JSONArray jsonArray) {
		return jsonArray.toString();
	}
	
	/**
	 * 转为格式化后的JSON字符串
	 * @param jsonObject JSONObject
	 * @return JSON字符串
	 */
	public static String toJsonPrettyStr(JSONObject jsonObject) {
		return jsonObject.toString(4);
	}
	
	/**
	 * 转为格式化后的JSON字符串
	 * @param jsonArray JSONArray
	 * @return JSON字符串
	 */
	public static String toJsonPrettyStr(JSONArray jsonArray) {
		return jsonArray.toString(4);
	}
	
	/**
	 * 转为实体类对象
	 * @param json JSONObject
	 * @param beanClass 实体类对象
	 * @return 实体类对象
	 */
	public static <T> T toBean(JSONObject json, Class<T> beanClass) {
		return null == json ? null : json.toBean(beanClass);
	}

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 * 为了能在HTML中较好的显示，会将</转义为<\/<br>
	 * JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 *
	 * @param string A String
	 * @return A String correctly formatted for insertion in a JSON text.
	 */
	public static String quote(String string) {
		StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			try {
				return quote(string, sw).toString();
			} catch (IOException ignored) {
				// will never happen - we are writing to a string writer
				return "";
			}
		}
	}

	public static Writer quote(String string, Writer w) throws IOException {
		if (string == null || string.length() == 0) {
			w.write("\"\"");
			return w;
		}

		char b;
		char c = 0;
		String hhhh;
		int i;
		int len = string.length();

		w.write('"');
		for (i = 0; i < len; i++) {
			b = c;
			c = string.charAt(i);
			switch (c) {
				case '\\':
				case '"':
					w.write('\\');
					w.write(c);
					break;
				case '/':
					if (b == '<') {
						w.write('\\');
					}
					w.write(c);
					break;
				case '\b':
					w.write("\\b");
					break;
				case '\t':
					w.write("\\t");
					break;
				case '\n':
					w.write("\\n");
					break;
				case '\f':
					w.write("\\f");
					break;
				case '\r':
					w.write("\\r");
					break;
				default:
					if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
						w.write("\\u");
						hhhh = Integer.toHexString(c);
						w.write("0000", 0, 4 - hhhh.length());
						w.write(hhhh);
					} else {
						w.write(c);
					}
			}
		}
		w.write('"');
		return w;
	}
	
	/**
	 * Wrap an object, if necessary. <br>
	 * If the object is null, return the JSONNull.NULL object. <br>
	 * If it is an array or collection, wrap it in a JSONArray. <br>
	 * If it is a map, wrap it in a JSONObject. <br>
	 * If it is a standard property (Double, String, et al) then it is already wrapped. <br>
	 * Otherwise, if it comes from one of the java packages, turn it into a string. <br>
	 * And if it doesn't, try to wrap it in a JSONObject. If the wrapping fails, then null is returned. <br>
	 *
	 * @param object The object to wrap
	 * @return The wrapped value
	 */
	public static Object wrap(Object object) {
		try {
			if (object == null) {
				return JSONNull.NULL;
			}
			if (object instanceof JSONObject || object instanceof JSONArray || JSONNull.NULL.equals(object) || object instanceof JSONString || ObjectUtil.isBasicType(object)) {
				return object;
			}

			if (object instanceof Collection) {
				Collection<?> coll = (Collection<?>) object;
				return new JSONArray(coll);
			}
			if (object.getClass().isArray()) {
				return new JSONArray(object);
			}
			if (object instanceof Map) {
				Map<?, ?> map = (Map<?, ?>) object;
				return new JSONObject(map);
			}
			Package objectPackage = object.getClass().getPackage();
			String objectPackageName = objectPackage != null ? objectPackage.getName() : "";
			if (objectPackageName.startsWith("java.") || objectPackageName.startsWith("javax.") || object.getClass().getClassLoader() == null) {
				return object.toString();
			}
			return new JSONObject(object);
		} catch (Exception exception) {
			return null;
		}
	}
}
