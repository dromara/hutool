/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.db.ds;

import org.dromara.hutool.core.exceptions.CloneException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.func.Wrapper;

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
public class DSWrapper implements Wrapper<DataSource>, DataSource, Closeable, Cloneable {

	private final DataSource ds;
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
		this.ds = ds;
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

	/**
	 * 获取原始的数据源
	 *
	 * @return 原始数据源
	 */
	@Override
	public DataSource getRaw() {
		return this.ds;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return ds.getLogWriter();
	}

	@Override
	public void setLogWriter(final PrintWriter out) throws SQLException {
		ds.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(final int seconds) throws SQLException {
		ds.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return ds.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return ds.getParentLogger();
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		return ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		return ds.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		return ds.getConnection(username, password);
	}

	@Override
	public void close() {
		if (this.ds instanceof AutoCloseable) {
			IoUtil.closeQuietly((AutoCloseable) this.ds);
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
