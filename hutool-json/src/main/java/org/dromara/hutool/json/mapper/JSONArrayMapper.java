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

package org.dromara.hutool.json.mapper;

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.collection.iter.ArrayIter;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.JSONArray;
import org.dromara.hutool.json.JSONConfig;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.reader.JSONParser;
import org.dromara.hutool.json.reader.JSONTokener;
import org.dromara.hutool.json.serializer.JSONSerializer;
import org.dromara.hutool.json.serializer.SerializerManager;
import org.dromara.hutool.json.serializer.SimpleJSONContext;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * 对象和JSONArray映射器，用于转换对象为JSONArray，支持：
 * <ul>
 *     <li>CharSequence 转 JSONArray，使用JSONTokener解析</li>
 *     <li>{@link Reader} 转 JSONArray，使用JSONTokener解析</li>
 *     <li>{@link InputStream} 转 JSONArray，使用JSONTokener解析</li>
 *     <li>JSONTokener 转 JSONArray，直接解析</li>
 *     <li>Iterable 转 JSONArray</li>
 *     <li>Iterator 转 JSONArray</li>
 *     <li>数组 转 JSONArray</li>
 * </ul>
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONArrayMapper {
	/**
	 * 创建ArrayMapper
	 *
	 * @param source    来源对象
	 * @param predicate 值过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤，，{@link Predicate#test(Object)}为{@code true}保留
	 * @return ObjectMapper
	 */
	public static JSONArrayMapper of(final Object source, final Predicate<MutableEntry<Object, Object>> predicate) {
		return new JSONArrayMapper(source, predicate);
	}

	private final Object source;
	private final Predicate<MutableEntry<Object, Object>> predicate;

	/**
	 * 构造
	 *
	 * @param source    来源对象
	 * @param predicate 值过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null}表示不过滤，，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public JSONArrayMapper(final Object source, final Predicate<MutableEntry<Object, Object>> predicate) {
		this.source = source;
		this.predicate = predicate;
	}

	/**
	 * 将给定对象转换为{@link JSONArray}
	 *
	 * @param jsonArray 目标{@link JSONArray}
	 * @throws JSONException 非数组或集合
	 */
	public void mapTo(final JSONArray jsonArray) throws JSONException {
		final Object source = this.source;
		if (null == source) {
			return;
		}

		// 自定义序列化
		final JSONSerializer<Object> serializer = SerializerManager.getInstance().getSerializer(source.getClass());
		if (null != serializer) {
			serializer.serialize(source, new SimpleJSONContext(jsonArray));
			return;
		}

		if (source instanceof JSONTokener) {
			mapFromTokener((JSONTokener) source, JSONConfig.of(), jsonArray);
		}if (source instanceof JSONParser) {
			((JSONParser)source).parseTo(jsonArray);
		} else if (source instanceof CharSequence) {
			// JSON字符串
			mapFromStr((CharSequence) source, jsonArray);
		} else if (source instanceof Reader) {
			mapFromTokener(new JSONTokener((Reader) source), jsonArray.config(), jsonArray);
		} else if (source instanceof InputStream) {
			mapFromTokener(new JSONTokener((InputStream) source), jsonArray.config(), jsonArray);
		} else if (source instanceof byte[]) {
			final byte[] bytesSource = (byte[]) source;
			if ('[' == bytesSource[0] && ']' == bytesSource[bytesSource.length - 1]) {
				mapFromTokener(new JSONTokener(IoUtil.toStream(bytesSource)), jsonArray.config(), jsonArray);
			} else {
				// https://github.com/dromara/hutool/issues/2369
				// 非标准的二进制流，则按照普通数组对待
				for (final byte b : bytesSource) {
					jsonArray.add(b);
				}
			}
		} else {
			final Iterator<?> iter;
			if (ArrayUtil.isArray(source)) {// 数组
				iter = new ArrayIter<>(source);
			} else if (source instanceof Iterator<?>) {// Iterator
				iter = ((Iterator<?>) source);
			} else if (source instanceof Iterable<?>) {// Iterable
				iter = ((Iterable<?>) source).iterator();
			} else {
				if (!jsonArray.config().isIgnoreError()) {
					throw new JSONException("Unsupported [{}] to JSONArray", source.getClass());
				}
				// 如果用户选择跳过异常，则跳过此值转换
				return;
			}

			Object next;
			while (iter.hasNext()) {
				next = iter.next();
				// 检查循环引用
				if (next != source) {
					if(null != this.predicate){
						final MutableEntry<Object, Object> entry = MutableEntry.of(jsonArray.size(), next);
						if (predicate.test(entry)) {
							// 使用修改后的键值对
							next = entry.getValue();
							jsonArray.add(next);
						}
					}else {
						jsonArray.add(next);
					}
				}
			}
		}
	}

	/**
	 * 初始化
	 *
	 * @param source    JSON字符串
	 * @param jsonArray {@link JSONArray}
	 */
	private void mapFromStr(final CharSequence source, final JSONArray jsonArray) {
		if (null != source) {
			mapFromTokener(new JSONTokener(StrUtil.trim(source)), jsonArray.config(), jsonArray);
		}
	}

	/**
	 * 初始化
	 *
	 * @param x         {@link JSONTokener}
	 * @param jsonArray {@link JSONArray}
	 */
	private void mapFromTokener(final JSONTokener x, final JSONConfig config, final JSONArray jsonArray) {
		JSONParser.of(x, config).setPredicate(this.predicate).parseTo(jsonArray);
	}
}
