/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.json;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListWrapper;
import org.dromara.hutool.core.lang.Validator;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.json.serializer.impl.ArrayTypeAdapter;
import org.dromara.hutool.json.serializer.impl.IterTypeAdapter;
import org.dromara.hutool.json.writer.JSONWriter;

import java.util.*;

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
public class JSONArray extends ListWrapper<JSON> implements JSON, JSONGetter<Integer>, RandomAccess {
	private static final long serialVersionUID = 2664900568717612292L;

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_CAPACITY = 10;

	private final JSONFactory factory;

	// region ----- Constructors

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
	 */
	public JSONArray(final int initialCapacity) {
		this(initialCapacity, JSONFactory.getInstance());
	}

	/**
	 * 构造<br>
	 * 默认使用{@link ArrayList} 实现
	 *
	 * @param config JSON配置项
	 * @since 4.6.5
	 */
	public JSONArray(final JSONConfig config) {
		this(DEFAULT_CAPACITY, config);
	}

	/**
	 * 构造<br>
	 * 默认使用{@link ArrayList} 实现
	 *
	 * @param initialCapacity 初始大小
	 * @param config          JSON配置项
	 */
	public JSONArray(final int initialCapacity, final JSONConfig config) {
		this(initialCapacity, JSONFactory.of(config, null));
	}

	/**
	 * 构造<br>
	 * 默认使用{@link ArrayList} 实现
	 *
	 * @param initialCapacity 初始大小
	 * @param factory         JSON工厂
	 */
	public JSONArray(final int initialCapacity, final JSONFactory factory) {
		super(new ArrayList<>(initialCapacity));
		this.factory = factory;
	}
	// endregion

	@Override
	public JSONFactory getFactory() {
		return this.factory;
	}

	@Override
	public JSON getJSON(final Integer key) {
		return this.raw.get(key);
	}

	/**
	 * Append an object value. This increases the array's length by one. <br>
	 * 加入元素，数组长度+1，等同于 {@link JSONArray#add(Object)}
	 *
	 * @param value 值，可以是： Boolean, Double, Integer, JSONArray, JSONObject, Long, or String, or the JSONNull.NULL。
	 * @return this.
	 */
	public JSONArray addObj(final Object value) {
		this.add(this.factory.getMapper().toJSON(value));
		return this;
	}

	/**
	 * 根据给定名列表，与其位置对应的值组成JSONObject
	 *
	 * @param names 名列表，位置与JSONArray中的值位置对应
	 * @return A JSONObject，无名或值返回null
	 * @throws JSONException 如果任何一个名为null
	 */
	public JSONObject toJSONObject(final JSONArray names) throws JSONException {
		if (names == null || names.size() == 0 || this.size() == 0) {
			return null;
		}
		final JSONObject jo = this.factory.ofObj();
		for (int i = 0; i < names.size(); i += 1) {
			jo.putObj(names.getStr(i), this.getObj(i));
		}
		return jo;
	}


	@Override
	public Iterator<JSON> iterator() {
		return raw.iterator();
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends JSON> c) {
		if (CollUtil.isEmpty(c)) {
			return false;
		}
		final List<JSON> list = new ArrayList<>(c.size());
		for (final JSON json : c) {
			if (null == json && config().isIgnoreNullValue()) {
				continue;
			}
			list.add(json);
		}
		return raw.addAll(index, list);
	}

	/**
	 * 加入或者替换JSONArray中指定Index的值，如果index大于JSONArray的长度，将在指定index设置值，之前的位置填充JSONNull.Null
	 *
	 * @param index   位置
	 * @param element 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this
	 */
	public JSONArray setObj(final int index, final Object element) {
		set(index, this.factory.getMapper().toJSON(element));
		return this;
	}

	/**
	 * 加入或者替换JSONArray中指定Index的值，如果index大于JSONArray的长度，将在指定index设置值，之前的位置填充JSONNull.Null
	 *
	 * @param index   位置
	 * @param element 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return 替换的值，即之前的值
	 */
	@Override
	public JSON set(final int index, final JSON element) {
		if (null == element && config().isIgnoreNullValue()) {
			return null;
		}

		// 越界则追加到指定位置
		final int size = size();
		if(index == size){
			add(element);
			return null;
		}
		if (index > size) {
			add(index, element);
			return null;
		}

		return this.raw.set(index, element);
	}

	@Override
	public boolean add(final JSON element) {
		if (null == element && config().isIgnoreNullValue()) {
			return false;
		}
		return super.add(element);
	}

	@Override
	public void add(int index, final JSON element) {
		final boolean ignoreNullValue = config().isIgnoreNullValue();
		if (null == element && ignoreNullValue) {
			return;
		}
		if (index < this.size()) {
			if (index < 0) {
				index = 0;
			}
			this.raw.add(index, element);
		} else {
			// issue#3286, 如果用户指定的index太大，容易造成Java heap space错误。
			if (!ignoreNullValue) {
				// issue#3286, 增加安全检查，最多增加10倍
				Validator.checkIndexLimit(index, (this.size() + 1) * 10);
				while (index != this.size()) {
					// 非末尾，则填充null
					this.add(null);
				}
			}
			this.add(element);
		}

	}

	@Override
	@SuppressWarnings({"unchecked"})
	public <T> T[] toArray(final T[] a) {
		return (T[]) ArrayTypeAdapter.INSTANCE.deserialize(this, a.getClass().getComponentType());
	}

	/**
	 * 转为Bean数组
	 *
	 * @param arrayClass 数组元素类型
	 * @return 实体类对象
	 */
	public Object toArray(final Class<?> arrayClass) {
		return ArrayTypeAdapter.INSTANCE.deserialize(this, arrayClass.getComponentType());
	}

	/**
	 * 转为{@link ArrayList}
	 *
	 * @param <T>         元素类型
	 * @param elementType 元素类型
	 * @return {@link ArrayList}
	 * @since 3.0.8
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> toList(final Class<T> elementType) {
		return (List<T>) IterTypeAdapter.INSTANCE.deserialize(this, ArrayList.class, elementType);
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
	public void write(final JSONWriter writer) throws JSONException {
		writer.beginArray();
		CollUtil.forEach(this, (index, value) -> writer.writeField(new MutableEntry<>(index, value)));
		writer.endArray();
	}
}
