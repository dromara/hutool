package com.xiaoleilu.hutool.db.dialect;

import java.sql.Connection;

import javax.sql.DataSource;

import com.xiaoleilu.hutool.db.DbUtil;
import com.xiaoleilu.hutool.db.dialect.impl.AnsiSqlDialect;
import com.xiaoleilu.hutool.db.dialect.impl.MysqlDialect;
import com.xiaoleilu.hutool.db.dialect.impl.OracleDialect;
import com.xiaoleilu.hutool.db.dialect.impl.PostgresqlDialect;
import com.xiaoleilu.hutool.db.dialect.impl.Sqlite3Dialect;
import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 方言工厂类
 * @author loolly
 *
 */
public class DialectFactory {
	
	/** JDBC 驱动 MySQL */
	public final static String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
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
	
	private DialectFactory() {
	}
	
	/**
	 * 创建方言
	 * @param driverName JDBC驱动类名
	 * @return 方言
	 */
	public static Dialect newDialect(String driverName) {
		if(StrUtil.isNotBlank(driverName)) {
			if(DRIVER_MYSQL.equalsIgnoreCase(driverName)) {
				return new MysqlDialect();
			}else if(DRIVER_ORACLE.equalsIgnoreCase(driverName)) {
				return new OracleDialect();
			}else if(DRIVER_SQLLITE3.equalsIgnoreCase(driverName)) {
				return new Sqlite3Dialect();
			}else if(DRIVER_POSTGRESQL.equalsIgnoreCase(driverName)) {
				return new PostgresqlDialect();
			}
		}
		
		//无法识别可支持的数据库类型默认使用ANSI方言，可兼容大部分SQL语句
		return new AnsiSqlDialect();
	}
	
	/**
	 * 创建方言
	 * @param ds 数据源
	 * @return 方言
	 */
	public static Dialect newDialect(DataSource ds) {
		return newDialect(DbUtil.identifyDriver(ds));
	}
	
	/**
	 * 创建方言
	 * @param conn 数据库连接对象
	 * @return 方言
	 */
	public static Dialect newDialect(Connection conn) {
		return newDialect(DbUtil.identifyDriver(conn));
	}
	
}
