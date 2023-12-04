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
 * 驱动匹配接口，通过实现此接口，可以：<br>
 * 通过{@link #isMatch(String)} 判断JDBC URL 是否匹配驱动的要求<br>
 * 通过{@link #getClassName()} 获取对应的驱动类名称
 *
 * @author looly
 */
public interface DriverMatcher {

	/**
	 * 自定义规则是否匹配 JDBC URL
	 *
	 * @param jdbcUrl JDBC URL
	 * @return 是否匹配
	 */
	boolean isMatch(final String jdbcUrl);

	/**
	 * 获取对应的驱动类名称
	 *
	 * @return 对应的驱动类名称
	 */
	String getClassName();
}
