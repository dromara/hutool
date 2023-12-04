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

package org.dromara.hutool.db.driver;

import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class DriverUtilTest {

	@Test
	public void identifyH2DriverTest(){
		final String url = "jdbc:h2:file:./db/test;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE;MODE=MYSQL";
		final String driver = DriverUtil.identifyDriver(url); // driver 返回 h2 的 driver
		Assertions.assertEquals("org.h2.Driver", driver);
	}

	@Test
	public void identifyDriverTest() {
		final Map<String, String> map = new HashMap<>(25);
		map.put("jdbc:mysql:", "com.mysql.cj.jdbc.Driver");
		map.put("jdbc:cobar:", "com.mysql.cj.jdbc.Driver");
		map.put("jdbc:oracle:", "oracle.jdbc.OracleDriver");
		map.put("jdbc:postgresql:", "org.postgresql.Driver");
		map.put("jdbc:sqlite:", "org.sqlite.JDBC");
		map.put("jdbc:sqlserver:", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
		map.put("jdbc:microsoft:", "com.microsoft.jdbc.sqlserver.SQLServerDriver");
		map.put("jdbc:h2:", "org.h2.Driver");
		map.put("jdbc:derby:", "org.apache.derby.jdbc.EmbeddedDriver");
		map.put("jdbc:hsqldb:", "org.hsqldb.jdbcDriver");
		map.put("jdbc:dm:", "dm.jdbc.driver.DmDriver");
		map.put("jdbc:kingbase8:", "com.kingbase8.Driver");
		map.put("jdbc:ignite:thin:", "org.apache.ignite.IgniteJdbcThinDriver");
		map.put("jdbc:clickhouse:", "com.clickhouse.jdbc.ClickHouseDriver");
		map.put("jdbc:highgo:", "com.highgo.jdbc.Driver");
		map.put("jdbc:db2:", "COM.ibm.db2.jdbc.app.DB2Driver");
		map.put("jdbc:xugu:", "com.xugu.cloudjdbc.Driver");
		map.put("jdbc:phoenix:", "org.apache.phoenix.jdbc.PhoenixDriver");
		map.put("jdbc:zenith:", "com.huawei.gauss.jdbc.ZenithDriver");
		map.put("jdbc:gbase:", "com.gbase.jdbc.Driver");
		map.put("jdbc:oscar:", "com.oscar.Driver");
		map.put("jdbc:sybase:Tds:", "com.sybase.jdbc4.jdbc.SybDriver");
		map.put("jdbc:mariadb:", "org.mariadb.jdbc.Driver");
		map.put("jdbc:hive2:", "org.apache.hive.jdbc.HiveDriver");
		map.put("jdbc:hive:", "org.apache.hadoop.hive.jdbc.HiveDriver");

		map.forEach((k, v) -> Assertions.assertEquals(v,
			DriverUtil.identifyDriver(k + RandomUtil.randomStringLower(2))));
	}
}
