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

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.db.DbException;
import org.dromara.hutool.db.ds.DSWrapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * 驱动相关工具类，包括自动获取驱动类名
 *
 * @author looly
 * @since 4.0.10
 */
public class DriverUtil {
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
