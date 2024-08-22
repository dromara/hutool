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

package org.dromara.hutool.db.driver;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.driver.matcher.Db2DriverMatcher;
import org.dromara.hutool.db.driver.matcher.DriverMatcher;
import org.dromara.hutool.db.driver.matcher.MysqlDriverMatcher;
import org.dromara.hutool.db.driver.matcher.StartsWithDriverMatcher;

import java.util.List;

/**
 * 驱动识别器<br>
 * 通过给定的规则列表，逐个查找，直到匹配到{@link DriverMatcher}后，获取其驱动名<br>
 * 部分驱动参考Druid实现
 *
 * @author looly
 * @since 6.0.0
 */
public class DriverIdentifier implements DriverNames{

	/**
	 * 单例驱动识别器
	 */
	public static DriverIdentifier INSTANCE = new DriverIdentifier(null);

	private final List<DriverMatcher> matcherList;

	/**
	 * 构造
	 *
	 * @param classLoader 类加载器
	 */
	public DriverIdentifier(final ClassLoader classLoader) {
		this.matcherList = ListUtil.of(
			// Mysql
			new MysqlDriverMatcher(classLoader),
			// Mariadb
			new StartsWithDriverMatcher(DRIVER_MARIADB, "jdbc:mariadb:"),
			// Oracle
			new StartsWithDriverMatcher(DRIVER_ORACLE, "jdbc:oracle:", "JDBC:oracle:"),
			new StartsWithDriverMatcher("com.alibaba.jdbc.AlibabaDriver", "jdbc:alibaba:oracle:"),
			// PostgreSQL
			new StartsWithDriverMatcher(DRIVER_POSTGRESQL, "jdbc:postgresql:"),
			// SQLServer
			new StartsWithDriverMatcher(DRIVER_SQLSERVER, "jdbc:sqlserver:"),
			new StartsWithDriverMatcher(DRIVER_SQLSERVER_OLD, "jdbc:microsoft:"),
			// SQLite3
			new StartsWithDriverMatcher(DRIVER_SQLLITE3, "jdbc:sqlite:"),
			// H2
			new StartsWithDriverMatcher(DRIVER_H2, "jdbc:h2:"),
			// Hive
			new StartsWithDriverMatcher(DRIVER_HIVE, "jdbc:hive:"),
			new StartsWithDriverMatcher(DRIVER_HIVE2, "jdbc:hive2:"),
			// Apache Derby
			new StartsWithDriverMatcher(DRIVER_DERBY_EMBEDDED, "jdbc:derby:"),
			// log4jdbc
			new StartsWithDriverMatcher(DRIVER_LOG4J, "jdbc:log4jdbc:"),
			// tidb
			new StartsWithDriverMatcher(DRIVER_TIDB, "jdbc:tidb:"),
			// oceanbase
			new StartsWithDriverMatcher(DRIVER_OCEANBASE, "jdbc:oceanbase:"),
			// SyBase JConnect，见：https://infocenter.sybase.com/help/index.jsp?topic=/com.sybase.infocenter.dc39001.0700/html/prjdbc0700/X28208.htm
			new StartsWithDriverMatcher(DRIVER_SYBASE, "jdbc:sybase:Tds:"),
			new StartsWithDriverMatcher(DRIVER_JTDS, "jdbc:jtds:"),
			new StartsWithDriverMatcher(DRIVER_DRUID, "jdbc:fake:", "jdbc:mock:"),
			new StartsWithDriverMatcher(DRIVER_EDB, "jdbc:edb:"),
			new StartsWithDriverMatcher(DRIVER_ODPS, "jdbc:odps:"),
			// HSQLDB
			new StartsWithDriverMatcher(DRIVER_HSQLDB, "jdbc:hsqldb:"),
			new Db2DriverMatcher(),
			new StartsWithDriverMatcher(DRIVER_INGRES, "jdbc:ingres:"),
			new StartsWithDriverMatcher(DRIVER_MCKOI, "jdbc:mckoi:"),
			new StartsWithDriverMatcher(DRIVER_CLOUDSCAPE, "jdbc:cloudscape:"),
			new StartsWithDriverMatcher(DRIVER_INFORMIX, "jdbc:informix-sqli:"),
			new StartsWithDriverMatcher(DRIVER_TIMESTEN, "jdbc:timesten:"),
			new StartsWithDriverMatcher(DRIVER_AS400, "jdbc:as400:"),
			new StartsWithDriverMatcher(DRIVER_ATTUNITY, "jdbc:attconnect:"),
			new StartsWithDriverMatcher(DRIVER_JSQL, "jdbc:JSQLConnect:"),
			new StartsWithDriverMatcher(DRIVER_JTURBO, "jdbc:JTurbo:"),
			new StartsWithDriverMatcher(DRIVER_INTERBASE, "jdbc:interbase:"),
			new StartsWithDriverMatcher(DRIVER_POINTBASE, "jdbc:pointbase:"),
			new StartsWithDriverMatcher(DRIVER_EDBC, "jdbc:edbc:"),
			new StartsWithDriverMatcher(DRIVER_MIMER, "jdbc:mimer:multi1:"),
			// Apache Ignite
			new StartsWithDriverMatcher(DRIVER_IGNITE_THIN, "jdbc:ignite:thin:"),
			// 达梦7
			new StartsWithDriverMatcher(DRIVER_DM, "jdbc:dm:"),
			// 人大金仓
			new StartsWithDriverMatcher(DRIVER_KINGBASE, "jdbc:kingbase:"),
			// 人大金仓8
			new StartsWithDriverMatcher(DRIVER_KINGBASE8, "jdbc:kingbase8:"),
			// 南大通用
			new StartsWithDriverMatcher(DRIVER_GBASE, "jdbc:gbase:"),
			// 虚谷
			new StartsWithDriverMatcher(DRIVER_XUGU, "jdbc:xugu:"),
			// 神通
			new StartsWithDriverMatcher(DRIVER_OSCAR, "jdbc:oscar:"),
			// Apache Phoenix轻客户端
			new StartsWithDriverMatcher(DRIVER_PHOENIX_THIN, "jdbc:phoenix:thin:"),
			// Apache Phoenix重客户端（在轻客户端后检测）
			new StartsWithDriverMatcher(DRIVER_PHOENIX, "jdbc:phoenix:"),
			new StartsWithDriverMatcher(DRIVER_KYLIN, "jdbc:kylin:"),
			new StartsWithDriverMatcher(DRIVER_ELASTIC, "jdbc:elastic:"),
			new StartsWithDriverMatcher(DRIVER_CLICK_HOUSE, "jdbc:clickhouse:"),
			new StartsWithDriverMatcher(DRIVER_PRESTO, "jdbc:presto:"),
			new StartsWithDriverMatcher(DRIVER_TRINO, "jdbc:trino:"),
			// 浪潮K-DB
			new StartsWithDriverMatcher(DRIVER_INSPUR, "jdbc:inspur:"),
			new StartsWithDriverMatcher(DRIVER_POLARDB, "jdbc:polardb"),
			// 瀚高
			new StartsWithDriverMatcher(DRIVER_HIGHGO, "jdbc:highgo:"),
			new StartsWithDriverMatcher(DRIVER_GREENPLUM, "jdbc:pivotal:greenplum:"),
			// 华为OpenGauss
			new StartsWithDriverMatcher(DRIVER_GAUSS, "jdbc:zenith:"),
			new StartsWithDriverMatcher(DRIVER_OPENGAUSS, "jdbc:opengauss:")
		);
	}

	/**
	 * 通过JDBC URL等信息识别JDBC驱动名
	 *
	 * @param jdbcUrl JDBC URL
	 * @return 驱动类名，未识别到返回{@code null}
	 */
	public String identifyDriver(final String jdbcUrl) {
		if (StrUtil.isBlank(jdbcUrl)) {
			return null;
		}

		return this.matcherList.stream()
			.filter(driverMatcher -> driverMatcher.test(jdbcUrl))
			.findFirst()
			.map(DriverMatcher::getClassName).orElse(null);
	}

	/**
	 * 自定义增加{@link DriverMatcher}
	 *
	 * @param matcher {@link DriverMatcher}
	 * @return this
	 */
	public DriverIdentifier addMatcher(final DriverMatcher matcher) {
		if (null != matcher) {
			this.matcherList.add(matcher);
		}
		return this;
	}
}
