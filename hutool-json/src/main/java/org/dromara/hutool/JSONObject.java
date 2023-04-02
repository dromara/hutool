/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool;

import org.dromara.hutool.collection.CollUtil;
import org.dromara.hutool.lang.func.LambdaInfo;
import org.dromara.hutool.lang.func.LambdaUtil;
import org.dromara.hutool.lang.func.SerFunction;
import org.dromara.hutool.lang.func.SerSupplier;
import org.dromara.hutool.lang.mutable.MutableEntry;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.map.MapWrapper;
import org.dromara.hutool.util.ObjUtil;
import org.dromara.hutool.mapper.JSONObjectMapper;
import org.dromara.hutool.writer.JSONWriter;

import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

/**
 * JSON对象<br>
 * 例：<br>
 *
 * <pre>
 * json = new JSONObject().put(&quot;JSON&quot;, &quot;Hello, World!&quot;).toString();
 * </pre>
 *
 * @author looly
 */
public class JSONObject extends MapWrapper<String, Object> implements JSON, JSONGetter<String> {
	private static final long serialVersionUID = -330220388580734346L;

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_CAPACITY = MapUtil.DEFAULT_INITIAL_CAPACITY;

	/**
	 * 配置项
	 */
	private JSONConfig config;

	// -------------------------------------------------------------------------------------------------------------------- Constructor start

	/**
	 * 构造，初始容量为 {@link #DEFAULT_CAPACITY}，KEY有序
	 */
	public JSONObject() {
		this(JSONConfig.of());
	}

	/**
	 * 构造
	 *
	 * @param config JSON配置项
	 * @since 4.6.5
	 */
	public JSONObject(final JSONConfig config) {
		this(DEFAULT_CAPACITY, config);
	}

	/**
	 * 构造
	 *
	 * @param capacity 初始大小
	 * @param config   JSON配置项，{@code null}则使用默认配置
	 * @since 4.1.19
	 */
	public JSONObject(final int capacity, final JSONConfig config) {
		super(InternalJSONUtil.createRawMap(capacity, ObjUtil.defaultIfNull(config, JSONConfig.of())));
		this.config = ObjUtil.defaultIfNull(config, JSONConfig.of());
	}

	/**
	 * 构建JSONObject，JavaBean默认忽略null值，其它对象不忽略，规则如下：
	 * <ol>
	 * <li>value为Map，将键值对加入JSON对象</li>
	 * <li>value为JSON字符串（CharSequence），使用JSONTokener解析</li>
	 * <li>value为JSONTokener，直接解析</li>
	 * <li>value为普通JavaBean，如果为普通的JavaBean，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象。
	 * 例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"</li>
	 * </ol>
	 *
	 * @param source JavaBean或者Map对象或者String
	 */
	public JSONObject(final Object source) {
		this(source, JSONConfig.of().setIgnoreNullValue(InternalJSONUtil.defaultIgnoreNullValue(source)));
	}

	/**
	 * 构建JSONObject，规则如下：
	 * <ol>
	 * <li>value为Map，将键值对加入JSON对象</li>
	 * <li>value为JSON字符串（CharSequence），使用JSONTokener解析</li>
	 * <li>value为JSONTokener，直接解析</li>
	 * <li>value为普通JavaBean，如果为普通的JavaBean，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象。例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"</li>
	 * </ol>
	 * <p>
	 * 如果给定值为Map，将键值对加入JSON对象;<br>
	 * 如果为普通的JavaBean，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象<br>
	 * 例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"
	 *
	 * @param source JavaBean或者Map对象或者String
	 * @param config JSON配置文件，{@code null}则使用默认配置
	 * @since 4.2.2
	 */
	public JSONObject(final Object source, final JSONConfig config) {
		this(source, config, null);
	}

	/**
	 * 构建JSONObject，规则如下：
	 * <ol>
	 * <li>value为Map，将键值对加入JSON对象</li>
	 * <li>value为JSON字符串（CharSequence），使用JSONTokener解析</li>
	 * <li>value为JSONTokener，直接解析</li>
	 * <li>value为普通JavaBean，如果为普通的JavaBean，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象。例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"</li>
	 * </ol>
	 * <p>
	 * 如果给定值为Map，将键值对加入JSON对象;<br>
	 * 如果为普通的JavaBean，调用其getters方法（getXXX或者isXXX）获得值，加入到JSON对象<br>
	 * 例如：如果JavaBean对象中有个方法getName()，值为"张三"，获得的键值对为：name: "张三"
	 *
	 * @param source    JavaBean或者Map对象或者String
	 * @param config    JSON配置文件，{@code null}则使用默认配置
	 * @param predicate 键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤，{@link Predicate#test(Object)}为{@code true}保留
	 * @since 5.8.0
	 */
	public JSONObject(final Object source, final JSONConfig config, final Predicate<MutableEntry<String, Object>> predicate) {
		this(DEFAULT_CAPACITY, config);
		JSONObjectMapper.of(source, predicate).mapTo(this);
	}
	// -------------------------------------------------------------------------------------------------------------------- Constructor end

