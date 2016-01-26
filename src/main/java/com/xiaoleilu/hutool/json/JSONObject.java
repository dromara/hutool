package com.xiaoleilu.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * JSONObject是一个无序键值对. 它是一个由大括号包围的，使用冒号分隔name和value的字符串，每个键值对使用逗号隔开。<br>
 * 此对象使用 <code>get</code> 和 <code>opt</code> 方法通过name获得value, 使用 <code>put</code>方法增加或替换值。<br>
 * value支持的类型如下： <code>Boolean</code>, <code>JSONArray</code>, <code>JSONObject</code>, <code>Number</code>, <code>String</code>, 或者 <code>JSONObject.NULL</code> <br>
 * 
 * <code>put</code> 方法示例：<br>
 * 
 * <pre>
 * myString = new JSONObject().put(&quot;JSON&quot;, &quot;Hello, World!&quot;).toString();
 * </pre>
 * 
 * 结果： <code>{"JSON": "Hello, World"}</code>. <br>
 * <code>toString</code> 生成的字符串严格遵循JSON语法格式. 构造访问传入的JSON可以兼容更宽泛的语法:
 * <ul>
 * <li><code>,</code>&nbsp;<small>(逗号)</small> 可以出现在最后一个花括号之前。</li>
 * <li>字符串支持 <code>'</code>&nbsp;<small>(单引号)</small>包围。</li>
 * <li>字符串可以不被引号包围，但是这个字符串不能在字符串开始位置包含引号，不能以空格开始或结尾，不能包含字符<code>{ } [ ] / \ : , #</code>也不能是数字或者<code>true</code>, <code>false</code>, or <code>null</code>这些关键字。</li>
 * </ul>
 *
 * @author JSON.org，looly
 */
public class JSONObject {

	/**
	 * The map where the JSONObject's properties are kept.
	 */
	private final Map<String, Object> map;

	/**
	 * <code>NULL</code> 对象用于减少歧义来表示Java 中的<code>null</code> <br>
	 * <code>JSONObject.NULL.equals(null)</code> 返回 <code>true</code>. <br>
	 * <code>JSONObject.NULL.toString()</code> 返回 <code>"null"</code>.
	 */
	public static final Object NULL = new Null();

	/**
	 * 空构造
	 */
	public JSONObject() {
		this.map = new HashMap<String, Object>();
	}

