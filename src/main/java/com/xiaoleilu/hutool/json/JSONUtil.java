package com.xiaoleilu.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;

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
	 * 为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
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

	/**
	 * 对所有双引号做转义处理（使用双反斜杠做转义）<br>
	 *  为了能在HTML中较好的显示，会将&lt;/转义为&lt;\/<br>
	 *  JSON字符串中不能包含控制字符和未经转义的引号和反斜杠
	 * @param string A String
	 * @param writer Writer
	 * @return A String correctly formatted for insertion in a JSON text.
	 * @throws IOException
	 */
	public static Writer quote(String string, Writer writer) throws IOException {
		if (StrUtil.isEmpty(string)) {
			writer.write("\"\"");
			return writer;
		}

		char b;		//back char
		char c = 0; //current char
		String hhhh;
		int i;
		int len = string.length();

		writer.write('"');
		for (i = 0; i < len; i++) {
			b = c;
			c = string.charAt(i);
			switch (c) {
				case '\\':
				case '"':
					writer.write('\\');
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
					if (c < ' ' || (c >= '\u0080' && c < '\u00a0') || (c >= '\u2000' && c < '\u2100')) {
						writer.write("\\u");
						hhhh = Integer.toHexString(c);
						writer.write("0000", 0, 4 - hhhh.length());
						writer.write(hhhh);
					} else {
						writer.write(c);
					}
			}
		}
		writer.write('"');
		return writer;
	}
	
	/**
	 * 在需要的时候包装对象<br>
	 * If <code>null</code> -> <code>JSONNull.NULL</code><br>
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
			if (ObjectUtil.isArray(object)) {
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
	 * Make a JSON text of an Object value. <br>
	 * If the object has an value.toJSONString() method, then that method will be used to produce the JSON text. <br>
	 * The method is required to produce a strictly conforming text. <br>
	 * If the object does not contain a toJSONString method (which is the most common case), then a text will be produced by other means. <br>
	 * If the value is an array or Collection, then a JSONArray will be made from it and its toJSONString method will be called. <br>
	 * If the value is a MAP, then a JSONObject will be made from it and its toJSONString method will be called. <br>
	 * Otherwise, the value's toString method will be called, and the result will be quoted.<br>
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
	
	/**
	 * Try to convert a string into a number, boolean, or null. If the string can't be converted, return the string.
	 *
	 * @param string A String.
	 * @return A simple JSON value.
	 */
	protected static Object stringToValue(String string) {
		Double d;
		if(null == string || "null".equalsIgnoreCase(string)){
			return JSONNull.NULL;
		}
		
		if (StrUtil.EMPTY.equals(string)) {
			return string;
		}
		if ("true".equalsIgnoreCase(string)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(string)) {
			return Boolean.FALSE;
		}

		/*
		 * If it might be a number, try converting it. If a number cannot be produced, then the value will just be a string.
		 */

		char b = string.charAt(0);
		if ((b >= '0' && b <= '9') || b == '-') {
			try {
				if (string.indexOf('.') > -1 || string.indexOf('e') > -1 || string.indexOf('E') > -1) {
					d = Double.valueOf(string);
					if (!d.isInfinite() && !d.isNaN()) {
						return d;
					}
				} else {
					Long myLong = new Long(string);
					if (string.equals(myLong.toString())) {
						if (myLong == myLong.intValue()) {
							return myLong.intValue();
						} else {
							return myLong;
						}
					}
				}
			} catch (Exception ignore) {
			}
		}
		return string;
	}
}
