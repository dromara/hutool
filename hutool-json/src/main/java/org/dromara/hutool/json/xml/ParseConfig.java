/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
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

import java.io.Serializable;

/**
 * XML解析为JSON的可选选项<br>
 * 参考：https://github.com/stleary/JSON-java/blob/master/src/main/java/org/json/ParserConfiguration.java
 *
 * @author AylwardJ, Looly
 */
public class ParseConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 默认最大嵌套深度
	 */
	public static final int DEFAULT_MAXIMUM_NESTING_DEPTH = 512;

	/**
	 * 创建ParseConfig
	 *
	 * @return ParseConfig
	 */
	public static ParseConfig of() {
		return new ParseConfig();
	}

	/**
	 * 是否保持值为String类型，如果为{@code false}，则尝试转换为对应类型(numeric, boolean, string)
	 */
	private boolean keepStrings;
	/**
	 * 最大嵌套深度，用于解析时限制解析层级，当大于这个层级时抛出异常，-1表示无限制
	 */
	private int maxNestingDepth = -1;

	/**
	 * 是否保持值为String类型，如果为{@code false}，则尝试转换为对应类型(numeric, boolean, string)
	 *
	 * @return 是否保持值为String类型
	 */
	public boolean isKeepStrings() {
		return keepStrings;
	}

	/**
	 * 设置是否保持值为String类型，如果为{@code false}，则尝试转换为对应类型(numeric, boolean, string)
	 *
	 * @param keepStrings 是否保持值为String类型
	 * @return this
	 */
	public ParseConfig setKeepStrings(final boolean keepStrings) {
		this.keepStrings = keepStrings;
		return this;
	}

	/**
	 * 获取最大嵌套深度，用于解析时限制解析层级，当大于这个层级时抛出异常，-1表示无限制
	 *
	 * @return 最大嵌套深度
	 */
	public int getMaxNestingDepth() {
		return maxNestingDepth;
	}

	/**
	 * 设置最大嵌套深度，用于解析时限制解析层级，当大于这个层级时抛出异常，-1表示无限制
	 *
	 * @param maxNestingDepth 最大嵌套深度
	 * @return this
	 */
	public ParseConfig setMaxNestingDepth(final int maxNestingDepth) {
		this.maxNestingDepth = maxNestingDepth;
		return this;
	}
}