	/**
	 * 使用其他<code>JSONObject</code>构造新的<code>JSONObject</code>，并只加入指定name对应的键值对。
	 *
	 * @param jsonObject A JSONObject.
	 * @param names 需要的name列表
	 */
	public JSONObject(JSONObject jsonObject, String... names) {
		this();
		for (String name : names) {
			try {
				this.putOnce(name, jsonObject.opt(name));
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * Construct a JSONObject from a JSONTokener.
	 *
	 * @param x A JSONTokener object containing the source string.
	 * @throws JSONException If there is a syntax error in the source string or a duplicated key.
	 */
	public JSONObject(JSONTokener x) throws JSONException {
		this();
		char c;
		String key;

		if (x.nextClean() != '{') {
			throw x.syntaxError("A JSONObject text must begin with '{'");
		}
		for (;;) {
			c = x.nextClean();
			switch (c) {
				case 0:
					throw x.syntaxError("A JSONObject text must end with '}'");
				case '}':
					return;
				default:
					x.back();
					key = x.nextValue().toString();
			}

			// The key is followed by ':'.

			c = x.nextClean();
			if (c != ':') {
				throw x.syntaxError("Expected a ':' after a key");
			}
			this.putOnce(key, x.nextValue());

			// Pairs are separated by ','.

			switch (x.nextClean()) {
				case ';':
				case ',':
					if (x.nextClean() == '}') {
						return;
					}
					x.back();
					break;
				case '}':
					return;
				default:
					throw x.syntaxError("Expected a ',' or '}'");
			}
		}
	}

	/**
	 * Construct a JSONObject from a Map.
	 *
	 * @param map A map object that can be used to initialize the contents of the JSONObject.
	 */
	public JSONObject(Map<?, ?> map) {
		this();
		if (map != null) {
			for (final Entry<?, ?> e : map.entrySet()) {
				final Object value = e.getValue();
				if (value != null) {
					this.map.put(String.valueOf(e.getKey()), wrap(value));
				}
			}
		}
	}

	/**
	 * Construct a JSONObject from an Object using bean getters. It reflects on all of the public methods of the object. For each of the methods with no parameters and a name starting with
	 * <code>"get"</code> or <code>"is"</code> followed by an uppercase letter, the method is invoked, and a key and the value returned from the getter method are put into the new JSONObject.
	 *
	 * The key is formed by removing the <code>"get"</code> or <code>"is"</code> prefix. If the second remaining character is not upper case, then the first character is converted to lower case.
	 *
	 * For example, if an object has a method named <code>"getName"</code>, and if the result of calling <code>object.getName()</code> is <code>"Larry Fine"</code>, then the JSONObject will contain
	 * <code>"name": "Larry Fine"</code>.
	 *
	 * @param bean An object that has getter methods that should be used to make a JSONObject.
	 */
	public JSONObject(Object bean) {
		this();
		this.populateMap(bean);
	}

	/**
	 * Construct a JSONObject from an Object, using reflection to find the public members. The resulting JSONObject's keys will be the strings from the names array, and the values will be the field
	 * values associated with those keys in the object. If a key is not found or not visible, then it will not be copied into the new JSONObject.
	 *
	 * @param object An object that has fields that should be used to make a JSONObject.
	 * @param names An array of strings, the names of the fields to be obtained from the object.
	 */
	public JSONObject(Object object, String names[]) {
		this();
		Class<?> c = object.getClass();
		for (int i = 0; i < names.length; i += 1) {
			String name = names[i];
			try {
				this.putOpt(name, c.getField(name).get(object));
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * Construct a JSONObject from a source JSON text string. This is the most commonly used JSONObject constructor.
	 *
	 * @param source A string beginning with <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code> &nbsp;<small>(right brace)</small>.
	 * @exception JSONException If there is a syntax error in the source string or a duplicated key.
	 */
	public JSONObject(String source) throws JSONException {
		this(new JSONTokener(source));
	}

	/**
	 * Construct a JSONObject from a ResourceBundle.
	 *
	 * @param baseName The ResourceBundle base name.
	 * @param locale The Locale to load the ResourceBundle for.
	 * @throws JSONException If any JSONExceptions are detected.
	 */
	public JSONObject(String baseName, Locale locale) throws JSONException {
		this();
		ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, Thread.currentThread().getContextClassLoader());

		// Iterate through the keys in the bundle.

		Enumeration<String> keys = bundle.getKeys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			if (key != null) {

				// Go through the path, ensuring that there is a nested JSONObject for each
				// segment except the last. Add the value using the last segment's name into
				// the deepest nested JSONObject.

				String[] path = ((String) key).split("\\.");
				int last = path.length - 1;
				JSONObject target = this;
				for (int i = 0; i < last; i += 1) {
					String segment = path[i];
					JSONObject nextTarget = target.optJSONObject(segment);
					if (nextTarget == null) {
						nextTarget = new JSONObject();
						target.put(segment, nextTarget);
					}
					target = nextTarget;
				}
				target.put(path[last], bundle.getString((String) key));
			}
		}
	}

	/**
	 * Accumulate values under a key. It is similar to the put method except that if there is already an object stored under the key then a JSONArray is stored under the key to hold all of the
	 * accumulated values. If there is already a JSONArray, then the new value is appended to it. In contrast, the put method replaces the previous value.
	 *
	 * If only one value is accumulated that is not a JSONArray, then the result will be the same as using put. But if multiple values are accumulated, then the result will be like append.
	 *
	 * @param key A key string.
	 * @param value An object to be accumulated under the key.
	 * @return this.
	 * @throws JSONException If the value is an invalid number or if the key is null.
	 */
	public JSONObject accumulate(String key, Object value) throws JSONException {
		testValidity(value);
		Object object = this.opt(key);
		if (object == null) {
			this.put(key, value instanceof JSONArray ? new JSONArray().put(value) : value);
		} else if (object instanceof JSONArray) {
			((JSONArray) object).put(value);
		} else {
			this.put(key, new JSONArray().put(object).put(value));
		}
		return this;
	}

	/**
	 * Append values to the array under a key. If the key does not exist in the JSONObject, then the key is put in the JSONObject with its value being a JSONArray containing the value parameter. If
	 * the key was already associated with a JSONArray, then the value parameter is appended to it.
	 *
	 * @param key A key string.
	 * @param value An object to be accumulated under the key.
	 * @return this.
	 * @throws JSONException If the key is null or if the current value associated with the key is not a JSONArray.
	 */
	public JSONObject append(String key, Object value) throws JSONException {
		testValidity(value);
		Object object = this.opt(key);
		if (object == null) {
			this.put(key, new JSONArray().put(value));
		} else if (object instanceof JSONArray) {
			this.put(key, ((JSONArray) object).put(value));
		} else {
			throw new JSONException("JSONObject[" + key + "] is not a JSONArray.");
		}
		return this;
	}

	/**
	 * Produce a string from a double. The string "null" will be returned if the number is not finite.
	 *
	 * @param d A double.
	 * @return A String.
	 */
	public static String doubleToString(double d) {
		if (Double.isInfinite(d) || Double.isNaN(d)) {
			return "null";
		}

		// Shave off trailing zeros and decimal point, if possible.

		String string = Double.toString(d);
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
	 * Get the value object associated with a key.
	 *
	 * @param key A key string.
	 * @return The object associated with the key.
	 * @throws JSONException if the key is not found.
	 */
	public Object get(String key) throws JSONException {
		if (key == null) {
			throw new JSONException("Null key.");
		}
		Object object = this.opt(key);
		if (object == null) {
			throw new JSONException("JSONObject[" + quote(key) + "] not found.");
		}
		return object;
	}

	/**
	 * Get the enum value associated with a key.
	 * 
	 * @param clazz The type of enum to retrieve.
	 * @param key A key string.
	 * @return The enum value associated with the key
	 * @throws JSONException if the key is not found or if the value cannot be converted to an enum.
	 */
	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) throws JSONException {
		E val = optEnum(clazz, key);
		if (val == null) {
			// JSONException should really take a throwable argument.
			// If it did, I would re-implement this with the Enum.valueOf
			// method and place any thrown exception in the JSONException
			throw new JSONException("JSONObject[" + quote(key) + "] is not an enum of type " + quote(clazz.getSimpleName()) + ".");
		}
		return val;
	}

	/**
	 * Get the boolean value associated with a key.
	 *
	 * @param key A key string.
	 * @return The truth.
	 * @throws JSONException if the value is not a Boolean or the String "true" or "false".
	 */
	public boolean getBoolean(String key) throws JSONException {
		Object object = this.get(key);
		if (object.equals(Boolean.FALSE) || (object instanceof String && ((String) object).equalsIgnoreCase("false"))) {
			return false;
		} else if (object.equals(Boolean.TRUE) || (object instanceof String && ((String) object).equalsIgnoreCase("true"))) {
			return true;
		}
		throw new JSONException("JSONObject[" + quote(key) + "] is not a Boolean.");
	}

	/**
	 * Get the BigInteger value associated with a key.
	 *
	 * @param key A key string.
	 * @return The numeric value.
	 * @throws JSONException if the key is not found or if the value cannot be converted to BigInteger.
	 */
	public BigInteger getBigInteger(String key) throws JSONException {
		Object object = this.get(key);
		try {
			return new BigInteger(object.toString());
		} catch (Exception e) {
			throw new JSONException("JSONObject[" + quote(key) + "] could not be converted to BigInteger.");
		}
	}

	/**
	 * Get the BigDecimal value associated with a key.
	 *
	 * @param key A key string.
	 * @return The numeric value.
	 * @throws JSONException if the key is not found or if the value cannot be converted to BigDecimal.
	 */
	public BigDecimal getBigDecimal(String key) throws JSONException {
		Object object = this.get(key);
		try {
			return new BigDecimal(object.toString());
		} catch (Exception e) {
			throw new JSONException("JSONObject[" + quote(key) + "] could not be converted to BigDecimal.");
		}
	}

	/**
	 * Get the double value associated with a key.
	 *
	 * @param key A key string.
	 * @return The numeric value.
	 * @throws JSONException if the key is not found or if the value is not a Number object and cannot be converted to a number.
	 */
	public double getDouble(String key) throws JSONException {
		Object object = this.get(key);
		try {
			return object instanceof Number ? ((Number) object).doubleValue() : Double.parseDouble((String) object);
		} catch (Exception e) {
			throw new JSONException("JSONObject[" + quote(key) + "] is not a number.");
		}
	}

	/**
	 * Get the int value associated with a key.
	 *
	 * @param key A key string.
	 * @return The integer value.
	 * @throws JSONException if the key is not found or if the value cannot be converted to an integer.
	 */
	public int getInt(String key) throws JSONException {
		Object object = this.get(key);
		try {
			return object instanceof Number ? ((Number) object).intValue() : Integer.parseInt((String) object);
		} catch (Exception e) {
			throw new JSONException("JSONObject[" + quote(key) + "] is not an int.");
		}
	}

	/**
	 * Get the JSONArray value associated with a key.
	 *
	 * @param key A key string.
	 * @return A JSONArray which is the value.
	 * @throws JSONException if the key is not found or if the value is not a JSONArray.
	 */
	public JSONArray getJSONArray(String key) throws JSONException {
		Object object = this.get(key);
		if (object instanceof JSONArray) {
			return (JSONArray) object;
		}
		throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONArray.");
	}

	/**
	 * Get the JSONObject value associated with a key.
	 *
	 * @param key A key string.
	 * @return A JSONObject which is the value.
	 * @throws JSONException if the key is not found or if the value is not a JSONObject.
	 */
	public JSONObject getJSONObject(String key) throws JSONException {
		Object object = this.get(key);
		if (object instanceof JSONObject) {
			return (JSONObject) object;
		}
		throw new JSONException("JSONObject[" + quote(key) + "] is not a JSONObject.");
	}

	/**
	 * Get the long value associated with a key.
	 *
	 * @param key A key string.
	 * @return The long value.
	 * @throws JSONException if the key is not found or if the value cannot be converted to a long.
	 */
	public long getLong(String key) throws JSONException {
		Object object = this.get(key);
		try {
			return object instanceof Number ? ((Number) object).longValue() : Long.parseLong((String) object);
		} catch (Exception e) {
			throw new JSONException("JSONObject[" + quote(key) + "] is not a long.");
		}
	}

	/**
	 * Get an array of field names from a JSONObject.
	 *
	 * @return An array of field names, or null if there are no names.
	 */
	public static String[] getNames(JSONObject jo) {
		int length = jo.length();
		if (length == 0) {
			return null;
		}
		Iterator<String> iterator = jo.keys();
		String[] names = new String[length];
		int i = 0;
		while (iterator.hasNext()) {
			names[i] = iterator.next();
			i += 1;
		}
		return names;
	}

	/**
	 * Get an array of field names from an Object.
	 *
	 * @return An array of field names, or null if there are no names.
	 */
	public static String[] getNames(Object object) {
		if (object == null) {
			return null;
		}
		Class<?> klass = object.getClass();
		Field[] fields = klass.getFields();
		int length = fields.length;
		if (length == 0) {
			return null;
		}
		String[] names = new String[length];
		for (int i = 0; i < length; i += 1) {
			names[i] = fields[i].getName();
		}
		return names;
	}

	/**
	 * Get the string associated with a key.
	 *
	 * @param key A key string.
	 * @return A string which is the value.
	 * @throws JSONException if there is no string value for the key.
	 */
	public String getString(String key) throws JSONException {
		Object object = this.get(key);
		if (object instanceof String) {
			return (String) object;
		}
		throw new JSONException("JSONObject[" + quote(key) + "] not a string.");
	}

	/**
	 * Determine if the JSONObject contains a specific key.
	 *
	 * @param key A key string.
	 * @return true if the key exists in the JSONObject.
	 */
	public boolean has(String key) {
		return this.map.containsKey(key);
	}

	/**
	 * Increment a property of a JSONObject. If there is no such property, create one with a value of 1. If there is such a property, and if it is an Integer, Long, Double, or Float, then add one to
	 * it.
	 *
	 * @param key A key string.
	 * @return this.
	 * @throws JSONException If there is already a property with this name that is not an Integer, Long, Double, or Float.
	 */
	public JSONObject increment(String key) throws JSONException {
		Object value = this.opt(key);
		if (value == null) {
			this.put(key, 1);
		} else if (value instanceof BigInteger) {
			this.put(key, ((BigInteger) value).add(BigInteger.ONE));
		} else if (value instanceof BigDecimal) {
			this.put(key, ((BigDecimal) value).add(BigDecimal.ONE));
		} else if (value instanceof Integer) {
			this.put(key, (Integer) value + 1);
		} else if (value instanceof Long) {
			this.put(key, (Long) value + 1);
		} else if (value instanceof Double) {
			this.put(key, (Double) value + 1);
		} else if (value instanceof Float) {
			this.put(key, (Float) value + 1);
		} else {
			throw new JSONException("Unable to increment [" + quote(key) + "].");
		}
		return this;
	}

	/**
	 * Determine if the value associated with the key is null or if there is no value.
	 *
	 * @param key A key string.
	 * @return true if there is no value associated with the key or if the value is the JSONObject.NULL object.
	 */
	public boolean isNull(String key) {
		return JSONObject.NULL.equals(this.opt(key));
	}

	/**
	 * Get an enumeration of the keys of the JSONObject.
	 *
	 * @return An iterator of the keys.
	 */
	public Iterator<String> keys() {
		return this.keySet().iterator();
	}

	/**
	 * Get a set of keys of the JSONObject.
	 *
	 * @return A keySet.
	 */
	public Set<String> keySet() {
		return this.map.keySet();
	}

	/**
	 * Get the number of keys stored in the JSONObject.
	 *
	 * @return The number of keys in the JSONObject.
	 */
	public int length() {
		return this.map.size();
	}

	/**
	 * Produce a JSONArray containing the names of the elements of this JSONObject.
	 *
	 * @return A JSONArray containing the key strings, or null if the JSONObject is empty.
	 */
	public JSONArray names() {
		JSONArray ja = new JSONArray();
		Iterator<String> keys = this.keys();
		while (keys.hasNext()) {
			ja.put(keys.next());
		}
		return ja.length() == 0 ? null : ja;
	}

	/**
	 * Produce a string from a Number.
	 *
	 * @param number A Number
	 * @return A String.
	 * @throws JSONException If n is a non-finite number.
	 */
	public static String numberToString(Number number) throws JSONException {
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
	 * Get an optional value associated with a key.
	 *
	 * @param key A key string.
	 * @return An object which is the value, or null if there is no value.
	 */
	public Object opt(String key) {
		return key == null ? null : this.map.get(key);
	}

	/**
	 * Get the enum value associated with a key.
	 * 
	 * @param clazz The type of enum to retrieve.
	 * @param key A key string.
	 * @return The enum value associated with the key or null if not found
	 */
	public <E extends Enum<E>> E optEnum(Class<E> clazz, String key) {
		return this.optEnum(clazz, key, null);
	}

	/**
	 * Get the enum value associated with a key.
	 * 
	 * @param clazz The type of enum to retrieve.
	 * @param key A key string.
	 * @param defaultValue The default in case the value is not found
	 * @return The enum value associated with the key or defaultValue if the value is not found or cannot be assigned to clazz
	 */
	public <E extends Enum<E>> E optEnum(Class<E> clazz, String key, E defaultValue) {
		try {
			Object val = this.opt(key);
			if (NULL.equals(val)) {
				return defaultValue;
			}
			if (clazz.isAssignableFrom(val.getClass())) {
				// we just checked it!
				@SuppressWarnings("unchecked")
				E myE = (E) val;
				return myE;
			}
			return Enum.valueOf(clazz, val.toString());
		} catch (IllegalArgumentException | NullPointerException e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional boolean associated with a key. It returns false if there is no such key, or if the value is not Boolean.TRUE or the String "true".
	 *
	 * @param key A key string.
	 * @return The truth.
	 */
	public boolean optBoolean(String key) {
		return this.optBoolean(key, false);
	}

	/**
	 * Get an optional boolean associated with a key. It returns the defaultValue if there is no such key, or if it is not a Boolean or the String "true" or "false" (case insensitive).
	 *
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return The truth.
	 */
	public boolean optBoolean(String key, boolean defaultValue) {
		try {
			return this.getBoolean(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional double associated with a key, or NaN if there is no such key or if its value is not a number. If the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key A string which is the key.
	 * @return An object which is the value.
	 */
	public double optDouble(String key) {
		return this.optDouble(key, Double.NaN);
	}

	/**
	 * Get an optional BigInteger associated with a key, or the defaultValue if there is no such key or if its value is not a number. If the value is a string, an attempt will be made to evaluate it
	 * as a number.
	 *
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public BigInteger optBigInteger(String key, BigInteger defaultValue) {
		try {
			return this.getBigInteger(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional BigDecimal associated with a key, or the defaultValue if there is no such key or if its value is not a number. If the value is a string, an attempt will be made to evaluate it
	 * as a number.
	 *
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public BigDecimal optBigDecimal(String key, BigDecimal defaultValue) {
		try {
			return this.getBigDecimal(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional double associated with a key, or the defaultValue if there is no such key or if its value is not a number. If the value is a string, an attempt will be made to evaluate it as a
	 * number.
	 *
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public double optDouble(String key, double defaultValue) {
		try {
			return this.getDouble(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional int value associated with a key, or zero if there is no such key or if the value is not a number. If the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key A key string.
	 * @return An object which is the value.
	 */
	public int optInt(String key) {
		return this.optInt(key, 0);
	}

	/**
	 * Get an optional int value associated with a key, or the default if there is no such key or if the value is not a number. If the value is a string, an attempt will be made to evaluate it as a
	 * number.
	 *
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public int optInt(String key, int defaultValue) {
		try {
			return this.getInt(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional JSONArray associated with a key. It returns null if there is no such key, or if its value is not a JSONArray.
	 *
	 * @param key A key string.
	 * @return A JSONArray which is the value.
	 */
	public JSONArray optJSONArray(String key) {
		Object o = this.opt(key);
		return o instanceof JSONArray ? (JSONArray) o : null;
	}

	/**
	 * Get an optional JSONObject associated with a key. It returns null if there is no such key, or if its value is not a JSONObject.
	 *
	 * @param key A key string.
	 * @return A JSONObject which is the value.
	 */
	public JSONObject optJSONObject(String key) {
		Object object = this.opt(key);
		return object instanceof JSONObject ? (JSONObject) object : null;
	}

	/**
	 * Get an optional long value associated with a key, or zero if there is no such key or if the value is not a number. If the value is a string, an attempt will be made to evaluate it as a number.
	 *
	 * @param key A key string.
	 * @return An object which is the value.
	 */
	public long optLong(String key) {
		return this.optLong(key, 0);
	}

	/**
	 * Get an optional long value associated with a key, or the default if there is no such key or if the value is not a number. If the value is a string, an attempt will be made to evaluate it as a
	 * number.
	 *
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public long optLong(String key, long defaultValue) {
		try {
			return this.getLong(key);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get an optional string associated with a key. It returns an empty string if there is no such key. If the value is not a string and is not null, then it is converted to a string.
	 *
	 * @param key A key string.
	 * @return A string which is the value.
	 */
	public String optString(String key) {
		return this.optString(key, "");
	}

	/**
	 * Get an optional string associated with a key. It returns the defaultValue if there is no such key.
	 *
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return A string which is the value.
	 */
	public String optString(String key, String defaultValue) {
		Object object = this.opt(key);
		return NULL.equals(object) ? defaultValue : object.toString();
	}

	private void populateMap(Object bean) {
		Class<?> klass = bean.getClass();

		// If klass is a System class then set includeSuperClass to false.

		boolean includeSuperClass = klass.getClassLoader() != null;

		Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i += 1) {
			try {
				Method method = methods[i];
				if (Modifier.isPublic(method.getModifiers())) {
					String name = method.getName();
					String key = "";
					if (name.startsWith("get")) {
						if ("getClass".equals(name) || "getDeclaringClass".equals(name)) {
							key = "";
						} else {
							key = name.substring(3);
						}
					} else if (name.startsWith("is")) {
						key = name.substring(2);
					}
					if (key.length() > 0 && Character.isUpperCase(key.charAt(0)) && method.getParameterTypes().length == 0) {
						if (key.length() == 1) {
							key = key.toLowerCase();
						} else if (!Character.isUpperCase(key.charAt(1))) {
							key = key.substring(0, 1).toLowerCase() + key.substring(1);
						}

						Object result = method.invoke(bean, (Object[]) null);
						if (result != null) {
							this.map.put(key, wrap(result));
						}
					}
				}
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * Put a key/boolean pair in the JSONObject.
	 *
	 * @param key A key string.
	 * @param value A boolean which is the value.
	 * @return this.
	 * @throws JSONException If the key is null.
	 */
	public JSONObject put(String key, boolean value) throws JSONException {
		this.put(key, value ? Boolean.TRUE : Boolean.FALSE);
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject, where the value will be a JSONArray which is produced from a Collection.
	 *
	 * @param key A key string.
	 * @param value A Collection value.
	 * @return this.
	 * @throws JSONException
	 */
	public JSONObject put(String key, Collection<?> value) throws JSONException {
		this.put(key, new JSONArray(value));
		return this;
	}

	/**
	 * Put a key/double pair in the JSONObject.
	 *
	 * @param key A key string.
	 * @param value A double which is the value.
	 * @return this.
	 * @throws JSONException If the key is null or if the number is invalid.
	 */
	public JSONObject put(String key, double value) throws JSONException {
		this.put(key, new Double(value));
		return this;
	}

	/**
	 * Put a key/int pair in the JSONObject.
	 *
	 * @param key A key string.
	 * @param value An int which is the value.
	 * @return this.
	 * @throws JSONException If the key is null.
	 */
	public JSONObject put(String key, int value) throws JSONException {
		this.put(key, new Integer(value));
		return this;
	}

	/**
	 * Put a key/long pair in the JSONObject.
	 *
	 * @param key A key string.
	 * @param value A long which is the value.
	 * @return this.
	 * @throws JSONException If the key is null.
	 */
	public JSONObject put(String key, long value) throws JSONException {
		this.put(key, new Long(value));
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject, where the value will be a JSONObject which is produced from a Map.
	 *
	 * @param key A key string.
	 * @param value A Map value.
	 * @return this.
	 * @throws JSONException
	 */
	public JSONObject put(String key, Map<?, ?> value) throws JSONException {
		this.put(key, new JSONObject(value));
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject. If the value is null, then the key will be removed from the JSONObject if it is present.
	 *
	 * @param key A key string.
	 * @param value An object which is the value. It should be of one of these types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONObject.NULL object.
	 * @return this.
	 * @throws JSONException If the value is non-finite number or if the key is null.
	 */
	public JSONObject put(String key, Object value) throws JSONException {
		if (key == null) {
			throw new NullPointerException("Null key.");
		}
		if (value != null) {
			testValidity(value);
			this.map.put(key, value);
		} else {
			this.remove(key);
		}
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject, but only if the key and the value are both non-null, and only if there is not already a member with that name.
	 *
	 * @param key string
	 * @param value object
	 * @return this.
	 * @throws JSONException if the key is a duplicate
	 */
	public JSONObject putOnce(String key, Object value) throws JSONException {
		if (key != null && value != null) {
			if (this.opt(key) != null) {
				throw new JSONException("Duplicate key \"" + key + "\"");
			}
			this.put(key, value);
		}
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject, but only if the key and the value are both non-null.
	 *
	 * @param key A key string.
	 * @param value An object which is the value. It should be of one of these types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONObject.NULL object.
	 * @return this.
	 * @throws JSONException If the value is a non-finite number.
	 */
	public JSONObject putOpt(String key, Object value) throws JSONException {
		if (key != null && value != null) {
			this.put(key, value);
		}
		return this;
	}

	/**
	 * Produce a string in double quotes with backslash sequences in all the right places. A backslash will be inserted within </, producing <\/, allowing JSON text to be delivered in HTML. In JSON
	 * text, a string cannot contain a control character or an unescaped quote or backslash.
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
		for (i = 0; i < len; i += 1) {
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
	 * Remove a name and its value, if present.
	 *
	 * @param key The name to be removed.
	 * @return The value that was associated with the name, or null if there was no value.
	 */
	public Object remove(String key) {
		return this.map.remove(key);
	}

	/**
	 * Determine if two JSONObjects are similar. They must contain the same set of names which must be associated with similar values.
	 *
	 * @param other The other JSONObject
	 * @return true if they are equal
	 */
	public boolean similar(Object other) {
		try {
			if (!(other instanceof JSONObject)) {
				return false;
			}
			Set<String> set = this.keySet();
			if (!set.equals(((JSONObject) other).keySet())) {
				return false;
			}
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String name = iterator.next();
				Object valueThis = this.get(name);
				Object valueOther = ((JSONObject) other).get(name);
				if (valueThis instanceof JSONObject) {
					if (!((JSONObject) valueThis).similar(valueOther)) {
						return false;
					}
				} else if (valueThis instanceof JSONArray) {
					if (!((JSONArray) valueThis).similar(valueOther)) {
						return false;
					}
				} else if (!valueThis.equals(valueOther)) {
					return false;
				}
			}
			return true;
		} catch (Throwable exception) {
			return false;
		}
	}

	/**
	 * Try to convert a string into a number, boolean, or null. If the string can't be converted, return the string.
	 *
	 * @param string A String.
	 * @return A simple JSON value.
	 */
	public static Object stringToValue(String string) {
		Double d;
		if (string.equals("")) {
			return string;
		}
		if (string.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		}
		if (string.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		}
		if (string.equalsIgnoreCase("null")) {
			return JSONObject.NULL;
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

	/**
	 * Throw an exception if the object is a NaN or infinite number.
	 *
	 * @param o The object to test.
	 * @throws JSONException If o is a non-finite number.
	 */
	public static void testValidity(Object o) throws JSONException {
		if (o != null) {
			if (o instanceof Double) {
				if (((Double) o).isInfinite() || ((Double) o).isNaN()) {
					throw new JSONException("JSON does not allow non-finite numbers.");
				}
			} else if (o instanceof Float) {
				if (((Float) o).isInfinite() || ((Float) o).isNaN()) {
					throw new JSONException("JSON does not allow non-finite numbers.");
				}
			}
		}
	}

	/**
	 * Produce a JSONArray containing the values of the members of this JSONObject.
	 *
	 * @param names A JSONArray containing a list of key strings. This determines the sequence of the values in the result.
	 * @return A JSONArray of values.
	 * @throws JSONException If any of the values are non-finite numbers.
	 */
	public JSONArray toJSONArray(JSONArray names) throws JSONException {
		if (names == null || names.length() == 0) {
			return null;
		}
		JSONArray ja = new JSONArray();
		for (int i = 0; i < names.length(); i += 1) {
			ja.put(this.opt(names.getString(i)));
		}
		return ja;
	}

	/**
	 * Make a JSON text of this JSONObject. For compactness, no whitespace is added. If this would not result in a syntactically correct JSON text, then null will be returned instead.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @return a printable, displayable, portable, transmittable representation of the object, beginning with <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code>&nbsp;
	 *         <small>(right brace)</small>.
	 */
	public String toString() {
		try {
			return this.toString(0);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Make a prettyprinted JSON text of this JSONObject.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param indentFactor The number of spaces to add to each level of indentation.
	 * @return a printable, displayable, portable, transmittable representation of the object, beginning with <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code>&nbsp;
	 *         <small>(right brace)</small>.
	 * @throws JSONException If the object contains an invalid number.
	 */
	public String toString(int indentFactor) throws JSONException {
		StringWriter w = new StringWriter();
		synchronized (w.getBuffer()) {
			return this.write(w, indentFactor, 0).toString();
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
	public static String valueToString(Object value) throws JSONException {
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
	 * Wrap an object, if necessary. If the object is null, return the NULL object. If it is an array or collection, wrap it in a JSONArray. If it is a map, wrap it in a JSONObject. If it is a
	 * standard property (Double, String, et al) then it is already wrapped. Otherwise, if it comes from one of the java packages, turn it into a string. And if it doesn't, try to wrap it in a
	 * JSONObject. If the wrapping fails, then null is returned.
	 *
	 * @param object The object to wrap
	 * @return The wrapped value
	 */
	public static Object wrap(Object object) {
		try {
			if (object == null) {
				return NULL;
			}
			if (object instanceof JSONObject || object instanceof JSONArray || NULL.equals(
					object) || object instanceof JSONString || object instanceof Byte || object instanceof Character || object instanceof Short || object instanceof Integer || object instanceof Long || object instanceof Boolean || object instanceof Float || object instanceof Double || object instanceof String || object instanceof BigInteger || object instanceof BigDecimal) {
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
	 * Write the contents of the JSONObject as JSON text to a writer. For compactness, no whitespace is added.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @return The writer.
	 * @throws JSONException
	 */
	public Writer write(Writer writer) throws JSONException {
		return this.write(writer, 0, 0);
	}

	static final Writer writeValue(Writer writer, Object value, int indentFactor, int indent) throws JSONException, IOException {
		if (value == null || value.equals(null)) {
			writer.write("null");
		} else if (value instanceof JSONObject) {
			((JSONObject) value).write(writer, indentFactor, indent);
		} else if (value instanceof JSONArray) {
			((JSONArray) value).write(writer, indentFactor, indent);
		} else if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			new JSONObject(map).write(writer, indentFactor, indent);
		} else if (value instanceof Collection) {
			Collection<?> coll = (Collection<?>) value;
			new JSONArray(coll).write(writer, indentFactor, indent);
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

	static final void indent(Writer writer, int indent) throws IOException {
		for (int i = 0; i < indent; i += 1) {
			writer.write(' ');
		}
	}

	/**
	 * Write the contents of the JSONObject as JSON text to a writer. For compactness, no whitespace is added.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param writer Writes the serialized JSON
	 * @param indentFactor The number of spaces to add to each level of indentation.
	 * @param indent The indention of the top level.
	 * @return The writer.
	 * @throws JSONException
	 */
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		try {
			boolean commanate = false;
			final int length = this.length();
			Iterator<String> keys = this.keys();
			writer.write('{');

			if (length == 1) {
				Object key = keys.next();
				writer.write(quote(key.toString()));
				writer.write(':');
				if (indentFactor > 0) {
					writer.write(' ');
				}
				writeValue(writer, this.map.get(key), indentFactor, indent);
			} else if (length != 0) {
				final int newindent = indent + indentFactor;
				while (keys.hasNext()) {
					Object key = keys.next();
					if (commanate) {
						writer.write(',');
					}
					if (indentFactor > 0) {
						writer.write('\n');
					}
					indent(writer, newindent);
					writer.write(quote(key.toString()));
					writer.write(':');
					if (indentFactor > 0) {
						writer.write(' ');
					}
					writeValue(writer, this.map.get(key), indentFactor, newindent);
					commanate = true;
				}
				if (indentFactor > 0) {
					writer.write('\n');
				}
				indent(writer, indent);
			}
			writer.write('}');
			return writer;
		} catch (IOException exception) {
			throw new JSONException(exception);
		}
	}

	/**
	 * JSONObject.NULL is equivalent to the value that JavaScript calls null, whilst Java's null is equivalent to the value that JavaScript calls undefined.
	 */
	private static final class Null {

		/**
		 * There is only intended to be a single instance of the NULL object, so the clone method returns itself.
		 *
		 * @return NULL.
		 */
		@Override
		protected final Object clone() {
			return this;
		}

		/**
		 * A Null object is equal to the null value and to itself.
		 *
		 * @param object An object to test for nullness.
		 * @return true if the object parameter is the JSONObject.NULL object or null.
		 */
		@Override
		public boolean equals(Object object) {
			return object == null || object == this;
		}

		/**
		 * Get the "null" string value.
		 *
		 * @return The string "null".
		 */
		public String toString() {
			return "null";
		}
	}
}
