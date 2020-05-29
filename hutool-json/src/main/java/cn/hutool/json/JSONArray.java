package cn.hutool.json;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.collection.ArrayIter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.json.serialize.GlobalSerializeMapping;
import cn.hutool.json.serialize.JSONSerializer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import static cn.hutool.json.JSONConverter.jsonConvert;

/**
 * JSON数组<br>
 * JSON数组是表示中括号括住的数据表现形式<br>
 * 对应的JSON字符串格格式例如:
 * 
 * <pre>
 * ["a", "b", "c", 12]
 * </pre>
 * 
 * @author looly
 */
public class JSONArray implements JSON, JSONGetter<Integer>, List<Object>, RandomAccess {
	private static final long serialVersionUID = 2664900568717612292L;

	/** 默认初始大小 */
	public static final int DEFAULT_CAPACITY = 10;

	/** 持有原始数据的List */
	private final List<Object> rawList;
	/** 配置项 */
	private final JSONConfig config;

	// -------------------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造<br>
	 * 默认使用{@link ArrayList} 实现
	 */
	public JSONArray() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * 构造<br>
	 * 默认使用{@link ArrayList} 实现
	 * 
	 * @param initialCapacity 初始大小
	 * @since 3.2.2
	 */
	public JSONArray(int initialCapacity) {
		this(initialCapacity, JSONConfig.create());
	}

	/**
	 * 构造<br>
	 * 默认使用{@link ArrayList} 实现
	 * 
	 * @param config JSON配置项
	 * @since 4.6.5
	 */
	public JSONArray(JSONConfig config) {
		this(DEFAULT_CAPACITY, config);
	}

	/**
	 * 构造<br>
	 * 默认使用{@link ArrayList} 实现
	 * 
	 * @param initialCapacity 初始大小
	 * @param config JSON配置项
	 * @since 4.1.19
	 */
	public JSONArray(int initialCapacity, JSONConfig config) {
		this.rawList = new ArrayList<>(initialCapacity);
		this.config = config;
	}

	/**
	 * 构造<br>
	 * 将参数数组中的元素转换为JSON对应的对象加入到JSONArray中
	 * 
	 * @param list 初始化的JSON数组
	 */
	public JSONArray(Iterable<Object> list) {
		this();
		for (Object o : list) {
			this.add(o);
		}
	}

	/**
	 * 构造<br>
	 * 将参数数组中的元素转换为JSON对应的对象加入到JSONArray中
	 * 
	 * @param list 初始化的JSON数组
	 */
	public JSONArray(Collection<Object> list) {
		this(list.size());
		this.addAll(list);
	}

	/**
	 * 使用 {@link JSONTokener} 做为参数构造
	 *
	 * @param x A {@link JSONTokener}
	 * @throws JSONException If there is a syntax error.
	 */
	public JSONArray(JSONTokener x) throws JSONException {
		this();
		init(x);
	}

	/**
	 * 从String构造（JSONArray字符串）
	 *
	 * @param source JSON数组字符串
	 * @throws JSONException If there is a syntax error.
	 */
	public JSONArray(CharSequence source) throws JSONException {
		this();
		init(source);
	}

	/**
	 * 从对象构造，忽略{@code null}的值<br>
	 * 支持以下类型的参数：
	 * 
	 * <pre>
	 * 1. 数组
	 * 2. {@link Iterable}对象
	 * 3. JSON数组字符串
	 * </pre>
	 *
	 * @param object 数组或集合或JSON数组字符串
	 * @throws JSONException 非数组或集合
	 */
	public JSONArray(Object object) throws JSONException {
		this(object, true);
	}

	/**
	 * 从对象构造<br>
	 * 支持以下类型的参数：
	 * 
	 * <pre>
	 * 1. 数组
	 * 2. {@link Iterable}对象
	 * 3. JSON数组字符串
	 * </pre>
	 *
	 * @param object 数组或集合或JSON数组字符串
	 * @param ignoreNullValue 是否忽略空值
	 * @throws JSONException 非数组或集合
	 */
	public JSONArray(Object object, boolean ignoreNullValue) throws JSONException {
		this(object, JSONConfig.create().setIgnoreNullValue(ignoreNullValue));
	}

