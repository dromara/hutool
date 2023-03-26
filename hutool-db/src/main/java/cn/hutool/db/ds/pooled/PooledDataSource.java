package cn.hutool.db.ds.pooled;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.StrUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.db.DbRuntimeException;
import cn.hutool.db.ds.simple.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 池化数据源
 *
 * @author Looly
 *
 */
public class PooledDataSource extends AbstractDataSource {

	private Queue<PooledConnection> freePool;
	private int activeCount; // 活跃连接数

	private final DbConfig config;

	/**
	 * 获得一个数据源
	 *
	 * @param group 数据源分组
	 * @return {@code PooledDataSource}
	 */
	synchronized public static PooledDataSource getDataSource(final String group) {
		return new PooledDataSource(group);
	}

	/**
	 * 获得一个数据源，使用空分组
	 *
	 * @return {@code PooledDataSource}
	 */
	synchronized public static PooledDataSource getDataSource() {
		return new PooledDataSource();
	}

	// -------------------------------------------------------------------- Constructor start
	/**
	 * 构造，读取默认的配置文件和默认分组
	 */
	public PooledDataSource() {
		this(StrUtil.EMPTY);
	}

	/**
	 * 构造，读取默认的配置文件
	 *
	 * @param group 分组
	 */
	public PooledDataSource(final String group) {
		this(new DbSetting(), group);
	}

	/**
	 * 构造
	 *
	 * @param setting 数据库配置文件对象
	 * @param group 分组
	 */
	public PooledDataSource(final DbSetting setting, final String group) {
		this(setting.getDbConfig(group));
	}

	/**
	 * 构造
	 *
	 * @param config 数据库配置
	 */
	public PooledDataSource(final DbConfig config) {
		this.config = config;
		freePool = new LinkedList<>();
		int initialSize = config.getInitialSize();
		try {
			while (initialSize-- > 0) {
				freePool.offer(newConnection());
			}
		} catch (final SQLException e) {
			throw new DbRuntimeException(e);
		}
	}
	// -------------------------------------------------------------------- Constructor start

	/**
	 * 从数据库连接池中获取数据库连接对象
	 */
	@Override
	public synchronized Connection getConnection() throws SQLException {
		return getConnection(config.getMaxWait());
	}

	@Override
	public Connection getConnection(final String username, final String password) throws SQLException {
		throw new SQLException("Pooled DataSource is not allow to get special Connection!");
	}

	/**
	 * 释放连接，连接会被返回给连接池
	 *
	 * @param conn 连接
	 * @return 释放成功与否
	 */
	protected synchronized boolean free(final PooledConnection conn) {
		activeCount--;
		return freePool.offer(conn);
	}

	/**
	 * 创建新连接
	 *
	 * @return 新连接
	 * @throws SQLException SQL异常
	 */
	public PooledConnection newConnection() throws SQLException {
		return new PooledConnection(this);
	}

	public DbConfig getConfig() {
		return config;
	}

	/**
	 * 获取连接对象
	 *
	 * @param wait 当池中无连接等待的毫秒数
	 * @return 连接对象
	 * @throws SQLException SQL异常
	 */
	public PooledConnection getConnection(final long wait) throws SQLException {
		try {
			return getConnectionDirect();
		} catch (final Exception e) {
			ThreadUtil.sleep(wait);
		}
		return getConnectionDirect();
	}

	@Override
	synchronized public void close() {
		if (CollUtil.isNotEmpty(this.freePool)) {
			this.freePool.forEach(PooledConnection::release);
			this.freePool.clear();
			this.freePool = null;
		}
	}

	@Override
	protected void finalize() {
		IoUtil.closeQuietly(this);
	}

	/**
	 * 直接从连接池中获取连接，如果池中无连接直接抛出异常
	 *
	 * @return PooledConnection
	 * @throws SQLException SQL异常
	 */
	private PooledConnection getConnectionDirect() throws SQLException {
		if (null == freePool) {
			throw new SQLException("PooledDataSource is closed!");
		}

		final int maxActive = config.getMaxActive();
		if (maxActive <= 0 || maxActive < this.activeCount) {
			// 超过最大使用限制
			throw new SQLException("In used Connection is more than Max Active.");
		}

		PooledConnection conn = freePool.poll();
		if (null == conn || conn.open().isClosed()) {
			conn = this.newConnection();
		}
		activeCount++;
		return conn;
	}

}
