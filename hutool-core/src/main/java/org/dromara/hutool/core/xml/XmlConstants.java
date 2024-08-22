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

package org.dromara.hutool.core.xml;

import org.dromara.hutool.core.text.CharUtil;

import java.util.regex.Pattern;

/**
 * XML相关常量
 *
 * @author looly
 * @since 6.0.0
 */
public class XmlConstants {
	/**
	 * 字符串常量：XML 不间断空格转义 {@code "&nbsp;" -> " "}
	 */
	public static final String NBSP = "&nbsp;";

	/**
	 * 字符串常量：XML And 符转义 {@code "&amp;" -> "&"}
	 */
	public static final String AMP = "&amp;";
	/**
	 * The Character '&amp;'.
	 */
	public static final Character C_AMP = CharUtil.AMP;

	/**
	 * 字符串常量：XML 双引号转义 {@code "&quot;" -> "\""}
	 */
	public static final String QUOTE = "&quot;";

	/**
	 * 字符串常量：XML 单引号转义 {@code "&apos" -> "'"}
	 */
	public static final String APOS = "&apos;";
	/**
	 * The Character '''.
	 */
	public static final Character C_APOS = CharUtil.SINGLE_QUOTE;

	/**
	 * 字符串常量：XML 小于号转义 {@code "&lt;" -> "<"}
	 */
	public static final String LT = "&lt;";

	/**
	 * The Character '&lt;'.
	 */
	public static final Character C_LT = '<';

	/**
	 * 字符串常量：XML 大于号转义 {@code "&gt;" -> ">"}
	 */
	public static final String GT = "&gt;";

	/**
	 * The Character '&gt;'.
	 */
	public static final Character C_GT = '>';

	/**
	 * The Character '!'.
	 */
	public static final Character C_BANG = '!';

	/**
	 * The Character '?'.
	 */
	public static final Character C_QUEST = '?';

	/**
	 * 在XML中无效的字符 正则
	 */
	public static final Pattern INVALID_PATTERN = Pattern.compile("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]");
	/**
	 * 在XML中注释的内容 正则
	 */
	public static final Pattern COMMENT_PATTERN = Pattern.compile("(?s)<!--.+?-->");
	/**
	 * XML格式化输出默认缩进量
	 */
	public static final int INDENT_DEFAULT = 2;
}
