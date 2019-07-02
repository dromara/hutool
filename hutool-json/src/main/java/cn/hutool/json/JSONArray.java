package cn.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.collection.ArrayIter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

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
public class JSONArray extends JSONGetter<Integer> implements JSON, List<Object>, RandomAccess {
	private static final long serialVersionUID = 2664900568717612292L;

	/** 默认初始大小 */
	private static final int DEFAULT_CAPACITY = 10;

	/** 持有原始数据的List */
	private final List<Object> rawList;
	/** 配置项 */
	private JSONConfig config;

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
	 * @param initialCapacity 初始大小
	 * @param config JSON配置项
	 * @since 4.1.19
	 */
	public JSONArray(int initialCapacity, JSONConfig config) {
		this.rawList = new ArrayList<Object>(initialCapacity);
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
		for (Object o : list) {
			this.add(o);
		}
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
		this(DEFAULT_CAPACITY, JSONConfig.create().setIgnoreNullValue(ignoreNullValue));
		init(object);
	}
	// -------------------------------------------------------------------------------------------------------------------- Constructor start
	
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
	 */
	public JSONArray put(Object value) {
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
		JSONObject jo = new JSONObject();
		for (int i = 0; i < names.size(); i += 1) {
			jo.put(names.getStr(i), this.getObj(i));
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
			if (other.rawList != null) {
				return false;
			}
		} else if (!rawList.equals(other.rawList)) {
			return false;
		}
		return true;
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
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		return (T[]) JSONConverter.toArray(this, a.getClass().getComponentType());
	}

	@Override
	public boolean add(Object e) {
		return this.rawList.add(JSONUtil.wrap(e, this.config.isIgnoreNullValue()));
	}

	@Override
	public Object remove(int index) {
		return index >= 0 && index < this.size() ? this.rawList.remove(index) : null;
	}

	@Override
	public boolean remove(Object o) {
		return rawList.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return rawList.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Object> c) {
		if (CollUtil.isEmpty(c)) {
			return false;
		}
		for (Object obj : c) {
			this.add(obj);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Object> c) {
		if (CollUtil.isEmpty(c)) {
			return false;
		}
		final ArrayList<Object> list = new ArrayList<>(c.size());
		for (Object object : c) {
			list.add(JSONUtil.wrap(object, this.config.isIgnoreNullValue()));
		}
		return rawList.addAll(index, list);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.rawList.removeAll(c);
	}

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
		return this.rawList.set(index, JSONUtil.wrap(element, this.config.isIgnoreNullValue()));
	}

	@Override
	public void add(int index, Object element) {
		if (index < 0) {
			throw new JSONException("JSONArray[" + index + "] not found.");
		}
		if (index < this.size()) {
			InternalJSONUtil.testValidity(element);
			this.rawList.add(index, JSONUtil.wrap(element, this.config.isIgnoreNullValue()));
		} else {
			while (index != this.size()) {
				this.add(JSONNull.NULL);
			}
			this.put(element);
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
	 * 转为JSON字符串，指定缩进值
	 *
	 * @param indentFactor 缩进值，既缩进空格数
	 * @return JSON字符串
	 * @throws JSONException JSON写入异常
	 */
	@Override
	public String toJSONString(int indentFactor) throws JSONException {
		StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			return this.write(sw, indentFactor, 0).toString();
		}
	}

	@Override
	public Writer write(Writer writer) throws JSONException {
		return this.write(writer, 0, 0);
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
			if(ObjectUtil.isNull(obj) && isIgnoreNullValue) {
				continue;
			}
			if (isFirst) {
				isFirst = false;
			}else {
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
	 * @param object 数组或集合或JSON数组字符串
	 * @throws JSONException 非数组或集合
	 */
	private void init(Object object) throws JSONException{
		if (object instanceof CharSequence) {
			// JSON字符串
			init((CharSequence) object);
		} else {
			Iterator<?> iter;
			if (object.getClass().isArray()) {// 数组
				iter = new ArrayIter<>(object);
			} else if (object instanceof Iterator<?>) {// Iterator
				iter = ((Iterator<?>) object);
			} else if (object instanceof Iterable<?>) {// Iterable
				iter = ((Iterable<?>) object).iterator();
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
			init(new JSONTokener(StrUtil.trim(source)));
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
