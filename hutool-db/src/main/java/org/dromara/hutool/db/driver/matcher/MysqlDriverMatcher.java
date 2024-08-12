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
