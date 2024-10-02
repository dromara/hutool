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

import org.dromara.hutool.core.func.LambdaInfo;
import org.dromara.hutool.core.func.LambdaUtil;
import org.dromara.hutool.core.func.SerFunction;
import org.dromara.hutool.core.func.SerSupplier;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.map.MapWrapper;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.json.support.InternalJSONUtil;
import org.dromara.hutool.json.writer.JSONWriter;

import java.util.Arrays;
import java.util.Map;

/**
 * JSON对象<br>
 * 对象是 JSON 中的映射类型。他们将“键”映射到“值”。在 JSON 中，“键”必须始终是字符串。这些对中的每一组通常被称为“属性”。<br>
 * 例：
 * <pre>{@code
 *   json = new JSONObject().put("JSON", "Hello, World!").toString();
 * }</pre>
 *
 * @author looly
 */
public class JSONObject extends MapWrapper<String, JSON> implements JSON, JSONGetter<String>{
	private static final long serialVersionUID = 1L;

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_CAPACITY = MapUtil.DEFAULT_INITIAL_CAPACITY;

	private final JSONFactory factory;

	// region ----- 构造
	/**
	 * 构造，初始容量为 {@link #DEFAULT_CAPACITY}，KEY有序
	 */
	public JSONObject() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param capacity 初始大小
	 */
	public JSONObject(final int capacity) {
		this(capacity, JSONFactory.getInstance());
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
	 */
	public JSONObject(final int capacity, final JSONConfig config) {
		this(capacity, JSONFactory.of(config, null));
	}

	/**
	 * 构造
	 *
	 * @param capacity 初始大小
	 * @param factory  JSON工厂类
	 */
	public JSONObject(final int capacity, final JSONFactory factory) {
		super(InternalJSONUtil.createRawMap(capacity, factory));
		this.factory = factory;
	}
	// endregion

	@Override
	public JSONFactory getFactory() {
		return this.factory;
	}

	@Override
	public void write(final JSONWriter writer) throws JSONException {
		writer.beginObj();
		this.forEach((key, value) -> writer.writeField(new MutableEntry<>(key, value)));
		writer.endObj();
	}

	// region ----- get
	/**
	 * 根据lambda的方法引用，获取
	 *
	 * @param func 方法引用
	 * @param <P>  参数类型
	 * @param <T>  返回值类型
	 * @return 获取表达式对应属性和返回的对象
	 */
	public <P, T> T get(final SerFunction<P, T> func) {
		final LambdaInfo lambdaInfo = LambdaUtil.resolve(func);
		return get(lambdaInfo.getFieldName(), lambdaInfo.getReturnType());
	}

	@Override
	public Object getObj(final String key, final Object defaultValue) {
		final Object value;
		final JSON json = get(key);
		if(json instanceof JSONPrimitive){
			value = ((JSONPrimitive) json).getValue();
		}else {
			value = json;
		}
		return ObjUtil.defaultIfNull(value, defaultValue);
	}

	@Override
	public JSON getJSON(final String key) {
		return get(key);
	}
	// endregion

	// region ----- increment or append
	/**
	 * 对值加一，如果值不存在，赋值1，如果为数字类型，做加一操作
	 *
	 * @param key A key string.
	 * @return this.
	 * @throws JSONException 如果存在值非Integer, Long, Double, 或 Float.
	 */
	public JSONObject increment(final String key) throws JSONException {
		final JSON json = this.get(key);
		if(null == json){
			return putValue(key, 1);
		}

		if(json instanceof JSONPrimitive){
			final JSONPrimitive jsonPrimitive = (JSONPrimitive) json;
			if(jsonPrimitive.isNumber()){
				jsonPrimitive.increment();
				return this;
			}
		}

		throw new JSONException("Unable to increment key: {} type: {}", key, json.getClass());
	}

	/**
	 * 追加值.
	 * <ul>
	 *     <li>如果键值对不存在或对应值为{@code null}，则value为单独值</li>
	 *     <li>如果值是一个{@link JSONArray}，追加之</li>
	 *     <li>如果值是一个其他值，则和旧值共同组合为一个{@link JSONArray}</li>
	 * </ul>
	 *
	 * @param key       键
	 * @param value     值
	 * @return this.
	 * @throws JSONException 如果给定键为{@code null}或者键对应的值存在且为非JSONArray
	 * @since 6.0.0
	 */
	public JSONObject append(final String key, final Object value) throws JSONException {
		final Object object = this.getObj(key);
		if (object == null) {
			this.putValue(key, value);
		} else if (object instanceof JSONArray) {
			((JSONArray) object).addValue(value);
		} else {
			this.putValue(key, factory.ofArray().addValue(object).addValue(value));
		}
		return this;
	}
	// endregion

	// region ----- put
	/**
	 * 通过lambda批量设置值<br>
	 * 实际使用时，可以使用getXXX的方法引用来完成键值对的赋值：
	 * <pre>{@code
	 *     User user = GenericBuilder.of(User::new).with(User::setUsername, "hutool").build();
	 *     (new JSONObject()).setFields(user::getNickname, user::getUsername);
	 * }</pre>
	 *
	 * @param fields lambda,不能为空
	 * @return this
	 */
	public JSONObject putFields(final SerSupplier<?>... fields) {
		Arrays.stream(fields).forEach(f -> putValue(LambdaUtil.getFieldName(f), f.get()));
		return this;
	}

	/**
	 * 设置所有键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param map 键值对
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	public JSONObject putAllValue(final Map<?, ?> map) {
		if(MapUtil.isNotEmpty(map)){
			map.forEach((key, value) -> putValue(StrUtil.toStringOrNull(key), value));
		}
		return this;
	}

	/**
	 * 设置设置{@code null}值。在忽略null模式下移除对应键值对。
	 *
	 * @param key 键
	 * @return this.
	 */
	public JSONObject putNull(final String key){
		this.put(key, null);
		return this;
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key   键
	 * @param value Boolean类型对象
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	public JSONObject putValue(final String key, final Boolean value) throws JSONException {
		this.put(key, factory.ofPrimitive(value));
		return this;
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key   键
	 * @param value Number类型对象
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	public JSONObject putValue(final String key, final Number value) throws JSONException {
		this.put(key, factory.ofPrimitive(value));
		return this;
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key   键
	 * @param value Character类型对象
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	public JSONObject putValue(final String key, final Character value) throws JSONException {
		this.put(key, factory.ofPrimitive(value));
		return this;
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key   键
	 * @param value String类型对象
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	public JSONObject putValue(final String key, final String value) throws JSONException {
		// put时，不解析字符串，而是作为JSONPrimitive对待
		this.put(key, factory.ofPrimitive(value));
		return this;
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key   键
	 * @param value 值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the {@code null}
	 * @return this.
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	public JSONObject putValue(final String key, final Object value) throws JSONException {
		if(null == value){
			return putNull(key);
		}
		// put时，如果value为字符串，不解析，而是作为JSONPrimitive对待
		this.put(key, factory.getMapper().toJSON(value, false));
		return this;
	}

	/**
	 * 设置键值对到JSONObject中，在忽略null模式下，如果值为{@code null}，将此键移除
	 *
	 * @param key            键
	 * @param value          值对象. 可以是以下类型: Boolean, Double, Integer, JSONArray, JSONObject, Long, String, or the JSONNull.NULL.
	 * @return 旧值
	 * @throws JSONException 值是无穷数字抛出此异常
	 */
	@Override
	public JSON put(final String key, final JSON value) throws JSONException {
		if (null == key) {
			return null;
		}

		final JSONConfig config = config();
		if (null == value && config.isIgnoreNullValue()) {
			// 忽略值模式下如果值为空清除key
			return this.remove(key);
		}

		final JSONConfig.DuplicateMode duplicateMode = config.getDuplicateMode();
		if (JSONConfig.DuplicateMode.OVERRIDE != duplicateMode && containsKey(key)) {
			if(JSONConfig.DuplicateMode.IGNORE == duplicateMode){
				return null;
			}
			throw new JSONException("Duplicate key \"{}\"", key);
		}
		return super.put(key, value);
	}
	// endregion


	@Override
	public String toString() {
		return toJSONString(0);
	}
}