	@Override
	public JSONConfig getConfig() {
		return this.config;
	}

	/**
	 * 设置转为字符串时的日期格式，默认为时间戳（null值）<br>
	 * 此方法设置的日期格式仅对转换为JSON字符串有效，对解析JSON为bean无效。
	 *
	 * @param format 格式，null表示使用时间戳
	 * @return this
	 * @since 4.1.19
	 */
	public JSONObject setDateFormat(final String format) {
		this.config.setDateFormat(format);
		return this;
	}

	/**
	 * 将指定KEY列表的值组成新的JSONArray
	 *
	 * @param names KEY列表
	 * @return A JSONArray of values.
	 * @throws JSONException If any of the values are non-finite numbers.
	 */
	public JSONArray toJSONArray(final Collection<String> names) throws JSONException {
		if (CollUtil.isEmpty(names)) {
			return null;
		}
		final JSONArray ja = new JSONArray(this.config);
		Object value;
		for (final String name : names) {
			value = this.get(name);
			if (null != value) {
				ja.set(value);
			}
		}
		return ja;
	}

	@Override
	public Object getObj(final String key, final Object defaultValue) {
		return this.getOrDefault(key, defaultValue);
	}

	/**
	 * 根据lambda的方法引用，获取
	 * @param func 方法引用
	 * @param <P> 参数类型
	 * @param <T> 返回值类型
	 * @return 获取表达式对应属性和返回的对象
	 */
	public <P, T> T get(final SerFunction<P, T> func){
		final LambdaInfo lambdaInfo = LambdaUtil.resolve(func);
		return get(lambdaInfo.getFieldName(), lambdaInfo.getReturnType());
	}

	/**
	 * PUT 键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key   键
	 * @param value 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	@Override
	public Object put(final String key, final Object value) throws JSONException {
		return put(key, value, null, getConfig().isCheckDuplicate());
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key   键
	 * @param value 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	public JSONObject set(final String key, final Object value) throws JSONException {
		set(key, value, null);
		return this;
	}

	/**
	 * 通过lambda批量设置值<br>
	 * 实际使用时，可以使用getXXX的方法引用来完成键值对的赋值：
	 * <pre>
	 *     User user = GenericBuilder.of(User::new).with(User::setUsername, "hutool").build();
	 *     (new JSONObject()).setFields(user::getNickname, user::getUsername);
	 * </pre>
	 *
	 * @param fields lambda,不能为空
	 * @return this
	 */
	public JSONObject setFields(final SerSupplier<?>... fields) {
		Arrays.stream(fields).forEach(f -> set(LambdaUtil.getFieldName(f), f.get()));
		return this;
	}

