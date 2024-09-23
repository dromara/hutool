/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.json.serializer.impl;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.map.MapWrapper;
import org.dromara.hutool.core.reflect.TypeUtil;
import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONArray;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.serializer.JSONContext;
import org.dromara.hutool.json.serializer.MatcherJSONDeserializer;
import org.dromara.hutool.json.serializer.MatcherJSONSerializer;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

/**
 * Iterator序列化器，将{@link Iterable}或{@link Iterator}转换为JSONArray
 *
 * @author Looly
 */
public class IterTypeAdapter implements MatcherJSONSerializer<Object>, MatcherJSONDeserializer<Object> {

	/**
	 * 单例
	 */
	public static final IterTypeAdapter INSTANCE = new IterTypeAdapter();

	@Override
	public boolean match(final Object bean, final JSONContext context) {
		if (bean instanceof MapWrapper) {
			return false;
		}
		return bean instanceof Iterable || bean instanceof Iterator;
	}

	@Override
	public boolean match(final JSON json, final Type deserializeType) {
		if (json instanceof JSONArray || json instanceof JSONObject) {
			final Class<?> rawType = TypeUtil.getClass(deserializeType);
			// 反序列化只支持到集合
			return Collection.class.isAssignableFrom(rawType);
		}
		return false;
	}

	@Override
	public JSON serialize(final Object bean, final JSONContext context) {
		final Iterator<?> iter;
		if (bean instanceof Iterator<?>) {// Iterator
			iter = ((Iterator<?>) bean);
		} else {// Iterable
			iter = ((Iterable<?>) bean).iterator();
		}

		final JSONArray json = context.getOrCreateArray();
		mapFromIterator(bean, iter, json);
		return json;
	}

	@Override
	public Object deserialize(final JSON json, final Type deserializeType) {
		final Class<?> collectionClass = TypeUtil.getClass(deserializeType);
		final Type elementType = TypeUtil.getTypeArgument(deserializeType);
		return deserialize(json, collectionClass, elementType);
	}

	/**
	 * 反序列化
	 *
	 * @param json            JSON
	 * @param collectionClass 集合类型
	 * @param elementType     元素类型
	 * @return 反序列化后的集合对象
	 */
	public Object deserialize(final JSON json, final Class<?> collectionClass, final Type elementType) {
		final Collection<?> result = CollUtil.create(collectionClass, TypeUtil.getClass(elementType));

		if (json instanceof JSONObject) {
			fill((JSONObject) json, result, elementType);
		} else {
			fill((JSONArray) json, result, elementType);
		}

		return result;
	}

	/**
	 * 从Iterator中读取数据，并添加到JSONArray中
	 *
	 * @param iter      {@link Iterator}
	 * @param jsonArray {@link JSONArray}
	 */
	private void mapFromIterator(final Object source, final Iterator<?> iter, final JSONArray jsonArray) {
		Object next;
		while (iter.hasNext()) {
			next = iter.next();
			// 检查循环引用
			if (next != source) {
				jsonArray.set(next);
			}
		}
	}

	/**
	 * 将JSONObject转换为集合
	 *
	 * @param json        JSONObject
	 * @param result      结果集合
	 * @param elementType 元素类型
	 */
	private void fill(final JSONObject json, final Collection<?> result, final Type elementType) {
		json.forEach((key, value)->{
			result.add(null == value ? null : value.toBean(elementType));
		});
	}

	/**
	 * 将JSONArray转换为集合
	 *
	 * @param json        JSONArray
	 * @param result      结果集合
	 * @param elementType 元素类型
	 */
	private void fill(final JSONArray json, final Collection<?> result, final Type elementType) {
		json.forEach((element)->{
			result.add(null == element ? null : element.toBean(elementType));
		});
	}
}
