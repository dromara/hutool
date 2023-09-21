/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.dialect;

import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.dromara.hutool.db.dialect.DriverNamePool.*;

public class DialectFactoryTest {

	@Test
	public void identifyDriverTest(){
		final Map<String,String> map = new HashMap<>(25);
		map.put("mysql",DRIVER_MYSQL_V6);
		map.put("cobar",DRIVER_MYSQL_V6);
		map.put("oracle",DRIVER_ORACLE);
		map.put("postgresql",DRIVER_POSTGRESQL);
		map.put("sqlite",DRIVER_SQLLITE3);
		map.put("sqlserver",DRIVER_SQLSERVER);
		map.put("microsoft",DRIVER_SQLSERVER);
		map.put("h2",DRIVER_H2);
		map.put("derby",DRIVER_DERBY);
		map.put("hsqldb",DRIVER_HSQLDB);
		map.put("dm",DRIVER_DM7);
		map.put("kingbase8",DRIVER_KINGBASE8);
		map.put("ignite",DRIVER_IGNITE_THIN);
		map.put("clickhouse",DRIVER_CLICK_HOUSE);
		map.put("highgo",DRIVER_HIGHGO);
		map.put("db2",DRIVER_DB2);
		map.put("xugu",DRIVER_XUGU);
		map.put("phoenix",DRIVER_PHOENIX);
		map.put("zenith",DRIVER_GAUSS);
		map.put("gbase",DRIVER_GBASE);
		map.put("oscar",DRIVER_OSCAR);
		map.put("sybase",DRIVER_SYBASE);
		map.put("mariadb",DRIVER_MARIADB);

		// 单元测试歧义
		//map.put("hive2",DRIVER_HIVE2);
		//map.put("hive",DRIVER_HIVE);

		map.forEach((k,v) -> Assertions.assertEquals(v,
				DialectFactory.identifyDriver(k+ RandomUtil.randomStringLower(2),null) ));

	}
}
