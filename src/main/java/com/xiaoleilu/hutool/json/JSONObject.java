package com.xiaoleilu.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import com.xiaoleilu.hutool.getter.OptNullBasicTypeFromObjectGetter;
import com.xiaoleilu.hutool.lang.Conver;
import com.xiaoleilu.hutool.util.BeanUtil;

/**
 * JSONObject是一个无序键值对. 它是一个由大括号包围的，使用冒号分隔name和value的字符串，每个键值对使用逗号隔开。<br>
 * 此对象使用 <code>get</code> 和 <code>opt</code> 方法通过name获得value, 使用 <code>put</code>方法增加或替换值。<br>
 * value支持的类型如下： <code>Boolean</code>, <code>JSONArray</code>, <code>JSONObject</code>, <code>Number</code>, <code>String</code>, 或者 <code>JSONNull.NULL</code> <br>
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
public class JSONObject extends OptNullBasicTypeFromObjectGetter<String> implements JSON{

	/**
	 * The map where the JSONObject's properties are kept.
	 */
	private final Map<String, Object> map;

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
				this.putOnce(name, jsonObject.get(name));
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
					this.map.put(String.valueOf(e.getKey()), JSONUtil.wrap(value));
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
					JSONObject nextTarget = target.getJSONObject(segment);
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
		JSONUtil.testValidity(value);
		Object object = this.get(key);
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
		JSONUtil.testValidity(value);
		Object object = this.get(key);
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
	 *是否存在指定KEY
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
		Object value = this.get(key);
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
			throw new JSONException("Unable to increment [" + JSONUtil.quote(key) + "].");
		}
		return this;
	}

	/**
	 * Determine if the value associated with the key is null or if there is no value.
	 *
	 * @param key A key string.
	 * @return true if there is no value associated with the key or if the value is the JSONNull.NULL object.
	 */
	public boolean isNull(String key) {
		return JSONNull.NULL.equals(this.get(key));
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
	 * 获得指定KEY对应的值
	 *
	 * @param key KEY
	 * @return 值，无对应值返回Null
	 */
	public Object get(String key) {
		return getObj(key);
	}
	
	@Override
	public Object getObj(String key, Object defaultValue) {
		Object obj = this.map.get(key);
		return null == obj ? defaultValue : obj;
	}

	/**
	 * 获得Enum类型的值
	 * @param clazz Enum的Class
	 * @param key KEY
	 * @return Enum类型的值，无则返回Null
	 */
	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) {
		return this.getEnum(clazz, key, null);
	}

	/**
	 * 获得Enum类型的值
	 * @param clazz Enum的Class
	 * @param key KEY
	 * @param defaultValue 默认值
	 * @return Enum类型的值，无则返回Null
	 */
	public <E extends Enum<E>> E getEnum(Class<E> clazz, String key, E defaultValue) {
		return Conver.toEnum(clazz, this.get(key), defaultValue);
	}

	/**
	 * 获得JSONArray对象
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONArray类型，返回null
	 */
	public JSONArray getJSONArray(String key) {
		Object o = this.get(key);
		return o instanceof JSONArray ? (JSONArray) o : null;
	}

	/**
	 * 获得JSONObject对象
	 * @param key KEY
	 * @return JSONArray对象，如果值为null或者非JSONObject类型，返回null
	 */
	public JSONObject getJSONObject(String key) {
		Object object = this.get(key);
		return object instanceof JSONObject ? (JSONObject) object : null;
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
							this.map.put(key, JSONUtil.wrap(result));
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
	 * @param value An object which is the value. It should be of one of these types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL object.
	 * @return this.
	 * @throws JSONException If the value is non-finite number or if the key is null.
	 */
	public JSONObject put(String key, Object value) throws JSONException {
		if (key == null) {
			throw new NullPointerException("Null key.");
		}
		if (value != null) {
			JSONUtil.testValidity(value);
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
			if (map.containsKey(key)) {
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
	 * @param value An object which is the value. It should be of one of these types: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL object.
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
			ja.put(this.get(names.getStr(i)));
		}
		return ja;
	}
	
	/**
	 * 转为实体类对象
	 * @param clazz 实体类
	 * @return 实体类对象
	 */
	public <T> T toBean(Class<T> clazz){
		return BeanUtil.mapToBean(this.map, clazz);
	}
	
	/**
	 * 转为实体类对象
	 * @param bean 实体类
	 * @return 实体类对象
	 */
	public <T> T toBean(T bean){
		return BeanUtil.fillBeanWithMap(this.map, bean);
	}

	/**
	 * Make a JSON text of this JSONObject. For compactness, no whitespace is added. If this would not result in a syntactically correct JSON text, then null will be returned instead.
	 * <p>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @return a printable, displayable, portable, transmittable representation of the object, beginning with <code>{</code>&nbsp;<small>(left brace)</small> and ending with <code>}</code>&nbsp;
	 *         <small>(right brace)</small>.
	 */
	@Override
	public String toString() {
		try {
			return this.toJSONString(0);
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
	@Override
	public String toJSONString(int indentFactor) throws JSONException {
		StringWriter w = new StringWriter();
		synchronized (w.getBuffer()) {
			return this.write(w, indentFactor, 0).toString();
		}
	}

	@Override
	public Writer write(Writer writer) throws JSONException {
		return this.write(writer, 0, 0);
	}

	@Override
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		try {
			boolean commanate = false;
			final int length = this.length();
			Iterator<String> keys = this.keys();
			writer.write('{');

			if (length == 1) {
				Object key = keys.next();
				writer.write(JSONUtil.quote(key.toString()));
				writer.write(':');
				if (indentFactor > 0) {
					writer.write(' ');
				}
				JSONUtil.writeValue(writer, this.map.get(key), indentFactor, indent);
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
					JSONUtil.indent(writer, newindent);
					writer.write(JSONUtil.quote(key.toString()));
					writer.write(':');
					if (indentFactor > 0) {
						writer.write(' ');
					}
					JSONUtil.writeValue(writer, this.map.get(key), indentFactor, newindent);
					commanate = true;
				}
				if (indentFactor > 0) {
					writer.write('\n');
				}
				JSONUtil.indent(writer, indent);
			}
			writer.write('}');
			return writer;
		} catch (IOException exception) {
			throw new JSONException(exception);
		}
	}
}
