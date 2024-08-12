/*
 * Copyright (c) 2013-2024 Hutool Team.
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

package org.dromara.hutool.core.text.placeholder;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字符串模式匹配，使用${XXXXX}作为变量，例如：
 *
 * <pre>
 *     pattern: ${name}-${age}-${gender}-${country}-${province}-${city}-${status}
 *     text:    "小明-19-男-中国-河南-郑州-已婚"
 *     result:  {name=小明, age=19, gender=男, country=中国, province=河南, city=郑州, status=已婚}
 * </pre>
 *
 * @author looly
 * @since 5.6.0
 */
public class StrMatcher {

	private final List<String> patterns;

	/**
	 * 构造
	 *
	 * @param pattern 模式，变量用${XXX}占位
	 */
	public StrMatcher(final String pattern) {
		this.patterns = parse(pattern);
	}

	/**
	 * 匹配并提取匹配到的内容
	 *
	 * @param text 被匹配的文本
	 * @return 匹配的map，key为变量名，value为匹配到的值
	 */
	public Map<String, String> match(final String text) {
		final HashMap<String, String> result = MapUtil.newHashMap(true);
		int from = 0;
		String key = null;
		int to;
		for (final String part : patterns) {
			if (StrUtil.isWrap(part, "${", "}")) {
				// 变量
				key = StrUtil.sub(part, 2, part.length() - 1);
			} else {
				to = text.indexOf(part, from);
				if (to < 0) {
					//普通字符串未匹配到，说明整个模式不能匹配，返回空
					return MapUtil.empty();
				}
				if (null != key && to > from) {
					// 变量对应部分有内容
					result.put(key, text.substring(from, to));
				}
				// 下一个起始点是普通字符串的末尾
				from = to + part.length();
				key = null;
			}
		}

		if (null != key && from < text.length()) {
			// 变量对应部分有内容
			result.put(key, text.substring(from));
		}

		return result;
	}

	/**
	 * 解析表达式
	 *
	 * @param pattern 表达式，使用${XXXX}作为变量占位符
	 * @return 表达式
	 */
	private static List<String> parse(final String pattern) {
		final List<String> patterns = new ArrayList<>();
		final int length = pattern.length();
		char c = 0;
		char pre;
		boolean inVar = false;
		final StringBuilder part = StrUtil.builder();
		for (int i = 0; i < length; i++) {
			pre = c;
			c = pattern.charAt(i);
			if (inVar) {
				part.append(c);
				if ('}' == c) {
					// 变量结束
					inVar = false;
					patterns.add(part.toString());
					part.setLength(0);
				}
			} else if ('{' == c && '$' == pre) {
				// 变量开始
				inVar = true;
				final String preText = part.substring(0, part.length() - 1);
				if (StrUtil.isNotEmpty(preText)) {
					patterns.add(preText);
				}
				part.setLength(0);
				part.append(pre).append(c);
			} else {
				// 普通字符
				part.append(c);
			}
		}

		if (part.length() > 0) {
			patterns.add(part.toString());
		}
		return patterns;
	}
}
