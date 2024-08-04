/*
 * Copyright (c) 2024-2024. looly(loolly@aliyun.com)
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

/**
 * 常用数据库驱动名
 *
 * @author looly
 * @since 6.0.0
 */
public interface DriverNames {

	/**
	 * JDBC 驱动 MySQL
	 */
	String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	/**
	 * JDBC 驱动 MySQL，在6.X版本中变动驱动类名，且使用SPI机制
	 */
	String DRIVER_MYSQL_V6 = "com.mysql.cj.jdbc.Driver";
	/**
	 * JDBC 驱动 MariaDB
	 */
	String DRIVER_MARIADB = "org.mariadb.jdbc.Driver";
	/**
	 * JDBC 驱动 Oracle
	 */
	String DRIVER_ORACLE = "oracle.jdbc.OracleDriver";
	/**
	 * JDBC 驱动 Oracle，旧版使用
	 */
	String DRIVER_ORACLE_OLD = "oracle.jdbc.driver.OracleDriver";
	/**
	 * JDBC 驱动 PostgreSQL
	 */
	String DRIVER_POSTGRESQL = "org.postgresql.Driver";
	/**
	 * JDBC 驱动 SQLLite3
	 */
	String DRIVER_SQLLITE3 = "org.sqlite.JDBC";
	/**
	 * JDBC 驱动 SQLServer
	 */
	String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/**
	 * JDBC 驱动 SQLServer，4.0前使用
	 */
	String DRIVER_SQLSERVER_OLD = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	/**
	 * JDBC 驱动 Hive
	 */
	String DRIVER_HIVE = "org.apache.hadoop.hive.jdbc.HiveDriver";
	/**
	 * JDBC 驱动 Hive2
	 */
	String DRIVER_HIVE2 = "org.apache.hive.jdbc.HiveDriver";
	/**
	 * JDBC 驱动 H2
	 */
	String DRIVER_H2 = "org.h2.Driver";
	/**
	 * JDBC 驱动 Derby
	 */
	String DRIVER_DERBY = "org.apache.derby.jdbc.AutoloadedDriver";
	/**
	 * JDBC 驱动 Derby，嵌入式驱动程序
	 */
	String DRIVER_DERBY_EMBEDDED = "org.apache.derby.jdbc.EmbeddedDriver";
	/**
	 * JDBC 驱动 HSQLDB
	 */
	String DRIVER_HSQLDB = "org.hsqldb.jdbc.JDBCDriver";
	/**
	 * JDBC 驱动 达梦
	 */
	String DRIVER_DM = "dm.jdbc.driver.DmDriver";
	/**
	 * JDBC 驱动 人大金仓
	 */
	String DRIVER_KINGBASE = "com.kingbase.Driver";
	/**
	 * JDBC 驱动 人大金仓8
	 */
	String DRIVER_KINGBASE8 = "com.kingbase8.Driver";
	/**
	 * JDBC 驱动 Ignite thin
	 */
	String DRIVER_IGNITE_THIN = "org.apache.ignite.IgniteJdbcThinDriver";
	/**
	 * JDBC 驱动 ClickHouse
	 */
	String DRIVER_CLICK_HOUSE = "com.clickhouse.jdbc.ClickHouseDriver";
	/**
	 * JDBC 驱动 瀚高数据库
	 */
	String DRIVER_HIGHGO = "com.highgo.jdbc.Driver";
	/**
	 * JDBC 驱动 DB2
	 */
	String DRIVER_DB2 = "com.ibm.db2.jdbc.app.DB2Driver";
	/**
	 * JDBC 驱动 虚谷数据库
	 */
	String DRIVER_XUGU = "com.xugu.cloudjdbc.Driver";
	/**
	 * JDBC 驱动 Apache Phoenix 瘦客户端
	 */
	String DRIVER_PHOENIX_THIN = "org.apache.phoenix.queryserver.client.Driver";
	/**
	 * JDBC 驱动 Apache Phoenix
	 */
	String DRIVER_PHOENIX = "org.apache.phoenix.jdbc.PhoenixDriver";
	/**
	 * JDBC 驱动 南大通用
	 */
	String DRIVER_GBASE = "com.gbase.jdbc.Driver";
	/**
	 * JDBC 驱动 神州数据库
	 */
	String DRIVER_OSCAR = "com.oscar.Driver";
	/**
	 * JDBC 驱动 Sybase
	 */
	String DRIVER_SYBASE = "com.sybase.jdbc4.jdbc.SybDriver";
	/**
	 * JDBC 驱动 华为高斯
	 */
	String DRIVER_GAUSS = "com.huawei.gauss.jdbc.ZenithDriver";
	/**
	 * JDBC 驱动 OpenGauss
	 */
	String DRIVER_OPENGAUSS = "org.opengauss.Driver";
	/**
	 * JDBC 驱动 log4jdbc
	 */
	String DRIVER_LOG4J = "net.sf.log4jdbc.DriverSpy";
	/**
	 * JDBC 驱动 Tidb
	 */
	String DRIVER_TIDB = "io.tidb.bigdata.jdbc.TiDBDriver";
	/**
	 * JDBC 驱动 OceanBase
	 */
	String DRIVER_OCEANBASE = "com.oceanbase.jdbc.Driver";
	/**
	 * JDBC 驱动 JTDS
	 */
	String DRIVER_JTDS = "net.sourceforge.jtds.jdbc.Driver";
	/**
	 * JDBC 驱动 Druid
	 */
	String DRIVER_DRUID = "com.alibaba.druid.mock.MockDriver";
	/**
	 * JDBC 驱动 edb
	 */
	String DRIVER_EDB = "com.edb.Driver";
	/**
	 * JDBC 驱动 odps
	 */
	String DRIVER_ODPS = "com.aliyun.odps.jdbc.OdpsDriver";
	/**
	 * JDBC 驱动 Ingres
	 */
	String DRIVER_INGRES = "com.ingres.jdbc.IngresDriver";
	/**
	 * JDBC 驱动 Mckoi
	 */
	String DRIVER_MCKOI = "com.mckoi.JDBCDriver";
	/**
	 * JDBC 驱动 cloudscape
	 */
	String DRIVER_CLOUDSCAPE = "COM.cloudscape.core.JDBCDriver";
	/**
	 * JDBC 驱动 informix
	 */
	String DRIVER_INFORMIX = "com.informix.jdbc.IfxDriver";
	/**
	 * JDBC 驱动 timesten
	 */
	String DRIVER_TIMESTEN = "com.timesten.jdbc.TimesTenDriver";
	/**
	 * JDBC 驱动 as400
	 */
	String DRIVER_AS400 = "com.ibm.as400.access.AS400JDBCDriver";
	/**
	 * JDBC 驱动 attunity
	 */
	String DRIVER_ATTUNITY = "com.attunity.jdbc.NvDriver";
	/**
	 * JDBC 驱动 JSQL
	 */
	String DRIVER_JSQL = "com.jnetdirect.jsql.JSQLDriver";
	/**
	 * JDBC 驱动 jturbo
	 */
	String DRIVER_JTURBO = "com.newatlanta.jturbo.driver.Driver";
	/**
	 * JDBC 驱动 interbase
	 */
	String DRIVER_INTERBASE = "interbase.interclient.Driver";
	/**
	 * JDBC 驱动 pointbase
	 */
	String DRIVER_POINTBASE = "com.pointbase.jdbc.jdbcUniversalDriver";
	/**
	 * JDBC 驱动 edbc
	 */
	String DRIVER_EDBC = "ca.edbc.jdbc.EdbcDriver";
	/**
	 * JDBC 驱动 mimer
	 */
	String DRIVER_MIMER = "com.mimer.jdbc.Driver";
	/**
	 * JDBC 驱动 Apache Kylin
	 */
	String DRIVER_KYLIN = "org.apache.kylin.jdbc.Driver";
	/**
	 * JDBC 驱动 elastic
	 */
	String DRIVER_ELASTIC = "com.alibaba.xdriver.elastic.jdbc.ElasticDriver";
	/**
	 * JDBC 驱动 Presto
	 */
	String DRIVER_PRESTO = "com.facebook.presto.jdbc.PrestoDriver";
	/**
	 * JDBC 驱动 Trino
	 */
	String DRIVER_TRINO = "io.trino.jdbc.TrinoDriver";
	/**
	 * JDBC 驱动 浪潮K-DB
	 */
	String DRIVER_INSPUR = "com.inspur.jdbc.KdDriver";
	/**
	 * JDBC 驱动 polardb
	 */
	String DRIVER_POLARDB = "com.aliyun.polardb.Driver";
	/**
	 * JDBC 驱动 Greenplum
	 */
	String DRIVER_GREENPLUM = "com.pivotal.jdbc.GreenplumDriver";

}
