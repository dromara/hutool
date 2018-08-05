package cn.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import cn.hutool.core.bean.BeanDesc.PropDesc;
import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.CaseInsensitiveLinkedMap;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;

/**
 * JSON对象<br>
 * 例：<br>
 * 
 * <pre>
 * json = new JSONObject().put(&quot;JSON&quot;, &quot;Hello, World!&quot;).toString();
 * </pre>
 * 
 * @author looly
 */
public class JSONObject extends JSONGetter<String> implements JSON, Map<String, Object> {
	private static final long serialVersionUID = -330220388580734346L;

	/** 默认初始大小 */
	private static final int DEFAULT_CAPACITY = 16;

	/** JSON的KV持有Map */
	private final Map<String, Object> rawHashMap;
	/** 是否忽略空值 */
	private boolean ignoreNullValue = true;

	// -------------------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造，初始容量为 {@link #DEFAULT_CAPACITY}，KEY无序
	 */
	public JSONObject() {
		this(DEFAULT_CAPACITY, false);
	}

	/**
	 * 构造，初始容量为 {@link #DEFAULT_CAPACITY}
	 * 
	 * @param isOrder 是否有序
	 * @since 3.0.9
	 */
	public JSONObject(boolean isOrder) {
		this(DEFAULT_CAPACITY, isOrder);
	}

	/**
	 * 构造
	 * 
	 * @param capacity 初始大小
	 * @param isOrder 是否有序
	 * @since 3.0.9
	 */
	public JSONObject(int capacity, boolean isOrder) {
		this(capacity, false, isOrder);
	}

	/**
	 * 构造
	 * 
	 * @param capacity 初始大小
	 * @param isIgnoreCase 是否忽略KEY大小写
	 * @param isOrder 是否有序
	 * @since 3.3.1
	 */
	public JSONObject(int capacity, boolean isIgnoreCase, boolean isOrder) {
		if (isIgnoreCase) {
			this.rawHashMap = isOrder ? new CaseInsensitiveLinkedMap<String, Object>(capacity) : new CaseInsensitiveMap<String, Object>(capacity);
		} else {
			this.rawHashMap = isOrder ? new LinkedHashMap<String, Object>(capacity) : new HashMap<String, Object>(capacity);
		}
	}

	/**
	 * 使用其他<code>JSONObject</code>构造新的<code>JSONObject</code>，并只加入指定name对应的键值对。<br>
	 * 如果传入的name列表为空则加入所有键值对
	 *
	 * @param jsonObject A JSONObject.
	 * @param names 需要的name列表，如果为空加入所有键值对
	 */
	public JSONObject(JSONObject jsonObject, String... names) {
		this();
		if(ArrayUtil.isNotEmpty(names)) {
			for (String name : names) {
				try {
					this.putOnce(name, jsonObject.getObj(name));
				} catch (Exception ignore) {
				}
			}
		} else {
			this.putAll(jsonObject);
		}
	}
	
	/**
	 * 使用{@link JSONTokener}构建
	 *
	 * @param x {@link JSONTokener}
	 * @throws JSONException 语法错误
	 */
	public JSONObject(JSONTokener x) throws JSONException {
		this(x, false);
	}

	/**
	 * 使用{@link JSONTokener}构建
	 *
	 * @param x {@link JSONTokener}
	 * @param isOrder 是否有序
	 * @throws JSONException 语法错误
	 * @since 4.0.10
	 */
	public JSONObject(JSONTokener x, boolean isOrder) throws JSONException {
		this(isOrder);
		init(x);
	}

	/**
	 * 构建JSONObject，如果给定值为Map，将键值对加入JSON对象;<br>
	 * 如果为普通的JavaBean，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象<br>
	 * 例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"<br>
	 * 此方法会忽略空值，但是对JSON字符串不影响
	 * 
	 * @param source JavaBean或者Map对象或者String
	 */
	public JSONObject(Object source) {
		this(source, true);
	}

	/**
	 * 构建JSONObject，如果给定值为Map，将键值对加入JSON对象;<br>
	 * 如果为普通的JavaBean，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象<br>
	 * 例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"
	 * 
	 * @param source JavaBean或者Map对象或者String
	 * @param ignoreNullValue 是否忽略空值，如果source为JSON字符串，不忽略空值
	 * @since 3.0.9
	 */
	public JSONObject(Object source, boolean ignoreNullValue) {
		this(DEFAULT_CAPACITY, (source instanceof LinkedHashMap));
		this.ignoreNullValue = ignoreNullValue;
		if (null != source) {
			if (source instanceof Map) {
				for (final Entry<?, ?> e : ((Map<?, ?>) source).entrySet()) {
					final Object value = e.getValue();
					if (false == ignoreNullValue || value != null) {
						this.rawHashMap.put(Convert.toStr(e.getKey()), JSONUtil.wrap(value, ignoreNullValue));
					}
				}
			} else if (source instanceof CharSequence) {
				// 可能为JSON字符串
				init((CharSequence) source);
			} else if (source instanceof Number) {
				// ignore Number
			} else {
				// 普通Bean
				this.populateMap(source);
			}
		}
	}