	/**
	 * 从对象构造<br>
	 * 支持以下类型的参数：
	 * 
	 * <pre>
	 * 1. 数组
	 * 2. {@link Iterable}对象
	 * 3. JSON数组字符串
	 * </pre>
	 *
	 * @param object 数组或集合或JSON数组字符串
	 * @param jsonConfig JSON选项
	 * @throws JSONException 非数组或集合
	 * @since 4.6.5
	 */
	public JSONArray(Object object, JSONConfig jsonConfig) throws JSONException {
		this(DEFAULT_CAPACITY, jsonConfig);
		init(object);
	}
	// -------------------------------------------------------------------------------------------------------------------- Constructor start

	@Override
	public JSONConfig getConfig() {
		return this.config;
	}

	/**
	 * 设置转为字符串时的日期格式，默认为时间戳（null值）
	 * 
	 * @param format 格式，null表示使用时间戳
	 * @return this
	 * @since 4.1.19
	 */
	public JSONArray setDateFormat(String format) {
		this.config.setDateFormat(format);
		return this;
	}

	/**
	 * JSONArray转为以<code>separator</code>为分界符的字符串
	 *
	 * @param separator 分界符
	 * @return a string.
	 * @throws JSONException If the array contains an invalid number.
	 */
	public String join(String separator) throws JSONException {
		int len = this.rawList.size();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < len; i += 1) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(InternalJSONUtil.valueToString(this.rawList.get(i)));
		}
		return sb.toString();
	}

	@Override
	public Object get(int index) {
		return this.rawList.get(index);
	}

	@Override
	public Object getObj(Integer index, Object defaultValue) {
		return (index < 0 || index >= this.size()) ? defaultValue : this.rawList.get(index);
	}

	@Override
	public Object getByPath(String expression) {
		return BeanPath.create(expression).get(this);
	}

	@Override
	public <T> T getByPath(String expression, Class<T> resultType) {
		return jsonConvert(resultType, getByPath(expression), true);
	}

	@Override
	public void putByPath(String expression, Object value) {
		BeanPath.create(expression).set(this, value);
	}

	/**
	 * Append an object value. This increases the array's length by one. <br>
	 * 加入元素，数组长度+1，等同于 {@link JSONArray#add(Object)}
	 *
	 * @param value 值，可以是： Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONNull.NULL。
	 * @return this.
	 * @see #set(Object)
	 */
	public JSONArray put(Object value) {
		return set(value);
	}

	/**
	 * Append an object value. This increases the array's length by one. <br>
	 * 加入元素，数组长度+1，等同于 {@link JSONArray#add(Object)}
	 *
	 * @param value 值，可以是： Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONNull.NULL。
	 * @return this.
	 * @since 5.2.5
	 */
	public JSONArray set(Object value) {
		this.add(value);
		return this;
	}

	/**
	 * 加入或者替换JSONArray中指定Index的值，如果index大于JSONArray的长度，将在指定index设置值，之前的位置填充JSONNull.Null
	 *
	 * @param index 位置
	 * @param value 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this.
	 * @throws JSONException index &lt; 0 或者非有限的数字
	 */
	public JSONArray put(int index, Object value) throws JSONException {
		this.add(index, value);
		return this;
	}

	/**
	 * 根据给定名列表，与其位置对应的值组成JSONObject
	 *
	 * @param names 名列表，位置与JSONArray中的值位置对应
	 * @return A JSONObject，无名或值返回null
	 * @throws JSONException 如果任何一个名为null
	 */
	public JSONObject toJSONObject(JSONArray names) throws JSONException {
		if (names == null || names.size() == 0 || this.size() == 0) {
			return null;
		}
		final JSONObject jo = new JSONObject(this.config);
		for (int i = 0; i < names.size(); i += 1) {
			jo.set(names.getStr(i), this.getObj(i));
		}
		return jo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rawList == null) ? 0 : rawList.hashCode());
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
		final JSONArray other = (JSONArray) obj;
		if (rawList == null) {
			return other.rawList == null;
		} else {
			return rawList.equals(other.rawList);
		}
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public Iterator<Object> iterator() {
		return rawList.iterator();
	}

	/**
	 * 当此JSON列表的每个元素都是一个JSONObject时，可以调用此方法返回一个Iterable，便于使用foreach语法遍历
	 * 
	 * @return Iterable
	 * @since 4.0.12
	 */
	public Iterable<JSONObject> jsonIter() {
		return new JSONObjectIter(iterator());
	}

	@Override
	public int size() {
		return rawList.size();
	}

	@Override
	public boolean isEmpty() {
		return rawList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return rawList.contains(o);
	}

	@Override
	public Object[] toArray() {
		return rawList.toArray();
	}

	@Override
	@SuppressWarnings({"unchecked"})
	public <T> T[] toArray(T[] a) {
		return (T[]) JSONConverter.toArray(this, a.getClass().getComponentType());
	}

	@Override
	public boolean add(Object e) {
		return this.rawList.add(JSONUtil.wrap(e, this.config));
	}

	@Override
	public Object remove(int index) {
		return index >= 0 && index < this.size() ? this.rawList.remove(index) : null;
	}

	@Override
	public boolean remove(Object o) {
		return rawList.remove(o);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean containsAll(Collection<?> c) {
		return rawList.containsAll(c);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean addAll(Collection<?> c) {
		if (CollUtil.isEmpty(c)) {
			return false;
		}
		for (Object obj : c) {
			this.add(obj);
		}
		return true;
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean addAll(int index, Collection<?> c) {
		if (CollUtil.isEmpty(c)) {
			return false;
		}
		final ArrayList<Object> list = new ArrayList<>(c.size());
		for (Object object : c) {
			list.add(JSONUtil.wrap(object, this.config));
		}
		return rawList.addAll(index, list);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean removeAll(Collection<?> c) {
		return this.rawList.removeAll(c);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public boolean retainAll(Collection<?> c) {
		return this.rawList.retainAll(c);
	}

	@Override
	public void clear() {
		this.rawList.clear();

	}

	@Override
	public Object set(int index, Object element) {
		return this.rawList.set(index, JSONUtil.wrap(element, this.config));
	}

	@Override
	public void add(int index, Object element) {
		if (index < 0) {
			throw new JSONException("JSONArray[{}] not found.", index);
		}
		if (index < this.size()) {
			InternalJSONUtil.testValidity(element);
			this.rawList.add(index, JSONUtil.wrap(element, this.config));
		} else {
			while (index != this.size()) {
				this.add(JSONNull.NULL);
			}
			this.set(element);
		}

	}

	@Override
	public int indexOf(Object o) {
		return this.rawList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.rawList.lastIndexOf(o);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public ListIterator<Object> listIterator() {
		return this.rawList.listIterator();
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public ListIterator<Object> listIterator(int index) {
		return this.rawList.listIterator(index);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public List<Object> subList(int fromIndex, int toIndex) {
		return this.rawList.subList(fromIndex, toIndex);
	}

	/**
	 * 转为Bean数组
	 * 
	 * @param arrayClass 数组元素类型
	 * @return 实体类对象
	 */
	public Object toArray(Class<?> arrayClass) {
		return JSONConverter.toArray(this, arrayClass);
	}

	/**
	 * 转为{@link ArrayList}
	 * 
	 * @param <T> 元素类型
	 * @param elementType 元素类型
	 * @return {@link ArrayList}
	 * @since 3.0.8
	 */
	public <T> List<T> toList(Class<T> elementType) {
		return JSONConverter.toList(this, elementType);
	}

	/**
	 * 转为JSON字符串，无缩进
	 *
	 * @return JSONArray字符串
	 */
	@Override
	public String toString() {
		return this.toJSONString(0);
	}

	@Override
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		try {
			return doWrite(writer, indentFactor, indent);
		} catch (IOException e) {
			throw new JSONException(e);
		}
	}

	// ------------------------------------------------------------------------------------------------- Private method start

	/**
	 * 将JSON内容写入Writer
	 * 
	 * @param writer writer
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent 本级别缩进量
	 * @return Writer
	 * @throws IOException IO相关异常
	 */
	private Writer doWrite(Writer writer, int indentFactor, int indent) throws IOException {
		writer.write(CharUtil.BRACKET_START);
		final int newindent = indent + indentFactor;
		final boolean isIgnoreNullValue = this.config.isIgnoreNullValue();
		boolean isFirst = true;
		for (Object obj : this.rawList) {
			if (ObjectUtil.isNull(obj) && isIgnoreNullValue) {
				continue;
			}
			if (isFirst) {
				isFirst = false;
			} else {
				writer.write(CharUtil.COMMA);
			}

			if (indentFactor > 0) {
				writer.write(CharUtil.LF);
			}
			InternalJSONUtil.indent(writer, newindent);
			InternalJSONUtil.writeValue(writer, obj, indentFactor, newindent, this.config);
		}

		if (indentFactor > 0) {
			writer.write(CharUtil.LF);
		}
		InternalJSONUtil.indent(writer, indent);
		writer.write(CharUtil.BRACKET_END);
		return writer;
	}

	/**
	 * 初始化
	 * 
	 * @param source 数组或集合或JSON数组字符串
	 * @throws JSONException 非数组或集合
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void init(Object source) throws JSONException {
		if (null == source) {
			return;
		}

		final JSONSerializer serializer = GlobalSerializeMapping.getSerializer(source.getClass());
		if (null != serializer && JSONArray.class.equals(TypeUtil.getTypeArgument(serializer.getClass()))) {
			// 自定义序列化
			serializer.serialize(this, source);
		} else if (source instanceof CharSequence) {
			// JSON字符串
			init((CharSequence) source);
		}else if (source instanceof JSONTokener) {
			init((JSONTokener) source);
		} else {
			Iterator<?> iter;
			if (ArrayUtil.isArray(source)) {// 数组
				iter = new ArrayIter<>(source);
			} else if (source instanceof Iterator<?>) {// Iterator
				iter = ((Iterator<?>) source);
			} else if (source instanceof Iterable<?>) {// Iterable
				iter = ((Iterable<?>) source).iterator();
			} else {
				throw new JSONException("JSONArray initial value should be a string or collection or array.");
			}
			while (iter.hasNext()) {
				this.add(iter.next());
			}
		}
	}

	/**
	 * 初始化
	 * 
	 * @param source JSON字符串
	 */
	private void init(CharSequence source) {
		if (null != source) {
			init(new JSONTokener(StrUtil.trim(source), this.config));
		}
	}

	/**
	 * 初始化
	 * 
	 * @param x {@link JSONTokener}
	 */
	private void init(JSONTokener x) {
		if (x.nextClean() != '[') {
			throw x.syntaxError("A JSONArray text must start with '['");
		}
		if (x.nextClean() != ']') {
			x.back();
			for (;;) {
				if (x.nextClean() == ',') {
					x.back();
					this.rawList.add(JSONNull.NULL);
				} else {
					x.back();
					this.rawList.add(x.nextValue());
				}
				switch (x.nextClean()) {
				case ',':
					if (x.nextClean() == ']') {
						return;
					}
					x.back();
					break;
				case ']':
					return;
				default:
					throw x.syntaxError("Expected a ',' or ']'");
				}
			}
		}
	}
	// ------------------------------------------------------------------------------------------------- Private method end
}
