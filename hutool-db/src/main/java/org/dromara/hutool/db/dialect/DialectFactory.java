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

package org.dromara.hutool.db.dialect;

import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.config.DbConfig;
import org.dromara.hutool.db.dialect.impl.*;
import org.dromara.hutool.db.driver.DriverNames;
import org.dromara.hutool.db.ds.DSWrapper;
import org.dromara.hutool.log.LogUtil;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 方言工厂类
 *
 * @author loolly
 */
public class DialectFactory {

	private static final Map<DataSource, Dialect> DIALECT_POOL = new SafeConcurrentHashMap<>();

	private DialectFactory() {
	}

	/**
	 * 创建方言，如果配置中用户指定了方言，则直接返回用户指定的方言，否则根据驱动名等信息自动创建方言<br>
	 * 驱动名是不分区大小写完全匹配的
	 *
	 * @param dbConfig 数据库配置
	 * @return 方言
	 */
	public static Dialect newDialect(final DbConfig dbConfig) {
		Dialect dialect = dbConfig.getDialect();
		if(null == dialect){
			dialect = internalNewDialect(dbConfig);
		}

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
			if (DriverNames.DRIVER_MYSQL.equalsIgnoreCase(driverName) || DriverNames.DRIVER_MYSQL_V6.equalsIgnoreCase(driverName)) {
				return new MysqlDialect(dbConfig);
			} else if (DriverNames.DRIVER_ORACLE.equalsIgnoreCase(driverName) || DriverNames.DRIVER_ORACLE_OLD.equalsIgnoreCase(driverName)) {
				return new OracleDialect(dbConfig);
			} else if (DriverNames.DRIVER_SQLLITE3.equalsIgnoreCase(driverName)) {
				return new Sqlite3Dialect(dbConfig);
			} else if (DriverNames.DRIVER_POSTGRESQL.equalsIgnoreCase(driverName)) {
				return new PostgresqlDialect(dbConfig);
			} else if (DriverNames.DRIVER_H2.equalsIgnoreCase(driverName)) {
				return new H2Dialect(dbConfig);
			} else if (DriverNames.DRIVER_SQLSERVER.equalsIgnoreCase(driverName)) {
				return new SqlServer2012Dialect(dbConfig);
			} else if (DriverNames.DRIVER_SQLSERVER_OLD.equalsIgnoreCase(driverName)) {
				// TODO 无法简单通过jdbc url 判断SqlServer版本
				return new SqlServer2005Dialect(dbConfig);
			} else if (DriverNames.DRIVER_PHOENIX.equalsIgnoreCase(driverName)) {
				return new PhoenixDialect(dbConfig);
			} else if (DriverNames.DRIVER_DM.equalsIgnoreCase(driverName)) {
				return new DmDialect(dbConfig);
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
