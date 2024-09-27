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

import org.dromara.hutool.core.bean.path.BeanPath;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.json.writer.JSONWriter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.function.Predicate;

/**
 * JSON树模型接口，表示树中的一个节点。实现包括：
 * <ul>
 *     <li>{@link JSONObject}表示键值对形式的节点</li>
 *     <li>{@link JSONArray}表示列表形式的节点</li>
 *     <li>{@link JSONPrimitive}表示数字、Boolean、字符串形式的节点</li>
 *     <li>{@code null}表示空节点</li>
 * </ul>
 *
 * @author Looly
 */
public interface JSON extends Serializable {

	/**
	 * 获取JSON配置
	 *
	 * @return {@link JSONConfig}
	 * @since 5.3.0
	 */
	JSONConfig config();

	/**
	 * JSON大小，对于JSONObject，是键值对的多少，JSONArray则是元素的个数，JSON原始数据为1
	 *
	 * @return 大小
	 */
	int size();

	/**
	 * 判断JSON是否为空，即大小为0
	 *
	 * @return 是否为空
	 */
	default boolean isEmpty() {
		return 0 == size();
	}

	/**
	 * 转为JSONObject
	 *
	 * @return JSONObject
	 */
	default JSONObject asJSONObject() {
		return (JSONObject) this;
	}

	/**
	 * 转为JSONArray
	 *
	 * @return JSONArray
	 */
	default JSONArray asJSONArray() {
		return (JSONArray) this;
	}

	/**
	 * 转为JSONPrimitive
	 *
	 * @return JSONPrimitive
	 */
	default JSONPrimitive asJSONPrimitive() {
		return (JSONPrimitive) this;
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * <p>
	 * 获取表达式对应值后转换为对应类型的值
	 *
	 * @param <T>        返回值类型
	 * @param expression 表达式
	 * @return 对象
	 * @see BeanPath#getValue(Object)
	 */
	default <T> T getObjByPath(final String expression) {
		return getByPath(expression, Object.class);
	}

	/**
	 * 通过表达式获取JSON中嵌套的对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
	 * 表达式栗子：
	 *
	 * <pre>
	 * persion
	 * persion.name
	 * persons[3]
	 * person.friends[5].name
	 * </pre>
	 * <p>
	 * 获取表达式对应值后转换为对应类型的值
	 *
	 * @param <T>        返回值类型
	 * @param expression 表达式
	 * @param resultType 返回值类型
	 * @return 对象
	 * @see BeanPath#getValue(Object)
	 */
	default <T> T getByPath(final String expression, final Type resultType) {
		final JSON json = getByPath(expression);
		if (null == json) {
			return null;
		}

		return json.toBean(resultType);
	}

	/**
	 * 通过表达式获取JSON中嵌套的JSON对象<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
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
	 * @return JSON对象
	 * @see BeanPath#getValue(Object)
	 */
	default JSON getByPath(final String expression) {
		return (JSON) JSONFactory.of(config(), null).ofBeanPath(expression).getValue(this);
	}

	/**
	 * 设置表达式指定位置（或filed对应）的值<br>
	 * 若表达式指向一个JSONArray则设置其坐标对应位置的值，若指向JSONObject则put对应key的值<br>
	 * 注意：如果为JSONArray，设置值下标小于其长度，将替换原有值，否则追加新值<br>
	 * <ol>
	 * <li>.表达式，可以获取Bean对象中的属性（字段）值或者Map中key对应的值</li>
	 * <li>[]表达式，可以获取集合等对象中对应index的值</li>
	 * </ol>
	 * <p>
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
	 * @param value      值
	 */
	default void putByPath(final String expression, final Object value) {
		JSONFactory.of(config(), null).ofBeanPath(expression).setValue(this, value);
	}

	/**
	 * 格式化打印JSON，缩进为4个空格
	 *
	 * @return 格式化后的JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 * @since 3.0.9
	 */
	default String toStringPretty() throws JSONException {
		return this.toJSONString(2);
	}

	/**
	 * 格式化输出JSON字符串
	 *
	 * @param indentFactor 每层缩进空格数
	 * @return JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 */
	default String toJSONString(final int indentFactor) throws JSONException {
		return toJSONString(indentFactor, null);
	}

	/**
	 * 格式化输出JSON字符串
	 *
	 * @param indentFactor 每层缩进空格数
	 * @param predicate    过滤器，用于过滤不需要的键值对
	 * @return JSON字符串
	 * @throws JSONException 包含非法数抛出此异常
	 */
	default String toJSONString(final int indentFactor, final Predicate<MutableEntry<Object, Object>> predicate) throws JSONException {
		final JSONWriter jsonWriter = JSONFactory.of(config(), predicate).ofWriter(new StringBuilder(), indentFactor);
		this.write(jsonWriter);
		return jsonWriter.toString();
	}

	/**
	 * 将JSON内容写入Writer<br>
	 * Warning: This method assumes that the data structure is acyclical.
	 *
	 * @param writer writer
	 * @throws JSONException JSON相关异常
	 */
	void write(JSONWriter writer) throws JSONException;

	/**
	 * 转为实体类对象
	 *
	 * @param <T>  Bean类型
	 * @param type {@link Type}
	 * @return 实体类对象
	 */
	default <T> T toBean(final Type type) {
		return JSONFactory.of(config(), null).toBean(this, type);
	}
}
