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
	 * @param factory  JSON工厂
	 * @return JSONParser
	 */
	public static JSONParser of(final JSONTokener tokener, final JSONFactory factory) {
		return new JSONParser(tokener, factory);
	}

	private final JSONTokener tokener;
	private final JSONFactory factory;

	/**
	 * 构造
	 *
	 * @param tokener {@link JSONTokener}
	 * @param factory  JSON工厂
	 */
	public JSONParser(final JSONTokener tokener, final JSONFactory factory) {
		this.tokener = tokener;
		this.factory = factory;
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
		if (null == json) {
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
				final JSONObject jsonObject = factory.ofObj();
				nextTo(jsonObject);
				result = jsonObject;
				break;
			case CharUtil.BRACKET_START:
				final JSONArray jsonArray = factory.ofArray();
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

			// 过滤并设置键值对，通过MutablePair实现过滤、修改键值对等
			set(jsonObject, key, nextJSON(tokener.nextClean()));

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
	 * 设置键值对，通过前置过滤器过滤、修改键值对等
	 *
	 * @param jsonObject JSON对象
	 * @param key 键
	 * @param value 值
	 */
	private void set(final JSONObject jsonObject, final String key, final JSON value){
		// 添加前置过滤，通过MutablePair实现过滤、修改键值对等
		final Predicate<MutableEntry<Object, Object>> predicate = factory.getPredicate();
		if (null != predicate) {
			final MutableEntry<Object, Object> entry = new MutableEntry<>(key, value);
			if (predicate.test(entry)) {
				// 使用修改后的键值对
				jsonObject.putObj((String) entry.getKey(), entry.getValue());
			}
		} else {
			jsonObject.put(key, value);
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
				set(jsonArray, nextJSON(CharUtil.COMMA == c ? tokener.nextClean() : c));
			}
		}
	}

	/**
	 * 设置数组元素，通过前置过滤器过滤、修改键值对等
	 *
	 * @param jsonArray JSON数组
	 * @param value 值
	 */
	private void set(final JSONArray jsonArray, final JSON value) {
		final Predicate<MutableEntry<Object, Object>> predicate = factory.getPredicate();
		if (null != predicate) {
			// 使用过滤器
			final MutableEntry<Object, Object> entry = MutableEntry.of(jsonArray.size(), value);
			if (predicate.test(entry)) {
				// 使用修改后的键值对，用户修改后可能不是JSON，此处使用set，调用mapper转换
				jsonArray.setObj((Integer) entry.getKey(), entry.getValue());
			}
		} else {
			jsonArray.add(value);
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
				return factory.ofPrimitive(tokener.nextWrapString(firstChar));
			case 't':
			case 'T':
				checkTrue(tokener.next(3));
				return factory.ofPrimitive(true);
			case 'f':
			case 'F':
				checkFalse(tokener.next(4));
				return factory.ofPrimitive(false);
			case 'n':
			case 'N':
				checkNull(tokener.next(3));
				return null;
			default:
				final Object value = InternalJSONUtil.parseNumberOrString(tokener.nextUnwrapString(firstChar));
				// 非引号包围，可能为数字、null等
				return null == value ? null : factory.ofPrimitive(value);
		}
	}

	/**
	 * 检查是否为true的rue部分
	 *
	 * @param next 值
	 */
	private void checkTrue(final char[] next) {
		if ((next[0] == 'r' || next[0] == 'R') &&
			(next[1] == 'u' || next[1] == 'U') &&
			(next[2] == 'e' || next[2] == 'E')
		) {
			return;
		}

		throw tokener.syntaxError("Expected true but : t" + String.valueOf(next));
	}

	/**
	 * 检查是否为false的alse部分
	 *
	 * @param next 值
	 */
	private void checkFalse(final char[] next) {
		if ((next[0] == 'a' || next[0] == 'A') &&
			(next[1] == 'l' || next[1] == 'L') &&
			(next[2] == 's' || next[2] == 'S') &&
			(next[3] == 'e' || next[3] == 'E')
		) {
			return;
		}

		throw tokener.syntaxError("Expected false but : f" + String.valueOf(next));
	}

	/**
	 * 检查是否为null的ull部分
	 *
	 * @param next 值
	 */
	private void checkNull(final char[] next) {
		if ((next[0] == 'u' || next[0] == 'U') &&
			(next[1] == 'l' || next[1] == 'L') &&
			(next[2] == 'l' || next[2] == 'L')
		) {
			return;
		}

		throw tokener.syntaxError("Expected null but : n" + String.valueOf(next));
	}
}
