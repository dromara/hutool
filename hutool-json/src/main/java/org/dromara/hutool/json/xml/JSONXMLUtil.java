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

import org.dromara.hutool.json.JSON;
import org.dromara.hutool.json.JSONException;
import org.dromara.hutool.json.JSONObject;

/**
 * 提供静态方法在XML和JSONObject之间转换
 *
 * @author JSON.org, looly
 * @see JSONXMLParser
 * @see JSONXMLSerializer
 */
public class JSONXMLUtil {

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 * Content text may be placed in a "content" member. Comments, prologs, DTDs, and {@code <[ [ ]]>} are ignored.
	 *
	 * @param string The source string.
	 * @return A JSONObject containing the structured data from the XML string.
	 * @throws JSONException Thrown if there is an errors while parsing the string
	 */
	public static JSONObject toJSONObject(final String string) throws JSONException {
		return toJSONObject(string, ParseConfig.of());
	}

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 * Content text may be placed in a "content" member. Comments, prologs, DTDs, and {@code <[ [ ]]>} are ignored.
	 * All values are converted as strings, for 1, 01, 29.0 will not be coerced to numbers but will instead be the exact value as seen in the XML document.
	 *
	 * @param string      XML字符串
	 * @param parseConfig XML解析选项
	 * @return A JSONObject containing the structured data from the XML string.
	 * @throws JSONException Thrown if there is an errors while parsing the string
	 */
	public static JSONObject toJSONObject(final String string, final ParseConfig parseConfig) throws JSONException {
		return toJSONObject(string, new JSONObject(), parseConfig);
	}

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 *
	 * @param jo          JSONObject
	 * @param xmlStr      XML字符串
	 * @param parseConfig XML解析选项
	 * @return A JSONObject 解析后的JSON对象，与传入的jo为同一对象
	 * @throws JSONException 解析异常
	 * @since 5.3.1
	 */
	public static JSONObject toJSONObject(final String xmlStr, final JSONObject jo, final ParseConfig parseConfig) throws JSONException {
		JSONXMLParser.of(parseConfig, null).parseJSONObject(xmlStr, jo);
		return jo;
	}

	/**
	 * 转换JSONObject为XML
	 *
	 * @param json JSON对象或数组
	 * @return XML字符串
	 * @throws JSONException JSON解析异常
	 */
	public static String toXml(final JSON json) throws JSONException {
		return toXml(json, null);
	}

	/**
	 * 转换JSONObject为XML
	 *
	 * @param json  JSON对象或数组
	 * @param tagName 可选标签名称，名称为空时忽略标签
	 * @return A string.
	 * @throws JSONException JSON解析异常
	 */
	public static String toXml(final JSON json, final String tagName) throws JSONException {
		return toXml(json, tagName, "content");
	}

	/**
	 * 转换JSONObject为XML
	 *
	 * @param json      JSON对象或数组
	 * @param tagName     可选标签名称，名称为空时忽略标签
	 * @param contentKeys 标识为内容的key,遇到此key直接解析内容而不增加对应名称标签
	 * @return A string.
	 * @throws JSONException JSON解析异常
	 */
	public static String toXml(final JSON json, final String tagName, final String... contentKeys) throws JSONException {
		return JSONXMLSerializer.toXml(json, tagName, contentKeys);
	}
}
