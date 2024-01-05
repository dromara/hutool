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

package org.dromara.hutool.json.xml;

import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.xml.XmlConstants;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.mapper.JSONValueMapper;

/**
 * XML解析器，将XML解析为JSON对象
 *
 * @author JSON.org, looly
 * @since 5.7.11
 */
public class JSONXMLParser {

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 *
	 * @param xmlStr      XML字符串
	 * @param jo          JSONObject
	 * @param parseConfig 解析选项
	 * @throws JSONException 解析异常
	 */
	public static void parseJSONObject(final String xmlStr, final JSONObject jo, final ParseConfig parseConfig) throws JSONException {
		final XMLTokener x = new XMLTokener(xmlStr, jo.config());
		while (x.more() && x.skipPast("<")) {
			parse(x, jo, null, parseConfig, 0);
		}
	}

	/**
	 * 扫描XML内容，并解析到JSONObject中。
	 *
	 * @param x                   {@link XMLTokener}
	 * @param context             {@link JSONObject}
	 * @param name                标签名，null表示从根标签开始解析
	 * @param parseConfig         解析选项
	 * @param currentNestingDepth 当前层级
	 * @return {@code true}表示解析完成
	 * @throws JSONException JSON异常
	 */
	private static boolean parse(final XMLTokener x, final JSONObject context, final String name,
								 final ParseConfig parseConfig, final int currentNestingDepth) throws JSONException {
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
						jsonobject.append(string, keepStrings ? token : JSONValueMapper.toJsonValue((String) token));
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
						context.append(tagName, jsonobject);
					} else {
						context.append(tagName, StrUtil.EMPTY);
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
								jsonobject.append("content", keepStrings ? token : JSONValueMapper.toJsonValue(string));
							}

						} else if (token == XmlConstants.C_LT) {
							// Nested element
							// issue#2748 of CVE-2022-45688
							final int maxNestingDepth = parseConfig.getMaxNestingDepth();
							if (maxNestingDepth > -1 && currentNestingDepth >= maxNestingDepth) {
								throw x.syntaxError("Maximum nesting depth of " + maxNestingDepth + " reached");
							}

							// Nested element
							if (parse(x, jsonobject, tagName, parseConfig, currentNestingDepth + 1)) {
								if (jsonobject.isEmpty()) {
									context.append(tagName, StrUtil.EMPTY);
								} else if (jsonobject.size() == 1 && jsonobject.get("content") != null) {
									context.append(tagName, jsonobject.get("content"));
								} else {
									context.append(tagName, jsonobject);
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
