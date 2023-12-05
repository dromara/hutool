/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
