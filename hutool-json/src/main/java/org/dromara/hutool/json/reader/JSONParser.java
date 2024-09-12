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
 * <ul>
 *     <li>JSON字符串 --&gt; {@link JSONTokener} --&gt; {@link JSONObject}</li>
 *     <li>JSON字符串 --&gt; {@link JSONTokener} --&gt; {@link JSONArray}</li>
 * </ul>
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
	public JSON nextValue() {
		return nextValue(tokener.nextClean());
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
	private JSON nextValue(final char c) {
		switch (c) {
			case CharUtil.DELIM_START:
				final JSONObject jsonObject = new JSONObject(tokener, config);
				nextJSONObject(jsonObject);
				return jsonObject;
			case CharUtil.BRACKET_START:
				final JSONArray jsonArray = new JSONArray(tokener, config);
				nextJSONArray(jsonArray);
				return jsonArray;
			default:
				return nextJSONPrimitive(c);
		}
	}

	/**
	 * 解析为JSONObject
	 *
	 * @param jsonObject JSON对象
	 */
	private void nextJSONObject(final JSONObject jsonObject) {
		final JSONTokener tokener = this.tokener;

		char c;
		String key;
		for (; ; ) {
			c = tokener.nextClean();
			if (c == CharUtil.DELIM_END) {// 对象结束
				return;
			} else {
				key = tokener.nextKey(c);
			}

			// The key is followed by ':'.
			tokener.nextColon();

			// 过滤并设置键值对
			Object value = nextValue();
			// 添加前置过滤，通过MutablePair实现过滤、修改键值对等
			if (null != predicate) {
				final MutableEntry<Object, Object> pair = new MutableEntry<>(key, value);
				if (predicate.test(pair)) {
					// 使用修改后的键值对
					key = (String) pair.getKey();
					value = pair.getValue();
					jsonObject.set(key, value);
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
	 * 解析为JSONArray
	 *
	 * @param jsonArray JSON数组
	 */
	private void nextJSONArray(final JSONArray jsonArray) {
		final JSONTokener tokener = this.tokener;
		char c;
		for (; ; ) {
			c = tokener.nextClean();
			switch (c) {
				case CharUtil.BRACKET_END:
					return;
				case CharUtil.COMMA:
					jsonArray.add(nextValue());
				default:
					Object value = CharUtil.COMMA == c ? nextValue() : nextValue(c);
					if (null != predicate) {
						// 使用过滤器
						final MutableEntry<Object, Object> pair = MutableEntry.of(jsonArray.size(), value);
						if (predicate.test(pair)) {
							// 使用修改后的键值对
							value = pair.getValue();
							jsonArray.add(value);
						}
					}else {
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
	 * @param c 值类型
	 * @return JSONPrimitive或{@code null}
	 */
	private JSONPrimitive nextJSONPrimitive(final char c) {
		switch (c) {
			case CharUtil.DOUBLE_QUOTES:
			case CharUtil.SINGLE_QUOTE:
				// 引号包围，表示字符串值
				return new JSONPrimitive(tokener.nextWrapString(c));
			default:
				final Object value = InternalJSONUtil.parseValueFromString(tokener.nextUnwrapString(c));
				// 非引号包围，可能为boolean、数字、null等
				return null == value ? null : new JSONPrimitive(value);
		}
	}
}
