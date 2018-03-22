package com.xiaoleilu.hutool.db.dialect;

import java.sql.Connection;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.dialect.impl.AnsiSqlDialect;
import com.xiaoleilu.hutool.db.dialect.impl.H2Dialect;
import com.xiaoleilu.hutool.db.dialect.impl.MysqlDialect;
import com.xiaoleilu.hutool.db.dialect.impl.OracleDialect;
import com.xiaoleilu.hutool.db.dialect.impl.PostgresqlDialect;
import com.xiaoleilu.hutool.db.dialect.impl.SqlServer2012Dialect;
import com.xiaoleilu.hutool.db.dialect.impl.Sqlite3Dialect;
import com.xiaoleilu.hutool.util.ClassLoaderUtil;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 方言工厂类
 * 
 * @author loolly
 *
 */
public class DialectFactory {

	/** JDBC 驱动 MySQL */
	public final static String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	/** JDBC 驱动 MySQL，在6.X版本中变动驱动类名，且使用SPI机制 */
	public final static String DRIVER_MYSQL_V6 = "com.mysql.cj.jdbc.Driver";
	/** JDBC 驱动 Oracle */
	public final static String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	/** JDBC 驱动 PostgreSQL */
	public final static String DRIVER_POSTGRESQL = "org.postgresql.Driver";
	/** JDBC 驱动 SQLLite3 */
	public final static String DRIVER_SQLLITE3 = "org.sqlite.JDBC";
	/** JDBC 驱动 SQLServer */
	public final static String DRIVER_SQLSERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	/** JDBC 驱动 Hive */
	public final static String DRIVER_HIVE = "org.apache.hadoop.hive.jdbc.HiveDriver";
	/** JDBC 驱动 Hive2 */
	public final static String DRIVER_HIVE2 = "org.apache.hive.jdbc.HiveDriver";
	/** JDBC 驱动 H2 */
	public final static String DRIVER_H2 = "org.h2.Driver";
	/** JDBC 驱动 Derby */
	public final static String DRIVER_DERBY = "org.apache.derby.jdbc.ClientDriver";
	/** JDBC 驱动 Derby嵌入式 */
	public final static String DRIVER_DERBY_EMBEDDED = "org.apache.derby.jdbc.EmbeddedDriver";

	private DialectFactory() {
	}

	/**
	 * 创建方言
	 * 
	 * @param driverName JDBC驱动类名
	 * @return 方言
	 */
	public static Dialect newDialect(String driverName) {
		if (StrUtil.isNotBlank(driverName)) {
			if (DRIVER_MYSQL.equalsIgnoreCase(driverName) || DRIVER_MYSQL_V6.equalsIgnoreCase(driverName)) {
				return new MysqlDialect();
			} else if (DRIVER_ORACLE.equalsIgnoreCase(driverName)) {
				return new OracleDialect();
			} else if (DRIVER_SQLLITE3.equalsIgnoreCase(driverName)) {
				return new Sqlite3Dialect();
			} else if (DRIVER_POSTGRESQL.equalsIgnoreCase(driverName)) {
				return new PostgresqlDialect();
			} else if (DRIVER_H2.equalsIgnoreCase(driverName)) {
				return new H2Dialect();
			} else if (DRIVER_SQLSERVER.equalsIgnoreCase(driverName)) {
				return new SqlServer2012Dialect();
			}
		}

		// 无法识别可支持的数据库类型默认使用ANSI方言，可兼容大部分SQL语句
		return new AnsiSqlDialect();
	}

	/**
	 * 通过JDBC URL等信息识别JDBC驱动名
	 * 
	 * @param nameContainsProductInfo 包含数据库标识的字符串
	 * @return 驱动
	 */
	public static String identifyDriver(String nameContainsProductInfo) {
		if (StrUtil.isBlank(nameContainsProductInfo)) {
			return null;
		}
		// 全部转为小写，忽略大小写
		nameContainsProductInfo = nameContainsProductInfo.toLowerCase();

		String driver = null;
		if (nameContainsProductInfo.contains("mysql")) {
			driver = ClassLoaderUtil.isPresent(DRIVER_MYSQL_V6) ? DRIVER_MYSQL_V6 : DRIVER_MYSQL;
		} else if (nameContainsProductInfo.contains("oracle")) {
			driver = DRIVER_ORACLE;
		} else if (nameContainsProductInfo.contains("postgresql")) {
			driver = DRIVER_POSTGRESQL;
		} else if (nameContainsProductInfo.contains("sqlite")) {
			driver = DRIVER_SQLLITE3;
		} else if (nameContainsProductInfo.contains("sqlserver")) {
			driver = DRIVER_SQLSERVER;
		} else if (nameContainsProductInfo.contains("hive")) {
			driver = DRIVER_HIVE;
		} else if (nameContainsProductInfo.contains("h2")) {
			driver = DRIVER_H2;
		} else if (nameContainsProductInfo.startsWith("jdbc:derby://")) {
			// Derby数据库网络连接方式
			driver = DRIVER_DERBY;
		} else if (nameContainsProductInfo.contains("derby")) {
			// 嵌入式Derby数据库
			driver = DRIVER_DERBY_EMBEDDED;
		}

		return driver;
	}

	/**
	 * 创建方言
	 * 
	 * @param ds 数据源
	 * @return 方言
	 */
	public static Dialect newDialect(DataSource ds) {
		return newDialect(DbUtil.identifyDriver(ds));
	}

	/**
	 * 创建方言
	 * 
	 * @param conn 数据库连接对象
	 * @return 方言
	 */
	public static Dialect newDialect(Connection conn) {
		return newDialect(DbUtil.identifyDriver(conn));
	}

}
