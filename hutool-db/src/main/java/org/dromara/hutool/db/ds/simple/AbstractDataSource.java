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
