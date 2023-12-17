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

package org.dromara.hutool.db.ds.simple;

import org.dromara.hutool.core.exception.CloneException;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 数据源抽象实现
 *
 * @author Looly
 */
public abstract class AbstractDataSource implements DataSource, Cloneable, Closeable {

	@Override
	public PrintWriter getLogWriter() {
		return DriverManager.getLogWriter();
	}

	@Override
	public void setLogWriter(final PrintWriter out) {
		DriverManager.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(final int seconds) {
		DriverManager.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() {
		return DriverManager.getLoginTimeout();
	}

	@Override
	public <T> T unwrap(final Class<T> iface) throws SQLException {
		throw new SQLException("Can't support unwrap method!");
	}

	@Override
	public boolean isWrapperFor(final Class<?> iface) throws SQLException {
		throw new SQLException("Can't support isWrapperFor method!");
	}

	/**
	 * Support from JDK7
	 *
	 * @since 1.7
	 */
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("DataSource can't support getParentLogger method!");
	}

	@Override
	public AbstractDataSource clone() {
		try {
			return (AbstractDataSource) super.clone();
		} catch (final CloneNotSupportedException e) {
			throw new CloneException(e);
		}
	}
}
