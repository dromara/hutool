package com.xiaoleilu.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.util.BeanUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * JSON对象<br>
 * 例：<br>
 *<pre>
 * myString = new JSONObject().put(&quot;JSON&quot;, &quot;Hello, World!&quot;).toString();
 * </pre>
 * @author looly
 */
public class JSONObject extends JSONGetter<String> implements JSON, Map<String, Object> {

	/**
	 * The map where the JSONObject's properties are kept.
	 */
	private final Map<String, Object> rawHashMap = new HashMap<>();

	/**
	 * 空构造
	 */
	public JSONObject() {
	}

	// -------------------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 使用其他<code>JSONObject</code>构造新的<code>JSONObject</code>，并只加入指定name对应的键值对。
	 *
	 * @param jsonObject A JSONObject.
	 * @param names 需要的name列表
	 */
	public JSONObject(JSONObject jsonObject, String... names) {
		for (String name : names) {
			try {
				this.putOnce(name, jsonObject.getObj(name));
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
		init(x);
	}

	/**
	 * 构建JSONObject，如果给定值为Map，将键值对加入JSON对象;<br>
	 * 如果为普通的JavaBean，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象<br>
	 * 例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"
	 * 
	 * @param source JavaBean或者Map对象或者String
	 */
	public JSONObject(Object source) {
		if (null != source) {
			if (source instanceof Map) {
				for (final Entry<?, ?> e : ((Map<?, ?>) source).entrySet()) {
					final Object value = e.getValue();
					if (value != null) {
						this.rawHashMap.put(Convert.toStr(e.getKey()), JSONUtil.wrap(value));
					}
				}
			}else if(source instanceof String){
				init((String)source);
			} else {
				this.populateMap(source);
			}
		}
	}

	/**
	 * Construct a JSONObject from an Object, using reflection to find the public members. The resulting JSONObject's keys will be the strings from the names array, and the values will be the field
	 * values associated with those keys in the object. If a key is not found or not visible, then it will not be copied into the new JSONObject.
	 *
	 * @param object An object that has fields that should be used to make a JSONObject.
	 * @param names An array of strings, the names of the fields to be obtained from the object.
	 */
	public JSONObject(Object object, String[] names) {
		Class<?> c = object.getClass();
		for (String name : names) {
			try {
				this.putOpt(name, c.getField(name).get(object));
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * 从JSON字符串解析为JSON对象
	 *
	 * @param source 以大括号 {} 包围的字符串，其中KEY和VALUE使用 : 分隔，每个键值对使用逗号分隔
	 * @exception JSONException JSON字符串语法错误
	 */
	public JSONObject(String source) throws JSONException {
		this(new JSONTokener(source));
	}
	// -------------------------------------------------------------------------------------------------------------------- Constructor end
	
	/**
	 * key对应值是否为<code>null</code>或无此key
	 *
	 * @param key 键
	 * @return true 无此key或值为<code>null</code>或{@link JSONNull#NULL}返回<code>false</code>，其它返回<code>true</code>
	 */
	public boolean isNull(String key) {
		return JSONNull.NULL.equals(this.getObj(key));
	}

	/**
	 * 将指定KEY列表的值组成新的JSONArray
	 *
	 * @param names KEY列表
	 * @return A JSONArray of values.
	 * @throws JSONException If any of the values are non-finite numbers.
	 */
	public JSONArray toJSONArray(Collection<String> names) throws JSONException {
		if (CollectionUtil.isEmpty(names)) {
			return null;
		}
		final JSONArray ja = new JSONArray();
		Object value;
		for (String name : names) {
			value = this.get(name);
			if(null != value){
				ja.put(value);
			}
		}
		return ja;
	}
	
	/**
	 * 转为实体类对象
	 * 
	 * @param clazz 实体类
	 * @return 实体类对象
	 */
	public <T> T toBean(Class<T> clazz) {
		return BeanUtil.mapToBean(this.rawHashMap, clazz, false);
	}

	/**
	 * 转为实体类对象
	 * 
	 * @param bean 实体类
	 * @return 实体类对象
	 */
	public <T> T toBean(T bean) {
		return BeanUtil.fillBeanWithMap(this.rawHashMap, bean, false);
	}
	
	@Override
	public int size() {
		return rawHashMap.size();
	}

	@Override
	public boolean isEmpty() {
		return rawHashMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return rawHashMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return rawHashMap.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return rawHashMap.get(key);
	}
	
	@Override
	public Object getObj(String key, Object defaultValue) {
		Object obj = this.rawHashMap.get(key);
		return null == obj ? defaultValue : obj;
	}

	/**
	 * PUT 键值对到JSONObject中，如果值为<code>null</code>，将此键移除
	 *
	 * @param key A key string.
	 * @param value 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	@Override
	public JSONObject put(String key, Object value) throws JSONException {
		if (key == null) {
			throw new NullPointerException("Null key.");
		}
		if (value != null) {
			JSONUtil.testValidity(value);
			this.rawHashMap.put(key, JSONUtil.wrap(value));
		} else {
			this.remove(key);
		}
		return this;
	}
	
	/**
	 * Put a key/value pair in the JSONObject, but only if the key and the value are both non-null, <br>
	 * and only if there is not already a member with that name.
	 * 一次性Put 键值对，如果key已经存在抛出异常，如果键值中有null值，忽略
	 *
	 * @param key 键
	 * @param value 值对象，可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this.
	 * @throws JSONException 值是无穷数字、键重复抛出异常
	 */
	public JSONObject putOnce(String key, Object value) throws JSONException {
		if (key != null && value != null) {
			if (rawHashMap.containsKey(key)) {
				throw new JSONException(StrUtil.format("Duplicate key \"{}\"", key));
			}
			this.put(key, value);
		}
		return this;
	}

	/**
	 * 在键和值都为非空的情况下put到JSONObject中
	 *
	 * @param key 键
	 * @param value 值对象，可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this.
	 * @throws JSONException 值是无穷数字
	 */
	public JSONObject putOpt(String key, Object value) throws JSONException {
		if (key != null && value != null) {
			this.put(key, value);
		}
		return this;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		rawHashMap.putAll(m);
	}
	
	/**
	 * 积累值。类似于put，当key对应value已经存在时，与value组成新的JSONArray. <br>
	 * 如果只有一个值，此值就是value，如果多个值，则是添加到新的JSONArray中
	 *
	 * @param key A key string.
	 * @param value An object to be accumulated under the key.
	 * @return this.
	 * @throws JSONException If the value is an invalid number or if the key is null.
	 */
	public JSONObject accumulate(String key, Object value) throws JSONException {
		JSONUtil.testValidity(value);
		Object object = this.getObj(key);
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
	 * 追加值，如果key无对应值，就添加一个JSONArray，其元素只有value，如果值已经是一个JSONArray，则添加到值JSONArray中。
	 *
	 * @param key A key string.
	 * @param value An object to be accumulated under the key.
	 * @return this.
	 * @throws JSONException 如果给定键为<code>null</code>或者键对应的值存在且为非JSONArray
	 */
	public JSONObject append(String key, Object value) throws JSONException {
		JSONUtil.testValidity(value);
		Object object = this.getObj(key);
		if (object == null) {
			this.put(key, new JSONArray().put(value));
		} else if (object instanceof JSONArray) {
			this.put(key, ((JSONArray) object).put(value));
		} else {
			throw new JSONException("JSONObject [" + key + "] is not a JSONArray.");
		}
		return this;
	}

	/**
	 * 对值加一，如果值不存在，赋值1，如果为数字类型，做加一操作
	 *
	 * @param key A key string.
	 * @return this.
	 * @throws JSONException If there is already a property with this name that is not an Integer, Long, Double, or Float.
	 */
	public JSONObject increment(String key) throws JSONException {
		Object value = this.getObj(key);
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
	
	@Override
	public Object remove(Object key) {
		return rawHashMap.remove(key);
	}

	@Override
	public void clear() {
		rawHashMap.clear();
	}
	
	@Override
	public Set<String> keySet() {
		return this.rawHashMap.keySet();
	}

	@Override
	public Collection<Object> values() {
		return rawHashMap.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return rawHashMap.entrySet();
	}
	
	/**
	 *JSON中的key以及对应的value相等则判定为与此对象相同
	 *
	 * @param other The other JSONObject
	 * @return true if they are equal
	 */
	@Override
	public boolean equals(Object other) {
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
				Object valueThis = this.getObj(name);
				Object valueOther = ((JSONObject) other).getObj(name);
				if (valueThis instanceof JSONObject) {
					if (!((JSONObject) valueThis).equals(valueOther)) {
						return false;
					}
				} else if (valueThis instanceof JSONArray) {
					if (!((JSONArray) valueThis).equals(valueOther)) {
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
	 * 格式化输出JSON字符串
	 *
	 * @param indentFactor 每层缩进空格数
	 * @return JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
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
			final int length = this.size();
			Iterator<String> keys = this.keySet().iterator();
			writer.write('{');

			if (length == 1) {
				Object key = keys.next();
				writer.write(JSONUtil.quote(key.toString()));
				writer.write(':');
				if (indentFactor > 0) {
					writer.write(' ');
				}
				JSONUtil.writeValue(writer, this.rawHashMap.get(key), indentFactor, indent);
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
					JSONUtil.writeValue(writer, this.rawHashMap.get(key), indentFactor, newindent);
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

	// ------------------------------------------------------------------------------------------------- Private method start
	/**
	 * Bean对象转Map
	 * 
	 * @param bean Bean对象
	 */
	private void populateMap(Object bean) {
		Class<?> clazz = bean.getClass();

		// If klass is a System class then set includeSuperClass to false.

		boolean includeSuperClass = clazz.getClassLoader() != null;

		Method[] methods = includeSuperClass ? clazz.getMethods() : clazz.getDeclaredMethods();
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
							this.rawHashMap.put(key, JSONUtil.wrap(result));
						}
					}
				}
			} catch (Exception ignore) {
			}
		}
	}
	
	/**
	 * 初始化
	 * @param source JSON字符串
	 */
	private void init(String source){
		init(new JSONTokener(source));
	}

	/**
	 * 初始化
	 * @param x JSONTokener
	 */
	private void init(JSONTokener x) {
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
	// ------------------------------------------------------------------------------------------------- Private method end
}
