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

package org.dromara.hutool.json.reader;

import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.json.*;

import java.util.function.Predicate;

/**
 * JSON字符串解析器，实现：
 * <pre>{@code
 *            JSONTokener
 *     字符串 -------------> JSONObject
 *     字符串 -------------> JSONArray
 *     字符串 -------------> JSONPrimitive
 * }</pre>
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONParser {

	/**
	 * 创建JSONParser
	 *
	 * @param tokener {@link JSONTokener}
	 * @param config  JSON配置
	 * @return JSONParser
	 */
	public static JSONParser of(final JSONTokener tokener, final JSONConfig config) {
		return new JSONParser(tokener, config);
	}

	/**
	 * JSON配置
	 */
	private final JSONConfig config;
	private final JSONTokener tokener;
	/**
	 * 过滤器，用于过滤或修改键值对，返回null表示忽略此键值对，返回非null表示修改后返回<br>
	 * entry中，key在JSONObject中为name，在JSONArray中为index
	 */
	private Predicate<MutableEntry<Object, Object>> predicate;

	/**
	 * 构造
	 *
	 * @param tokener {@link JSONTokener}
	 * @param config  JSON配置
	 */
	public JSONParser(final JSONTokener tokener, final JSONConfig config) {
		this.tokener = tokener;
		this.config = config;
	}

	/**
	 * 获取{@link JSONTokener}
	 *
	 * @return {@link JSONTokener}
	 */
	public JSONTokener getTokener() {
		return this.tokener;
	}

	/**
	 * 设置过滤器，用于过滤或修改键值对，返回null表示忽略此键值对，返回非null表示修改后返回
	 *
	 * @param predicate 过滤器，用于过滤或修改键值对，返回null表示忽略此键值对，返回非null表示修改后返回
	 * @return this
	 */
	public JSONParser setPredicate(final Predicate<MutableEntry<Object, Object>> predicate) {
		this.predicate = predicate;
		return this;
	}

	/**
	 * 获取下一个值，可以是：
	 * <pre>
	 *     JSONObject
	 *     JSONArray
	 *     JSONPrimitive
	 *     null
	 * </pre>
	 *
	 * @return JSON值
	 */
	public JSON parse() {
		final JSON json = nextJSON(tokener.nextClean());
		tokener.checkEnd();
		return json;
	}

	/**
	 * 解析为JSONObject或JSONArray，解析值包括：
	 * <pre>
	 *     JSONObject
	 *     JSONArray
	 * </pre>
	 *
	 * @param json JSON对象或数组，用于存储解析结果
	 */
	public void parseTo(final JSON json) {
		if(null == json){
			return;
		}
		switch (tokener.nextClean()) {
			case CharUtil.DELIM_START:
				nextTo((JSONObject) json);
				break;
			case CharUtil.BRACKET_START:
				nextTo((JSONArray) json);
				break;
			default:
				throw new JSONException("Unsupported: " + json.getClass());
		}
	}

	/**
	 * 获取下一个值，可以是：
	 * <pre>
	 *     JSONObject
	 *     JSONArray
	 *     JSONPrimitive
	 *     null
	 * </pre>
	 *
	 * @return JSON值
	 */
	private JSON nextJSON(final char firstChar) {
		final JSON result;
		switch (firstChar) {
			case CharUtil.DELIM_START:
				final JSONObject jsonObject = new JSONObject(config);
				nextTo(jsonObject);
				result = jsonObject;
				break;
			case CharUtil.BRACKET_START:
				final JSONArray jsonArray = new JSONArray(config);
				nextTo(jsonArray);
				result = jsonArray;
				break;
			default:
				result = nextJSONPrimitive(firstChar);
		}

		return result;
	}

	/**
	 * 解析下一个值为JSONObject，第一个字符必须读取完后再调用此方法
	 *
	 * @param jsonObject JSON对象
	 */
	private void nextTo(final JSONObject jsonObject) {
		final JSONTokener tokener = this.tokener;

		char c;
		String key;
		for (; ; ) {
			c = tokener.nextClean();
			if (c == CharUtil.DELIM_END) {
				// 对象结束
				return;
			} else {
				key = tokener.nextKey(c);
			}

			// The key is followed by ':'.
			tokener.nextColon();

			// 过滤并设置键值对
			final JSON value = nextJSON(tokener.nextClean());
			// 添加前置过滤，通过MutablePair实现过滤、修改键值对等
			if (null != predicate) {
				final MutableEntry<Object, Object> entry = new MutableEntry<>(key, value);
				if (predicate.test(entry)) {
					// 使用修改后的键值对
					key = (String) entry.getKey();
					jsonObject.set(key, entry.getValue());
				}
			}else {
				jsonObject.set(key, value);
			}

			// Pairs are separated by ',' or ';'
			switch (tokener.nextClean()) {
				case ';':
				case CharUtil.COMMA:
					if (tokener.nextClean() == CharUtil.DELIM_END) {
						// issue#2380
						// 尾后逗号（Trailing Commas），JSON中虽然不支持，但是ECMAScript 2017支持，此处做兼容。
						return;
					}
					tokener.back();
					break;
				case CharUtil.DELIM_END:
					return;
				default:
					throw tokener.syntaxError("Expected a ',' or '}'");
			}
		}
	}

	/**
	 * 解析下一个值为JSONArray，第一个字符必须读取完后再调用此方法
	 *
	 * @param jsonArray JSON数组
	 */
	private void nextTo(final JSONArray jsonArray) {
		final JSONTokener tokener = this.tokener;
		char c;
		for (; ; ) {
			c = tokener.nextClean();
			if (c == CharUtil.BRACKET_END) {
				// 数组结束
				return;
			} else {
				// ,value or value
				JSON value = nextJSON(CharUtil.COMMA == c ? tokener.nextClean() : c);
				if (null != predicate) {
					// 使用过滤器
					final MutableEntry<Object, Object> entry = MutableEntry.of(jsonArray.size(), value);
					if (predicate.test(entry)) {
						// 使用修改后的键值对
						value = (JSON) entry.getValue();
						jsonArray.add(value);
					}
				} else {
					jsonArray.add(value);
				}
			}
		}
	}

	/**
	 * 解析为JSONPrimitive或{@code null}，解析值包括：
	 * <pre>
	 *     boolean
	 *     number
	 *     string
	 * </pre>
	 *
	 * @param firstChar 第一个字符，引号或字母
	 * @return JSONPrimitive或{@code null}
	 */
	private JSONPrimitive nextJSONPrimitive(final char firstChar) {
		switch (firstChar) {
			case CharUtil.DOUBLE_QUOTES:
			case CharUtil.SINGLE_QUOTE:
				// 引号包围，表示字符串值
				return new JSONPrimitive(tokener.nextWrapString(firstChar), config);
			default:
				final Object value = InternalJSONUtil.parseValueFromString(tokener.nextUnwrapString(firstChar));
				// 非引号包围，可能为boolean、数字、null等
				return null == value ? null : new JSONPrimitive(value, config);
		}
	}
}
