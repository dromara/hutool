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

package org.dromara.hutool.json.xml;

import org.dromara.hutool.core.text.CharUtil;
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
	 * The Character '&amp;'.
	 */
	public static final Character AMP = CharUtil.AMP;

	/**
	 * The Character '''.
	 */
	public static final Character APOS = CharUtil.SINGLE_QUOTE;

	/**
	 * The Character '!'.
	 */
	public static final Character BANG = '!';

	/**
	 * The Character '='.
	 */
	public static final Character EQ = '=';

	/**
	 * The Character '&gt;'.
	 */
	public static final Character GT = '>';

	/**
	 * The Character '&lt;'.
	 */
	public static final Character LT = '<';

	/**
	 * The Character '?'.
	 */
	public static final Character QUEST = '?';

	/**
	 * The Character '"'.
	 */
	public static final Character QUOT = CharUtil.DOUBLE_QUOTES;

	/**
	 * The Character '/'.
	 */
	public static final Character SLASH = CharUtil.SLASH;

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
		return toJSONObject(string, false);
	}

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 * Content text may be placed in a "content" member. Comments, prologs, DTDs, and {@code <[ [ ]]>} are ignored.
	 * All values are converted as strings, for 1, 01, 29.0 will not be coerced to numbers but will instead be the exact value as seen in the XML document.
	 *
	 * @param string      The source string.
	 * @param keepStrings If true, then values will not be coerced into boolean or numeric values and will instead be left as strings
	 * @return A JSONObject containing the structured data from the XML string.
	 * @throws JSONException Thrown if there is an errors while parsing the string
	 */
	public static JSONObject toJSONObject(final String string, final boolean keepStrings) throws JSONException {
		return toJSONObject(new JSONObject(), string, keepStrings);
	}

	/**
	 * 转换XML为JSONObject
	 * 转换过程中一些信息可能会丢失，JSON中无法区分节点和属性，相同的节点将被处理为JSONArray。
	 *
	 * @param jo          JSONObject
	 * @param xmlStr      XML字符串
	 * @param keepStrings 如果为{@code true}，则值保持String类型，不转换为数字或boolean
	 * @return A JSONObject 解析后的JSON对象，与传入的jo为同一对象
	 * @throws JSONException 解析异常
	 * @since 5.3.1
	 */
	public static JSONObject toJSONObject(final JSONObject jo, final String xmlStr, final boolean keepStrings) throws JSONException {
		JSONXMLParser.parseJSONObject(jo, xmlStr, keepStrings);
		return jo;
	}

	/**
	 * 转换JSONObject为XML
	 *
	 * @param object JSON对象或数组
	 * @return XML字符串
	 * @throws JSONException JSON解析异常
	 */
	public static String toXml(final Object object) throws JSONException {
		return toXml(object, null);
	}

	/**
	 * 转换JSONObject为XML
	 *
	 * @param object  JSON对象或数组
	 * @param tagName 可选标签名称，名称为空时忽略标签
	 * @return A string.
	 * @throws JSONException JSON解析异常
	 */
	public static String toXml(final Object object, final String tagName) throws JSONException {
		return toXml(object, tagName, "content");
	}

	/**
	 * 转换JSONObject为XML
	 *
	 * @param object      JSON对象或数组
	 * @param tagName     可选标签名称，名称为空时忽略标签
	 * @param contentKeys 标识为内容的key,遇到此key直接解析内容而不增加对应名称标签
	 * @return A string.
	 * @throws JSONException JSON解析异常
	 */
	public static String toXml(final Object object, final String tagName, final String... contentKeys) throws JSONException {
		return JSONXMLSerializer.toXml(object, tagName, contentKeys);
	}
}
