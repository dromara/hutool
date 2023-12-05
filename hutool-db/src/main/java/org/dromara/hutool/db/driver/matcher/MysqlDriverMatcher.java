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

import org.dromara.hutool.core.classloader.ClassLoaderUtil;

/**
 * Mysql匹配器
 *
 * @author looly
 */
public class MysqlDriverMatcher implements DriverMatcher {

	private final ClassLoader classLoader;
	private Boolean isVersion6;

	/**
	 * 构造
	 *
	 * @param classLoader 类加载器
	 */
	public MysqlDriverMatcher(final ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public boolean test(final String jdbcUrl) {
		return jdbcUrl.startsWith("jdbc:mysql:")
			// 阿里的Mysql分布式集群
			|| jdbcUrl.startsWith("jdbc:cobar:")
			// log4jdbc for Mysql
			|| jdbcUrl.startsWith("jdbc:log4jdbc:mysql:");
	}

	@Override
	public String getClassName() {
		final String driverNameV6 = "com.mysql.cj.jdbc.Driver";
		if (isVersion6 == null) {
			isVersion6 = ClassLoaderUtil.isPresent(driverNameV6, classLoader);
		}
		return isVersion6 ? driverNameV6 : "com.mysql.jdbc.Driver";
	}
}
