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
public class DriverIdentifier {

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
			new StartsWithDriverMatcher("org.mariadb.jdbc.Driver", "jdbc:mariadb:"),
			// Oracle
			new StartsWithDriverMatcher("oracle.jdbc.OracleDriver", "jdbc:oracle:", "JDBC:oracle:"),
			new StartsWithDriverMatcher("com.alibaba.jdbc.AlibabaDriver", "jdbc:alibaba:oracle:"),
			// PostgreSQL
			new StartsWithDriverMatcher("org.postgresql.Driver", "jdbc:postgresql:"),
			// SQLServer
			new StartsWithDriverMatcher("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver:"),
			new StartsWithDriverMatcher("com.microsoft.jdbc.sqlserver.SQLServerDriver", "jdbc:microsoft:"),
			// SQLite3
			new StartsWithDriverMatcher("org.sqlite.JDBC", "jdbc:sqlite:"),
			// H2
			new StartsWithDriverMatcher("org.h2.Driver", "jdbc:h2:"),
			// Hive
			new StartsWithDriverMatcher("org.apache.hadoop.hive.jdbc.HiveDriver", "jdbc:hive:"),
			new StartsWithDriverMatcher("org.apache.hive.jdbc.HiveDriver", "jdbc:hive2:"),
			// Apache Derby
			new StartsWithDriverMatcher("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:"),
			// log4jdbc
			new StartsWithDriverMatcher("net.sf.log4jdbc.DriverSpy", "jdbc:log4jdbc:"),
			// tidb
			new StartsWithDriverMatcher("io.tidb.bigdata.jdbc.TiDBDriver", "jdbc:tidb:"),
			// oceanbase
			new StartsWithDriverMatcher("com.oceanbase.jdbc.Driver", "jdbc:oceanbase:"),
			// SyBase JConnect，见：https://infocenter.sybase.com/help/index.jsp?topic=/com.sybase.infocenter.dc39001.0700/html/prjdbc0700/X28208.htm
			new StartsWithDriverMatcher("com.sybase.jdbc4.jdbc.SybDriver", "jdbc:sybase:Tds:"),
			new StartsWithDriverMatcher("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:"),
			new StartsWithDriverMatcher("com.alibaba.druid.mock.MockDriver", "jdbc:fake:", "jdbc:mock:"),
			new StartsWithDriverMatcher("com.edb.Driver", "jdbc:edb:"),
			new StartsWithDriverMatcher("com.aliyun.odps.jdbc.OdpsDriver", "jdbc:odps:"),
			// HSQLDB
			new StartsWithDriverMatcher("org.hsqldb.jdbcDriver", "jdbc:hsqldb:"),
			new Db2DriverMatcher(),
			new StartsWithDriverMatcher("com.ingres.jdbc.IngresDriver", "jdbc:ingres:"),
			new StartsWithDriverMatcher("com.mckoi.JDBCDriver", "jdbc:mckoi:"),
			new StartsWithDriverMatcher("COM.cloudscape.core.JDBCDriver", "jdbc:cloudscape:"),
			new StartsWithDriverMatcher("com.informix.jdbc.IfxDriver", "jdbc:informix-sqli:"),
			new StartsWithDriverMatcher("com.timesten.jdbc.TimesTenDriver", "jdbc:timesten:"),
			new StartsWithDriverMatcher("com.ibm.as400.access.AS400JDBCDriver", "jdbc:as400:"),
			new StartsWithDriverMatcher("com.attunity.jdbc.NvDriver", "jdbc:attconnect:"),
			new StartsWithDriverMatcher("com.jnetdirect.jsql.JSQLDriver", "jdbc:JSQLConnect:"),
			new StartsWithDriverMatcher("com.newatlanta.jturbo.driver.Driver", "jdbc:JTurbo:"),
			new StartsWithDriverMatcher("interbase.interclient.Driver", "jdbc:interbase:"),
			new StartsWithDriverMatcher("com.pointbase.jdbc.jdbcUniversalDriver", "jdbc:pointbase:"),
			new StartsWithDriverMatcher("ca.edbc.jdbc.EdbcDriver", "jdbc:edbc:"),
			new StartsWithDriverMatcher("com.mimer.jdbc.Driver", "jdbc:mimer:multi1:"),
			// Apache Ignite
			new StartsWithDriverMatcher("org.apache.ignite.IgniteJdbcThinDriver", "jdbc:ignite:thin:"),
			// 达梦7
			new StartsWithDriverMatcher("dm.jdbc.driver.DmDriver", "jdbc:dm:"),
			// 人大金仓
			new StartsWithDriverMatcher("com.kingbase.Driver", "jdbc:kingbase:"),
			// 人大金仓8
			new StartsWithDriverMatcher("com.kingbase8.Driver", "jdbc:kingbase8:"),
			// 南大通用
			new StartsWithDriverMatcher("com.gbase.jdbc.Driver", "jdbc:gbase:"),
			// 虚谷
			new StartsWithDriverMatcher("com.xugu.cloudjdbc.Driver", "jdbc:xugu:"),
			// 神通
			new StartsWithDriverMatcher("com.oscar.Driver", "jdbc:oscar:"),
			// Apache Phoenix轻客户端
			new StartsWithDriverMatcher("org.apache.phoenix.queryserver.client.Driver", "jdbc:phoenix:thin:"),
			// Apache Phoenix重客户端（在轻客户端后检测）
			new StartsWithDriverMatcher("org.apache.phoenix.jdbc.PhoenixDriver", "jdbc:phoenix:"),
			new StartsWithDriverMatcher("org.apache.kylin.jdbc.Driver", "jdbc:kylin:"),
			new StartsWithDriverMatcher("com.alibaba.xdriver.elastic.jdbc.ElasticDriver", "jdbc:elastic:"),
			new StartsWithDriverMatcher("com.clickhouse.jdbc.ClickHouseDriver", "jdbc:clickhouse:"),
			new StartsWithDriverMatcher("com.facebook.presto.jdbc.PrestoDriver", "jdbc:presto:"),
			new StartsWithDriverMatcher("io.trino.jdbc.TrinoDriver", "jdbc:trino:"),
			// 浪潮K-DB
			new StartsWithDriverMatcher("com.inspur.jdbc.KdDriver", "jdbc:inspur:"),
			new StartsWithDriverMatcher("com.aliyun.polardb.Driver", "jdbc:polardb"),
			// 瀚高
			new StartsWithDriverMatcher("com.highgo.jdbc.Driver", "jdbc:highgo:"),
			new StartsWithDriverMatcher("com.pivotal.jdbc.GreenplumDriver", "jdbc:pivotal:greenplum:"),
			// 华为OpenGauss
			new StartsWithDriverMatcher("com.huawei.gauss.jdbc.ZenithDriver", "jdbc:zenith:"),
			new StartsWithDriverMatcher("org.opengauss.Driver", "jdbc:opengauss:")
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