	/**
	 * 一次性Put 键值对，如果key已经存在抛出异常，如果键值中有null值，忽略
	 *
	 * @param key       键
	 * @param value     值对象，可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @param predicate 键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤，{@link Predicate#test(Object)}为{@code true}保留
	 * @return this
	 * @throws JSONException 值是无穷数字、键重复抛出异常
	 * @since 5.8.0
	 */
	public JSONObject set(final String key, final Object value, final Predicate<MutableEntry<String, Object>> predicate) throws JSONException {
		put(key, value, predicate, getConfig().isCheckDuplicate());
		return this;
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key            键
	 * @param value          值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @param predicate      键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤，{@link Predicate#test(Object)}为{@code true}保留
	 * @param checkDuplicate 是否检查重复键，如果为{@code true}，则出现重复键时抛出{@link JSONException}异常
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 * @since 5.8.0
	 */
	public JSONObject set(final String key, final Object value, final Predicate<MutableEntry<String, Object>> predicate, final boolean checkDuplicate) throws JSONException {
		put(key, value, predicate, checkDuplicate);
		return this;
	}

	/**
	 * 在键和值都为非空的情况下put到JSONObject中
	 *
	 * @param key   键
	 * @param value 值对象，可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return this.
	 * @throws JSONException 值是无穷数字
	 */
	public JSONObject setOpt(final String key, final Object value) throws JSONException {
		if (key != null && value != null) {
			this.set(key, value);
		}
		return this;
	}

	@Override
	public void putAll(final Map<? extends String, ?> m) {
		for (final Entry<? extends String, ?> entry : m.entrySet()) {
			this.set(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 追加值.
	 * <ul>
	 *     <li>如果键值对不存在或对应值为{@code null}，则value为单独值</li>
	 *     <li>如果值是一个{@link JSONArray}，追加之</li>
	 *     <li>如果值是一个其他值，则和旧值共同组合为一个{@link JSONArray}</li>
	 * </ul>
	 *
	 * @param key   键
	 * @param value 值
	 * @return this.
	 * @throws JSONException 如果给定键为{@code null}或者键对应的值存在且为非JSONArray
	 */
	public JSONObject append(final String key, final Object value) throws JSONException {
		final Object object = this.getObj(key);
		if (object == null) {
			this.set(key, value);
		} else if (object instanceof JSONArray) {
			((JSONArray) object).set(value);
		} else {
			this.set(key, JSONUtil.ofArray(this.config).set(object).set(value));
		}
		return this;
	}

	/**
	 * 对值加一，如果值不存在，赋值1，如果为数字类型，做加一操作
	 *
	 * @param key A key string.
	 * @return this.
	 * @throws JSONException 如果存在值非Integer, Long, Double, 或 Float.
	 */
	public JSONObject increment(final String key) throws JSONException {
		final Object value = this.getObj(key);
		if (value == null) {
			this.set(key, 1);
		} else if (value instanceof BigInteger) {
			this.set(key, ((BigInteger) value).add(BigInteger.ONE));
		} else if (value instanceof BigDecimal) {
			this.set(key, ((BigDecimal) value).add(BigDecimal.ONE));
		} else if (value instanceof Integer) {
			this.set(key, (Integer) value + 1);
		} else if (value instanceof Long) {
			this.set(key, (Long) value + 1);
		} else if (value instanceof Double) {
			this.set(key, (Double) value + 1);
		} else if (value instanceof Float) {
			this.set(key, (Float) value + 1);
		} else {
			throw new JSONException("Unable to increment [" + InternalJSONUtil.quote(key) + "].");
		}
		return this;
	}

	/**
	 * 返回JSON字符串<br>
	 * 如果解析错误，返回{@code null}
	 *
	 * @return JSON字符串
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
	 * @param predicate    过滤器，同时可以修改编辑键和值，{@link Predicate#test(Object)}为{@code true}保留
	 * @return JSON字符串
	 * @since 5.7.15
	 */
	public String toJSONString(final int indentFactor, final Predicate<MutableEntry<Object, Object>> predicate) {
		final StringWriter sw = new StringWriter();
		synchronized (sw.getBuffer()) {
			return this.write(sw, indentFactor, 0, predicate).toString();
		}
	}

	/**
	 * 将JSON内容写入Writer<br>
	 * 支持过滤器，即选择哪些字段或值不写出
	 *
	 * @param writer       writer
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @param indent       本级别缩进量
	 * @param predicate    过滤器，同时可以修改编辑键和值
	 * @return Writer
	 * @throws JSONException JSON相关异常
	 * @since 5.7.15
	 */
	@Override
	public Writer write(final Writer writer, final int indentFactor, final int indent, final Predicate<MutableEntry<Object, Object>> predicate) throws JSONException {
		final JSONWriter jsonWriter = JSONWriter.of(writer, indentFactor, indent, config)
				.beginObj();
		this.forEach((key, value) -> jsonWriter.writeField(new MutableEntry<>(key, value), predicate));
		jsonWriter.end();
		// 此处不关闭Writer，考虑writer后续还需要填内容
		return writer;
	}

	@Override
	public JSONObject clone() throws CloneNotSupportedException {
		final JSONObject clone = (JSONObject) super.clone();
		clone.config = this.config;
		return clone;
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key            键
	 * @param value          值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @param predicate      键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤，{@link Predicate#test(Object)}为{@code true}保留
	 * @param checkDuplicate 是否检查重复键，如果为{@code true}，则出现重复键时抛出{@link JSONException}异常
	 * @return 旧值
	 * @throws JSONException 值是无穷数字抛出此异常
	 * @since 5.8.0
	 */
	private Object put(String key, Object value, final Predicate<MutableEntry<String, Object>> predicate, final boolean checkDuplicate) throws JSONException {
		if (null == key) {
			return null;
		}

		// 添加前置过滤，通过MutablePair实现过滤、修改键值对等
		if (null != predicate) {
			final MutableEntry<String, Object> pair = new MutableEntry<>(key, value);
			if (predicate.test(pair)) {
				// 使用修改后的键值对
				key = pair.getKey();
				value = pair.getValue();
			} else {
				// 键值对被过滤
				return null;
			}
		}

		final boolean ignoreNullValue = this.config.isIgnoreNullValue();
		if (null == value && ignoreNullValue) {
			// 忽略值模式下如果值为空清除key
			return this.remove(key);
		} else if (checkDuplicate && containsKey(key)) {
			throw new JSONException("Duplicate key \"{}\"", key);
		}
		return super.put(key, InternalJSONUtil.wrap(value, this.config));
	}
}
