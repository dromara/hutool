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

package org.dromara.hutool.json;

import org.dromara.hutool.core.bean.path.BeanPath;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.reader.JSONParser;
import org.dromara.hutool.json.reader.JSONTokener;
import org.dromara.hutool.json.serializer.JSONMapper;
import org.dromara.hutool.json.support.JSONNodeBeanFactory;
import org.dromara.hutool.json.writer.JSONWriter;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.function.Predicate;

/**
 * JSON工厂类，用于JSON创建、解析、转换为Bean等功能
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONFactory {

	/**
	 * 单例
	 */
	private static class InstanceHolder {
		public static final JSONFactory INSTANCE = of(JSONConfig.of(), null);
	}

	/**
	 * 获取单例
	 *
	 * @return 单例
	 */
	public static JSONFactory getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 创建JSON工厂
	 *
	 * @param config    JSON配置
	 * @param predicate 键值对过滤器，{@code null}表示不过滤
	 * @return JSON工厂
	 */
	public static JSONFactory of(final JSONConfig config, final Predicate<MutableEntry<Object, Object>> predicate) {
		return new JSONFactory(config, predicate);
	}

	private final JSONConfig config;
	/**
	 * 过滤器，用于过滤或修改键值对，返回null表示忽略此键值对，返回非null表示修改后返回<br>
	 * entry中，key在JSONObject中为name，在JSONArray中为index
	 */
	private final Predicate<MutableEntry<Object, Object>> predicate;
	private volatile JSONMapper mapper;

	/**
	 * 构造
	 *
	 * @param config    配置项
	 * @param predicate 键值对过滤器，用于过滤掉不需要的键值对，例如：过滤掉值为null的键值对
	 */
	public JSONFactory(final JSONConfig config, final Predicate<MutableEntry<Object, Object>> predicate) {
		this.config = ObjUtil.defaultIfNull(config, JSONConfig::of);
		this.predicate = predicate;
	}

	/**
	 * 获取配置项
	 *
	 * @return 配置项
	 */
	public JSONConfig getConfig() {
		return this.config;
	}

	/**
	 * 获取键值对过滤器
	 *
	 * @return 键值对过滤器
	 */
	public Predicate<MutableEntry<Object, Object>> getPredicate() {
		return this.predicate;
	}

	/**
	 * 获取{@link JSONMapper}，用于实现Bean和JSON的转换<br>
	 * 此方法使用双重检查锁实现懒加载模式，只有mapper被使用时才初始化
	 *
	 * @return {@link JSONMapper}
	 */
	public JSONMapper getMapper() {
		if (null == this.mapper) {
			synchronized (this) {
				if (null == this.mapper) {
					this.mapper = JSONMapper.of(this);
				}
			}
		}
		return this.mapper;
	}

	// region ----- of

	/**
	 * 创建JSONObject
	 *
	 * @return JSONObject
	 */
	public JSONObject ofObj() {
		return new JSONObject(JSONObject.DEFAULT_CAPACITY, this);
	}

	/**
	 * 创建JSONArray
	 *
	 * @return JSONArray
	 */
	public JSONArray ofArray() {
		return new JSONArray(JSONArray.DEFAULT_CAPACITY, this);
	}

	/**
	 * 创建JSONPrimitive
	 *
	 * @param value 值
	 * @return JSONPrimitive
	 */
	public JSONPrimitive ofPrimitive(final Object value) {
		return new JSONPrimitive(value, this);
	}

	/**
	 * 创建{@link JSONParser}，用于JSON解析
	 *
	 * @param tokener {@link JSONTokener}
	 * @return {@link JSONParser}
	 */
	public JSONParser ofParser(final JSONTokener tokener){
		return JSONParser.of(tokener, this);
	}

	/**
	 * 创建{@link JSONWriter}，用于JSON写出
	 *
	 * @param appendable {@link Appendable}
	 * @return {@link JSONWriter}
	 */
	public JSONWriter ofWriter(final Appendable appendable) {
		return ofWriter(appendable, 0);
	}

	/**
	 * 创建{@link JSONWriter}，用于JSON写出
	 *
	 * @param appendable  {@link Appendable}
	 * @param prettyPrint 是否格式化输出
	 * @return {@link JSONWriter}
	 */
	public JSONWriter ofWriter(final Appendable appendable, final boolean prettyPrint) {
		return ofWriter(appendable, prettyPrint ? 2 : 0);
	}

	/**
	 * 创建{@link JSONWriter}，用于JSON写出
	 *
	 * @param appendable   {@link Appendable}
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量，用于格式化输出
	 * @return {@link JSONWriter}
	 */
	public JSONWriter ofWriter(final Appendable appendable, final int indentFactor) {
		return JSONWriter.of(appendable, indentFactor, config, predicate);
	}

	/**
	 * 创建BeanPath，用于使用路径方式访问或设置值
	 *
	 * @param expression BeanPath表达式
	 * @return BeanPath
	 */
	public BeanPath<JSON> ofBeanPath(final String expression) {
		return BeanPath.of(expression, new JSONNodeBeanFactory(config));
	}
	// endregion

	// region ----- parse

	/**
	 * JSON字符串转JSONObject对象
	 *
	 * @param obj Bean对象或者Map
	 * @return JSONObject
	 */
	public JSONObject parseObj(Object obj) {
		if (obj instanceof byte[]) {
			obj = new ByteArrayInputStream((byte[]) obj);
		}

		final JSONMapper mapper = getMapper();
		if (obj instanceof CharSequence) {
			return (JSONObject) mapper.map((CharSequence) obj);
		}
		return mapper.mapObj(obj);
	}

	/**
	 * 对象转{@link JSONArray}，支持：
	 * <ul>
	 *     <li>{@link JSONObject} 遍历Entry，结果为：[{k1:v1}, {k2: v2}, ...]</li>
	 *     <li>{@link CharSequence}，解析[...]字符串</li>
	 *     <li>其它支持和自定义的对象（如集合、数组等）</li>
	 * </ul>
	 *
	 * @param obj 数组或集合对象
	 * @return JSONArray
	 */
	public JSONArray parseArray(final Object obj) {
		final JSONMapper mapper = getMapper();
		if (obj instanceof JSONObject) {
			return mapper.mapFromJSONObject((JSONObject) obj);
		}

		if (obj instanceof CharSequence) {
			return (JSONArray) mapper.map((CharSequence) obj);
		}
		return mapper.mapArray(obj);
	}

	/**
	 * 转换对象为JSON，如果用户不配置JSONConfig，则JSON的有序与否与传入对象有关。<br>
	 * 支持的对象：
	 * <ul>
	 *     <li>String: 转换为相应的对象</li>
	 *     <li>Array、Iterable、Iterator：转换为JSONArray</li>
	 *     <li>Bean对象：转为JSONObject</li>
	 * </ul>
	 *
	 * @param obj 对象
	 * @return JSON（JSONObject or JSONArray）
	 */
	public JSON parse(final Object obj) {
		final JSONMapper mapper = this.getMapper();
		if (obj instanceof CharSequence) {
			return mapper.map((CharSequence) obj);
		}
		return mapper.map(obj);
	}
	// endregion

	// region ----- toBean

	/**
	 * 将JSON转换为指定类型的Bean对象
	 *
	 * @param json JSON
	 * @param type Bean类型，泛型对象使用{@link org.dromara.hutool.core.reflect.TypeReference}
	 * @param <T>  泛型类型
	 * @return Bean对象
	 */
	public <T> T toBean(final JSON json, final Type type) {
		return getMapper().toBean(json, type);
	}
	// endregion


}
