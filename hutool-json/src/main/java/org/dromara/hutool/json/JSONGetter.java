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

import org.dromara.hutool.core.lang.getter.TypeGetter;
import org.dromara.hutool.core.util.ObjUtil;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 用于JSON的Getter类，提供各种类型的Getter方法
 *
 * @param <K> Key类型
 * @author Looly
 */
public interface JSONGetter<K> extends TypeGetter<K> {

	/**
	 * 获取JSON工厂
	 * @return JSON工厂
	 */
	JSONFactory getFactory();

	/**
	 * key对应值是否为{@code null}或无此key
	 *
	 * @param key 键
	 * @return true 无此key或值为{@code null}返回{@code false}，其它返回{@code true}
	 */
	default boolean isNull(final K key) {
		return ObjUtil.isNull(this.getJSON(key));
	}

	/**
	 * 获取字符串类型值，并转义不可见字符，如'\n'换行符会被转义为字符串"\n"
	 *
	 * @param key 键
	 * @return 字符串类型值
	 * @since 4.2.2
	 */
	default String getStrEscaped(final K key) {
		return getStrEscaped(key, null);
	}

	/**
	 * 获取字符串类型值，并转义不可见字符，如'\n'换行符会被转义为字符串"\n"
	 *
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 字符串类型值
	 * @since 4.2.2
	 */
	default String getStrEscaped(final K key, final String defaultValue) {
		return InternalJSONUtil.escape(getStr(key, defaultValue));
	}

	/**
	 * 获得JSONArray对象<br>
	 * 如果值为其它类型对象，尝试转换为{@link JSONArray}返回，否则抛出异常
	 *
	 * @param key KEY
	 * @return JSONArray对象，如果值为{@code null}，返回{@code null}，非JSONArray类型，尝试转换，转换失败抛出异常
	 */
	default JSONArray getJSONArray(final K key) {
		final JSON json = getJSON(key);
		if (null == json) {
			return null;
		}

		if (json instanceof JSONObject) {
			return getFactory().parseArray(json);
		}

		return json.asJSONArray();
	}

	/**
	 * 获得JSONObject对象<br>
	 * 如果值为其它类型对象，尝试转换为{@link JSONObject}返回，否则抛出异常
	 *
	 * @param key KEY
	 * @return JSONObject对象，如果值为{@code null}，返回{@code null}，非JSONObject类型，尝试转换，转换失败抛出异常
	 */
	default JSONObject getJSONObject(final K key) {
		final JSON json = getJSON(key);
		if (null == json) {
			return null;
		}

		return json.asJSONObject();
	}

	/**
	 * 从JSON中直接获取Bean的List列表<br>
	 * 先获取JSONArray对象，然后转为Bean的List
	 *
	 * @param <T>      Bean类型
	 * @param key      KEY
	 * @param beanType Bean类型
	 * @return Bean的List，如果值为null或者非JSONObject类型，返回null
	 * @since 5.7.20
	 */
	default <T> List<T> getBeanList(final K key, final Class<T> beanType) {
		final JSONArray jsonArray = getJSONArray(key);
		return (null == jsonArray) ? null : jsonArray.toList(beanType);
	}

	@Override
	default Object getObj(final K key, final Object defaultValue) {
		final Object value;
		final JSON json = getJSON(key);
		if (json instanceof JSONPrimitive) {
			value = ((JSONPrimitive) json).getValue();
		} else {
			value = json;
		}
		return ObjUtil.defaultIfNull(value, defaultValue);
	}

	@Override
	default <T> T get(final K key, final Type type, final T defaultValue) {
		final JSON value = getJSON(key);
		if (ObjUtil.isNull(value)) {
			return defaultValue;
		}

		return ObjUtil.defaultIfNull(value.toBean(type), defaultValue);
	}

	/**
	 * 获取JSON对象<br>
	 * 在JSON树模型中，JSON的节点都以JSON格式存储，所有get方法都基于此方法
	 *
	 * @param key KEY
	 * @return JSON对象
	 */
	JSON getJSON(final K key);
}
