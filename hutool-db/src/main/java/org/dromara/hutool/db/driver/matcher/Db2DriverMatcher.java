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
