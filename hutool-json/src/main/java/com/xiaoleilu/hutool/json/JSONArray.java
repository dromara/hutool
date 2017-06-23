package com.xiaoleilu.hutool.json;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.xiaoleilu.hutool.bean.BeanResolver;

/**
 * JSON数组<br>
 * JSON数组是表示中括号括住的数据表现形式<br>
 * 对应的JSON字符串格格式例如:
 * <pre>
 * ["a", "b", "c", 12]
 * </pre>
 * 
 * @author looly
 */
public class JSONArray extends JSONGetter<Integer> implements JSON, List<Object> {

	/** 持有原始数据的List */
	private final List<Object> rawList;

	// -------------------------------------------------------------------------------------------------------------------- Constructor start
	/**
	 * 构造<br>
	 * 默认使用{@link ArrayList} 实现
	 */
	public JSONArray() {
		this.rawList = new ArrayList<Object>();
	}

	/**
	 * 构造<br>
	 * 此构造初始化JSONArray的List实现，既传入的List类型是什么，JSONArray的类型就是什么
	 * 
	 * @param list 初始化的JSON数组
	 */
	public JSONArray(List<Object> list) {
		this.rawList = list;
	}

	/**
	 * 使用 {@link JSONTokener} 做为参数构造
	 *
	 * @param x A {@link JSONTokener}
	 * @throws JSONException If there is a syntax error.
	 */
	public JSONArray(JSONTokener x) throws JSONException {
		this();
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

	/**
	 * 从String构造（JSONArray字符串）
	 *
	 * @param source JSON数组字符串
	 * @throws JSONException If there is a syntax error.
	 */
	public JSONArray(String source) throws JSONException {
		this(new JSONTokener(source));
	}

	/**
	 * 从数组或{@link Collection}对象构造
	 *
	 * @param arrayOrCollection 数组或集合
	 * @throws JSONException 非数组或集合
	 */
	public JSONArray(Object arrayOrCollection) throws JSONException {
		this();
		if (arrayOrCollection.getClass().isArray()) {// 数组
			int length = Array.getLength(arrayOrCollection);
			for (int i = 0; i < length; i += 1) {
				this.put(JSONUtil.wrap(Array.get(arrayOrCollection, i)));
			}
		} else if (arrayOrCollection instanceof Iterable<?>) {// Iterable
			for (Object o : (Collection<?>) arrayOrCollection) {
				this.add(o);
			}
		} else {
			throw new JSONException("JSONArray initial value should be a string or collection or array.");
		}
	}
	// -------------------------------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 值是否为<code>null</code>
	 *
	 * @param index 值所在序列
	 * @return true if the value at the index is null, or if there is no value.
	 */
	public boolean isNull(int index) {
		return JSONNull.NULL.equals(this.getObj(index));
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
	public Object getByExp(String expression) {
		return BeanResolver.resolveBean(this, expression);
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
			boolean commanate = false;
			int length = this.size();
			writer.write('[');

			if (length == 1) {
				InternalJSONUtil.writeValue(writer, this.rawList.get(0), indentFactor, indent);
			} else if (length != 0) {
				final int newindent = indent + indentFactor;

				for (int i = 0; i < length; i += 1) {
					if (commanate) {
						writer.write(',');
					}
					if (indentFactor > 0) {
						writer.write('\n');
					}
					InternalJSONUtil.indent(writer, newindent);
					InternalJSONUtil.writeValue(writer, this.rawList.get(i), indentFactor, newindent);
					commanate = true;
				}
				if (indentFactor > 0) {
					writer.write('\n');
				}
				InternalJSONUtil.indent(writer, indent);
			}
			writer.write(']');
			return writer;
		} catch (IOException e) {
			throw new JSONException(e);
		}
	}

	@Override
	public Iterator<Object> iterator() {
		return rawList.iterator();
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
	public <T> T[] toArray(T[] a) {
		return rawList.toArray(a);
	}

	@Override
	public boolean add(Object e) {
		return this.rawList.add(JSONUtil.wrap(e));
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
		for (Object obj : c) {
			this.add(obj);
		}
		return rawList.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Object> c) {
		return rawList.addAll(index, c);
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
		return this.rawList.set(index, JSONUtil.wrap(element));
	}

	@Override
	public void add(int index, Object element) {
		if (index < 0) {
			throw new JSONException("JSONArray[" + index + "] not found.");
		}
		if (index < this.size()) {
			InternalJSONUtil.testValidity(element);
			this.rawList.set(index, JSONUtil.wrap(element));
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
	 * 转为Bean数组，转换异常将被抛出
	 * 
	 * @param clazz 数组元素类型
	 * @return 实体类对象
	 */
	public Object toArray(Class<?> clazz) {
		return toArray(clazz, false);
	}

	/**
	 * 转为Bean数组
	 * 
	 * @param arrayClass 数组元素类型
	 * @param ignoreError 是否忽略转换错误
	 * @return 实体类对象
	 */
	public Object[] toArray(Class<?> arrayClass, boolean ignoreError) {
		return InternalJSONUtil.toArray(this, arrayClass, ignoreError);
	}
}
