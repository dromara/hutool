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

import org.dromara.hutool.core.text.StrUtil;

/**
 * db2驱动匹配器，来自Druid的JdbcUtils
 *
 * @author druid
 */
public class Db2DriverMatcher implements DriverMatcher {

	private static final String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver"; // Type4
	private static final String DB2_DRIVER2 = "COM.ibm.db2.jdbc.app.DB2Driver"; // Type2
	private static final String DB2_DRIVER3 = "COM.ibm.db2.jdbc.net.DB2Driver"; // Type3

	private String jdbcUrl;

	@Override
	public boolean test(final String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
		return jdbcUrl.startsWith("jdbc:db2:");
	}

	@Override
	public String getClassName() {
		// Type2 COM.ibm.db2.jdbc.app.DB2Driver, url = jdbc:db2:databasename
		// Type3 COM.ibm.db2.jdbc.net.DB2Driver, url = jdbc:db2:ServerIP:6789:databasename
		// Type4 8.1+ com.ibm.db2.jcc.DB2Driver, url = jdbc:db2://ServerIP:50000/databasename
		final String jdbcUrl = this.jdbcUrl;
		final String prefix = "jdbc:db2:";
		if (StrUtil.isEmpty(jdbcUrl) || jdbcUrl.startsWith(prefix + "//")) { // Type4
			return DB2_DRIVER; // "com.ibm.db2.jcc.DB2Driver";
		} else {
			final String suffix = jdbcUrl.substring(prefix.length());
			if (suffix.indexOf(':') > 0) { // Type3
				return DB2_DRIVER3; // COM.ibm.db2.jdbc.net.DB2Driver
			} else { // Type2
				return DB2_DRIVER2; // COM.ibm.db2.jdbc.app.DB2Driver
			}
		}
	}
}
