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
