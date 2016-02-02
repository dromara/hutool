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
	 * @param jsonArray JSONArray
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
		return jsonObject.toJSONString(4);
	}
	
	/**
	 * 转为格式化后的JSON字符串
	 * @param jsonArray JSONArray
	 * @return JSON字符串
	 */
	public static String toJsonPrettyStr(JSONArray jsonArray) {
		return jsonArray.toJSONString(4);
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
	
	/**
	 * 写入值到Writer
	 * @param writer Writer
	 * @param value 值
	 * @param indentFactor
	 * @param indent 缩进空格数
	 * @return Writer
	 * @throws JSONException
	 * @throws IOException
	 */
	protected static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
		if (value == null || value.equals(null)) {
			writer.write("null");
		} else if (value instanceof JSON) {
			((JSON) value).write(writer, indentFactor, indent);
		}else if (value instanceof Map) {
			new JSONObject((Map<?, ?>) value).write(writer, indentFactor, indent);
		} else if (value instanceof Collection) {
			new JSONArray((Collection<?>) value).write(writer, indentFactor, indent);
		} else if (value.getClass().isArray()) {
			new JSONArray(value).write(writer, indentFactor, indent);
		} else if (value instanceof Number) {
			writer.write(numberToString((Number) value));
		} else if (value instanceof Boolean) {
			writer.write(value.toString());
		} else if (value instanceof JSONString) {
			Object o;
			try {
				o = ((JSONString) value).toJSONString();
			} catch (Exception e) {
				throw new JSONException(e);
			}
			writer.write(o != null ? o.toString() : quote(value.toString()));
		} else {
			quote(value.toString(), writer);
		}
		return writer;
	}

	/**
	 * 缩进，使用空格符
	 * @param writer
	 * @param indent
	 * @throws IOException
	 */
	protected static final void indent(Writer writer, int indent) throws IOException {
		for (int i = 0; i < indent; i += 1) {
			writer.write(' ');
		}
	}
	
	/**
	 * Produce a string from a Number.
	 *
	 * @param number A Number
	 * @return A String.
	 * @throws JSONException If n is a non-finite number.
	 */
	protected static String numberToString(Number number) throws JSONException {
		if (number == null) {
			throw new JSONException("Null pointer");
		}
		
		testValidity(number);

		// Shave off trailing zeros and decimal point, if possible.

		String string = number.toString();
		if (string.indexOf('.') > 0 && string.indexOf('e') < 0 && string.indexOf('E') < 0) {
			while (string.endsWith("0")) {
				string = string.substring(0, string.length() - 1);
			}
			if (string.endsWith(".")) {
				string = string.substring(0, string.length() - 1);
			}
		}
		return string;
	}
	
	/**
	 * 如果对象是Number 且是 NaN or infinite，将抛出异常
	 * @param obj 被检查的对象
	 * @throws JSONException If o is a non-finite number.
	 */
	protected static void testValidity(Object obj) throws JSONException {
		if(false == ObjectUtil.isValidIfNumber(obj)){
			throw new JSONException("JSON does not allow non-finite numbers.");
		}
	}
	
	/**
	 * Make a JSON text of an Object value. If the object has an value.toJSONString() method, then that method will be used to produce the JSON text. The method is required to produce a strictly
	 * conforming text. If the object does not contain a toJSONString method (which is the most common case), then a text will be produced by other means. If the value is an array or Collection, then
	 * a JSONArray will be made from it and its toJSONString method will be called. If the value is a MAP, then a JSONObject will be made from it and its toJSONString method will be called. Otherwise,
	 * the value's toString method will be called, and the result will be quoted.
	 *
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param value The value to be serialized.
	 * @return a printable, displayable, transmittable representation of the object, beginning with <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code>&nbsp;<small>(right
	 *         brace)</small>.
	 * @throws JSONException If the value is or contains an invalid number.
	 */
	protected static String valueToString(Object value) throws JSONException {
		if (value == null || value.equals(null)) {
			return "null";
		}
		if (value instanceof JSONString) {
			Object object;
			try {
				object = ((JSONString) value).toJSONString();
			} catch (Exception e) {
				throw new JSONException(e);
			}
			if (object instanceof String) {
				return (String) object;
			}
			throw new JSONException("Bad value from toJSONString: " + object);
		}
		if (value instanceof Number) {
			return numberToString((Number) value);
		}
		if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray) {
			return value.toString();
		}
		if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			return new JSONObject(map).toString();
		}
		if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value;
			return new JSONArray(coll).toString();
		}
		if (value.getClass().isArray()) {
			return new JSONArray(value).toString();
		}
		return quote(value.toString());
	}
}
