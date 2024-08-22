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

package org.dromara.hutool.json.xml;

import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.xml.XmlConstants;
import org.dromara.hutool.json.InternalJSONUtil;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONObject;

import java.util.function.Predicate;

/**
 * XML解析器，将XML解析为JSON对象
 *
 * @author JSON.org, looly
 * @since 5.7.11
 */
public class JSONXMLParser {

	/**
	 * 创建XML解析器
	 *
	 * @param parseConfig 解析选项
	 * @param predicate   键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@link Predicate#test(Object)}为{@code true}保留
	 * @return JSONXMLParser
	 */
	public static JSONXMLParser of(final ParseConfig parseConfig, final Predicate<MutableEntry<String, Object>> predicate) {
		return new JSONXMLParser(parseConfig, predicate);
	}

	private final ParseConfig parseConfig;
	private final Predicate<MutableEntry<String, Object>> predicate;

	/**
	 * 构造
	 *
	 * @param parseConfig 解析选项
	 * @param predicate   键值对过滤编辑器，可以通过实现此接口，完成解析前对键值对的过滤和修改操作，{@link Predicate#test(Object)}为{@code true}保留
	 */
	public JSONXMLParser(final ParseConfig parseConfig, final Predicate<MutableEntry<String, Object>> predicate) {
		this.parseConfig = parseConfig;
		this.predicate = predicate;
	}

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 *
	 * @param xmlStr      XML字符串
	 * @param jo          JSONObject
	 * @throws JSONException 解析异常
	 */
	public void parseJSONObject(final String xmlStr, final JSONObject jo) throws JSONException {
		final XMLTokener x = new XMLTokener(xmlStr);
		while (x.more() && x.skipPast("<")) {
			parse(x, jo, null, 0);
		}
	}

	/**
	 * 扫描XML内容，并解析到JSONObject中。
	 *
	 * @param x                   {@link XMLTokener}
	 * @param context             {@link JSONObject}
	 * @param name                标签名，null表示从根标签开始解析
	 * @param currentNestingDepth 当前层级
	 * @return {@code true}表示解析完成
	 * @throws JSONException JSON异常
	 */
	private boolean parse(final XMLTokener x, final JSONObject context, final String name, final int currentNestingDepth) throws JSONException {
		final char c;
		int i;
		final JSONObject jsonobject;
		String string;
		final String tagName;
		Object token;

		token = x.nextToken();

		if (token == XmlConstants.C_BANG) {
			c = x.next();
			if (c == '-') {
				if (x.next() == '-') {
					x.skipPast("-->");
					return false;
				}
				x.back();
			} else if (c == '[') {
				token = x.nextToken();
				if ("CDATA".equals(token)) {
					if (x.next() == '[') {
						string = x.nextCDATA();
						if (!string.isEmpty()) {
							context.append("content", string);
						}
						return false;
					}
				}
				throw x.syntaxError("Expected 'CDATA['");
			}
			i = 1;
			do {
				token = x.nextMeta();
				if (token == null) {
					throw x.syntaxError("Missing '>' after '<!'.");
				} else if (token == XmlConstants.C_LT) {
					i += 1;
				} else if (token == XmlConstants.C_GT) {
					i -= 1;
				}
			} while (i > 0);
			return false;
		} else if (token == XmlConstants.C_QUEST) {

			// <?
			x.skipPast("?>");
			return false;
		} else if (token == Character.valueOf(CharUtil.SLASH)) {

			// Close tag </

			token = x.nextToken();
			if (name == null) {
				throw x.syntaxError("Mismatched close tag " + token);
			}
			if (!token.equals(name)) {
				throw x.syntaxError("Mismatched " + name + " and " + token);
			}
			if (x.nextToken() != XmlConstants.C_GT) {
				throw x.syntaxError("Misshaped close tag");
			}
			return true;

		} else if (token instanceof Character) {
			throw x.syntaxError("Misshaped tag");

			// Open tag <

		} else {
			tagName = (String) token;
			token = null;
			jsonobject = new JSONObject();
			final boolean keepStrings = parseConfig.isKeepStrings();
			for (; ; ) {
				if (token == null) {
					token = x.nextToken();
				}

				// attribute = value
				if (token instanceof String) {
					string = (String) token;
					token = x.nextToken();
					if (token == Character.valueOf(CharUtil.EQUAL)) {
						token = x.nextToken();
						if (!(token instanceof String)) {
							throw x.syntaxError("Missing value");
						}
						jsonobject.append(string, keepStrings ? token : InternalJSONUtil.parseValueFromString((String) token));
						token = null;
					} else {
						jsonobject.append(string, "");
					}

				} else if (token == Character.valueOf(CharUtil.SLASH)) {
					// Empty tag <.../>
					if (x.nextToken() != XmlConstants.C_GT) {
						throw x.syntaxError("Misshaped tag");
					}
					if (!jsonobject.isEmpty()) {
						context.append(tagName, jsonobject, this.predicate);
					} else {
						context.append(tagName, StrUtil.EMPTY, this.predicate);
					}
					return false;

				} else if (token == XmlConstants.C_GT) {
					// Content, between <...> and </...>
					for (; ; ) {
						token = x.nextContent();
						if (token == null) {
							if (tagName != null) {
								throw x.syntaxError("Unclosed tag " + tagName);
							}
							return false;
						} else if (token instanceof String) {
							string = (String) token;
							if (!string.isEmpty()) {
								jsonobject.append("content", keepStrings ? token : InternalJSONUtil.parseValueFromString(string));
							}

						} else if (token == XmlConstants.C_LT) {
							// Nested element
							// issue#2748 of CVE-2022-45688
							final int maxNestingDepth = parseConfig.getMaxNestingDepth();
							if (maxNestingDepth > -1 && currentNestingDepth >= maxNestingDepth) {
								throw x.syntaxError("Maximum nesting depth of " + maxNestingDepth + " reached");
							}

							// Nested element
							if (parse(x, jsonobject, tagName, currentNestingDepth + 1)) {
								if (jsonobject.isEmpty()) {
									context.append(tagName, StrUtil.EMPTY, this.predicate);
								} else if (jsonobject.size() == 1 && jsonobject.get("content") != null) {
									context.append(tagName, jsonobject.get("content"), this.predicate);
								} else {
									context.append(tagName, jsonobject, this.predicate);
								}
								return false;
							}
						}
					}
				} else {
					throw x.syntaxError("Misshaped tag");
				}
			}
		}
	}
}
