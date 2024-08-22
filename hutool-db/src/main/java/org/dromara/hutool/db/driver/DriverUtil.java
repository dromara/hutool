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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.reflect.ClassUtil;
import org.dromara.hutool.core.reflect.ConstructorUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.ds.DSWrapper;

import javax.sql.DataSource;
import java.sql.*;

/**
 * 驱动相关工具类，包括自动获取驱动类名
 *
 * @author looly
 * @since 4.0.10
 */
public class DriverUtil {

	/**
	 * 创建驱动
	 *
	 * @param driverName  驱动类名
	 * @return 驱动
	 * @since 6.0.0
	 */
	public static Driver createDriver(final String driverName) {
		return createDriver(driverName, null);
	}

	/**
	 * 创建驱动
	 *
	 * @param driverName  驱动类名
	 * @param classLoader 类加载器
	 * @return 驱动
	 * @since 6.0.0
	 */
	public static Driver createDriver(final String driverName, final ClassLoader classLoader) {
		final Class<?> driverClass = ClassUtil.forName(driverName, true, classLoader);
		return (Driver) ConstructorUtil.newInstance(driverClass);
	}

	/**
	 * 通过JDBC URL等信息识别JDBC驱动名
	 *
	 * @param nameContainsProductInfo 包含数据库标识的字符串
	 * @return 驱动
	 * @see DriverIdentifier#identifyDriver(String)
	 */
	public static String identifyDriver(final String nameContainsProductInfo) {
		return DriverIdentifier.INSTANCE.identifyDriver(nameContainsProductInfo);
	}

	/**
	 * 识别JDBC驱动名
	 *
	 * @param ds 数据源
	 * @return 驱动
	 */
	public static String identifyDriver(final DataSource ds) {
		if (ds instanceof DSWrapper) {
			final String driver = ((DSWrapper) ds).getDriver();
			if (StrUtil.isNotBlank(driver)) {
				return driver;
			}
		}

		Connection conn = null;
		String driver;
		try {
			try {
				conn = ds.getConnection();
			} catch (final SQLException e) {
				throw new DbException("Get Connection error !", e);
			} catch (final NullPointerException e) {
				throw new DbException("Unexpected NullPointException, maybe [jdbcUrl] or [url] is empty!", e);
			}
			driver = identifyDriver(conn);
		} finally {
			IoUtil.closeQuietly(conn);
		}

		return driver;
	}

	/**
	 * 识别JDBC驱动名
	 *
	 * @param conn 数据库连接对象
	 * @return 驱动
	 * @throws DbException SQL异常包装，获取元数据信息失败
	 */
	public static String identifyDriver(final Connection conn) throws DbException {
		String driver;
		final DatabaseMetaData meta;
		try {
			meta = conn.getMetaData();
			driver = identifyDriver(meta.getDatabaseProductName());
			if (StrUtil.isBlank(driver)) {
				driver = identifyDriver(meta.getDriverName());
			}
		} catch (final SQLException e) {
			throw new DbException("Identify driver error!", e);
		}

		return driver;
	}
}
