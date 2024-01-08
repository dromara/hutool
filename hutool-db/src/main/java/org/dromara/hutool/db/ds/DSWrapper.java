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

package org.dromara.hutool.db.ds;

import org.dromara.hutool.core.exception.CloneException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.lang.wrapper.SimpleWrapper;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * {@link DataSource} 数据源实现包装，通过包装，提供基本功能外的额外功能和参数持有，包括：
 *
 * <pre>
 * 1. 提供驱动名的持有，用于确定数据库方言
 * </pre>
 *
 * @author looly
 * @since 4.3.2
 */
public class DSWrapper extends SimpleWrapper<DataSource> implements DataSource, Closeable, Cloneable {

	private final String driver;

	/**
	 * 包装指定的DataSource
	 *
	 * @param ds     原始的DataSource
	 * @param driver 数据库驱动类名
	 * @return DataSourceWrapper
	 */
	public static DSWrapper wrap(final DataSource ds, final String driver) {
		return new DSWrapper(ds, driver);
	}

	/**
	 * 构造
	 *
	 * @param ds     原始的DataSource
	 * @param driver 数据库驱动类名
	 */
	public DSWrapper(final DataSource ds, final String driver) {
		super(ds);
		this.driver = driver;
	}

	/**
	 * 获取驱动名
	 *
	 * @return 驱动名
	 */
	public String getDriver() {
		return this.driver;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return getRaw().getLogWriter();
	}

	@Override
	public void setLogWriter(final PrintWriter out) throws SQLException {
		getRaw().setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(final int seconds) throws SQLException {
		getRaw().setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return getRaw().getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return getRaw().getParentLogger();
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return getRaw().unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return getRaw().isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return getRaw().getConnection();
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		return getRaw().getConnection(username, password);
	}

	@Override
	public void close() {
		final DataSource ds = getRaw();
		if (ds instanceof AutoCloseable) {
			IoUtil.closeQuietly((AutoCloseable) ds);
		}
	}

	@Override
	public DSWrapper clone() {
		try {
			return (DSWrapper) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new CloneException(e);
		}
	}
}
