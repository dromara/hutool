package cn.hutool.json.xml;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;

/**
 * JSON转XML字符串工具
 *
 * @author looly
 * @since 5.7.11
 */
public class JSONXMLSerializer {
	/**
	 * 转换JSONObject为XML
	 * Convert a JSONObject into a well-formed, element-normal XML string.
	 *
	 * @param object A JSONObject.
	 * @return A string.
	 * @throws JSONException Thrown if there is an error parsing the string
	 */
	public static String toXml(Object object) throws JSONException {
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
	public static String toXml(Object object, String tagName) throws JSONException {
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
	public static String toXml(Object object, String tagName, String... contentKeys) throws JSONException {
		if (null == object) {
			return null;
		}

		final StringBuilder sb = new StringBuilder();
		if (object instanceof JSONObject) {

			// Emit <tagName>
			appendTag(sb, tagName, false);

			// Loop thru the keys.
			((JSONObject) object).forEach((key, value) -> {
				if (ArrayUtil.isArray(value)) {
					value = new JSONArray(value);
				}

				// Emit content in body
				if (ArrayUtil.contains(contentKeys, key)) {
					if (value instanceof JSONArray) {
						int i = 0;
						for (Object val : (JSONArray) value) {
							if (i > 0) {
								sb.append(CharUtil.LF);
							}
							sb.append(EscapeUtil.escapeXml(val.toString()));
							i++;
						}
					} else {
						sb.append(EscapeUtil.escapeXml(value.toString()));
					}

					// Emit an array of similar keys

				} else if (StrUtil.isEmptyIfStr(value)) {
					sb.append(wrapWithTag(null, key));
				} else if (value instanceof JSONArray) {
					for (Object val : (JSONArray) value) {
						if (val instanceof JSONArray) {
							sb.append(wrapWithTag(toXml(val), key));
						} else {
							sb.append(toXml(val, key));
						}
					}
				} else {
					sb.append(toXml(value, key));
				}
			});

			// Emit the </tagname> close tag
			appendTag(sb, tagName, true);
			return sb.toString();
		}

		if (ArrayUtil.isArray(object)) {
			object = new JSONArray(object);
		}

		if (object instanceof JSONArray) {
			for (Object val : (JSONArray) object) {
				// XML does not have good support for arrays. If an array
				// appears in a place where XML is lacking, synthesize an
				// <array> element.
				sb.append(toXml(val, tagName == null ? "array" : tagName));
			}
			return sb.toString();
		}

		return wrapWithTag(EscapeUtil.escapeXml(object.toString()), tagName);
	}

	/**
	 * 追加标签
	 *
	 * @param sb       XML内容
	 * @param tagName  标签名
	 * @param isEndTag 是否结束标签
	 * @since 5.7.11
	 */
	private static void appendTag(StringBuilder sb, String tagName, boolean isEndTag) {
		if (StrUtil.isNotBlank(tagName)) {
			sb.append('<');
			if (isEndTag) {
				sb.append('/');
			}
			sb.append(tagName).append('>');
		}
	}

	/**
	 * 将内容使用标签包装为XML
	 *
	 * @param tagName 标签名
	 * @param content 内容
	 * @return 包装后的XML
	 * @since 5.7.11
	 */
	private static String wrapWithTag(String content, String tagName) {
		if (StrUtil.isBlank(tagName)) {
			return StrUtil.wrap(content, "\"");
		}

		if (StrUtil.isEmpty(content)) {
			return "<" + tagName + "/>";
		} else {
			return "<" + tagName + ">" + content + "</" + tagName + ">";
		}
	}
}
