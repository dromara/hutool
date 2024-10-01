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

package org.dromara.hutool.json.support;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;

/**
 * JSON格式化风格，用于格式化JSON字符串
 *
 * @author looly
 * @since 6.0.0
 */
public class JSONFormatStyle {

	/**
	 * 获取格式化风格
	 *
	 * @param indentFactor 缩进因子，定义每一级别增加的缩进量
	 * @return JSONFormatStyle
	 */
	public static JSONFormatStyle getStyle(final int indentFactor){
		if(0 == indentFactor){
			return JSONFormatStyle.COMPACT;
		} else if(2 == indentFactor){
			return JSONFormatStyle.PRETTY;
		}
		return new JSONFormatStyle("\n", StrUtil.repeat(CharUtil.SPACE, indentFactor), indentFactor > 0);
	}

	/**
	 * 默认紧凑风格:
	 *
	 * <ul>
	 *   <li>无换行</li>
	 *   <li>无缩进</li>
	 *   <li>{@code ','} 和 {@code ':'}后无空格</li>
	 * </ul>
	 */
	public static final JSONFormatStyle COMPACT = new JSONFormatStyle("", "", false);

	/**
	 * 默认格式化风格:
	 *
	 * <ul>
	 *   <li>换行符：{@code "\n"}</li>
	 *   <li>双空格缩进</li>
	 *   <li>{@code ','} 和 {@code ':'}后加空格</li>
	 * </ul>
	 */
	public static final JSONFormatStyle PRETTY = new JSONFormatStyle("\n", "  ", true);

	/**
	 * 换行符，可以是 "\n" 或 "\r\n"的一个或多个
	 */
	private final String newline;
	/**
	 * 缩进符，如空格、制表符等
	 */
	private final String indent;
	/**
	 * {@code ','} 和 {@code ':'}分隔符后是否加空格，加空格结果如：{"a": 1}
	 */
	private final boolean spaceAfterSeparators;

	/**
	 * 构造
	 *
	 * @param newline              换行符
	 * @param indent               缩进符
	 * @param spaceAfterSeparators 分隔符后是否加空格
	 */
	public JSONFormatStyle(final String newline, final String indent, final boolean spaceAfterSeparators) {
		this.newline = Assert.notNull(newline);
		this.indent = Assert.notNull(indent);;
		this.spaceAfterSeparators = spaceAfterSeparators;
	}

	/**
	 * 获取换行符
	 *
	 * @return 换行符
	 */
	public String getNewline() {
		return newline;
	}

	/**
	 * 获取缩进符
	 *
	 * @return 缩进符
	 */
	public String getIndent() {
		return indent;
	}

	/**
	 * 分隔符后是否加空格
	 *
	 * @return 分隔符后是否加空格
	 */
	public boolean isSpaceAfterSeparators() {
		return spaceAfterSeparators;
	}
}
