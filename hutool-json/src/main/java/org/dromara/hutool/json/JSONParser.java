/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json;

import org.dromara.hutool.core.lang.mutable.Mutable;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.text.CharUtil;

import java.util.function.Predicate;

/**
 * JSON字符串解析器，实现：
 * <ul>
 *     <li>JSON字符串 --&gt; {@link JSONTokener} --&gt; {@link JSONObject}</li>
 *     <li>JSON字符串 --&gt; {@link JSONTokener} --&gt; {@link JSONArray}</li>
 * </ul>
 *
 * @author looly
 * @since 5.8.0
 */
public class JSONParser {

	/**
	 * JSON配置
	 */
	private final JSONConfig config;

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

	private final JSONTokener tokener;

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
	 * 是否结束
	 *
	 * @return 是否结束
	 */
	public boolean end() {
		return this.tokener.end();
	}

	// region parseTo

	/**
	 * 解析{@link JSONTokener}中的字符到目标的{@link JSONObject}中
	 *
	 * @param jsonObject {@link JSONObject}
	 * @param predicate  键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@code null}表示不过滤，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public void parseTo(final JSONObject jsonObject, final Predicate<MutableEntry<String, Object>> predicate) {
		final JSONTokener tokener = this.tokener;

		if (tokener.nextClean() != '{') {
			throw tokener.syntaxError("A JSONObject text must begin with '{'");
		}

		char prev;
		char c;
		String key;
		while (true) {
			prev = tokener.getPrevious();
			c = tokener.nextClean();
			switch (c) {
				case 0:
					throw tokener.syntaxError("A JSONObject text must end with '}'");
				case '}':
					return;
				case '{':
				case '[':
					if (prev == '{') {
						throw tokener.syntaxError("A JSONObject can not directly nest another JSONObject or JSONArray.");
					}
				default:
					tokener.back();
					key = nextValue(true).toString();
			}

			// The key is followed by ':'.

			c = tokener.nextClean();
			if (c != ':') {
				throw tokener.syntaxError("Expected a ':' after a key");
			}

			jsonObject.set(key, nextValue(false), predicate);

			// Pairs are separated by ','.

			switch (tokener.nextClean()) {
				case ';':
				case CharUtil.COMMA:
					if (tokener.nextClean() == '}') {
						// issue#2380
						// 尾后逗号（Trailing Commas），JSON中虽然不支持，但是ECMAScript 2017支持，此处做兼容。
						return;
					}
					tokener.back();
					break;
				case '}':
					return;
				default:
					throw tokener.syntaxError("Expected a ',' or '}'");
			}
		}
	}

	/**
	 * 解析JSON字符串到{@link JSONArray}中
	 *
	 * @param jsonArray {@link JSONArray}
	 * @param predicate 键值对过滤编辑器，可以通过实现此接口，完成解析前对值的过滤和修改操作，{@code null} 表示不过滤，，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public void parseTo(final JSONArray jsonArray, final Predicate<Mutable<Object>> predicate) {
		final JSONTokener x = this.tokener;

		if (x.nextClean() != '[') {
			throw x.syntaxError("A JSONArray text must start with '['");
		}
		if (x.nextClean() != ']') {
			x.back();
			for (; ; ) {
				if (x.nextClean() == CharUtil.COMMA) {
					x.back();
					jsonArray.addRaw(null, predicate);
				} else {
					x.back();
					jsonArray.addRaw(nextValue(false), predicate);
				}
				switch (x.nextClean()) {
					case CharUtil.COMMA:
						if (x.nextClean() == ']') {
							return;
						}
						x.back();
						break;
					case ']':
						return;
					default:
						throw x.syntaxError("Expected a ',' or ']'");
				}
			}
		}
	}
	// endregion

	/**
	 * 获得下一个值，值类型可以是Boolean, Double, Integer, JSONArray, JSONObject, Long, or String
	 *
	 * @param getOnlyStringValue 是否只获取String值
	 * @return Boolean, Double, Integer, JSONArray, JSONObject, Long, or String
	 * @throws JSONException 语法错误
	 */
	public Object nextValue(final boolean getOnlyStringValue) throws JSONException {
		return nextValue(getOnlyStringValue, (token, tokener, config) -> {
			switch (token) {
				case '{':
					try {
						return new JSONObject(this, config);
					} catch (final StackOverflowError e) {
						throw new JSONException("JSONObject depth too large to process.", e);
					}
				case '[':
					try {
						return new JSONArray(this, config);
					} catch (final StackOverflowError e) {
						throw new JSONException("JSONObject depth too large to process.", e);
					}
			}
			throw new JSONException("Unsupported object build for token {}", token);
		});
	}

	/**
	 * 获得下一个值，值类型可以是Boolean, Double, Integer, JSONArray, JSONObject, Long, or String
	 *
	 * @param getOnlyStringValue 是否只获取String值
	 * @param objectBuilder JSON对象构建器
	 * @return Boolean, Double, Integer, JSONArray, JSONObject, Long, or String
	 * @throws JSONException 语法错误
	 */
	public Object nextValue(final boolean getOnlyStringValue, final ObjectBuilder objectBuilder) throws JSONException {
		final JSONTokener tokener = this.tokener;
		char c = tokener.nextClean();
		switch (c) {
			case '"':
			case '\'':
				return tokener.nextString(c);
			case '{':
			case '[':
				if (getOnlyStringValue) {
					throw tokener.syntaxError("String value must not begin with '{'");
				}
				tokener.back();
				return objectBuilder.build(c, tokener, this.config);
		}

		/*
		 * Handle unquoted text. This could be the values true, false, or null, or it can be a number.
		 * An implementation (such as this one) is allowed to also accept non-standard forms. Accumulate
		 * characters until we reach the end of the text or a formatting character.
		 */

		final StringBuilder sb = new StringBuilder();
		while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
			sb.append(c);
			c = tokener.next();
		}
		tokener.back();

		final String valueString = sb.toString().trim();
		if (valueString.isEmpty()) {
			throw tokener.syntaxError("Missing value");
		}
		return getOnlyStringValue ? valueString : InternalJSONUtil.parseValueFromString(valueString);
	}

	/**
	 * 对象构建抽象，通过实现此接口，从{@link JSONTokener}解析值并构建指定对象
	 */
	@FunctionalInterface
	public interface ObjectBuilder {
		/**
		 * 构建
		 *
		 * @param token   符号表示，用于区分对象类型
		 * @param tokener {@link JSONTokener}
		 * @param config  {@link JSONConfig}
		 * @return 构建的对象
		 */
		Object build(char token, JSONTokener tokener, JSONConfig config);
	}
}
