package cn.hutool.db;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程相关的数据库连接持有器<br>
 * 此对象为单例类，用于存储线程相关的Connection对象。<br>
 * 在多数据源情况下，由于数据源的不同，连接对象也不同，因此获取连接时需要DataSource关联获取
 * 
 * @author looly
 *
 */
public enum ThreadLocalConnection {
	INSTANCE;

	private final ThreadLocal<GroupedConnection> threadLocal = new ThreadLocal<>();

	/**
	 * 获取数据源对应的数据库连接
	 * 
	 * @param ds 数据源
	 * @return Connection
	 * @throws SQLException SQL异常
	 */
	public Connection get(DataSource ds) throws SQLException {
		GroupedConnection groupedConnection = threadLocal.get();
		if (null == groupedConnection) {
			groupedConnection = new GroupedConnection();
			threadLocal.set(groupedConnection);
		}
		return groupedConnection.get(ds);
	}

	/**
	 * 关闭数据库，并从线程池中移除
	 * 
	 * @param ds 数据源
	 * @since 4.1.7
	 */
	public void close(DataSource ds) {
		GroupedConnection groupedConnection = threadLocal.get();
		if (null != groupedConnection) {
			groupedConnection.close(ds);
			if (groupedConnection.isEmpty()) {
				// 当所有分组都没有持有的连接时，移除这个分组连接
				threadLocal.remove();
			}
		}
	}

	/**
	 * 分组连接，根据不同的分组获取对应的连接，用于多数据源情况
	 * 
	 * @author Looly
	 */
	public static class GroupedConnection {

		/** 连接的Map，考虑到大部分情况是单数据库，故此处初始大小1 */
		private final Map<DataSource, Connection> connMap = new HashMap<>(1, 1);

		/**
		 * 获取连接，如果获取的连接为空或者已被关闭，重新创建连接
		 * 
		 * @param ds 数据源
		 * @return Connection
		 * @throws SQLException SQL异常
		 */
		public Connection get(DataSource ds) throws SQLException {
			Connection conn = connMap.get(ds);
			if (null == conn || conn.isClosed()) {
				conn = ds.getConnection();
				connMap.put(ds, conn);
			}
			return conn;
		}

		/**
		 * 关闭并移除Connection<br>
		 * 如果处于事务中，则不进行任何操作
		 * 
		 * @param ds 数据源
		 * @return this
		 */
		public GroupedConnection close(DataSource ds) {
			final Connection conn = connMap.get(ds);
			if (null != conn) {
				try {
					if (false == conn.getAutoCommit()) {
						// 非自动提交事务的连接，不做关闭（可能处于事务中）
						return this;
					}
				} catch (SQLException e) {
					// ignore
				}
				connMap.remove(ds);
				DbUtil.close(conn);
			}
			return this;
		}

		/**
		 * 持有的连接是否为空
		 * 
		 * @return 持有的连接是否为空
		 * @since 4.6.4
		 */
		public boolean isEmpty() {
			return connMap.isEmpty();
		}
	}
}
