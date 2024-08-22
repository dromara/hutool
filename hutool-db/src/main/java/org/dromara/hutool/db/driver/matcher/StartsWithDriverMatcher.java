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

package org.dromara.hutool.db.driver.matcher;

/**
 * 判断jdbc url的起始字符串匹配对应的驱动类名
 *
 * @author looly
 * @since 6.0.0
 */
public class StartsWithDriverMatcher implements DriverMatcher {

	private final String className;
	private final String[] startsWithStrs;

	/**
	 * 构造
	 *
	 * @param className      驱动类名
	 * @param startsWithStrs 开头字符串标记列表，或关系
	 */
	public StartsWithDriverMatcher(final String className, final String... startsWithStrs) {
		this.className = className;
		this.startsWithStrs = startsWithStrs;
	}

	@Override
	public boolean test(final String jdbcUrl) {
		for (final String startsWithStr : startsWithStrs) {
			if (jdbcUrl.startsWith(startsWithStr)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getClassName() {
		return className;
	}
}
