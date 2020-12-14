package cn.hutool.db.ds;

import cn.hutool.core.io.IoUtil;

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
public class DataSourceWrapper implements DataSource, Closeable, Cloneable {

	private final DataSource ds;
	private final String driver;

	/**
	 * 包装指定的DataSource
	 *
	 * @param ds     原始的DataSource
	 * @param driver 数据库驱动类名
	 * @return {@link DataSourceWrapper}
	 */
	public static DataSourceWrapper wrap(DataSource ds, String driver) {
		return new DataSourceWrapper(ds, driver);
	}

	/**
	 * 构造
	 *
	 * @param ds     原始的DataSource
	 * @param driver 数据库驱动类名
	 */
	public DataSourceWrapper(DataSource ds, String driver) {
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
	public DataSource getRaw() {
		return this.ds;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return ds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		ds.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
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
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return ds.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return ds.getConnection(username, password);
	}

	@Override
	public void close() {
		if (this.ds instanceof AutoCloseable) {
			IoUtil.close((AutoCloseable) this.ds);
		}
	}

}