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

import org.dromara.hutool.core.map.SafeConcurrentHashMap;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.impl.*;
import org.dromara.hutool.db.ds.DSWrapper;
import org.dromara.hutool.log.LogUtil;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 方言工厂类
 *
 * @author loolly
 */
public class DialectFactory implements DriverNamePool {

	private static final Map<DataSource, Dialect> DIALECT_POOL = new SafeConcurrentHashMap<>();

	private DialectFactory() {
	}

	/**
	 * 根据驱动名创建方言<br>
	 * 驱动名是不分区大小写完全匹配的
	 *
	 * @param dbConfig 数据库配置
	 * @return 方言
	 */
	public static Dialect newDialect(final DbConfig dbConfig) {
		final Dialect dialect = internalNewDialect(dbConfig);
		LogUtil.debug("Use Dialect: [{}].", dialect.getClass().getSimpleName());
		return dialect;
	}

	/**
	 * 根据驱动名创建方言<br>
	 * 驱动名是不分区大小写完全匹配的
	 *
	 * @param dbConfig 数据库配置
	 * @return 方言
	 */
	private static Dialect internalNewDialect(final DbConfig dbConfig) {
		final String driverName = dbConfig.getDriver();

		if (StrUtil.isNotBlank(driverName)) {
			if (DRIVER_MYSQL.equalsIgnoreCase(driverName) || DRIVER_MYSQL_V6.equalsIgnoreCase(driverName)) {
				return new MysqlDialect(dbConfig);
			} else if (DRIVER_ORACLE.equalsIgnoreCase(driverName) || DRIVER_ORACLE_OLD.equalsIgnoreCase(driverName)) {
				return new OracleDialect(dbConfig);
			} else if (DRIVER_SQLLITE3.equalsIgnoreCase(driverName)) {
				return new Sqlite3Dialect(dbConfig);
			} else if (DRIVER_POSTGRESQL.equalsIgnoreCase(driverName)) {
				return new PostgresqlDialect(dbConfig);
			} else if (DRIVER_H2.equalsIgnoreCase(driverName)) {
				return new H2Dialect(dbConfig);
			} else if (DRIVER_SQLSERVER.equalsIgnoreCase(driverName)) {
				return new SqlServer2012Dialect(dbConfig);
			} else if (DRIVER_PHOENIX.equalsIgnoreCase(driverName)) {
				return new PhoenixDialect(dbConfig);
			}
		}
		// 无法识别可支持的数据库类型默认使用ANSI方言，可兼容大部分SQL语句
		return new AnsiSqlDialect(dbConfig);
	}

	/**
	 * 获取共享方言
	 *
	 * @param ds 数据源，每一个数据源对应一个唯一方言
	 * @return {@link Dialect}方言
	 */
	public static Dialect getDialect(final DataSource ds) {
		Dialect dialect = DIALECT_POOL.get(ds);
		if (null == dialect) {
			// 数据源作为锁的意义在于：不同数据源不会导致阻塞，相同数据源获取方言时可保证互斥
			//noinspection SynchronizationOnLocalVariableOrMethodParameter
			synchronized (ds) {
				dialect = DIALECT_POOL.computeIfAbsent(ds, DialectFactory::newDialect);
			}
		}
		return dialect;
	}

	/**
	 * 创建方言
	 *
	 * @param ds 数据源
	 * @return 方言
	 */
	public static Dialect newDialect(final DataSource ds) {
		return newDialect(ds instanceof DSWrapper ? ((DSWrapper) ds).getDbConfig() : DbConfig.of());
	}
}
