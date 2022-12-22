package cn.hutool.json;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.lang.mutable.Mutable;
import cn.hutool.core.lang.mutable.MutableObj;
import cn.hutool.core.lang.mutable.MutablePair;
import cn.hutool.core.text.StrJoiner;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.serialize.JSONWriter;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

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

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_CAPACITY = 10;

	/**
	 * 持有原始数据的List
	 */
	private List<Object> rawList;
	/**
	 * 配置项
	 */
	private final JSONConfig config;

	// region Constructors

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
	 * @param config          JSON配置项
	 * @since 4.1.19
	 */
	public JSONArray(int initialCapacity, JSONConfig config) {
		this.rawList = new ArrayList<>(initialCapacity);
		this.config = ObjectUtil.defaultIfNull(config, JSONConfig::create);
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
	 * @param object          数组或集合或JSON数组字符串
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
	 * @param object     数组或集合或JSON数组字符串
	 * @param jsonConfig JSON选项
	 * @throws JSONException 非数组或集合
	 * @since 4.6.5
	 */
	public JSONArray(Object object, JSONConfig jsonConfig) throws JSONException {
		this(object, jsonConfig, null);
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
	 * @param object     数组或集合或JSON数组字符串
	 * @param jsonConfig JSON选项
	 * @param filter     键值对过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤
	 * @throws JSONException 非数组或集合
	 * @since 5.8.0
	 */
	public JSONArray(Object object, JSONConfig jsonConfig, Filter<Mutable<Object>> filter) throws JSONException {
		this(DEFAULT_CAPACITY, jsonConfig);
		ObjectMapper.of(object).map(this, filter);
	}
	// endregion

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
	 * JSONArray转为以{@code separator}为分界符的字符串
	 *
	 * @param separator 分界符
	 * @return a string.
	 * @throws JSONException If the array contains an invalid number.
	 */
	public String join(String separator) throws JSONException {
		return StrJoiner.of(separator)
				.append(this, InternalJSONUtil::valueToString).toString();
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
		return JSONConverter.jsonConvert(resultType, getByPath(expression), true);
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
	 * @see #set(int, Object)
	 */
	public JSONArray put(int index, Object value) throws JSONException {
		this.set(index, value);
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
		return addRaw(JSONUtil.wrap(e, this.config), null);
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

	/**
	 * 加入或者替换JSONArray中指定Index的值，如果index大于JSONArray的长度，将在指定index设置值，之前的位置填充JSONNull.Null
	 *
	 * @param index   位置
	 * @param element 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return 替换的值，即之前的值
	 */
	@Override
	public Object set(int index, Object element) {
		return set(index, element, null);
	}

	/**
	 * 加入或者替换JSONArray中指定Index的值，如果index大于JSONArray的长度，将在指定index设置值，之前的位置填充JSONNull.Null
	 *
	 * @param index   位置
	 * @param element 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @param filter  过滤器，可以修改值，key（index）无法修改
	 * @return 替换的值，即之前的值
	 * @since 5.8.0
	 */
	public Object set(int index, Object element, Filter<MutablePair<Integer, Object>> filter) {
		// 添加前置过滤，通过MutablePair实现过滤、修改键值对等
		if (null != filter) {
			final MutablePair<Integer, Object> pair = new MutablePair<>(index, element);
			if (filter.accept(pair)) {
				// 使用修改后的值
				element = pair.getValue();
			}
		}

		if (index >= size()) {
			add(index, element);
		}
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

	@Override
	public ListIterator<Object> listIterator() {
		return this.rawList.listIterator();
	}

	@Override
	public ListIterator<Object> listIterator(int index) {
		return this.rawList.listIterator(index);
	}

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
	 * @param <T>         元素类型
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

	/**
	 * 返回JSON字符串<br>
	 * 支持过滤器，即选择哪些字段或值不写出
	 *
	 * @param indentFactor 每层缩进空格数
	 * @param filter       过滤器，可以修改值，key（index）无法修改
	 * @return JSON字符串
	 * @since 5.7.15
	 */
	public String toJSONString(int indentFactor, Filter<MutablePair<Object, Object>> filter) {
		final StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			return this.write(sw, indentFactor, 0, filter).toString();
		}
	}

	@Override
	public Writer write(Writer writer, int indentFactor, int indent) throws JSONException {
		return write(writer, indentFactor, indent, null);
	}

	/**
	 * 将JSON内容写入Writer<br>
	 * 支持过滤器，即选择哪些字段或值不写出
	 *
	 * @param writer       writer
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param filter       过滤器，可以修改值，key（index）无法修改
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 * @since 5.7.15
	 */
	public Writer write(Writer writer, int indentFactor, int indent, Filter<MutablePair<Object, Object>> filter) throws JSONException {
		final JSONWriter jsonWriter = JSONWriter.of(writer, indentFactor, indent, config).beginArray();

		CollUtil.forEach(this, (value, index) -> jsonWriter.writeField(new MutablePair<>(index, value), filter));
		jsonWriter.end();
		// 此处不关闭Writer，考虑writer后续还需要填内容
		return writer;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		final JSONArray clone = (JSONArray) super.clone();
		clone.rawList = ObjectUtil.clone(this.rawList);
		return clone;
	}

	/**
	 * 原始添加，添加的对象不做任何处理
	 *
	 * @param obj    添加的对象
	 * @param filter 键值对过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤
	 * @return 是否加入成功
	 * @since 5.8.0
	 */
	protected boolean addRaw(Object obj, Filter<Mutable<Object>> filter) {
		// 添加前置过滤，通过MutablePair实现过滤、修改键值对等
		if (null != filter) {
			final Mutable<Object> mutable = new MutableObj<>(obj);
			if (filter.accept(mutable)) {
				// 使用修改后的值
				obj = mutable.get();
			}else{
				// 键值对被过滤
				return false;
			}
		}
		return this.rawList.add(obj);
	}
}
