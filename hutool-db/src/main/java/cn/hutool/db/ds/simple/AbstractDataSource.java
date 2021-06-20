package cn.hutool.db.ds.simple;

import java.io.Closeable;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * 数据源抽象实现
 * @author Looly
 *
 */
public abstract class AbstractDataSource implements DataSource, Cloneable, Closeable{
	@Override
	public PrintWriter getLogWriter() {
		return DriverManager.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) {
		DriverManager.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) {
		DriverManager.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() {
		return DriverManager.getLoginTimeout();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new SQLException("Can't support unwrap method!");
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new SQLException("Can't support isWrapperFor method!");
	}

	/**
	 * Support from JDK7
	 * @since 1.7
	 */
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new SQLFeatureNotSupportedException("DataSource can't support getParentLogger method!");
	}
}
