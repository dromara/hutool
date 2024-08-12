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

import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.lang.mutable.MutableEntry;
import org.dromara.hutool.core.map.reference.WeakConcurrentMap;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.placeholder.template.NamedPlaceholderStrTemplate;
import org.dromara.hutool.core.text.placeholder.template.SinglePlaceholderStrTemplate;

import java.util.Map;

/**
 * 字符串格式化工具
 *
 * @author Looly
 */
public class StrFormatter {
	private static final WeakConcurrentMap<Map.Entry<CharSequence, Object>, StrTemplate> CACHE = new WeakConcurrentMap<>();

	/**
	 * 格式化字符串<br>
	 * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
	 * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "a", "b") =》 this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "a", "b") =》 this is {} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "a", "b") =》 this is \a for b<br>
	 *
	 * @param strPattern 字符串模板
	 * @param argArray   参数列表
	 * @return 结果
	 */
	public static String format(final String strPattern, final Object... argArray) {
		return formatWith(strPattern, StrUtil.EMPTY_JSON, argArray);
	}

	/**
	 * 格式化字符串<br>
	 * 此方法只是简单将指定占位符 按照顺序替换为参数<br>
	 * 如果想输出占位符使用 \\转义即可，如果想输出占位符之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "{}", "a", "b") =》 this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "{}", "a", "b") =》 this is {} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "{}", "a", "b") =》 this is \a for b<br>
	 *
	 * @param strPattern  字符串模板
	 * @param placeHolder 占位符，例如{}
	 * @param argArray    参数列表
	 * @return 结果
	 * @since 5.7.14
	 */
	public static String formatWith(final String strPattern, final String placeHolder, final Object... argArray) {
		if (StrUtil.isBlank(strPattern) || StrUtil.isBlank(placeHolder) || ArrayUtil.isEmpty(argArray)) {
			return strPattern;
		}
		return ((SinglePlaceholderStrTemplate) CACHE.computeIfAbsent(MutableEntry.of(strPattern, placeHolder), k ->
				StrTemplate.of(strPattern).placeholder(placeHolder).build()))
				.format(argArray);
	}

	/**
	 * 格式化文本，使用 {varName} 占位<br>
	 * map = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
	 *
	 * @param template   文本模板，被替换的部分用 {key} 表示
	 * @param map        参数值对
	 * @param ignoreNull 是否忽略 {@code null} 值，忽略则 {@code null} 值对应的变量不被替换，否则替换为""
	 * @return 格式化后的文本
	 * @since 5.7.10
	 */
	public static String format(final CharSequence template, final Map<?, ?> map, final boolean ignoreNull) {
		if (null == template) {
			return null;
		}
		if (null == map || map.isEmpty()) {
			return template.toString();
		}

		return ((NamedPlaceholderStrTemplate) CACHE.computeIfAbsent(MutableEntry.of(template, ignoreNull), k -> {
			final NamedPlaceholderStrTemplate.Builder builder = StrTemplate.ofNamed(template.toString());
			if (ignoreNull) {
				builder.addFeatures(StrTemplate.Feature.FORMAT_NULL_VALUE_TO_WHOLE_PLACEHOLDER);
			} else {
				builder.addFeatures(StrTemplate.Feature.FORMAT_NULL_VALUE_TO_EMPTY);
			}
			return builder.build();
		})).format(map);
	}

	/**
	 * 清空缓存
	 */
	public static void clear() {
		CACHE.clear();
	}
}
