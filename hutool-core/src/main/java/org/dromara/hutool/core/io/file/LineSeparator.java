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

package org.dromara.hutool.core.io.file;

/**
 * 换行符枚举<br>
 * 换行符包括：
 * <pre>
 * Mac系统换行符："\r"
 * Linux系统换行符："\n"
 * Windows系统换行符："\r\n"
 * </pre>
 *
 * @author Looly
 * @see #MAC
 * @see #LINUX
 * @see #WINDOWS
 * @since 3.1.0
 */
public enum LineSeparator {

	/**
	 * Mac系统换行符："\r"
	 */
	MAC("\r"),
	/**
	 * Linux系统换行符："\n"
	 */
	LINUX("\n"),
	/**
	 * Windows系统换行符："\r\n"
	 */
	WINDOWS("\r\n");

	private final String value;

	/**
	 * 构造
	 *
	 * @param lineSeparator 换行符
	 */
	LineSeparator(final String lineSeparator) {
		this.value = lineSeparator;
	}

	/**
	 * 获取换行符值
	 *
	 * @return 值
	 */
	public String getValue() {
		return this.value;
	}
}