	/**
	 * 使用反射方式获取public字段名和字段值，构建JSONObject对象<br>
	 * KEY或VALUE任意一个为null则不加入，字段不存在也不加入
	 *
	 * @param pojo 包含需要字段的Bean对象
	 * @param names 需要构建JSONObject的字段名列表
	 */
	public JSONObject(Object pojo, String[] names) {
		this();
		Class<?> c = pojo.getClass();
		Field field;
		for (String name : names) {
			try {
				field = c.getField(name);
				if (null != field) {
					this.putOpt(name, field.get(pojo));
				}
			} catch (Exception ignore) {
				// ignore
			}
		}
	}

	/**
	 * 从JSON字符串解析为JSON对象
	 *
	 * @param source 以大括号 {} 包围的字符串，其中KEY和VALUE使用 : 分隔，每个键值对使用逗号分隔
	 * @exception JSONException JSON字符串语法错误
	 */
	public JSONObject(CharSequence source) throws JSONException {
		this(new JSONTokener(StrUtil.str(source)));
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
			if (null != value) {
				ja.put(value);
			}
		}
		return ja;
	}

	/**
	 * 转为实体类对象，转换异常将被抛出
	 * 
	 * @param <T> Bean类型
	 * @param clazz 实体类
	 * @return 实体类对象
	 */
	public <T> T toBean(Class<T> clazz) {
		return toBean(clazz, false);
	}

	/**
	 * 转为实体类对象
	 * 
	 * @param <T> Bean类型
	 * @param clazz 实体类
	 * @param ignoreError 是否忽略转换错误
	 * @return 实体类对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T toBean(Class<T> clazz, boolean ignoreError) {
		if (Map.class.isAssignableFrom(clazz) || Object.class.equals(clazz)) {
			return (T) this;
		}
		return toBean(ReflectUtil.newInstance(clazz), ignoreError);
	}

	/**
	 * 转为实体类对象，转换异常将被抛出
	 * 
	 * @param <T> Bean类型
	 * @param type {@link Type}
	 * @return 实体类对象
	 * @since 3.0.8
	 */
	public <T> T toBean(Type type) {
		return toBean(type, false);
	}

	/**
	 * 转为实体类对象
	 * 
	 * @param <T> Bean类型
	 * @param type {@link Type}
	 * @param ignoreError 是否忽略转换错误
	 * @return 实体类对象
	 * @since 3.0.8
	 */
	@SuppressWarnings("unchecked")
	public <T> T toBean(Type type, boolean ignoreError) {
		if(Object.class.equals(type)) {
			return (T) this;
		}
		final Class<?> clazz = TypeUtil.getClass(type);
		if (null == clazz) {
			throw new IllegalArgumentException(StrUtil.format("Can not know Class of Type {} !", type));
		}
		return (T) toBean(ReflectUtil.newInstance(clazz), ignoreError);
	}

	/**
	 * 转为实体类对象，转换异常将被抛出
	 * 
	 * @param <T> Bean类型
	 * @param bean 实体类
	 * @return 实体类对象
	 */
	public <T> T toBean(T bean) {
		return toBean(bean, false);
	}

	/**
	 * 转为实体类对象
	 * 
	 * @param <T> Bean类型
	 * @param bean 实体类
	 * @param ignoreError 是否忽略转换错误
	 * @return 实体类对象
	 */
	public <T> T toBean(T bean, boolean ignoreError) {
		return InternalJSONUtil.toBean(this, bean, ignoreError);
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
	 * @param expression 表达式
	 * @return 对象
	 * @see BeanPath#get(Object)
	 * @deprecated 请使用{@link #getByPath(String)}
	 */
	@Override
	@Deprecated
	public Object getByExp(String expression) {
		return getByPath(expression);
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
	 * 获取表达式对应值后转换为对应类型的值
	 * 
	 * @param <T> 返回值类型
	 * @param expression 表达式
	 * @param resultType 返回值类型
	 * @return 对象
	 * @see BeanPath#get(Object)
	 * @since 3.1.0
	 * @deprecated 请使用{@link #getByPath(String, Class)}
	 */
	@Deprecated
	@Override
	public <T> T getByExp(String expression, Class<T> resultType) {
		return getByPath(expression, resultType);
	}

	@Override
	public Object getByPath(String expression) {
		return BeanPath.create(expression).get(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getByPath(String expression, Class<T> resultType) {
		return (T) InternalJSONUtil.jsonConvert(resultType, getByPath(expression), true);
	}
	
	@Override
	public void putByPath(String expression, Object value) {
		BeanPath.create(expression).set(this, value);
	}

	/**
	 * PUT 键值对到JSONObject中，如果值为<code>null</code>，将此键移除
	 *
	 * @param key 键
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
			InternalJSONUtil.testValidity(value);
			this.rawHashMap.put(key, JSONUtil.wrap(value, this.ignoreNullValue));
		} else {
			this.remove(key);
		}
		return this;
	}

	/**
	 * Put a key/value pair in the JSONObject, but only if the key and the value are both non-null, <br>
	 * and only if there is not already a member with that name. 一次性Put 键值对，如果key已经存在抛出异常，如果键值中有null值，忽略
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
		for (Entry<? extends String, ? extends Object> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 积累值。类似于put，当key对应value已经存在时，与value组成新的JSONArray. <br>
	 * 如果只有一个值，此值就是value，如果多个值，则是添加到新的JSONArray中
	 *
	 * @param key 键
	 * @param value 被积累的值
	 * @return this.
	 * @throws JSONException 如果给定键为<code>null</code>或者键对应的值存在且为非JSONArray
	 */
	public JSONObject accumulate(String key, Object value) throws JSONException {
		InternalJSONUtil.testValidity(value);
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
	 * @param key 键
	 * @param value 值
	 * @return this.
	 * @throws JSONException 如果给定键为<code>null</code>或者键对应的值存在且为非JSONArray
	 */
	public JSONObject append(String key, Object value) throws JSONException {
		InternalJSONUtil.testValidity(value);
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
	 * @throws JSONException 如果存在值非Integer, Long, Double, 或 Float.
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rawHashMap == null) ? 0 : rawHashMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final JSONObject other = (JSONObject) obj;
		if (rawHashMap == null) {
			if (other.rawHashMap != null) {
				return false;
			}
		} else if (!rawHashMap.equals(other.rawHashMap)) {
			return false;
		}
		return true;
	}

	/**
	 * 返回JSON字符串<br>
	 * 如果解析错误，返回<code>null</code>
	 *
	 * @return JSON字符串
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
	 * 格式化打印JSON，缩进为4个空格
	 * 
	 * @return 格式化后的JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 * @since 3.0.9
	 */
	@Override
	public String toStringPretty() throws JSONException {
		return this.toJSONString(4);
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
		final StringWriter w = new StringWriter();
		return this.write(w, indentFactor, 0).toString();
	}

	@Override
	public Writer write(Writer writer) throws JSONException {
		return this.write(writer, 0, 0);
	}

	@Override
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		boolean commanate = false;
		final int length = this.size();
		Iterator<String> keys = this.keySet().iterator();
		try {
			writer.write(StrUtil.C_DELIM_START);
			if (length == 1) {
				// 只有一对键值对
				Object key = keys.next();
				writer.write(JSONUtil.quote(key.toString()));
				writer.write(StrUtil.C_COLON);
				if (indentFactor > 0) {
					writer.write(StrUtil.C_SPACE);
				}
				InternalJSONUtil.writeValue(writer, this.rawHashMap.get(key), indentFactor, indent);
			} else if (length != 0) {
				final int newindent = indent + indentFactor;
				while (keys.hasNext()) {
					Object key = keys.next();
					if (commanate) {
						writer.write(StrUtil.C_COMMA);
					}
					if (indentFactor > 0) {
						writer.write(StrUtil.C_LF);
					}
					InternalJSONUtil.indent(writer, newindent);
					writer.write(JSONUtil.quote(key.toString()));
					writer.write(StrUtil.C_COLON);
					if (indentFactor > 0) {
						writer.write(StrUtil.C_SPACE);
					}
					InternalJSONUtil.writeValue(writer, this.rawHashMap.get(key), indentFactor, newindent);
					commanate = true;
				}
				if (indentFactor > 0) {
					writer.write(StrUtil.C_LF);
				}
				InternalJSONUtil.indent(writer, indent);
			}
			writer.write(StrUtil.C_DELIM_END);
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
	 * @param ignoreNullValue 是否忽略空值
	 */
	private void populateMap(Object bean) {
		final Collection<PropDesc> props = BeanUtil.getBeanDesc(bean.getClass()).getProps();

		Method getter;
		Object value;
		for (PropDesc prop : props) {
			// 得到property对应的getter方法
			getter = prop.getGetter();
			if (null == getter) {
				// 无Getter跳过
				continue;
			}

			// 只读取有getter方法的属性
			try {
				value = getter.invoke(bean);
			} catch (Exception ignore) {
				// 忽略读取失败的属性
				continue;
			}

			if (null == value && this.ignoreNullValue) {
				// 值为null且用户定义跳过则跳过
				continue;
			}

			if (value != bean) {
				// 防止循环引用
				this.rawHashMap.put(prop.getFieldName(), JSONUtil.wrap(value, this.ignoreNullValue));
			}
		}
	}

	/**
	 * 初始化
	 * 
	 * @param source JSON字符串
	 */
	private void init(CharSequence source) {
		init(new JSONTokener(source.toString()));
	}

	/**
	 * 初始化
	 * 
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
